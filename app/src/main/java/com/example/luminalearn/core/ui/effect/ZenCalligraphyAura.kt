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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

private data class ZenMote(
    val initialXRatio: Float,
    val initialYRatio: Float,
    val speed: Float,
    val radius: Float,
    val alphaMultiplier: Float
)

/**
 * Hiệu ứng Vầng Trăng Thiền Định & Bụi Sao Cổ Phong (Zen Moon & Floating Stardust) độc quyền cho DailyWisdomCard.
 * Vầng trăng ngọc dịu nhẹ kết hợp các hạt bụi ánh sáng trầm lắng, tạo không gian trang trọng, thư thái cho danh ngôn Hán học.
 */
@Composable
fun ZenCalligraphyAura(
    modifier: Modifier = Modifier,
    moteCount: Int = 18
) {
    val motes = remember(moteCount) {
        val rnd = Random(888)
        List(moteCount) {
            ZenMote(
                initialXRatio = rnd.nextFloat(),
                initialYRatio = rnd.nextFloat(),
                speed = 0.4f + rnd.nextFloat() * 0.6f,
                radius = 1.2f + rnd.nextFloat() * 1.8f,
                alphaMultiplier = 0.3f + rnd.nextFloat() * 0.5f
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "zen_aura_anim")
    val moonGlow by transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "moon_glow"
    )

    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "mote_time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Vầng trăng ngọc Zen Halo ở góc trên bên phải
        val moonCenter = Offset(width * 0.86f, height * 0.32f)
        val moonRadius = width * 0.35f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF818CF8).copy(alpha = 0.15f * moonGlow),
                    Color(0xFF4F46E5).copy(alpha = 0.08f * moonGlow),
                    Color.Transparent
                ),
                center = moonCenter,
                radius = moonRadius
            ),
            center = moonCenter,
            radius = moonRadius
        )

        // Vành trăng khuyết thanh tao (Zen Crescent Ring)
        drawCircle(
            color = Color(0xFFC7D2FE).copy(alpha = 0.12f * moonGlow),
            radius = moonRadius * 0.55f,
            center = moonCenter,
            style = Stroke(width = 1.2f)
        )

        // Các hạt bụi ánh sáng trôi lơ lửng chậm rãi
        motes.forEach { mote ->
            val x = (mote.initialXRatio * width + sin(time * mote.speed) * 12f) % width
            val y = (mote.initialYRatio * height + sin(time * mote.speed + 1f) * 8f) % height
            val alpha = (0.2f + 0.6f * sin(time * mote.speed)) * mote.alphaMultiplier

            drawCircle(
                color = Color(0xFFE0E7FF).copy(alpha = alpha.coerceIn(0.1f, 0.8f)),
                radius = mote.radius,
                center = Offset(x, y)
            )
        }
    }
}
