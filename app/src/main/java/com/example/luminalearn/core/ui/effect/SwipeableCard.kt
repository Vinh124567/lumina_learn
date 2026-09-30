package com.example.luminalearn.core.ui.effect

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

enum class SwipeDirection {
    Left,
    Right
}

/**
 * Thẻ vuốt gạt theo quán tính (Tinder/Flashcard style): Vuốt sang phải (Đã thuộc), vuốt sang trái (Ôn lại).
 */
@Composable
fun SwipeableCard(
    modifier: Modifier = Modifier,
    swipeThreshold: Float = 240f,
    onSwiped: (SwipeDirection) -> Unit,
    content: @Composable () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .graphicsLayer {
                rotationZ = (offsetX.value / 35f).coerceIn(-18f, 18f)
                val alphaProgress = 1f - (abs(offsetX.value) / 1200f).coerceIn(0f, 0.35f)
                alpha = alphaProgress
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (offsetX.value > swipeThreshold) {
                                offsetX.animateTo(1200f, spring(stiffness = Spring.StiffnessMedium))
                                onSwiped(SwipeDirection.Right)
                            } else if (offsetX.value < -swipeThreshold) {
                                offsetX.animateTo(-1200f, spring(stiffness = Spring.StiffnessMedium))
                                onSwiped(SwipeDirection.Left)
                            } else {
                                launch { offsetX.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
                                launch { offsetY.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y * 0.35f)
                        }
                    }
                )
            }
    ) {
        content()
    }
}
