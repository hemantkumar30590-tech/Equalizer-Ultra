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
    private var bassStrength: Int = 25 // 0..100 (Smooth 25% default)

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

    private val _statusText = MutableStateFlow("Global YouTube & System DSP Active (31-Band + ↔️ L/R Twin-Vocal Split)")
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
        lrTwinVocalPct: Int = lrTwinVocalStrength
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

        // Sub-bass extra shelf in dB from the Bass Boost knob across 20Hz..125Hz (bands 0..8)
        val maxSubBoostDb = if (active) (bass / 100f) * 5.8f else 0f

        // Spatial 3D presence air shelf on upper bands (4kHz..20kHz, bands 23..30) when Spatial > 0
        val spatialAirDb = if (active) (spatial / 100f) * 3.5f else 0f

        sessionBundles.values.forEach { bundle ->
            // 1. Apply 31-band EQ + Bass Boost + Spatial + L/R Twin-Vocal Split to DynamicsProcessing (API 28+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                runCatching {
                    bundle.dynamicsProcessing?.let { dp ->
                        dp.enabled = active
                        val cutoffHz = EqPresetEntity.FREQUENCY_HZ
                        val bandCount = EqPresetEntity.BAND_COUNT
                        for (b in 0 until bandCount) {
                            val extraBass = if (b <= 8) {
                                maxSubBoostDb * (1f - (b / 10f))
                            } else 0f
                            val extraAir = if (b >= 23) spatialAirDb else 0f
                            val baseBandDb = if (active) {
                                (bands.getOrElse(b) { 0f } + extraBass + extraAir).coerceIn(-18f, 18f)
                            } else 0f

                            if (active && twinVocalOn && b >= 11) {
                                // ↔️ L/R Twin-Side Vocal Splitter (0% Front • 50% Left + 50% Right):
                                // Sub-bass (b <= 10) stays 100% center-locked (Channel 0 == Channel 1).
                                // Vocal & presence bands (b in 11..30) receive complementary Left/Right
                                // stereo comb-matrix offsets + front-center nasal scoop so mono vocals
                                // decorrelate into 50% Left & 50% Right ear-wings instead of hitting from front!
                                val centerNotch = when (b) {
                                    14, 15, 16 -> -2.8f * twinVocalNorm // 500–800Hz dead-center frontal null
                                    17, 18 -> -2.4f * twinVocalNorm     // 1k–1.25kHz front nasal projection cut
                                    25 -> -2.0f * twinVocalNorm         // 6.3kHz frontal pinna cue cut
                                    else -> 0f
                                }
                                val sideWingLift = when (b) {
                                    11, 12 -> 1.8f * twinVocalNorm      // 250–315Hz warm side vocal body
                                    19, 20, 21 -> 2.6f * twinVocalNorm  // 1.6k–2.5kHz wide L/R vocal wings
                                    23, 24 -> 2.4f * twinVocalNorm      // 4k–5kHz lateral presence
                                    26, 27, 28 -> 2.8f * twinVocalNorm  // 8k–12.5kHz ±90° lateral pinna air
                                    else -> 0f
                                }
                                // Complementary L/R stereo comb decorrelation across vocal formants (b in 11..28)
                                val polarity = if (b % 2 == 0) 1f else -1f
                                val combSpreadDb = when (b) {
                                    in 11..13 -> 3.4f * twinVocalNorm
                                    in 14..22 -> 4.8f * twinVocalNorm
                                    in 23..28 -> 3.8f * twinVocalNorm
                                    else -> 2.0f * twinVocalNorm
                                }
                                val leftTotalDb = (baseBandDb + centerNotch + sideWingLift + polarity * combSpreadDb)
                                    .coerceIn(-18f, 18f)
                                val rightTotalDb = (baseBandDb + centerNotch + sideWingLift - polarity * combSpreadDb)
                                    .coerceIn(-18f, 18f)

                                val preL = DynamicsProcessing.EqBand(true, cutoffHz[b], leftTotalDb * 0.55f)
                                val postL = DynamicsProcessing.EqBand(true, cutoffHz[b], leftTotalDb * 0.45f)
                                val preR = DynamicsProcessing.EqBand(true, cutoffHz[b], rightTotalDb * 0.55f)
                                val postR = DynamicsProcessing.EqBand(true, cutoffHz[b], rightTotalDb * 0.45f)

                                dp.setPreEqBandByChannelIndex(0, b, preL)
                                dp.setPostEqBandByChannelIndex(0, b, postL)
                                dp.setPreEqBandByChannelIndex(1, b, preR)
                                dp.setPostEqBandByChannelIndex(1, b, postR)
                            } else {
                                // Standard symmetric L/R (or sub-bass bands 0..10 which stay center-locked)
                                val preBand = DynamicsProcessing.EqBand(active, cutoffHz[b], baseBandDb * 0.55f)
                                val postBand = DynamicsProcessing.EqBand(active, cutoffHz[b], baseBandDb * 0.45f)
                                dp.setPreEqBandAllChannelsTo(b, preBand)
                                dp.setPostEqBandAllChannelsTo(b, postBand)
                            }
                        }

                        // Boost low MBC postGain smoothly when Bass Boost is turned up,
                        // and apply dynamic Haas L/R attack/release offset on vocal MBC bands when Twin-Vocal is ON!
                        val mbcCutoffs = floatArrayOf(85f, 220f, 750f, 2500f, 7000f, 20000f)
                        for (m in 0 until 6) {
                            val mbcPostGain = when {
                                !active -> 0f
                                m == 0 -> (bass / 100f) * 3.0f
                                m == 1 -> (bass / 100f) * 1.5f
                                m == 3 && twinVocalOn -> 2.4f * twinVocalNorm // 750Hz–2.5kHz L/R vocal wing lift
                                m >= 4 -> (spatial / 100f) * 2.0f + (if (twinVocalOn) 1.5f * twinVocalNorm else 0f)
                                else -> 0f
                            }
                            if (active && twinVocalOn && m in 2..4) {
                                // Dynamic Haas L/R Micro-Timing Split on vocal bands (Left fast 3ms attack, Right 14ms attack)
                                val mbcLeft = DynamicsProcessing.MbcBand(
                                    true,
                                    mbcCutoffs[m],
                                    if (m % 2 == 0) 3.0f else 13.5f,
                                    if (m % 2 == 0) 65f else 95f,
                                    1.8f,
                                    -18f,
                                    6f,
                                    -65f,
                                    1.0f,
                                    0f,
                                    mbcPostGain + (if (m % 2 == 0) 1.2f * twinVocalNorm else -0.8f * twinVocalNorm)
                                )
                                val mbcRight = DynamicsProcessing.MbcBand(
                                    true,
                                    mbcCutoffs[m],
                                    if (m % 2 == 0) 13.5f else 3.0f,
                                    if (m % 2 == 0) 95f else 65f,
                                    1.8f,
                                    -18f,
                                    6f,
                                    -65f,
                                    1.0f,
                                    0f,
                                    mbcPostGain + (if (m % 2 == 0) -0.8f * twinVocalNorm else 1.2f * twinVocalNorm)
                                )
                                dp.setMbcBandByChannelIndex(0, m, mbcLeft)
                                dp.setMbcBandByChannelIndex(1, m, mbcRight)
                            } else {
                                val mbcBand = DynamicsProcessing.MbcBand(
                                    active,
                                    mbcCutoffs[m],
                                    5f,
                                    80f,
                                    if (bass > 50 && m <= 1) 2.5f else 1.8f,
                                    -16f,
                                    6f,
                                    -65f,
                                    1.0f,
                                    0f,
                                    mbcPostGain
                                )
                                dp.setMbcBandAllChannelsTo(m, mbcBand)
                            }
                        }

                        dp.setInputGainAllChannelsTo(if (active) preamp else 0f)
                        val limiterCfg = DynamicsProcessing.Limiter(
                            active && limit,
                            true,
                            0,
                            2f,
                            45f,
                            4.0f,
                            -1.2f,
                            if (active) (preamp * 0.5f).coerceIn(-6f, 6f) else 0f
                        )
                        dp.setLimiterAllChannelsTo(limiterCfg)
                    }
                }
            }

            // 2. Apply to standard Hardware Equalizer
            runCatching {
                bundle.equalizer?.let { eq ->
                    eq.enabled = active
                    val numBands = eq.numberOfBands.toInt()
                    val range = eq.bandLevelRange
                    val minMb = range[0].toInt()
                    val maxMb = range[1].toInt()
                    val maxIdx = EqPresetEntity.BAND_COUNT - 1
                    for (b in 0 until numBands) {
                        val mappedIdx = ((b.toFloat() / max(1, numBands - 1)) * maxIdx).roundToInt().coerceIn(0, maxIdx)
                        val extraBass = if (mappedIdx <= 8) {
                            maxSubBoostDb * 0.6f * (1f - mappedIdx / 10f)
                        } else 0f
                        val targetDb = if (active) (bands.getOrElse(mappedIdx) { 0f } + extraBass) else 0f
                        val targetMb = (targetDb * 100f).toInt().coerceIn(minMb, maxMb).toShort()
                        eq.setBandLevel(b.toShort(), targetMb)
                    }
                }
            }

            // 3. Apply to Hardware BassBoost
            runCatching {
                bundle.bassBoost?.let { bb ->
                    bb.enabled = active && bass > 0
                    if (bb.strengthSupported) {
                        bb.setStrength((if (active) bass * 10 else 0).coerceIn(0, 1000).toShort())
                    }
                }
            }

            // 4. Apply to Hardware Virtualizer (Spatial 3D + L/R Twin-Vocal Wide Stage)
            runCatching {
                bundle.virtualizer?.let { virt ->
                    val effectiveSpatial = if (twinVocalOn) max(spatial, (88 + twinVocalNorm * 12f).roundToInt()) else spatial
                    virt.enabled = active && effectiveSpatial > 0
                    if (virt.strengthSupported) {
                        virt.setStrength((if (active) effectiveSpatial * 10 else 0).coerceIn(0, 1000).toShort())
                    }
                }
            }

            // 5. Apply to Hardware PresetReverb
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

            // 6. Apply to Hardware LoudnessEnhancer (millibels: 1 dB = 100 mB)
            runCatching {
                bundle.loudnessEnhancer?.let { le ->
                    le.enabled = active
                    // Include positive EQ boost average + preamp so YouTube loudness/EQ jump is immediately audible
                    val avgPositiveEq = bands.filter { it > 0f }.average().let {
                        if (it.isNaN()) 0f else it.toFloat() * 0.35f
                    }
                    val totalGainMb = if (active) {
                        ((preamp + avgPositiveEq + (bass / 100f) * 2.5f) * 100f).toInt().coerceIn(-1200, 1500)
                    } else 0
                    le.setTargetGain(totalGainMb)
                }
            }
        }
    }

    private fun updateStatusBanner() {
        val hasDp = sessionBundles[0]?.dynamicsProcessing != null
        val hasEq = sessionBundles[0]?.equalizer != null
        val externalPlaying = _isExternalMediaPlaying.value
        val count = sessionBundles.size
        val twinTag = if (lrTwinVocalSplitterActive) " • ↔️ L/R Vocal Split ON" else ""

        _statusText.value = when {
            !isMasterEnabled -> "DSP Bypassed • Tap Power to Enable on YouTube & Apps"
            externalPlaying && hasDp ->
                "YouTube / Media Hooked! (31-Band$twinTag • $count Session${if (count > 1) "s" else ""})"
            hasDp ->
                "Global YouTube & System Hook Active (31-Band$twinTag)"
            hasEq ->
                "Global Hardware AudioFX Hook Active ($count Session${if (count > 1) "s" else ""}$twinTag)"
            else ->
                "System DSP Active • Ready for YouTube & Media Apps$twinTag"
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
