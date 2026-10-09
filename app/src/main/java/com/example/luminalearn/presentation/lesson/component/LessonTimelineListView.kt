package com.example.luminalearn.presentation.lesson.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val BrandPurple = Color(0xFF6366F1)
private val BrandIndigoDark = Color(0xFF4F46E5)
private val BrandCyan = Color(0xFF06B6D4)
private val SuccessGreen = Color(0xFF10B981)
private val BorderHairline = Color(0xFFE2E8F0)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)

private enum class ListSpineConnectorType {
    COMPLETED,
    ACTIVE_FLOW,
    LOCKED
}

/**
 * Component hiển thị Danh sách bài học theo giao diện Timeline Stepper cao cấp:
 * - Dành riêng cho Tab "Danh sách" (List View).
 * - Cột trục Timeline bên trái kết nối các bài học.
 * - Thẻ bài học chi tiết bên phải hiển thị đầy đủ thông tin chữ Hán, bính âm, thời gian và điểm thưởng.
 */
@Composable
fun LessonTimelineListView(
    lessons: List<ChineseLessonData>,
    onSelectLesson: (ChineseLessonData) -> Unit,
    modifier: Modifier = Modifier
) {
    if (lessons.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Không có bài học trong danh sách này",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                color = TextMuted
            )
        }
        return
    }

    val firstUncompletedIndex = remember(lessons) {
        val idx = lessons.indexOfFirst { !it.isCompleted }
        if (idx >= 0) idx else lessons.size - 1
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        lessons.forEachIndexed { index, lesson ->
            val status = when {
                index < firstUncompletedIndex -> JourneyNodeStatus.COMPLETED
                index == firstUncompletedIndex -> JourneyNodeStatus.CURRENT
                else -> JourneyNodeStatus.LOCKED
            }

            val hasNextItem = index < lessons.size - 1
            val isChestNext = (index + 1) % 3 == 0 && hasNextItem

            val connectorType = when (status) {
                JourneyNodeStatus.COMPLETED -> ListSpineConnectorType.COMPLETED
                JourneyNodeStatus.CURRENT -> ListSpineConnectorType.ACTIVE_FLOW
                JourneyNodeStatus.LOCKED -> ListSpineConnectorType.LOCKED
            }

            // Trạm bài học dạng Timeline Stepper Item
            TimelineStationItem(
                index = index + 1,
                lesson = lesson,
                status = status,
                showConnectorBelow = hasNextItem,
                connectorType = connectorType,
                onClick = { onSelectLesson(lesson) }
            )

            // Trạm Rương Kho Báu Mốc Chặng (Sau mỗi 3 bài học)
            if (isChestNext) {
                val chestConnectorType = when {
                    index < firstUncompletedIndex -> ListSpineConnectorType.COMPLETED
                    index == firstUncompletedIndex -> ListSpineConnectorType.ACTIVE_FLOW
                    else -> ListSpineConnectorType.LOCKED
                }
                TimelineChestItem(
                    chestIndex = (index + 1) / 3,
                    isUnlocked = index < firstUncompletedIndex,
                    showConnectorBelow = true,
                    connectorType = chestConnectorType,
                    onOpenChest = { onSelectLesson(lesson) }
                )
            }
        }
    }
}

@Composable
private fun TimelineStationItem(
    index: Int,
    lesson: ChineseLessonData,
    status: JourneyNodeStatus,
    showConnectorBelow: Boolean,
    connectorType: ListSpineConnectorType,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .width(62.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StationNodeOrb(
                index = index,
                status = status,
                onClick = onClick
            )

            if (showConnectorBelow) {
                VerticalSpineConnector(
                    connectorType = connectorType,
                    modifier = Modifier
                        .weight(1f)
                        .width(62.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (showConnectorBelow) 28.dp else 0.dp)
        ) {
            StationDetailCard(
                index = index,
                lesson = lesson,
                status = status,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun TimelineChestItem(
    chestIndex: Int,
    isUnlocked: Boolean,
    showConnectorBelow: Boolean,
    connectorType: ListSpineConnectorType,
    onOpenChest: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .width(62.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) {
                            Brush.linearGradient(
                                listOf(Color(0xFFFEF08A), Color(0xFFFACC15))
                            )
                        } else {
                            Brush.linearGradient(
                                listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0))
                            )
                        }
                    )
                    .border(
                        BorderStroke(
                            1.5.dp,
                            if (isUnlocked) Color(0xFFF59E0B) else BorderHairline
                        ),
                        CircleShape
                    )
                    .bounceClick(scaleDown = 0.92f, onClick = onOpenChest),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isUnlocked) "🎁" else "📦",
                    fontSize = 20.sp
                )
            }

            if (showConnectorBelow) {
                VerticalSpineConnector(
                    connectorType = connectorType,
                    modifier = Modifier
                        .weight(1f)
                        .width(62.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (showConnectorBelow) 28.dp else 0.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = if (isUnlocked) Color(0xFFFFFBEB) else Color.White,
                border = BorderStroke(
                    1.dp,
                    if (isUnlocked) Color(0xFFFDE68A) else BorderHairline
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "RƯƠNG THƯỞNG MỐC $chestIndex",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) Color(0xFFB45309) else TextMuted
                        )
                        Text(
                            text = if (isUnlocked) "Nhận +50 EXP & Huy hiệu" else "Hoàn thành 3 bài để mở",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VerticalSpineConnector(
    connectorType: ListSpineConnectorType,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        val totalHeight = size.height
        if (totalHeight <= 0) return@Canvas

        val centerX = size.width / 2f
        val segmentCount = 4
        val strokeWidth = 3.5.dp.toPx()
        val gap = 9.dp.toPx()
        val verticalPadding = 6.dp.toPx()
        val availableHeight = totalHeight - (verticalPadding * 2) - ((segmentCount - 1) * gap)

        if (availableHeight <= 0) return@Canvas
        val segmentHeight = availableHeight / segmentCount

        for (i in 0 until segmentCount) {
            val segStartY = verticalPadding + i * (segmentHeight + gap)
            val segEndY = segStartY + segmentHeight

            when (connectorType) {
                ListSpineConnectorType.COMPLETED -> {
                    drawLine(
                        color = SuccessGreen.copy(alpha = 0.22f),
                        start = Offset(centerX, segStartY),
                        end = Offset(centerX, segEndY),
                        strokeWidth = strokeWidth * 2.2f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        brush = Brush.verticalGradient(
                            colors = listOf(SuccessGreen, BrandCyan),
                            startY = segStartY,
                            endY = segEndY
                        ),
                        start = Offset(centerX, segStartY),
                        end = Offset(centerX, segEndY),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }

                ListSpineConnectorType.ACTIVE_FLOW -> {
                    val (segColors, glowAlpha) = when (i) {
                        0 -> listOf(BrandPurple, BrandPurple) to 0.35f
                        1 -> listOf(BrandPurple, BrandIndigoDark) to 0.22f
                        2 -> listOf(BrandIndigoDark, BrandCyan) to 0.15f
                        else -> listOf(BrandCyan.copy(alpha = 0.7f), Color(0xFFCBD5E1)) to 0f
                    }

                    if (glowAlpha > 0f) {
                        drawLine(
                            color = segColors.first().copy(alpha = glowAlpha),
                            start = Offset(centerX, segStartY),
                            end = Offset(centerX, segEndY),
                            strokeWidth = strokeWidth * 2.2f,
                            cap = StrokeCap.Round
                        )
                    }

                    drawLine(
                        brush = Brush.verticalGradient(
                            colors = segColors,
                            startY = segStartY,
                            endY = segEndY
                        ),
                        start = Offset(centerX, segStartY),
                        end = Offset(centerX, segEndY),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }

                ListSpineConnectorType.LOCKED -> {
                    drawLine(
                        color = Color(0xFFE2E8F0),
                        start = Offset(centerX, segStartY),
                        end = Offset(centerX, segEndY),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
private fun StationNodeOrb(
    index: Int,
    status: JourneyNodeStatus,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "nodeBreathing")
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(62.dp)
    ) {
        when (status) {
            JourneyNodeStatus.CURRENT -> {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = BrandPurple,
                    shadowElevation = 3.dp,
                    modifier = Modifier.padding(bottom = 3.dp)
                ) {
                    Text(
                        text = "TIẾP",
                        fontFamily = PlusJakartaSans,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 0.4.sp,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp)
                    )
                }
            }
            JourneyNodeStatus.COMPLETED -> {
                Row(
                    modifier = Modifier.padding(bottom = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    repeat(3) {
                        Text(text = "⭐", fontSize = 7.5.sp)
                    }
                }
            }
            JourneyNodeStatus.LOCKED -> {
                Spacer(modifier = Modifier.height(11.dp))
            }
        }

        val orbSize = when (status) {
            JourneyNodeStatus.CURRENT -> (56.dp * breathingScale)
            JourneyNodeStatus.COMPLETED -> 52.dp
            JourneyNodeStatus.LOCKED -> 48.dp
        }

        Box(
            modifier = Modifier
                .size(orbSize)
                .bounceClick(scaleDown = 0.92f, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            when (status) {
                JourneyNodeStatus.CURRENT -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        BrandPurple.copy(alpha = 0.35f),
                                        BrandCyan.copy(alpha = 0.15f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(elevation = 8.dp, shape = CircleShape, spotColor = BrandPurple)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(BrandPurple, BrandIndigoDark, BrandCyan)
                                )
                            )
                            .border(BorderStroke(2.dp, Color.White), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = "Bắt đầu học",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                JourneyNodeStatus.COMPLETED -> {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(elevation = 3.dp, shape = CircleShape)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(SuccessGreen, Color(0xFF059669))
                                )
                            )
                            .border(BorderStroke(1.8.dp, Color(0xFFD1FAE5)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }

                JourneyNodeStatus.LOCKED -> {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF8FAFC))
                            .border(BorderStroke(1.2.dp, Color(0xFFE2E8F0)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lock),
                                contentDescription = "Chưa mở",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = "%02d".format(index),
                                fontFamily = PlusJakartaSans,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StationDetailCard(
    index: Int,
    lesson: ChineseLessonData,
    status: JourneyNodeStatus,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val topicEmoji = remember(lesson.category) {
        when {
            lesson.category.contains("Phát âm", true) || lesson.category.contains("Pinyin", true) -> "🗣️"
            lesson.category.contains("Bộ thủ", true) || lesson.category.contains("Chữ Hán", true) -> "✍️"
            lesson.category.contains("Ngữ pháp", true) -> "🧠"
            else -> "💬"
        }
    }

    val topicBgColor = remember(lesson.category) {
        when {
            lesson.category.contains("Phát âm", true) || lesson.category.contains("Pinyin", true) -> Color(0xFFEEF2FF)
            lesson.category.contains("Bộ thủ", true) || lesson.category.contains("Chữ Hán", true) -> Color(0xFFFEF3C7)
            lesson.category.contains("Ngữ pháp", true) -> Color(0xFFF3E8FF)
            else -> Color(0xFFE0F2FE)
        }
    }

    val topicTextColor = remember(lesson.category) {
        when {
            lesson.category.contains("Phát âm", true) || lesson.category.contains("Pinyin", true) -> Color(0xFF4F46E5)
            lesson.category.contains("Bộ thủ", true) || lesson.category.contains("Chữ Hán", true) -> Color(0xFFD97706)
            lesson.category.contains("Ngữ pháp", true) -> Color(0xFF9333EA)
            else -> Color(0xFF0284C7)
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = when (status) {
                    JourneyNodeStatus.CURRENT -> 10.dp
                    JourneyNodeStatus.COMPLETED -> 4.dp
                    JourneyNodeStatus.LOCKED -> 1.5.dp
                },
                shape = RoundedCornerShape(22.dp),
                spotColor = when (status) {
                    JourneyNodeStatus.CURRENT -> BrandPurple.copy(alpha = 0.28f)
                    else -> Color(0xFF64748B).copy(alpha = 0.12f)
                }
            )
            .bounceClick(scaleDown = 0.98f, onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(
            width = when (status) {
                JourneyNodeStatus.CURRENT -> 1.5.dp
                JourneyNodeStatus.COMPLETED -> 0.8.dp
                JourneyNodeStatus.LOCKED -> 0.5.dp
            },
            color = when (status) {
                JourneyNodeStatus.CURRENT -> BrandPurple
                JourneyNodeStatus.COMPLETED -> SuccessGreen.copy(alpha = 0.4f)
                JourneyNodeStatus.LOCKED -> BorderHairline
            }
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(4.5.dp)
                    .height(36.dp)
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(topEnd = 3.dp, bottomEnd = 3.dp))
                    .background(
                        when (status) {
                            JourneyNodeStatus.CURRENT -> BrandPurple
                            JourneyNodeStatus.COMPLETED -> SuccessGreen
                            JourneyNodeStatus.LOCKED -> Color(0xFFCBD5E1)
                        }
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(topicBgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = topicEmoji, fontSize = 13.sp)
                        }

                        Text(
                            text = "BÀI %02d".format(index),
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (status == JourneyNodeStatus.CURRENT) BrandPurple else TextMuted,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(7.dp),
                        color = topicBgColor
                    ) {
                        Text(
                            text = lesson.category.ifBlank { "Lý thuyết" },
                            fontFamily = PlusJakartaSans,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = topicTextColor,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp),
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = lesson.title,
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (status == JourneyNodeStatus.LOCKED) Color(0xFF475569) else TextMain,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (lesson.pinyinHanziTitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = lesson.pinyinHanziTitle,
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (status == JourneyNodeStatus.CURRENT) BrandIndigoDark else TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF1F5F9)
                        ) {
                            Text(
                                text = "⏱ 15p",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextMuted,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFFEF9C3)
                        ) {
                            Text(
                                text = "+30 ✨",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF854D0E),
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }

                    when (status) {
                        JourneyNodeStatus.CURRENT -> {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.Transparent,
                                shadowElevation = 3.dp
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(BrandPurple, BrandIndigoDark)
                                            ),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.5.dp)
                                ) {
                                    Text(
                                        text = "Vào học ngay →",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                        JourneyNodeStatus.COMPLETED -> {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Text(
                                    text = "Đã xong ✓",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D),
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }
                        JourneyNodeStatus.LOCKED -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_lock),
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = "Chưa mở",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
