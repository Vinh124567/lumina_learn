package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class VocabCatalogTab(val title: String) {
    HSK_ROADMAP("🎯 Lộ trình HSK"),
    THEMATIC_LIFE("🌟 Chủ đề đời sống")
}

private val ActiveIndigo = Color(0xFF4F46E5)
private val InactiveText = Color(0xFF64748B)

/**
 * Segmented Control 2 Tab với cơ chế di tay bám theo ngón tay 1:1 trong thời gian thực (Interactive Realtime Drag):
 * - Đặt ngón tay và di qua lại liên tục: Viên pill bám dính trực tiếp theo đầu ngón tay không độ trễ.
 * - Nhấc tay ra: Viên pill tự động nảy (snap) bằng Spring Physics đàn hồi về tab gần nhất.
 * - Chạm (Tap): Tự động lướt mượt mà về tab được chạm.
 */
@Composable
fun VocabSegmentedTab(
    selectedTab: VocabCatalogTab,
    onTabSelected: (VocabCatalogTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val tabShape = RoundedCornerShape(11.dp)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF1F5F9))
            .padding(4.dp)
    ) {
        val paddingPx = with(density) { 4.dp.toPx() }
        val totalWidthPx = constraints.maxWidth.toFloat()
        val availableWidthPx = (totalWidthPx - (paddingPx * 2)).coerceAtLeast(1f)
        val tabWidthPx = availableWidthPx / 2f
        val maxOffsetPx = tabWidthPx
        val tabWidthDp = with(density) { tabWidthPx.toDp() }

        // Vị trí offset của Pill Indicator (tính theo pixel)
        val offsetX = remember { Animatable(if (selectedTab == VocabCatalogTab.HSK_ROADMAP) 0f else maxOffsetPx) }
        var isDragging by remember { mutableStateOf(false) }

        // Cập nhật vị trí khi selectedTab thay đổi từ bên ngoài (nếu không đang di tay)
        LaunchedEffect(selectedTab, maxOffsetPx) {
            if (!isDragging && maxOffsetPx > 0f) {
                val targetOffset = if (selectedTab == VocabCatalogTab.HSK_ROADMAP) 0f else maxOffsetPx
                offsetX.animateTo(
                    targetValue = targetOffset,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            }
        }

        val gestureModifier = Modifier
            .pointerInput(maxOffsetPx, selectedTab) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        isDragging = true
                    },
                    onDragEnd = {
                        isDragging = false
                        val currentVal = offsetX.value
                        val targetTab = if (currentVal > (maxOffsetPx / 2f)) {
                            VocabCatalogTab.THEMATIC_LIFE
                        } else {
                            VocabCatalogTab.HSK_ROADMAP
                        }
                        val snapTargetPx = if (targetTab == VocabCatalogTab.THEMATIC_LIFE) maxOffsetPx else 0f

                        if (targetTab != selectedTab) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onTabSelected(targetTab)
                        }

                        scope.launch {
                            offsetX.animateTo(
                                targetValue = snapTargetPx,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            )
                        }
                    },
                    onDragCancel = {
                        isDragging = false
                        val snapTargetPx = if (selectedTab == VocabCatalogTab.THEMATIC_LIFE) maxOffsetPx else 0f
                        scope.launch {
                            offsetX.animateTo(
                                targetValue = snapTargetPx,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            )
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            val newOffset = (offsetX.value + dragAmount).coerceIn(0f, maxOffsetPx)
                            offsetX.snapTo(newOffset)
                        }
                    }
                )
            }
            .pointerInput(totalWidthPx, selectedTab) {
                detectTapGestures { tapPosition ->
                    val isRightSide = tapPosition.x >= (totalWidthPx / 2f)
                    val targetTab = if (isRightSide) VocabCatalogTab.THEMATIC_LIFE else VocabCatalogTab.HSK_ROADMAP
                    if (targetTab != selectedTab) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onTabSelected(targetTab)
                    }
                }
            }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(gestureModifier)
        ) {
            // ── 1. VIÊN PILL BÁM DÍNH TRỰC TIẾP THEO ĐẦU NGÓN TAY TRONG THỜI GIAN THỰC ──
            Box(
                modifier = Modifier
                    .offset { IntOffset(x = offsetX.value.roundToInt(), y = 0) }
                    .width(tabWidthDp)
                    .fillMaxHeight()
                    .shadow(elevation = 2.dp, shape = tabShape)
                    .clip(tabShape)
                    .background(Color.White)
                    .border(BorderStroke(0.5.dp, Color(0xFFE2E8F0)), tabShape)
            )

            // ── 2. LỚP TEXT VỚI HIỆU ỨNG CHUYỂN MÀU THEO TỶ LỆ KÉO THỰC TẾ ──
            val dragFraction = if (maxOffsetPx > 0f) (offsetX.value / maxOffsetPx).coerceIn(0f, 1f) else 0f
            val hskColor = lerp(ActiveIndigo, InactiveText, dragFraction)
            val lifeColor = lerp(InactiveText, ActiveIndigo, dragFraction)

            Row(modifier = Modifier.fillMaxSize()) {
                // Tab 1: HSK
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = VocabCatalogTab.HSK_ROADMAP.title,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = if (dragFraction < 0.5f) FontWeight.Bold else FontWeight.Medium,
                        color = hskColor
                    )
                }

                // Tab 2: Chủ đề đời sống
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = VocabCatalogTab.THEMATIC_LIFE.title,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = if (dragFraction >= 0.5f) FontWeight.Bold else FontWeight.Medium,
                        color = lifeColor
                    )
                }
            }
        }
    }
}
