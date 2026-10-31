package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.dsp.EsportsThreatRadarState
import com.example.dsp.FutureGenAudioLabEngine
import com.example.dsp.HolographicVibeProfile
import com.example.dsp.NeuralBrainwaveMode
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HyperViolet
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.SignalEmerald
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun FutureGenQuickStripCard(
    uiState: EqUiState,
    esportsRadar: EsportsThreatRadarState,
    onOpenFutureLab: () -> Unit,
    onRunSonarSweep: () -> Unit,
    onToggleAutoRider: () -> Unit,
    onToggleEsportsXray: () -> Unit,
    onApplyHolographicVibe: (HolographicVibeProfile) -> Unit
) {
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.5.dp,
            brush = Brush.horizontalGradient(
                listOf(ElectricCyan, HyperViolet, NeonMagenta)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(listOf(NeonMagenta, HyperViolet, ElectricCyan))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Future-Gen Quantum Lab",
                            tint = AmoledBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "FUTURE-GEN QUANTUM LAB",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "7-IN-1 NEXT-GEN",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                                color = AmoledBlack,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .background(SignalEmerald, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = "Sonar Room Scan • Brainwave 40Hz • 360° Radar • Bio-Ear DNA • AI Rider • X-Ray",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = ElectricCyan,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Button(
                    onClick = onOpenFutureLab,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricCyan,
                        contentColor = AmoledBlack
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("open_future_lab_btn")
                ) {
                    Text(
                        text = "OPEN LAB",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick 1-Tap Future Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = onRunSonarSweep,
                    border = BorderStroke(1.dp, ElectricCyan),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Radar,
                        contentDescription = "Sonar Scan",
                        tint = ElectricCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (uiState.isRunningSonarScan) "SCANNING..." else "SONAR SCAN",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = ElectricCyan,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = onToggleAutoRider,
                    border = BorderStroke(
                        1.dp,
                        if (uiState.isDynamicAutoRiderEnabled) SignalEmerald else ObsidianBorder
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Auto-Rider",
                        tint = if (uiState.isDynamicAutoRiderEnabled) SignalEmerald else TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (uiState.isDynamicAutoRiderEnabled) "AI RIDER: ON" else "AI RIDER",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = if (uiState.isDynamicAutoRiderEnabled) SignalEmerald else TextPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = onToggleEsportsXray,
                    border = BorderStroke(
                        1.dp,
                        if (uiState.isEsportsXrayEnabled) NeonMagenta else ObsidianBorder
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = "Esports X-Ray",
                        tint = if (uiState.isEsportsXrayEnabled) NeonMagenta else TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (uiState.isEsportsXrayEnabled) "X-RAY: ON" else "X-RAY HUD",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = if (uiState.isEsportsXrayEnabled) NeonMagenta else TextPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Holographic Vibe-to-Sound 1-Tap Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(vertical = 2.dp)
            ) {
                items(FutureGenAudioLabEngine.HOLOGRAPHIC_VIBES, key = { it.id }) { vibe ->
                    val selected = uiState.activePresetName.contains(vibe.title, ignoreCase = true)
                    FilterChip(
                        selected = selected,
                        onClick = { onApplyHolographicVibe(vibe) },
                        label = {
                            Text(
                                text = "✨ ${vibe.title}",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HyperViolet,
                            selectedLabelColor = Color.White,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selected,
                            borderColor = HyperViolet.copy(alpha = 0.6f),
                            selectedBorderColor = ElectricCyan
                        )
                    )
                }
            }

            // Live Future Telemetry Readout Bar
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(AmoledBlack)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        uiState.isEsportsXrayEnabled -> esportsRadar.dominantCueLabel
                        uiState.isDynamicAutoRiderEnabled -> uiState.autoRiderStatusText
                        uiState.neuralBrainwaveMode != NeuralBrainwaveMode.OFF ->
                            "🧠 NEURAL ${uiState.neuralBrainwaveMode.badge} ACTIVE • 360° AZIMUTH ${uiState.radarAzimuthDeg.roundToInt()}°"
                        else -> uiState.sonarTelemetryText
                    },
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = SignalEmerald,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "EAR ID: ${uiState.bioEarAgeYears}Y (${uiState.bioEarGoldenScore}%)",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                    color = StudioAmber,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FutureGenQuantumLabDialog(
    uiState: EqUiState,
    esportsRadar: EsportsThreatRadarState,
    onRunSonarSweep: () -> Unit,
    onSelectBrainwaveMode: (NeuralBrainwaveMode) -> Unit,
    onUpdateRadarPosition: (Float, Float) -> Unit,
    onToggleRadarOrbit: () -> Unit,
    onUpdateLeftRightBalance: (Float) -> Unit,
    onPlayAudiogramTone: (Int) -> Unit,
    onUpdateAudiogramSensitivity: (Int, Float) -> Unit,
    onApplyBioEarDnaProfile: () -> Unit,
    onToggleDynamicAutoRider: () -> Unit,
    onToggleEsportsXray: () -> Unit,
    onApplyHolographicVibe: (HolographicVibeProfile) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = AmoledBlack,
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(1.5.dp, ElectricCyan),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(690.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Quantum Lab",
                            tint = ElectricCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "EQULTRA QUANTUM LAB (2027 SUITE)",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "7 Next-Gen Acoustic, Neural & Spatial Engines",
                                style = MaterialTheme.typography.labelSmall,
                                color = ElectricCyan
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Quantum Lab",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 1. SONAR ACOUSTIC ROOM & EARBUD SCANNER
                    Surface(
                        color = ObsidianCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Radar,
                                        contentDescription = "Sonar Scanner",
                                        tint = ElectricCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "1. Acoustic Sonar Impulse Scanner",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Plays 45Hz–12.8kHz chirp sweep & reverses room/earbud resonance",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = uiState.sonarTelemetryText,
                                style = MaterialTheme.typography.labelSmall,
                                color = SignalEmerald,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = onRunSonarSweep,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ElectricCyan,
                                    contentColor = AmoledBlack
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (uiState.isRunningSonarScan) {
                                        "EMITTING SONAR CHIRP SWEEP..."
                                    } else {
                                        "RUN 1-SECOND SONAR ACOUSTIC CALIBRATION"
                                    },
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    // 2. NEURAL BRAINWAVE & BIO-RHYTHM ENTRAINMENT
                    Surface(
                        color = ObsidianCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, HyperViolet.copy(alpha = 0.7f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "Neural Brainwave Engine",
                                    tint = HyperViolet,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "2. Neural Brainwave & Bio-Rhythm DSP",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = uiState.neuralBrainwaveMode.subtitle,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                NeuralBrainwaveMode.entries.forEach { mode ->
                                    val selected = uiState.neuralBrainwaveMode == mode
                                    FilterChip(
                                        selected = selected,
                                        onClick = { onSelectBrainwaveMode(mode) },
                                        label = {
                                            Text(
                                                text = "${mode.title} (${mode.badge})",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = HyperViolet,
                                            selectedLabelColor = Color.White,
                                            containerColor = AmoledBlack,
                                            labelColor = TextSecondary
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // 3. 360° SOUNDSTAGE RADAR & L/R CHANNEL BALANCE
                    Surface(
                        color = ObsidianCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, NeonMagenta.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.SurroundSound,
                                        contentDescription = "360 Soundstage Radar",
                                        tint = NeonMagenta,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "3. 360° Holographic Soundstage Radar",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Drag virtual speaker orbit (ITD/ILD Binaural Panning)",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = TextSecondary
                                        )
                                    }
                                }
                                OutlinedButton(
                                    onClick = onToggleRadarOrbit,
                                    border = BorderStroke(
                                        1.dp,
                                        if (uiState.isRadarAutoOrbitEnabled) SignalEmerald else ObsidianBorder
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(
                                        text = if (uiState.isRadarAutoOrbitEnabled) "360° ORBIT: ON" else "AUTO ORBIT",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (uiState.isRadarAutoOrbitEnabled) SignalEmerald else TextPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Interactive 360° Radar Canvas
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                SoundstageRadarCanvas(
                                    azimuthDeg = uiState.radarAzimuthDeg,
                                    distanceNorm = uiState.radarDistanceNorm,
                                    onPositionChanged = onUpdateRadarPosition
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "L/R Audiophile Balance",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                                Text(
                                    text = String.format(Locale.US, "%+.1f dB", uiState.leftRightBalanceDb),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ElectricCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Slider(
                                value = uiState.leftRightBalanceDb,
                                onValueChange = onUpdateLeftRightBalance,
                                valueRange = -6f..6f,
                                colors = SliderDefaults.colors(
                                    thumbColor = ElectricCyan,
                                    activeTrackColor = ElectricCyan,
                                    inactiveTrackColor = AmoledBlack
                                )
                            )
                        }
                    }

                    // 4. BIO-EAR AGE & GOLDEN HEARING ID SCANNER
                    Surface(
                        color = ObsidianCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, StudioAmber.copy(alpha = 0.7f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Hearing,
                                    contentDescription = "Bio-Ear DNA",
                                    tint = StudioAmber,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "4. Bio-Ear Age & Golden Hearing ID",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = uiState.bioEarDiagnosisSummary,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = StudioAmber
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            val testFreqs = listOf(250, 1000, 4000, 8000, 12500, 16000)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                testFreqs.forEachIndexed { idx, hz ->
                                    val rating = uiState.audiogramSensitivities.getOrElse(idx) { 0.85f }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(AmoledBlack)
                                            .padding(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { onPlayAudiogramTone(hz) },
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = "Play ${hz}Hz",
                                                tint = StudioAmber,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = if (hz >= 1000) "${hz / 1000f}kHz" else "${hz}Hz",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                color = StudioAmber
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Hear: ${(rating * 100).roundToInt()}%",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                            color = TextSecondary
                                        )
                                        Slider(
                                            value = rating,
                                            onValueChange = { onUpdateAudiogramSensitivity(idx, it) },
                                            valueRange = 0.2f..1.0f,
                                            modifier = Modifier.width(92.dp),
                                            colors = SliderDefaults.colors(
                                                thumbColor = StudioAmber,
                                                activeTrackColor = StudioAmber
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = onApplyBioEarDnaProfile,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StudioAmber,
                                    contentColor = AmoledBlack
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "SYNTHESIZE PERSONAL BIO-EAR DNA 31-BAND CURVE",
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    // 5 & 6. DYNAMIC AI AUTO-RIDER + ESPORTS TACTICAL X-RAY HUD
                    Surface(
                        color = ObsidianCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, SignalEmerald.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "5. Dynamic Live AI Auto-Rider",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = uiState.autoRiderStatusText,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = SignalEmerald
                                    )
                                }
                                Switch(
                                    checked = uiState.isDynamicAutoRiderEnabled,
                                    onCheckedChange = { onToggleDynamicAutoRider() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = AmoledBlack,
                                        checkedTrackColor = SignalEmerald
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "6. Esports Tactical Audio X-Ray HUD",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = esportsRadar.dominantCueLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = NeonMagenta
                                    )
                                }
                                Switch(
                                    checked = uiState.isEsportsXrayEnabled,
                                    onCheckedChange = { onToggleEsportsXray() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = NeonMagenta
                                    )
                                )
                            }

                            if (uiState.isEsportsXrayEnabled) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    ThreatBarColumn("LEFT FLANK", esportsRadar.leftThreatLevel, NeonMagenta, Modifier.weight(1f))
                                    ThreatBarColumn("CENTER", esportsRadar.centerThreatLevel, ElectricCyan, Modifier.weight(1f))
                                    ThreatBarColumn("RIGHT FLANK", esportsRadar.rightThreatLevel, NeonMagenta, Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    // 7. HOLOGRAPHIC AI VIBE-TO-SOUND GENERATOR
                    Surface(
                        color = ObsidianCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, ObsidianBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "7. Holographic AI Vibe-to-Sound Morphs",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Simultaneously morphs 31-Band EQ, 360° Stage, Reverb & Neural Wave",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FutureGenAudioLabEngine.HOLOGRAPHIC_VIBES.forEach { vibe ->
                                Surface(
                                    color = AmoledBlack,
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, HyperViolet.copy(alpha = 0.5f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { onApplyHolographicVibe(vibe) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "✨ ${vibe.title}",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = ElectricCyan,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = vibe.subtitle,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = TextSecondary
                                            )
                                        }
                                        Text(
                                            text = vibe.badge,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .background(HyperViolet, RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ThreatBarColumn(
    label: String,
    level: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp), color = TextSecondary)
            Text(
                text = "${(level * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { level.coerceIn(0.05f, 1f) },
            color = color,
            trackColor = AmoledBlack,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Composable
private fun SoundstageRadarCanvas(
    azimuthDeg: Float,
    distanceNorm: Float,
    onPositionChanged: (Float, Float) -> Unit
) {
    val rad = (azimuthDeg - 90f) * (PI.toFloat() / 180f)
    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(CircleShape)
            .background(AmoledBlack)
            .border(1.dp, ObsidianBorder, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val dx = offset.x - cx
                        val dy = offset.y - cy
                        val maxR = min(cx, cy) * 0.85f
                        val dist = (sqrt(dx * dx + dy * dy) / maxR).coerceIn(0.15f, 1f)
                        val angleDeg = ((atan2(dy, dx) * 180f / PI.toFloat()) + 90f).let {
                            if (it > 180f) it - 360f else if (it < -180f) it + 360f else it
                        }
                        onPositionChanged(angleDeg, dist)
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val dx = change.position.x - cx
                        val dy = change.position.y - cy
                        val maxR = min(cx, cy) * 0.85f
                        val dist = (sqrt(dx * dx + dy * dy) / maxR).coerceIn(0.15f, 1f)
                        val angleDeg = ((atan2(dy, dx) * 180f / PI.toFloat()) + 90f).let {
                            if (it > 180f) it - 360f else if (it < -180f) it + 360f else it
                        }
                        onPositionChanged(angleDeg, dist)
                    }
                }
        ) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val maxR = min(cx, cy) * 0.85f

            // Concentric radar rings
            for (r in listOf(0.35f, 0.68f, 1.0f)) {
                drawCircle(
                    color = ElectricCyan.copy(alpha = 0.18f),
                    radius = maxR * r,
                    center = Offset(cx, cy),
                    style = Stroke(width = 1.dp.toPx())
                )
            }
            // Crosshairs
            drawLine(
                color = ElectricCyan.copy(alpha = 0.15f),
                start = Offset(cx, 0f),
                end = Offset(cx, size.height),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = ElectricCyan.copy(alpha = 0.15f),
                start = Offset(0f, cy),
                end = Offset(size.width, cy),
                strokeWidth = 1.dp.toPx()
            )

            // Listener head at center
            drawCircle(
                color = SignalEmerald,
                radius = 6.dp.toPx(),
                center = Offset(cx, cy)
            )

            // Virtual sound source node
            val sx = cx + cos(rad) * (maxR * distanceNorm.coerceIn(0.15f, 1f))
            val sy = cy + sin(rad) * (maxR * distanceNorm.coerceIn(0.15f, 1f))

            drawLine(
                color = NeonMagenta.copy(alpha = 0.6f),
                start = Offset(cx, cy),
                end = Offset(sx, sy),
                strokeWidth = 2.dp.toPx()
            )
            drawCircle(
                color = NeonMagenta.copy(alpha = 0.28f),
                radius = 14.dp.toPx(),
                center = Offset(sx, sy)
            )
            drawCircle(
                color = ElectricCyan,
                radius = 7.dp.toPx(),
                center = Offset(sx, sy)
            )
        }
    }
}
