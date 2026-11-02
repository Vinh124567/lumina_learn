package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Nút trượt Arcade (Swipe to Action / Slide to Enter):
 * - Kéo con trượt (thumb) sang ngang từ trái qua phải.
 * - Khi kéo, dải nền track sẽ fill và chuyển màu gradient động mượt mà theo độ dài ngón tay kéo.
 * - Đạt ngưỡng >= 75% sẽ tự động hoàn tất trượt và kích hoạt callback [onSwiped].
 * - Buông tay trước 75% sẽ bung hiệu ứng spring nảy trở lại vị trí ban đầu.
 */
@Composable
fun ArcadeSwipeButton(
    text: String,
    onSwiped: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 42.dp,
    thumbIcon: String = "▶",
    activeGradient: List<Color> = listOf(
        Color(0xFF0284C7), // Sky Cyan
        Color(0xFF38BDF8), // Light Cyan
        Color(0xFF10B981)  // Neon Emerald
    ),
    inactiveTrackColor: Color = Color(0xFFF1F5F9),
    borderColor: Color = PixelColors.BorderDark
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val trackShape = PixelStepCornerShape(2.5.dp)
    val thumbShape = PixelStepCornerShape(2.dp)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(trackShape)
            .background(inactiveTrackColor)
            .border(BorderStroke(1.5.dp, borderColor), trackShape)
    ) {
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val paddingPx = with(density) { 3.dp.toPx() }
        val thumbSizeDp = height - 6.dp
        val thumbSizePx = with(density) { thumbSizeDp.toPx() }
        val maxDragPx = (maxWidthPx - thumbSizePx - (paddingPx * 2)).coerceAtLeast(1f)

        val offsetX = remember { Animatable(0f) }
        var isDragging by remember { androidx.compose.runtime.mutableStateOf(false) }

        val currentOffset = offsetX.value
        val progress = (currentOffset / maxDragPx).coerceIn(0f, 1f)

        // ── 1. DẢI MÀU GRADIENT CHUYỂN DẦN THEO TIẾN ĐỘ TRƯỢT ──
        if (progress > 0.01f) {
            val filledWidthDp = with(density) { (currentOffset + thumbSizePx + paddingPx).toDp() }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(filledWidthDp)
                    .clip(trackShape)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = activeGradient
                        )
                    )
            )
        }

        // ── 2. TEXT HƯỚNG DẪN Ở GIỮA THANH TRƯỢT ──
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Đổi màu text khi gradient tràn qua
                val textColor = if (progress > 0.45f) Color.White else Color(0xFF475569)
                Text(
                    text = text,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Black,
                    color = textColor,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (progress > 0.75f) "✓" else "»»",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (progress > 0.45f) Color.White else Color(0xFF94A3B8)
                )
            }
        }

        // ── 3. CON TRƯỢT CHÍNH (THUMB HANDLE) ──
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (paddingPx + currentOffset).roundToInt(),
                        y = paddingPx.roundToInt()
                    )
                }
                .size(thumbSizeDp)
                .clip(thumbShape)
                .background(Color.White)
                .border(BorderStroke(1.5.dp, borderColor), thumbShape)
                .pointerInput(maxDragPx) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            isDragging = true
                        },
                        onDragEnd = {
                            isDragging = false
                            if (progress >= 0.75f) {
                                coroutineScope.launch {
                                    // Snap hết sang phải
                                    offsetX.animateTo(
                                        targetValue = maxDragPx,
                                        animationSpec = spring(stiffness = Spring.StiffnessHigh)
                                    )
                                    onSwiped()
                                    // Reset lại vị trí sau 600ms để tái sử dụng
                                    delay(600)
                                    offsetX.snapTo(0f)
                                }
                            } else {
                                coroutineScope.launch {
                                    // Nảy đàn hồi về vị trí cũ
                                    offsetX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMediumLow
                                        )
                                    )
                                }
                            }
                        },
                        onDragCancel = {
                            isDragging = false
                            coroutineScope.launch {
                                offsetX.animateTo(0f)
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            val newTarget = (offsetX.value + dragAmount).coerceIn(0f, maxDragPx)
                            coroutineScope.launch {
                                offsetX.snapTo(newTarget)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = thumbIcon,
                fontSize = 14.sp,
                color = if (progress > 0.75f) PixelColors.PixelGreen else PixelColors.BorderDark
            )
        }
    }
}
