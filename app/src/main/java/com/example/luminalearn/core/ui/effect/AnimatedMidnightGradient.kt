package com.example.luminalearn.core.ui.effect

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Hiệu ứng Gradient Midnight chuyển động trôi lượn hữu cơ (Dynamic Flowing Cosmic Gradient).
 * Sử dụng drawBehind để tối ưu hiệu năng 60-120fps mà không gây recomposition hay lag máy.
 * Các dải màu di chuyển mượt mà rõ ràng qua bề mặt thẻ.
 */
fun Modifier.animatedMidnightGradient(
    colors: List<Color> = listOf(
        Color(0xFF0F172A),
        Color(0xFF1E1C59),
        Color(0xFF2E2C80),
        Color(0xFF3F3DBA),
        Color(0xFF1E1C59),
        Color(0xFF0F172A)
    ),
    durationMillis: Int = 6000
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "midnight_gradient_anim")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient_phase"
    )

    this.drawBehind {
        val width = size.width
        val height = size.height
        val shift = width * progress

        val brush = Brush.linearGradient(
            colors = colors,
            start = Offset(-width + shift, -height * 0.3f),
            end = Offset(width * 1.2f + shift, height * 1.3f)
        )
        drawRect(brush = brush)
    }
}
