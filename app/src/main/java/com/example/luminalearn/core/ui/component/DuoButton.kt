package com.example.luminalearn.core.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Nút bấm 3D phong cách Duolingo: Khi nhấn ngón tay xuống, mặt nút thụt lún vật lý 3D vào trong mặt phẳng.
 */
@Composable
fun DuoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF5C50F6),
    shadowColor: Color = Color(0xFF3730A3),
    shape: Shape = RoundedCornerShape(16.dp),
    depth: Dp = 4.dp,
    content: @Composable BoxScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val currentOffset by animateDpAsState(
        targetValue = if (isPressed) depth else 0.dp,
        animationSpec = tween(durationMillis = 60),
        label = "duo_btn_press"
    )

    Box(
        modifier = modifier
            .padding(bottom = depth)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        try {
                            tryAwaitRelease()
                        } finally {
                            isPressed = false
                        }
                    },
                    onTap = { onClick() }
                )
            }
    ) {
        // Lớp đáy 3D (Shadow base)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = depth)
                .clip(shape)
                .background(shadowColor)
        )

        // Mặt nút nổi phía trên (Top surface)
        Box(
            modifier = Modifier
                .offset(y = currentOffset)
                .clip(shape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}
