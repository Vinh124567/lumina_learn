package com.example.luminalearn.core.ui.effect

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Hiệu ứng lướt xuất hiện xếp tầng mềm mại chuẩn iOS (Staggered Entrance Animation).
 * Sử dụng graphicsLayer để tối ưu hiệu năng render GPU, không gây recomposition thừa.
 */
fun Modifier.staggeredEntrance(
    index: Int,
    baseDelayMillis: Int = 35,
    initialOffsetY: Float = 28f
): Modifier = composed {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 320,
            delayMillis = (index * baseDelayMillis).coerceAtLeast(0),
            easing = FastOutSlowInEasing
        ),
        label = "stagger_alpha"
    )

    val offsetY by animateFloatAsState(
        targetValue = if (isVisible) 0f else initialOffsetY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "stagger_offsetY"
    )

    this.graphicsLayer {
        this.alpha = alpha
        this.translationY = offsetY
    }
}
