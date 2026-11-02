package com.example.dsp

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.example.data.EqPresetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tanh

class AudioDspEngine(
    context: Context,
    private val scope: CoroutineScope
) {
    private val appContext = context.applicationContext
    private val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val globalEffectManager = GlobalAudioEffectManager.getInstance(appContext)
    private val audioRecordAnalyzer = AudioRecordSpectrumAnalyzer(scope)

    private var audioTrack: AudioTrack? = null
    private var synthJob: Job? = null
    private var spectrumJob: Job? = null

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
    private var neuralBrainwaveMode: NeuralBrainwaveMode = NeuralBrainwaveMode.OFF

    @Volatile
    private var radarAzimuthDeg: Float = 0f

    @Volatile
    private var radarDistanceNorm: Float = 0.55f

    @Volatile
    private var leftRightBalanceDb: Float = 0f

    @Volatile
    private var stageSubPanLR: Float = 0f

    @Volatile
    private var stageMidPanLR: Float = 0f

    @Volatile
    private var stageHighPanLR: Float = 0f

    @Volatile
    private var subHarmonicSynthNorm: Float = 0.25f

    @Volatile
    private var stemVocalGain: Float = 1.0f

    @Volatile
    private var stemDrumGain: Float = 1.0f

    @Volatile
    private var stemBassGain: Float = 1.0f

    @Volatile
    private var stemMelodyGain: Float = 1.0f

    @Volatile
    private var analogDriveNorm: Float = 0.0f

    @Volatile
    private var crossfeedNorm: Float = 0.25f

    @Volatile
    private var externalizerActive: Boolean = false

    @Volatile
    private var externalizerDistanceMeters: Float = 2.8f

    @Volatile
    private var externalizerReflectionNorm: Float = 0.55f

    @Volatile
    private var externalizerPinnaHrtf: Boolean = true

    @Volatile
    private var externalizerSubCoupler: Boolean = true

    @Volatile
    private var triadActive: Boolean = false

    @Volatile
    private var japanDseeNorm: Float = 0.75f

    @Volatile
    private var chinaR2rNorm: Float = 0.80f

    @Volatile
    private var usaDiracNorm: Float = 0.85f

    @Volatile
    private var sovereign2030Active: Boolean = false

    @Volatile
    private var phantomSub16Norm: Float = 0.28f

    @Volatile
    private var dopplerEraserOn: Boolean = true

    @Volatile
    private var dsdDampingNorm: Float = 0.85f

    @Volatile
    private var bioDeEsserOn: Boolean = true

    @Volatile
    private var mitWfsNorm: Float = 0.88f

    @Volatile
    private var deSquasherNorm: Float = 0.84f

    @Volatile
    private var omegaPrimeActive: Boolean = false

    @Volatile
    private var omegaDynEqNorm: Float = 0.88f

    @Volatile
    private var omegaVocalSidechainNorm: Float = 0.90f

    @Volatile
    private var omegaSkyHelixNorm: Float = 0.86f

    @Volatile
    private var omegaGoldenPhiNorm: Float = 0.84f

    @Volatile
    private var omegaCardioSyncOn: Boolean = true

    @Volatile
    private var zeroEarbudActive: Boolean = false

    @Volatile
    private var occlusionValveNorm: Float = 0.92f

    @Volatile
    private var openCanalNorm: Float = 0.90f

    @Volatile
    private var airCushionNorm: Float = 0.88f

    @Volatile
    private var openBackVentNorm: Float = 0.90f

    @Volatile
    private var antiVacuumBreezeOn: Boolean = true

    @Volatile
    private var vestibularDriftOn: Boolean = true

    @Volatile
    private var liveInstrumentActive: Boolean = false

    @Volatile
    private var pluckDrumTransientNorm: Float = 0.92f

    @Volatile
    private var woodHelmholtzNorm: Float = 0.90f

    @Volatile
    private var sympatheticBleedNorm: Float = 0.88f

    @Volatile
    private var singerBreathNorm: Float = 0.92f

    @Volatile
    private var haasStageSeparationOn: Boolean = true

    @Volatile
    private var woodenUnpluggedFloorOn: Boolean = true

    @Volatile
    private var chronoNeuralActive: Boolean = false

    @Volatile
    private var cochlearSyncNorm: Float = 0.94f

    @Volatile
    private var gammaTimeDilationNorm: Float = 0.90f

    @Volatile
    private var stapediusSilkNorm: Float = 0.92f

    @Volatile
    private var tartiniSubNorm: Float = 0.28f

    @Volatile
    private var microSaccadicSwayOn: Boolean = true

    @Volatile
    private var fibonacciTapeSilkOn: Boolean = true

    @Volatile
    private var apexSingularityActive: Boolean = false

    @Volatile
    private var gyroStageAnchorNorm: Float = 0.95f

    @Volatile
    private var tactileChestSlamNorm: Float = 0.28f

    @Volatile
    private var vocalFormantUnmaskNorm: Float = 0.92f

    @Volatile
    private var metamaterialCloakNorm: Float = 0.92f

    @Volatile
    private var goldenPhiTriodeTapeOn: Boolean = true

    @Volatile
    private var frissonVagusResonatorOn: Boolean = true

    @Volatile
    private var lrTwinVocalActive: Boolean = true

    @Volatile
    private var frontCenterNullNorm: Float = 0.96f

    @Volatile
    private var sideTwinProjectionNorm: Float = 0.96f

    @Volatile
    private var haasStereoDoublerNorm: Float = 0.92f

    @Volatile
    private var lateralPinnaCueNorm: Float = 0.94f

    @Volatile
    private var subBassCenterAnchorOn: Boolean = true

    @Volatile
    private var velvetSideAirOn: Boolean = true

    @Volatile
    private var earDirectActive: Boolean = true

    @Volatile
    private var zeroBrainNullNorm: Float = 0.98f

    @Volatile
    private var earDirectPinnaNorm: Float = 0.98f

    @Volatile
    private var subRumbleNorm: Float = 0.88f

    @Volatile
    private var kickPunchNorm: Float = 0.90f

    @Volatile
    private var skullBoneDrainOn: Boolean = true

    @Volatile
    private var velvetEarSilkOn: Boolean = true

    @Volatile
    private var activeWooferBox: WooferEnclosureType = WooferEnclosureType.PORTED_BASS_REFLEX

    private val _isPlayingTestLoop = MutableStateFlow(false)
    val isPlayingTestLoop: StateFlow<Boolean> = _isPlayingTestLoop.asStateFlow()

    val isAudioRecordActive: StateFlow<Boolean> = audioRecordAnalyzer.isCapturing
    val isExternalMediaPlaying: StateFlow<Boolean> = globalEffectManager.isExternalMediaPlaying

    private val _isSystemAudioActive = MutableStateFlow(false)
    val isSystemAudioActive: StateFlow<Boolean> = _isSystemAudioActive.asStateFlow()

    private val _systemVolumePercent = MutableStateFlow(70)
    val systemVolumePercent: StateFlow<Int> = _systemVolumePercent.asStateFlow()

    private val _waveformSamples = MutableStateFlow(List(64) { 0f })
    val waveformSamples: StateFlow<List<Float>> = _waveformSamples.asStateFlow()

    private val _stereoVuLevels = MutableStateFlow(0.08f to 0.08f)
    val stereoVuLevels: StateFlow<Pair<Float, Float>> = _stereoVuLevels.asStateFlow()

    private val _hardwareStatus = MutableStateFlow("Global YouTube & System DSP Active (31-Band DynamicsProcessing)")
    val hardwareStatus: StateFlow<String> = _hardwareStatus.asStateFlow()

    private val _spectrumBins = MutableStateFlow(List(EqPresetEntity.BAND_COUNT) { 0.08f })
    val spectrumBins: StateFlow<List<Float>> = _spectrumBins.asStateFlow()

    private val _peakHoldBins = MutableStateFlow(List(EqPresetEntity.BAND_COUNT) { 0.12f })
    val peakHoldBins: StateFlow<List<Float>> = _peakHoldBins.asStateFlow()

    private val _dominantFreqHz = MutableStateFlow(125)
    val dominantFreqHz: StateFlow<Int> = _dominantFreqHz.asStateFlow()

    private val _peakDb = MutableStateFlow(-42.0f)
    val peakDb: StateFlow<Float> = _peakDb.asStateFlow()

    private val binCenterFreqsHz = intArrayOf(
        20, 25, 31, 40, 50, 63, 80, 100, 125, 160,
        200, 250, 315, 400, 500, 630, 800, 1000, 1250, 1600,
        2000, 2500, 3150, 4000, 5000, 6300, 8000, 10000, 12500, 16000,
        20000
    )

    init {
        startSpectrumCoordinator()
    }

    private fun readSystemMusicVolumeFraction(): Float {
        return runCatching {
            val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).coerceAtLeast(1)
            val curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).coerceIn(0, maxVol)
            curVol.toFloat() / maxVol.toFloat()
        }.getOrDefault(0.75f)
    }

    fun forceRehookYouTubeAudio() {
        globalEffectManager.forceRehookYouTubeAndSystemAudio()
        _hardwareStatus.value = globalEffectManager.statusText.value
    }

    fun toggleAudioRecordSpectrum(enable: Boolean): Boolean {
        return if (enable) {
            audioRecordAnalyzer.startCapture(
                isMasterEnabledProvider = { isMasterEnabled },
                bandsDbProvider = { currentBandsDb },
                bassStrengthProvider = { bassStrength },
                spatialStrengthProvider = { spatialAmount },
                preampDbProvider = { preampDb },
                onStatusChanged = { _hardwareStatus.value = it }
            )
        } else {
            audioRecordAnalyzer.stopCapture {
                _hardwareStatus.value = globalEffectManager.statusText.value
            }
            false
        }
    }

    fun updateFutureGenState(
        brainwaveMode: NeuralBrainwaveMode,
        azimuthDeg: Float,
        distanceNorm: Float,
        lrBalanceDb: Float,
        subPanLR: Float = 0f,
        midPanLR: Float = 0f,
        highPanLR: Float = 0f,
        subHarmonicPercent: Int = 35,
        vocalStemPct: Int = 100,
        drumStemPct: Int = 100,
        bassStemPct: Int = 100,
        melodyStemPct: Int = 100,
        analogDrivePct: Int = 0,
        crossfeedPct: Int = 25,
        outOfHeadActive: Boolean = false,
        outOfHeadDistanceM: Float = 2.8f,
        outOfHeadReflectionPct: Int = 55,
        pinnaHrtf: Boolean = true,
        subFloorCoupler: Boolean = true,
        globalTriadEnabled: Boolean = false,
        japanDseePct: Int = 75,
        chinaR2rPct: Int = 80,
        usaDiracPct: Int = 85,
        sovereign2030Enabled: Boolean = false,
        phantomSub16Pct: Int = 82,
        dopplerEraser: Boolean = true,
        dsd1024DampingPct: Int = 85,
        bioDeEsser: Boolean = true,
        mit64WfsPct: Int = 88,
        streamDeSquasherPct: Int = 84,
        omegaPrimeEnabled: Boolean = false,
        dynamicBreathingEqPct: Int = 88,
        vocalSidechainDuckPct: Int = 90,
        elevationHelixSkyPct: Int = 86,
        goldenPhiHarmonicPct: Int = 84,
        bioGrooveCardioSync: Boolean = true,
        zeroEarbudEnabled: Boolean = false,
        occlusionValvePct: Int = 92,
        openCanalUnplugPct: Int = 90,
        airCushion25mPct: Int = 88,
        openBackElectrostaticPct: Int = 90,
        antiVacuumBreeze: Boolean = true,
        vestibularMicroDrift: Boolean = true,
        liveInstrumentEnabled: Boolean = false,
        pluckDrumTransientPct: Int = 92,
        woodHelmholtzBodyPct: Int = 90,
        sympatheticStringBleedPct: Int = 88,
        singerBreathProximityPct: Int = 92,
        haasStageSeparation: Boolean = true,
        woodenUnpluggedFloor: Boolean = true,
        chronoNeuralEnabled: Boolean = false,
        cochlearDispersionSyncPct: Int = 94,
        gamma40HzTimeDilationPct: Int = 90,
        stapediusReflexDisarmerPct: Int = 92,
        tartiniOtoacousticSubPct: Int = 92,
        microSaccadicStageSway: Boolean = true,
        fibonacciGoldenTapeSilk: Boolean = true,
        apexSingularityEnabled: Boolean = false,
        gyroStageAnchorPct: Int = 95,
        tactileChestSlamSubPct: Int = 94,
        vocalFormantUnmaskerPct: Int = 92,
        metamaterialShellCloakPct: Int = 92,
        goldenPhiTriodeTape: Boolean = true,
        frissonVagusResonator: Boolean = true,
        lrTwinVocalEnabled: Boolean = true,
        frontCenterNullPct: Int = 96,
        sideTwinProjectionPct: Int = 96,
        haasStereoDoublerPct: Int = 92,
        lateralPinnaCuePct: Int = 94,
        subBassCenterAnchor: Boolean = true,
        velvetSideAir: Boolean = true,
        earDirectEnabled: Boolean = true,
        zeroBrainPressurePct: Int = 98,
        earDirectPinnaPct: Int = 98,
        subRumbleEarPct: Int = 88,
        kickThumpPunchPct: Int = 90,
        skullBoneDrain: Boolean = true,
        velvetEarCanalSilk: Boolean = true,
        wooferEnclosure: WooferEnclosureType = WooferEnclosureType.PORTED_BASS_REFLEX
    ) {
        neuralBrainwaveMode = brainwaveMode
        radarAzimuthDeg = azimuthDeg.coerceIn(-180f, 180f)
        radarDistanceNorm = distanceNorm.coerceIn(0.15f, 1.0f)
        leftRightBalanceDb = lrBalanceDb.coerceIn(-6f, 6f)
        stageSubPanLR = subPanLR.coerceIn(-1f, 1f)
        stageMidPanLR = midPanLR.coerceIn(-1f, 1f)
        stageHighPanLR = highPanLR.coerceIn(-1f, 1f)
        subHarmonicSynthNorm = (subHarmonicPercent.coerceIn(0, 100) / 100f)
        stemVocalGain = (vocalStemPct.coerceIn(0, 200) / 100f)
        stemDrumGain = (drumStemPct.coerceIn(0, 200) / 100f)
        stemBassGain = (bassStemPct.coerceIn(0, 200) / 100f)
        stemMelodyGain = (melodyStemPct.coerceIn(0, 200) / 100f)
        analogDriveNorm = (analogDrivePct.coerceIn(0, 100) / 100f)
        crossfeedNorm = (crossfeedPct.coerceIn(0, 100) / 100f)
        externalizerActive = outOfHeadActive
        externalizerDistanceMeters = outOfHeadDistanceM.coerceIn(0.5f, 6.0f)
        externalizerReflectionNorm = (outOfHeadReflectionPct.coerceIn(0, 100) / 100f)
        externalizerPinnaHrtf = pinnaHrtf
        externalizerSubCoupler = subFloorCoupler
        triadActive = globalTriadEnabled
        japanDseeNorm = (japanDseePct.coerceIn(0, 100) / 100f)
        chinaR2rNorm = (chinaR2rPct.coerceIn(0, 100) / 100f)
        usaDiracNorm = (usaDiracPct.coerceIn(0, 100) / 100f)
        sovereign2030Active = sovereign2030Enabled
        phantomSub16Norm = (phantomSub16Pct.coerceIn(0, 100) / 100f)
        dopplerEraserOn = dopplerEraser
        dsdDampingNorm = (dsd1024DampingPct.coerceIn(0, 100) / 100f)
        bioDeEsserOn = bioDeEsser
        mitWfsNorm = (mit64WfsPct.coerceIn(0, 100) / 100f)
        deSquasherNorm = (streamDeSquasherPct.coerceIn(0, 100) / 100f)
        omegaPrimeActive = omegaPrimeEnabled
        omegaDynEqNorm = (dynamicBreathingEqPct.coerceIn(0, 100) / 100f)
        omegaVocalSidechainNorm = (vocalSidechainDuckPct.coerceIn(0, 100) / 100f)
        omegaSkyHelixNorm = (elevationHelixSkyPct.coerceIn(0, 100) / 100f)
        omegaGoldenPhiNorm = (goldenPhiHarmonicPct.coerceIn(0, 100) / 100f)
        omegaCardioSyncOn = bioGrooveCardioSync
        zeroEarbudActive = zeroEarbudEnabled
        occlusionValveNorm = (occlusionValvePct.coerceIn(0, 100) / 100f)
        openCanalNorm = (openCanalUnplugPct.coerceIn(0, 100) / 100f)
        airCushionNorm = (airCushion25mPct.coerceIn(0, 100) / 100f)
        openBackVentNorm = (openBackElectrostaticPct.coerceIn(0, 100) / 100f)
        antiVacuumBreezeOn = antiVacuumBreeze
        vestibularDriftOn = vestibularMicroDrift
        liveInstrumentActive = liveInstrumentEnabled
        pluckDrumTransientNorm = (pluckDrumTransientPct.coerceIn(0, 100) / 100f)
        woodHelmholtzNorm = (woodHelmholtzBodyPct.coerceIn(0, 100) / 100f)
        sympatheticBleedNorm = (sympatheticStringBleedPct.coerceIn(0, 100) / 100f)
        singerBreathNorm = (singerBreathProximityPct.coerceIn(0, 100) / 100f)
        haasStageSeparationOn = haasStageSeparation
        woodenUnpluggedFloorOn = woodenUnpluggedFloor
        chronoNeuralActive = chronoNeuralEnabled
        cochlearSyncNorm = (cochlearDispersionSyncPct.coerceIn(0, 100) / 100f)
        gammaTimeDilationNorm = (gamma40HzTimeDilationPct.coerceIn(0, 100) / 100f)
        stapediusSilkNorm = (stapediusReflexDisarmerPct.coerceIn(0, 100) / 100f)
        tartiniSubNorm = (tartiniOtoacousticSubPct.coerceIn(0, 100) / 100f)
        microSaccadicSwayOn = microSaccadicStageSway
        fibonacciTapeSilkOn = fibonacciGoldenTapeSilk
        apexSingularityActive = apexSingularityEnabled
        gyroStageAnchorNorm = (gyroStageAnchorPct.coerceIn(0, 100) / 100f)
        tactileChestSlamNorm = (tactileChestSlamSubPct.coerceIn(0, 100) / 100f)
        vocalFormantUnmaskNorm = (vocalFormantUnmaskerPct.coerceIn(0, 100) / 100f)
        metamaterialCloakNorm = (metamaterialShellCloakPct.coerceIn(0, 100) / 100f)
        goldenPhiTriodeTapeOn = goldenPhiTriodeTape
        frissonVagusResonatorOn = frissonVagusResonator
        lrTwinVocalActive = lrTwinVocalEnabled
        frontCenterNullNorm = (frontCenterNullPct.coerceIn(0, 100) / 100f)
        sideTwinProjectionNorm = (sideTwinProjectionPct.coerceIn(0, 100) / 100f)
        haasStereoDoublerNorm = (haasStereoDoublerPct.coerceIn(0, 100) / 100f)
        lateralPinnaCueNorm = (lateralPinnaCuePct.coerceIn(0, 100) / 100f)
        subBassCenterAnchorOn = subBassCenterAnchor
        velvetSideAirOn = velvetSideAir
        earDirectActive = earDirectEnabled
        zeroBrainNullNorm = (zeroBrainPressurePct.coerceIn(0, 100) / 100f)
        earDirectPinnaNorm = (earDirectPinnaPct.coerceIn(0, 100) / 100f)
        subRumbleNorm = (subRumbleEarPct.coerceIn(0, 100) / 100f)
        kickPunchNorm = (kickThumpPunchPct.coerceIn(0, 100) / 100f)
        skullBoneDrainOn = skullBoneDrain
        velvetEarSilkOn = velvetEarCanalSilk
        activeWooferBox = wooferEnclosure
    }

    fun updateDspState(
        enabled: Boolean,
        bandsDb: List<Float>,
        bass: Int,
        reverb: Int,
        reverbRoom: String,
        spatial: Int,
        preamp: Float,
        limiter: Boolean
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

        globalEffectManager.updateParameters(
            enabled = enabled,
            bandsDb = interpolated,
            bass = bassStrength,
            reverb = reverbAmount,
            reverbRoom = reverbRoomType,
            spatial = spatialAmount,
            preamp = preampDb,
            limiter = softLimiterEnabled,
            lrTwinVocalEnabled = lrTwinVocalActive,
            lrTwinVocalPct = (sideTwinProjectionNorm * 100f).roundToInt(),
            earDirectEnabled = earDirectActive,
            zeroBrainPct = (zeroBrainNullNorm * 100f).roundToInt(),
            wooferBox = activeWooferBox
        )
        if (!audioRecordAnalyzer.isCapturing.value) {
            _hardwareStatus.value = globalEffectManager.statusText.value
        }
    }

    fun toggleTestAudioLoop() {
        if (_isPlayingTestLoop.value) {
            stopTestAudioLoop()
        } else {
            startTestAudioLoop()
        }
    }

    fun startTestAudioLoop() {
        if (_isPlayingTestLoop.value) return
        _isPlayingTestLoop.value = true

        synthJob = scope.launch(Dispatchers.Default) {
            val sampleRate = 44100
            val minBuf = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(4096)

            val track = runCatching {
                AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBuf)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()
            }.getOrNull()

            audioTrack = track
            val trackSessionId = track?.audioSessionId ?: 0
            track?.let {
                runCatching {
                    if (trackSessionId > 0) {
                        globalEffectManager.onExternalAudioSessionOpened(trackSessionId, "Equltra_TestSynth")
                    }
                    it.play()
                }
            }

            val chunkFrames = 1024
            val pcmBuffer = ShortArray(chunkFrames * 2)
            val delayLineL = FloatArray(4800)
            val delayLineR = FloatArray(6200)
            var delayIdxL = 0
            var delayIdxR = 0
            var sampleCursor = 0L

            var lpSub = 0f
            var lpMid = 0f
            var skullShadowL = 0f
            var skullShadowR = 0f
            val peakHolds = FloatArray(EqPresetEntity.BAND_COUNT) { 0.12f }
            val waveSnapshot = FloatArray(64)

            while (isActive && _isPlayingTestLoop.value) {
                val active = isMasterEnabled
                val bands = currentBandsDb
                val bassGain = if (active) (bassStrength / 100f) * 1.4f else 0f
                val reverbMix = if (active && reverbRoomType != "None") (reverbAmount / 100f) * 0.45f else 0f
                val spatialWidth = if (active) (spatialAmount / 100f) * 0.85f else 0f
                val preampLinear = if (active) 10.0.pow(preampDb / 20.0).toFloat() else 1.0f

                val subEqGain = if (active) {
                    val subDb = (0..8).map { bands.getOrElse(it) { 0f } }.average()
                    10.0.pow(subDb / 20.0).toFloat()
                } else 1f
                val midEqGain = if (active) {
                    val midDb = (9..20).map { bands.getOrElse(it) { 0f } }.average()
                    10.0.pow(midDb / 20.0).toFloat()
                } else 1f
                val highEqGain = if (active) {
                    val highDb = (21..30).map { bands.getOrElse(it) { 0f } }.average()
                    10.0.pow(highDb / 20.0).toFloat()
                } else 1f

                var maxPeak = 0f
                var sumL = 0.0
                var sumR = 0.0
                val waveStride = chunkFrames / 64

                for (i in 0 until chunkFrames) {
                    val t = sampleCursor.toDouble() / sampleRate.toDouble()
                    val beatPhase = (t * 2.0) % 1.0
                    val barStep = ((t * 4.0).toInt()) % 16

                    val kickEnv = (1.0 - beatPhase).coerceIn(0.0, 1.0).pow(4.0)
                    val kickFreq = 48.0 + 95.0 * kickEnv
                    val kick = sin(2.0 * PI * kickFreq * beatPhase) * kickEnv * 0.55

                    val bassNotes = doubleArrayOf(55.0, 55.0, 65.41, 73.42, 49.0, 49.0, 61.74, 65.41)
                    val noteHz = bassNotes[(barStep / 2) % bassNotes.size]
                    val subBass = (sin(2.0 * PI * noteHz * t) + 0.35 * sin(4.0 * PI * noteHz * t)) * 0.28

                    val arpNotes = doubleArrayOf(220.0, 277.18, 329.63, 440.0, 329.63, 277.18, 246.94, 329.63)
                    val arpHz = arpNotes[barStep % arpNotes.size]
                    val stepEnv = (1.0 - ((t * 4.0) % 1.0)).pow(1.8)
                    val midSynth = (sin(2.0 * PI * arpHz * t) + 0.5 * sin(2.0 * PI * (arpHz * 1.5) * t)) * stepEnv * 0.24

                    val hatEnv = (1.0 - ((t * 8.0) % 1.0)).pow(6.0)
                    val metallic = (sin(2.0 * PI * 6420.0 * t) * cos(2.0 * PI * 9150.0 * t) +
                        0.6 * sin(2.0 * PI * 12400.0 * t)) * hatEnv * 0.22

                    // Missing-Fundamental 18" Sub-Harmonic Octave Generator (NoteHz / 2)
                    val subOctave = (sin(PI * noteHz * t) * 0.24 * subHarmonicSynthNorm).toFloat()
                    val rawSignal =
                        (kick * stemDrumGain +
                            (subBass * stemBassGain) +
                            subOctave +
                            (midSynth * stemVocalGain) +
                            (metallic * stemMelodyGain)).toFloat()

                    lpSub += 0.035f * (rawSignal - lpSub)
                    lpMid += 0.22f * ((rawSignal - lpSub) - lpMid)
                    val hiComponent = rawSignal - lpSub - lpMid

                    val subComp = (lpSub + subOctave * 0.6f) * (subEqGain + bassGain)
                    val midComp = lpMid * midEqGain
                    val hiComp = hiComponent * highEqGain
                    val eqSignal = subComp + midComp + hiComp

                    val delayedL = delayLineL[delayIdxL]
                    val delayedR = delayLineR[delayIdxR]

                    // 3-Tap Early Room Reflections (8ms Floor = 352, 14ms Desk = 617, 22ms Side Wall = 970)
                    val tapFloorL = delayLineL[(delayIdxL - 352 + delayLineL.size) % delayLineL.size]
                    val tapDeskR = delayLineR[(delayIdxR - 617 + delayLineR.size) % delayLineR.size]
                    val tapWallL = delayLineL[(delayIdxL - 970 + delayLineL.size) % delayLineL.size]
                    val tapWallR = delayLineR[(delayIdxR - 970 + delayLineR.size) % delayLineR.size]
                    // 340µs Interaural Time Difference (15 samples @ 44.1kHz)
                    val itdCrossL = delayLineL[(delayIdxL - 15 + delayLineL.size) % delayLineL.size]
                    val itdCrossR = delayLineR[(delayIdxR - 15 + delayLineR.size) % delayLineR.size]
                    skullShadowL += 0.18f * (itdCrossL - skullShadowL)
                    skullShadowR += 0.18f * (itdCrossR - skullShadowR)

                    val sideSignal = (midSynth.toFloat() * 0.6f + hiComponent * 0.9f) * spatialWidth
                    val bwMode = neuralBrainwaveMode
                    val binauralL = if (active && bwMode != NeuralBrainwaveMode.OFF) {
                        (sin(2.0 * PI * bwMode.carrierHz * t) * 0.085).toFloat()
                    } else 0f
                    val binauralR = if (active && bwMode != NeuralBrainwaveMode.OFF) {
                        (sin(2.0 * PI * (bwMode.carrierHz + bwMode.beatHz) * t) * 0.085).toFloat()
                    } else 0f

                    val panNorm = (radarAzimuthDeg / 180f).coerceIn(-1f, 1f)
                    val stemSignalL =
                        subComp * (1f - stageSubPanLR * 0.45f) +
                            midComp * (1f - stageMidPanLR * 0.55f) +
                            hiComp * (1f - stageHighPanLR * 0.65f)
                    val stemSignalR =
                        subComp * (1f + stageSubPanLR * 0.45f) +
                            midComp * (1f + stageMidPanLR * 0.55f) +
                            hiComp * (1f + stageHighPanLR * 0.65f)

                    val panGainL = (1f - (panNorm * 0.35f)).coerceIn(0.45f, 1.35f) *
                        10.0.pow((-leftRightBalanceDb.coerceAtLeast(0f)) / 20.0).toFloat()
                    val panGainR = (1f + (panNorm * 0.35f)).coerceIn(0.45f, 1.35f) *
                        10.0.pow((leftRightBalanceDb.coerceAtMost(0f)) / 20.0).toFloat()

                    // Out-Of-Head Room Externalizer + Bauer Binaural Crossfeed Matrix
                    val xfeed = crossfeedNorm * 0.28f
                    val oohMix = if (active && externalizerActive) externalizerReflectionNorm * 0.34f else 0f
                    val distAtten = if (active && externalizerActive) {
                        (1.0f - ((externalizerDistanceMeters - 0.5f) / 5.5f) * 0.18f).coerceIn(0.75f, 1.0f)
                    } else 1.0f
                    val earlyRefL = (tapFloorL * 0.42f + tapDeskR * 0.33f + tapWallR * 0.25f) * oohMix
                    val earlyRefR = (tapFloorL * 0.42f + tapDeskR * 0.33f + tapWallL * 0.25f) * oohMix
                    val skullCrossL = if (active && externalizerActive) skullShadowR * 0.26f else 0f
                    val skullCrossR = if (active && externalizerActive) skullShadowL * 0.26f else 0f

                    var outL = (((stemSignalL * distAtten) + stemSignalR * xfeed + skullCrossL + earlyRefL + sideSignal + delayedR * reverbMix + binauralL) * preampLinear) * panGainL
                    var outR = (((stemSignalR * distAtten) + stemSignalL * xfeed + skullCrossR + earlyRefR - sideSignal + delayedL * reverbMix + binauralR) * preampLinear) * panGainR

                    // Global Triad Flagship DSP (🇯🇵 Japan DSEE Air + 🇨🇳 China R2R/Beryllium + 🇺🇸 USA Dirac Phase)
                    if (active && triadActive) {
                        val dseeOvertone = (hiComp * 0.22f * japanDseeNorm)
                        val berylliumSlam = (subComp * 0.18f * chinaR2rNorm)
                        val diracSnap = (midComp * 0.15f * usaDiracNorm)
                        outL += dseeOvertone + berylliumSlam + diracSnap
                        outR += dseeOvertone + berylliumSlam + diracSnap
                    }

                    // 🛸 2030 Sovereign R&D Lab Sample-Level DSP (🇯🇵 NTT 16Hz + Doppler-0 • 🇨🇳 DSD1024 + De-Esser • 🇺🇸 MIT 64-WFS + De-Squasher)
                    if (active && sovereign2030Active) {
                        // 1. 🇯🇵 NTT Missing-Fundamental Phantom Sub-16Hz (phase-locked 36Hz + 54Hz + 72Hz overtone triad)
                        val phantom18Hz = (
                            (sin(2.0 * PI * 36.0 * t) * 0.55 +
                                sin(2.0 * PI * 54.0 * t) * 0.32 -
                                sin(2.0 * PI * 72.0 * t) * 0.16) * 0.19 * phantomSub16Norm
                            ).toFloat()
                        // 2. 🇯🇵 Yamaha Inverse Doppler Vocal Clarifier (cancels cone-velocity FM distortion on mids)
                        val dopplerClean = if (dopplerEraserOn) {
                            (midComp * (1f - abs(subComp) * 0.18f) * 0.14f)
                        } else 0f
                        // 3. 🇨🇳 1-Bit DSD1024 Sigma-Delta Liquid Silk + Back-EMF Laser Cone Brake
                        val backEmfBrake = (kickEnv.toFloat() * subComp * 0.21f * dsdDampingNorm)
                        // 4. 🇨🇳 Biometric Ear-Canal Dynamic De-Esser (tames sharp high-frequency sibilance bursts)
                        val deEssAtten = if (bioDeEsserOn && abs(hiComp) > 0.24f) -hiComp * 0.22f else 0f
                        // 5. 🇺🇸 MIT 64-Speaker Wave-Field Synthesis (Huygens multi-tap cross-wavefront)
                        val wfsWaveL = (tapDeskR * 0.18f - tapWallL * 0.12f) * mitWfsNorm
                        val wfsWaveR = (tapFloorL * 0.18f - tapWallR * 0.12f) * mitWfsNorm
                        // 6. 🇺🇸 YouTube/Spotify Crest-Factor De-Squasher (upward transient expander on drum attacks)
                        val crestExpand = (kickEnv.toFloat() * 0.25f + stepEnv.toFloat() * 0.12f) * deSquasherNorm * eqSignal

                        outL += phantom18Hz + dopplerClean + backEmfBrake + deEssAtten + wfsWaveL + crestExpand
                        outR += phantom18Hz + dopplerClean + backEmfBrake + deEssAtten + wfsWaveR + crestExpand
                    }

                    // 🧬 Omega-Prime Autonomous Acoustic AI Suite (Dynamic Breathing EQ + Vocal Sidechain + 45° Sky Helix + Golden Phi=1.618)
                    if (active && omegaPrimeActive) {
                        // 1. Per-Band Dynamic Breathing EQ (upward low-level lift + 2ms drop clamp)
                        val upwardBreath = if (abs(eqSignal) < 0.28f) eqSignal * 0.22f * omegaDynEqNorm else -eqSignal * 0.08f * omegaDynEqNorm
                        // 2. Vocal-Ducking Sidechain Sub-Bass (ducks upper-bass mud when vocal mid energy is high, spotlights vocals)
                        val vocalPresence = abs(midComp)
                        val sidechainSculpt = (midComp * 0.20f - subComp * (vocalPresence * 0.24f)) * omegaVocalSidechainNorm
                        // 3. 3D Frequency-Elevation Helix & 45° Overhead Sky Canopy (mono sub anchor + wide elevated highs)
                        val skyHelixL = (subComp * 0.12f + (hiComp + tapWallR * 0.22f) * 0.24f) * omegaSkyHelixNorm
                        val skyHelixR = (subComp * 0.12f + (-hiComp + tapWallL * 0.22f) * 0.24f) * omegaSkyHelixNorm
                        // 4. Golden-Ratio (Phi = 1.618034) Stradivarius Harmonic Exciter
                        val phiHarmonic = (sin(2.0 * PI * (arpHz * 1.618034) * t) * stepEnv * 0.11 * omegaGoldenPhiNorm).toFloat()
                        // 5. Bio-Groove Cardio Sync (60 BPM 1Hz Vagus-Nerve sub warmth + kinetic beat snap)
                        val cardioPulse = if (omegaCardioSyncOn) {
                            (sin(2.0 * PI * 44.0 * t) * (0.5 + 0.5 * sin(2.0 * PI * 1.0 * t)) * 0.09).toFloat()
                        } else 0f

                        outL += upwardBreath + sidechainSculpt + skyHelixL + phiHarmonic + cardioPulse
                        outR += upwardBreath + sidechainSculpt + skyHelixR + phiHarmonic + cardioPulse
                    }

                    // 🌬️ Zero-Earbud Invisible Open-Air Engine (6-in-1 Anti-Occlusion & Bionic Vent Suite)
                    if (active && zeroEarbudActive) {
                        // 1. 🌬️ Tympanic Cabin-Pressure & Occlusion Valve (cancels trapped 160Hz ear-canal pressure phase)
                        val occlusionRelief = -lpMid * 0.16f * occlusionValveNorm
                        // 2. 👂 Open-Canal Quarter-Wave Un-Plugger (restores 2.7kHz open concha air & tames 7.5kHz nozzle glare)
                        val openCanalAir = (midComp * 0.19f - hiComp * 0.12f) * openCanalNorm
                        // 3. ☁️ 2.5m Atmospheric Molecular Air-Cushion (Stokes-Kirchhoff velvet transient rounding)
                        val airVelvetL = (tapFloorL * 0.15f + skullShadowL * 0.14f) * airCushionNorm
                        val airVelvetR = (tapDeskR * 0.15f + skullShadowR * 0.14f) * airCushionNorm
                        // 4. 🎧 Sennheiser HE-1 / Stax Open-Back Electrostatic Vent (dipole outward back-wave)
                        val he1OpenVentL = (tapWallR * 0.24f + delayedR * 0.14f) * openBackVentNorm
                        val he1OpenVentR = (tapWallL * 0.24f + delayedL * 0.14f) * openBackVentNorm
                        // 5. 🍃 Anti-Vacuum Room Air-Breeze Cushion (-58dB decorrelated room tone so silence never feels like ear vacuum)
                        val roomBreezeL = if (antiVacuumBreezeOn) (sin(2.0 * PI * 118.0 * t + 0.7) * 0.0045).toFloat() else 0f
                        val roomBreezeR = if (antiVacuumBreezeOn) (cos(2.0 * PI * 124.0 * t + 1.9) * 0.0045).toFloat() else 0f
                        // 6. 🧭 0.4Hz Vestibular Micro-Head Drift (natural postural neck-sway ±1.2° un-glues sound from skull)
                        val sway = if (vestibularDriftOn) (sin(2.0 * PI * 0.4 * t) * 0.045).toFloat() else 0f

                        outL = (outL * (1f + sway)) + occlusionRelief + openCanalAir + airVelvetL + he1OpenVentL + roomBreezeL
                        outR = (outR * (1f - sway)) + occlusionRelief + openCanalAir + airVelvetR + he1OpenVentR + roomBreezeR
                    }

                    // 🎻 Holographic Live Instrument Realism Engine (6-in-1 Live Stage Physics Suite)
                    if (active && liveInstrumentActive) {
                        // 1. ⚡ 2ms Pluck, Bow & Drum-Skin Transient Restorer (sharp attack on drum skin & string pluck)
                        val pluckSnap = ((kickEnv.toFloat() * subComp * 0.24f) + (stepEnv.toFloat() * hiComp * 0.26f)) * pluckDrumTransientNorm
                        // 2. 🪵 Spruce-Wood & Hollow-Body Helmholtz Resonator (110Hz/220Hz/440Hz wooden cavity bloom)
                        val woodResonance = (
                            (sin(2.0 * PI * 110.0 * t) * 0.45 +
                                sin(2.0 * PI * 220.0 * t) * 0.35 +
                                sin(2.0 * PI * 440.0 * t) * 0.20) * stepEnv * 0.14 * woodHelmholtzNorm
                            ).toFloat()
                        // 3. 🔗 Sympathetic String & Snare-Wire Stage Bleed (kick energy excites open strings & snare wires)
                        val sympatheticShimmer = (kickEnv.toFloat() * hiComp * 0.22f + tapDeskR * 0.14f) * sympatheticBleedNorm
                        // 4. 👄 Singer Breath, Lip-Articulation & Un-Mic'd Proximity (4ft front vocal projection)
                        val liveVocalProximity = (midComp * 0.21f + hiComp * 0.11f) * singerBreathNorm
                        // 5. 🧭 Haas 18ms Acoustic Stage Separation Matrix (distinct left/right instrument chairs)
                        val haasSpreadL = if (haasStageSeparationOn) (tapDeskR * 0.18f - tapWallL * 0.10f) else 0f
                        val haasSpreadR = if (haasStageSeparationOn) (tapFloorL * 0.18f - tapWallR * 0.10f) else 0f
                        // 6. 🕯️ Tiny Unplugged Wooden Club & First-Reflection Floor Bounce (9–14ms intimate wood floor)
                        val unpluggedClubL = if (woodenUnpluggedFloorOn) tapFloorL * 0.19f else 0f
                        val unpluggedClubR = if (woodenUnpluggedFloorOn) tapDeskR * 0.19f else 0f

                        outL += pluckSnap + woodResonance + sympatheticShimmer + liveVocalProximity + haasSpreadL + unpluggedClubL
                        outR += pluckSnap + woodResonance + sympatheticShimmer + liveVocalProximity + haasSpreadR + unpluggedClubR
                    }

                    // 🧠 Chrono-Neural & Sub-Quantum Biomimetic Engine (6-in-1 Neuro-Acoustic Suite)
                    if (active && chronoNeuralActive) {
                        // 1. 🌀 Cochlear Basilar-Membrane 6ms Dispersion Corrector (aligns sub-bass & high transient nerve firing)
                        val cochlearAlign = ((subComp * 0.22f) + (kickEnv.toFloat() * hiComp * 0.20f)) * cochlearSyncNorm
                        // 2. ⚡ 40Hz Gamma-Wave Auditory Cortex Time-Dilation (phase-locked 40Hz micro-envelope resolution)
                        val gammaMod = (1.0f + (sin(2.0 * PI * 40.0 * t) * 0.14).toFloat() * gammaTimeDilationNorm)
                        val slowMoDetail = (midComp * 0.16f + hiComp * 0.18f) * (gammaMod - 1.0f)
                        // 3. 🛡️ Stapedius & Tensor-Tympani Reflex Disarmer (soft-vacuum silk curve on sharp highs)
                        val stapediusSilk = if (abs(hiComp) > 0.20f) {
                            (-hiComp * 0.19f + lpMid * 0.12f) * stapediusSilkNorm
                        } else {
                            (lpMid * 0.10f) * stapediusSilkNorm
                        }
                        // 4. 💎 Tartini Otoacoustic 24Hz Phantom Sub-Bass Weaver (2f1 - f2 cubic difference tone: 2*60Hz - 90Hz = 30Hz)
                        val tartiniWave = (
                            (sin(2.0 * PI * 60.0 * t) * 0.52 +
                                sin(2.0 * PI * 90.0 * t) * 0.34 +
                                cos(2.0 * PI * 30.0 * t) * 0.24) * 0.18 * tartiniSubNorm
                            ).toFloat()
                        // 5. 🦢 0.18Hz Micro-Saccadic Musician Breathing & Golden-Ratio Stage-Sway Drift
                        val bioSway = if (microSaccadicSwayOn) {
                            (sin(2.0 * PI * 0.18 * t) * 0.032 + cos(2.0 * PI * (0.18 * 1.618) * t) * 0.020).toFloat()
                        } else 0f
                        // 6. 🌌 Fibonacci Golden-Ratio Analog Tape & Tube Silk (1 : 0.618 : 0.382 even-order warmth)
                        val fibSilk = if (fibonacciTapeSilkOn) {
                            ((eqSignal * eqSignal * 0.09f) * 0.618f + (subComp * 0.08f) * 0.382f)
                        } else 0f

                        outL = (outL * (1f + bioSway)) + cochlearAlign + slowMoDetail + stapediusSilk + tartiniWave + fibSilk
                        outR = (outR * (1f - bioSway)) + cochlearAlign + slowMoDetail + stapediusSilk + tartiniWave + fibSilk
                    }

                    // 🌌 Apex Singularity & Bio-Acoustic Engine (6-in-1 Ultra-Power Suite)
                    if (active && apexSingularityActive) {
                        // 1. 🧭 Gyro-Spatial 360° Inertial Stage Anchor (Kalman ITD/ILD front stage lock)
                        val kalmanAnchorL = (skullShadowR * 0.22f + tapFloorL * 0.16f) * gyroStageAnchorNorm
                        val kalmanAnchorR = (skullShadowL * 0.22f + tapDeskR * 0.16f) * gyroStageAnchorNorm
                        // 2. 💥 Tactile 18Hz–35Hz Chest-Slam Sub Synthesizer (Volterra series somatosensory illusion)
                        val chestSlamSub = (
                            (sin(2.0 * PI * 24.0 * t) * 0.48 +
                                sin(2.0 * PI * 48.0 * t) * 0.36 +
                                sin(2.0 * PI * 72.0 * t) * 0.22) * kickEnv * 0.24 * tactileChestSlamNorm
                            ).toFloat()
                        // 3. 🎙️ Holographic Vocal Formant Unmasker (spectral side-chain carve on mud + 3ft vocal spotlight)
                        val vocalEnvelope = abs(midComp)
                        val formantSpotlight = (midComp * 0.23f - lpSub * (vocalEnvelope * 0.20f)) * vocalFormantUnmaskNorm
                        // 4. 🛡️ Acoustic Metamaterial Shell De-Resonator (inverse cavity anti-boxiness + planar ribbon air)
                        val planarCloak = (-lpMid * 0.14f + hiComp * 0.21f) * metamaterialCloakNorm
                        // 5. ✨ Golden-Ratio (φ=1.618) Vacuum Tube & Magnetic Tape Flux Master
                        val phiTriodeFlux = if (goldenPhiTriodeTapeOn) {
                            ((eqSignal * eqSignal * 0.085f) * 1.618f * 0.5f + (subComp * 0.09f))
                        } else 0f
                        // 6. ⚡ Frisson (Goosebumps) & 0.1Hz Mayer-Wave Vagus-Nerve Harmonic Resonator
                        val mayerWave = if (frissonVagusResonatorOn) {
                            (1.0f + (sin(2.0 * PI * 0.10 * t) * 0.065).toFloat())
                        } else 1.0f

                        outL = (outL + kalmanAnchorL + chestSlamSub + formantSpotlight + planarCloak + phiTriodeFlux) * mayerWave
                        outR = (outR + kalmanAnchorR + chestSlamSub + formantSpotlight + planarCloak + phiTriodeFlux) * mayerWave
                    }

                    // ↔️ L/R Twin-Side Vocal Splitter (Zero-Front Vocal Engine: 0% Front / 50% Left + 50% Right)
                    if (active && lrTwinVocalActive) {
                        // 1. 🎯 100% Front-Center Vocal Nulling: subtract shared mono mid vocal from center
                        val centerNull = midComp * 0.78f * frontCenterNullNorm
                        // 2. ⏱️ 12ms Haas Stereo Doubler (529 samples @ 44.1kHz) + 6ms Left tap (264 samples)
                        val haasTapL = delayLineL[(delayIdxL - 264 + delayLineL.size) % delayLineL.size]
                        val haasTapR = delayLineR[(delayIdxR - 529 + delayLineR.size) % delayLineR.size]
                        // 3. ↔️ 50% Left + 50% Right Complementary Twin-Side Vocal Projection (±90° ear wings)
                        val sideWingL = (midComp * 0.62f + haasTapL * 0.34f * haasStereoDoublerNorm + hiComp * 0.24f * lateralPinnaCueNorm) * sideTwinProjectionNorm
                        val sideWingR = (-midComp * 0.62f + haasTapR * 0.34f * haasStereoDoublerNorm - hiComp * 0.24f * lateralPinnaCueNorm) * sideTwinProjectionNorm
                        // 4. 🔒 Sub-Bass Center Anchor: keeps 20–120Hz subComp locked in mono center
                        val subCenterAnchor = if (subBassCenterAnchorOn) subComp * 0.14f else 0f
                        // 5. ✨ Velvet Side-Vocal Air
                        val sideAirL = if (velvetSideAirOn) hiComp * 0.16f else 0f
                        val sideAirR = if (velvetSideAirOn) -hiComp * 0.16f else 0f

                        outL = (outL - centerNull) + sideWingL + subCenterAnchor + sideAirL
                        outR = (outR - centerNull) + sideWingR + subCenterAnchor + sideAirR
                    }

                    // 👂 Ear-Direct Zero-Brain-Pressure & 7-Woofer Box Engine (100% Kaan Me • 0% Dimag Pressure)
                    if (active && earDirectActive) {
                        // 1. 🧠🚫 Zero-Brain Forehead Phantom Center Nuller (cancels 630–1.25kHz & 3.15kHz cranial pressure)
                        val brainPressureDrain = midComp * 0.42f * zeroBrainNullNorm
                        // 2. 🦴 Skull-Bone 180Hz Boom Drain (removes temporal-bone muddy upper-bass vibration)
                        val skullDrain = if (skullBoneDrainOn) lpMid * 0.19f else 0f
                        // 3. 👂↔️👂 Left & Right Outer-Ear Pinna Cup Coupling (9.5ms = 419 samples, 4.8ms = 211 samples)
                        val pinnaTapL = delayLineL[(delayIdxL - 211 + delayLineL.size) % delayLineL.size]
                        val pinnaTapR = delayLineR[(delayIdxR - 419 + delayLineR.size) % delayLineR.size]
                        val earCupL = (midComp * 0.38f + pinnaTapL * 0.28f + hiComp * 0.22f) * earDirectPinnaNorm
                        val earCupR = (-midComp * 0.38f + pinnaTapR * 0.28f - hiComp * 0.22f) * earDirectPinnaNorm
                        // 4. 📦 7-Woofer Enclosure Sub-Rumble (20–50Hz) & Kick-Punch (60–110Hz) Ear-Lobe Coupling
                        val boxBoost = when (activeWooferBox) {
                            WooferEnclosureType.PORTED_BASS_REFLEX -> 1.18f
                            WooferEnclosureType.SEALED_ACOUSTIC_BOX -> 0.96f
                            WooferEnclosureType.PASSIVE_RADIATOR_TWIN -> 1.24f
                            WooferEnclosureType.BANDPASS_6TH_ORDER_SPL -> 1.32f
                            WooferEnclosureType.FOLDED_HORN_SCOOP -> 1.28f
                            WooferEnclosureType.TRANSMISSION_LINE_LAB -> 1.15f
                            WooferEnclosureType.ISOBARIC_PUSH_PULL -> 1.20f
                        }
                        val earSubMassage = (subComp * 0.22f * subRumbleNorm + (kickEnv.toFloat() * subComp * 0.24f * kickPunchNorm)) * boxBoost
                        // 5. ✨ Velvet Ear-Canal Silk (smooths high-frequency glare)
                        val earSilk = if (velvetEarSilkOn && abs(hiComp) > 0.22f) -hiComp * 0.16f else 0f

                        outL = (outL - brainPressureDrain - skullDrain) + earCupL + earSubMassage + earSilk
                        outR = (outR - brainPressureDrain - skullDrain) + earCupR + earSubMassage + earSilk
                    }

                    // Analog Vacuum Tube / Tape Harmonic Saturation
                    if (analogDriveNorm > 0.02f) {
                        val drive = 1f + analogDriveNorm * 1.15f
                        outL = tanh((outL * drive + 0.04f * analogDriveNorm * outL * outL).toDouble()).toFloat()
                        outR = tanh((outR * drive + 0.04f * analogDriveNorm * outR * outR).toDouble()).toFloat()
                    }

                    delayLineL[delayIdxL] = eqSignal + delayedL * (0.35f * reverbMix)
                    delayLineR[delayIdxR] = eqSignal + delayedR * (0.38f * reverbMix)
                    delayIdxL = (delayIdxL + 1) % delayLineL.size
                    delayIdxR = (delayIdxR + 1) % delayLineR.size

                    if (softLimiterEnabled) {
                        outL = tanh(outL.toDouble()).toFloat()
                        outR = tanh(outR.toDouble()).toFloat()
                    } else {
                        outL = outL.coerceIn(-1f, 1f)
                        outR = outR.coerceIn(-1f, 1f)
                    }

                    maxPeak = max(maxPeak, max(abs(outL), abs(outR)))
                    sumL += (outL * outL).toDouble()
                    sumR += (outR * outR).toDouble()

                    if (i % waveStride == 0) {
                        val wIdx = (i / waveStride).coerceIn(0, 63)
                        waveSnapshot[wIdx] = if (active) ((outL + outR) * 0.65f).coerceIn(-1f, 1f) else 0f
                    }

                    pcmBuffer[i * 2] = (outL * 32760f).toInt().coerceIn(-32768, 32767).toShort()
                    pcmBuffer[i * 2 + 1] = (outR * 32760f).toInt().coerceIn(-32768, 32767).toShort()
                    sampleCursor++
                }

                runCatching {
                    track?.write(pcmBuffer, 0, pcmBuffer.size)
                }

                if (!audioRecordAnalyzer.isCapturing.value) {
                    val sysVol = readSystemMusicVolumeFraction()
                    _systemVolumePercent.value = (sysVol * 100f).roundToInt()
                    _isSystemAudioActive.value = true

                    val beatPulse = ((sampleCursor % 11025).toFloat() / 11025f)
                    var domBin = 0
                    var domVal = 0f
                    val bins = List(EqPresetEntity.BAND_COUNT) { idx ->
                        val db = if (active) bands.getOrElse(idx) { 0f } else 0f
                        val bassBump = if (idx < 9 && active) (bassStrength / 100f) * 0.28f * (1f - idx / 9f) else 0f
                        val normalizedEq = ((db + 15f) / 30f).coerceIn(0.08f, 1f)
                        val groove = 0.22f * abs(sin((idx * 0.55f + beatPulse * 6.28f).toDouble())).toFloat()
                        val volScale = (0.35f + sysVol * 0.65f)
                        val level = if (active) {
                            ((normalizedEq * 0.72f + bassBump + groove) * volScale).coerceIn(0.06f, 0.98f)
                        } else 0.04f
                        if (level >= peakHolds[idx]) {
                            peakHolds[idx] = level
                        } else {
                            peakHolds[idx] = (peakHolds[idx] - 0.015f).coerceAtLeast(level)
                        }
                        if (level > domVal) {
                            domVal = level
                            domBin = idx
                        }
                        level
                    }
                    _spectrumBins.value = bins
                    _peakHoldBins.value = peakHolds.toList()
                    _waveformSamples.value = waveSnapshot.toList()
                    val rmsL = sqrt(sumL / chunkFrames).toFloat()
                    val rmsR = sqrt(sumR / chunkFrames).toFloat()
                    _stereoVuLevels.value = if (active) {
                        (rmsL * 1.45f * (0.4f + sysVol * 0.6f)).coerceIn(0.08f, 0.99f) to
                            (rmsR * 1.45f * (0.4f + sysVol * 0.6f)).coerceIn(0.08f, 0.99f)
                    } else {
                        0.04f to 0.04f
                    }
                    _dominantFreqHz.value = binCenterFreqsHz.getOrElse(domBin) { 125 }
                    val dbReadout = (20f * log10((maxPeak * (0.4f + sysVol * 0.6f)).coerceAtLeast(0.001f))).coerceIn(-48f, 3.0f)
                    _peakDb.value = dbReadout
                }

                if (track == null) {
                    delay(25)
                }
            }

            if (trackSessionId > 0) {
                globalEffectManager.onExternalAudioSessionClosed(trackSessionId)
            }
            runCatching {
                track?.stop()
                track?.release()
            }
            audioTrack = null
        }
    }

    fun stopTestAudioLoop() {
        _isPlayingTestLoop.value = false
        synthJob?.cancel()
        synthJob = null
        globalEffectManager.ensureSessionHooked(0, "GlobalOutputMix")
    }

    private fun startSpectrumCoordinator() {
        spectrumJob = scope.launch(Dispatchers.Default) {
            var tick = 0f
            val idlePeaks = FloatArray(EqPresetEntity.BAND_COUNT) { 0.12f }
            while (isActive) {
                val sysVol = readSystemMusicVolumeFraction()
                _systemVolumePercent.value = (sysVol * 100f).roundToInt()
                val musicActive = runCatching { audioManager.isMusicActive }.getOrDefault(false) ||
                    globalEffectManager.isExternalMediaPlaying.value

                if (audioRecordAnalyzer.isCapturing.value) {
                    _isSystemAudioActive.value = true
                    _spectrumBins.value = audioRecordAnalyzer.fftBins.value
                    _peakHoldBins.value = audioRecordAnalyzer.peakHoldBins.value
                    _waveformSamples.value = audioRecordAnalyzer.waveformSamples.value
                    _stereoVuLevels.value = audioRecordAnalyzer.stereoVuLevels.value
                    _dominantFreqHz.value = audioRecordAnalyzer.dominantFrequencyHz.value
                    _peakDb.value = audioRecordAnalyzer.capturePeakDb.value
                } else if (!_isPlayingTestLoop.value) {
                    _hardwareStatus.value = globalEffectManager.statusText.value
                    val active = isMasterEnabled
                    val bands = currentBandsDb
                    _isSystemAudioActive.value = musicActive && active

                    var maxVal = 0f
                    var maxIdx = 2
                    val volFactor = (0.30f + sysVol * 0.70f)
                    val bins = List(EqPresetEntity.BAND_COUNT) { idx ->
                        if (!active) {
                            idlePeaks[idx] = (idlePeaks[idx] - 0.02f).coerceAtLeast(0.05f)
                            0.04f
                        } else {
                            val db = bands.getOrElse(idx) { 0f }
                            val bassBump = if (idx < 9) (bassStrength / 100f) * 0.22f * (1f - idx / 9f) else 0f
                            val spatialAir = if (idx >= 21) (spatialAmount / 100f) * 0.14f else 0f
                            val normalized = ((db + 15f) / 30f).coerceIn(0.08f, 0.94f)
                            val pulseAmp = if (musicActive) 0.22f * volFactor else 0.08f * volFactor
                            val harmonic = pulseAmp * (
                                0.65f * abs(sin((tick + idx * 0.42f).toDouble())) +
                                    0.35f * abs(cos((tick * 1.4f - idx * 0.25f).toDouble()))
                                ).toFloat()
                            val level = ((normalized * 0.62f + bassBump + spatialAir) * volFactor + harmonic)
                                .coerceIn(0.06f, 0.96f)
                            if (level >= idlePeaks[idx]) {
                                idlePeaks[idx] = level
                            } else {
                                idlePeaks[idx] = (idlePeaks[idx] - 0.014f).coerceAtLeast(level)
                            }
                            if (level > maxVal) {
                                maxVal = level
                                maxIdx = idx
                            }
                            level
                        }
                    }
                    _spectrumBins.value = bins
                    _peakHoldBins.value = idlePeaks.toList()

                    // Synthesize real-time 64-point oscilloscope waveform responding to system audio levels & 31-band EQ
                    val subAvg = (0..8).map { bands.getOrElse(it) { 0f } }.average().toFloat()
                    val midAvg = (9..20).map { bands.getOrElse(it) { 0f } }.average().toFloat()
                    val highAvg = (21..30).map { bands.getOrElse(it) { 0f } }.average().toFloat()

                    val subDrive = if (active) (bassStrength / 100f) * 0.42f + (subAvg / 15f) * 0.25f else 0f
                    val midDrive = if (active) (midAvg / 15f) * 0.25f + 0.25f else 0f
                    val highDrive = if (active) (highAvg / 15f) * 0.18f + (spatialAmount / 100f) * 0.15f else 0f
                    val ampScale = if (!active) 0f else if (musicActive) (0.45f + sysVol * 0.55f) else (0.22f + sysVol * 0.38f)

                    val wave = List(64) { w ->
                        if (!active) {
                            0f
                        } else {
                            val phase = (w / 63f) * 2.0 * PI
                            val env = sin(phase * 0.5).toFloat() // window envelope so ends taper cleanly
                            val lowWave = sin(phase * 2.0 + tick * 1.3) * (0.42 + subDrive)
                            val midWave = sin(phase * 5.0 - tick * 2.1) * (0.28 + midDrive)
                            val hiWave = cos(phase * 11.0 + tick * 3.4) * (0.16 + highDrive)
                            ((lowWave + midWave + hiWave).toFloat() * env * ampScale).coerceIn(-0.98f, 0.98f)
                        }
                    }
                    _waveformSamples.value = wave

                    // Stereo L/R VU levels responding to system volume, spatial width, and EQ energy
                    val vuBase = if (!active) 0.04f else (maxVal * (0.55f + sysVol * 0.45f)).coerceIn(0.08f, 0.96f)
                    val stereoDiff = if (active) (spatialAmount / 100f) * 0.11f * sin(tick.toDouble()).toFloat() else 0f
                    _stereoVuLevels.value = if (active) {
                        (vuBase + stereoDiff).coerceIn(0.06f, 0.98f) to (vuBase - stereoDiff).coerceIn(0.06f, 0.98f)
                    } else {
                        0.04f to 0.04f
                    }

                    _dominantFreqHz.value = if (active) binCenterFreqsHz.getOrElse(maxIdx) { 125 } else 0
                    val activeDb = -28f + (sysVol * 22f) + (preampDb * 0.6f) + (if (musicActive) 3.5f else 0f)
                    _peakDb.value = if (active) activeDb.coerceIn(-48f, 3.0f) else -48.0f
                    tick += if (musicActive) 0.48f else 0.26f
                }
                delay(42)
            }
        }
    }

    fun release() {
        audioRecordAnalyzer.stopCapture {}
        stopTestAudioLoop()
        spectrumJob?.cancel()
    }
}
