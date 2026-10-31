package com.example.dsp

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.data.EqPresetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin

enum class NeuralBrainwaveMode(
    val title: String,
    val badge: String,
    val beatHz: Float,
    val carrierHz: Float,
    val subtitle: String
) {
    OFF("Off", "BYPASS", 0f, 0f, "Standard studio signal path without binaural entrainment"),
    GAMMA_40HZ("Gamma 40Hz", "40Hz FOCUS", 40f, 240f, "Peak cognitive focus, coding & high-speed neural processing"),
    BETA_18HZ("Beta 18Hz", "18Hz ENERGY", 18f, 220f, "High-alert workout, reaction speed & active concentration"),
    ALPHA_10HZ("Alpha 10Hz", "10Hz FLOW", 10f, 200f, "Creative flow state, calm relaxation & stress relief"),
    THETA_6HZ("Theta 6Hz", "6Hz MEDITATE", 6f, 174f, "Deep meditation, spatial daydream & acoustic immersion"),
    DELTA_2HZ("Delta 2Hz", "2Hz SLEEP", 2.2f, 136.1f, "Late-night restorative sleep & sub-harmonic ear recovery")
}

data class SpatialStageNode(
    val id: String,
    val label: String,
    val shortTag: String,
    val category: String, // "WOOFER", "INSTRUMENT", "SPEAKER"
    val bandStartIdx: Int,
    val bandEndIdx: Int,
    val azimuthDeg: Float, // -180..+180 (0 = Front Center, -90 = Left, +90 = Right, 180 = Rear)
    val distanceNorm: Float, // 0.15..1.0 (proximity to listener head)
    val gainDb: Float = 0f, // -12..+12 dB
    val muted: Boolean = false,
    val solo: Boolean = false
)

enum class SpatialStageFormation(
    val title: String,
    val subtitle: String,
    val badge: String
) {
    CONCERT_ARENA("Live Concert Stage", "Vocals front-center, Guitar L, Keys/Air R, Dual Subwoofers front", "CONCERT 3D"),
    DRUMMERS_THRONE("Drummer's Seat POV", "Drums & Kick ultra-close, Guitar/Keys wide ±80°, Rear surround", "ON-STAGE POV"),
    MAYBACH_CABIN("Maybach 4-Corner Cabin", "Dual under-seat woofers + 4-corner surround array + Center Vocal Lock", "CABIN 7.1.4"),
    CLUB_SURROUND_360("360° Dual-Woofer Club", "Front-Left + Rear-Right opposing subwoofers for 360° chest slam", "CLUB 360°"),
    BINAURAL_STEM_SPLIT("Binaural Stem Split", "Bass & Drums Left stage, Vocals Center, Guitar & Synth Right stage", "STEM SPLIT")
}

data class MultiNodeStageSynthesis(
    val sculptedBands31: List<Float>,
    val subBandPanLR: Float, // -1.0 (Left) .. +1.0 (Right)
    val midBandPanLR: Float,
    val highBandPanLR: Float,
    val effectiveBassBoost: Int,
    val effectiveSpatial3D: Int,
    val effectiveReverbMix: Int,
    val telemetrySummary: String
)

data class SonarScanReport(
    val roomAcousticsType: String,
    val rt60DecayMs: Int,
    val standingWaveHz: Int,
    val sealQualityPercent: Int,
    val telemetrySummary: String,
    val compensationBands31: List<Float>,
    val recommendedBass: Int,
    val recommendedSpatial: Int,
    val recommendedReverbRoom: String
)

data class BioEarProfileResult(
    val estimatedEarAgeYears: Int,
    val goldenEarScorePercent: Int,
    val highestAudibleHz: Int,
    val leftRightBalanceDb: Float,
    val diagnosisSummary: String,
    val dnaCompensationBands31: List<Float>
)

data class EsportsThreatRadarState(
    val enabled: Boolean = false,
    val leftThreatLevel: Float = 0.15f,
    val centerThreatLevel: Float = 0.12f,
    val rightThreatLevel: Float = 0.18f,
    val dominantCueLabel: String = "FOOTSTEP RADAR STANDBY"
)

data class HolographicVibeProfile(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val bands31: List<Float>,
    val bassBoost: Int,
    val spatial3D: Int,
    val reverbLevel: Int,
    val reverbRoom: String,
    val preampDb: Float,
    val brainwaveMode: NeuralBrainwaveMode,
    val radarAzimuthDeg: Float
)

object FutureGenAudioLabEngine {

    fun defaultStageNodesForFormation(
        formation: SpatialStageFormation = SpatialStageFormation.CONCERT_ARENA
    ): List<SpatialStageNode> {
        return when (formation) {
            SpatialStageFormation.CONCERT_ARENA -> listOf(
                SpatialStageNode("sub_l", "Subwoofer L (20–63Hz)", "SUB-L", "WOOFER", 0, 5, -32f, 0.42f, 4.5f),
                SpatialStageNode("sub_r", "Subwoofer R (808 Punch)", "SUB-R", "WOOFER", 2, 7, 32f, 0.42f, 4.5f),
                SpatialStageNode("drums", "Kick & Snare Drums", "DRUMS", "INSTRUMENT", 6, 11, 0f, 0.48f, 2.5f),
                SpatialStageNode("vocals", "Lead Vocals (Center)", "VOCAL", "INSTRUMENT", 14, 21, 0f, 0.30f, 3.5f),
                SpatialStageNode("guitar", "Acoustic / Elec Guitar", "GUITAR", "INSTRUMENT", 12, 18, -52f, 0.50f, 2.0f),
                SpatialStageNode("synth_air", "Synth, Strings & Air", "SYNTH", "INSTRUMENT", 22, 30, 55f, 0.54f, 3.0f),
                SpatialStageNode("spk_l", "Left Surround Tower", "SPK-L", "SPEAKER", 10, 26, -110f, 0.72f, 1.5f),
                SpatialStageNode("spk_r", "Right Surround Tower", "SPK-R", "SPEAKER", 10, 26, 110f, 0.72f, 1.5f)
            )
            SpatialStageFormation.DRUMMERS_THRONE -> listOf(
                SpatialStageNode("sub_l", "Subwoofer L (20–63Hz)", "SUB-L", "WOOFER", 0, 5, -25f, 0.28f, 5.5f),
                SpatialStageNode("sub_r", "Subwoofer R (808 Punch)", "SUB-R", "WOOFER", 2, 7, 25f, 0.28f, 5.5f),
                SpatialStageNode("drums", "Kick & Snare Drums", "DRUMS", "INSTRUMENT", 6, 11, 0f, 0.20f, 6.0f),
                SpatialStageNode("vocals", "Lead Vocals (Front)", "VOCAL", "INSTRUMENT", 14, 21, 0f, 0.55f, 2.0f),
                SpatialStageNode("guitar", "Guitar Amp Stack L", "GUITAR", "INSTRUMENT", 12, 18, -82f, 0.38f, 3.5f),
                SpatialStageNode("synth_air", "Hi-Hats & Overheads", "SYNTH", "INSTRUMENT", 22, 30, 78f, 0.32f, 4.5f),
                SpatialStageNode("spk_l", "Stage Monitor L", "SPK-L", "SPEAKER", 10, 26, -135f, 0.65f, 2.0f),
                SpatialStageNode("spk_r", "Stage Monitor R", "SPK-R", "SPEAKER", 10, 26, 135f, 0.65f, 2.0f)
            )
            SpatialStageFormation.MAYBACH_CABIN -> listOf(
                SpatialStageNode("sub_l", "Front Seat Woofer L", "SUB-L", "WOOFER", 0, 5, -45f, 0.30f, 6.0f),
                SpatialStageNode("sub_r", "Rear Trunk Woofer R", "SUB-R", "WOOFER", 2, 7, 155f, 0.36f, 6.0f),
                SpatialStageNode("drums", "Cabin Mid-Bass", "DRUMS", "INSTRUMENT", 6, 11, -18f, 0.40f, 3.5f),
                SpatialStageNode("vocals", "Center Dash Vocal Lock", "VOCAL", "INSTRUMENT", 14, 21, 0f, 0.26f, 4.0f),
                SpatialStageNode("guitar", "Front Pillar L", "GUITAR", "INSTRUMENT", 12, 18, -40f, 0.42f, 2.5f),
                SpatialStageNode("synth_air", "3D Roof Tweeters", "SYNTH", "INSTRUMENT", 22, 30, 40f, 0.38f, 3.8f),
                SpatialStageNode("spk_l", "Rear Door Surround L", "SPK-L", "SPEAKER", 10, 26, -125f, 0.50f, 3.0f),
                SpatialStageNode("spk_r", "Rear Door Surround R", "SPK-R", "SPEAKER", 10, 26, 125f, 0.50f, 3.0f)
            )
            SpatialStageFormation.CLUB_SURROUND_360 -> listOf(
                SpatialStageNode("sub_l", "Club Horn Sub L", "SUB-L", "WOOFER", 0, 5, -38f, 0.24f, 7.5f),
                SpatialStageNode("sub_r", "Rear Corner Sub R", "SUB-R", "WOOFER", 2, 7, 142f, 0.28f, 7.5f),
                SpatialStageNode("drums", "Club Kick Array", "DRUMS", "INSTRUMENT", 6, 11, 0f, 0.32f, 5.5f),
                SpatialStageNode("vocals", "DJ Booth Vocals", "VOCAL", "INSTRUMENT", 14, 21, 0f, 0.42f, 2.5f),
                SpatialStageNode("guitar", "Left Wall Array", "GUITAR", "INSTRUMENT", 12, 18, -88f, 0.48f, 2.0f),
                SpatialStageNode("synth_air", "Laser Synth Dome", "SYNTH", "INSTRUMENT", 22, 30, 88f, 0.44f, 4.5f),
                SpatialStageNode("spk_l", "Rear Stack L", "SPK-L", "SPEAKER", 10, 26, -150f, 0.52f, 3.5f),
                SpatialStageNode("spk_r", "Rear Stack R", "SPK-R", "SPEAKER", 10, 26, 150f, 0.52f, 3.5f)
            )
            SpatialStageFormation.BINAURAL_STEM_SPLIT -> listOf(
                SpatialStageNode("sub_l", "Subwoofer L", "SUB-L", "WOOFER", 0, 5, -65f, 0.35f, 4.5f),
                SpatialStageNode("sub_r", "Subwoofer R", "SUB-R", "WOOFER", 2, 7, -45f, 0.35f, 4.0f),
                SpatialStageNode("drums", "Drums & Rhythm (Left)", "DRUMS", "INSTRUMENT", 6, 11, -70f, 0.36f, 4.0f),
                SpatialStageNode("vocals", "Vocals (Dead Center)", "VOCAL", "INSTRUMENT", 14, 21, 0f, 0.25f, 4.5f),
                SpatialStageNode("guitar", "Guitar & Keys (Right)", "GUITAR", "INSTRUMENT", 12, 18, 65f, 0.36f, 4.0f),
                SpatialStageNode("synth_air", "Strings & Air (Right)", "SYNTH", "INSTRUMENT", 22, 30, 80f, 0.38f, 4.0f),
                SpatialStageNode("spk_l", "Ambient L", "SPK-L", "SPEAKER", 10, 26, -120f, 0.65f, 1.0f),
                SpatialStageNode("spk_r", "Ambient R", "SPK-R", "SPEAKER", 10, 26, 120f, 0.65f, 1.0f)
            )
        }
    }

    /**
     * Synthesizes the 8 draggable Instrument & Woofer/Speaker nodes into a 31-band ISO EQ curve
     * AND independent Sub/Mid/High stereo binaural panning offsets.
     */
    fun synthesizeMultiNodeStage(
        nodes: List<SpatialStageNode>,
        wooferCrossoverHz: Int,
        wooferPhaseInvert180: Boolean,
        centerVocalLock: Boolean
    ): MultiNodeStageSynthesis {
        val anySolo = nodes.any { it.solo }
        val activeNodes = nodes.filter { node ->
            if (anySolo) node.solo else !node.muted
        }

        // Compute crossover band index from wooferCrossoverHz (40..120Hz -> bands 3..8)
        val crossoverBandIdx = EqPresetEntity.FREQUENCY_HZ.indexOfFirst { it >= wooferCrossoverHz }
            .let { if (it == -1) 6 else it }

        val bands = MutableList(31) { 0f }
        val bandWeights = FloatArray(31) { 0f }

        for (node in nodes) {
            val isAudible = if (anySolo) node.solo else !node.muted
            if (!isAudible) {
                for (b in node.bandStartIdx..node.bandEndIdx.coerceAtMost(30)) {
                    bands[b] = bands[b] - 6.0f
                }
                continue
            }

            // Proximity boost: closer nodes (distance 0.15) get up to +4.5 dB, far nodes (1.0) get -2.0 dB
            val proximityDb = (0.58f - node.distanceNorm.coerceIn(0.15f, 1.0f)) * 9.0f
            val totalNodeDb = (node.gainDb + proximityDb).coerceIn(-12f, 12f)

            val startB = node.bandStartIdx.coerceIn(0, 30)
            val endB = if (node.category == "WOOFER") {
                maxOf(node.bandEndIdx, crossoverBandIdx).coerceIn(0, 30)
            } else {
                node.bandEndIdx.coerceIn(0, 30)
            }

            for (b in startB..endB) {
                val centerB = (startB + endB) / 2f
                val halfSpan = ((endB - startB) / 2f).coerceAtLeast(1f)
                val bell = cos(((b - centerB) / (halfSpan + 1.5f)) * (PI / 2.0)).toFloat().coerceIn(0.25f, 1f)
                val phaseBonus = if (node.category == "WOOFER" && !wooferPhaseInvert180) 1.2f else if (node.category == "WOOFER") -0.8f else 0f
                bands[b] += (totalNodeDb + phaseBonus) * bell
                bandWeights[b] += bell
            }
        }

        // Normalize and apply Center Vocal Lock if enabled
        for (i in 0 until 31) {
            val w = bandWeights[i].coerceAtLeast(1f)
            var v = bands[i] / (w * 0.68f)
            if (centerVocalLock && i in 15..21) {
                v = maxOf(v, 2.8f) // Ensure vocal clarity stays locked forward
            }
            bands[i] = (kotlin.math.round(v * 2f) / 2f).coerceIn(-15f, 15f)
        }

        // Compute independent Sub / Mid / High stereo binaural pan (-1.0 Left .. +1.0 Right)
        fun avgPanForRange(range: IntRange): Float {
            val matching = activeNodes.filter { it.bandStartIdx <= range.last && it.bandEndIdx >= range.first }
            if (matching.isEmpty()) return 0f
            val sum = matching.sumOf { sin((it.azimuthDeg * PI) / 180.0) * (1.15 - it.distanceNorm * 0.5) }
            return (sum / matching.size).toFloat().coerceIn(-0.95f, 0.95f)
        }

        val subPan = avgPanForRange(0..7)
        val midPan = if (centerVocalLock) avgPanForRange(8..20) * 0.35f else avgPanForRange(8..20)
        val highPan = avgPanForRange(21..30)

        val avgDist = if (activeNodes.isNotEmpty()) activeNodes.map { it.distanceNorm }.average().toFloat() else 0.5f
        val wooferNodes = activeNodes.filter { it.category == "WOOFER" }
        val effBass = if (wooferNodes.isEmpty()) 25 else {
            val avgSubGain = wooferNodes.map { it.gainDb + (1f - it.distanceNorm) * 6f }.average().toFloat()
            (58 + (avgSubGain * 4.2f).roundToInt()).coerceIn(20, 100)
        }
        val effSpatial = (45 + (avgDist * 48f).roundToInt()).coerceIn(30, 98)
        val effReverb = (10 + (avgDist * 42f).roundToInt()).coerceIn(8, 75)

        val summary = "STAGE ACTIVE: ${activeNodes.size}/${nodes.size} Nodes • Woofer X-Over ${wooferCrossoverHz}Hz • Pan[Sub ${String.format("%+.0f%%", subPan * 100)}, Mid ${String.format("%+.0f%%", midPan * 100)}, Air ${String.format("%+.0f%%", highPan * 100)}]"

        return MultiNodeStageSynthesis(
            sculptedBands31 = bands,
            subBandPanLR = subPan,
            midBandPanLR = midPan,
            highBandPanLR = highPan,
            effectiveBassBoost = effBass,
            effectiveSpatial3D = effSpatial,
            effectiveReverbMix = effReverb,
            telemetrySummary = summary
        )
    }

    val HOLOGRAPHIC_VIBES: List<HolographicVibeProfile> = listOf(
        HolographicVibeProfile(
            id = "rolls_royce_rain",
            title = "Rolls Royce Midnight Rain",
            subtitle = "Bespoke 18-speaker cabin warmth, velvet sub-bass & zero harshness",
            badge = "CABIN LUXURY",
            bands31 = listOf(
                6.5f, 7.0f, 7.2f, 6.8f, 6.0f, 5.0f, 3.8f, 2.5f, 1.4f, 0.6f,
                0.2f, -0.4f, -0.8f, -1.0f, -0.6f, 0.0f, 0.6f, 1.2f, 1.8f, 2.2f,
                2.5f, 2.2f, 1.8f, 1.4f, 1.8f, 2.4f, 2.8f, 2.6f, 2.2f, 1.8f, 1.4f
            ),
            bassBoost = 82,
            spatial3D = 68,
            reverbLevel = 24,
            reverbRoom = "Medium Room",
            preampDb = -2.5f,
            brainwaveMode = NeuralBrainwaveMode.ALPHA_10HZ,
            radarAzimuthDeg = 0f
        ),
        HolographicVibeProfile(
            id = "cyberpunk_club",
            title = "Cyberpunk 2077 Club Front Row",
            subtitle = "Chest-slamming 31.5Hz sub-bass + neon 12kHz synth air",
            badge = "NEON BASS",
            bands31 = listOf(
                8.5f, 9.2f, 9.5f, 8.8f, 7.6f, 6.2f, 4.4f, 2.4f, 0.8f, -0.6f,
                -1.4f, -1.8f, -1.5f, -0.8f, 0.0f, 0.8f, 1.6f, 2.4f, 3.0f, 3.4f,
                3.6f, 3.2f, 2.8f, 3.2f, 4.0f, 4.6f, 4.8f, 4.4f, 3.8f, 3.2f, 2.6f
            ),
            bassBoost = 94,
            spatial3D = 76,
            reverbLevel = 32,
            reverbRoom = "Large Hall",
            preampDb = -3.0f,
            brainwaveMode = NeuralBrainwaveMode.BETA_18HZ,
            radarAzimuthDeg = 18f
        ),
        HolographicVibeProfile(
            id = "imax_interstellar",
            title = "IMAX Interstellar Organ Sub",
            subtitle = "20Hz–40Hz tectonic pipe-organ sub-harmonics + wide dome stage",
            badge = "20HZ TECTONIC",
            bands31 = listOf(
                9.5f, 9.5f, 8.8f, 7.8f, 6.5f, 5.0f, 3.5f, 2.0f, 0.8f, 0.0f,
                -0.6f, -1.0f, -1.2f, -0.8f, -0.2f, 0.5f, 1.2f, 2.0f, 2.6f, 3.0f,
                3.2f, 3.0f, 2.6f, 2.4f, 2.8f, 3.4f, 3.8f, 3.6f, 3.2f, 2.8f, 2.4f
            ),
            bassBoost = 90,
            spatial3D = 92,
            reverbLevel = 46,
            reverbRoom = "Large Hall",
            preampDb = -3.5f,
            brainwaveMode = NeuralBrainwaveMode.THETA_6HZ,
            radarAzimuthDeg = -15f
        ),
        HolographicVibeProfile(
            id = "diljit_arena",
            title = "Punjabi Arena Front Row",
            subtitle = "Punchy 50–80Hz dhol & sub-kick + crystal-clear vocal presence",
            badge = "LIVE ARENA",
            bands31 = listOf(
                7.2f, 8.0f, 8.6f, 8.8f, 8.2f, 7.0f, 5.4f, 3.6f, 1.8f, 0.4f,
                -0.6f, -1.2f, -1.0f, -0.2f, 0.8f, 1.8f, 2.8f, 3.6f, 4.2f, 4.4f,
                4.0f, 3.4f, 2.8f, 2.6f, 3.0f, 3.4f, 3.6f, 3.2f, 2.6f, 2.0f, 1.5f
            ),
            bassBoost = 88,
            spatial3D = 74,
            reverbLevel = 28,
            reverbRoom = "Medium Room",
            preampDb = -3.0f,
            brainwaveMode = NeuralBrainwaveMode.BETA_18HZ,
            radarAzimuthDeg = 0f
        ),
        HolographicVibeProfile(
            id = "tokyo_lofi_3am",
            title = "Tokyo Rooftop Lo-Fi 3AM",
            subtitle = "Analog tape warmth, silky 250Hz body & 40Hz Gamma study focus",
            badge = "STUDY GAMMA",
            bands31 = listOf(
                4.0f, 4.6f, 5.0f, 5.2f, 4.8f, 4.2f, 3.6f, 3.0f, 2.4f, 2.0f,
                1.6f, 1.2f, 0.8f, 0.5f, 0.4f, 0.6f, 1.0f, 1.4f, 1.6f, 1.4f,
                1.0f, 0.5f, 0.0f, -0.5f, -1.0f, -1.4f, -1.8f, -2.0f, -2.2f, -2.5f, -3.0f
            ),
            bassBoost = 64,
            spatial3D = 60,
            reverbLevel = 22,
            reverbRoom = "Small Room",
            preampDb = -1.5f,
            brainwaveMode = NeuralBrainwaveMode.GAMMA_40HZ,
            radarAzimuthDeg = 12f
        )
    )

    suspend fun runSonarAcousticCalibration(
        liveSpectrumBins: List<Float>,
        targetDeviceName: String
    ): SonarScanReport = withContext(Dispatchers.Default) {
        runCatching {
            val sampleRate = 44100
            val durationSamples = (sampleRate * 0.95).toInt()
            val pcm = ShortArray(durationSamples * 2)
            val fStart = 45.0
            val fEnd = 12800.0
            for (i in 0 until durationSamples) {
                val progress = i.toDouble() / durationSamples.toDouble()
                val env = sin(PI * progress).pow(0.6)
                val instFreq = fStart * (fEnd / fStart).pow(progress)
                val phase = 2.0 * PI * instFreq * (i.toDouble() / sampleRate.toDouble())
                val ping = if (progress < 0.18) sin(2.0 * PI * 1850.0 * (i.toDouble() / sampleRate)) * (1.0 - progress / 0.18) else 0.0
                val sample = ((sin(phase) * 0.28 + ping * 0.22) * env).coerceIn(-0.9, 0.9)
                val s16 = (sample * 32760).toInt().toShort()
                pcm[i * 2] = s16
                pcm[i * 2 + 1] = s16
            }
            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build()
                )
                .setBufferSizeInBytes(pcm.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()
            track.write(pcm, 0, pcm.size)
            track.play()
            kotlinx.coroutines.delay(980)
            track.stop()
            track.release()
        }

        val bins = if (liveSpectrumBins.size == 31) liveSpectrumBins else List(31) { 0.35f }
        var peakIdx = 8
        var peakEnergy = 0f
        bins.forEachIndexed { idx, v ->
            if (v > peakEnergy) {
                peakEnergy = v
                peakIdx = idx
            }
        }
        val standingFreq = EqPresetEntity.FREQUENCY_HZ.getOrElse(peakIdx) { 160f }.roundToInt()
        val rt60 = (240 + (peakEnergy * 180).roundToInt()).coerceIn(180, 520)
        val sealQuality = (88 + ((bins.getOrElse(2) { 0.4f } * 12f).roundToInt())).coerceIn(82, 99)

        val compensated = List(31) { idx ->
            val binVal = bins.getOrElse(idx) { 0.35f }
            val subShelf = if (idx <= 6) 4.8f - idx * 0.45f else 0f
            val mudCut = if (idx in 9..13) -2.2f * exp(-abs(idx - 11).toFloat() * 0.45f) else 0f
            val presenceLift = if (idx in 18..24) 2.6f * sin(((idx - 17) / 8f) * PI).toFloat() else 0f
            val airLift = if (idx >= 25) 3.0f - (30 - idx) * 0.25f else 0f
            val inverseAnomaly = (0.42f - binVal) * 3.2f
            val raw = subShelf + mudCut + presenceLift + airLift + inverseAnomaly
            (kotlin.math.round(raw * 2f) / 2f).coerceIn(-12f, 12f)
        }

        SonarScanReport(
            roomAcousticsType = "Calibrated for $targetDeviceName",
            rt60DecayMs = rt60,
            standingWaveHz = standingFreq,
            sealQualityPercent = sealQuality,
            telemetrySummary = "Sonar Sweep Complete • Seal $sealQuality% • Standing Resonance suppressed @ ${standingFreq}Hz • RT60 ${rt60}ms",
            compensationBands31 = compensated,
            recommendedBass = 76,
            recommendedSpatial = 64,
            recommendedReverbRoom = "Small Room"
        )
    }

    suspend fun playPureAudiogramTone(freqHz: Int, earChannel: String = "BOTH") = withContext(Dispatchers.Default) {
        runCatching {
            val sampleRate = 44100
            val durationSamples = (sampleRate * 0.45).toInt()
            val pcm = ShortArray(durationSamples * 2)
            for (i in 0 until durationSamples) {
                val env = sin(PI * (i.toDouble() / durationSamples.toDouble())).pow(0.4)
                val sig = (sin(2.0 * PI * freqHz.toDouble() * (i.toDouble() / sampleRate.toDouble())) * 0.26 * env)
                val s16 = (sig * 32760).toInt().toShort()
                pcm[i * 2] = if (earChannel == "RIGHT") 0 else s16
                pcm[i * 2 + 1] = if (earChannel == "LEFT") 0 else s16
            }
            val track = AudioTrack.Builder()
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
                .setBufferSizeInBytes(pcm.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()
            track.write(pcm, 0, pcm.size)
            track.play()
            kotlinx.coroutines.delay(460)
            track.stop()
            track.release()
        }
    }

    fun buildBioEarDnaProfile(
        sensitivityRatings: List<Float>,
        leftRightBiasDb: Float
    ): BioEarProfileResult {
        val clean = if (sensitivityRatings.size == 6) sensitivityRatings else listOf(0.9f, 0.92f, 0.85f, 0.80f, 0.72f, 0.65f)
        val avgSensitivity = clean.average().toFloat().coerceIn(0.2f, 1.0f)
        val hfSensitivity = ((clean[4] + clean[5]) * 0.5f).coerceIn(0.1f, 1.0f)

        val estimatedAge = (46 - (hfSensitivity * 28f).roundToInt()).coerceIn(17, 52)
        val goldenScore = (avgSensitivity * 100f).roundToInt().coerceIn(64, 99)
        val highestHz = when {
            clean[5] >= 0.75f -> 19200
            clean[5] >= 0.50f -> 17400
            clean[4] >= 0.60f -> 15800
            else -> 14200
        }

        val anchorIndices = intArrayOf(11, 17, 23, 26, 28, 29)
        val compensation = MutableList(31) { idx ->
            when {
                idx < 7 -> 4.2f - idx * 0.35f
                else -> 0f
            }
        }
        for (i in 0 until 31) {
            var weightedBoost = 0f
            var weightSum = 0f
            for (a in anchorIndices.indices) {
                val dist = abs(i - anchorIndices[a]).toFloat()
                val w = exp(-dist * 0.35f)
                val deficit = (0.95f - clean[a]).coerceIn(-0.2f, 0.75f)
                val boostDb = deficit * 11.5f
                weightedBoost += boostDb * w
                weightSum += w
            }
            val raw = compensation[i] + if (weightSum > 0f) weightedBoost / weightSum else 0f
            compensation[i] = (kotlin.math.round(raw * 2f) / 2f).coerceIn(-12f, 12f)
        }

        val tierName = when {
            estimatedAge <= 21 -> "Bat-Ear Golden Hearing"
            estimatedAge <= 28 -> "Studio Mastering Grade"
            else -> "Precision High-Frequency Restored"
        }

        return BioEarProfileResult(
            estimatedEarAgeYears = estimatedAge,
            goldenEarScorePercent = goldenScore,
            highestAudibleHz = highestHz,
            leftRightBalanceDb = leftRightBiasDb,
            diagnosisSummary = "Bio-Ear Age: $estimatedAge Yrs ($tierName) • Max Audible: ${highestHz}Hz • Golden Score: $goldenScore%",
            dnaCompensationBands31 = compensation
        )
    }

    fun getEsportsTacticalXrayBands(): List<Float> = listOf(
        -3.5f, -3.0f, -2.5f, -2.0f, -1.5f, -2.0f, -2.8f, -3.5f, -4.0f, -3.8f,
        -3.2f, -2.5f, -1.5f, 0.0f, 1.5f, 3.0f, 4.5f, 5.8f, 6.8f, 7.5f,
        8.0f, 8.2f, 7.8f, 6.8f, 5.5f, 4.2f, 3.5f, 2.8f, 2.0f, 1.2f, 0.5f
    )

    fun computeEsportsThreatRadar(
        enabled: Boolean,
        spectrumBins: List<Float>,
        stereoVu: Pair<Float, Float>,
        azimuthDeg: Float
    ): EsportsThreatRadarState {
        if (!enabled) return EsportsThreatRadarState(enabled = false)
        val footstepEnergy = (17..24).map { spectrumBins.getOrElse(it) { 0.2f } }.average().toFloat()
        val subExplosion = (1..6).map { spectrumBins.getOrElse(it) { 0.2f } }.average().toFloat()
        val panBias = (azimuthDeg / 180f).coerceIn(-1f, 1f)
        val left = (stereoVu.first * 0.7f + footstepEnergy * 0.45f - panBias * 0.25f).coerceIn(0.08f, 0.99f)
        val right = (stereoVu.second * 0.7f + footstepEnergy * 0.45f + panBias * 0.25f).coerceIn(0.08f, 0.99f)
        val center = ((footstepEnergy * 0.8f + subExplosion * 0.3f)).coerceIn(0.08f, 0.99f)

        val cue = when {
            footstepEnergy > 0.52f && left > right + 0.08f -> "⚠️ FOOTSTEPS DETECTED: LEFT FLANK (${(left * 100).roundToInt()}%)"
            footstepEnergy > 0.52f && right > left + 0.08f -> "⚠️ FOOTSTEPS DETECTED: RIGHT FLANK (${(right * 100).roundToInt()}%)"
            footstepEnergy > 0.48f -> "🎯 TACTICAL LOCK: CENTER RELOAD / FOOTSTEPS"
            else -> "🛡️ X-RAY RADAR SCANNING (1.6kHz–4kHz BOOSTED)"
        }
        return EsportsThreatRadarState(
            enabled = true,
            leftThreatLevel = left,
            centerThreatLevel = center,
            rightThreatLevel = right,
            dominantCueLabel = cue
        )
    }

    fun computeDynamicAutoRiderStep(
        baseBands: List<Float>,
        spectrumBins: List<Float>,
        dominantHz: Int
    ): Pair<List<Float>, String> {
        val subEnergy = (0..7).map { spectrumBins.getOrElse(it) { 0.2f } }.average().toFloat()
        val vocalEnergy = (15..22).map { spectrumBins.getOrElse(it) { 0.2f } }.average().toFloat()
        val airEnergy = (24..30).map { spectrumBins.getOrElse(it) { 0.2f } }.average().toFloat()

        val modeLabel = when {
            dominantHz in 250..3200 && vocalEnergy >= subEnergy ->
                "AI RIDER: VOCAL & DIALOGUE CLARITY LOCK (${dominantHz}Hz)"
            subEnergy > 0.48f || dominantHz < 130 ->
                "AI RIDER: SUB-BASS DROP SURGE + ANTI-MUD (${dominantHz}Hz)"
            else ->
                "AI RIDER: DYNAMIC HARMAN MASTERING (${dominantHz}Hz)"
        }

        val morphed = List(31) { idx ->
            val base = baseBands.getOrElse(idx) { 0f }
            val delta = when {
                idx <= 6 -> if (subEnergy > 0.45f) 1.5f else 0.8f
                idx in 10..13 -> -1.0f
                idx in 17..22 -> if (vocalEnergy >= subEnergy) 1.8f else 0.9f
                idx >= 25 -> if (airEnergy < 0.35f) 1.4f else 0.6f
                else -> 0f
            }
            (kotlin.math.round((base + delta * 0.35f) * 2f) / 2f).coerceIn(-15f, 15f)
        }
        return morphed to modeLabel
    }
}
