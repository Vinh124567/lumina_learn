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
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

private data class EmberParticle(
    val initialXRatio: Float,
    val speed: Float,
    val radius: Float,
    val swayAmplitude: Float,
    val swayFrequency: Float,
    val color: Color
)

/**
 * Hiệu ứng Tàn Lửa Bốc Lên (Rising Flame Embers) độc quyền cho StreakCard.
 * Các tàn lửa vàng cam, đỏ hổ phách bay lượn từ dưới lên trên tạo cảm giác ngọn lửa sống động.
 */
@Composable
fun RisingFlameEmbers(
    modifier: Modifier = Modifier,
    particleCount: Int = 28
) {
    val embers = remember(particleCount) {
        val colors = listOf(
            Color(0xFFF59E0B), // Amber
            Color(0xFFEF4444), // Crimson
            Color(0xFFFBBF24), // Gold
            Color(0xFFF97316), // Orange
            Color(0xFFFEF08A)  // Bright Yellow
        )
        val rnd = Random(777)
        List(particleCount) {
            EmberParticle(
                initialXRatio = rnd.nextFloat(),
                speed = 0.35f + rnd.nextFloat() * 0.65f,
                radius = 1.5f + rnd.nextFloat() * 2.2f,
                swayAmplitude = 8f + rnd.nextFloat() * 16f,
                swayFrequency = 1.5f + rnd.nextFloat() * 2.5f,
                color = colors[rnd.nextInt(colors.size)]
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "flame_embers_anim")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ember_flow"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        embers.forEach { ember ->
            // Y position moves from height to 0 and loops
            val rawY = (1f - (progress * ember.speed) % 1f) * height
            val sway = sin(progress * 2f * PI.toFloat() * ember.swayFrequency) * ember.swayAmplitude
            val x = (ember.initialXRatio * width + sway).coerceIn(0f, width)

            // Fade in at bottom, bright in middle, fade out at top
            val normalizedY = rawY / height
            val alpha = (sin(normalizedY * PI.toFloat()) * 0.85f).coerceIn(0f, 0.9f)

            drawCircle(
                color = ember.color.copy(alpha = alpha),
                radius = ember.radius,
                center = Offset(x, rawY)
            )
        }
    }
}
