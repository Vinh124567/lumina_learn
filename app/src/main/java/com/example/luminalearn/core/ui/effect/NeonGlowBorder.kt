package com.example.luminalearn.core.ui.effect

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Hiệu ứng viền phát sáng Gradient chuyển động mượt mà (chỉ chạy màu bên trong đường viền, không xoay hình khối).
 */
fun Modifier.neonGlowBorder(
    borderWidth: Dp = 1.5.dp,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: List<Color> = listOf(
        Color(0xFF5C50F6),
        Color(0xFFA855F7),
        Color(0xFF38BDF8),
        Color(0xFF5C50F6)
    ),
    durationMillis: Int = 3000
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "glow_transition")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "glow_progress"
    )

    this.border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(progress * 800f, 0f),
            end = Offset((progress + 1f) * 800f, 800f)
        ),
        shape = shape
    )
}
