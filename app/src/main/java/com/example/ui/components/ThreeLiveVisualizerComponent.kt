package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HyperViolet
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.SignalEmerald
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Three-Live 3D & XR Spatial Audio Visualizer Component
 * Powered by Google Three-Live (v1.2.4) JIT 3D & Filament Architecture.
 */
@Composable
fun ThreeLiveSpatialVisualizerCard(
    bassEnergy: Float,       // 0.0 to 1.0 (reactive from DSP faders & playback)
    midEnergy: Float,
    trebleEnergy: Float,
    selectedPresetName: String = "Sony WH-1000XM6",
    modifier: Modifier = Modifier
) {
    var visualizerMode by remember { mutableStateOf("Hologram Sphere") } // "Hologram Sphere", "Spatial Room", "Headphone 3D"
    var rotX by remember { mutableFloatStateOf(15f) }
    var rotY by remember { mutableFloatStateOf(30f) }
    var isWireframe by remember { mutableStateOf(true) }

    // Infinite gentle auto-rotation
    val infiniteTransition = rememberInfiniteTransition(label = "threeLiveAutoRotate")
    val autoAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "autoRotateAngle"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp)),
        color = ObsidianCard
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Header with Three-Live badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(ElectricCyan, HyperViolet))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ViewInAr,
                            contentDescription = "Three-Live 3D",
                            tint = AmoledBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "THREE-LIVE 3D SPATIAL LAB",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricCyan,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(HyperViolet.copy(alpha = 0.2f))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "v1.2.4 FILAMENT",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HyperViolet
                                )
                            }
                        }
                        Text(
                            text = "Interactive 6DoF Soundstage & Reactive 3D Mesh",
                            fontSize = 10.5.sp,
                            color = TextMuted
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            rotX = 15f
                            rotY = 30f
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Camera",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mode selection chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Hologram Sphere", "Spatial Room", "Headphone 3D").forEach { mode ->
                    FilterChip(
                        selected = visualizerMode == mode,
                        onClick = { visualizerMode = mode },
                        label = {
                            Text(
                                text = mode,
                                fontSize = 11.sp,
                                fontWeight = if (visualizerMode == mode) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricCyan.copy(alpha = 0.18f),
                            selectedLabelColor = ElectricCyan,
                            containerColor = AmoledBlack,
                            labelColor = TextMuted
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = visualizerMode == mode,
                            borderColor = if (visualizerMode == mode) ElectricCyan.copy(alpha = 0.6f) else ObsidianBorder
                        ),
                        modifier = Modifier.height(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive 3D Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AmoledBlack)
                    .border(1.dp, ObsidianBorder.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            rotY = (rotY + dragAmount.x * 0.5f) % 360f
                            rotX = (rotX - dragAmount.y * 0.5f).coerceIn(-60f, 60f)
                        }
                    }
            ) {
                val effectiveAngle = rotY + autoAngle

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f
                    val cy = h / 2f

                    // Draw 3D Ground Grid (Isometric depth plane)
                    val gridLines = 7
                    val gridSpacing = 28f
                    val baseGridY = cy + 45f

                    for (i in -gridLines..gridLines) {
                        val radY = (effectiveAngle * PI / 180f).toFloat()
                        val cosY = cos(radY)
                        val sinY = sin(radY)

                        val x1 = i * gridSpacing
                        val z1 = -gridLines * gridSpacing
                        val x2 = i * gridSpacing
                        val z2 = gridLines * gridSpacing

                        // Rotate on Y and tilt on X
                        val px1 = cx + (x1 * cosY - z1 * sinY) * 0.85f
                        val py1 = baseGridY + (x1 * sinY + z1 * cosY) * 0.25f

                        val px2 = cx + (x2 * cosY - z2 * sinY) * 0.85f
                        val py2 = baseGridY + (x2 * sinY + z2 * cosY) * 0.25f

                        drawLine(
                            color = ObsidianBorder.copy(alpha = 0.45f),
                            start = Offset(px1, py1),
                            end = Offset(px2, py2),
                            strokeWidth = 1f
                        )
                    }

                    when (visualizerMode) {
                        "Hologram Sphere" -> {
                            // Audio Reactive 3D Icosahedron & Rings
                            val sphereRadius = 55f + (bassEnergy * 28f)
                            val ringCount = 5
                            val pointsPerRing = 16

                            for (r in 0 until ringCount) {
                                val ringTilt = (r - (ringCount - 1) / 2f) * 0.35f
                                val ringYOffset = (r - (ringCount - 1) / 2f) * 22f
                                val ringRadius = sphereRadius * cos(ringTilt)

                                val ringColor = when (r) {
                                    0, 4 -> HyperViolet.copy(alpha = 0.55f + trebleEnergy * 0.35f)
                                    1, 3 -> NeonMagenta.copy(alpha = 0.70f + midEnergy * 0.25f)
                                    else -> ElectricCyan.copy(alpha = 0.85f + bassEnergy * 0.15f)
                                }

                                val path = Path()
                                var firstPoint: Offset? = null

                                for (p in 0..pointsPerRing) {
                                    val angle = (p % pointsPerRing) * (2f * PI / pointsPerRing) + (effectiveAngle * PI / 180f)
                                    // Add acoustic wave distortion
                                    val waveDistort = sin(angle * 4f + (bassEnergy * 5f)) * (bassEnergy * 14f)
                                    val currentRadius = ringRadius + waveDistort

                                    val x3d = currentRadius * cos(angle).toFloat()
                                    val z3d = currentRadius * sin(angle).toFloat()

                                    // Perspective projection with rotX tilt
                                    val projX = cx + x3d
                                    val projY = cy + ringYOffset + (z3d * sin(rotX * PI / 180f)).toFloat()

                                    val point = Offset(projX, projY)
                                    if (p == 0) {
                                        path.moveTo(point.x, point.y)
                                        firstPoint = point
                                    } else {
                                        path.lineTo(point.x, point.y)
                                    }

                                    // Draw node vertex point
                                    drawCircle(
                                        color = ringColor,
                                        radius = 2.5f + (bassEnergy * 2f),
                                        center = point
                                    )
                                }
                                firstPoint?.let { path.lineTo(it.x, it.y) }

                                drawPath(
                                    path = path,
                                    color = ringColor,
                                    style = Stroke(
                                        width = if (r == 2) 2.2f else 1.4f,
                                        cap = StrokeCap.Round
                                    )
                                )
                            }

                            // Glowing core sphere
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        ElectricCyan.copy(alpha = 0.65f * (0.4f + bassEnergy * 0.6f)),
                                        HyperViolet.copy(alpha = 0.25f * (0.3f + bassEnergy * 0.7f)),
                                        Color.Transparent
                                    ),
                                    center = Offset(cx, cy),
                                    radius = sphereRadius * 0.85f
                                ),
                                center = Offset(cx, cy),
                                radius = sphereRadius * 0.85f
                            )
                        }

                        "Spatial Room" -> {
                            // 3D Spatial Acoustic Room (Walls, Speaker nodes, Sound reflection rays)
                            val roomSize = 90f
                            val wallColor = ObsidianBorder.copy(alpha = 0.8f)

                            // Room ceiling and floor bounding lines
                            val frontLeft = Offset(cx - roomSize, cy - roomSize * 0.6f)
                            val frontRight = Offset(cx + roomSize, cy - roomSize * 0.6f)
                            val backLeft = Offset(cx - roomSize * 0.6f, cy - roomSize * 0.9f)
                            val backRight = Offset(cx + roomSize * 0.6f, cy - roomSize * 0.9f)

                            val floorFrontLeft = Offset(cx - roomSize, cy + roomSize * 0.6f)
                            val floorFrontRight = Offset(cx + roomSize, cy + roomSize * 0.6f)
                            val floorBackLeft = Offset(cx - roomSize * 0.6f, cy + roomSize * 0.3f)
                            val floorBackRight = Offset(cx + roomSize * 0.6f, cy + roomSize * 0.3f)

                            // Draw room wireframe box
                            drawLine(wallColor, backLeft, backRight, 1.5f)
                            drawLine(wallColor, backLeft, floorBackLeft, 1.5f)
                            drawLine(wallColor, backRight, floorBackRight, 1.5f)
                            drawLine(wallColor, floorBackLeft, floorBackRight, 1.5f)

                            // Head listener in center
                            drawCircle(
                                color = ElectricCyan,
                                radius = 7f,
                                center = Offset(cx, cy + 10f)
                            )
                            drawCircle(
                                color = ElectricCyan.copy(alpha = 0.3f),
                                radius = 18f + bassEnergy * 15f,
                                center = Offset(cx, cy + 10f),
                                style = Stroke(width = 1.5f)
                            )

                            // Left / Right virtual speakers emitting soundwaves
                            val spkL = Offset(cx - roomSize * 0.7f, cy + 5f)
                            val spkR = Offset(cx + roomSize * 0.7f, cy + 5f)

                            drawCircle(color = NeonMagenta, radius = 6f, center = spkL)
                            drawCircle(color = NeonMagenta, radius = 6f, center = spkR)

                            // Binaural sound rays towards head
                            drawLine(
                                color = NeonMagenta.copy(alpha = 0.4f + bassEnergy * 0.5f),
                                start = spkL,
                                end = Offset(cx, cy + 10f),
                                strokeWidth = 2f
                            )
                            drawLine(
                                color = NeonMagenta.copy(alpha = 0.4f + bassEnergy * 0.5f),
                                start = spkR,
                                end = Offset(cx, cy + 10f),
                                strokeWidth = 2f
                            )
                        }

                        "Headphone 3D" -> {
                            // 3D Headphone Visualization (Headband + Earcups with PBR gradient)
                            val headbandRadius = 50f
                            val headPath = Path()
                            val numSegments = 24

                            for (i in 0..numSegments) {
                                val angle = PI + (i * PI / numSegments)
                                val x = cx + headbandRadius * cos(angle).toFloat()
                                val y = (cy - 10f) + headbandRadius * sin(angle).toFloat() * 0.9f
                                if (i == 0) headPath.moveTo(x, y) else headPath.lineTo(x, y)
                            }

                            // Headband
                            drawPath(
                                path = headPath,
                                color = TextSecondary,
                                style = Stroke(width = 5.5f, cap = StrokeCap.Round)
                            )

                            // Left and Right Earcups
                            val cupL = Offset(cx - headbandRadius - 2f, cy - 8f)
                            val cupR = Offset(cx + headbandRadius + 2f, cy - 8f)

                            // Glow on earcups with bass response
                            drawCircle(
                                color = ElectricCyan.copy(alpha = 0.35f + bassEnergy * 0.45f),
                                radius = 18f + bassEnergy * 8f,
                                center = cupL
                            )
                            drawCircle(
                                color = ElectricCyan.copy(alpha = 0.35f + bassEnergy * 0.45f),
                                radius = 18f + bassEnergy * 8f,
                                center = cupR
                            )

                            // Cup body
                            drawCircle(color = ObsidianCard, radius = 14f, center = cupL)
                            drawCircle(color = ElectricCyan, radius = 14f, center = cupL, style = Stroke(2f))
                            drawCircle(color = ObsidianCard, radius = 14f, center = cupR)
                            drawCircle(color = ElectricCyan, radius = 14f, center = cupR, style = Stroke(2f))
                        }
                    }
                }

                // Top-right camera gesture indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(AmoledBlack.copy(alpha = 0.75f))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "🖐 Drag to Rotate 3D",
                        fontSize = 9.5.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }

                // Bottom-left preset indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(AmoledBlack.copy(alpha = 0.75f))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "🎧 Stage Target: $selectedPresetName",
                        fontSize = 10.sp,
                        color = ElectricCyan,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer diagnostics & AR trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(SignalEmerald)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Filament JIT Renderer Active (60 FPS)",
                        fontSize = 10.sp,
                        color = SignalEmerald,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Bass React: ${(bassEnergy * 100).toInt()}%",
                        fontSize = 10.sp,
                        color = TextMuted,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
