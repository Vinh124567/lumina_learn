package com.example.luminalearn.core.ui.effect

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Thẻ lật 3D 180 độ: Mặt trước hiển thị chữ Hán, chạm vào sẽ xoay lật sang mặt sau (Pinyin, Nghĩa, Ví dụ).
 */
@Composable
fun FlipCard3D(
    modifier: Modifier = Modifier,
    isFlipped: Boolean? = null,
    onFlip: ((Boolean) -> Unit)? = null,
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit
) {
    var internalFlipped by remember { mutableStateOf(false) }
    val flipped = isFlipped ?: internalFlipped

    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "flip_rotation"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 14f * density
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                val nextState = !flipped
                if (isFlipped == null) {
                    internalFlipped = nextState
                }
                onFlip?.invoke(nextState)
            }
    ) {
        if (rotation <= 90f) {
            frontContent()
        } else {
            Box(
                modifier = Modifier.graphicsLayer {
                    rotationY = 180f
                }
            ) {
                backContent()
            }
        }
    }
}
