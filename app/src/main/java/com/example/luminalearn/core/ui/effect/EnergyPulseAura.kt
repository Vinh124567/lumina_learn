package com.example.luminalearn.core.ui.effect

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Hiệu ứng Vòng Xoáy Năng Lượng Điện Từ (Energy Pulse & Orbital Rings) độc quyền cho DailyGoalCard.
 * Các vòng sóng năng lượng lan tỏa nhịp nhàng biểu trưng cho sự tích lũy tiến độ học tập hằng ngày.
 */
@Composable
fun EnergyPulseAura(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "energy_pulse_anim")
    val pulseProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_flow"
    )

    val breathingIntensity by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing_intensity"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val center = Offset(width * 0.88f, height * 0.28f)
        val maxRadius = width * 0.45f

        // Vùng sáng xuyên thấu ở góc
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF6366F1).copy(alpha = 0.22f * breathingIntensity),
                    Color(0xFF38BDF8).copy(alpha = 0.12f * breathingIntensity),
                    Color.Transparent
                ),
                center = center,
                radius = maxRadius
            ),
            center = center,
            radius = maxRadius
        )

        // 3 vòng sóng lan tỏa (Expanding Wave Rings)
        for (i in 0..2) {
            val ringProgress = (pulseProgress + i * 0.33f) % 1f
            val ringRadius = 15f + ringProgress * (maxRadius * 0.85f)
            val ringAlpha = (1f - ringProgress) * 0.35f * breathingIntensity

            drawCircle(
                color = Color(0xFF818CF8).copy(alpha = ringAlpha),
                radius = ringRadius,
                center = center,
                style = Stroke(width = 1.5f)
            )
        }
    }
}
