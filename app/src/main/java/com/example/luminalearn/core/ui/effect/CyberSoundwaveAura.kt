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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.sin

/**
 * Hiệu ứng Sóng Âm Trí Tuệ Nhân Tạo (Cyber AI Soundwave Aura) độc quyền cho AiRoleplayDialogueCard.
 * Các dải sóng âm đối thoại nhảy múa nhịp nhàng ở góc thẻ tạo cảm giác AI đang giao tiếp thực tế.
 */
@Composable
fun CyberSoundwaveAura(
    modifier: Modifier = Modifier,
    barCount: Int = 16
) {
    val transition = rememberInfiniteTransition(label = "cyber_voice_anim")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "voice_phase"
    )

    val auraPulse by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Vòng phát sáng Cyber Radial Glow ở góc trên bên phải
        val center = Offset(width * 0.88f, height * 0.35f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF38BDF8).copy(alpha = 0.18f * auraPulse),
                    Color(0xFF818CF8).copy(alpha = 0.10f * auraPulse),
                    Color.Transparent
                ),
                center = center,
                radius = width * 0.45f
            ),
            center = center,
            radius = width * 0.45f
        )

        // Các vạch sóng âm AI (Equalizer Waveform Bars) thanh mảnh
        val barWidth = 3.5f
        val gap = 5.5f
        val startX = width * 0.72f
        val baseY = height * 0.42f
        val maxBarHeight = height * 0.38f

        for (i in 0 until barCount) {
            val offsetAngle = phase + (i * 0.45f)
            val wave = ((sin(offsetAngle) + 1f) / 2f) * ((sin(offsetAngle * 1.8f) + 1f) / 2f)
            val barH = (maxBarHeight * (0.15f + 0.85f * wave)).coerceAtLeast(4f)
            val x = startX + i * (barWidth + gap)

            if (x < width - 16f) {
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF38BDF8).copy(alpha = 0.75f),
                            Color(0xFFA855F7).copy(alpha = 0.35f)
                        ),
                        startY = baseY - barH / 2f,
                        endY = baseY + barH / 2f
                    ),
                    topLeft = Offset(x, baseY - barH / 2f),
                    size = Size(barWidth, barH),
                    cornerRadius = CornerRadius(2f, 2f)
                )
            }
        }
    }
}
