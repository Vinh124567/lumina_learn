package com.example.luminalearn.core.ui.effect

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Hiệu ứng "Thở" (Breathing pulse): Phập phồng nhẹ nhàng thu hút sự chú ý vào các nút hành động chính.
 */
fun Modifier.breathingGlow(
    minScale: Float = 0.98f,
    maxScale: Float = 1.03f,
    durationMillis: Int = 1800
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "breathing_transition")
    val scale by transition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing_scale"
    )

    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}
