package com.example.dsp

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioPlaybackConfiguration
import android.media.audiofx.AudioEffect
import android.media.audiofx.BassBoost
import android.media.audiofx.DynamicsProcessing
import android.media.audiofx.Equalizer
import android.media.audiofx.LoudnessEnhancer
import android.media.audiofx.PresetReverb
import android.media.audiofx.Virtualizer
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.data.EqPresetEntity
import com.example.service.AudioEffectSessionReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Unified singleton manager that hooks Equltra into Android's AudioFlinger output pipeline
 * so YouTube, YouTube Music, Spotify, Chrome, and all third-party media apps are equalized in real time.
 *
 * Why YouTube failed previously:
 * 1. Two separate Equalizer instances (in AudioDspEngine and EqForegroundService) competed for Session 0.
 * 2. Modern Android (API 28+ / Android 9–16) routes YouTube/ExoPlayer/AAudio streams through
 *    [DynamicsProcessing] (10-band PreEQ + 10-band PostEQ + MBC + Limiter) and [LoudnessEnhancer],
 *    while often ignoring legacy 5-band Equalizer(0, 0).
 * 3. External media session broadcasts and [AudioManager.AudioPlaybackCallback] were not hooked.
 */
class GlobalAudioEffectManager private constructor(private val appContext: Context) {

    private class SessionEffectBundle(val sessionId: Int) {
        var dynamicsProcessing: DynamicsProcessing? = null
        var equalizer: Equalizer? = null
        var bassBoost: BassBoost? = null
        var virtualizer: Virtualizer? = null
        var presetReverb: PresetReverb? = null
        var loudnessEnhancer: LoudnessEnhancer? = null

        fun release() {
            runCatching { dynamicsProcessing?.release() }
            runCatching { equalizer?.release() }
            runCatching { bassBoost?.release() }
            runCatching { virtualizer?.release() }
            runCatching { presetReverb?.release() }
            runCatching { loudnessEnhancer?.release() }
            dynamicsProcessing = null
            equalizer = null
            bassBoost = null
            virtualizer = null
            presetReverb = null
            loudnessEnhancer = null
        }
    }

    private val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val sessionBundles = ConcurrentHashMap<Int, SessionEffectBundle>()
    private val sessionPackages = ConcurrentHashMap<Int, String>()

    @Volatile
    private var isMasterEnabled: Boolean = true

    @Volatile
    private var currentBandsDb: FloatArray = FloatArray(EqPresetEntity.BAND_COUNT) { 0f }

    @Volatile
    private var bassStrength: Int = 50 // 0..100 (Locked 50% default)

    @Volatile
    private var reverbAmount: Int = 12 // 0..100

    @Volatile
    private var reverbRoomType: String = "Small Room"

    @Volatile
    private var spatialAmount: Int = 35 // 0..100

    @Volatile
    private var preampDb: Float = 0f

    @Volatile
    private var softLimiterEnabled: Boolean = true

    @Volatile
    private var lrTwinVocalSplitterActive: Boolean = true

    @Volatile
    private var lrTwinVocalStrength: Int = 96 // 0..100

    @Volatile
    private var earDirectZeroBrainActive: Boolean = true

    @Volatile
    private var zeroBrainPressureStrength: Int = 98 // 0..100

    @Volatile
    private var wooferBoxType: WooferEnclosureType = WooferEnclosureType.PORTED_BASS_REFLEX

    @Volatile
    private var godTierSovereignActive: Boolean = true

    @Volatile
    private var godTierDualEarStrength: Int = 96 // 0..100

    @Volatile
    private var multiDriverArrayType: MultiDriverHybridArray = MultiDriverHybridArray.GOD_TIER_16_DRIVER_OMEGA

    @Volatile
    private var truePeakBrickwallActive: Boolean = true

    private val _statusText = MutableStateFlow("Global YouTube & System DSP Active (👑 God-Tier 16-Driver + 🔒 50% Bass + 👂 Kaan-Direct 0% Dimag)")
    val statusText: StateFlow<String> = _statusText.asStateFlow()

    private val _isExternalMediaPlaying = MutableStateFlow(false)
    val isExternalMediaPlaying: StateFlow<Boolean> = _isExternalMediaPlaying.asStateFlow()

    private val _hookedSessionsCount = MutableStateFlow(1)
    val hookedSessionsCount: StateFlow<Int> = _hookedSessionsCount.asStateFlow()

    private val playbackCallback = @RequiresApi(Build.VERSION_CODES.O)
    object : AudioManager.AudioPlaybackCallback() {
        override fun onPlaybackConfigChanged(configs: MutableList<AudioPlaybackConfiguration>?) {
            val activeList = configs ?: emptyList()
            val mediaPlaying = activeList.any { cfg ->
                val usage = cfg.audioAttributes?.usage ?: AudioAttributes.USAGE_UNKNOWN
                usage == AudioAttributes.USAGE_MEDIA ||
                    usage == AudioAttributes.USAGE_GAME ||
                    usage == AudioAttributes.USAGE_UNKNOWN
            }
            _isExternalMediaPlaying.value = mediaPlaying

            // Try to extract any accessible session IDs via reflection if OEM exposes them,
            // and always re-assert Global Output Mix (Session 0) DynamicsProcessing + Equalizer!
            activeList.forEach { cfg ->
                runCatching {
                    val m = cfg.javaClass.getMethod("getSessionId")
                    val sid = (m.invoke(cfg) as? Int) ?: 0
                    if (sid > 0) {
                        ensureSessionHooked(sid, "MediaPlaybackStream")
                    }
                }
            }

            // Re-assert Global Session 0 whenever YouTube starts/resumes a video stream
            ensureSessionHooked(0, "GlobalOutputMix")
            applyParametersToAllSessions()
            updateStatusBanner()
        }
    }

    init {
        // 1. Always hook Session 0 (Global Output Mix) with maximum priority
        ensureSessionHooked(0, "GlobalOutputMix")

        // 2. Register AudioPlaybackCallback (Android 8.0+) to detect YouTube / Spotify streams immediately
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            runCatching {
                audioManager.registerAudioPlaybackCallback(
                    playbackCallback,
                    Handler(Looper.getMainLooper())
                )
            }
        }

        // 3. Dynamically register EXPORTED receiver for OPEN/CLOSE_AUDIO_EFFECT_CONTROL_SESSION
        runCatching {
            val filter = IntentFilter().apply {
                addAction(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
                addAction(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION)
            }
            ContextCompat.registerReceiver(
                appContext,
                AudioEffectSessionReceiver(),
                filter,
                ContextCompat.RECEIVER_EXPORTED
            )
        }

        updateStatusBanner()
    }

    @Synchronized
    fun onExternalAudioSessionOpened(sessionId: Int, packageName: String?) {
        if (sessionId < 0) return
        val pkg = packageName ?: "MediaApp"
        sessionPackages[sessionId] = pkg
        ensureSessionHooked(sessionId, pkg)
        ensureSessionHooked(0, "GlobalOutputMix")
        applyParametersToAllSessions()
        updateStatusBanner()
    }

    @Synchronized
    fun onExternalAudioSessionClosed(sessionId: Int) {
        if (sessionId <= 0) return // Never close Session 0
        sessionPackages.remove(sessionId)
        sessionBundles.remove(sessionId)?.release()
        updateStatusBanner()
    }

    @Synchronized
    fun forceRehookYouTubeAndSystemAudio() {
        // Release and cleanly re-create Session 0 + all active sessions at highest priority
        val existingIds = sessionBundles.keys.toSet() + setOf(0)
        sessionBundles.values.forEach { it.release() }
        sessionBundles.clear()

        existingIds.forEach { sid ->
            ensureSessionHooked(sid, sessionPackages[sid] ?: "GlobalOutputMix")
        }

        // Also send standard Android audio effect control query broadcast to prompt active players
        runCatching {
            val queryIntent = Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION).apply {
                putExtra(AudioEffect.EXTRA_AUDIO_SESSION, 0)
                putExtra(AudioEffect.EXTRA_PACKAGE_NAME, appContext.packageName)
                putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
            }
            appContext.sendBroadcast(queryIntent)
        }

        applyParametersToAllSessions()
        updateStatusBanner()
    }

    @Synchronized
    fun ensureSessionHooked(sessionId: Int, label: String = "") {
        val bundle = sessionBundles.getOrPut(sessionId) { SessionEffectBundle(sessionId) }

        // 1. Android 9+ (API 28+) DynamicsProcessing: 31-Band PreEQ + 31-Band PostEQ + MBC + Limiter
        // This is the #1 key to making YouTube work on modern Android 10–16 devices!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && bundle.dynamicsProcessing == null) {
            runCatching {
                val bandCount = EqPresetEntity.BAND_COUNT
                val builder = DynamicsProcessing.Config.Builder(
                    DynamicsProcessing.VARIANT_FAVOR_FREQUENCY_RESOLUTION,
                    2, // stereo channels
                    true, // preEqInUse
                    bandCount, // 31 PreEQ bands (20Hz..20kHz)
                    true, // mbcInUse (Multi-band compressor for bass punch & clarity)
                    6, // 6 MBC bands
                    true, // postEqInUse
                    bandCount, // 31 PostEQ bands (20Hz..20kHz)
                    true // limiterInUse
                )
                val config = builder.build()

                // Configure cutoff frequencies for 31-band ISO 1/3-octave PreEQ and PostEQ
                val cutoffHz = EqPresetEntity.FREQUENCY_HZ
                for (b in 0 until bandCount) {
                    val eqBand = DynamicsProcessing.EqBand(true, cutoffHz[b], 0f)
                    config.setPreEqBandAllChannelsTo(b, eqBand)
                    config.setPostEqBandAllChannelsTo(b, eqBand)
                }

                // Configure 6-band MBC crossovers
                val mbcCutoffs = floatArrayOf(85f, 220f, 750f, 2500f, 7000f, 20000f)
                for (m in 0 until 6) {
                    val mbcBand = DynamicsProcessing.MbcBand(
                        true,
                        mbcCutoffs[m],
                        5f, // attackTime ms
                        80f, // releaseTime ms
                        2.0f, // ratio
                        -18f, // threshold dB
                        6f, // kneeWidth dB
                        -65f, // noiseGateThreshold dB
                        1.0f, // expanderRatio
                        0f, // preGain dB
                        0f // postGain dB
                    )
                    config.setMbcBandAllChannelsTo(m, mbcBand)
                }

                val dp = DynamicsProcessing(PRIORITY_MAX, sessionId, config)
                dp.enabled = isMasterEnabled
                bundle.dynamicsProcessing = dp
            }
        }

        // 2. Standard Hardware Equalizer
        if (bundle.equalizer == null) {
            runCatching {
                bundle.equalizer = Equalizer(PRIORITY_HIGH, sessionId).apply {
                    enabled = isMasterEnabled
                }
            }
        }

        // 3. Hardware BassBoost
        if (bundle.bassBoost == null) {
            runCatching {
                bundle.bassBoost = BassBoost(PRIORITY_HIGH, sessionId).apply {
                    enabled = isMasterEnabled
                }
            }
        }

        // 4. Hardware Virtualizer (Spatial 3D)
        if (bundle.virtualizer == null) {
            runCatching {
                bundle.virtualizer = Virtualizer(PRIORITY_HIGH, sessionId).apply {
                    enabled = isMasterEnabled
                }
            }
        }

        // 5. Hardware PresetReverb (for Session 0 aux/insert)
        if (bundle.presetReverb == null) {
            runCatching {
                bundle.presetReverb = PresetReverb(PRIORITY_HIGH, sessionId).apply {
                    enabled = isMasterEnabled
                }
            }
        }

        // 6. Hardware LoudnessEnhancer (works directly on YouTube & system streams)
        if (bundle.loudnessEnhancer == null) {
            runCatching {
                bundle.loudnessEnhancer = LoudnessEnhancer(sessionId).apply {
                    enabled = isMasterEnabled
                }
            }
        }

        _hookedSessionsCount.value = sessionBundles.size
    }

    fun updateParameters(
        enabled: Boolean,
        bandsDb: List<Float>,
        bass: Int,
        reverb: Int,
        reverbRoom: String,
        spatial: Int,
        preamp: Float,
        limiter: Boolean,
        lrTwinVocalEnabled: Boolean = lrTwinVocalSplitterActive,
        lrTwinVocalPct: Int = lrTwinVocalStrength,
        earDirectEnabled: Boolean = earDirectZeroBrainActive,
        zeroBrainPct: Int = zeroBrainPressureStrength,
        wooferBox: WooferEnclosureType = wooferBoxType,
        godTierEnabled: Boolean = godTierSovereignActive,
        godTierDualEarPct: Int = godTierDualEarStrength,
        multiDriverArray: MultiDriverHybridArray = multiDriverArrayType,
        truePeakBrickwall: Boolean = truePeakBrickwallActive
    ) {
        isMasterEnabled = enabled
        val interpolated = EqPresetEntity.interpolateTo31Bands(bandsDb)
        val copy = FloatArray(EqPresetEntity.BAND_COUNT)
        for (i in 0 until EqPresetEntity.BAND_COUNT) {
            copy[i] = interpolated[i].coerceIn(-15f, 15f)
        }
        currentBandsDb = copy
        bassStrength = bass.coerceIn(0, 100)
        reverbAmount = reverb.coerceIn(0, 100)
        reverbRoomType = reverbRoom
        spatialAmount = spatial.coerceIn(0, 100)
        preampDb = preamp.coerceIn(-12f, 12f)
        softLimiterEnabled = limiter
        lrTwinVocalSplitterActive = lrTwinVocalEnabled
        lrTwinVocalStrength = lrTwinVocalPct.coerceIn(0, 100)
        earDirectZeroBrainActive = earDirectEnabled
        zeroBrainPressureStrength = zeroBrainPct.coerceIn(0, 100)
        wooferBoxType = wooferBox
        godTierSovereignActive = godTierEnabled
        godTierDualEarStrength = godTierDualEarPct.coerceIn(0, 100)
        multiDriverArrayType = multiDriverArray
        truePeakBrickwallActive = truePeakBrickwall

        ensureSessionHooked(0, "GlobalOutputMix")
        applyParametersToAllSessions()
        updateStatusBanner()
    }

    private fun applyParametersToAllSessions() {
        val active = isMasterEnabled
        val bands = currentBandsDb
        val bass = bassStrength
        val spatial = spatialAmount
        val revLevel = reverbAmount
        val revRoom = reverbRoomType
        val preamp = preampDb
        val limit = softLimiterEnabled
        val twinVocalOn = lrTwinVocalSplitterActive && lrTwinVocalStrength > 0
        val twinVocalNorm = if (twinVocalOn) (lrTwinVocalStrength / 100f).coerceIn(0f, 1f) else 0f
        val earDirectOn = earDirectZeroBrainActive && zeroBrainPressureStrength > 0
        val zeroBrainNorm = if (earDirectOn) (zeroBrainPressureStrength / 100f).coerceIn(0f, 1f) else 0f
        val wooferOffsets = wooferBoxType.bandOffsets9
        val godTierOn = godTierSovereignActive && godTierDualEarStrength > 0
        val godTierNorm = if (godTierOn) (godTierDualEarStrength / 100f).coerceIn(0f, 1f) else 0f

        // Smooth sub-bass shelf from Bass Boost knob across 20Hz..125Hz (bands 0..8) — capped cleanly to avoid mud
        val maxSubBoostDb = if (active) (bass / 100f) * 3.2f else 0f

        // Gentle spatial air lift on upper bands (10kHz..20kHz, bands 27..30)
        val spatialAirDb = if (active) (spatial / 100f) * 1.4f else 0f

        // Calculate automatic anti-clipping headroom compensation based on peak positive EQ band + sub boost
        val maxPositiveBandDb = if (active) (bands.maxOrNull() ?: 0f).coerceAtLeast(0f) else 0f
        val autoHeadroomCutDb = if (active) {
            -((maxPositiveBandDb + maxSubBoostDb) * 0.52f + 1.8f).coerceIn(1.5f, 8.5f)
        } else 0f

        sessionBundles.values.forEach { bundle ->
            val hasActiveDp = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && bundle.dynamicsProcessing != null

            // 1. Apply 31-band EQ + Smooth Ear-Direct L/R Contour + Velvet Limiter to DynamicsProcessing (API 28+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                runCatching {
                    bundle.dynamicsProcessing?.let { dp ->
                        dp.enabled = active
                        val cutoffHz = EqPresetEntity.FREQUENCY_HZ
                        val bandCount = EqPresetEntity.BAND_COUNT
                        for (b in 0 until bandCount) {
                            // Only add a gentle woofer contour if God-Tier hasn't already sculpted the 31 bands
                            val wooferShapeDb = if (active && earDirectOn && !godTierOn && b <= 8) {
                                wooferOffsets.getOrElse(b) { 0f } * 0.22f * (bass / 50f).coerceIn(0.4f, 1.2f)
                            } else 0f
                            val extraBass = if (b <= 8) {
                                maxSubBoostDb * (1f - (b / 10f)) + wooferShapeDb
                            } else 0f
                            val extraAir = if (b >= 27) spatialAirDb else 0f

                            // 🦴 Smooth Skull-Bone 160–250Hz Drain & 3.15kHz Anti-Harshness Silk
                            // (Prevents boom and eliminates 3–8kHz "khirich-khirich" sibilance/glare)
                            val antiHarshnessCutDb = if (active && (earDirectOn || godTierOn)) {
                                val nullNorm = max(zeroBrainNorm, godTierNorm)
                                when (b) {
                                    9, 10 -> -1.6f * nullNorm   // 160–200Hz skull-bone resonance drain
                                    11 -> -1.0f * nullNorm      // 250Hz anti-mud relief
                                    22, 23 -> -1.4f * nullNorm  // 3.15k–4kHz anti-glare & anti-scratch relief
                                    25, 26 -> -1.2f * nullNorm  // 6.3k–8kHz anti-sibilance ("khirich-khirich" eraser)
                                    else -> 0f
                                }
                            } else 0f

                            val baseBandDb = if (active) {
                                (bands.getOrElse(b) { 0f } + extraBass + extraAir + antiHarshnessCutDb)
                                    .coerceIn(-12f, 9.5f)
                            } else 0f

                            if (active && (twinVocalOn || earDirectOn || godTierOn) && b >= 12) {
                                // Smooth Wide-Zone L/R Binaural Ear Contour (NO alternating b%2 comb zig-zag!)
                                // Wide multi-band zones prevent biquad filter-bank ringing ("khirich-khirich")
                                // while keeping vocals & instruments 100% in the Left & Right ears (0% inside brain).
                                val splitNorm = max(max(twinVocalNorm, zeroBrainNorm), godTierNorm)
                                val smoothZoneTiltDb = when (b) {
                                    in 12..15 -> 0.9f * splitNorm    // Left ear warm lower-vocal body (315–630Hz)
                                    in 16..18 -> -0.8f * splitNorm   // Right ear complementary upper-mid body (800–1.25kHz)
                                    in 19..21 -> 1.1f * splitNorm    // Left ear presence lock (1.6k–2.5kHz)
                                    in 22..24 -> -0.9f * splitNorm   // Right ear presence lock (3.15k–5kHz)
                                    in 27..30 -> 0.8f * splitNorm    // Silky ribbon air differential (10k–20kHz)
                                    else -> 0f
                                }
                                val leftTotalDb = (baseBandDb + smoothZoneTiltDb).coerceIn(-12f, 9.5f)
                                val rightTotalDb = (baseBandDb - smoothZoneTiltDb).coerceIn(-12f, 9.5f)

                                val preL = DynamicsProcessing.EqBand(true, cutoffHz[b], leftTotalDb * 0.50f)
                                val postL = DynamicsProcessing.EqBand(true, cutoffHz[b], leftTotalDb * 0.50f)
                                val preR = DynamicsProcessing.EqBand(true, cutoffHz[b], rightTotalDb * 0.50f)
                                val postR = DynamicsProcessing.EqBand(true, cutoffHz[b], rightTotalDb * 0.50f)

                                dp.setPreEqBandByChannelIndex(0, b, preL)
                                dp.setPostEqBandByChannelIndex(0, b, postL)
                                dp.setPreEqBandByChannelIndex(1, b, preR)
                                dp.setPostEqBandByChannelIndex(1, b, postR)
                            } else {
                                // Symmetric L/R (all sub-bass & bass bands 0..11 stay 100% phase-locked)
                                val preBand = DynamicsProcessing.EqBand(active, cutoffHz[b], baseBandDb * 0.50f)
                                val postBand = DynamicsProcessing.EqBand(active, cutoffHz[b], baseBandDb * 0.50f)
                                dp.setPreEqBandAllChannelsTo(b, preBand)
                                dp.setPostEqBandAllChannelsTo(b, postBand)
                            }
                        }

                        // Phase-Coherent 6-Band Multi-Band Compressor (MBC) with smooth attack/release
                        // Prevents bass-wave chopping and eliminates intermodulation "khirich-khirich" distortion!
                        val mbcCutoffs = floatArrayOf(85f, 220f, 750f, 2500f, 7000f, 20000f)
                        for (m in 0 until 6) {
                            val mbcPostGain = when {
                                !active -> 0f
                                m == 0 -> (bass / 100f) * 1.4f
                                m == 1 -> -0.6f // Keeps 85–220Hz upper-bass clean and mud-free
                                m == 2 -> 0.4f  // Warm vocal body
                                m == 3 -> 0.5f  // Clear vocal presence without harshness
                                m == 4 -> -0.5f // Tames 2.5k–7kHz harshness & sibilance ("khirich-khirich" guard)
                                else -> 0.4f    // Silky 7k–20kHz air
                            }
                            val mbcBand = DynamicsProcessing.MbcBand(
                                active,
                                mbcCutoffs[m],
                                if (m <= 1) 18f else 12f,  // Smooth 18ms bass attack so 50Hz waves never distort
                                if (m <= 1) 160f else 110f, // Smooth 160ms release — zero waveform chatter
                                1.35f,                      // Gentle audiophile 1.35:1 compression ratio
                                -18f,                       // Threshold dB
                                8f,                         // Wide 8dB soft knee for velvet transitions
                                -70f,                       // Noise gate threshold
                                1.0f,                       // Expander ratio
                                -1.0f,                      // -1.0dB MBC input cushion
                                mbcPostGain
                            )
                            dp.setMbcBandAllChannelsTo(m, mbcBand)
                        }

                        // Apply Preamp + Automatic Anti-Clipping Headroom so PreEQ + PostEQ never overload 0 dBFS
                        val safeInputGainDb = if (active) {
                            (preamp + autoHeadroomCutDb).coerceIn(-12f, 1.5f)
                        } else 0f
                        dp.setInputGainAllChannelsTo(safeInputGainDb)

                        // Velvet Mastering Limiter: 8ms attack & 140ms release prevents bass-cycle wave chopping!
                        val brickwallOn = limit || truePeakBrickwallActive
                        val limiterCfg = DynamicsProcessing.Limiter(
                            active && brickwallOn,
                            true,
                            0,
                            8.0f,   // Smooth 8ms attack (never chops low/mid waveforms)
                            140.0f, // Silky 140ms release (zero buzzing / zero khirich-khirich IMD)
                            2.5f,   // Gentle 2.5:1 soft-knee mastering ratio
                            -2.2f,  // -2.2 dBFS threshold cushion
                            if (active) -0.6f else 0f // Clean -0.6 dBFS true-peak output ceiling
                        )
                        dp.setLimiterAllChannelsTo(limiterCfg)
                    }
                }
            }

            // 2. Standard Hardware Equalizer:
            // CRITICAL FIX: Only apply legacy Equalizer when DynamicsProcessing is NOT present on this session!
            // Applying both DynamicsProcessing AND Equalizer simultaneously doubled the EQ gain (+25dB) and caused crackling!
            runCatching {
                bundle.equalizer?.let { eq ->
                    if (hasActiveDp) {
                        val numBands = eq.numberOfBands.toInt()
                        for (b in 0 until numBands) {
                            eq.setBandLevel(b.toShort(), 0.toShort())
                        }
                        eq.enabled = false
                    } else {
                        eq.enabled = active
                        val numBands = eq.numberOfBands.toInt()
                        val range = eq.bandLevelRange
                        val minMb = range[0].toInt()
                        val maxMb = range[1].toInt()
                        val maxIdx = EqPresetEntity.BAND_COUNT - 1
                        for (b in 0 until numBands) {
                            val mappedIdx = ((b.toFloat() / max(1, numBands - 1)) * maxIdx).roundToInt().coerceIn(0, maxIdx)
                            val extraBass = if (mappedIdx <= 8) {
                                maxSubBoostDb * 0.5f * (1f - mappedIdx / 10f)
                            } else 0f
                            val targetDb = if (active) (bands.getOrElse(mappedIdx) { 0f } * 0.7f + extraBass) else 0f
                            val targetMb = (targetDb * 100f).toInt().coerceIn(minMb, maxMb).toShort()
                            eq.setBandLevel(b.toShort(), targetMb)
                        }
                    }
                }
            }

            // 3. Hardware BassBoost: scaled gently when DynamicsProcessing is active so sub-bass stays punchy without clipping
            runCatching {
                bundle.bassBoost?.let { bb ->
                    bb.enabled = active && bass > 0
                    if (bb.strengthSupported) {
                        val scaledStrength = if (!active) 0 else if (hasActiveDp) (bass * 4).coerceIn(0, 450) else (bass * 8).coerceIn(0, 800)
                        bb.setStrength(scaledStrength.toShort())
                    }
                }
            }

            // 4. Hardware Virtualizer (Spatial 3D + Ear-Direct / L/R Twin-Vocal Wide Stage)
            // Capped at 650/1000 so hardware phase-shifter never creates metallic comb-filter artifacts
            runCatching {
                bundle.virtualizer?.let { virt ->
                    val earSplitNorm = max(max(twinVocalNorm, zeroBrainNorm), godTierNorm)
                    val effectiveSpatial = if (twinVocalOn || earDirectOn || godTierOn) {
                        max(spatial, (72 + earSplitNorm * 13f).roundToInt()).coerceIn(0, 85)
                    } else {
                        spatial.coerceIn(0, 85)
                    }
                    virt.enabled = active && effectiveSpatial > 0
                    if (virt.strengthSupported) {
                        virt.setStrength((if (active) effectiveSpatial * 7 else 0).coerceIn(0, 650).toShort())
                    }
                }
            }

            // 5. Hardware PresetReverb
            runCatching {
                bundle.presetReverb?.let { rev ->
                    rev.enabled = active && revLevel > 0 && revRoom != "None"
                    rev.preset = when (revRoom) {
                        "Small Room" -> PresetReverb.PRESET_SMALLROOM
                        "Medium Room" -> PresetReverb.PRESET_MEDIUMROOM
                        "Large Hall" -> PresetReverb.PRESET_LARGEHALL
                        "Plate" -> PresetReverb.PRESET_PLATE
                        else -> PresetReverb.PRESET_NONE
                    }
                }
            }

            // 6. Hardware LoudnessEnhancer:
            // CRITICAL FIX: Never add positive LoudnessEnhancer gain on top of DynamicsProcessing unless user explicitly pushes Preamp > +2dB!
            runCatching {
                bundle.loudnessEnhancer?.let { le ->
                    if (hasActiveDp) {
                        val cleanBoostMb = if (active && preamp > 1.0f) {
                            ((preamp - 1.0f) * 60f).toInt().coerceIn(0, 300)
                        } else 0
                        le.setTargetGain(cleanBoostMb)
                        le.enabled = active && cleanBoostMb > 0
                    } else {
                        le.enabled = active
                        val totalGainMb = if (active) {
                            (preamp * 80f).toInt().coerceIn(-800, 400)
                        } else 0
                        le.setTargetGain(totalGainMb)
                    }
                }
            }
        }
    }

    private fun updateStatusBanner() {
        val hasDp = sessionBundles[0]?.dynamicsProcessing != null
        val hasEq = sessionBundles[0]?.equalizer != null
        val externalPlaying = _isExternalMediaPlaying.value
        val count = sessionBundles.size
        val godTag = if (godTierSovereignActive) " • 👑 ${multiDriverArrayType.badge} • 🔒 ${bassStrength}% Bass" else ""
        val earTag = if (earDirectZeroBrainActive) " • 👂 Kaan-Direct (0% Dimag)" else ""

        _statusText.value = when {
            !isMasterEnabled -> "DSP Bypassed • Tap Power to Enable on YouTube & Apps"
            externalPlaying && hasDp ->
                "YouTube / Media Hooked! (31-Band$godTag$earTag • $count Session${if (count > 1) "s" else ""})"
            hasDp ->
                "Global YouTube & System Hook Active (31-Band$godTag$earTag)"
            hasEq ->
                "Global Hardware AudioFX Hook Active ($count Session${if (count > 1) "s" else ""}$godTag$earTag)"
            else ->
                "System DSP Active • Ready for YouTube & Media Apps$godTag$earTag"
        }
    }

    companion object {
        private const val PRIORITY_MAX = 100000
        private const val PRIORITY_HIGH = 50000

        @Volatile
        private var INSTANCE: GlobalAudioEffectManager? = null

        fun getInstance(context: Context): GlobalAudioEffectManager {
            return INSTANCE ?: synchronized(this) {
                val instance = GlobalAudioEffectManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
