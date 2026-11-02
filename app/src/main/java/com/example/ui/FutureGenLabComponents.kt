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
import androidx.compose.material.icons.filled.SpeakerGroup
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
import com.example.dsp.LrTwinVocalSplitterMode
import com.example.dsp.NeuralBrainwaveMode
import com.example.dsp.OmegaPrimeMode
import com.example.dsp.OutOfHeadExternalizerMode
import com.example.dsp.ServoTransducerProfile
import com.example.dsp.Sovereign2030RndMode
import com.example.dsp.SpatialStageFormation
import com.example.dsp.SpatialStageNode
import com.example.dsp.SpeedCruiseMode
import com.example.dsp.StemDeMixerPreset
import com.example.dsp.WooferEnclosureType
import com.example.dsp.ZeroEarbudOpenAirMode
import com.example.dsp.EarDirectZeroBrainMode
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HyperViolet
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.SignalEmerald
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
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
                                text = "v2.7 KAAN-DIRECT & 7-WOOFER LAB",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "👂 100% KAAN • 0% DIMAG",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                                color = AmoledBlack,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .background(SignalEmerald, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = "100% Kaan Direct • 0% Dimag Pressure • 7 Woofer Boxes • L/R Vocal • 25% Bass Lock",
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
                        text = "3D STAGE & LAB",
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
                    onClick = onOpenFutureLab,
                    border = BorderStroke(1.dp, StudioAmber),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(1.1f)
                        .height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SpeakerGroup,
                        contentDescription = "3D Instrument & Woofer Stage",
                        tint = StudioAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "3D WOOFER STAGE",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = StudioAmber,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = onRunSonarSweep,
                    border = BorderStroke(1.dp, ElectricCyan),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(0.95f)
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
                        text = if (uiState.isRunningSonarScan) "SCAN..." else "SONAR",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
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
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(0.95f)
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
                        text = if (uiState.isDynamicAutoRiderEnabled) "RIDER ON" else "AI RIDER",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
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
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(0.95f)
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
                        text = if (uiState.isEsportsXrayEnabled) "X-RAY ON" else "X-RAY",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
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
                            "🧠 NEURAL ${uiState.neuralBrainwaveMode.badge} • ${uiState.stageTelemetrySummary}"
                        else -> uiState.stageTelemetrySummary
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

/**
 * Interactive 3D Multi-Instrument & Virtual 7.1.4 Dual-Woofer/Speaker Stage Architect Card.
 * Can be embedded directly in Bass & DSP Tab as well as inside the Quantum Lab Dialog.
 */
@Composable
fun MultiNodeInstrumentStageCard(
    uiState: EqUiState,
    onSelectNode: (String) -> Unit,
    onUpdateNodePosition: (String, Float, Float) -> Unit,
    onUpdateNodeGain: (String, Float) -> Unit,
    onToggleNodeMute: (String) -> Unit,
    onToggleNodeSolo: (String) -> Unit,
    onApplyFormation: (SpatialStageFormation) -> Unit,
    onUpdateWooferCrossover: (Int) -> Unit,
    onToggleWooferPhase: () -> Unit,
    onToggleCenterVocalLock: () -> Unit
) {
    val selectedNode = uiState.stageNodes.find { it.id == uiState.selectedStageNodeId }
        ?: uiState.stageNodes.firstOrNull()

    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.5.dp, StudioAmber.copy(alpha = 0.85f)),
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
                        imageVector = Icons.Default.SpeakerGroup,
                        contentDescription = "3D Multi-Instrument & Woofer Stage",
                        tint = StudioAmber,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "3D INSTRUMENT & DUAL-WOOFER STAGE ARCHITECT",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Drag Subwoofers, Vocals, Drums, Guitar, Synth & Surround Towers in 360°",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = StudioAmber
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 1-Tap Stage Formations
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SpatialStageFormation.entries.forEach { formation ->
                    val isSelected = uiState.selectedStageFormation == formation
                    FilterChip(
                        selected = isSelected,
                        onClick = { onApplyFormation(formation) },
                        label = {
                            Text(
                                text = "${formation.title} (${formation.badge})",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StudioAmber,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Interactive 360° Multi-Node Studio Floor Canvas
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                MultiNodeStageFloorCanvas(
                    nodes = uiState.stageNodes,
                    selectedNodeId = uiState.selectedStageNodeId,
                    onSelectNode = onSelectNode,
                    onMoveNode = onUpdateNodePosition
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Selectable Instrument / Woofer Node Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                uiState.stageNodes.forEach { node ->
                    val isSelected = node.id == uiState.selectedStageNodeId
                    val nodeColor = when (node.category) {
                        "WOOFER" -> NeonMagenta
                        "INSTRUMENT" -> ElectricCyan
                        else -> SignalEmerald
                    }
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectNode(node.id) },
                        label = {
                            Text(
                                text = "${node.shortTag} (${node.azimuthDeg.roundToInt()}°)",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = nodeColor,
                            selectedLabelColor = if (nodeColor == NeonMagenta) Color.White else AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = if (node.muted) TextSecondary.copy(alpha = 0.4f) else nodeColor
                        )
                    )
                }
            }

            // Selected Node Inspector Controls (Gain dB, Angle, Distance, Mute, Solo)
            if (selectedNode != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = AmoledBlack,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ObsidianBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${selectedNode.label} [${selectedNode.shortTag}]",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = ElectricCyan,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Angle: ${selectedNode.azimuthDeg.roundToInt()}° • Distance: ${(selectedNode.distanceNorm * 10f).roundToInt() / 10f}m • Gain: ${String.format(Locale.US, "%+.1fdB", selectedNode.gainDb)}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TextSecondary
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { onToggleNodeMute(selectedNode.id) },
                                    border = BorderStroke(1.dp, if (selectedNode.muted) NeonMagenta else ObsidianBorder),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(
                                        text = if (selectedNode.muted) "MUTED" else "MUTE",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = if (selectedNode.muted) NeonMagenta else TextSecondary
                                    )
                                }
                                OutlinedButton(
                                    onClick = { onToggleNodeSolo(selectedNode.id) },
                                    border = BorderStroke(1.dp, if (selectedNode.solo) StudioAmber else ObsidianBorder),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(
                                        text = if (selectedNode.solo) "SOLO ON" else "SOLO",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = if (selectedNode.solo) StudioAmber else TextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Node Gain",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = TextSecondary,
                                modifier = Modifier.width(66.dp)
                            )
                            Slider(
                                value = selectedNode.gainDb,
                                onValueChange = { onUpdateNodeGain(selectedNode.id, it) },
                                valueRange = -12f..12f,
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = ElectricCyan,
                                    activeTrackColor = ElectricCyan
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dual-Woofer Crossover Hz + Phase 0°/180° + Center Vocal Lock
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dual-Woofer Crossover: ${uiState.wooferCrossoverHz} Hz",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedButton(
                                onClick = onToggleWooferPhase,
                                border = BorderStroke(1.dp, if (uiState.wooferPhaseInvert180) NeonMagenta else ElectricCyan),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(
                                    text = if (uiState.wooferPhaseInvert180) "PHASE: 180°" else "PHASE: 0°",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = if (uiState.wooferPhaseInvert180) NeonMagenta else ElectricCyan
                                )
                            }
                            OutlinedButton(
                                onClick = onToggleCenterVocalLock,
                                border = BorderStroke(1.dp, if (uiState.centerVocalLock) SignalEmerald else ObsidianBorder),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(
                                    text = if (uiState.centerVocalLock) "VOCAL LOCK: ON" else "VOCAL LOCK: OFF",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = if (uiState.centerVocalLock) SignalEmerald else TextSecondary
                                )
                            }
                        }
                    }

                    Slider(
                        value = uiState.wooferCrossoverHz.toFloat(),
                        onValueChange = { onUpdateWooferCrossover(it.roundToInt()) },
                        valueRange = 40f..120f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )

                    Text(
                        text = uiState.stageTelemetrySummary,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = SignalEmerald
                    )
                }
            }
        }
    }
}

@Composable
private fun MultiNodeStageFloorCanvas(
    nodes: List<SpatialStageNode>,
    selectedNodeId: String,
    onSelectNode: (String) -> Unit,
    onMoveNode: (String, Float, Float) -> Unit
) {
    Box(
        modifier = Modifier
            .size(230.dp)
            .clip(CircleShape)
            .background(AmoledBlack)
            .border(1.5.dp, StudioAmber.copy(alpha = 0.7f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(nodes, selectedNodeId) {
                    detectTapGestures { tapOffset ->
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val maxR = min(cx, cy) * 0.84f

                        // Check if user tapped near an existing node to select it
                        val tappedNode = nodes.minByOrNull { node ->
                            val rad = (node.azimuthDeg - 90f) * (PI.toFloat() / 180f)
                            val nx = cx + cos(rad) * (maxR * node.distanceNorm)
                            val ny = cy + sin(rad) * (maxR * node.distanceNorm)
                            hypot(tapOffset.x - nx, tapOffset.y - ny)
                        }
                        if (tappedNode != null) {
                            val rad = (tappedNode.azimuthDeg - 90f) * (PI.toFloat() / 180f)
                            val nx = cx + cos(rad) * (maxR * tappedNode.distanceNorm)
                            val ny = cy + sin(rad) * (maxR * tappedNode.distanceNorm)
                            if (hypot(tapOffset.x - nx, tapOffset.y - ny) < 34f) {
                                onSelectNode(tappedNode.id)
                                return@detectTapGestures
                            }
                        }

                        // Otherwise move currently selected node to tapped position
                        val dx = tapOffset.x - cx
                        val dy = tapOffset.y - cy
                        val dist = (sqrt(dx * dx + dy * dy) / maxR).coerceIn(0.15f, 1f)
                        val angleDeg = ((atan2(dy, dx) * 180f / PI.toFloat()) + 90f).let {
                            if (it > 180f) it - 360f else if (it < -180f) it + 360f else it
                        }
                        onMoveNode(selectedNodeId, angleDeg, dist)
                    }
                }
                .pointerInput(nodes, selectedNodeId) {
                    detectDragGestures(
                        onDragStart = { startOffset ->
                            val cx = size.width / 2f
                            val cy = size.height / 2f
                            val maxR = min(cx, cy) * 0.84f
                            val nearest = nodes.minByOrNull { node ->
                                val rad = (node.azimuthDeg - 90f) * (PI.toFloat() / 180f)
                                val nx = cx + cos(rad) * (maxR * node.distanceNorm)
                                val ny = cy + sin(rad) * (maxR * node.distanceNorm)
                                hypot(startOffset.x - nx, startOffset.y - ny)
                            }
                            if (nearest != null) {
                                val rad = (nearest.azimuthDeg - 90f) * (PI.toFloat() / 180f)
                                val nx = cx + cos(rad) * (maxR * nearest.distanceNorm)
                                val ny = cy + sin(rad) * (maxR * nearest.distanceNorm)
                                if (hypot(startOffset.x - nx, startOffset.y - ny) < 48f) {
                                    onSelectNode(nearest.id)
                                }
                            }
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val cx = size.width / 2f
                            val cy = size.height / 2f
                            val maxR = min(cx, cy) * 0.84f
                            val dx = change.position.x - cx
                            val dy = change.position.y - cy
                            val dist = (sqrt(dx * dx + dy * dy) / maxR).coerceIn(0.15f, 1f)
                            val angleDeg = ((atan2(dy, dx) * 180f / PI.toFloat()) + 90f).let {
                                if (it > 180f) it - 360f else if (it < -180f) it + 360f else it
                            }
                            onMoveNode(selectedNodeId, angleDeg, dist)
                        }
                    )
                }
        ) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val maxR = min(cx, cy) * 0.84f

            // Acoustic room rings
            for (r in listOf(0.32f, 0.64f, 0.96f)) {
                drawCircle(
                    color = ElectricCyan.copy(alpha = 0.16f),
                    radius = maxR * r,
                    center = Offset(cx, cy),
                    style = Stroke(width = 1.dp.toPx())
                )
            }
            // Front/Back and Left/Right axis lines
            drawLine(
                color = ElectricCyan.copy(alpha = 0.14f),
                start = Offset(cx, 0f),
                end = Offset(cx, size.height),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = ElectricCyan.copy(alpha = 0.14f),
                start = Offset(0f, cy),
                end = Offset(size.width, cy),
                strokeWidth = 1.dp.toPx()
            )

            // Listener head in center
            drawCircle(
                color = SignalEmerald.copy(alpha = 0.25f),
                radius = 13.dp.toPx(),
                center = Offset(cx, cy)
            )
            drawCircle(
                color = SignalEmerald,
                radius = 6.dp.toPx(),
                center = Offset(cx, cy)
            )

            // Draw all 8 Instrument, Subwoofer & Speaker nodes
            nodes.forEach { node ->
                val rad = (node.azimuthDeg - 90f) * (PI.toFloat() / 180f)
                val nx = cx + cos(rad) * (maxR * node.distanceNorm.coerceIn(0.15f, 1f))
                val ny = cy + sin(rad) * (maxR * node.distanceNorm.coerceIn(0.15f, 1f))
                val isSelected = node.id == selectedNodeId

                val baseColor = when (node.category) {
                    "WOOFER" -> NeonMagenta
                    "INSTRUMENT" -> ElectricCyan
                    else -> StudioAmber
                }
                val color = if (node.muted) baseColor.copy(alpha = 0.25f) else baseColor

                // Vector ray from listener head to node
                drawLine(
                    color = color.copy(alpha = if (isSelected) 0.75f else 0.28f),
                    start = Offset(cx, cy),
                    end = Offset(nx, ny),
                    strokeWidth = if (isSelected) 2.2.dp.toPx() else 1.dp.toPx()
                )

                // Outer halo if selected or woofer
                if (isSelected || node.category == "WOOFER") {
                    drawCircle(
                        color = color.copy(alpha = if (isSelected) 0.36f else 0.18f),
                        radius = if (isSelected) 16.dp.toPx() else 12.dp.toPx(),
                        center = Offset(nx, ny)
                    )
                }

                // Node core
                drawCircle(
                    color = if (isSelected) Color.White else color,
                    radius = if (isSelected) 8.dp.toPx() else 6.dp.toPx(),
                    center = Offset(nx, ny)
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
    onSelectStageNode: (String) -> Unit = {},
    onUpdateStageNodePosition: (String, Float, Float) -> Unit = { _, _, _ -> },
    onUpdateStageNodeGain: (String, Float) -> Unit = { _, _ -> },
    onToggleStageNodeMute: (String) -> Unit = {},
    onToggleStageNodeSolo: (String) -> Unit = {},
    onApplyStageFormation: (SpatialStageFormation) -> Unit = {},
    onUpdateWooferCrossover: (Int) -> Unit = {},
    onToggleWooferPhase: () -> Unit = {},
    onToggleCenterVocalLock: () -> Unit = {},
    onSelectGyroMode: (GyroHeadTrackingMode) -> Unit = {},
    onUpdateSubHarmonicSynth: (Int) -> Unit = {},
    onApplyStemPreset: (StemDeMixerPreset) -> Unit = {},
    onUpdateStemLevels: (Int, Int, Int, Int) -> Unit = { _, _, _, _ -> },
    onSelectSpeedCruise: (SpeedCruiseMode) -> Unit = {},
    onSelectAnalogMode: (AnalogSaturationMode) -> Unit = {},
    onUpdateAnalogDrive: (Int) -> Unit = {},
    onToggleTinnitusShield: () -> Unit = {},
    onUpdateTinnitusNotchFreq: (Int) -> Unit = {},
    onUpdateCrossfeed: (Int) -> Unit = {},
    onToggleBassHapticWoofer: () -> Unit = {},
    onApplyVenueClone: (AcousticVenueClone) -> Unit = {},
    onCaptureReferenceDna: () -> Unit = {},
    onSelectOutOfHeadMode: (OutOfHeadExternalizerMode) -> Unit = {},
    onUpdateOutOfHeadDistance: (Float) -> Unit = {},
    onUpdateOutOfHeadReflection: (Int) -> Unit = {},
    onTogglePinnaChestHrtf: () -> Unit = {},
    onToggleSubFloorCoupler: () -> Unit = {},
    onSelectGlobalTriadMode: (GlobalTriadMode) -> Unit = {},
    onUpdateJapanDsee: (Int) -> Unit = {},
    onUpdateChinaR2r: (Int) -> Unit = {},
    onUpdateUsaDirac: (Int) -> Unit = {},
    onToggleFletcherMunson: () -> Unit = {},
    onSelectSovereign2030Mode: (Sovereign2030RndMode) -> Unit = {},
    onUpdatePhantomSub16Hz: (Int) -> Unit = {},
    onToggleDopplerEraser: () -> Unit = {},
    onUpdateDsd1024Damping: (Int) -> Unit = {},
    onToggleBioEarDeEsser: () -> Unit = {},
    onUpdateMit64WfsWall: (Int) -> Unit = {},
    onUpdateStreamDeSquasher: (Int) -> Unit = {},
    onSelectOmegaPrimeMode: (OmegaPrimeMode) -> Unit = {},
    onSelectServoProfile: (ServoTransducerProfile) -> Unit = {},
    onUpdateDynamicBreathingEq: (Int) -> Unit = {},
    onUpdateVocalSidechainDuck: (Int) -> Unit = {},
    onUpdateElevationHelixSky: (Int) -> Unit = {},
    onUpdateGoldenPhiHarmonic: (Int) -> Unit = {},
    onToggleBioGrooveCardioSync: () -> Unit = {},
    onSelectZeroEarbudMode: (ZeroEarbudOpenAirMode) -> Unit = {},
    onUpdateOcclusionValve: (Int) -> Unit = {},
    onUpdateOpenCanalUnplug: (Int) -> Unit = {},
    onUpdateAirCushion25m: (Int) -> Unit = {},
    onUpdateOpenBackElectrostatic: (Int) -> Unit = {},
    onToggleAntiVacuumBreeze: () -> Unit = {},
    onToggleVestibularMicroDrift: () -> Unit = {},
    onSelectLiveInstrumentMode: (HolographicLiveInstrumentMode) -> Unit = {},
    onUpdatePluckDrumTransient: (Int) -> Unit = {},
    onUpdateWoodHelmholtzBody: (Int) -> Unit = {},
    onUpdateSympatheticStringBleed: (Int) -> Unit = {},
    onUpdateSingerBreathProximity: (Int) -> Unit = {},
    onToggleHaasStageSeparation: () -> Unit = {},
    onToggleWoodenUnpluggedFloor: () -> Unit = {},
    onSelectChronoNeuralMode: (ChronoNeuralBiomimeticMode) -> Unit = {},
    onUpdateCochlearDispersionSync: (Int) -> Unit = {},
    onUpdateGamma40HzTimeDilation: (Int) -> Unit = {},
    onUpdateStapediusReflexDisarmer: (Int) -> Unit = {},
    onUpdateTartiniOtoacousticSub: (Int) -> Unit = {},
    onToggleMicroSaccadicStageSway: () -> Unit = {},
    onToggleFibonacciGoldenTapeSilk: () -> Unit = {},
    onSelectApexSingularityMode: (ApexSingularityMode) -> Unit = {},
    onUpdateGyroStageAnchor: (Int) -> Unit = {},
    onUpdateTactileChestSlamSub: (Int) -> Unit = {},
    onUpdateVocalFormantUnmasker: (Int) -> Unit = {},
    onUpdateMetamaterialShellCloak: (Int) -> Unit = {},
    onToggleGoldenPhiTriodeTape: () -> Unit = {},
    onToggleFrissonVagusResonator: () -> Unit = {},
    onSelectLrTwinVocalMode: (LrTwinVocalSplitterMode) -> Unit = {},
    onToggleLrTwinVocalSwitch: () -> Unit = {},
    onUpdateFrontCenterNull: (Int) -> Unit = {},
    onUpdateSideTwinProjection: (Int) -> Unit = {},
    onUpdateHaasStereoDoubler: (Int) -> Unit = {},
    onUpdateLateralPinnaCue: (Int) -> Unit = {},
    onToggleSubBassCenterAnchor: () -> Unit = {},
    onToggleVelvetSideAir: () -> Unit = {},
    onSelectEarDirectMode: (EarDirectZeroBrainMode) -> Unit = {},
    onToggleEarDirectSwitch: () -> Unit = {},
    onSelectWooferBox: (WooferEnclosureType) -> Unit = {},
    onUpdateZeroBrainPressure: (Int) -> Unit = {},
    onUpdateEarDirectPinna: (Int) -> Unit = {},
    onUpdateSubRumbleEar: (Int) -> Unit = {},
    onUpdateKickThumpPunch: (Int) -> Unit = {},
    onToggleSkullBoneDrain: () -> Unit = {},
    onToggleVelvetEarCanalSilk: () -> Unit = {},
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
                .height(710.dp)
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
                                text = "EQULTRA v2.7 KAAN-DIRECT & 7-WOOFER LAB",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "👂 100% Kaan • 🧠🚫 0% Dimag • 📦 7 Woofer Boxes • ↔️ 50% L/R Vocal",
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
                    // 00000000000. 👂 EAR-DIRECT ZERO-BRAIN-PRESSURE & 7-WOOFER BOX ENGINE ("KAAN ME SOUND, NA KI DIMAG ME")
                    EarDirectZeroBrainAndWooferCard(
                        uiState = uiState,
                        onSelectEarDirectMode = onSelectEarDirectMode,
                        onToggleMainSwitch = onToggleEarDirectSwitch,
                        onSelectWooferBox = onSelectWooferBox,
                        onUpdateZeroBrainPressure = onUpdateZeroBrainPressure,
                        onUpdateEarDirectPinna = onUpdateEarDirectPinna,
                        onUpdateSubRumbleEar = onUpdateSubRumbleEar,
                        onUpdateKickThumpPunch = onUpdateKickThumpPunch,
                        onToggleSkullBoneDrain = onToggleSkullBoneDrain,
                        onToggleVelvetEarCanalSilk = onToggleVelvetEarCanalSilk
                    )

                    // 0000000000. ↔️ L/R TWIN-SIDE VOCAL SPLITTER (ZERO-FRONT VOCAL ENGINE: 0% FRONT • 50% LEFT + 50% RIGHT)
                    LrTwinVocalSplitterCard(
                        uiState = uiState,
                        onSelectMode = onSelectLrTwinVocalMode,
                        onToggleMainSwitch = onToggleLrTwinVocalSwitch,
                        onUpdateFrontCenterNull = onUpdateFrontCenterNull,
                        onUpdateSideTwinProjection = onUpdateSideTwinProjection,
                        onUpdateHaasStereoDoubler = onUpdateHaasStereoDoubler,
                        onUpdateLateralPinnaCue = onUpdateLateralPinnaCue,
                        onToggleSubBassCenterAnchor = onToggleSubBassCenterAnchor,
                        onToggleVelvetSideAir = onToggleVelvetSideAir
                    )

                    // 000000000. 🌌 APEX SINGULARITY & BIO-ACOUSTIC ENGINE (6-IN-1 ULTRA-POWER SUITE)
                    ApexSingularityCard(
                        uiState = uiState,
                        onSelectMode = onSelectApexSingularityMode,
                        onUpdateGyroStageAnchor = onUpdateGyroStageAnchor,
                        onUpdateTactileChestSlamSub = onUpdateTactileChestSlamSub,
                        onUpdateVocalFormantUnmasker = onUpdateVocalFormantUnmasker,
                        onUpdateMetamaterialShellCloak = onUpdateMetamaterialShellCloak,
                        onToggleGoldenPhiTriodeTape = onToggleGoldenPhiTriodeTape,
                        onToggleFrissonVagusResonator = onToggleFrissonVagusResonator
                    )

                    // 00000000. 🧠 CHRONO-NEURAL & SUB-QUANTUM BIOMIMETIC ENGINE (6-IN-1 NEURO-ACOUSTIC SUITE)
                    ChronoNeuralBiomimeticCard(
                        uiState = uiState,
                        onSelectMode = onSelectChronoNeuralMode,
                        onUpdateCochlearDispersionSync = onUpdateCochlearDispersionSync,
                        onUpdateGamma40HzTimeDilation = onUpdateGamma40HzTimeDilation,
                        onUpdateStapediusReflexDisarmer = onUpdateStapediusReflexDisarmer,
                        onUpdateTartiniOtoacousticSub = onUpdateTartiniOtoacousticSub,
                        onToggleMicroSaccadicStageSway = onToggleMicroSaccadicStageSway,
                        onToggleFibonacciGoldenTapeSilk = onToggleFibonacciGoldenTapeSilk
                    )

                    // 0000000. 🎻 HOLOGRAPHIC LIVE INSTRUMENT REALISM ENGINE (6-IN-1 LIVE STAGE PHYSICS)
                    HolographicLiveInstrumentCard(
                        uiState = uiState,
                        onSelectMode = onSelectLiveInstrumentMode,
                        onUpdatePluckDrumTransient = onUpdatePluckDrumTransient,
                        onUpdateWoodHelmholtzBody = onUpdateWoodHelmholtzBody,
                        onUpdateSympatheticStringBleed = onUpdateSympatheticStringBleed,
                        onUpdateSingerBreathProximity = onUpdateSingerBreathProximity,
                        onToggleHaasStageSeparation = onToggleHaasStageSeparation,
                        onToggleWoodenUnpluggedFloor = onToggleWoodenUnpluggedFloor
                    )

                    // 000000. 🌬️ ZERO-EARBUD INVISIBLE OPEN-AIR ENGINE (6-IN-1 ANTI-OCCLUSION SUITE)
                    ZeroEarbudInvisibleOpenAirCard(
                        uiState = uiState,
                        onSelectMode = onSelectZeroEarbudMode,
                        onUpdateOcclusionValve = onUpdateOcclusionValve,
                        onUpdateOpenCanalUnplug = onUpdateOpenCanalUnplug,
                        onUpdateAirCushion25m = onUpdateAirCushion25m,
                        onUpdateOpenBackElectrostatic = onUpdateOpenBackElectrostatic,
                        onToggleAntiVacuumBreeze = onToggleAntiVacuumBreeze,
                        onToggleVestibularMicroDrift = onToggleVestibularMicroDrift
                    )

                    // 00000. 🧬 OMEGA-PRIME AUTONOMOUS ACOUSTIC AI SUITE (6-IN-1 MASTER ENGINE)
                    OmegaPrimeAutonomousAiCard(
                        uiState = uiState,
                        onSelectMode = onSelectOmegaPrimeMode,
                        onSelectServoProfile = onSelectServoProfile,
                        onUpdateDynamicBreathingEq = onUpdateDynamicBreathingEq,
                        onUpdateVocalSidechainDuck = onUpdateVocalSidechainDuck,
                        onUpdateElevationHelixSky = onUpdateElevationHelixSky,
                        onUpdateGoldenPhiHarmonic = onUpdateGoldenPhiHarmonic,
                        onToggleBioGrooveCardioSync = onToggleBioGrooveCardioSync
                    )

                    // 0000. 🛸 2030 SOVEREIGN R&D LAB (🇯🇵 JAPAN • 🇨🇳 CHINA • 🇺🇸 USA NEXT-GEN ROADMAP)
                    Sovereign2030RndLabCard(
                        uiState = uiState,
                        onSelectMode = onSelectSovereign2030Mode,
                        onUpdatePhantomSub16Hz = onUpdatePhantomSub16Hz,
                        onToggleDopplerEraser = onToggleDopplerEraser,
                        onUpdateDsd1024Damping = onUpdateDsd1024Damping,
                        onToggleBioEarDeEsser = onToggleBioEarDeEsser,
                        onUpdateMit64WfsWall = onUpdateMit64WfsWall,
                        onUpdateStreamDeSquasher = onUpdateStreamDeSquasher
                    )

                    // 000. GLOBAL TRIAD FLAGSHIP HI-FI SUITE (🇯🇵 JAPAN • 🇨🇳 CHINA • 🇺🇸 USA)
                    GlobalTriadHiFiCard(
                        uiState = uiState,
                        onSelectTriadMode = onSelectGlobalTriadMode,
                        onUpdateJapanDsee = onUpdateJapanDsee,
                        onUpdateChinaR2r = onUpdateChinaR2r,
                        onUpdateUsaDirac = onUpdateUsaDirac,
                        onToggleFletcherMunson = onToggleFletcherMunson
                    )

                    // 00. OUT-OF-HEAD ROOM EXTERNALIZER (SMYTH A16 / OOHL STYLE)
                    OutOfHeadRoomExternalizerCard(
                        uiState = uiState,
                        onSelectMode = onSelectOutOfHeadMode,
                        onUpdateDistanceMeters = onUpdateOutOfHeadDistance,
                        onUpdateEarlyReflection = onUpdateOutOfHeadReflection,
                        onTogglePinnaChestHrtf = onTogglePinnaChestHrtf,
                        onToggleSubFloorCoupler = onToggleSubFloorCoupler
                    )

                    // 0. 3D MULTI-INSTRUMENT & DUAL-WOOFER STAGE ARCHITECT
                    MultiNodeInstrumentStageCard(
                        uiState = uiState,
                        onSelectNode = onSelectStageNode,
                        onUpdateNodePosition = onUpdateStageNodePosition,
                        onUpdateNodeGain = onUpdateStageNodeGain,
                        onToggleNodeMute = onToggleStageNodeMute,
                        onToggleNodeSolo = onToggleStageNodeSolo,
                        onApplyFormation = onApplyStageFormation,
                        onUpdateWooferCrossover = onUpdateWooferCrossover,
                        onToggleWooferPhase = onToggleWooferPhase,
                        onToggleCenterVocalLock = onToggleCenterVocalLock
                    )

                    // 0B. 2030 HYPER-MATRIX 10-ENGINE SUITE
                    HyperMatrix2030SuiteCard(
                        uiState = uiState,
                        onSelectGyroMode = onSelectGyroMode,
                        onUpdateSubHarmonicSynth = onUpdateSubHarmonicSynth,
                        onApplyStemPreset = onApplyStemPreset,
                        onUpdateStemLevels = onUpdateStemLevels,
                        onSelectSpeedCruise = onSelectSpeedCruise,
                        onSelectAnalogMode = onSelectAnalogMode,
                        onUpdateAnalogDrive = onUpdateAnalogDrive,
                        onToggleTinnitusShield = onToggleTinnitusShield,
                        onUpdateTinnitusNotchFreq = onUpdateTinnitusNotchFreq,
                        onUpdateCrossfeed = onUpdateCrossfeed,
                        onToggleBassHapticWoofer = onToggleBassHapticWoofer,
                        onApplyVenueClone = onApplyVenueClone,
                        onCaptureReferenceDna = onCaptureReferenceDna
                    )

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
                                            text = "3. 360° Master Soundstage Orbit & L/R Trim",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Global head-tracked orbit + Audiophile Left/Right dB balance",
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
fun HyperMatrix2030SuiteCard(
    uiState: EqUiState,
    onSelectGyroMode: (GyroHeadTrackingMode) -> Unit,
    onUpdateSubHarmonicSynth: (Int) -> Unit,
    onApplyStemPreset: (StemDeMixerPreset) -> Unit,
    onUpdateStemLevels: (Int, Int, Int, Int) -> Unit,
    onSelectSpeedCruise: (SpeedCruiseMode) -> Unit,
    onSelectAnalogMode: (AnalogSaturationMode) -> Unit,
    onUpdateAnalogDrive: (Int) -> Unit,
    onToggleTinnitusShield: () -> Unit,
    onUpdateTinnitusNotchFreq: (Int) -> Unit,
    onUpdateCrossfeed: (Int) -> Unit,
    onToggleBassHapticWoofer: () -> Unit,
    onApplyVenueClone: (AcousticVenueClone) -> Unit,
    onCaptureReferenceDna: () -> Unit
) {
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.5.dp,
            brush = Brush.horizontalGradient(
                listOf(NeonMagenta, ElectricCyan, SignalEmerald)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🚀 2030 HYPER-MATRIX 10-ENGINE DSP SUITE",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "10X ULTRA",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                            color = AmoledBlack,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .background(SignalEmerald, RoundedCornerShape(5.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = uiState.hyperMatrixSummary,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = ElectricCyan,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // 1. 4D GYROSCOPE HEAD-TRACKING & WOOFER CHEST LOCK
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "1. 4D Gyro Head-Tracking & Inertial Woofer Lock",
                            style = MaterialTheme.typography.labelLarge,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Yaw: ${uiState.gyroLiveYawDeg.roundToInt()}°",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GyroHeadTrackingMode.entries.forEach { mode ->
                            val selected = uiState.gyroHeadTrackingMode == mode
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectGyroMode(mode) },
                                label = {
                                    Text(
                                        text = "${mode.title} (${mode.badge})",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ElectricCyan,
                                    selectedLabelColor = AmoledBlack,
                                    containerColor = ObsidianCard,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                }
            }

            // 2 & 8. MISSING-FUNDAMENTAL 18" SUB-HARMONIC SYNTH + BASS-TO-HAPTIC WOOFER
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, NeonMagenta.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "2. Virtual 18\" Sub-Harmonic Synth: ${uiState.subHarmonicSynthPercent}%",
                                style = MaterialTheme.typography.labelLarge,
                                color = NeonMagenta,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Generates 24Hz–45Hz Missing-Fundamental Octave + Haptic Chest Slam",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                                color = TextSecondary
                            )
                        }
                        OutlinedButton(
                            onClick = onToggleBassHapticWoofer,
                            border = BorderStroke(
                                1.dp,
                                if (uiState.isBassHapticWooferEnabled) SignalEmerald else ObsidianBorder
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text(
                                text = if (uiState.isBassHapticWooferEnabled) "💓 HAPTIC SUB: ON" else "💓 HAPTIC SUB",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = if (uiState.isBassHapticWooferEnabled) SignalEmerald else TextPrimary
                            )
                        }
                    }
                    Slider(
                        value = uiState.subHarmonicSynthPercent.toFloat(),
                        onValueChange = { onUpdateSubHarmonicSynth(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )
                }
            }

            // 3. LIVE 4-STEM DE-MIXER & KARAOKE / VOCAL ISOLATOR
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, StudioAmber.copy(alpha = 0.7f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "3. Live 4-Stem De-Mixer & Karaoke Isolator (0%–200%)",
                        style = MaterialTheme.typography.labelLarge,
                        color = StudioAmber,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        StemDeMixerPreset.entries.forEach { preset ->
                            val selected = uiState.selectedStemPreset == preset
                            FilterChip(
                                selected = selected,
                                onClick = { onApplyStemPreset(preset) },
                                label = {
                                    Text(
                                        text = "${preset.title} (${preset.badge})",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = StudioAmber,
                                    selectedLabelColor = AmoledBlack,
                                    containerColor = ObsidianCard,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    StemSliderRow(
                        label = "🎤 Vocals (${uiState.stemVocalPercent}%)",
                        value = uiState.stemVocalPercent,
                        color = ElectricCyan,
                        onChange = {
                            onUpdateStemLevels(it, uiState.stemDrumPercent, uiState.stemBassPercent, uiState.stemMelodyPercent)
                        }
                    )
                    StemSliderRow(
                        label = "🥁 Drums (${uiState.stemDrumPercent}%)",
                        value = uiState.stemDrumPercent,
                        color = StudioAmber,
                        onChange = {
                            onUpdateStemLevels(uiState.stemVocalPercent, it, uiState.stemBassPercent, uiState.stemMelodyPercent)
                        }
                    )
                    StemSliderRow(
                        label = "🔊 Sub-Bass (${uiState.stemBassPercent}%)",
                        value = uiState.stemBassPercent,
                        color = NeonMagenta,
                        onChange = {
                            onUpdateStemLevels(uiState.stemVocalPercent, uiState.stemDrumPercent, it, uiState.stemMelodyPercent)
                        }
                    )
                    StemSliderRow(
                        label = "🎸 Melody/Air (${uiState.stemMelodyPercent}%)",
                        value = uiState.stemMelodyPercent,
                        color = SignalEmerald,
                        onChange = {
                            onUpdateStemLevels(uiState.stemVocalPercent, uiState.stemDrumPercent, uiState.stemBassPercent, it)
                        }
                    )
                }
            }

            // 4 & 5. SPEED-ADAPTIVE CRUISE + ANALOG VACUUM TUBE / TAPE SATURATION
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, HyperViolet.copy(alpha = 0.7f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "4. Speed-Adaptive Car / Bike / Gym Cruise DSP",
                        style = MaterialTheme.typography.labelLarge,
                        color = SignalEmerald,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SpeedCruiseMode.entries.forEach { mode ->
                            val selected = uiState.speedCruiseMode == mode
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectSpeedCruise(mode) },
                                label = {
                                    Text(
                                        text = "${mode.title} [${mode.badge}]",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SignalEmerald,
                                    selectedLabelColor = AmoledBlack,
                                    containerColor = ObsidianCard,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "5. Analog Vacuum Tube, Tape Glue & Air Exciter (Drive ${uiState.analogDrivePercent}%)",
                        style = MaterialTheme.typography.labelLarge,
                        color = HyperViolet,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AnalogSaturationMode.entries.forEach { mode ->
                            val selected = uiState.analogSaturationMode == mode
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectAnalogMode(mode) },
                                label = {
                                    Text(
                                        text = "${mode.title} (${mode.badge})",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = HyperViolet,
                                    selectedLabelColor = Color.White,
                                    containerColor = ObsidianCard,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                    Slider(
                        value = uiState.analogDrivePercent.toFloat(),
                        onValueChange = { onUpdateAnalogDrive(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = HyperViolet,
                            activeTrackColor = HyperViolet
                        )
                    )
                }
            }

            // 6 & 7. SMART TINNITUS NOTCH-SHIELD + BINAURAL STUDIO MONITOR CROSSFEED
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "6. Smart Tinnitus Notch-Shield (${uiState.tinnitusNotchFreqHz} Hz)",
                                style = MaterialTheme.typography.labelLarge,
                                color = ElectricCyan,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = uiState.safeListeningDbText,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                                color = SignalEmerald
                            )
                        }
                        Switch(
                            checked = uiState.isTinnitusShieldEnabled,
                            onCheckedChange = { onToggleTinnitusShield() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AmoledBlack,
                                checkedTrackColor = SignalEmerald
                            )
                        )
                    }
                    Slider(
                        value = uiState.tinnitusNotchFreqHz.toFloat(),
                        onValueChange = { onUpdateTinnitusNotchFreq(it.roundToInt()) },
                        valueRange = 5000f..12000f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    Text(
                        text = "7. Bauer Binaural Studio Monitor Crossfeed: ${uiState.crossfeedStrengthPercent}% (280µs Matrix)",
                        style = MaterialTheme.typography.labelLarge,
                        color = StudioAmber,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = uiState.crossfeedStrengthPercent.toFloat(),
                        onValueChange = { onUpdateCrossfeed(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber
                        )
                    )
                }
            }

            // 9 & 10. MULTI-ROOM VENUE IMPULSE CLONER + AI REFERENCE TRACK DNA MATCHER
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SignalEmerald.copy(alpha = 0.65f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "9. Multi-Room Legendary Acoustic Venue Cloner",
                        style = MaterialTheme.typography.labelLarge,
                        color = SignalEmerald,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AcousticVenueClone.entries.forEach { venue ->
                            val selected = uiState.activeVenueClone == venue
                            FilterChip(
                                selected = selected,
                                onClick = { onApplyVenueClone(venue) },
                                label = {
                                    Text(
                                        text = "${venue.title} (${venue.badge})",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SignalEmerald,
                                    selectedLabelColor = AmoledBlack,
                                    containerColor = ObsidianCard,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "10. AI Reference Track Mix Cloner (${uiState.referenceTrackName})",
                        style = MaterialTheme.typography.labelLarge,
                        color = ElectricCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = onCaptureReferenceDna,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricCyan,
                            contentColor = AmoledBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🧬 CAPTURE & CLONE CURRENT SONG'S 31-BAND MIX DNA",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StemSliderRow(
    label: String,
    value: Int,
    color: Color,
    onChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(118.dp)
        )
        Slider(
            value = value.toFloat(),
            onValueChange = { onChange(it.roundToInt()) },
            valueRange = 0f..200f,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color
            )
        )
    }
}

@Composable
fun OutOfHeadRoomExternalizerCard(
    uiState: EqUiState,
    onSelectMode: (OutOfHeadExternalizerMode) -> Unit,
    onUpdateDistanceMeters: (Float) -> Unit,
    onUpdateEarlyReflection: (Int) -> Unit,
    onTogglePinnaChestHrtf: () -> Unit,
    onToggleSubFloorCoupler: () -> Unit
) {
    val isActive = uiState.outOfHeadMode != OutOfHeadExternalizerMode.OFF
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.5.dp,
            brush = Brush.horizontalGradient(
                listOf(ElectricCyan, SignalEmerald, StudioAmber)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SpeakerGroup,
                            contentDescription = "Out-Of-Head Room Externalizer",
                            tint = SignalEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "OUT-OF-HEAD ROOM EXTERNALIZER (OOHL)",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Makes earbuds/headphones sound like real speakers playing outside in your room!",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = StudioAmber
                    )
                }
                Text(
                    text = if (isActive) uiState.outOfHeadMode.badge else "IN-EAR",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = AmoledBlack,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            if (isActive) SignalEmerald else TextSecondary,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Live Telemetry Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isActive) SignalEmerald.copy(alpha = 0.6f) else ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.outOfHeadTelemetryText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isActive) SignalEmerald else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }

            // 1-Tap Out-Of-Head Speaker Environment Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutOfHeadExternalizerMode.entries.forEach { mode ->
                    val selected = uiState.outOfHeadMode == mode
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectMode(mode) },
                        label = {
                            Text(
                                text = "${mode.title} [${mode.badge}]",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SignalEmerald,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // Distance Throw Slider (0.5m .. 6.0m) + Early Room Wall Reflection Slider (0% .. 100%)
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Speaker Distance Throw (Bahar Ki Doori)",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = String.format(Locale.US, "%.1f Meters Outside Head", uiState.outOfHeadDistanceMeters),
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.outOfHeadDistanceMeters,
                        onValueChange = onUpdateDistanceMeters,
                        valueRange = 0.5f..6.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Early Room Wall, Floor & Desk Bounce (8–22ms)",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.outOfHeadEarlyReflectionPercent}% Room Air",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.outOfHeadEarlyReflectionPercent.toFloat(),
                        onValueChange = { onUpdateEarlyReflection(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber
                        )
                    )
                }
            }

            // Psychoacoustic Hardware Switches: Pinna/Chest HRTF + Sub-Bass Floor Coupler
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.isPinnaChestHrtfEnabled,
                    onClick = onTogglePinnaChestHrtf,
                    label = {
                        Text(
                            text = if (uiState.isPinnaChestHrtfEnabled) {
                                "👂 Outer-Ear Pinna 8kHz Notch & Chest Bounce: ON"
                            } else {
                                "👂 Outer-Ear Pinna & Chest HRTF: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricCyan,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = uiState.isSubFloorCouplerEnabled,
                    onClick = onToggleSubFloorCoupler,
                    label = {
                        Text(
                            text = if (uiState.isSubFloorCouplerEnabled) {
                                "🔊 Sub-Bass Floor & Wall Pressure Wave: ON"
                            } else {
                                "🔊 Sub-Bass Floor Coupler: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonMagenta,
                        selectedLabelColor = Color.White,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun GlobalTriadHiFiCard(
    uiState: EqUiState,
    onSelectTriadMode: (GlobalTriadMode) -> Unit,
    onUpdateJapanDsee: (Int) -> Unit,
    onUpdateChinaR2r: (Int) -> Unit,
    onUpdateUsaDirac: (Int) -> Unit,
    onToggleFletcherMunson: () -> Unit
) {
    val isActive = uiState.globalTriadMode != GlobalTriadMode.OFF
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.6.dp,
            brush = Brush.horizontalGradient(
                listOf(NeonMagenta, StudioAmber, ElectricCyan, SignalEmerald)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Global Triad Hi-Fi",
                            tint = StudioAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🇯🇵 JAPAN • 🇨🇳 CHINA • 🇺🇸 USA FLAGSHIP HI-FI SUITE",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Sony DSEE + K2HD (JP) • Holo R2R DAC + Beryllium (CN) • Vegas Sphere + Dirac Phase (US)",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = ElectricCyan
                    )
                }
                Text(
                    text = uiState.globalTriadMode.badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = AmoledBlack,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            if (isActive) StudioAmber else TextSecondary,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Live Telemetry Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isActive) StudioAmber.copy(alpha = 0.65f) else ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.globalTriadTelemetryText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isActive) StudioAmber else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }

            // 1-Tap Country & Omega Fusion Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                GlobalTriadMode.entries.forEach { mode ->
                    val selected = uiState.globalTriadMode == mode
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectTriadMode(mode) },
                        label = {
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StudioAmber,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // 3 National Flagship Sliders (Japan DSEE, China R2R+Beryllium, USA Dirac+Sphere)
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🇯🇵 Japan: Sony DSEE 14–20kHz Upscaler & K2HD Silk",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.japanDseePercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.japanDseePercent.toFloat(),
                        onValueChange = { onUpdateJapanDsee(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🇨🇳 China: Holo R2R Ladder DAC + Beryllium Bass + EST Air",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.chinaR2rBerylliumPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.chinaR2rBerylliumPercent.toFloat(),
                        onValueChange = { onUpdateChinaR2r(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🇺🇸 USA: Vegas Sphere 167K LFE + Dirac Transient Phase",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.usaDiracSpherePercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.usaDiracSpherePercent.toFloat(),
                        onValueChange = { onUpdateUsaDirac(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = SignalEmerald,
                            activeTrackColor = SignalEmerald
                        )
                    )
                }
            }

            // THX Fletcher-Munson Equal-Loudness Switch
            FilterChip(
                selected = uiState.isFletcherMunsonEnabled,
                onClick = onToggleFletcherMunson,
                label = {
                    Text(
                        text = if (uiState.isFletcherMunsonEnabled) {
                            "🇺🇸 THX ISO 226 Fletcher-Munson Equal-Loudness (Full Sub-Bass at Low Volume): ON"
                        } else {
                            "🇺🇸 THX ISO 226 Fletcher-Munson Equal-Loudness: OFF"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StudioAmber,
                    selectedLabelColor = AmoledBlack,
                    containerColor = AmoledBlack,
                    labelColor = TextSecondary
                )
            )
        }
    }
}

@Composable
fun Sovereign2030RndLabCard(
    uiState: EqUiState,
    onSelectMode: (Sovereign2030RndMode) -> Unit,
    onUpdatePhantomSub16Hz: (Int) -> Unit,
    onToggleDopplerEraser: () -> Unit,
    onUpdateDsd1024Damping: (Int) -> Unit,
    onToggleBioEarDeEsser: () -> Unit,
    onUpdateMit64WfsWall: (Int) -> Unit,
    onUpdateStreamDeSquasher: (Int) -> Unit
) {
    val isActive = uiState.sovereign2030Mode != Sovereign2030RndMode.OFF
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.8.dp,
            brush = Brush.horizontalGradient(
                listOf(ElectricCyan, HyperViolet, NeonMagenta, SignalEmerald, StudioAmber)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "2030 Sovereign R&D Lab",
                            tint = ElectricCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🛸 2030 SOVEREIGN R&D LAB (🇯🇵 • 🇨🇳 • 🇺🇸 FUTURE TECH)",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "🇯🇵 Phantom 16Hz + Doppler-0 • 🇨🇳 1-Bit DSD1024 + De-Esser • 🇺🇸 MIT 64-WFS + De-Squasher",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = SignalEmerald
                    )
                }
                Text(
                    text = uiState.sovereign2030Mode.badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = AmoledBlack,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            if (isActive) ElectricCyan else TextSecondary,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Live Telemetry Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isActive) ElectricCyan.copy(alpha = 0.65f) else ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.sovereign2030TelemetryText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isActive) ElectricCyan else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }

            // 1-Tap 2030 R&D Mode Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Sovereign2030RndMode.entries.forEach { mode ->
                    val selected = uiState.sovereign2030Mode == mode
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectMode(mode) },
                        label = {
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricCyan,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // 4 Future R&D Sliders
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // 1. 🇯🇵 NTT Phantom Sub-16Hz
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🇯🇵 NTT Phantom Sub-16Hz Missing-Fundamental Neuro-Bass",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.phantomSub16HzPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.phantomSub16HzPercent.toFloat(),
                        onValueChange = { onUpdatePhantomSub16Hz(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    // 2. 🇨🇳 1-Bit DSD1024 + Back-EMF Damping Factor
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🇨🇳 1-Bit DSD1024 Liquid Stream & Back-EMF Laser Damping",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.dsd1024DampingPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.dsd1024DampingPercent.toFloat(),
                        onValueChange = { onUpdateDsd1024Damping(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )

                    // 3. 🇺🇸 MIT 64-Speaker Wave-Field Synthesis (WFS) Wall
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🇺🇸 MIT 64-Speaker Wave-Field Synthesis (WFS) Holographic Wall",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.mit64WfsWallPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.mit64WfsWallPercent.toFloat(),
                        onValueChange = { onUpdateMit64WfsWall(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = SignalEmerald,
                            activeTrackColor = SignalEmerald
                        )
                    )

                    // 4. 🇺🇸 YouTube/Spotify Loudness De-Squasher
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🇺🇸 YouTube/Spotify Drum De-Squasher (Crest-Factor Restorer)",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "+${String.format(Locale.US, "%.1f", (uiState.streamDeSquasherPercent / 100f) * 6.0f)} dB",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.streamDeSquasherPercent.toFloat(),
                        onValueChange = { onUpdateStreamDeSquasher(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber
                        )
                    )
                }
            }

            // 2 Hardware R&D Switches: 🇯🇵 Inverse Doppler Vocal Eraser + 🇨🇳 Biometric Ear-Canal De-Esser
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.isDopplerEraserEnabled,
                    onClick = onToggleDopplerEraser,
                    label = {
                        Text(
                            text = if (uiState.isDopplerEraserEnabled) {
                                "🇯🇵 Yamaha Inverse Doppler Vocal Eraser (Crystal Vocals at 100% Bass): ON"
                            } else {
                                "🇯🇵 Yamaha Inverse Doppler Vocal Eraser: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricCyan,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = uiState.isBioEarDeEsserEnabled,
                    onClick = onToggleBioEarDeEsser,
                    label = {
                        Text(
                            text = if (uiState.isBioEarDeEsserEnabled) {
                                "🇨🇳 Biometric Ear-Canal Sibilance De-Esser (Zero 7kHz Piercing): ON"
                            } else {
                                "🇨🇳 Biometric Ear-Canal De-Esser: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonMagenta,
                        selectedLabelColor = Color.White,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun OmegaPrimeAutonomousAiCard(
    uiState: EqUiState,
    onSelectMode: (OmegaPrimeMode) -> Unit,
    onSelectServoProfile: (ServoTransducerProfile) -> Unit,
    onUpdateDynamicBreathingEq: (Int) -> Unit,
    onUpdateVocalSidechainDuck: (Int) -> Unit,
    onUpdateElevationHelixSky: (Int) -> Unit,
    onUpdateGoldenPhiHarmonic: (Int) -> Unit,
    onToggleBioGrooveCardioSync: () -> Unit
) {
    val isActive = uiState.omegaPrimeMode != OmegaPrimeMode.OFF
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.9.dp,
            brush = Brush.horizontalGradient(
                listOf(SignalEmerald, ElectricCyan, StudioAmber, NeonMagenta, HyperViolet)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Omega-Prime Autonomous AI",
                            tint = SignalEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🧬 OMEGA-PRIME AUTONOMOUS ACOUSTIC AI (6-IN-1 MASTER)",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Breathing Dynamic EQ • Vocal-Ducking Sidechain Bass • 45° Sky Helix • Golden φ=1.618 • Servo Guard",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = StudioAmber
                    )
                }
                Text(
                    text = uiState.omegaPrimeMode.badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = AmoledBlack,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            if (isActive) SignalEmerald else TextSecondary,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Live Telemetry Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isActive) SignalEmerald.copy(alpha = 0.7f) else ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.omegaPrimeTelemetryText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isActive) SignalEmerald else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }

            // 1-Tap Omega-Prime Master Mode Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OmegaPrimeMode.entries.forEach { mode ->
                    val selected = uiState.omegaPrimeMode == mode
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectMode(mode) },
                        label = {
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SignalEmerald,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // Virtual Servo-Woofer Transducer Excursion Guard Selector
            Text(
                text = "🛡️ Virtual Servo-Woofer Excursion Guard (Select Your Speaker/Earbud Size):",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = ElectricCyan,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ServoTransducerProfile.entries.forEach { profile ->
                    val selected = uiState.servoTransducerProfile == profile
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectServoProfile(profile) },
                        label = {
                            Text(
                                text = profile.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StudioAmber,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            // 4 Autonomous Acoustic AI Sliders
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // 1. Per-Band Dynamic Breathing EQ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🎚️ Per-Band Dynamic Breathing EQ (Upward Lift + Drop Clamp)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.dynamicBreathingEqPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.dynamicBreathingEqPercent.toFloat(),
                        onValueChange = { onUpdateDynamicBreathingEq(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = SignalEmerald,
                            activeTrackColor = SignalEmerald
                        )
                    )

                    // 2. Vocal-Ducking Sidechain Sub-Bass
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🎤 Studio Vocal-Ducking Sidechain Sub-Bass (100% Bass + Lyrics)",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.vocalSidechainDuckPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.vocalSidechainDuckPercent.toFloat(),
                        onValueChange = { onUpdateVocalSidechainDuck(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    // 3. 3D Frequency-Elevation Helix & 45° Sky Dome
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🌀 3D Frequency-Elevation Helix & 45° Overhead Sky Canopy",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.elevationHelixSkyPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.elevationHelixSkyPercent.toFloat(),
                        onValueChange = { onUpdateElevationHelixSky(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )

                    // 4. Golden-Ratio (Phi = 1.618) Stradivarius Harmonic Exciter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "✨ Golden-Ratio (φ = 1.618) Stradivarius Soul Harmonic Exciter",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.goldenPhiHarmonicPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.goldenPhiHarmonicPercent.toFloat(),
                        onValueChange = { onUpdateGoldenPhiHarmonic(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber
                        )
                    )
                }
            }

            // Bio-Groove Cardio-Rhythm Sync Switch
            FilterChip(
                selected = uiState.isBioGrooveCardioSyncEnabled,
                onClick = onToggleBioGrooveCardioSync,
                label = {
                    Text(
                        text = if (uiState.isBioGrooveCardioSyncEnabled) {
                            "🫀 Bio-Groove Cardio-Rhythm Sync (44Hz Vagus-Nerve Warmth + Kinetic Snap): ON"
                        } else {
                            "🫀 Bio-Groove Cardio-Rhythm Sync: OFF"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SignalEmerald,
                    selectedLabelColor = AmoledBlack,
                    containerColor = AmoledBlack,
                    labelColor = TextSecondary
                )
            )
        }
    }
}

@Composable
fun ZeroEarbudInvisibleOpenAirCard(
    uiState: EqUiState,
    onSelectMode: (ZeroEarbudOpenAirMode) -> Unit,
    onUpdateOcclusionValve: (Int) -> Unit,
    onUpdateOpenCanalUnplug: (Int) -> Unit,
    onUpdateAirCushion25m: (Int) -> Unit,
    onUpdateOpenBackElectrostatic: (Int) -> Unit,
    onToggleAntiVacuumBreeze: () -> Unit,
    onToggleVestibularMicroDrift: () -> Unit
) {
    val isActive = uiState.zeroEarbudMode != ZeroEarbudOpenAirMode.OFF
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 2.0.dp,
            brush = Brush.horizontalGradient(
                listOf(ElectricCyan, SignalEmerald, StudioAmber, HyperViolet, NeonMagenta)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Hearing,
                            contentDescription = "Zero-Earbud Invisible Open-Air Engine",
                            tint = ElectricCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🌬️ ZERO-EARBUD INVISIBLE OPEN-AIR ENGINE (6-IN-1)",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "0% Earbud Plugged Feel! Cancels 160Hz Canal Pressure • Restores 2.7kHz Open Ear • HE-1 Vent",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = SignalEmerald
                    )
                }
                Text(
                    text = uiState.zeroEarbudMode.badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = AmoledBlack,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            if (isActive) ElectricCyan else TextSecondary,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Live Telemetry Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isActive) ElectricCyan.copy(alpha = 0.7f) else ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.zeroEarbudTelemetryText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isActive) ElectricCyan else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }

            // 1-Tap Zero-Earbud Invisible Mode Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ZeroEarbudOpenAirMode.entries.forEach { mode ->
                    val selected = uiState.zeroEarbudMode == mode
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectMode(mode) },
                        label = {
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricCyan,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // 4 Anti-Occlusion & Bionic Open-Air Sliders
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // 1. 🌬️ Tympanic Cabin-Pressure & Occlusion Valve
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🌬️ Tympanic Cabin-Pressure & 160Hz Occlusion Relief Valve",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.occlusionValvePercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.occlusionValvePercent.toFloat(),
                        onValueChange = { onUpdateOcclusionValve(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    // 2. 👂 Open-Canal Quarter-Wave Un-Plugger
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "👂 Open-Canal 2.7kHz Un-Plugger & 8kHz Nozzle Spike Eraser",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.openCanalUnplugPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.openCanalUnplugPercent.toFloat(),
                        onValueChange = { onUpdateOpenCanalUnplug(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = SignalEmerald,
                            activeTrackColor = SignalEmerald
                        )
                    )

                    // 3. ☁️ 2.5m Atmospheric Molecular Air-Cushion
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "☁️ 2.5m Atmospheric Molecular Air-Cushion (8-Hr Zero Pain)",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.airCushion25mPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.airCushion25mPercent.toFloat(),
                        onValueChange = { onUpdateAirCushion25m(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber
                        )
                    )

                    // 4. 🎧 Sennheiser HE-1 / Stax Open-Back Electrostatic Vent
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🎧 Sennheiser HE-1 / Stax Open-Back Electrostatic Vent",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.openBackElectrostaticPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.openBackElectrostaticPercent.toFloat(),
                        onValueChange = { onUpdateOpenBackElectrostatic(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )
                }
            }

            // 5 & 6. 🍃 Anti-Vacuum Room Air-Breeze Cushion + 🧭 0.4Hz Vestibular Micro-Head Drift
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.isAntiVacuumBreezeEnabled,
                    onClick = onToggleAntiVacuumBreeze,
                    label = {
                        Text(
                            text = if (uiState.isAntiVacuumBreezeEnabled) {
                                "🍃 -58dB Anti-Vacuum Room Air-Breeze Cushion (Zero Suction): ON"
                            } else {
                                "🍃 Anti-Vacuum Room Air-Breeze Cushion: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SignalEmerald,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = uiState.isVestibularMicroDriftEnabled,
                    onClick = onToggleVestibularMicroDrift,
                    label = {
                        Text(
                            text = if (uiState.isVestibularMicroDriftEnabled) {
                                "🧭 0.4Hz Vestibular Micro-Head Drift (Un-Glues Sound from Skull): ON"
                            } else {
                                "🧭 0.4Hz Vestibular Micro-Head Drift: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricCyan,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun HolographicLiveInstrumentCard(
    uiState: EqUiState,
    onSelectMode: (HolographicLiveInstrumentMode) -> Unit,
    onUpdatePluckDrumTransient: (Int) -> Unit,
    onUpdateWoodHelmholtzBody: (Int) -> Unit,
    onUpdateSympatheticStringBleed: (Int) -> Unit,
    onUpdateSingerBreathProximity: (Int) -> Unit,
    onToggleHaasStageSeparation: () -> Unit,
    onToggleWoodenUnpluggedFloor: () -> Unit
) {
    val isActive = uiState.liveInstrumentMode != HolographicLiveInstrumentMode.OFF
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 2.0.dp,
            brush = Brush.horizontalGradient(
                listOf(StudioAmber, SignalEmerald, ElectricCyan, NeonMagenta, HyperViolet)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SpeakerGroup,
                            contentDescription = "Holographic Live Instrument Realism Engine",
                            tint = StudioAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🎻 HOLOGRAPHIC LIVE INSTRUMENT REALISM (6-IN-1 STAGE)",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Makes Every Instrument Sound 100% Live! 2ms Pluck/Skin Snap • Wood Body • Sympathetic Bleed",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = StudioAmber
                    )
                }
                Text(
                    text = uiState.liveInstrumentMode.badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = AmoledBlack,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            if (isActive) StudioAmber else TextSecondary,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Live Telemetry Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isActive) StudioAmber.copy(alpha = 0.7f) else ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.liveInstrumentTelemetryText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isActive) StudioAmber else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }

            // 1-Tap Live Instrument Mode Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                HolographicLiveInstrumentMode.entries.forEach { mode ->
                    val selected = uiState.liveInstrumentMode == mode
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectMode(mode) },
                        label = {
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StudioAmber,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // 4 Live Instrument Physics Sliders
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // 1. ⚡ 2ms Pluck, Bow & Drum-Skin Transient Restorer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "⚡ 2ms String-Pluck, Bow-Rosin & Drum-Skin Attack Restorer",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.pluckDrumTransientPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.pluckDrumTransientPercent.toFloat(),
                        onValueChange = { onUpdatePluckDrumTransient(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber
                        )
                    )

                    // 2. 🪵 Spruce-Wood & Hollow-Body Helmholtz Resonator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🪵 Spruce-Wood & Hollow-Body Helmholtz Cavity (110/220/440Hz)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.woodHelmholtzBodyPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.woodHelmholtzBodyPercent.toFloat(),
                        onValueChange = { onUpdateWoodHelmholtzBody(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = SignalEmerald,
                            activeTrackColor = SignalEmerald
                        )
                    )

                    // 3. 🔗 Sympathetic String & Snare-Wire Stage Bleed
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🔗 Sympathetic String & Snare-Wire Live Stage Bleed",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.sympatheticStringBleedPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.sympatheticStringBleedPercent.toFloat(),
                        onValueChange = { onUpdateSympatheticStringBleed(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    // 4. 👄 Singer Breath, Lip-Articulation & Un-Mic'd Proximity
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "👄 Live Singer Breath & Un-Mic'd 4ft Front-Row Projection",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.singerBreathProximityPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.singerBreathProximityPercent.toFloat(),
                        onValueChange = { onUpdateSingerBreathProximity(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )
                }
            }

            // 5 & 6. 🧭 Haas 18ms Acoustic Stage Separation + 🕯️ Tiny Unplugged Wooden Club Floor Bounce
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.isHaasStageSeparationEnabled,
                    onClick = onToggleHaasStageSeparation,
                    label = {
                        Text(
                            text = if (uiState.isHaasStageSeparationEnabled) {
                                "🧭 Haas 18ms Stage Separation (Distinct Chair for Every Instrument): ON"
                            } else {
                                "🧭 Haas 18ms Stage Separation Matrix: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudioAmber,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = uiState.isWoodenUnpluggedFloorEnabled,
                    onClick = onToggleWoodenUnpluggedFloor,
                    label = {
                        Text(
                            text = if (uiState.isWoodenUnpluggedFloorEnabled) {
                                "🕯️ Unplugged Wooden Stage Floor & 12ms First-Reflection: ON"
                            } else {
                                "🕯️ Unplugged Wooden Stage Floor Bounce: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SignalEmerald,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun ChronoNeuralBiomimeticCard(
    uiState: EqUiState,
    onSelectMode: (ChronoNeuralBiomimeticMode) -> Unit,
    onUpdateCochlearDispersionSync: (Int) -> Unit,
    onUpdateGamma40HzTimeDilation: (Int) -> Unit,
    onUpdateStapediusReflexDisarmer: (Int) -> Unit,
    onUpdateTartiniOtoacousticSub: (Int) -> Unit,
    onToggleMicroSaccadicStageSway: () -> Unit,
    onToggleFibonacciGoldenTapeSilk: () -> Unit
) {
    val isActive = uiState.chronoNeuralMode != ChronoNeuralBiomimeticMode.OFF
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 2.0.dp,
            brush = Brush.horizontalGradient(
                listOf(HyperViolet, ElectricCyan, SignalEmerald, StudioAmber, NeonMagenta)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Chrono-Neural & Sub-Quantum Biomimetic Engine",
                            tint = ElectricCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🧠 CHRONO-NEURAL & BIOMIMETIC ENGINE (6-IN-1 NEURO-PHYSICS)",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Cochlear 6ms Fluid Sync • 40Hz Slow-Mo HD Hearing • Stapedius Eardrum Silk • Tartini 24Hz Sub",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = ElectricCyan
                    )
                }
                Text(
                    text = uiState.chronoNeuralMode.badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = AmoledBlack,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            if (isActive) ElectricCyan else TextSecondary,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Live Telemetry Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isActive) ElectricCyan.copy(alpha = 0.7f) else ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.chronoNeuralTelemetryText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isActive) ElectricCyan else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }

            // 1-Tap Chrono-Neural Biomimetic Mode Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ChronoNeuralBiomimeticMode.entries.forEach { mode ->
                    val selected = uiState.chronoNeuralMode == mode
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectMode(mode) },
                        label = {
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricCyan,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // 4 Chrono-Neural Biophysics Sliders
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // 1. 🌀 Cochlear Basilar-Membrane 6ms Dispersion Corrector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🌀 Cochlear Basilar-Membrane 6ms Fluid Phase-Sync",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.cochlearDispersionSyncPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.cochlearDispersionSyncPercent.toFloat(),
                        onValueChange = { onUpdateCochlearDispersionSync(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    // 2. ⚡ 40Hz Gamma-Wave Thalamocortical Time-Dilation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "⚡ 40Hz Gamma Auditory Cortex Time-Dilation (Slow-Mo HD)",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.gamma40HzTimeDilationPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.gamma40HzTimeDilationPercent.toFloat(),
                        onValueChange = { onUpdateGamma40HzTimeDilation(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber
                        )
                    )

                    // 3. 🛡️ Stapedius & Tensor-Tympani Reflex Disarmer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🛡️ Stapedius & Tensor-Tympani Eardrum Muscle Relaxation Silk",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.stapediusReflexDisarmerPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.stapediusReflexDisarmerPercent.toFloat(),
                        onValueChange = { onUpdateStapediusReflexDisarmer(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = SignalEmerald,
                            activeTrackColor = SignalEmerald
                        )
                    )

                    // 4. 💎 Tartini Otoacoustic 24Hz Phantom Sub-Bass Weaver
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "💎 Tartini Otoacoustic 24Hz–32Hz Self-Generated Sub Weaver",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.tartiniOtoacousticSubPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.tartiniOtoacousticSubPercent.toFloat(),
                        onValueChange = { onUpdateTartiniOtoacousticSub(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )
                }
            }

            // 5 & 6. 🦢 0.18Hz Micro-Saccadic Stage Sway + 🌌 Fibonacci Golden-Ratio Tape Silk
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.isMicroSaccadicStageSwayEnabled,
                    onClick = onToggleMicroSaccadicStageSway,
                    label = {
                        Text(
                            text = if (uiState.isMicroSaccadicStageSwayEnabled) {
                                "🦢 0.18Hz Musician Breathing & Golden-Ratio Stage Sway: ON"
                            } else {
                                "🦢 0.18Hz Musician Breathing & Stage Sway: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricCyan,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = uiState.isFibonacciGoldenTapeSilkEnabled,
                    onClick = onToggleFibonacciGoldenTapeSilk,
                    label = {
                        Text(
                            text = if (uiState.isFibonacciGoldenTapeSilkEnabled) {
                                "🌌 Fibonacci (1:0.618:0.382) Zero-Grain Analog Tape Silk: ON"
                            } else {
                                "🌌 Fibonacci Golden-Ratio Tape Silk: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudioAmber,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun ApexSingularityCard(
    uiState: EqUiState,
    onSelectMode: (ApexSingularityMode) -> Unit,
    onUpdateGyroStageAnchor: (Int) -> Unit,
    onUpdateTactileChestSlamSub: (Int) -> Unit,
    onUpdateVocalFormantUnmasker: (Int) -> Unit,
    onUpdateMetamaterialShellCloak: (Int) -> Unit,
    onToggleGoldenPhiTriodeTape: () -> Unit,
    onToggleFrissonVagusResonator: () -> Unit
) {
    val isActive = uiState.apexSingularityMode != ApexSingularityMode.OFF
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 2.2.dp,
            brush = Brush.horizontalGradient(
                listOf(NeonMagenta, ElectricCyan, SignalEmerald, StudioAmber, HyperViolet)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Apex Singularity & Bio-Acoustic Engine",
                            tint = NeonMagenta,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🌌 APEX SINGULARITY ENGINE (6-IN-1 ULTRA-POWER SUITE)",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Gyro 360° Stage Anchor • 18Hz Chest-Slam Sub • Vocal Formant Unmasker • Planar Cloak • Frisson 3x",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = SignalEmerald
                    )
                }
                Text(
                    text = uiState.apexSingularityMode.badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = AmoledBlack,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            if (isActive) SignalEmerald else TextSecondary,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Live Telemetry Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isActive) SignalEmerald.copy(alpha = 0.7f) else ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.apexSingularityTelemetryText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isActive) SignalEmerald else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }

            // 1-Tap Apex Singularity Mode Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ApexSingularityMode.entries.forEach { mode ->
                    val selected = uiState.apexSingularityMode == mode
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectMode(mode) },
                        label = {
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SignalEmerald,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // 4 Ultra-Power Physics Sliders
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // 1. 🧭 Gyro-Spatial 360° Inertial Stage Anchor
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🧭 Gyro-Spatial 360° Inertial Stage Lock (Kalman ITD/ILD)",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.gyroStageAnchorPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.gyroStageAnchorPercent.toFloat(),
                        onValueChange = { onUpdateGyroStageAnchor(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    // 2. 💥 Tactile 18Hz–35Hz Chest-Slam Sub Synthesizer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "💥 Tactile 18Hz–35Hz Somatosensory Chest-Slam Sub Synth",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.tactileChestSlamSubPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.tactileChestSlamSubPercent.toFloat(),
                        onValueChange = { onUpdateTactileChestSlamSub(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )

                    // 3. 🎙️ Holographic Vocal Formant Unmasker (3ft Center Spotlight)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🎙️ Holographic Vocal Breath & Formant Unmasker (3ft Center)",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.vocalFormantUnmaskerPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.vocalFormantUnmaskerPercent.toFloat(),
                        onValueChange = { onUpdateVocalFormantUnmasker(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber
                        )
                    )

                    // 4. 🛡️ Acoustic Metamaterial Shell De-Resonator (Planar Ribbon Cloak)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🛡️ Metamaterial Shell Cloak (Zero-Plastic Planar Ribbon)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.metamaterialShellCloakPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.metamaterialShellCloakPercent.toFloat(),
                        onValueChange = { onUpdateMetamaterialShellCloak(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = SignalEmerald,
                            activeTrackColor = SignalEmerald
                        )
                    )
                }
            }

            // 5 & 6. ✨ Golden-Ratio (φ=1.618) Triode/Tape Flux + ⚡ Frisson (Goosebumps) Vagus Resonator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.isGoldenPhiTriodeTapeEnabled,
                    onClick = onToggleGoldenPhiTriodeTape,
                    label = {
                        Text(
                            text = if (uiState.isGoldenPhiTriodeTapeEnabled) {
                                "✨ Golden-Ratio (φ=1.618) Vacuum Tube & Tape Flux: ON"
                            } else {
                                "✨ Golden-Ratio (φ=1.618) Tube & Tape Flux: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudioAmber,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = uiState.isFrissonVagusResonatorEnabled,
                    onClick = onToggleFrissonVagusResonator,
                    label = {
                        Text(
                            text = if (uiState.isFrissonVagusResonatorEnabled) {
                                "⚡ 0.1Hz Vagus-Nerve & Frisson (Goosebumps 3x) Resonator: ON"
                            } else {
                                "⚡ 0.1Hz Vagus-Nerve Frisson Resonator: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonMagenta,
                        selectedLabelColor = Color.White,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun LrTwinVocalSplitterCard(
    uiState: EqUiState,
    onSelectMode: (LrTwinVocalSplitterMode) -> Unit,
    onToggleMainSwitch: () -> Unit,
    onUpdateFrontCenterNull: (Int) -> Unit,
    onUpdateSideTwinProjection: (Int) -> Unit,
    onUpdateHaasStereoDoubler: (Int) -> Unit,
    onUpdateLateralPinnaCue: (Int) -> Unit,
    onToggleSubBassCenterAnchor: () -> Unit,
    onToggleVelvetSideAir: () -> Unit
) {
    val isActive = uiState.lrTwinVocalMode != LrTwinVocalSplitterMode.OFF
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 2.4.dp,
            brush = Brush.horizontalGradient(
                listOf(ElectricCyan, NeonMagenta, SignalEmerald, StudioAmber)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SurroundSound,
                            contentDescription = "L/R Twin-Side Vocal Splitter",
                            tint = ElectricCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "↔️ L/R TWIN-SIDE VOCAL SPLITTER (0% FRONT VOCAL)",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "0% Front Center Null • 50% Left + 50% Right Vocal Wings • 12ms Haas Doubler • ±90° Pinna Lock",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = ElectricCyan
                    )
                }
                Switch(
                    checked = isActive,
                    onCheckedChange = { onToggleMainSwitch() },
                    modifier = Modifier.testTag("lr_twin_vocal_switch"),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AmoledBlack,
                        checkedTrackColor = ElectricCyan,
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = AmoledBlack
                    )
                )
            }

            // Live Telemetry Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isActive) ElectricCyan.copy(alpha = 0.8f) else ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.lrTwinVocalTelemetryText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isActive) ElectricCyan else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }

            // 1-Tap L/R Twin-Vocal Mode Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LrTwinVocalSplitterMode.entries.forEach { mode ->
                    val selected = uiState.lrTwinVocalMode == mode
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectMode(mode) },
                        label = {
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricCyan,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // 4 Zero-Front L/R Vocal Physics Sliders
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // 1. 🎯 100% Front-Center Vocal Nuller
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🎯 Front-Center Vocal Nuller (0% Front Eraser)",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.frontCenterNullPercent}% NULL",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.frontCenterNullPercent.toFloat(),
                        onValueChange = { onUpdateFrontCenterNull(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )

                    // 2. ↔️ 50% Left + 50% Right Side-Matrix Projector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "↔️ 50% Left + 50% Right Twin-Side Vocal Split",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.sideTwinProjectionPercent}% L/R",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.sideTwinProjectionPercent.toFloat(),
                        onValueChange = { onUpdateSideTwinProjection(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    // 3. ⏱️ 12ms Haas Micro-Phase Stereo Doubler
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "⏱️ 12ms Haas Micro-Phase Stereo Vocal Doubler",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.haasStereoDoublerPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.haasStereoDoublerPercent.toFloat(),
                        onValueChange = { onUpdateHaasStereoDoubler(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber
                        )
                    )

                    // 4. 👂 Lateral ±90° Pinna Azimuth Cue Sculptor
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "👂 Lateral ±90° Pinna Ear-Side Azimuth Lock",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.lateralPinnaCuePercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.lateralPinnaCuePercent.toFloat(),
                        onValueChange = { onUpdateLateralPinnaCue(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = SignalEmerald,
                            activeTrackColor = SignalEmerald
                        )
                    )
                }
            }

            // 5 & 6. 🔒 20Hz–120Hz Sub-Bass Center Anchor + ✨ Velvet Side-Vocal Air
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.isSubBassCenterAnchorEnabled,
                    onClick = onToggleSubBassCenterAnchor,
                    label = {
                        Text(
                            text = if (uiState.isSubBassCenterAnchorEnabled) {
                                "🔒 20Hz–120Hz Sub-Bass Center Lock: ON"
                            } else {
                                "🔒 20Hz–120Hz Sub-Bass Center Lock: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SignalEmerald,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = uiState.isVelvetSideAirEnabled,
                    onClick = onToggleVelvetSideAir,
                    label = {
                        Text(
                            text = if (uiState.isVelvetSideAirEnabled) {
                                "✨ Velvet Side-Vocal Ribbon Air: ON"
                            } else {
                                "✨ Velvet Side-Vocal Ribbon Air: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricCyan,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun EarDirectZeroBrainAndWooferCard(
    uiState: EqUiState,
    onSelectEarDirectMode: (EarDirectZeroBrainMode) -> Unit,
    onToggleMainSwitch: () -> Unit,
    onSelectWooferBox: (WooferEnclosureType) -> Unit,
    onUpdateZeroBrainPressure: (Int) -> Unit,
    onUpdateEarDirectPinna: (Int) -> Unit,
    onUpdateSubRumbleEar: (Int) -> Unit,
    onUpdateKickThumpPunch: (Int) -> Unit,
    onToggleSkullBoneDrain: () -> Unit,
    onToggleVelvetEarCanalSilk: () -> Unit
) {
    val isActive = uiState.earDirectMode != EarDirectZeroBrainMode.OFF
    val activeBox = uiState.wooferEnclosureType
    Surface(
        color = ObsidianCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 2.6.dp,
            brush = Brush.horizontalGradient(
                listOf(SignalEmerald, ElectricCyan, StudioAmber, NeonMagenta)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Hearing,
                            contentDescription = "Ear-Direct Zero-Brain-Pressure Engine",
                            tint = SignalEmerald,
                            modifier = Modifier.size(21.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "👂 KAAN-DIRECT & ZERO-DIMAG ENGINE + 7 WOOFER BOX",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "100% Sound Sidha Left & Right Kaan Me • 0% Dimag/Forehead Pressure • 7 Subwoofer Enclosures",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = SignalEmerald
                    )
                }
                Switch(
                    checked = isActive,
                    onCheckedChange = { onToggleMainSwitch() },
                    modifier = Modifier.testTag("ear_direct_zero_brain_switch"),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AmoledBlack,
                        checkedTrackColor = SignalEmerald,
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = AmoledBlack
                    )
                )
            }

            // Visual Left Kaan | 0% Dimag | Right Kaan Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    color = AmoledBlack,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, if (isActive) SignalEmerald else ObsidianBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "👂 LEFT KAAN",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = if (isActive) "${uiState.earDirectPinnaPercent}% DIRECT" else "BYPASS",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Surface(
                    color = AmoledBlack,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, if (isActive) NeonMagenta else ObsidianBorder),
                    modifier = Modifier.weight(1.15f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🧠🚫 DIMAG / SKULL",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = NeonMagenta,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = if (isActive) "0% PRESSURE (${uiState.zeroBrainPressurePercent}% NULL)" else "NORMAL",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                            color = StudioAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                Surface(
                    color = AmoledBlack,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, if (isActive) ElectricCyan else ObsidianBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "RIGHT KAAN 👂",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = if (isActive) "${uiState.earDirectPinnaPercent}% DIRECT" else "BYPASS",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Live Telemetry Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isActive) SignalEmerald.copy(alpha = 0.8f) else ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.earDirectTelemetryText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isActive) SignalEmerald else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }

            // 1. 1-Tap Kaan-Direct Zero-Dimag Mode Chips
            Text(
                text = "1️⃣ Kaan-Direct Mode (1-Tap Zero-Dimag Selector):",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = ElectricCyan,
                fontWeight = FontWeight.ExtraBold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                EarDirectZeroBrainMode.entries.forEach { mode ->
                    val selected = uiState.earDirectMode == mode
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectEarDirectMode(mode) },
                        label = {
                            Text(
                                text = mode.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SignalEmerald,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // 2. 7-Type Woofer & Subwoofer Enclosure Box Simulator
            Text(
                text = "2️⃣ 7-Type Woofer & Subwoofer Box Simulator (Select Your Bass Cabinet):",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = StudioAmber,
                fontWeight = FontWeight.ExtraBold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                WooferEnclosureType.entries.forEach { box ->
                    val selected = uiState.wooferEnclosureType == box
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectWooferBox(box) },
                        label = {
                            Text(
                                text = box.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StudioAmber,
                            selectedLabelColor = AmoledBlack,
                            containerColor = AmoledBlack,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            // Active Woofer Box Spec Banner
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, StudioAmber.copy(alpha = 0.65f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)) {
                    Text(
                        text = "${activeBox.title}  •  [${activeBox.badge}]",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        color = StudioAmber,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = activeBox.shortSpec,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = TextSecondary
                    )
                }
            }

            // 3. 4 Kaan-Direct & Dual-Zone Bass Physics Sliders
            Surface(
                color = AmoledBlack,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // Slider 1: 🧠🚫 Zero-Brain / Anti-Skull Pressure Nuller
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🧠🚫 Zero-Dimag / Forehead Pressure Nuller (630Hz–3.15kHz)",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.zeroBrainPressurePercent}% NULL",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonMagenta,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.zeroBrainPressurePercent.toFloat(),
                        onValueChange = { onUpdateZeroBrainPressure(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMagenta,
                            activeTrackColor = NeonMagenta
                        )
                    )

                    // Slider 2: 👂↔️👂 Left & Right Outer-Ear Pinna Cup Coupling
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "👂↔️👂 Left & Right Kaan Pinna Cup Coupling (100% Ear Lock)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.earDirectPinnaPercent}% KAAN",
                            style = MaterialTheme.typography.labelSmall,
                            color = SignalEmerald,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.earDirectPinnaPercent.toFloat(),
                        onValueChange = { onUpdateEarDirectPinna(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = SignalEmerald,
                            activeTrackColor = SignalEmerald
                        )
                    )

                    // Slider 3: 🌊 20–50Hz Sub-Rumble Ear-Drum Wave
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🌊 20–50Hz Sub-Woofer Rumble (Deep Ear-Lobe Air Wave)",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.subRumbleEarPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.subRumbleEarPercent.toFloat(),
                        onValueChange = { onUpdateSubRumbleEar(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan
                        )
                    )

                    // Slider 4: 🥁 60–110Hz Tight Kick & Dholak Thump Punch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🥁 60–110Hz Mid-Bass Woofer Kick & Dholak Thump",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.kickThumpPunchPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Slider(
                        value = uiState.kickThumpPunchPercent.toFloat(),
                        onValueChange = { onUpdateKickThumpPunch(it.roundToInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber
                        )
                    )
                }
            }

            // 4. 🦴 180Hz Skull-Bone Boom Drain + ✨ Velvet Ear-Canal Silk Switches
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.isSkullBoneDrainEnabled,
                    onClick = onToggleSkullBoneDrain,
                    label = {
                        Text(
                            text = if (uiState.isSkullBoneDrainEnabled) {
                                "🦴 160–250Hz Skull-Bone Boom Drain (No Head Vibration): ON"
                            } else {
                                "🦴 160–250Hz Skull-Bone Boom Drain: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SignalEmerald,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = uiState.isVelvetEarCanalSilkEnabled,
                    onClick = onToggleVelvetEarCanalSilk,
                    label = {
                        Text(
                            text = if (uiState.isVelvetEarCanalSilkEnabled) {
                                "✨ Velvet Ear-Canal Silk (Zero Ear-Drum Fatigue): ON"
                            } else {
                                "✨ Velvet Ear-Canal Silk: OFF"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricCyan,
                        selectedLabelColor = AmoledBlack,
                        containerColor = AmoledBlack,
                        labelColor = TextSecondary
                    )
                )
            }
        }
    }
}
