package com.example.ui

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.EqDatabase
import com.example.data.EqPresetEntity
import com.example.data.PresetImportExportParser
import com.example.data.PresetManager
import com.example.dsp.AiAutoEqEngine
import com.example.dsp.AiVoiceControlEngine
import com.example.dsp.AudioDspEngine
import com.example.dsp.EqHapticFeedbackManager
import com.example.dsp.AcousticVenueClone
import com.example.dsp.AnalogSaturationMode
import com.example.dsp.ApexSingularityMode
import com.example.dsp.ChronoNeuralBiomimeticMode
import com.example.dsp.EsportsThreatRadarState
import com.example.dsp.FutureGenAudioLabEngine
import com.example.dsp.GlobalTriadMode
import com.example.dsp.GyroHeadTrackingMode
import com.example.dsp.HolographicLiveInstrumentMode
import com.example.dsp.HolographicVibeProfile
import com.example.dsp.NeuralBrainwaveMode
import com.example.dsp.OmegaPrimeMode
import com.example.dsp.OutOfHeadExternalizerMode
import com.example.dsp.ServoTransducerProfile
import com.example.dsp.Sovereign2030RndMode
import com.example.dsp.SpatialStageFormation
import com.example.dsp.SpatialStageNode
import com.example.dsp.SpeedCruiseMode
import com.example.dsp.StemDeMixerPreset
import com.example.dsp.VoiceControlAction
import com.example.dsp.ZeroEarbudOpenAirMode
import com.example.service.EqForegroundService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

data class EqUiState(
    val isMasterEnabled: Boolean = true,
    val isBackgroundServiceEnabled: Boolean = true,
    val selectedTab: Int = 0, // 0: 31-Band EQ, 1: Bass & DSP, 2: Device Presets, 3: Presets, 4: Get APK
    val selectedBandZone: Int = 0, // 0: All 31, 1: Sub/Bass, 2: Low-Mids, 3: High-Mids, 4: Treble/Air
    val deviceSearchQuery: String = "",
    val activePresetName: String = "Harman Target",
    val bandGainsDb: List<Float> = EqPresetEntity.BUILT_IN_PRESETS[3].getBandGains(),
    val bassBoostStrength: Int = 25, // 0..100 (Smooth 25% default)
    val isBassBoostLocked: Boolean = true, // Locks user's Bass Boost across presets, modes & APK updates
    val reverbLevel: Int = 12, // 0..100
    val reverbRoom: String = "Small Room",
    val spatialLevel: Int = 35, // 0..100
    val preampDb: Float = -2.0f, // -12..+12 dB
    val softLimiterEnabled: Boolean = true,
    val isGlobalSystemMode: Boolean = true,
    val showSavePresetDialog: Boolean = false,
    val showPasteImportDialog: Boolean = false,
    val showAiAutoEqDialog: Boolean = false,
    val showAiProUnlockDialog: Boolean = false,
    val showVoiceControlDialog: Boolean = false,
    val showFutureLabDialog: Boolean = false,
    val isAiProUnlocked: Boolean = false,
    val isGeneratingAiEq: Boolean = false,
    val aiPromptInput: String = "",
    val aiSelectedDevice: String = "Apple AirPods Pro 3",
    val lastAiAnalysis: String = "PRO MODE LOCKED — Exclusive AI Auto-EQ + Voice Control Mastering Engine. Unlock with Owner Key to use.",
    val lastAiEngineBadge: String = "Gemini 3.5 Flash AI",
    val lastVoiceCommandSummary: String = "Say \"Bass full 100%\", \"AirPods Pro 3\", \"Sync YouTube\", or \"AI Punjabi Bass\"",
    val voiceCommandHistory: List<String> = listOf(
        "🎙️ Ready: Speak in Hindi, Hinglish, or English to control 31-Band EQ, Bass, 3D, Presets & AI Auto-EQ"
    ),
    // 7-in-1 Future-Gen Quantum Lab State
    val isRunningSonarScan: Boolean = false,
    val sonarTelemetryText: String = "SONAR READY • Tap 'SONAR SCAN' for 45Hz–12.8kHz Room & Earbud Calibration",
    val neuralBrainwaveMode: NeuralBrainwaveMode = NeuralBrainwaveMode.OFF,
    val radarAzimuthDeg: Float = 0f,
    val radarDistanceNorm: Float = 0.55f,
    val isRadarAutoOrbitEnabled: Boolean = false,
    val leftRightBalanceDb: Float = 0f,
    val audiogramSensitivities: List<Float> = listOf(0.92f, 0.94f, 0.88f, 0.84f, 0.78f, 0.74f),
    val bioEarAgeYears: Int = 20,
    val bioEarGoldenScore: Int = 92,
    val bioEarDiagnosisSummary: String = "Bio-Ear Age: 20 Yrs (Bat-Ear Golden Hearing) • Max Audible: 18.8kHz",
    val isDynamicAutoRiderEnabled: Boolean = false,
    val autoRiderStatusText: String = "AI AUTO-RIDER STANDBY • Real-time genre & vocal adaptation",
    val isEsportsXrayEnabled: Boolean = false,
    // 8. 3D Multi-Instrument & Virtual 7.1.4 Woofer/Speaker Stage Architect
    val stageNodes: List<SpatialStageNode> = FutureGenAudioLabEngine.defaultStageNodesForFormation(SpatialStageFormation.CONCERT_ARENA),
    val selectedStageNodeId: String = "sub_l",
    val selectedStageFormation: SpatialStageFormation = SpatialStageFormation.CONCERT_ARENA,
    val wooferCrossoverHz: Int = 80,
    val wooferPhaseInvert180: Boolean = false,
    val centerVocalLock: Boolean = true,
    val stageSubPanLR: Float = 0f,
    val stageMidPanLR: Float = 0f,
    val stageHighPanLR: Float = 0f,
    val stageTelemetrySummary: String = "3D STAGE ARCHITECT READY • Drag Woofers, Vocals, Drums, Guitar & Towers anywhere in 360°",
    // 2030 Hyper-Matrix 10-Engine Suite
    val gyroHeadTrackingMode: GyroHeadTrackingMode = GyroHeadTrackingMode.OFF,
    val gyroLiveYawDeg: Float = 0f,
    val subHarmonicSynthPercent: Int = 25, // Missing Fundamental 18" Sub Synthesizer (Smooth 25% default)
    val stemVocalPercent: Int = 100,       // Live 4-Stem De-Mixer: 0..200%
    val stemDrumPercent: Int = 100,
    val stemBassPercent: Int = 100,
    val stemMelodyPercent: Int = 100,
    val selectedStemPreset: StemDeMixerPreset = StemDeMixerPreset.STUDIO_BALANCED,
    val speedCruiseMode: SpeedCruiseMode = SpeedCruiseMode.OFF,
    val analogSaturationMode: AnalogSaturationMode = AnalogSaturationMode.CLEAN_DIGITAL,
    val analogDrivePercent: Int = 35,
    val isTinnitusShieldEnabled: Boolean = false,
    val tinnitusNotchFreqHz: Int = 7500,
    val safeListeningDbText: String = "SAFE DOSE: 74 dB SPL • Zero Ear-Fatigue Guard Ready",
    val crossfeedStrengthPercent: Int = 30,
    val isBassHapticWooferEnabled: Boolean = false,
    val bassHapticIntensity: Int = 80,
    val activeVenueClone: AcousticVenueClone = AcousticVenueClone.NONE,
    val referenceTrackName: String = "No Reference Locked",
    val isReferenceDnaActive: Boolean = false,
    val hyperMatrixSummary: String = "2030 HYPER-MATRIX READY • 18\" SubSynth, 4-Stem De-Mixer, 4D Gyro, Tube & Venue Cloner",
    // Out-Of-Head Room Externalizer (Smyth A16 / OOHL Style)
    val outOfHeadMode: OutOfHeadExternalizerMode = OutOfHeadExternalizerMode.OFF,
    val outOfHeadDistanceMeters: Float = 3.0f,
    val outOfHeadEarlyReflectionPercent: Int = 58,
    val isPinnaChestHrtfEnabled: Boolean = true,
    val isSubFloorCouplerEnabled: Boolean = true,
    val outOfHeadTelemetryText: String = "OUT-OF-HEAD EXTERNALIZER READY • Tap any Room Speaker Mode to move sound outside your ears!",
    // Global Triad Flagship Hi-Fi Suite (🇯🇵 JAPAN • 🇨🇳 CHINA • 🇺🇸 USA)
    val globalTriadMode: GlobalTriadMode = GlobalTriadMode.OFF,
    val japanDseePercent: Int = 78,
    val chinaR2rBerylliumPercent: Int = 82,
    val usaDiracSpherePercent: Int = 85,
    val isFletcherMunsonEnabled: Boolean = true,
    val globalTriadTelemetryText: String = "GLOBAL TRIAD READY • Tap 🇯🇵 Japan DSEE, 🇨🇳 China R2R+Beryllium, 🇺🇸 USA Vegas Sphere or 🌐 Triad Omega",
    // 🛸 2030 Sovereign R&D Lab (🇯🇵 NTT 16Hz + Doppler-0 • 🇨🇳 DSD1024 + Bio De-Esser • 🇺🇸 MIT 64-WFS + De-Squasher)
    val sovereign2030Mode: Sovereign2030RndMode = Sovereign2030RndMode.OFF,
    val phantomSub16HzPercent: Int = 28,
    val isDopplerEraserEnabled: Boolean = true,
    val dsd1024DampingPercent: Int = 85,
    val isBioEarDeEsserEnabled: Boolean = true,
    val mit64WfsWallPercent: Int = 88,
    val streamDeSquasherPercent: Int = 84,
    val sovereign2030TelemetryText: String = "🛸 2030 R&D LAB READY • Tap 🇯🇵 NTT 16Hz, 🇨🇳 DSD1024, 🇺🇸 MIT 64-WFS or 🛸 2030 Singularity",
    // 🧬 Omega-Prime Autonomous Acoustic AI Suite (6-in-1 Master Engine)
    val omegaPrimeMode: OmegaPrimeMode = OmegaPrimeMode.OFF,
    val servoTransducerProfile: ServoTransducerProfile = ServoTransducerProfile.TWS_EARBUD_6MM,
    val dynamicBreathingEqPercent: Int = 88,
    val vocalSidechainDuckPercent: Int = 90,
    val elevationHelixSkyPercent: Int = 86,
    val goldenPhiHarmonicPercent: Int = 84,
    val isBioGrooveCardioSyncEnabled: Boolean = true,
    val omegaPrimeTelemetryText: String = "🧬 OMEGA-PRIME AI READY • Tap 🧬 Omega-Prime AI, 🎤 Vocal Sidechain, 🌀 Sky Helix or ✨ Golden Phi=1.618",
    // 🌬️ Zero-Earbud Invisible Open-Air Engine (6-in-1 Anti-Occlusion & Bionic Vent Suite)
    val zeroEarbudMode: ZeroEarbudOpenAirMode = ZeroEarbudOpenAirMode.OFF,
    val occlusionValvePercent: Int = 92,
    val openCanalUnplugPercent: Int = 90,
    val airCushion25mPercent: Int = 88,
    val openBackElectrostaticPercent: Int = 90,
    val isAntiVacuumBreezeEnabled: Boolean = true,
    val isVestibularMicroDriftEnabled: Boolean = true,
    val zeroEarbudTelemetryText: String = "🌬️ ZERO-EARBUD ENGINE READY • Tap 👂 Bare-Ear 100%, 🎧 HE-1 Open-Back or 🧬 Bionic Open-Air Omega",
    // 🎻 Holographic Live Instrument Realism Engine (6-in-1 Live Stage Physics Suite)
    val liveInstrumentMode: HolographicLiveInstrumentMode = HolographicLiveInstrumentMode.OFF,
    val pluckDrumTransientPercent: Int = 92,
    val woodHelmholtzBodyPercent: Int = 90,
    val sympatheticStringBleedPercent: Int = 88,
    val singerBreathProximityPercent: Int = 92,
    val isHaasStageSeparationEnabled: Boolean = true,
    val isWoodenUnpluggedFloorEnabled: Boolean = true,
    val liveInstrumentTelemetryText: String = "🎻 LIVE INSTRUMENT ENGINE READY • Tap 🎸 MTV/Coke Studio Unplugged, 🥁 Live Drum Kit or 🔥 Live Concert Omega",
    // 🧠 Chrono-Neural & Sub-Quantum Biomimetic Engine (6-in-1 Neuro-Acoustic Suite)
    val chronoNeuralMode: ChronoNeuralBiomimeticMode = ChronoNeuralBiomimeticMode.OFF,
    val cochlearDispersionSyncPercent: Int = 94,
    val gamma40HzTimeDilationPercent: Int = 90,
    val stapediusReflexDisarmerPercent: Int = 92,
    val tartiniOtoacousticSubPercent: Int = 28,
    val isMicroSaccadicStageSwayEnabled: Boolean = true,
    val isFibonacciGoldenTapeSilkEnabled: Boolean = true,
    val chronoNeuralTelemetryText: String = "🧠 CHRONO-NEURAL BIOMIMETIC READY • Tap 🌀 Cochlear 6ms Sync, ⚡ 40Hz Slow-Mo HD, 🛡️ Eardrum Silk or 🧠 Neuro-Omega",
    // 🌌 Apex Singularity & Bio-Acoustic Engine (6-in-1 Ultra-Power Suite)
    val apexSingularityMode: ApexSingularityMode = ApexSingularityMode.OFF,
    val gyroStageAnchorPercent: Int = 95,
    val tactileChestSlamSubPercent: Int = 28,
    val vocalFormantUnmaskerPercent: Int = 92,
    val metamaterialShellCloakPercent: Int = 92,
    val isGoldenPhiTriodeTapeEnabled: Boolean = true,
    val isFrissonVagusResonatorEnabled: Boolean = true,
    val apexSingularityTelemetryText: String = "🌌 APEX SINGULARITY READY • Tap 🧭 Gyro 360° Lock, 💥 18Hz Chest-Slam, 🎙️ Vocal Unmasker or 🌌 Apex Singularity"
)

class EqViewModel(application: Application) : AndroidViewModel(application) {

    private val presetManager: PresetManager
    private val dspEngine: AudioDspEngine
    private val voiceEngine: AiVoiceControlEngine
    val hapticManager: EqHapticFeedbackManager

    private var lastSelectedNonFlatPreset: EqPresetEntity = EqPresetEntity.BUILT_IN_PRESETS[3]

    private val _uiState: MutableStateFlow<EqUiState>
    val uiState: StateFlow<EqUiState>

    val devicePresets: List<EqPresetEntity>
    val allPresets: StateFlow<List<EqPresetEntity>>
    val importedPresets: StateFlow<List<EqPresetEntity>>
    val isPlayingTestLoop: StateFlow<Boolean>
    val isAudioRecordActive: StateFlow<Boolean>
    val isSystemAudioActive: StateFlow<Boolean>
    val systemVolumePercent: StateFlow<Int>
    val waveformSamples: StateFlow<List<Float>>
    val stereoVuLevels: StateFlow<Pair<Float, Float>>
    val spectrumBins: StateFlow<List<Float>>
    val peakHoldBins: StateFlow<List<Float>>
    val dominantFreqHz: StateFlow<Int>
    val peakDb: StateFlow<Float>
    val hardwareStatus: StateFlow<String>

    val isVoiceListening: StateFlow<Boolean>
    val isVoiceHandsFree: StateFlow<Boolean>
    val isVoiceTtsEnabled: StateFlow<Boolean>
    val voiceRmsLevel: StateFlow<Float>
    val voiceLiveTranscript: StateFlow<String>
    val voiceStatusBadge: StateFlow<String>

    val isHapticsEnabled: StateFlow<Boolean>
    val lastHapticEventLabel: StateFlow<String>
    val hapticTriggerCount: StateFlow<Int>

    private val _esportsRadarState = MutableStateFlow(EsportsThreatRadarState())
    val esportsRadarState: StateFlow<EsportsThreatRadarState> = _esportsRadarState.asStateFlow()

    private val serviceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == EqForegroundService.ACTION_SERVICE_STATE_CHANGED) {
                reloadFromPresetManager()
            }
        }
    }

    init {
        val db = EqDatabase.getInstance(application)
        presetManager = PresetManager(application, db.eqPresetDao())
        dspEngine = AudioDspEngine(application, viewModelScope)
        hapticManager = EqHapticFeedbackManager(application)
        hapticManager.setHapticsEnabled(presetManager.isHapticsEnabled())
        voiceEngine = AiVoiceControlEngine(application, viewModelScope) { recognizedSpeech ->
            executeVoiceCommand(recognizedSpeech)
        }
        devicePresets = presetManager.devicePresets

        val restored = presetManager.loadSavedActiveProfile()
        if (!restored.name.equals("Flat", ignoreCase = true)) {
            lastSelectedNonFlatPreset = restored
        }
        val bgEnabled = presetManager.isBackgroundServiceEnabled()
        val proUnlocked = presetManager.isAiProUnlocked()
        val bassLocked = presetManager.isBassLockEnabled()
        _uiState = MutableStateFlow(
            EqUiState(
                isMasterEnabled = presetManager.isDspEnabled(),
                isBackgroundServiceEnabled = bgEnabled,
                isAiProUnlocked = proUnlocked,
                isBassBoostLocked = bassLocked,
                activePresetName = restored.name,
                bandGainsDb = applyBassLockToBands(restored.getBandGains(), restored.bassBoostStrength, bassLocked),
                bassBoostStrength = restored.bassBoostStrength,
                reverbLevel = restored.reverbLevel,
                reverbRoom = restored.reverbRoom,
                spatialLevel = restored.spatialLevel,
                preampDb = restored.preampDb,
                lastAiAnalysis = if (proUnlocked) {
                    "OWNER PRO UNLOCKED 👑 — Tap any AI Scenario Chip, use Voice Control 🎙️, or open AI Studio to auto-tune all 31 bands & DSP."
                } else {
                    "PRO MODE LOCKED 🔒 — Exclusive 31-Band AI Auto-EQ + Voice Control Mastering Engine. Tap UNLOCK PRO (or say 'Unlock Pro 30590') to access."
                }
            )
        )
        uiState = _uiState.asStateFlow()

        allPresets = presetManager.allSoundProfiles
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = EqPresetEntity.BUILT_IN_PRESETS
            )

        importedPresets = presetManager.importedProfiles
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        isPlayingTestLoop = dspEngine.isPlayingTestLoop
        isAudioRecordActive = dspEngine.isAudioRecordActive
        isSystemAudioActive = dspEngine.isSystemAudioActive
        systemVolumePercent = dspEngine.systemVolumePercent
        waveformSamples = dspEngine.waveformSamples
        stereoVuLevels = dspEngine.stereoVuLevels
        spectrumBins = dspEngine.spectrumBins
        peakHoldBins = dspEngine.peakHoldBins
        dominantFreqHz = dspEngine.dominantFreqHz
        peakDb = dspEngine.peakDb
        hardwareStatus = dspEngine.hardwareStatus

        isVoiceListening = voiceEngine.isListening
        isVoiceHandsFree = voiceEngine.isHandsFreeLoop
        isVoiceTtsEnabled = voiceEngine.isVoiceTtsEnabled
        voiceRmsLevel = voiceEngine.voiceRmsLevel
        voiceLiveTranscript = voiceEngine.liveTranscript
        voiceStatusBadge = voiceEngine.voiceStatusBadge

        isHapticsEnabled = hapticManager.isHapticsEnabled
        lastHapticEventLabel = hapticManager.lastHapticEventLabel
        hapticTriggerCount = hapticManager.hapticTriggerCount

        runCatching {
            ContextCompat.registerReceiver(
                application,
                serviceReceiver,
                IntentFilter(EqForegroundService.ACTION_SERVICE_STATE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }

        syncDspEngine()
        startFutureGenCoordinator()
    }

    private fun reloadFromPresetManager() {
        val restored = presetManager.loadSavedActiveProfile()
        val enabled = presetManager.isDspEnabled()
        val bgEnabled = presetManager.isBackgroundServiceEnabled()
        val bassLocked = presetManager.isBassLockEnabled()
        val effectiveBands = applyBassLockToBands(restored.getBandGains(), restored.bassBoostStrength, bassLocked)
        _uiState.update {
            it.copy(
                isMasterEnabled = enabled,
                isBackgroundServiceEnabled = bgEnabled,
                isBassBoostLocked = bassLocked,
                activePresetName = restored.name,
                bandGainsDb = effectiveBands,
                bassBoostStrength = restored.bassBoostStrength,
                reverbLevel = restored.reverbLevel,
                reverbRoom = restored.reverbRoom,
                spatialLevel = restored.spatialLevel,
                preampDb = restored.preampDb
            )
        }
        dspEngine.updateDspState(
            enabled = enabled,
            bandsDb = effectiveBands,
            bass = restored.bassBoostStrength,
            reverb = restored.reverbLevel,
            reverbRoom = restored.reverbRoom,
            spatial = restored.spatialLevel,
            preamp = restored.preampDb,
            limiter = _uiState.value.softLimiterEnabled
        )
    }

    private fun resolveBass(candidateBass: Int, state: EqUiState = _uiState.value): Int {
        return if (state.isBassBoostLocked) {
            state.bassBoostStrength.coerceIn(0, 100)
        } else {
            candidateBass.coerceIn(0, 100)
        }
    }

    private fun applyBassLockToBands(
        bands: List<Float>,
        lockedBass: Int = _uiState.value.bassBoostStrength,
        isLocked: Boolean = _uiState.value.isBassBoostLocked
    ): List<Float> {
        val interpolated = EqPresetEntity.interpolateTo31Bands(bands)
        if (!isLocked) return interpolated
        // When Bass Lock is ON, cap excessive sub-bass spikes on bands 0..8 (20Hz..125Hz)
        // proportional to the user's chosen Bass Boost slider so modes never secretly boom.
        val maxAllowedSubDb = ((lockedBass / 100f) * 8.5f + 1.0f).coerceIn(1.0f, 12.0f)
        return interpolated.mapIndexed { idx, db ->
            if (idx <= 8 && db > maxAllowedSubDb) {
                val blend = (idx / 9f).coerceIn(0f, 1f)
                val capped = maxAllowedSubDb * (1f - blend * 0.35f) + db * (blend * 0.35f)
                (kotlin.math.round(capped * 10f) / 10f).coerceIn(-15f, 15f)
            } else {
                db
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        val clamped = tabIndex.coerceIn(0, 4)
        if (_uiState.value.selectedTab != clamped) {
            hapticManager.onControlTick("Tab $clamped")
        }
        _uiState.update { it.copy(selectedTab = clamped) }
    }

    fun selectBandZone(zoneIndex: Int) {
        val clamped = zoneIndex.coerceIn(0, 4)
        if (_uiState.value.selectedBandZone != clamped) {
            hapticManager.onControlTick("Band Zone $clamped")
        }
        _uiState.update { it.copy(selectedBandZone = clamped) }
    }

    fun toggleHapticsEnabled() {
        val next = !isHapticsEnabled.value
        presetManager.setHapticsEnabled(next)
        hapticManager.setHapticsEnabled(next)
    }

    fun updateDeviceSearchQuery(query: String) {
        _uiState.update { it.copy(deviceSearchQuery = query) }
    }

    fun toggleMasterEnabled() {
        val nextEnabled = !_uiState.value.isMasterEnabled
        _uiState.update { it.copy(isMasterEnabled = nextEnabled) }
        hapticManager.onPresetToggled(nextEnabled, _uiState.value.activePresetName)
        syncDspEngine()
    }

    fun setMasterEnabled(enabled: Boolean) {
        val changed = _uiState.value.isMasterEnabled != enabled
        _uiState.update { it.copy(isMasterEnabled = enabled) }
        if (changed) {
            hapticManager.onPresetToggled(enabled, _uiState.value.activePresetName)
        }
        syncDspEngine()
    }

    fun setBackgroundServiceEnabled(enabled: Boolean) {
        presetManager.setBackgroundServiceEnabled(enabled)
        _uiState.update { it.copy(isBackgroundServiceEnabled = enabled) }
        hapticManager.onControlTick(if (enabled) "Background DSP Active" else "Background DSP Off")
        val app = getApplication<Application>()
        if (enabled) {
            EqForegroundService.startOrUpdate(app)
        } else {
            EqForegroundService.stop(app)
        }
    }

    fun updateBandGain(bandIndex: Int, gainDb: Float) {
        if (bandIndex !in 0 until EqPresetEntity.BAND_COUNT) return
        val clamped = (kotlin.math.round(gainDb * 2f) / 2f).coerceIn(-15f, 15f)
        val oldGain = _uiState.value.bandGainsDb.getOrElse(bandIndex) { 0f }
        if (EqHapticFeedbackManager.didCrossZeroDb(oldGain, clamped)) {
            hapticManager.onControlTick(
                label = "${EqPresetEntity.FREQUENCY_LABELS.getOrElse(bandIndex) { "Band" }} 0.0dB",
                isZeroDbSnap = true
            )
        }
        _uiState.update { state ->
            val updated = EqPresetEntity.interpolateTo31Bands(state.bandGainsDb).toMutableList()
            updated[bandIndex] = clamped
            state.copy(
                bandGainsDb = updated,
                activePresetName = "Custom Curve"
            )
        }
        syncDspEngine()
    }

    fun resetToFlat() {
        val flatPreset = EqPresetEntity.BUILT_IN_PRESETS.first()
        _uiState.update { state ->
            state.copy(
                isMasterEnabled = true,
                activePresetName = flatPreset.name,
                bandGainsDb = flatPreset.getBandGains(),
                bassBoostStrength = flatPreset.bassBoostStrength,
                reverbLevel = flatPreset.reverbLevel,
                reverbRoom = flatPreset.reverbRoom,
                spatialLevel = flatPreset.spatialLevel,
                preampDb = flatPreset.preampDb
            )
        }
        hapticManager.onPresetToggled(false, "Flat Reset")
        syncDspEngine()
    }

    fun updateBassBoost(strength: Int) {
        val clamped = strength.coerceIn(0, 100)
        presetManager.setUserPreferredBass(clamped)
        _uiState.update { state ->
            state.copy(
                bassBoostStrength = clamped,
                bandGainsDb = applyBassLockToBands(state.bandGainsDb, clamped, state.isBassBoostLocked)
            )
        }
        syncDspEngine()
    }

    fun toggleBassBoostLock() {
        val next = !_uiState.value.isBassBoostLocked
        setBassBoostLock(next)
    }

    fun setBassBoostLock(locked: Boolean) {
        presetManager.setBassLockEnabled(locked)
        if (locked) {
            presetManager.setUserPreferredBass(_uiState.value.bassBoostStrength)
        }
        _uiState.update { state ->
            state.copy(
                isBassBoostLocked = locked,
                bandGainsDb = applyBassLockToBands(state.bandGainsDb, state.bassBoostStrength, locked)
            )
        }
        hapticManager.onPresetToggled(locked, if (locked) "Bass Memory Locked 🔒" else "Bass Memory Unlocked 🔓")
        syncDspEngine()
    }

    fun updateSpatialLevel(level: Int) {
        _uiState.update { it.copy(spatialLevel = level.coerceIn(0, 100)) }
        syncDspEngine()
    }

    fun updateReverbLevel(level: Int) {
        _uiState.update { it.copy(reverbLevel = level.coerceIn(0, 100)) }
        syncDspEngine()
    }

    fun updateReverbRoom(room: String) {
        if (_uiState.value.reverbRoom != room) {
            hapticManager.onControlTick("Reverb: $room")
        }
        _uiState.update { it.copy(reverbRoom = room) }
        syncDspEngine()
    }

    fun updatePreampDb(db: Float) {
        val clamped = (kotlin.math.round(db * 2f) / 2f).coerceIn(-12f, 12f)
        val oldDb = _uiState.value.preampDb
        if (EqHapticFeedbackManager.didCrossZeroDb(oldDb, clamped)) {
            hapticManager.onControlTick("Preamp 0.0dB", isZeroDbSnap = true)
        }
        _uiState.update { it.copy(preampDb = clamped) }
        syncDspEngine()
    }

    fun toggleSoftLimiter() {
        val next = !_uiState.value.softLimiterEnabled
        _uiState.update { it.copy(softLimiterEnabled = next) }
        hapticManager.onPresetToggled(next, "Soft-Clip Limiter")
        syncDspEngine()
    }

    /**
     * Selects or toggles an equalizer preset with acoustic-tailored tactile haptic feedback:
     * - If the tapped preset is not currently active (or master EQ is bypassed), it activates
     *   the preset and fires an acoustic-matched selection haptic pulse.
     * - If the tapped preset is already active and toggleIfAlreadyActive is true, it toggles
     *   between that preset and Flat bypass (or restores the last active preset if Flat was tapped),
     *   firing a tactile toggle ON/OFF haptic snap.
     */
    fun applyPreset(preset: EqPresetEntity, toggleIfAlreadyActive: Boolean = true) {
        val currentState = _uiState.value
        val isSamePreset = currentState.activePresetName.equals(preset.name, ignoreCase = true)

        if (isSamePreset && currentState.isMasterEnabled && toggleIfAlreadyActive) {
            val flatPreset = EqPresetEntity.BUILT_IN_PRESETS.first()
            if (!preset.name.equals(flatPreset.name, ignoreCase = true)) {
                lastSelectedNonFlatPreset = preset
                _uiState.update { state ->
                    state.copy(
                        activePresetName = flatPreset.name,
                        bandGainsDb = flatPreset.getBandGains(),
                        bassBoostStrength = resolveBass(flatPreset.bassBoostStrength, state),
                        reverbLevel = flatPreset.reverbLevel,
                        reverbRoom = flatPreset.reverbRoom,
                        spatialLevel = flatPreset.spatialLevel,
                        preampDb = flatPreset.preampDb
                    )
                }
                hapticManager.onPresetToggled(enabled = false, presetName = preset.name)
                syncDspEngine()
                return
            } else if (!lastSelectedNonFlatPreset.name.equals(flatPreset.name, ignoreCase = true)) {
                val restored = lastSelectedNonFlatPreset
                _uiState.update { state ->
                    val effectiveBass = resolveBass(restored.bassBoostStrength, state)
                    state.copy(
                        isMasterEnabled = true,
                        activePresetName = restored.name,
                        bandGainsDb = applyBassLockToBands(restored.getBandGains(), effectiveBass, state.isBassBoostLocked),
                        bassBoostStrength = effectiveBass,
                        reverbLevel = restored.reverbLevel,
                        reverbRoom = restored.reverbRoom,
                        spatialLevel = restored.spatialLevel,
                        preampDb = restored.preampDb
                    )
                }
                hapticManager.onPresetToggled(enabled = true, presetName = restored.name)
                syncDspEngine()
                return
            }
        }

        if (!preset.name.equals("Flat", ignoreCase = true)) {
            lastSelectedNonFlatPreset = preset
        }

        _uiState.update { state ->
            val effectiveBass = resolveBass(preset.bassBoostStrength, state)
            state.copy(
                isMasterEnabled = true,
                activePresetName = preset.name,
                bandGainsDb = applyBassLockToBands(preset.getBandGains(), effectiveBass, state.isBassBoostLocked),
                bassBoostStrength = effectiveBass,
                reverbLevel = preset.reverbLevel,
                reverbRoom = preset.reverbRoom,
                spatialLevel = preset.spatialLevel,
                preampDb = preset.preampDb
            )
        }
        hapticManager.onPresetSelected(preset)
        syncDspEngine()
    }

    fun setSavePresetDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showSavePresetDialog = visible) }
    }

    fun setPasteImportDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showPasteImportDialog = visible) }
    }

    fun setAiAutoEqDialogVisible(visible: Boolean) {
        if (visible && !_uiState.value.isAiProUnlocked) {
            _uiState.update { it.copy(showAiProUnlockDialog = true) }
            return
        }
        _uiState.update { it.copy(showAiAutoEqDialog = visible) }
    }

    fun setAiProUnlockDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showAiProUnlockDialog = visible) }
    }

    fun setVoiceControlDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showVoiceControlDialog = visible) }
    }

    fun startVoiceListening() {
        voiceEngine.startListening()
    }

    fun stopVoiceListening() {
        voiceEngine.stopListening()
    }

    fun toggleVoiceHandsFree() {
        val next = !isVoiceHandsFree.value
        voiceEngine.setHandsFreeLoop(next)
        if (next && !isVoiceListening.value) {
            voiceEngine.startListening()
        } else if (!next && isVoiceListening.value) {
            voiceEngine.stopListening()
        }
    }

    fun toggleVoiceTts() {
        voiceEngine.setVoiceTtsEnabled(!isVoiceTtsEnabled.value)
    }

    fun executeVoiceCommand(
        rawSpokenText: String,
        onFeedbackMessage: (String) -> Unit = {}
    ) {
        val cleanText = rawSpokenText.trim()
        if (cleanText.isEmpty()) return

        val currentState = _uiState.value
        val parsed = AiVoiceControlEngine.parseVoiceCommand(
            rawCommand = cleanText,
            currentBands = currentState.bandGainsDb,
            devicePresets = devicePresets,
            allPresets = allPresets.value
        )

        var unlockedNow = currentState.isAiProUnlocked

        for (action in parsed.actions) {
            when (action) {
                is VoiceControlAction.UnlockProWithKey -> {
                    val ok = tryUnlockAiProMode(action.key)
                    unlockedNow = ok
                }
                is VoiceControlAction.LockProMode -> {
                    lockAiProMode()
                    unlockedNow = false
                }
                is VoiceControlAction.SelectTab -> {
                    selectTab(action.tabIndex)
                }
                is VoiceControlAction.SetMasterEnabled -> {
                    setMasterEnabled(action.enabled)
                }
                is VoiceControlAction.SetBackgroundService -> {
                    setBackgroundServiceEnabled(action.enabled)
                }
                is VoiceControlAction.SyncYouTube -> {
                    rehookYouTubeAudio()
                }
                is VoiceControlAction.SetTestAudioLoop -> {
                    if (isPlayingTestLoop.value != action.play) {
                        toggleTestAudioLoop()
                    }
                }
                is VoiceControlAction.SetMicSpectrum -> {
                    setAudioRecordSpectrumEnabled(action.enabled)
                }
                is VoiceControlAction.SetBassBoost -> {
                    updateBassBoost(action.strength)
                }
                is VoiceControlAction.AdjustBassBoost -> {
                    updateBassBoost((_uiState.value.bassBoostStrength + action.delta).coerceIn(0, 100))
                }
                is VoiceControlAction.SetSpatial3D -> {
                    updateSpatialLevel(action.level)
                }
                is VoiceControlAction.AdjustSpatial3D -> {
                    updateSpatialLevel((_uiState.value.spatialLevel + action.delta).coerceIn(0, 100))
                }
                is VoiceControlAction.SetReverbLevel -> {
                    updateReverbLevel(action.level)
                }
                is VoiceControlAction.SetReverbRoom -> {
                    updateReverbRoom(action.room)
                    action.level?.let { updateReverbLevel(it) }
                }
                is VoiceControlAction.SetPreampDb -> {
                    updatePreampDb(action.db)
                }
                is VoiceControlAction.SetSoftLimiter -> {
                    if (_uiState.value.softLimiterEnabled != action.enabled) {
                        toggleSoftLimiter()
                    }
                }
                is VoiceControlAction.SetSingleBand -> {
                    updateBandGain(action.bandIndex, action.gainDb)
                }
                is VoiceControlAction.ApplyCurveOffsets -> {
                    _uiState.update {
                        it.copy(
                            bandGainsDb = action.targetBands,
                            activePresetName = "Voice: ${action.label}"
                        )
                    }
                    syncDspEngine()
                }
                is VoiceControlAction.ResetFlat -> {
                    resetToFlat()
                }
                is VoiceControlAction.ApplyPreset -> {
                    applyPreset(action.preset)
                }
                is VoiceControlAction.RunAiAutoEq -> {
                    if (action.deviceHint != null) {
                        updateAiSelectedDevice(action.deviceHint)
                    }
                    if (!unlockedNow) {
                        // Automatically unlock for Voice AI command or prompt if locked
                        _uiState.update {
                            it.copy(
                                aiPromptInput = action.prompt,
                                showAiProUnlockDialog = true,
                                lastVoiceCommandSummary = "🔒 PRO Locked: Say 'Unlock Pro 30590' or enter Owner Key to run AI Auto-EQ"
                            )
                        }
                        voiceEngine.speakFeedback("A I Auto E Q is locked in Pro Mode. Say Unlock Pro 3 0 5 9 0 to unlock.")
                        onFeedbackMessage("PRO Mode Locked 🔒 — Say 'Unlock Pro 30590' or unlock with Owner PIN!")
                        return
                    } else {
                        generateAndApplyAiAutoEq(
                            overridePrompt = action.prompt,
                            saveToLibrary = true
                        ) { aiPreset ->
                            voiceEngine.speakFeedback("Applied ${aiPreset.name}. Bass ${aiPreset.bassBoostStrength} percent, Spatial ${aiPreset.spatialLevel} percent.")
                            onFeedbackMessage("🎙️ Voice AI Auto-EQ Applied: ${aiPreset.name}")
                        }
                    }
                }
            }
        }

        val logEntry = "🎙️ \"${parsed.spokenTranscript}\" → ${parsed.actionTitle}"
        _uiState.update { state ->
            val updatedHistory = (listOf(logEntry) + state.voiceCommandHistory).take(8)
            state.copy(
                lastVoiceCommandSummary = "${parsed.actionTitle} (\"${parsed.spokenTranscript}\")",
                voiceCommandHistory = updatedHistory
            )
        }

        if (parsed.actions.none { it is VoiceControlAction.RunAiAutoEq }) {
            voiceEngine.speakFeedback(parsed.spokenFeedback)
            onFeedbackMessage("🎙️ ${parsed.actionTitle}")
        }
    }

    fun tryUnlockAiProMode(ownerKeyInput: String): Boolean {
        val valid = presetManager.verifyOwnerProAccess(ownerKeyInput)
        if (valid) {
            presetManager.setAiProUnlocked(true)
            _uiState.update {
                it.copy(
                    isAiProUnlocked = true,
                    showAiProUnlockDialog = false,
                    lastAiAnalysis = "OWNER PRO UNLOCKED 👑 — Tap any AI Scenario Chip, use Voice Control 🎙️, or open AI Studio to auto-tune all 10 bands & DSP."
                )
            }
        }
        return valid
    }

    fun lockAiProMode() {
        presetManager.setAiProUnlocked(false)
        _uiState.update {
            it.copy(
                isAiProUnlocked = false,
                showAiAutoEqDialog = false,
                lastAiAnalysis = "PRO MODE LOCKED 🔒 — Exclusive AI Auto-EQ + Voice Control Mastering Engine. Tap UNLOCK PRO (Owner Only) to access."
            )
        }
    }

    fun updateAiPromptInput(prompt: String) {
        _uiState.update { it.copy(aiPromptInput = prompt) }
    }

    fun updateAiSelectedDevice(device: String) {
        _uiState.update { it.copy(aiSelectedDevice = device) }
    }

    fun generateAndApplyAiAutoEq(
        overridePrompt: String? = null,
        saveToLibrary: Boolean = true,
        onComplete: (EqPresetEntity) -> Unit = {}
    ) {
        val state = _uiState.value
        if (!state.isAiProUnlocked) {
            _uiState.update { it.copy(showAiProUnlockDialog = true) }
            return
        }
        val promptToUse = (overridePrompt ?: state.aiPromptInput).trim()
            .ifEmpty { "Live Spectrum Smart Calibration" }

        _uiState.update {
            it.copy(
                isGeneratingAiEq = true,
                aiPromptInput = promptToUse
            )
        }

        viewModelScope.launch {
            val result = AiAutoEqEngine.generateAiPreset(
                userPrompt = promptToUse,
                targetDeviceName = state.aiSelectedDevice,
                spectrumBins = spectrumBins.value,
                dominantFreqHz = dominantFreqHz.value,
                peakDb = peakDb.value
            )

            val finalPreset = if (saveToLibrary) {
                presetManager.saveCustomSoundProfile(
                    name = result.preset.name,
                    description = result.preset.description,
                    bandGainsDb = result.preset.getBandGains(),
                    bassBoostStrength = result.preset.bassBoostStrength,
                    reverbLevel = result.preset.reverbLevel,
                    reverbRoom = result.preset.reverbRoom,
                    spatialLevel = result.preset.spatialLevel,
                    preampDb = result.preset.preampDb
                )
            } else {
                result.preset
            }

            _uiState.update { s ->
                val effectiveBass = resolveBass(finalPreset.bassBoostStrength, s)
                s.copy(
                    isGeneratingAiEq = false,
                    isMasterEnabled = true,
                    activePresetName = finalPreset.name,
                    bandGainsDb = applyBassLockToBands(finalPreset.getBandGains(), effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    reverbLevel = finalPreset.reverbLevel,
                    reverbRoom = finalPreset.reverbRoom,
                    spatialLevel = finalPreset.spatialLevel,
                    preampDb = finalPreset.preampDb,
                    lastAiAnalysis = result.acousticAnalysis,
                    lastAiEngineBadge = result.sourceEngine
                )
            }
            hapticManager.onAiPresetApplied(finalPreset.name)
            syncDspEngine()
            onComplete(finalPreset)
        }
    }

    fun saveCurrentAsCustomPreset(name: String, description: String) {
        val state = _uiState.value
        viewModelScope.launch {
            val saved = presetManager.saveCustomSoundProfile(
                name = name,
                description = description,
                bandGainsDb = state.bandGainsDb,
                bassBoostStrength = state.bassBoostStrength,
                reverbLevel = state.reverbLevel,
                reverbRoom = state.reverbRoom,
                spatialLevel = state.spatialLevel,
                preampDb = state.preampDb
            )
            _uiState.update {
                it.copy(
                    activePresetName = saved.name,
                    showSavePresetDialog = false
                )
            }
            if (_uiState.value.isBackgroundServiceEnabled) {
                EqForegroundService.startOrUpdate(getApplication())
            }
        }
    }

    fun importPresetFromContent(
        rawContent: String,
        fallbackFileName: String = "Imported Preset",
        onResult: (EqPresetEntity?) -> Unit = {}
    ) {
        val parsed = PresetImportExportParser.parseImportedContent(rawContent, fallbackFileName)
        if (parsed == null) {
            onResult(null)
            return
        }
        viewModelScope.launch {
            val saved = presetManager.saveImportedSoundProfile(parsed)
            _uiState.update {
                it.copy(
                    activePresetName = saved.name,
                    bandGainsDb = saved.getBandGains(),
                    bassBoostStrength = saved.bassBoostStrength,
                    reverbLevel = saved.reverbLevel,
                    reverbRoom = saved.reverbRoom,
                    spatialLevel = saved.spatialLevel,
                    preampDb = saved.preampDb,
                    showPasteImportDialog = false
                )
            }
            syncDspEngine()
            onResult(saved)
        }
    }

    fun buildCurrentPresetExportJson(): String {
        val s = _uiState.value
        val snapshot = EqPresetEntity(
            name = s.activePresetName,
            category = "Custom",
            description = "Exported from Equltra",
            bandsCsv = s.bandGainsDb.joinToString(",") { String.format(Locale.US, "%.1f", it) },
            bassBoostStrength = s.bassBoostStrength,
            reverbLevel = s.reverbLevel,
            reverbRoom = s.reverbRoom,
            spatialLevel = s.spatialLevel,
            preampDb = s.preampDb
        )
        return snapshot.toExportJson()
    }

    fun deleteCustomPreset(preset: EqPresetEntity) {
        if (preset.isBuiltIn) return
        viewModelScope.launch {
            presetManager.deleteCustomSoundProfile(preset)
        }
    }

    fun toggleTestAudioLoop() {
        dspEngine.toggleTestAudioLoop()
    }

    fun setAudioRecordSpectrumEnabled(enable: Boolean) {
        dspEngine.toggleAudioRecordSpectrum(enable)
    }

    fun rehookYouTubeAudio() {
        dspEngine.forceRehookYouTubeAudio()
        if (_uiState.value.isBackgroundServiceEnabled) {
            EqForegroundService.startOrUpdate(getApplication())
        }
    }

    fun setFutureLabDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showFutureLabDialog = visible) }
    }

    fun runSonarAcousticScan(onDone: (String) -> Unit = {}) {
        if (_uiState.value.isRunningSonarScan) return
        _uiState.update {
            it.copy(
                isRunningSonarScan = true,
                sonarTelemetryText = "🔊 EMITTING 45Hz–12.8kHz LOGARITHMIC SONAR SWEEP..."
            )
        }
        viewModelScope.launch {
            val report = FutureGenAudioLabEngine.runSonarAcousticCalibration(
                liveSpectrumBins = spectrumBins.value,
                targetDeviceName = _uiState.value.aiSelectedDevice
            )
            _uiState.update { s ->
                val effectiveBass = resolveBass(report.recommendedBass, s)
                s.copy(
                    isRunningSonarScan = false,
                    isMasterEnabled = true,
                    activePresetName = "Sonar Room Calibrated",
                    bandGainsDb = applyBassLockToBands(report.compensationBands31, effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    spatialLevel = report.recommendedSpatial,
                    reverbRoom = report.recommendedReverbRoom,
                    sonarTelemetryText = report.telemetrySummary
                )
            }
            hapticManager.onAiPresetApplied("Sonar Room Calibrated")
            syncDspEngine()
            onDone(report.telemetrySummary)
        }
    }

    fun setNeuralBrainwaveMode(mode: NeuralBrainwaveMode) {
        _uiState.update { it.copy(neuralBrainwaveMode = mode) }
        hapticManager.onControlTick("Brainwave: ${mode.title}")
        syncDspEngine()
    }

    fun updateRadarPosition(azimuthDeg: Float, distanceNorm: Float) {
        _uiState.update {
            it.copy(
                radarAzimuthDeg = azimuthDeg.coerceIn(-180f, 180f),
                radarDistanceNorm = distanceNorm.coerceIn(0.15f, 1.0f)
            )
        }
        syncDspEngine()
    }

    fun toggleRadarAutoOrbit() {
        val next = !_uiState.value.isRadarAutoOrbitEnabled
        _uiState.update { it.copy(isRadarAutoOrbitEnabled = next) }
        hapticManager.onPresetToggled(next, "360° Auto Orbit")
    }

    fun updateLeftRightBalance(balanceDb: Float) {
        val clamped = (kotlin.math.round(balanceDb * 2f) / 2f).coerceIn(-6f, 6f)
        _uiState.update { it.copy(leftRightBalanceDb = clamped) }
        syncDspEngine()
    }

    fun playAudiogramCalibrationTone(freqHz: Int) {
        viewModelScope.launch {
            FutureGenAudioLabEngine.playPureAudiogramTone(freqHz)
        }
    }

    fun updateAudiogramSensitivity(index: Int, rating: Float) {
        if (index !in 0..5) return
        _uiState.update { state ->
            val list = state.audiogramSensitivities.toMutableList()
            if (list.size == 6) {
                list[index] = rating.coerceIn(0.2f, 1.0f)
            }
            state.copy(audiogramSensitivities = list)
        }
    }

    fun applyBioEarDnaProfile(onDone: (String) -> Unit = {}) {
        val s = _uiState.value
        val profile = FutureGenAudioLabEngine.buildBioEarDnaProfile(
            sensitivityRatings = s.audiogramSensitivities,
            leftRightBiasDb = s.leftRightBalanceDb
        )
        _uiState.update {
            it.copy(
                isMasterEnabled = true,
                activePresetName = "Bio-Ear DNA (${profile.estimatedEarAgeYears}Y)",
                bandGainsDb = profile.dnaCompensationBands31,
                bioEarAgeYears = profile.estimatedEarAgeYears,
                bioEarGoldenScore = profile.goldenEarScorePercent,
                bioEarDiagnosisSummary = profile.diagnosisSummary
            )
        }
        hapticManager.onAiPresetApplied("Bio-Ear DNA")
        syncDspEngine()
        onDone(profile.diagnosisSummary)
    }

    fun toggleDynamicAutoRider() {
        val next = !_uiState.value.isDynamicAutoRiderEnabled
        _uiState.update {
            it.copy(
                isDynamicAutoRiderEnabled = next,
                autoRiderStatusText = if (next) {
                    "AI RIDER ACTIVE • Real-time spectral vocal & bass morphing"
                } else {
                    "AI AUTO-RIDER STANDBY • Real-time genre & vocal adaptation"
                }
            )
        }
        hapticManager.onPresetToggled(next, "Dynamic AI Auto-Rider")
    }

    fun toggleEsportsXray() {
        val next = !_uiState.value.isEsportsXrayEnabled
        if (next) {
            _uiState.update { s ->
                val effectiveBass = resolveBass(34, s)
                s.copy(
                    isEsportsXrayEnabled = true,
                    isMasterEnabled = true,
                    activePresetName = "Esports Tactical X-Ray",
                    bandGainsDb = applyBassLockToBands(FutureGenAudioLabEngine.getEsportsTacticalXrayBands(), effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    spatialLevel = 92,
                    reverbRoom = "None",
                    reverbLevel = 0
                )
            }
        } else {
            _uiState.update { it.copy(isEsportsXrayEnabled = false) }
        }
        hapticManager.onPresetToggled(next, "Esports Tactical X-Ray")
        syncDspEngine()
    }

    fun applyHolographicVibe(vibe: HolographicVibeProfile) {
        _uiState.update { s ->
            val effectiveBass = resolveBass(vibe.bassBoost, s)
            s.copy(
                isMasterEnabled = true,
                activePresetName = vibe.title,
                bandGainsDb = applyBassLockToBands(vibe.bands31, effectiveBass, s.isBassBoostLocked),
                bassBoostStrength = effectiveBass,
                spatialLevel = vibe.spatial3D,
                reverbLevel = vibe.reverbLevel,
                reverbRoom = vibe.reverbRoom,
                preampDb = vibe.preampDb,
                neuralBrainwaveMode = vibe.brainwaveMode,
                radarAzimuthDeg = vibe.radarAzimuthDeg
            )
        }
        hapticManager.onAiPresetApplied(vibe.title)
        syncDspEngine()
    }

    fun selectStageNode(nodeId: String) {
        _uiState.update { it.copy(selectedStageNodeId = nodeId) }
        hapticManager.onControlTick("Node: $nodeId")
    }

    fun updateStageNodePosition(nodeId: String, azimuthDeg: Float, distanceNorm: Float) {
        _uiState.update { state ->
            val updatedNodes = state.stageNodes.map { node ->
                if (node.id == nodeId) {
                    node.copy(
                        azimuthDeg = azimuthDeg.coerceIn(-180f, 180f),
                        distanceNorm = distanceNorm.coerceIn(0.15f, 1.0f)
                    )
                } else node
            }
            val synth = FutureGenAudioLabEngine.synthesizeMultiNodeStage(
                nodes = updatedNodes,
                wooferCrossoverHz = state.wooferCrossoverHz,
                wooferPhaseInvert180 = state.wooferPhaseInvert180,
                centerVocalLock = state.centerVocalLock
            )
            val effectiveBass = resolveBass(synth.effectiveBassBoost, state)
            state.copy(
                isMasterEnabled = true,
                activePresetName = "3D Stage: ${state.selectedStageFormation.title}",
                stageNodes = updatedNodes,
                bandGainsDb = applyBassLockToBands(synth.sculptedBands31, effectiveBass, state.isBassBoostLocked),
                bassBoostStrength = effectiveBass,
                spatialLevel = synth.effectiveSpatial3D,
                reverbLevel = synth.effectiveReverbMix,
                stageSubPanLR = synth.subBandPanLR,
                stageMidPanLR = synth.midBandPanLR,
                stageHighPanLR = synth.highBandPanLR,
                stageTelemetrySummary = synth.telemetrySummary
            )
        }
        syncDspEngine()
    }

    fun updateStageNodeGain(nodeId: String, gainDb: Float) {
        val clamped = (kotlin.math.round(gainDb * 2f) / 2f).coerceIn(-12f, 12f)
        _uiState.update { state ->
            val updatedNodes = state.stageNodes.map { node ->
                if (node.id == nodeId) node.copy(gainDb = clamped) else node
            }
            val synth = FutureGenAudioLabEngine.synthesizeMultiNodeStage(
                nodes = updatedNodes,
                wooferCrossoverHz = state.wooferCrossoverHz,
                wooferPhaseInvert180 = state.wooferPhaseInvert180,
                centerVocalLock = state.centerVocalLock
            )
            val effectiveBass = resolveBass(synth.effectiveBassBoost, state)
            state.copy(
                isMasterEnabled = true,
                activePresetName = "3D Stage Custom",
                stageNodes = updatedNodes,
                bandGainsDb = applyBassLockToBands(synth.sculptedBands31, effectiveBass, state.isBassBoostLocked),
                bassBoostStrength = effectiveBass,
                spatialLevel = synth.effectiveSpatial3D,
                reverbLevel = synth.effectiveReverbMix,
                stageSubPanLR = synth.subBandPanLR,
                stageMidPanLR = synth.midBandPanLR,
                stageHighPanLR = synth.highBandPanLR,
                stageTelemetrySummary = synth.telemetrySummary
            )
        }
        syncDspEngine()
    }

    fun toggleStageNodeMute(nodeId: String) {
        _uiState.update { state ->
            val updatedNodes = state.stageNodes.map { node ->
                if (node.id == nodeId) node.copy(muted = !node.muted) else node
            }
            val synth = FutureGenAudioLabEngine.synthesizeMultiNodeStage(
                nodes = updatedNodes,
                wooferCrossoverHz = state.wooferCrossoverHz,
                wooferPhaseInvert180 = state.wooferPhaseInvert180,
                centerVocalLock = state.centerVocalLock
            )
            state.copy(
                stageNodes = updatedNodes,
                bandGainsDb = synth.sculptedBands31,
                stageSubPanLR = synth.subBandPanLR,
                stageMidPanLR = synth.midBandPanLR,
                stageHighPanLR = synth.highBandPanLR,
                stageTelemetrySummary = synth.telemetrySummary
            )
        }
        hapticManager.onControlTick("Mute $nodeId")
        syncDspEngine()
    }

    fun toggleStageNodeSolo(nodeId: String) {
        _uiState.update { state ->
            val updatedNodes = state.stageNodes.map { node ->
                if (node.id == nodeId) node.copy(solo = !node.solo) else node
            }
            val synth = FutureGenAudioLabEngine.synthesizeMultiNodeStage(
                nodes = updatedNodes,
                wooferCrossoverHz = state.wooferCrossoverHz,
                wooferPhaseInvert180 = state.wooferPhaseInvert180,
                centerVocalLock = state.centerVocalLock
            )
            state.copy(
                stageNodes = updatedNodes,
                bandGainsDb = synth.sculptedBands31,
                stageSubPanLR = synth.subBandPanLR,
                stageMidPanLR = synth.midBandPanLR,
                stageHighPanLR = synth.highBandPanLR,
                stageTelemetrySummary = synth.telemetrySummary
            )
        }
        hapticManager.onControlTick("Solo $nodeId")
        syncDspEngine()
    }

    fun applyStageFormation(formation: SpatialStageFormation) {
        val nodes = FutureGenAudioLabEngine.defaultStageNodesForFormation(formation)
        val s = _uiState.value
        val synth = FutureGenAudioLabEngine.synthesizeMultiNodeStage(
            nodes = nodes,
            wooferCrossoverHz = s.wooferCrossoverHz,
            wooferPhaseInvert180 = s.wooferPhaseInvert180,
            centerVocalLock = s.centerVocalLock
        )
        _uiState.update { state ->
            val effectiveBass = resolveBass(synth.effectiveBassBoost, state)
            state.copy(
                isMasterEnabled = true,
                activePresetName = "Stage: ${formation.title}",
                selectedStageFormation = formation,
                stageNodes = nodes,
                bandGainsDb = applyBassLockToBands(synth.sculptedBands31, effectiveBass, state.isBassBoostLocked),
                bassBoostStrength = effectiveBass,
                spatialLevel = synth.effectiveSpatial3D,
                reverbLevel = synth.effectiveReverbMix,
                stageSubPanLR = synth.subBandPanLR,
                stageMidPanLR = synth.midBandPanLR,
                stageHighPanLR = synth.highBandPanLR,
                stageTelemetrySummary = synth.telemetrySummary
            )
        }
        hapticManager.onAiPresetApplied(formation.title)
        syncDspEngine()
    }

    fun updateWooferCrossoverHz(hz: Int) {
        val clamped = hz.coerceIn(40, 120)
        _uiState.update { state ->
            val synth = FutureGenAudioLabEngine.synthesizeMultiNodeStage(
                nodes = state.stageNodes,
                wooferCrossoverHz = clamped,
                wooferPhaseInvert180 = state.wooferPhaseInvert180,
                centerVocalLock = state.centerVocalLock
            )
            state.copy(
                wooferCrossoverHz = clamped,
                bandGainsDb = synth.sculptedBands31,
                stageTelemetrySummary = synth.telemetrySummary
            )
        }
        syncDspEngine()
    }

    fun toggleWooferPhaseInvert() {
        _uiState.update { state ->
            val next = !state.wooferPhaseInvert180
            val synth = FutureGenAudioLabEngine.synthesizeMultiNodeStage(
                nodes = state.stageNodes,
                wooferCrossoverHz = state.wooferCrossoverHz,
                wooferPhaseInvert180 = next,
                centerVocalLock = state.centerVocalLock
            )
            state.copy(
                wooferPhaseInvert180 = next,
                bandGainsDb = synth.sculptedBands31,
                stageTelemetrySummary = synth.telemetrySummary
            )
        }
        hapticManager.onControlTick("Woofer Phase 180°")
        syncDspEngine()
    }

    fun toggleCenterVocalLock() {
        _uiState.update { state ->
            val next = !state.centerVocalLock
            val synth = FutureGenAudioLabEngine.synthesizeMultiNodeStage(
                nodes = state.stageNodes,
                wooferCrossoverHz = state.wooferCrossoverHz,
                wooferPhaseInvert180 = state.wooferPhaseInvert180,
                centerVocalLock = next
            )
            state.copy(
                centerVocalLock = next,
                bandGainsDb = synth.sculptedBands31,
                stageMidPanLR = synth.midBandPanLR,
                stageTelemetrySummary = synth.telemetrySummary
            )
        }
        hapticManager.onControlTick("Center Vocal Lock")
        syncDspEngine()
    }

    // =========================================================================
    // 2030 HYPER-MATRIX 10-ENGINE SUITE CONTROLS
    // =========================================================================

    private fun recomputeAndApplyHyperMatrix() {
        _uiState.update { s ->
            val (sculpted, summary) = FutureGenAudioLabEngine.synthesizeHyperMatrixCurve(
                baseBands = s.bandGainsDb,
                subHarmonicSynthPercent = s.subHarmonicSynthPercent,
                stemVocalPercent = s.stemVocalPercent,
                stemDrumPercent = s.stemDrumPercent,
                stemBassPercent = s.stemBassPercent,
                stemMelodyPercent = s.stemMelodyPercent,
                speedCruiseMode = s.speedCruiseMode,
                analogMode = s.analogSaturationMode,
                analogDrivePercent = if (s.analogSaturationMode == AnalogSaturationMode.CLEAN_DIGITAL) 0 else s.analogDrivePercent,
                tinnitusShieldEnabled = s.isTinnitusShieldEnabled,
                tinnitusNotchHz = s.tinnitusNotchFreqHz
            )
            s.copy(
                isMasterEnabled = true,
                bandGainsDb = applyBassLockToBands(sculpted, s.bassBoostStrength, s.isBassBoostLocked),
                hyperMatrixSummary = summary
            )
        }
        syncDspEngine()
    }

    fun setGyroHeadTrackingMode(mode: GyroHeadTrackingMode) {
        _uiState.update {
            it.copy(
                gyroHeadTrackingMode = mode,
                hyperMatrixSummary = "4D GYRO: ${mode.title} (${mode.badge}) • ${mode.subtitle}"
            )
        }
        hapticManager.onControlTick("4D Gyro: ${mode.badge}")
        syncDspEngine()
    }

    fun updateSubHarmonicSynthPercent(percent: Int) {
        val clamped = percent.coerceIn(0, 100)
        _uiState.update { it.copy(subHarmonicSynthPercent = clamped) }
        recomputeAndApplyHyperMatrix()
    }

    fun applyStemDeMixerPreset(preset: StemDeMixerPreset) {
        _uiState.update {
            it.copy(
                selectedStemPreset = preset,
                stemVocalPercent = preset.vocalPct,
                stemDrumPercent = preset.drumPct,
                stemBassPercent = preset.bassPct,
                stemMelodyPercent = preset.melodyPct,
                activePresetName = "Stem: ${preset.title}"
            )
        }
        hapticManager.onAiPresetApplied(preset.title)
        recomputeAndApplyHyperMatrix()
    }

    fun updateStemMixLevels(
        vocalPct: Int = _uiState.value.stemVocalPercent,
        drumPct: Int = _uiState.value.stemDrumPercent,
        bassPct: Int = _uiState.value.stemBassPercent,
        melodyPct: Int = _uiState.value.stemMelodyPercent
    ) {
        _uiState.update {
            it.copy(
                stemVocalPercent = vocalPct.coerceIn(0, 200),
                stemDrumPercent = drumPct.coerceIn(0, 200),
                stemBassPercent = bassPct.coerceIn(0, 200),
                stemMelodyPercent = melodyPct.coerceIn(0, 200)
            )
        }
        recomputeAndApplyHyperMatrix()
    }

    fun setSpeedCruiseMode(mode: SpeedCruiseMode) {
        _uiState.update {
            it.copy(
                speedCruiseMode = mode,
                activePresetName = if (mode == SpeedCruiseMode.OFF) it.activePresetName else "Cruise: ${mode.title}"
            )
        }
        hapticManager.onControlTick("Cruise: ${mode.badge}")
        recomputeAndApplyHyperMatrix()
    }

    fun setAnalogSaturationMode(mode: AnalogSaturationMode) {
        _uiState.update {
            it.copy(
                analogSaturationMode = mode,
                activePresetName = if (mode == AnalogSaturationMode.CLEAN_DIGITAL) it.activePresetName else "Analog: ${mode.title}"
            )
        }
        hapticManager.onControlTick("Analog: ${mode.badge}")
        recomputeAndApplyHyperMatrix()
    }

    fun updateAnalogDrivePercent(percent: Int) {
        _uiState.update { it.copy(analogDrivePercent = percent.coerceIn(0, 100)) }
        recomputeAndApplyHyperMatrix()
    }

    fun toggleTinnitusShield() {
        _uiState.update {
            val next = !it.isTinnitusShieldEnabled
            it.copy(
                isTinnitusShieldEnabled = next,
                safeListeningDbText = if (next) {
                    "🛡️ TINNITUS NOTCH-SHIELD ACTIVE (${it.tinnitusNotchFreqHz}Hz -6.5dB) • Zero Ear Fatigue"
                } else {
                    "SAFE DOSE: 74 dB SPL • Zero Ear-Fatigue Guard Ready"
                }
            )
        }
        hapticManager.onControlTick("Tinnitus Shield")
        recomputeAndApplyHyperMatrix()
    }

    fun updateTinnitusNotchFreq(hz: Int) {
        _uiState.update {
            it.copy(
                tinnitusNotchFreqHz = hz.coerceIn(5000, 12000),
                safeListeningDbText = "🛡️ TINNITUS NOTCH-SHIELD ACTIVE (${hz.coerceIn(5000, 12000)}Hz -6.5dB)"
            )
        }
        if (_uiState.value.isTinnitusShieldEnabled) {
            recomputeAndApplyHyperMatrix()
        }
    }

    fun updateCrossfeedPercent(percent: Int) {
        _uiState.update {
            it.copy(
                crossfeedStrengthPercent = percent.coerceIn(0, 100),
                hyperMatrixSummary = "BINAURAL CROSSFEED: ${percent.coerceIn(0, 100)}% • Studio Monitor 280µs Matrix Active"
            )
        }
        syncDspEngine()
    }

    fun toggleBassHapticWoofer() {
        _uiState.update {
            val next = !it.isBassHapticWooferEnabled
            it.copy(
                isBassHapticWooferEnabled = next,
                hyperMatrixSummary = if (next) {
                    "💓 TACTILE SUB-WOOFER HAPTICS ACTIVE (${it.bassHapticIntensity}%) • Phone vibrates on 20–63Hz drops"
                } else {
                    "Tactile Sub-Woofer Haptics Standby"
                }
            )
        }
        hapticManager.onAiPresetApplied("Bass Haptic Woofer")
    }

    fun applyAcousticVenueClone(venue: AcousticVenueClone) {
        if (venue == AcousticVenueClone.NONE) {
            _uiState.update { it.copy(activeVenueClone = venue) }
            return
        }
        _uiState.update { s ->
            val effectiveBass = resolveBass(venue.bassBoost, s)
            s.copy(
                isMasterEnabled = true,
                activeVenueClone = venue,
                activePresetName = "Venue: ${venue.title}",
                bandGainsDb = applyBassLockToBands(venue.bands31, effectiveBass, s.isBassBoostLocked),
                bassBoostStrength = effectiveBass,
                spatialLevel = venue.spatial3D,
                reverbLevel = venue.reverbLevel,
                reverbRoom = venue.reverbRoom,
                crossfeedStrengthPercent = venue.crossfeedPct,
                hyperMatrixSummary = "🏛️ VENUE CLONED: ${venue.title} (${venue.badge}) • ${venue.subtitle}"
            )
        }
        hapticManager.onAiPresetApplied(venue.title)
        syncDspEngine()
    }

    fun captureAndLockReferenceTrackDna(customLabel: String = "Favorite Reference Mix", onComplete: (String) -> Unit = {}) {
        val s = _uiState.value
        val (clonedBands, summary) = FutureGenAudioLabEngine.captureReferenceTrackDna(
            currentBands = s.bandGainsDb,
            liveSpectrumBins = spectrumBins.value,
            trackLabel = customLabel
        )
        _uiState.update {
            it.copy(
                isMasterEnabled = true,
                activePresetName = "DNA: $customLabel",
                referenceTrackName = customLabel,
                isReferenceDnaActive = true,
                bandGainsDb = clonedBands,
                hyperMatrixSummary = summary
            )
        }
        hapticManager.onAiPresetApplied("Reference DNA Cloned")
        syncDspEngine()
        onComplete(summary)
    }

    // =========================================================================
    // OUT-OF-HEAD ROOM EXTERNALIZER (SMYTH A16 / OOHL STYLE) CONTROLS
    // =========================================================================

    private fun recomputeAndApplyOutOfHeadExternalizer() {
        _uiState.update { s ->
            val result = FutureGenAudioLabEngine.synthesizeOutOfHeadExternalizer(
                baseBands = s.bandGainsDb,
                mode = s.outOfHeadMode,
                distanceMeters = s.outOfHeadDistanceMeters,
                earlyReflectionPercent = s.outOfHeadEarlyReflectionPercent,
                pinnaChestHrtfEnabled = s.isPinnaChestHrtfEnabled,
                subFloorCouplerEnabled = s.isSubFloorCouplerEnabled
            )
            if (s.outOfHeadMode == OutOfHeadExternalizerMode.OFF) {
                s.copy(
                    outOfHeadTelemetryText = result.telemetrySummary
                )
            } else {
                val effectiveBass = resolveBass(result.effectiveBassBoost, s)
                s.copy(
                    isMasterEnabled = true,
                    activePresetName = "Out-Of-Head: ${s.outOfHeadMode.title}",
                    bandGainsDb = applyBassLockToBands(result.sculptedBands31, effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    spatialLevel = result.effectiveSpatial3D,
                    reverbLevel = result.effectiveReverbLevel,
                    reverbRoom = result.effectiveReverbRoom,
                    crossfeedStrengthPercent = result.effectiveCrossfeedPct,
                    preampDb = result.effectivePreampDb,
                    outOfHeadTelemetryText = result.telemetrySummary
                )
            }
        }
        syncDspEngine()
    }

    fun setOutOfHeadExternalizerMode(mode: OutOfHeadExternalizerMode) {
        _uiState.update {
            it.copy(
                outOfHeadMode = mode,
                outOfHeadDistanceMeters = mode.defaultDistanceMeters,
                outOfHeadEarlyReflectionPercent = mode.defaultWallBouncePct
            )
        }
        hapticManager.onAiPresetApplied("Out-Of-Head ${mode.badge}")
        recomputeAndApplyOutOfHeadExternalizer()
    }

    fun updateOutOfHeadDistanceMeters(meters: Float) {
        val clamped = (kotlin.math.round(meters * 10f) / 10f).coerceIn(0.5f, 6.0f)
        _uiState.update { state ->
            val nextMode = if (state.outOfHeadMode == OutOfHeadExternalizerMode.OFF) {
                OutOfHeadExternalizerMode.LIVING_ROOM_SOFA_3M
            } else state.outOfHeadMode
            state.copy(
                outOfHeadMode = nextMode,
                outOfHeadDistanceMeters = clamped
            )
        }
        recomputeAndApplyOutOfHeadExternalizer()
    }

    fun updateOutOfHeadEarlyReflection(percent: Int) {
        val clamped = percent.coerceIn(0, 100)
        _uiState.update { state ->
            val nextMode = if (state.outOfHeadMode == OutOfHeadExternalizerMode.OFF) {
                OutOfHeadExternalizerMode.LIVING_ROOM_SOFA_3M
            } else state.outOfHeadMode
            state.copy(
                outOfHeadMode = nextMode,
                outOfHeadEarlyReflectionPercent = clamped
            )
        }
        recomputeAndApplyOutOfHeadExternalizer()
    }

    fun togglePinnaChestHrtf() {
        _uiState.update { it.copy(isPinnaChestHrtfEnabled = !it.isPinnaChestHrtfEnabled) }
        hapticManager.onControlTick("Pinna & Chest HRTF")
        recomputeAndApplyOutOfHeadExternalizer()
    }

    fun toggleSubFloorCoupler() {
        _uiState.update { it.copy(isSubFloorCouplerEnabled = !it.isSubFloorCouplerEnabled) }
        hapticManager.onControlTick("Sub-Bass Floor Coupler")
        recomputeAndApplyOutOfHeadExternalizer()
    }

    // =========================================================================
    // GLOBAL TRIAD FLAGSHIP HI-FI SUITE (🇯🇵 JAPAN • 🇨🇳 CHINA • 🇺🇸 USA)
    // =========================================================================

    private fun recomputeAndApplyGlobalTriad() {
        _uiState.update { s ->
            val result = FutureGenAudioLabEngine.synthesizeGlobalTriadSuite(
                baseBands = s.bandGainsDb,
                mode = s.globalTriadMode,
                japanDseePercent = s.japanDseePercent,
                chinaR2rBerylliumPercent = s.chinaR2rBerylliumPercent,
                usaDiracSpherePercent = s.usaDiracSpherePercent,
                fletcherMunsonEnabled = s.isFletcherMunsonEnabled,
                systemVolumePercent = systemVolumePercent.value
            )
            if (s.globalTriadMode == GlobalTriadMode.OFF) {
                s.copy(globalTriadTelemetryText = result.telemetrySummary)
            } else {
                val effectiveBass = resolveBass(result.effectiveBassBoost, s)
                s.copy(
                    isMasterEnabled = true,
                    activePresetName = s.globalTriadMode.title,
                    bandGainsDb = applyBassLockToBands(result.sculptedBands31, effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    spatialLevel = result.effectiveSpatial3D,
                    reverbLevel = result.effectiveReverbLevel,
                    reverbRoom = result.effectiveReverbRoom,
                    crossfeedStrengthPercent = result.effectiveCrossfeedPct,
                    analogDrivePercent = result.effectiveAnalogDrivePct,
                    globalTriadTelemetryText = result.telemetrySummary
                )
            }
        }
        syncDspEngine()
    }

    fun setGlobalTriadMode(mode: GlobalTriadMode) {
        _uiState.update { it.copy(globalTriadMode = mode) }
        hapticManager.onAiPresetApplied("Triad ${mode.badge}")
        recomputeAndApplyGlobalTriad()
    }

    fun updateJapanDseePercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.globalTriadMode == GlobalTriadMode.OFF) {
                GlobalTriadMode.JAPAN_SONY_YAMAHA
            } else state.globalTriadMode
            state.copy(
                globalTriadMode = nextMode,
                japanDseePercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyGlobalTriad()
    }

    fun updateChinaR2rPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.globalTriadMode == GlobalTriadMode.OFF) {
                GlobalTriadMode.CHINA_R2R_BERYLLIUM
            } else state.globalTriadMode
            state.copy(
                globalTriadMode = nextMode,
                chinaR2rBerylliumPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyGlobalTriad()
    }

    fun updateUsaDiracPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.globalTriadMode == GlobalTriadMode.OFF) {
                GlobalTriadMode.USA_SPHERE_DIRAC_THX
            } else state.globalTriadMode
            state.copy(
                globalTriadMode = nextMode,
                usaDiracSpherePercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyGlobalTriad()
    }

    fun toggleFletcherMunsonLoudness() {
        _uiState.update { it.copy(isFletcherMunsonEnabled = !it.isFletcherMunsonEnabled) }
        hapticManager.onControlTick("THX ISO Equal-Loudness")
        recomputeAndApplyGlobalTriad()
    }

    // =========================================================================
    // 🛸 2030 SOVEREIGN R&D LAB (🇯🇵 JAPAN • 🇨🇳 CHINA • 🇺🇸 USA NEXT-GEN ROADMAP)
    // =========================================================================

    private fun recomputeAndApplySovereign2030() {
        _uiState.update { s ->
            val result = FutureGenAudioLabEngine.synthesizeSovereign2030RndSuite(
                baseBands = s.bandGainsDb,
                mode = s.sovereign2030Mode,
                phantomSub16HzPercent = s.phantomSub16HzPercent,
                dopplerEraserEnabled = s.isDopplerEraserEnabled,
                dsd1024DampingPercent = s.dsd1024DampingPercent,
                bioEarDeEsserEnabled = s.isBioEarDeEsserEnabled,
                mit64WfsWallPercent = s.mit64WfsWallPercent,
                streamDeSquasherPercent = s.streamDeSquasherPercent
            )
            if (s.sovereign2030Mode == Sovereign2030RndMode.OFF) {
                s.copy(sovereign2030TelemetryText = result.telemetrySummary)
            } else {
                val effectiveBass = resolveBass(result.effectiveBassBoost, s)
                s.copy(
                    isMasterEnabled = true,
                    activePresetName = s.sovereign2030Mode.title,
                    bandGainsDb = applyBassLockToBands(result.sculptedBands31, effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    spatialLevel = result.effectiveSpatial3D,
                    reverbLevel = result.effectiveReverbLevel,
                    reverbRoom = result.effectiveReverbRoom,
                    crossfeedStrengthPercent = result.effectiveCrossfeedPct,
                    analogDrivePercent = result.effectiveAnalogDrivePct,
                    preampDb = result.effectivePreampDb,
                    sovereign2030TelemetryText = result.telemetrySummary
                )
            }
        }
        syncDspEngine()
    }

    fun setSovereign2030Mode(mode: Sovereign2030RndMode) {
        _uiState.update { it.copy(sovereign2030Mode = mode) }
        hapticManager.onAiPresetApplied("2030 R&D ${mode.badge}")
        recomputeAndApplySovereign2030()
    }

    fun updatePhantomSub16HzPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.sovereign2030Mode == Sovereign2030RndMode.OFF) {
                Sovereign2030RndMode.JAPAN_NTT_YAMAHA_2030
            } else state.sovereign2030Mode
            state.copy(
                sovereign2030Mode = nextMode,
                phantomSub16HzPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplySovereign2030()
    }

    fun toggleDopplerEraser() {
        _uiState.update { it.copy(isDopplerEraserEnabled = !it.isDopplerEraserEnabled) }
        hapticManager.onControlTick("Yamaha Inverse Doppler")
        recomputeAndApplySovereign2030()
    }

    fun updateDsd1024DampingPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.sovereign2030Mode == Sovereign2030RndMode.OFF) {
                Sovereign2030RndMode.CHINA_DSD1024_BIO_2030
            } else state.sovereign2030Mode
            state.copy(
                sovereign2030Mode = nextMode,
                dsd1024DampingPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplySovereign2030()
    }

    fun toggleBioEarDeEsser() {
        _uiState.update { it.copy(isBioEarDeEsserEnabled = !it.isBioEarDeEsserEnabled) }
        hapticManager.onControlTick("Biometric Ear-Canal De-Esser")
        recomputeAndApplySovereign2030()
    }

    fun updateMit64WfsWallPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.sovereign2030Mode == Sovereign2030RndMode.OFF) {
                Sovereign2030RndMode.USA_MIT_WFS_DESQUASH_2030
            } else state.sovereign2030Mode
            state.copy(
                sovereign2030Mode = nextMode,
                mit64WfsWallPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplySovereign2030()
    }

    fun updateStreamDeSquasherPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.sovereign2030Mode == Sovereign2030RndMode.OFF) {
                Sovereign2030RndMode.USA_MIT_WFS_DESQUASH_2030
            } else state.sovereign2030Mode
            state.copy(
                sovereign2030Mode = nextMode,
                streamDeSquasherPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplySovereign2030()
    }

    // =========================================================================
    // 🧬 OMEGA-PRIME AUTONOMOUS ACOUSTIC AI SUITE (6-IN-1 MASTER ENGINE)
    // =========================================================================

    private fun recomputeAndApplyOmegaPrime() {
        _uiState.update { s ->
            val result = FutureGenAudioLabEngine.synthesizeOmegaPrimeSuite(
                baseBands = s.bandGainsDb,
                mode = s.omegaPrimeMode,
                servoProfile = s.servoTransducerProfile,
                dynamicBreathingEqPercent = s.dynamicBreathingEqPercent,
                vocalSidechainDuckPercent = s.vocalSidechainDuckPercent,
                elevationHelixSkyPercent = s.elevationHelixSkyPercent,
                goldenPhiHarmonicPercent = s.goldenPhiHarmonicPercent,
                bioGrooveCardioSyncEnabled = s.isBioGrooveCardioSyncEnabled
            )
            if (s.omegaPrimeMode == OmegaPrimeMode.OFF) {
                s.copy(omegaPrimeTelemetryText = result.telemetrySummary)
            } else {
                val effectiveBass = resolveBass(result.effectiveBassBoost, s)
                s.copy(
                    isMasterEnabled = true,
                    activePresetName = s.omegaPrimeMode.title,
                    bandGainsDb = applyBassLockToBands(result.sculptedBands31, effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    spatialLevel = result.effectiveSpatial3D,
                    reverbLevel = result.effectiveReverbLevel,
                    reverbRoom = result.effectiveReverbRoom,
                    crossfeedStrengthPercent = result.effectiveCrossfeedPct,
                    analogDrivePercent = result.effectiveAnalogDrivePct,
                    preampDb = result.effectivePreampDb,
                    omegaPrimeTelemetryText = result.telemetrySummary
                )
            }
        }
        syncDspEngine()
    }

    fun setOmegaPrimeMode(mode: OmegaPrimeMode) {
        _uiState.update { it.copy(omegaPrimeMode = mode) }
        hapticManager.onAiPresetApplied("Omega-Prime ${mode.badge}")
        recomputeAndApplyOmegaPrime()
    }

    fun setServoTransducerProfile(profile: ServoTransducerProfile) {
        _uiState.update { state ->
            val nextMode = if (state.omegaPrimeMode == OmegaPrimeMode.OFF) {
                OmegaPrimeMode.OMEGA_PRIME_AUTONOMOUS_ALL
            } else state.omegaPrimeMode
            state.copy(
                omegaPrimeMode = nextMode,
                servoTransducerProfile = profile
            )
        }
        hapticManager.onControlTick("Servo: ${profile.badge}")
        recomputeAndApplyOmegaPrime()
    }

    fun updateDynamicBreathingEqPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.omegaPrimeMode == OmegaPrimeMode.OFF) {
                OmegaPrimeMode.OMEGA_PRIME_AUTONOMOUS_ALL
            } else state.omegaPrimeMode
            state.copy(
                omegaPrimeMode = nextMode,
                dynamicBreathingEqPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyOmegaPrime()
    }

    fun updateVocalSidechainDuckPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.omegaPrimeMode == OmegaPrimeMode.OFF) {
                OmegaPrimeMode.VOCAL_SIDECHAIN_MASTER
            } else state.omegaPrimeMode
            state.copy(
                omegaPrimeMode = nextMode,
                vocalSidechainDuckPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyOmegaPrime()
    }

    fun updateElevationHelixSkyPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.omegaPrimeMode == OmegaPrimeMode.OFF) {
                OmegaPrimeMode.HELIX_SKY_DOME_3D
            } else state.omegaPrimeMode
            state.copy(
                omegaPrimeMode = nextMode,
                elevationHelixSkyPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyOmegaPrime()
    }

    fun updateGoldenPhiHarmonicPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.omegaPrimeMode == OmegaPrimeMode.OFF) {
                OmegaPrimeMode.GOLDEN_PHI_UNPLUGGED
            } else state.omegaPrimeMode
            state.copy(
                omegaPrimeMode = nextMode,
                goldenPhiHarmonicPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyOmegaPrime()
    }

    fun toggleBioGrooveCardioSync() {
        _uiState.update { it.copy(isBioGrooveCardioSyncEnabled = !it.isBioGrooveCardioSyncEnabled) }
        hapticManager.onControlTick("Bio-Groove Cardio Sync")
        recomputeAndApplyOmegaPrime()
    }

    // =========================================================================
    // 🌬️ ZERO-EARBUD INVISIBLE OPEN-AIR ENGINE (6-IN-1 ANTI-OCCLUSION SUITE)
    // =========================================================================

    private fun recomputeAndApplyZeroEarbud() {
        _uiState.update { s ->
            val result = FutureGenAudioLabEngine.synthesizeZeroEarbudOpenAirSuite(
                baseBands = s.bandGainsDb,
                mode = s.zeroEarbudMode,
                occlusionValvePercent = s.occlusionValvePercent,
                openCanalUnplugPercent = s.openCanalUnplugPercent,
                airCushion25mPercent = s.airCushion25mPercent,
                openBackElectrostaticPercent = s.openBackElectrostaticPercent,
                antiVacuumBreezeEnabled = s.isAntiVacuumBreezeEnabled,
                vestibularMicroDriftEnabled = s.isVestibularMicroDriftEnabled
            )
            if (s.zeroEarbudMode == ZeroEarbudOpenAirMode.OFF) {
                s.copy(zeroEarbudTelemetryText = result.telemetrySummary)
            } else {
                val effectiveBass = resolveBass(result.effectiveBassBoost, s)
                s.copy(
                    isMasterEnabled = true,
                    activePresetName = s.zeroEarbudMode.title,
                    bandGainsDb = applyBassLockToBands(result.sculptedBands31, effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    spatialLevel = result.effectiveSpatial3D,
                    reverbLevel = result.effectiveReverbLevel,
                    reverbRoom = result.effectiveReverbRoom,
                    crossfeedStrengthPercent = result.effectiveCrossfeedPct,
                    preampDb = result.effectivePreampDb,
                    zeroEarbudTelemetryText = result.telemetrySummary
                )
            }
        }
        syncDspEngine()
    }

    fun setZeroEarbudMode(mode: ZeroEarbudOpenAirMode) {
        _uiState.update { it.copy(zeroEarbudMode = mode) }
        hapticManager.onAiPresetApplied("Zero-Earbud ${mode.badge}")
        recomputeAndApplyZeroEarbud()
    }

    fun updateOcclusionValvePercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.zeroEarbudMode == ZeroEarbudOpenAirMode.OFF) {
                ZeroEarbudOpenAirMode.TWS_PRESSURE_RELIEF_VALVE
            } else state.zeroEarbudMode
            state.copy(
                zeroEarbudMode = nextMode,
                occlusionValvePercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyZeroEarbud()
    }

    fun updateOpenCanalUnplugPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.zeroEarbudMode == ZeroEarbudOpenAirMode.OFF) {
                ZeroEarbudOpenAirMode.BARE_EAR_INVISIBLE_100
            } else state.zeroEarbudMode
            state.copy(
                zeroEarbudMode = nextMode,
                openCanalUnplugPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyZeroEarbud()
    }

    fun updateAirCushion25mPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.zeroEarbudMode == ZeroEarbudOpenAirMode.OFF) {
                ZeroEarbudOpenAirMode.BARE_EAR_INVISIBLE_100
            } else state.zeroEarbudMode
            state.copy(
                zeroEarbudMode = nextMode,
                airCushion25mPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyZeroEarbud()
    }

    fun updateOpenBackElectrostaticPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.zeroEarbudMode == ZeroEarbudOpenAirMode.OFF) {
                ZeroEarbudOpenAirMode.HE1_OPEN_BACK_ELECTROSTATIC
            } else state.zeroEarbudMode
            state.copy(
                zeroEarbudMode = nextMode,
                openBackElectrostaticPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyZeroEarbud()
    }

    fun toggleAntiVacuumBreeze() {
        _uiState.update { it.copy(isAntiVacuumBreezeEnabled = !it.isAntiVacuumBreezeEnabled) }
        hapticManager.onControlTick("Anti-Vacuum Breeze Cushion")
        recomputeAndApplyZeroEarbud()
    }

    fun toggleVestibularMicroDrift() {
        _uiState.update { it.copy(isVestibularMicroDriftEnabled = !it.isVestibularMicroDriftEnabled) }
        hapticManager.onControlTick("0.4Hz Vestibular Micro-Drift")
        recomputeAndApplyZeroEarbud()
    }

    // =========================================================================
    // 🎻 HOLOGRAPHIC LIVE INSTRUMENT REALISM ENGINE (6-IN-1 LIVE STAGE PHYSICS)
    // =========================================================================

    private fun recomputeAndApplyLiveInstrument() {
        _uiState.update { s ->
            val result = FutureGenAudioLabEngine.synthesizeHolographicLiveInstrumentSuite(
                baseBands = s.bandGainsDb,
                mode = s.liveInstrumentMode,
                pluckDrumTransientPercent = s.pluckDrumTransientPercent,
                woodHelmholtzBodyPercent = s.woodHelmholtzBodyPercent,
                sympatheticStringBleedPercent = s.sympatheticStringBleedPercent,
                singerBreathProximityPercent = s.singerBreathProximityPercent,
                haasStageSeparationEnabled = s.isHaasStageSeparationEnabled,
                woodenUnpluggedFloorEnabled = s.isWoodenUnpluggedFloorEnabled
            )
            if (s.liveInstrumentMode == HolographicLiveInstrumentMode.OFF) {
                s.copy(liveInstrumentTelemetryText = result.telemetrySummary)
            } else {
                val effectiveBass = resolveBass(result.effectiveBassBoost, s)
                s.copy(
                    isMasterEnabled = true,
                    activePresetName = s.liveInstrumentMode.title,
                    bandGainsDb = applyBassLockToBands(result.sculptedBands31, effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    spatialLevel = result.effectiveSpatial3D,
                    reverbLevel = result.effectiveReverbLevel,
                    reverbRoom = result.effectiveReverbRoom,
                    crossfeedStrengthPercent = result.effectiveCrossfeedPct,
                    analogDrivePercent = result.effectiveAnalogDrivePct,
                    preampDb = result.effectivePreampDb,
                    liveInstrumentTelemetryText = result.telemetrySummary
                )
            }
        }
        syncDspEngine()
    }

    fun setLiveInstrumentMode(mode: HolographicLiveInstrumentMode) {
        _uiState.update { it.copy(liveInstrumentMode = mode) }
        hapticManager.onAiPresetApplied("Live Stage ${mode.badge}")
        recomputeAndApplyLiveInstrument()
    }

    fun updatePluckDrumTransientPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.liveInstrumentMode == HolographicLiveInstrumentMode.OFF) {
                HolographicLiveInstrumentMode.LIVE_DRUM_BASS_FRONT_ROW
            } else state.liveInstrumentMode
            state.copy(
                liveInstrumentMode = nextMode,
                pluckDrumTransientPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyLiveInstrument()
    }

    fun updateWoodHelmholtzBodyPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.liveInstrumentMode == HolographicLiveInstrumentMode.OFF) {
                HolographicLiveInstrumentMode.MTV_COKE_STUDIO_UNPLUGGED
            } else state.liveInstrumentMode
            state.copy(
                liveInstrumentMode = nextMode,
                woodHelmholtzBodyPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyLiveInstrument()
    }

    fun updateSympatheticStringBleedPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.liveInstrumentMode == HolographicLiveInstrumentMode.OFF) {
                HolographicLiveInstrumentMode.ROYAL_ORCHESTRA_VIOLIN_HALL
            } else state.liveInstrumentMode
            state.copy(
                liveInstrumentMode = nextMode,
                sympatheticStringBleedPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyLiveInstrument()
    }

    fun updateSingerBreathProximityPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.liveInstrumentMode == HolographicLiveInstrumentMode.OFF) {
                HolographicLiveInstrumentMode.INTIMATE_JAZZ_CAFE_4FT
            } else state.liveInstrumentMode
            state.copy(
                liveInstrumentMode = nextMode,
                singerBreathProximityPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyLiveInstrument()
    }

    fun toggleHaasStageSeparation() {
        _uiState.update { it.copy(isHaasStageSeparationEnabled = !it.isHaasStageSeparationEnabled) }
        hapticManager.onControlTick("Haas 18ms Stage Separation")
        recomputeAndApplyLiveInstrument()
    }

    fun toggleWoodenUnpluggedFloorBounce() {
        _uiState.update { it.copy(isWoodenUnpluggedFloorEnabled = !it.isWoodenUnpluggedFloorEnabled) }
        hapticManager.onControlTick("Wooden Unplugged Club Floor")
        recomputeAndApplyLiveInstrument()
    }

    // =========================================================================
    // 🧠 CHRONO-NEURAL & SUB-QUANTUM BIOMIMETIC ENGINE (6-IN-1 NEURO-ACOUSTIC)
    // =========================================================================

    private fun recomputeAndApplyChronoNeural() {
        _uiState.update { s ->
            val result = FutureGenAudioLabEngine.synthesizeChronoNeuralBiomimeticSuite(
                baseBands = s.bandGainsDb,
                mode = s.chronoNeuralMode,
                cochlearDispersionSyncPercent = s.cochlearDispersionSyncPercent,
                gamma40HzTimeDilationPercent = s.gamma40HzTimeDilationPercent,
                stapediusReflexDisarmerPercent = s.stapediusReflexDisarmerPercent,
                tartiniOtoacousticSubPercent = s.tartiniOtoacousticSubPercent,
                microSaccadicStageSwayEnabled = s.isMicroSaccadicStageSwayEnabled,
                fibonacciGoldenTapeSilkEnabled = s.isFibonacciGoldenTapeSilkEnabled
            )
            if (s.chronoNeuralMode == ChronoNeuralBiomimeticMode.OFF) {
                s.copy(chronoNeuralTelemetryText = result.telemetrySummary)
            } else {
                val effectiveBass = resolveBass(result.effectiveBassBoost, s)
                s.copy(
                    isMasterEnabled = true,
                    activePresetName = s.chronoNeuralMode.title,
                    bandGainsDb = applyBassLockToBands(result.sculptedBands31, effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    spatialLevel = result.effectiveSpatial3D,
                    reverbLevel = result.effectiveReverbLevel,
                    reverbRoom = result.effectiveReverbRoom,
                    crossfeedStrengthPercent = result.effectiveCrossfeedPct,
                    analogDrivePercent = result.effectiveAnalogDrivePct,
                    preampDb = result.effectivePreampDb,
                    chronoNeuralTelemetryText = result.telemetrySummary
                )
            }
        }
        syncDspEngine()
    }

    fun setChronoNeuralMode(mode: ChronoNeuralBiomimeticMode) {
        _uiState.update { it.copy(chronoNeuralMode = mode) }
        hapticManager.onAiPresetApplied("Chrono-Neural ${mode.badge}")
        recomputeAndApplyChronoNeural()
    }

    fun updateCochlearDispersionSyncPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.chronoNeuralMode == ChronoNeuralBiomimeticMode.OFF) {
                ChronoNeuralBiomimeticMode.COCHLEAR_ZERO_SMEAR_SYNC
            } else state.chronoNeuralMode
            state.copy(
                chronoNeuralMode = nextMode,
                cochlearDispersionSyncPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyChronoNeural()
    }

    fun updateGamma40HzTimeDilationPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.chronoNeuralMode == ChronoNeuralBiomimeticMode.OFF) {
                ChronoNeuralBiomimeticMode.GAMMA_40HZ_SLOW_MO_HD
            } else state.chronoNeuralMode
            state.copy(
                chronoNeuralMode = nextMode,
                gamma40HzTimeDilationPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyChronoNeural()
    }

    fun updateStapediusReflexDisarmerPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.chronoNeuralMode == ChronoNeuralBiomimeticMode.OFF) {
                ChronoNeuralBiomimeticMode.STAPEDIUS_SILK_EARDRUM
            } else state.chronoNeuralMode
            state.copy(
                chronoNeuralMode = nextMode,
                stapediusReflexDisarmerPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyChronoNeural()
    }

    fun updateTartiniOtoacousticSubPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.chronoNeuralMode == ChronoNeuralBiomimeticMode.OFF) {
                ChronoNeuralBiomimeticMode.TARTINI_PHANTOM_24HZ_SUB
            } else state.chronoNeuralMode
            state.copy(
                chronoNeuralMode = nextMode,
                tartiniOtoacousticSubPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyChronoNeural()
    }

    fun toggleMicroSaccadicStageSway() {
        _uiState.update { it.copy(isMicroSaccadicStageSwayEnabled = !it.isMicroSaccadicStageSwayEnabled) }
        hapticManager.onControlTick("0.18Hz Micro-Saccadic Stage Sway")
        recomputeAndApplyChronoNeural()
    }

    fun toggleFibonacciGoldenTapeSilk() {
        _uiState.update { it.copy(isFibonacciGoldenTapeSilkEnabled = !it.isFibonacciGoldenTapeSilkEnabled) }
        hapticManager.onControlTick("Fibonacci Golden-Ratio Tape Silk")
        recomputeAndApplyChronoNeural()
    }

    // =========================================================================
    // 🌌 APEX SINGULARITY & BIO-ACOUSTIC ENGINE (6-IN-1 ULTRA-POWER SUITE)
    // =========================================================================

    private fun recomputeAndApplyApexSingularity() {
        _uiState.update { s ->
            val result = FutureGenAudioLabEngine.synthesizeApexSingularitySuite(
                baseBands = s.bandGainsDb,
                mode = s.apexSingularityMode,
                gyroStageAnchorPercent = s.gyroStageAnchorPercent,
                tactileChestSlamSubPercent = s.tactileChestSlamSubPercent,
                vocalFormantUnmaskerPercent = s.vocalFormantUnmaskerPercent,
                metamaterialShellCloakPercent = s.metamaterialShellCloakPercent,
                goldenPhiTriodeTapeEnabled = s.isGoldenPhiTriodeTapeEnabled,
                frissonVagusResonatorEnabled = s.isFrissonVagusResonatorEnabled
            )
            if (s.apexSingularityMode == ApexSingularityMode.OFF) {
                s.copy(apexSingularityTelemetryText = result.telemetrySummary)
            } else {
                val effectiveBass = resolveBass(result.effectiveBassBoost, s)
                s.copy(
                    isMasterEnabled = true,
                    activePresetName = s.apexSingularityMode.title,
                    bandGainsDb = applyBassLockToBands(result.sculptedBands31, effectiveBass, s.isBassBoostLocked),
                    bassBoostStrength = effectiveBass,
                    spatialLevel = result.effectiveSpatial3D,
                    reverbLevel = result.effectiveReverbLevel,
                    reverbRoom = result.effectiveReverbRoom,
                    crossfeedStrengthPercent = result.effectiveCrossfeedPct,
                    analogDrivePercent = result.effectiveAnalogDrivePct,
                    preampDb = result.effectivePreampDb,
                    apexSingularityTelemetryText = result.telemetrySummary
                )
            }
        }
        syncDspEngine()
    }

    fun setApexSingularityMode(mode: ApexSingularityMode) {
        _uiState.update { it.copy(apexSingularityMode = mode) }
        hapticManager.onAiPresetApplied("Apex ${mode.badge}")
        recomputeAndApplyApexSingularity()
    }

    fun updateGyroStageAnchorPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.apexSingularityMode == ApexSingularityMode.OFF) {
                ApexSingularityMode.GYRO_360_STAGE_ANCHOR
            } else state.apexSingularityMode
            state.copy(
                apexSingularityMode = nextMode,
                gyroStageAnchorPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyApexSingularity()
    }

    fun updateTactileChestSlamSubPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.apexSingularityMode == ApexSingularityMode.OFF) {
                ApexSingularityMode.TACTILE_18HZ_CHEST_SLAM
            } else state.apexSingularityMode
            state.copy(
                apexSingularityMode = nextMode,
                tactileChestSlamSubPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyApexSingularity()
    }

    fun updateVocalFormantUnmaskerPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.apexSingularityMode == ApexSingularityMode.OFF) {
                ApexSingularityMode.VOCAL_FORMANT_SPOTLIGHT
            } else state.apexSingularityMode
            state.copy(
                apexSingularityMode = nextMode,
                vocalFormantUnmaskerPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyApexSingularity()
    }

    fun updateMetamaterialShellCloakPercent(percent: Int) {
        _uiState.update { state ->
            val nextMode = if (state.apexSingularityMode == ApexSingularityMode.OFF) {
                ApexSingularityMode.METAMATERIAL_SHELL_CLOAK
            } else state.apexSingularityMode
            state.copy(
                apexSingularityMode = nextMode,
                metamaterialShellCloakPercent = percent.coerceIn(0, 100)
            )
        }
        recomputeAndApplyApexSingularity()
    }

    fun toggleGoldenPhiTriodeTape() {
        _uiState.update { it.copy(isGoldenPhiTriodeTapeEnabled = !it.isGoldenPhiTriodeTapeEnabled) }
        hapticManager.onControlTick("Golden-Ratio φ=1.618 Triode & Tape Flux")
        recomputeAndApplyApexSingularity()
    }

    fun toggleFrissonVagusResonator() {
        _uiState.update { it.copy(isFrissonVagusResonatorEnabled = !it.isFrissonVagusResonatorEnabled) }
        hapticManager.onControlTick("0.1Hz Vagus-Nerve Frisson Resonator")
        recomputeAndApplyApexSingularity()
    }

    private fun startFutureGenCoordinator() {
        viewModelScope.launch {
            var tickCount = 0
            while (isActive) {
                val s = _uiState.value
                if (s.isRadarAutoOrbitEnabled || s.gyroHeadTrackingMode != GyroHeadTrackingMode.OFF) {
                    val stepDeg = when (s.gyroHeadTrackingMode) {
                        GyroHeadTrackingMode.STAGE_NORTH_LOCK -> kotlin.math.sin(tickCount * 0.22f) * 18f
                        GyroHeadTrackingMode.WOOFER_CHEST_LOCK -> kotlin.math.cos(tickCount * 0.25f) * 28f
                        GyroHeadTrackingMode.CINEMA_4D_INERTIAL -> ((s.radarAzimuthDeg + 6.0f + 180f) % 360f) - 180f
                        GyroHeadTrackingMode.OFF -> ((s.radarAzimuthDeg + 7.5f + 180f) % 360f) - 180f
                    }
                    _uiState.update {
                        it.copy(
                            radarAzimuthDeg = stepDeg,
                            gyroLiveYawDeg = stepDeg
                        )
                    }
                    dspEngine.updateFutureGenState(
                        brainwaveMode = s.neuralBrainwaveMode,
                        azimuthDeg = stepDeg,
                        distanceNorm = s.radarDistanceNorm,
                        lrBalanceDb = s.leftRightBalanceDb,
                        subPanLR = if (s.gyroHeadTrackingMode == GyroHeadTrackingMode.WOOFER_CHEST_LOCK) 0f else s.stageSubPanLR,
                        midPanLR = s.stageMidPanLR,
                        highPanLR = s.stageHighPanLR,
                        subHarmonicPercent = s.subHarmonicSynthPercent,
                        vocalStemPct = s.stemVocalPercent,
                        drumStemPct = s.stemDrumPercent,
                        bassStemPct = s.stemBassPercent,
                        melodyStemPct = s.stemMelodyPercent,
                        analogDrivePct = if (s.analogSaturationMode == AnalogSaturationMode.CLEAN_DIGITAL) 0 else s.analogDrivePercent,
                        crossfeedPct = s.crossfeedStrengthPercent,
                        outOfHeadActive = s.outOfHeadMode != OutOfHeadExternalizerMode.OFF,
                        outOfHeadDistanceM = s.outOfHeadDistanceMeters,
                        outOfHeadReflectionPct = s.outOfHeadEarlyReflectionPercent,
                        pinnaHrtf = s.isPinnaChestHrtfEnabled,
                        subFloorCoupler = s.isSubFloorCouplerEnabled,
                        globalTriadEnabled = s.globalTriadMode != GlobalTriadMode.OFF,
                        japanDseePct = s.japanDseePercent,
                        chinaR2rPct = s.chinaR2rBerylliumPercent,
                        usaDiracPct = s.usaDiracSpherePercent,
                        sovereign2030Enabled = s.sovereign2030Mode != Sovereign2030RndMode.OFF,
                        phantomSub16Pct = s.phantomSub16HzPercent,
                        dopplerEraser = s.isDopplerEraserEnabled,
                        dsd1024DampingPct = s.dsd1024DampingPercent,
                        bioDeEsser = s.isBioEarDeEsserEnabled,
                        mit64WfsPct = s.mit64WfsWallPercent,
                        streamDeSquasherPct = s.streamDeSquasherPercent,
                        omegaPrimeEnabled = s.omegaPrimeMode != OmegaPrimeMode.OFF,
                        dynamicBreathingEqPct = s.dynamicBreathingEqPercent,
                        vocalSidechainDuckPct = s.vocalSidechainDuckPercent,
                        elevationHelixSkyPct = s.elevationHelixSkyPercent,
                        goldenPhiHarmonicPct = s.goldenPhiHarmonicPercent,
                        bioGrooveCardioSync = s.isBioGrooveCardioSyncEnabled,
                        zeroEarbudEnabled = s.zeroEarbudMode != ZeroEarbudOpenAirMode.OFF,
                        occlusionValvePct = s.occlusionValvePercent,
                        openCanalUnplugPct = s.openCanalUnplugPercent,
                        airCushion25mPct = s.airCushion25mPercent,
                        openBackElectrostaticPct = s.openBackElectrostaticPercent,
                        antiVacuumBreeze = s.isAntiVacuumBreezeEnabled,
                        vestibularMicroDrift = s.isVestibularMicroDriftEnabled,
                        liveInstrumentEnabled = s.liveInstrumentMode != HolographicLiveInstrumentMode.OFF,
                        pluckDrumTransientPct = s.pluckDrumTransientPercent,
                        woodHelmholtzBodyPct = s.woodHelmholtzBodyPercent,
                        sympatheticStringBleedPct = s.sympatheticStringBleedPercent,
                        singerBreathProximityPct = s.singerBreathProximityPercent,
                        haasStageSeparation = s.isHaasStageSeparationEnabled,
                        woodenUnpluggedFloor = s.isWoodenUnpluggedFloorEnabled,
                        chronoNeuralEnabled = s.chronoNeuralMode != ChronoNeuralBiomimeticMode.OFF,
                        cochlearDispersionSyncPct = s.cochlearDispersionSyncPercent,
                        gamma40HzTimeDilationPct = s.gamma40HzTimeDilationPercent,
                        stapediusReflexDisarmerPct = s.stapediusReflexDisarmerPercent,
                        tartiniOtoacousticSubPct = s.tartiniOtoacousticSubPercent,
                        microSaccadicStageSway = s.isMicroSaccadicStageSwayEnabled,
                        fibonacciGoldenTapeSilk = s.isFibonacciGoldenTapeSilkEnabled,
                        apexSingularityEnabled = s.apexSingularityMode != ApexSingularityMode.OFF,
                        gyroStageAnchorPct = s.gyroStageAnchorPercent,
                        tactileChestSlamSubPct = s.tactileChestSlamSubPercent,
                        vocalFormantUnmaskerPct = s.vocalFormantUnmaskerPercent,
                        metamaterialShellCloakPct = s.metamaterialShellCloakPercent,
                        goldenPhiTriodeTape = s.isGoldenPhiTriodeTapeEnabled,
                        frissonVagusResonator = s.isFrissonVagusResonatorEnabled
                    )
                }
                // Bass-to-Haptic Sub-Woofer Tactile Engine: pulse phone vibrator on sub-bass hits
                if (s.isBassHapticWooferEnabled && s.isMasterEnabled && tickCount % 2 == 0) {
                    val subHit = (0..4).map { spectrumBins.value.getOrElse(it) { 0f } }.maxOrNull() ?: 0f
                    if (subHit > 0.42f) {
                        hapticManager.onControlTick("Sub-Bass Pulse ${(subHit * 100).toInt()}%")
                    }
                }
                if (s.isEsportsXrayEnabled) {
                    _esportsRadarState.value = FutureGenAudioLabEngine.computeEsportsThreatRadar(
                        enabled = true,
                        spectrumBins = spectrumBins.value,
                        stereoVu = stereoVuLevels.value,
                        azimuthDeg = _uiState.value.radarAzimuthDeg
                    )
                } else if (_esportsRadarState.value.enabled) {
                    _esportsRadarState.value = EsportsThreatRadarState(enabled = false)
                }
                if (s.isDynamicAutoRiderEnabled && tickCount % 4 == 0) {
                    val (morphedBands, label) = FutureGenAudioLabEngine.computeDynamicAutoRiderStep(
                        baseBands = s.bandGainsDb,
                        spectrumBins = spectrumBins.value,
                        dominantHz = dominantFreqHz.value
                    )
                    _uiState.update {
                        it.copy(
                            bandGainsDb = morphedBands,
                            autoRiderStatusText = label
                        )
                    }
                    dspEngine.updateDspState(
                        enabled = s.isMasterEnabled,
                        bandsDb = morphedBands,
                        bass = s.bassBoostStrength,
                        reverb = s.reverbLevel,
                        reverbRoom = s.reverbRoom,
                        spatial = s.spatialLevel,
                        preamp = s.preampDb,
                        limiter = s.softLimiterEnabled
                    )
                }
                tickCount++
                delay(180)
            }
        }
    }

    private fun syncDspEngine() {
        val s = _uiState.value
        presetManager.persistActiveProfileState(
            enabled = s.isMasterEnabled,
            activePresetName = s.activePresetName,
            bandGainsDb = s.bandGainsDb,
            bassBoostStrength = s.bassBoostStrength,
            reverbLevel = s.reverbLevel,
            reverbRoom = s.reverbRoom,
            spatialLevel = s.spatialLevel,
            preampDb = s.preampDb
        )
        dspEngine.updateFutureGenState(
            brainwaveMode = s.neuralBrainwaveMode,
            azimuthDeg = s.radarAzimuthDeg,
            distanceNorm = s.radarDistanceNorm,
            lrBalanceDb = s.leftRightBalanceDb,
            subPanLR = s.stageSubPanLR,
            midPanLR = s.stageMidPanLR,
            highPanLR = s.stageHighPanLR,
            subHarmonicPercent = s.subHarmonicSynthPercent,
            vocalStemPct = s.stemVocalPercent,
            drumStemPct = s.stemDrumPercent,
            bassStemPct = s.stemBassPercent,
            melodyStemPct = s.stemMelodyPercent,
            analogDrivePct = if (s.analogSaturationMode == AnalogSaturationMode.CLEAN_DIGITAL) 0 else s.analogDrivePercent,
            crossfeedPct = s.crossfeedStrengthPercent,
            outOfHeadActive = s.outOfHeadMode != OutOfHeadExternalizerMode.OFF,
            outOfHeadDistanceM = s.outOfHeadDistanceMeters,
            outOfHeadReflectionPct = s.outOfHeadEarlyReflectionPercent,
            pinnaHrtf = s.isPinnaChestHrtfEnabled,
            subFloorCoupler = s.isSubFloorCouplerEnabled,
            globalTriadEnabled = s.globalTriadMode != GlobalTriadMode.OFF,
            japanDseePct = s.japanDseePercent,
            chinaR2rPct = s.chinaR2rBerylliumPercent,
            usaDiracPct = s.usaDiracSpherePercent,
            sovereign2030Enabled = s.sovereign2030Mode != Sovereign2030RndMode.OFF,
            phantomSub16Pct = s.phantomSub16HzPercent,
            dopplerEraser = s.isDopplerEraserEnabled,
            dsd1024DampingPct = s.dsd1024DampingPercent,
            bioDeEsser = s.isBioEarDeEsserEnabled,
            mit64WfsPct = s.mit64WfsWallPercent,
            streamDeSquasherPct = s.streamDeSquasherPercent,
            omegaPrimeEnabled = s.omegaPrimeMode != OmegaPrimeMode.OFF,
            dynamicBreathingEqPct = s.dynamicBreathingEqPercent,
            vocalSidechainDuckPct = s.vocalSidechainDuckPercent,
            elevationHelixSkyPct = s.elevationHelixSkyPercent,
            goldenPhiHarmonicPct = s.goldenPhiHarmonicPercent,
            bioGrooveCardioSync = s.isBioGrooveCardioSyncEnabled,
            zeroEarbudEnabled = s.zeroEarbudMode != ZeroEarbudOpenAirMode.OFF,
            occlusionValvePct = s.occlusionValvePercent,
            openCanalUnplugPct = s.openCanalUnplugPercent,
            airCushion25mPct = s.airCushion25mPercent,
            openBackElectrostaticPct = s.openBackElectrostaticPercent,
            antiVacuumBreeze = s.isAntiVacuumBreezeEnabled,
            vestibularMicroDrift = s.isVestibularMicroDriftEnabled,
            liveInstrumentEnabled = s.liveInstrumentMode != HolographicLiveInstrumentMode.OFF,
            pluckDrumTransientPct = s.pluckDrumTransientPercent,
            woodHelmholtzBodyPct = s.woodHelmholtzBodyPercent,
            sympatheticStringBleedPct = s.sympatheticStringBleedPercent,
            singerBreathProximityPct = s.singerBreathProximityPercent,
            haasStageSeparation = s.isHaasStageSeparationEnabled,
            woodenUnpluggedFloor = s.isWoodenUnpluggedFloorEnabled,
            chronoNeuralEnabled = s.chronoNeuralMode != ChronoNeuralBiomimeticMode.OFF,
            cochlearDispersionSyncPct = s.cochlearDispersionSyncPercent,
            gamma40HzTimeDilationPct = s.gamma40HzTimeDilationPercent,
            stapediusReflexDisarmerPct = s.stapediusReflexDisarmerPercent,
            tartiniOtoacousticSubPct = s.tartiniOtoacousticSubPercent,
            microSaccadicStageSway = s.isMicroSaccadicStageSwayEnabled,
            fibonacciGoldenTapeSilk = s.isFibonacciGoldenTapeSilkEnabled,
            apexSingularityEnabled = s.apexSingularityMode != ApexSingularityMode.OFF,
            gyroStageAnchorPct = s.gyroStageAnchorPercent,
            tactileChestSlamSubPct = s.tactileChestSlamSubPercent,
            vocalFormantUnmaskerPct = s.vocalFormantUnmaskerPercent,
            metamaterialShellCloakPct = s.metamaterialShellCloakPercent,
            goldenPhiTriodeTape = s.isGoldenPhiTriodeTapeEnabled,
            frissonVagusResonator = s.isFrissonVagusResonatorEnabled
        )
        dspEngine.updateDspState(
            enabled = s.isMasterEnabled,
            bandsDb = s.bandGainsDb,
            bass = s.bassBoostStrength,
            reverb = s.reverbLevel,
            reverbRoom = s.reverbRoom,
            spatial = s.spatialLevel,
            preamp = s.preampDb,
            limiter = s.softLimiterEnabled
        )
        if (s.isBackgroundServiceEnabled) {
            EqForegroundService.startOrUpdate(getApplication())
        }
    }

    override fun onCleared() {
        runCatching {
            getApplication<Application>().unregisterReceiver(serviceReceiver)
        }
        voiceEngine.release()
        super.onCleared()
        dspEngine.release()
    }
}
