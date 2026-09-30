package com.example.luminalearn.core.ui.effect

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class StarParticle(
    val baseAngle: Float,
    val distanceRatio: Float,
    val speed: Float,
    val strokeWidth: Float,
    val length: Float,
    val color: Color
)

/**
 * Hiệu ứng Dải Sao Chuyển Động Xoắn Ốc (Cosmic Swirling Starfield / Galaxy Stream)
 * Vẽ các tia sao viên nhộng (capsule dash) bay lượn và xoay vòng theo quỹ đạo xoắn ốc tuyệt đẹp.
 */
@Composable
fun CosmicStarfield(
    modifier: Modifier = Modifier,
    particleCount: Int = 45,
    focusCenterXRatio: Float = 0.85f,
    focusCenterYRatio: Float = 0.45f
) {
    val particles = remember(particleCount) {
        val colors = listOf(
            Color(0xFF818CF8), // Indigo
            Color(0xFF67E8F9), // Cyan
            Color(0xFFA78BFA), // Violet
            Color(0xFFC084FC), // Lavender
            Color(0xFF38BDF8)  // Sky
        )
        val rnd = Random(2026)
        List(particleCount) {
            StarParticle(
                baseAngle = rnd.nextFloat() * 2f * PI.toFloat(),
                distanceRatio = 0.12f + rnd.nextFloat() * 0.88f,
                speed = 0.35f + rnd.nextFloat() * 0.55f,
                strokeWidth = 2f + rnd.nextFloat() * 2.2f,
                length = 3.5f + rnd.nextFloat() * 7f,
                color = colors[rnd.nextInt(colors.size)]
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "starfield_anim")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "starfield_time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width * focusCenterXRatio
        val centerY = size.height * focusCenterYRatio
        val maxRadius = size.width * 0.65f

        particles.forEach { p ->
            val currentAngle = p.baseAngle + time * p.speed
            val r = p.distanceRatio * maxRadius
            val x = centerX + r * cos(currentAngle)
            val y = centerY + r * sin(currentAngle) * 0.62f

            // Tiếp tuyến của quỹ đạo elip để các vạch sao xoay theo luồng chuyển động
            val tangentAngle = currentAngle + (PI / 2f).toFloat()
            val dx = cos(tangentAngle) * (p.length / 2f)
            val dy = sin(tangentAngle) * (p.length / 2f) * 0.62f

            val twinkleAlpha = (0.35f + 0.35f * sin(currentAngle * 2.5f)).coerceIn(0.15f, 0.85f)

            drawLine(
                color = p.color.copy(alpha = twinkleAlpha),
                start = Offset(x - dx, y - dy),
                end = Offset(x + dx, y + dy),
                strokeWidth = p.strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}
