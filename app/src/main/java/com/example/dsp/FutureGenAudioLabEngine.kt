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

    /**
     * Synthesizes a 31-Band EQ curve + Sub/Mid/High stem scaling from the 4-Stem Live De-Mixer
     * (Vocals, Drums, Sub-Bass, Melody/Air) + Missing-Fundamental 18" Sub-Harmonic Synthesizer
     * + Analog Tube/Tape Warmth + Tinnitus Notch-Shield + Speed-Adaptive Cruise.
     */
    fun synthesizeHyperMatrixCurve(
        baseBands: List<Float>,
        subHarmonicSynthPercent: Int,
        stemVocalPercent: Int,
        stemDrumPercent: Int,
        stemBassPercent: Int,
        stemMelodyPercent: Int,
        speedCruiseMode: SpeedCruiseMode,
        analogMode: AnalogSaturationMode,
        analogDrivePercent: Int,
        tinnitusShieldEnabled: Boolean,
        tinnitusNotchHz: Int
    ): Pair<List<Float>, String> {
        val subSynthDb = (subHarmonicSynthPercent.coerceIn(0, 100) / 100f) * 6.5f
        val bassStemDb = ((stemBassPercent.coerceIn(0, 200) - 100) / 100f) * 8.5f
        val drumStemDb = ((stemDrumPercent.coerceIn(0, 200) - 100) / 100f) * 7.0f
        val vocalStemDb = ((stemVocalPercent.coerceIn(0, 200) - 100) / 100f) * 9.5f
        val melodyStemDb = ((stemMelodyPercent.coerceIn(0, 200) - 100) / 100f) * 7.5f
        val driveNorm = analogDrivePercent.coerceIn(0, 100) / 100f

        val notchBandIdx = EqPresetEntity.FREQUENCY_HZ.indexOfFirst { it >= tinnitusNotchHz }
            .let { if (it == -1) 25 else it }

        val out = List(31) { idx ->
            var g = baseBands.getOrElse(idx) { 0f }

            // 1. Missing-Fundamental 18" Sub-Harmonic Synthesizer (20Hz-50Hz sub-octave + 80Hz harmonic)
            if (idx in 0..4) {
                g += subSynthDb * (1f - idx * 0.14f)
            } else if (idx in 5..7) {
                g += subSynthDb * 0.45f
            }

            // 2. 4-Stem De-Mixer (Bass: 0..6, Drums: 6..10 & 20..22, Vocals: 14..20, Melody/Air: 11..14 & 23..30)
            when (idx) {
                in 0..5 -> g += bassStemDb
                in 6..9 -> g += drumStemDb * 0.85f + bassStemDb * 0.35f
                in 10..13 -> g += melodyStemDb * 0.65f
                in 14..20 -> g += vocalStemDb
                in 21..23 -> g += drumStemDb * 0.55f + vocalStemDb * 0.35f
                else -> g += melodyStemDb * 0.9f
            }

            // 3. Speed-Adaptive Car / Bike / Gym Cruise compensation
            g += when (speedCruiseMode) {
                SpeedCruiseMode.OFF -> 0f
                SpeedCruiseMode.CITY_CRUISE_40KMH -> if (idx <= 6) 2.2f else if (idx in 16..21) 1.5f else 0f
                SpeedCruiseMode.HIGHWAY_TURBO_100KMH -> if (idx <= 7) 4.2f else if (idx in 16..22) 2.8f else 0.8f
                SpeedCruiseMode.GYM_ADRENALINE_SPRINT -> if (idx <= 8) 3.8f else if (idx >= 23) 2.5f else 0f
            }

            // 4. Analog Vacuum Tube / Tape / Vinyl Harmonic Contour
            g += when (analogMode) {
                AnalogSaturationMode.CLEAN_DIGITAL -> 0f
                AnalogSaturationMode.TUBE_12AX7_WARMTH ->
                    if (idx in 4..12) 2.4f * driveNorm else if (idx in 15..20) 1.6f * driveNorm else -0.6f * driveNorm
                AnalogSaturationMode.ABBEY_TAPE_GLUE ->
                    if (idx in 2..8) 2.8f * driveNorm else if (idx >= 25) -1.2f * driveNorm else 0.8f * driveNorm
                AnalogSaturationMode.VINYL_AIR_EXCITER ->
                    if (idx >= 23) 3.6f * driveNorm else if (idx in 3..7) 1.8f * driveNorm else 0f
            }

            // 5. Smart Tinnitus Notch-Shield & Sibilance Guardian
            if (tinnitusShieldEnabled) {
                val dist = abs(idx - notchBandIdx)
                if (dist == 0) g -= 6.5f
                else if (dist == 1) g -= 3.5f
                else if (idx in 23..26) g = minOf(g, 1.5f)
            }

            (kotlin.math.round(g * 2f) / 2f).coerceIn(-15f, 15f)
        }

        val summary = buildString {
            append("2030 HYPER-MATRIX: ")
            append("18\" SubSynth ${subHarmonicSynthPercent}% • ")
            append("Stems[V:${stemVocalPercent}% D:${stemDrumPercent}% B:${stemBassPercent}% M:${stemMelodyPercent}%]")
            if (speedCruiseMode != SpeedCruiseMode.OFF) append(" • ${speedCruiseMode.badge}")
            if (analogMode != AnalogSaturationMode.CLEAN_DIGITAL) append(" • ${analogMode.badge}")
            if (tinnitusShieldEnabled) append(" • Tinnitus Shield ${tinnitusNotchHz}Hz")
        }

        return out to summary
    }

    /**
     * Captures a Reference Track's 31-band spectral DNA from live RTA bins and current curve,
     * allowing 1-tap cloning of any favorite song's mix balance onto other tracks.
     */
    fun captureReferenceTrackDna(
        currentBands: List<Float>,
        liveSpectrumBins: List<Float>,
        trackLabel: String
    ): Pair<List<Float>, String> {
        val bins = if (liveSpectrumBins.size == 31) liveSpectrumBins else List(31) { 0.42f }
        val cloned = List(31) { idx ->
            val binEnergy = bins[idx].coerceIn(0.1f, 0.95f)
            val base = currentBands.getOrElse(idx) { 0f }
            val spectralSignature = (binEnergy - 0.42f) * 10.5f
            val target = (base * 0.55f + spectralSignature * 0.45f) + when {
                idx <= 5 -> 2.2f
                idx in 16..21 -> 1.6f
                idx >= 25 -> 1.4f
                else -> 0f
            }
            (kotlin.math.round(target * 2f) / 2f).coerceIn(-15f, 15f)
        }
        val summary = "🧬 REFERENCE DNA LOCKED: '$trackLabel' • 31-Band Tonality & Sub/Vocal Envelope Cloned"
        return cloned to summary
    }

    /**
     * Synthesizes a 6-Part "Out-Of-Head Room Externalizer (Smyth A16 / OOHL Style)" 31-band curve
     * and spatial parameters so headphones/earbuds sound like physical loudspeakers playing
     * 0.5m to 6.0m outside in the room rather than inside the ears/skull.
     */
    fun synthesizeOutOfHeadExternalizer(
        baseBands: List<Float>,
        mode: OutOfHeadExternalizerMode,
        distanceMeters: Float,
        earlyReflectionPercent: Int,
        pinnaChestHrtfEnabled: Boolean,
        subFloorCouplerEnabled: Boolean
    ): OutOfHeadSynthesisResult {
        if (mode == OutOfHeadExternalizerMode.OFF) {
            return OutOfHeadSynthesisResult(
                sculptedBands31 = EqPresetEntity.interpolateTo31Bands(baseBands),
                effectiveBassBoost = 72,
                effectiveSpatial3D = 38,
                effectiveReverbLevel = 14,
                effectiveReverbRoom = "Small Room",
                effectiveCrossfeedPct = 20,
                effectivePreampDb = -1.5f,
                telemetrySummary = "OUT-OF-HEAD BYPASSED • Standard In-Ear Direct Headphone Playback"
            )
        }

        val cleanBase = EqPresetEntity.interpolateTo31Bands(baseBands)
        val distClamped = distanceMeters.coerceIn(0.5f, 6.0f)
        val distNorm = ((distClamped - 0.5f) / 5.5f).coerceIn(0f, 1f)
        val reflectionNorm = (earlyReflectionPercent.coerceIn(0, 100) / 100f)

        val outBands = List(31) { idx ->
            var g = cleanBase.getOrElse(idx) { 0f } * 0.65f

            // 1. Sub-Bass Floor & Wall Pressure Coupler (20Hz - 100Hz, bands 0..7)
            // Converts dry ear-canal bass into a physical floor/room acoustic wave
            if (subFloorCouplerEnabled) {
                when (idx) {
                    in 0..2 -> g += 2.6f + reflectionNorm * 1.6f
                    in 3..5 -> g += 3.8f + reflectionNorm * 2.2f // 40Hz-63Hz physical floor resonance
                    in 6..7 -> g += 2.4f + reflectionNorm * 1.4f
                }
            }

            // 2. Torso / Chest / Shoulder Acoustic Bounce (160Hz - 250Hz, bands 9..11)
            // + Desk/Floor first-reflection comb dip (315Hz - 400Hz, bands 12..13)
            if (pinnaChestHrtfEnabled) {
                when (idx) {
                    in 9..11 -> g += 2.0f + reflectionNorm * 1.2f  // Chest & shoulder warm bounce
                    in 12..13 -> g -= 1.4f                         // Floor/desk cancellation cue
                    in 19..22 -> g += 2.2f * (1f - distNorm * 0.35f) // 2kHz-3.15kHz frontal speaker throw
                    in 25..26 -> g -= 4.4f                         // 6.3kHz-8kHz Outer-Ear Pinna Concha Depth Notch
                }
            }

            // 3. Acoustic Air Absorption & Distance Throw (0.5m -> 6.0m)
            // Further speakers have natural HF air damping above 10kHz (bands 27..30)
            if (idx >= 27) {
                val airRollOff = distNorm * 3.4f + (idx - 27) * distNorm * 0.7f
                g -= airRollOff
            }

            // 4. Mode-Specific Room & Doorway Acoustic Transfer Function
            when (mode) {
                OutOfHeadExternalizerMode.STUDIO_DESK_1_2M -> {
                    if (idx in 17..22) g += 1.4f
                    if (idx in 4..7) g += 1.2f
                }
                OutOfHeadExternalizerMode.LIVING_ROOM_SOFA_3M -> {
                    if (idx in 2..6) g += 2.2f
                    if (idx in 8..10) g += 1.5f
                }
                OutOfHeadExternalizerMode.IMAX_SCREEN_6M -> {
                    if (idx in 0..5) g += 3.2f
                    if (idx in 16..21) g += 1.8f
                }
                OutOfHeadExternalizerMode.NEXT_ROOM_PARTY -> {
                    // Sounds like a loud speaker playing outside in the next room / hallway
                    if (idx in 2..7) g += 4.5f
                    if (idx in 8..11) g += 2.2f
                    if (idx >= 20) g -= (idx - 19) * 1.15f
                }
                OutOfHeadExternalizerMode.OPEN_AIR_LAWN -> {
                    // Zero room-wall boominess, deep open-air sub throw & crisp stage air
                    if (idx in 1..5) g += 2.8f
                    if (idx in 10..14) g -= 1.6f
                    if (idx in 22..24) g += 1.5f
                }
                OutOfHeadExternalizerMode.OFF -> Unit
            }

            (kotlin.math.round(g * 2f) / 2f).coerceIn(-15f, 15f)
        }

        val effBass = (68 + (if (subFloorCouplerEnabled) 14 else 0) + (reflectionNorm * 12).roundToInt()).coerceIn(35, 98)
        val effSpatial = (64 + (distNorm * 28).roundToInt()).coerceIn(45, 98)
        val effReverb = (mode.defaultWallBouncePct * 0.65f + reflectionNorm * 32f).roundToInt().coerceIn(12, 88)
        val effCrossfeed = (42 + (distNorm * 36).roundToInt()).coerceIn(30, 88)
        val effPreamp = (-1.5f - distNorm * 1.5f).coerceIn(-4.5f, 0f)

        val summary = buildString {
            append("🔊 OUT-OF-HEAD [${mode.badge}]: ")
            append(String.format(java.util.Locale.US, "%.1fm Throw", distClamped))
            append(" • Wall Bounce ${earlyReflectionPercent}%")
            append(" • ITD 340µs Crossfeed ${effCrossfeed}%")
            if (pinnaChestHrtfEnabled) append(" • Pinna 8kHz Notch ON")
            if (subFloorCouplerEnabled) append(" • Floor Sub-Wave ON")
        }

        return OutOfHeadSynthesisResult(
            sculptedBands31 = outBands,
            effectiveBassBoost = effBass,
            effectiveSpatial3D = effSpatial,
            effectiveReverbLevel = effReverb,
            effectiveReverbRoom = mode.recommendedRoom,
            effectiveCrossfeedPct = effCrossfeed,
            effectivePreampDb = effPreamp,
            telemetrySummary = summary
        )
    }

    /**
     * Synthesizes the "Global Triad Flagship Hi-Fi Suite (🇯🇵 JAPAN • 🇨🇳 CHINA • 🇺🇸 USA)"
     * combining:
     * - 🇯🇵 Japan: Sony DSEE Ultimate 14–20kHz Harmonic Restorer + Yamaha AFC / K2HD Silky Vocal + 360 Spherical Height
     * - 🇨🇳 China: Holo Discrete R2R Ladder DAC Organic Body + Beryllium Dynamic Bass Slam + Electrostatic Micro-Air
     * - 🇺🇸 USA: Las Vegas Sphere 167K LFE Sub Array + Dirac Impulse Phase Alignment + THX Fletcher-Munson Loudness Contour
     */
    fun synthesizeGlobalTriadSuite(
        baseBands: List<Float>,
        mode: GlobalTriadMode,
        japanDseePercent: Int,
        chinaR2rBerylliumPercent: Int,
        usaDiracSpherePercent: Int,
        fletcherMunsonEnabled: Boolean,
        systemVolumePercent: Int = 70
    ): GlobalTriadSynthesisResult {
        if (mode == GlobalTriadMode.OFF) {
            return GlobalTriadSynthesisResult(
                sculptedBands31 = EqPresetEntity.interpolateTo31Bands(baseBands),
                effectiveBassBoost = 76,
                effectiveSpatial3D = 42,
                effectiveReverbLevel = 16,
                effectiveReverbRoom = "Small Room",
                effectiveCrossfeedPct = 28,
                effectiveAnalogDrivePct = 25,
                telemetrySummary = "GLOBAL TRIAD STANDBY • Select 🇯🇵 Japan, 🇨🇳 China, 🇺🇸 USA or 🌐 Triad Omega Fusion"
            )
        }

        val cleanBase = EqPresetEntity.interpolateTo31Bands(baseBands)
        val jpNorm = (japanDseePercent.coerceIn(0, 100) / 100f) * mode.jpWeight
        val cnNorm = (chinaR2rBerylliumPercent.coerceIn(0, 100) / 100f) * mode.cnWeight
        val usNorm = (usaDiracSpherePercent.coerceIn(0, 100) / 100f) * mode.usWeight

        // ISO 226 Fletcher-Munson Equal-Loudness low-volume compensation (stronger when volume < 60%)
        val lowVolDeficit = if (fletcherMunsonEnabled) {
            ((68 - systemVolumePercent.coerceIn(15, 100)).coerceAtLeast(0) / 53f).coerceIn(0f, 1f)
        } else 0f

        val outBands = List(31) { idx ->
            var g = cleanBase.getOrElse(idx) { 0f } * 0.55f

            // 🇯🇵 JAPAN TECH: Sony WM1ZM2 Gold Walkman + DSEE Ultimate 14-20kHz Upscaler + K2HD Vocal Silk
            if (jpNorm > 0.01f) {
                when (idx) {
                    in 3..6 -> g += 2.2f * jpNorm     // Tight Japanese audiophile sub-bass
                    in 15..20 -> g += 2.6f * jpNorm   // K2HD Silky Vocal & String Harmonic Bloom (800Hz-2.5kHz)
                    in 24..26 -> g += 2.0f * jpNorm   // 360 Reality Spherical Height cue (5kHz-8kHz)
                    in 27..30 -> g += (3.8f + (idx - 27) * 0.7f) * jpNorm // DSEE Ultimate 10kHz-20kHz Harmonic Restorer
                }
            }

            // 🇨🇳 CHINA TECH: Holo R2R Discrete Ladder DAC + Beryllium Woofer Slam + Electrostatic Tweeter
            if (cnNorm > 0.01f) {
                when (idx) {
                    in 1..5 -> g += 4.2f * cnNorm     // Beryllium Dynamic Driver ultra-fast sub-slam (25Hz-63Hz)
                    in 6..8 -> g += 2.8f * cnNorm     // Mid-bass chest punch (80Hz-125Hz)
                    in 10..14 -> g += 1.8f * cnNorm   // R2R Resistor-Ladder Class-A organic lower-mid warmth (200Hz-500Hz)
                    in 21..23 -> g += 2.4f * cnNorm   // Planar Magnetic pin-point instrument separation (2.5kHz-4kHz)
                    in 26..29 -> g += 3.4f * cnNorm   // Electrostatic (EST) Tweeter micro-detail air (8kHz-16kHz)
                }
            }

            // 🇺🇸 USA TECH: Vegas Sphere 167K Array + Dirac Impulse Phase Alignment + Hollywood IMAX LFE
            if (usNorm > 0.01f) {
                when (idx) {
                    in 0..3 -> g += 4.8f * usNorm     // Hollywood IMAX & Vegas Sphere 20Hz-40Hz Infrasonic LFE
                    in 4..6 -> g += 3.4f * usNorm     // Meyer Sound Arena Sub-Throw (50Hz-80Hz)
                    in 11..13 -> g -= 2.2f * usNorm   // Dirac Impulse Phase Anti-Smear Cut (250Hz-400Hz box mud)
                    in 18..22 -> g += 3.0f * usNorm   // Zero-Smear Transient Kick & Vocal Snap (1.6kHz-3.15kHz)
                    in 25..28 -> g += 2.2f * usNorm   // THX AAA Achromatic Studio Clarity
                }
            }

            // Fletcher-Munson Equal-Loudness Contour (boosts sub-bass & air when listening at low/medium volume)
            if (fletcherMunsonEnabled) {
                val baseContour = 0.35f + lowVolDeficit * 0.65f
                if (idx <= 5) g += (3.2f - idx * 0.35f) * baseContour
                if (idx >= 26) g += (1.8f + (idx - 26) * 0.4f) * baseContour
            }

            (kotlin.math.round(g * 2f) / 2f).coerceIn(-15f, 15f)
        }

        val effBass = (72 + (cnNorm * 14f + usNorm * 16f).roundToInt()).coerceIn(45, 99)
        val effSpatial = (66 + (jpNorm * 16f + usNorm * 18f).roundToInt()).coerceIn(45, 98)
        val effReverb = when (mode) {
            GlobalTriadMode.JAPAN_SONY_YAMAHA -> 28
            GlobalTriadMode.CHINA_R2R_BERYLLIUM -> 20
            GlobalTriadMode.USA_SPHERE_DIRAC_THX -> 36
            GlobalTriadMode.TRIAD_OMEGA_FUSION -> 30
            GlobalTriadMode.OFF -> 14
        }
        val effRoom = when (mode) {
            GlobalTriadMode.JAPAN_SONY_YAMAHA -> "Large Hall"
            GlobalTriadMode.CHINA_R2R_BERYLLIUM -> "Small Room"
            GlobalTriadMode.USA_SPHERE_DIRAC_THX -> "Large Hall"
            GlobalTriadMode.TRIAD_OMEGA_FUSION -> "Medium Room"
            GlobalTriadMode.OFF -> "Small Room"
        }
        val effCrossfeed = (32 + (cnNorm * 22f + jpNorm * 18f).roundToInt()).coerceIn(20, 80)
        val effAnalogDrive = (28 + (cnNorm * 45f + jpNorm * 20f).roundToInt()).coerceIn(15, 90)

        val summary = buildString {
            append("${mode.badge}: ")
            append("🇯🇵 DSEE ${japanDseePercent}% • ")
            append("🇨🇳 R2R+Be ${chinaR2rBerylliumPercent}% • ")
            append("🇺🇸 Dirac+Sphere ${usaDiracSpherePercent}%")
            if (fletcherMunsonEnabled) append(" • THX ISO Loudness ON")
        }

        return GlobalTriadSynthesisResult(
            sculptedBands31 = outBands,
            effectiveBassBoost = effBass,
            effectiveSpatial3D = effSpatial,
            effectiveReverbLevel = effReverb,
            effectiveReverbRoom = effRoom,
            effectiveCrossfeedPct = effCrossfeed,
            effectiveAnalogDrivePct = effAnalogDrive,
            telemetrySummary = summary
        )
    }
}

data class GlobalTriadSynthesisResult(
    val sculptedBands31: List<Float>,
    val effectiveBassBoost: Int,
    val effectiveSpatial3D: Int,
    val effectiveReverbLevel: Int,
    val effectiveReverbRoom: String,
    val effectiveCrossfeedPct: Int,
    val effectiveAnalogDrivePct: Int,
    val telemetrySummary: String
)

enum class GlobalTriadMode(
    val title: String,
    val badge: String,
    val jpWeight: Float,
    val cnWeight: Float,
    val usWeight: Float,
    val subtitle: String
) {
    OFF(
        title = "Triad Bypass (Off)",
        badge = "BYPASS",
        jpWeight = 0f,
        cnWeight = 0f,
        usWeight = 0f,
        subtitle = "Standard Equltra DSP path"
    ),
    JAPAN_SONY_YAMAHA(
        title = "🇯🇵 Japan: Sony DSEE + K2HD + Suntory Hall",
        badge = "🇯🇵 JAPAN HI-RES",
        jpWeight = 1.0f,
        cnWeight = 0.25f,
        usWeight = 0.20f,
        subtitle = "WM1ZM2 Gold Signature • 14–20kHz DSEE Upscaler • Yamaha AFC 360° Silky Air"
    ),
    CHINA_R2R_BERYLLIUM(
        title = "🇨🇳 China: Holo R2R DAC + Beryllium + EST",
        badge = "🇨🇳 CHINA R2R",
        jpWeight = 0.25f,
        cnWeight = 1.0f,
        usWeight = 0.25f,
        subtitle = "Discrete R2R Ladder Class-A Warmth • Beryllium Woofer Slam • Electrostatic Tweeter"
    ),
    USA_SPHERE_DIRAC_THX(
        title = "🇺🇸 USA: Vegas Sphere + Dirac Phase + THX",
        badge = "🇺🇸 USA SPHERE",
        jpWeight = 0.20f,
        cnWeight = 0.25f,
        usWeight = 1.0f,
        subtitle = "Las Vegas Sphere 167K Array • Dirac Zero-Smear Transient Phase • IMAX 20Hz LFE"
    ),
    TRIAD_OMEGA_FUSION(
        title = "🌐 Triad Omega (🇯🇵 Japan + 🇨🇳 China + 🇺🇸 USA)",
        badge = "🌐 TRIAD OMEGA",
        jpWeight = 0.85f,
        cnWeight = 0.90f,
        usWeight = 0.90f,
        subtitle = "Ultimate 3-Nation Fusion: Japan DSEE Air + China R2R Beryllium + USA Dirac Sphere LFE"
    )
}

data class OutOfHeadSynthesisResult(
    val sculptedBands31: List<Float>,
    val effectiveBassBoost: Int,
    val effectiveSpatial3D: Int,
    val effectiveReverbLevel: Int,
    val effectiveReverbRoom: String,
    val effectiveCrossfeedPct: Int,
    val effectivePreampDb: Float,
    val telemetrySummary: String
)

enum class OutOfHeadExternalizerMode(
    val title: String,
    val badge: String,
    val defaultDistanceMeters: Float,
    val defaultWallBouncePct: Int,
    val recommendedRoom: String,
    val subtitle: String
) {
    OFF(
        title = "In-Ear Direct (Off)",
        badge = "IN-EAR",
        defaultDistanceMeters = 0.5f,
        defaultWallBouncePct = 0,
        recommendedRoom = "Small Room",
        subtitle = "Standard inside-the-head headphone playback"
    ),
    STUDIO_DESK_1_2M(
        title = "Studio Desk Monitors 1.2m",
        badge = "1.2M DESK",
        defaultDistanceMeters = 1.2f,
        defaultWallBouncePct = 38,
        recommendedRoom = "Small Room",
        subtitle = "Nearfield studio monitors sitting 4 feet in front of your face on a desk"
    ),
    LIVING_ROOM_SOFA_3M(
        title = "Living Room Sofa 3.0m",
        badge = "3.0M SOFA",
        defaultDistanceMeters = 3.0f,
        defaultWallBouncePct = 58,
        recommendedRoom = "Medium Room",
        subtitle = "Tower speakers & floor subwoofer playing 10 feet ahead in your living room"
    ),
    IMAX_SCREEN_6M(
        title = "IMAX Screen Throw 6.0m",
        badge = "6.0M IMAX",
        defaultDistanceMeters = 6.0f,
        defaultWallBouncePct = 72,
        recommendedRoom = "Large Hall",
        subtitle = "Giant theater array projecting dialog & LFE bass from 20 feet in front"
    ),
    NEXT_ROOM_PARTY(
        title = "Next-Room / Open-Door Speaker",
        badge = "NEXT ROOM",
        defaultDistanceMeters = 4.8f,
        defaultWallBouncePct = 85,
        recommendedRoom = "Large Hall",
        subtitle = "Hyper-realistic illusion of a party speaker booming outside in the next room"
    ),
    OPEN_AIR_LAWN(
        title = "Open-Air Lawn / Terrace Stage",
        badge = "LAWN STAGE",
        defaultDistanceMeters = 5.0f,
        defaultWallBouncePct = 44,
        recommendedRoom = "Plate",
        subtitle = "Outdoor open-sky stage throw with zero ear-canal pressure"
    )
}

enum class GyroHeadTrackingMode(
    val title: String,
    val badge: String,
    val subtitle: String
) {
    OFF("Off", "GYRO OFF", "Static stereo stage without head-tracking anchor"),
    STAGE_NORTH_LOCK("Stage North Lock", "STAGE LOCK", "Vocals & stage stay anchored front-center when you turn your head"),
    WOOFER_CHEST_LOCK("Woofer Chest Lock", "CHEST SUB", "18\" Subwoofers stay locked to your chest while instruments orbit 360°"),
    CINEMA_4D_INERTIAL("4D IMAX Inertial", "4D GYRO", "Full gyroscope + accelerometer binaural room rotation")
}

enum class StemDeMixerPreset(
    val title: String,
    val badge: String,
    val vocalPct: Int,
    val drumPct: Int,
    val bassPct: Int,
    val melodyPct: Int
) {
    STUDIO_BALANCED("Studio Balanced", "100% ALL", 100, 100, 100, 100),
    KARAOKE_VOCAL_KILL("Karaoke (No Vocals)", "KARAOKE", 15, 115, 120, 125),
    VOCAL_200_ISOLATE("200% Vocal Spotlight", "VOCAL 2X", 190, 75, 80, 75),
    DRUM_AND_BASS_ONLY("Drum & 808 Bass Solo", "D&B SOLO", 35, 175, 185, 45),
    MELODY_GUITAR_SOLO("Guitar & Synth Focus", "INSTRUMENT", 45, 70, 75, 180)
}

enum class SpeedCruiseMode(
    val title: String,
    val badge: String,
    val speedKmh: Int,
    val subtitle: String
) {
    OFF("Stationary / Room", "0 KM/H", 0, "Zero road/wind noise compensation"),
    CITY_CRUISE_40KMH("City Drive 45 km/h", "45 KM/H", 45, "Compensates engine & tire hum (+2.2dB Sub, +1.5dB Vocal)"),
    HIGHWAY_TURBO_100KMH("Highway Cruise 110 km/h", "110 KM/H", 110, "Anti-wind masking (+4.2dB Deep Bass, +2.8dB Presence)"),
    GYM_ADRENALINE_SPRINT("Gym / Running Sprint", "SPRINT", 18, "High-impact rhythm & sweat-seal compensation")
}

enum class AnalogSaturationMode(
    val title: String,
    val badge: String,
    val subtitle: String
) {
    CLEAN_DIGITAL("Ultra-Clean Digital", "PURE DSP", "Zero harmonic coloration, 64-bit transparent signal path"),
    TUBE_12AX7_WARMTH("12AX7 Vacuum Tube", "TUBE WARM", "Rich even-order tube harmonics & velvety vocal warmth"),
    ABBEY_TAPE_GLUE("Reel-to-Reel Tape", "TAPE GLUE", "Analog magnetic tape compression & warm sub-bass glue"),
    VINYL_AIR_EXCITER("Aphex Air Exciter", "AIR SHIMMER", "Restores 12kHz–20kHz studio sparkle to compressed streams")
}

enum class AcousticVenueClone(
    val title: String,
    val badge: String,
    val subtitle: String,
    val bands31: List<Float>,
    val bassBoost: Int,
    val spatial3D: Int,
    val reverbLevel: Int,
    val reverbRoom: String,
    val crossfeedPct: Int
) {
    NONE(
        title = "Direct Studio Bypass",
        badge = "DIRECT",
        subtitle = "No external venue impulse cloning",
        bands31 = List(31) { 0f },
        bassBoost = 35,
        spatial3D = 40,
        reverbLevel = 0,
        reverbRoom = "None",
        crossfeedPct = 0
    ),
    BURMESTER_S_CLASS(
        title = "Burmester 4D Mercedes S-Class",
        badge = "S-CLASS 4D",
        subtitle = "31-speaker luxury cabin + seat exciter subwoofers + silk dome tweeters",
        bands31 = listOf(
            7.5f, 8.0f, 8.2f, 7.6f, 6.8f, 5.6f, 4.2f, 2.8f, 1.5f, 0.5f,
            -0.4f, -0.8f, -0.6f, 0.0f, 0.8f, 1.6f, 2.4f, 3.0f, 3.4f, 3.2f,
            2.8f, 2.5f, 2.8f, 3.4f, 4.0f, 4.5f, 4.6f, 4.2f, 3.6f, 3.0f, 2.4f
        ),
        bassBoost = 82,
        spatial3D = 76,
        reverbLevel = 22,
        reverbRoom = "Small Room",
        crossfeedPct = 45
    ),
    BERGHAIN_TECHNO_BUNKER(
        title = "Berghain Berlin Sub Bunker",
        badge = "FUNKTION-1",
        subtitle = "Massive horn-loaded 18-inch sub stacks + concrete bunker slam",
        bands31 = listOf(
            9.5f, 9.8f, 9.5f, 8.8f, 7.8f, 6.5f, 4.8f, 3.0f, 1.2f, -0.5f,
            -1.5f, -2.0f, -1.8f, -1.0f, 0.0f, 1.0f, 1.8f, 2.5f, 3.0f, 3.2f,
            2.8f, 2.4f, 2.8f, 3.5f, 4.0f, 4.2f, 3.8f, 3.2f, 2.5f, 1.8f, 1.2f
        ),
        bassBoost = 96,
        spatial3D = 88,
        reverbLevel = 38,
        reverbRoom = "Large Hall",
        crossfeedPct = 30
    ),
    DOLBY_CINEMA_ROW_G(
        title = "Dolby Cinema Reference (Row G)",
        badge = "DOLBY ATMOS",
        subtitle = " SMPTE theatrical sub-LFE curve + pin-point center dialog array",
        bands31 = listOf(
            8.8f, 9.0f, 8.5f, 7.5f, 6.2f, 4.8f, 3.2f, 1.8f, 0.6f, -0.2f,
            -0.8f, -1.0f, -0.6f, 0.2f, 1.2f, 2.2f, 3.2f, 3.8f, 4.0f, 3.6f,
            3.0f, 2.6f, 2.8f, 3.2f, 3.6f, 3.8f, 3.5f, 3.0f, 2.4f, 1.8f, 1.2f
        ),
        bassBoost = 86,
        spatial3D = 92,
        reverbLevel = 30,
        reverbRoom = "Medium Room",
        crossfeedPct = 55
    ),
    COKE_STUDIO_LIVE_ROOM(
        title = "Coke Studio Live Acoustic Room",
        badge = "LIVE STUDIO",
        subtitle = "Warm wooden diffuser room, intimate vocals, tabla & acoustic guitar bloom",
        bands31 = listOf(
            5.2f, 5.8f, 6.2f, 6.0f, 5.4f, 4.5f, 3.5f, 2.4f, 1.5f, 0.8f,
            0.4f, 0.6f, 1.2f, 2.0f, 2.8f, 3.5f, 4.0f, 4.2f, 4.0f, 3.5f,
            3.0f, 2.8f, 3.2f, 3.6f, 3.8f, 3.5f, 3.0f, 2.5f, 2.0f, 1.5f, 1.0f
        ),
        bassBoost = 68,
        spatial3D = 74,
        reverbLevel = 26,
        reverbRoom = "Medium Room",
        crossfeedPct = 65
    ),
    WEMBLEY_VIP_ARENA(
        title = "Wembley Stadium Front VIP",
        badge = "STADIUM 360",
        subtitle = "Open-air festival line-array throw with massive crowd-stage width",
        bands31 = listOf(
            8.0f, 8.5f, 8.8f, 8.2f, 7.2f, 5.8f, 4.2f, 2.5f, 1.0f, 0.0f,
            -0.8f, -1.2f, -0.8f, 0.0f, 1.0f, 2.0f, 3.0f, 3.6f, 4.0f, 3.8f,
            3.4f, 3.0f, 3.4f, 4.0f, 4.5f, 4.8f, 4.4f, 3.8f, 3.0f, 2.2f, 1.6f
        ),
        bassBoost = 90,
        spatial3D = 95,
        reverbLevel = 46,
        reverbRoom = "Large Hall",
        crossfeedPct = 40
    )
}

