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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.core.util.GameSoundHelper
import com.example.luminalearn.ui.theme.PlusJakartaSans
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelSegmentedProgressBar
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

private val BrandPurple = Color(0xFF6366F1)
private val BrandIndigoDark = Color(0xFF4F46E5)
private val BrandCyan = Color(0xFF06B6D4)
private val SuccessGreen = Color(0xFF10B981)
private val BorderHairline = Color(0xFFE2E8F0)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)

private val StationColumnWidth = 96.dp

/**
 * Trạng thái của một trạm bài học trên bản đồ lộ trình
 */
enum class JourneyNodeStatus {
    COMPLETED,
    CURRENT,
    LOCKED,
    NEEDS_REVIEW
}

/**
 * Dữ liệu cấu trúc Unit (Chương bài học)
 */
private data class JourneyUnitData(
    val unitNumber: Int,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val lessons: List<ChineseLessonData>,
    val chestIndex: Int
)

/**
 * Các loại điểm dừng trên hành trình
 */
private sealed class JourneyStationItem {
    data class Lesson(
        val lessonIndex: Int,
        val lesson: ChineseLessonData,
        val status: JourneyNodeStatus
    ) : JourneyStationItem()

    data class Chest(
        val chestIndex: Int,
        val isUnlocked: Boolean,
        val linkedLesson: ChineseLessonData
    ) : JourneyStationItem()
}

/**
 * Trạng thái của đoạn đường nối giữa hai trạm
 */
private enum class ConnectorLineType {
    COMPLETED,
    ACTIVE_FLOW,
    LOCKED
}

/**
 * Bản đồ Lộ Trình Phiêu Lưu Đẳng Cấp (Lumina Pure Adventure Map):
 * - Phân chia theo từng Unit / Chương học với Banner sắc màu sinh động.
 * - Nút 3D dày nẩy tactile (lớp mặt trên lún xuống lớp đáy 3D khi bấm).
 * - Bỏ hoàn toàn thẻ bubble chật chội, chạm vào bài mở ngay LessonPreviewModal chi tiết.
 * - Trạm Rương Báu Mốc Chặng 3D và Cổng Boss Vượt Cấp Hoàng Gia.
 */
@Composable
fun LessonJourneyMapView(
    lessons: List<ChineseLessonData>,
    backendUnits: List<com.example.luminalearn.data.model.JourneyUnitDto> = emptyList(),
    onSelectLesson: (ChineseLessonData) -> Unit,
    onOpenChest: (unitNumber: Int, isUnlocked: Boolean) -> Unit = { _, _ -> },
    onOpenHandbook: (unitNumber: Int, unitTitle: String, lessons: List<ChineseLessonData>) -> Unit = { _, _, _ -> },
    onStartBossExam: (levelName: String, isUnlocked: Boolean) -> Unit = { _, _ -> },
    onQuickReview: (ChineseLessonData) -> Unit = {},
    onOpenAnalytics: () -> Unit = {},
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
                text = "Không có bài học trong cấp độ này",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                color = TextMuted
            )
        }
        return
    }

    val haptic = LocalHapticFeedback.current

    val firstUncompletedIndex = remember(lessons) {
        val idx = lessons.indexOfFirst { !it.isCompleted }
        if (idx >= 0) idx else lessons.size - 1
    }

    val completedCount = lessons.count { it.isCompleted }
    val totalCount = lessons.size
    val currentLevel = lessons.firstOrNull()?.level ?: "HSK"

    // Nhóm bài học thành các Unit (mỗi Unit gồm 3 bài học)
    val units = remember(lessons, backendUnits) {
        buildJourneyUnits(lessons, backendUnits)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── 1. BANNER TỔNG QUAN CẤP ĐỘ HSK (SLIM CAPSULE) ──
        StageRealmHeaderCard(
            levelName = currentLevel,
            completedCount = completedCount,
            totalCount = totalCount,
            onOpenAnalytics = onOpenAnalytics
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── 2. DANH SÁCH CÁC CHƯƠNG (UNITS) SINH ĐỘNG ──
        var globalLessonIndex = 0
        var globalStationCounter = 0

        units.forEachIndexed { unitIndex, unitData ->
            val unitLessons = unitData.lessons
            val unitCompletedCount = unitLessons.count { it.isCompleted }

            // Banner Chương (Unit Section Banner)
            UnitSectionBannerCard(
                unitData = unitData,
                completedCount = unitCompletedCount,
                totalCount = unitLessons.size,
                onOpenHandbook = onOpenHandbook,
                modifier = Modifier.padding(bottom = 18.dp)
            )

            // Danh sách các trạm trong Unit (Bài học + Rương báu ở cuối)
            val unitStations = mutableListOf<JourneyStationItem>()
            unitLessons.forEach { lesson ->
                globalLessonIndex++
                val status = when {
                    lesson.needsReview -> JourneyNodeStatus.NEEDS_REVIEW
                    (globalLessonIndex - 1) < firstUncompletedIndex -> JourneyNodeStatus.COMPLETED
                    (globalLessonIndex - 1) == firstUncompletedIndex -> JourneyNodeStatus.CURRENT
                    else -> JourneyNodeStatus.LOCKED
                }
                unitStations.add(
                    JourneyStationItem.Lesson(
                        lessonIndex = globalLessonIndex,
                        lesson = lesson,
                        status = status
                    )
                )
            }

            // Thêm Rương báu ở cuối mỗi Unit
            val isChestUnlocked = unitCompletedCount == unitLessons.size
            unitStations.add(
                JourneyStationItem.Chest(
                    chestIndex = unitData.chestIndex,
                    isUnlocked = isChestUnlocked,
                    linkedLesson = unitLessons.last()
                )
            )

            // Hiển thị các trạm trong Unit
            unitStations.forEachIndexed { stationLocalIndex, station ->
                val currentBias = getStationBias(globalStationCounter)

                // Hiển thị Quả Cầu 3D hoặc Rương 3D
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .zIndex(2f),
                    contentAlignment = BiasAlignment(currentBias, 0f)
                ) {
                    when (station) {
                        is JourneyStationItem.Lesson -> {
                            Tactile3DLessonNode(
                                lessonIndex = station.lessonIndex,
                                lesson = station.lesson,
                                status = station.status,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    GameSoundHelper.playClick()
                                    if (station.status == JourneyNodeStatus.NEEDS_REVIEW) {
                                        onQuickReview(station.lesson)
                                    } else {
                                        onSelectLesson(station.lesson)
                                    }
                                }
                            )
                        }
                        is JourneyStationItem.Chest -> {
                            Tactile3DChestNode(
                                chestIndex = station.chestIndex,
                                isUnlocked = station.isUnlocked,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    GameSoundHelper.playClick()
                                    onOpenChest(unitData.unitNumber, station.isUnlocked)
                                }
                            )
                        }
                    }
                }

                // Vẽ đường nối tới trạm tiếp theo trong Unit
                val isLastInUnit = stationLocalIndex == unitStations.size - 1
                if (!isLastInUnit) {
                    val nextBias = getStationBias(globalStationCounter + 1)
                    val lineType = getConnectorLineType(station, unitStations[stationLocalIndex + 1])

                    ConstellationBezierConnector(
                        startBias = currentBias,
                        endBias = nextBias,
                        lineType = lineType
                    )
                }

                globalStationCounter++
            }

            // Khoảng cách chuyển tiếp giữa 2 Unit
            if (unitIndex < units.size - 1) {
                val lastStationBias = getStationBias(globalStationCounter - 1)
                val nextUnitFirstBias = getStationBias(globalStationCounter)

                ConstellationBezierConnector(
                    startBias = lastStationBias,
                    endBias = nextUnitFirstBias,
                    lineType = if (unitCompletedCount == unitLessons.size) ConnectorLineType.COMPLETED else ConnectorLineType.LOCKED
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Đoạn nối từ Trạm cuối cùng dẫn vào Cổng Boss Vượt Cấp
        val finalStationBias = getStationBias(globalStationCounter - 1)
        val bossGateBias = if (kotlin.math.abs(finalStationBias) < 0.25f) 0.35f else 0.0f

        ConstellationBezierConnector(
            startBias = finalStationBias,
            endBias = bossGateBias,
            lineType = if (completedCount == totalCount) ConnectorLineType.COMPLETED else ConnectorLineType.LOCKED
        )

        // ── 3. CỔNG THỬ THÁCH VƯỢT CẤP HSK (BOSS CHECKPOINT SHRINE) ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .zIndex(2f),
            contentAlignment = BiasAlignment(bossGateBias, 0f)
        ) {
            ConstellationBossGate(
                levelName = currentLevel,
                totalCount = totalCount,
                isUnlocked = completedCount == totalCount,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    GameSoundHelper.playClick()
                    onStartBossExam(currentLevel, completedCount == totalCount)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * Xây dựng danh sách Unit thông minh từ danh sách bài học
 */
private fun buildJourneyUnits(
    lessons: List<ChineseLessonData>,
    backendUnits: List<com.example.luminalearn.data.model.JourneyUnitDto> = emptyList()
): List<JourneyUnitData> {
    val chunkedLessons = lessons.chunked(3)
    return chunkedLessons.mapIndexed { index, lessonGroup ->
        val beUnit = backendUnits.getOrNull(index)
        val title = beUnit?.title ?: "PHẦN ${index + 1}"
        val desc = beUnit?.description ?: "Kiến thức trọng tâm phần ${index + 1}"
        val emoji = beUnit?.icon ?: "🗣️"
        val primary = try {
            if (beUnit != null) Color(android.graphics.Color.parseColor(beUnit.gradientStart)) else Color(0xFF4F46E5)
        } catch (_: Exception) { Color(0xFF4F46E5) }
        val secondary = try {
            if (beUnit != null) Color(android.graphics.Color.parseColor(beUnit.gradientEnd)) else Color(0xFF6366F1)
        } catch (_: Exception) { Color(0xFF6366F1) }

        JourneyUnitData(
            unitNumber = index + 1,
            title = title,
            description = desc,
            iconEmoji = emoji,
            primaryColor = primary,
            secondaryColor = secondary,
            lessons = lessonGroup,
            chestIndex = index + 1
        )
    }
}

/**
 * Chuỗi tọa độ Bias uốn lượn S-Curve phong phú, tự nhiên.
 * Đảm bảo mọi cặp trạm kề nhau đều có độ chênh lệch biên độ >= 0.40f.
 */
private fun getStationBias(index: Int): Float {
    val biases = floatArrayOf(
        0.00f,  // Trạm 1: Giữa
        -0.52f, // Trạm 2: Trái
        0.48f,  // Trạm 3: Phải
        -0.25f, // Trạm 4 (Rương 1): Giữa-Trái
        0.52f,  // Trạm 5: Phải
        -0.45f, // Trạm 6: Trái
        0.28f,  // Trạm 7: Giữa-Phải
        -0.50f, // Trạm 8: Trái
        0.45f   // Trạm 9: Phải
    )
    return biases[index % biases.size]
}

/**
 * Xác định trạng thái của đường nối dựa trên 2 trạm liên tiếp
 */
private fun getConnectorLineType(
    currentStation: JourneyStationItem,
    nextStation: JourneyStationItem
): ConnectorLineType {
    val isCurrentCompleted = when (currentStation) {
        is JourneyStationItem.Lesson -> currentStation.status == JourneyNodeStatus.COMPLETED
        is JourneyStationItem.Chest -> currentStation.isUnlocked
    }

    val isNextCurrentOrCompleted = when (nextStation) {
        is JourneyStationItem.Lesson -> nextStation.status != JourneyNodeStatus.LOCKED
        is JourneyStationItem.Chest -> nextStation.isUnlocked
    }

    return when {
        isCurrentCompleted && isNextCurrentOrCompleted -> ConnectorLineType.COMPLETED
        isCurrentCompleted && !isNextCurrentOrCompleted -> ConnectorLineType.ACTIVE_FLOW
        else -> ConnectorLineType.LOCKED
    }
}

/**
 * Banner Chương (Unit Section Header Banner Card):
 * - Hộp thoại băng game 16-bit RPG với viền đen 2dp, bóng cứng 3.5dp,
 *   khe tản nhiệt console, nút Arcade [📖 SỔ TAY].
 */
@Composable
private fun UnitSectionBannerCard(
    unitData: JourneyUnitData,
    completedCount: Int,
    totalCount: Int,
    onOpenHandbook: (unitNumber: Int, unitTitle: String, lessons: List<ChineseLessonData>) -> Unit,
    modifier: Modifier = Modifier
) {
    val isUnitDone = completedCount == totalCount

    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderWidth = 2.dp,
        shadowOffset = 3.5.dp,
        headerTitle = "PHẦN ${unitData.unitNumber} ★ QUEST",
        headerBadge = if (isUnitDone) "✓ ĐÃ HOÀN THÀNH" else "$completedCount/$totalCount BÀI",
        headerColor = unitData.primaryColor,
        showGrille = true,
        showRivets = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = unitData.title,
                    fontFamily = PlusJakartaSans,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    letterSpacing = 0.2.sp
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = unitData.description,
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = PixelColors.MutedText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Nút mở Sổ tay dạng Arcade button
                PixelArcadeButton(
                    text = "📖 SỔ TAY",
                    onClick = {
                        onOpenHandbook(
                            unitData.unitNumber,
                            unitData.title,
                            unitData.lessons
                        )
                    },
                    color = PixelColors.PixelYellow,
                    fontSize = 10f,
                    contentPadding = 6.dp,
                    shadowOffset = 2.dp
                )

                // Icon huy hiệu đại diện Unit pixel
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(PixelColors.RetroCream)
                        .border(BorderStroke(1.8.dp, PixelColors.BorderDark), RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = unitData.iconEmoji, fontSize = 20.sp)
                }
            }
        }
    }
}

/**
 * Nút Học 16-Bit RPG Trạm Bài Học (Tactile Pixel Node):
 * - Viền đen dày dặn 2dp, bóng cứng 3D 4dp, phím nảy cơ học.
 * - Bài CURRENT có nhãn [▶ BẮT ĐẦU] vàng neon và mascot dẫn đường.
 * - Bài COMPLETED có huy hiệu 3 sao vàng pixel.
 */
@Composable
private fun Tactile3DLessonNode(
    lessonIndex: Int,
    lesson: ChineseLessonData,
    status: JourneyNodeStatus,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "nodeBreathing")
    val breathingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingAlpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(StationColumnWidth)
    ) {
        // Tag "BẮT ĐẦU" hoặc 3 sao Pixel trên đỉnh nút
        if (status == JourneyNodeStatus.CURRENT) {
            val mascotBobOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "mascotBob"
            )

            // Mascot Avatar pixel nhún nhảy
            Box(
                modifier = Modifier
                    .offset(y = mascotBobOffset.dp)
                    .padding(bottom = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                        .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🦜", fontSize = 13.sp)
                }
            }

            PixelBadge(
                text = "▶ BẮT ĐẦU",
                backgroundColor = PixelColors.PixelYellow,
                textColor = PixelColors.BorderDark,
                borderColor = PixelColors.BorderDark,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        } else if (status == JourneyNodeStatus.COMPLETED) {
            val earnedStars = if (lesson.stars > 0) lesson.stars.coerceIn(1, 3) else 3
            Row(
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 1.5.dp)
                    .padding(bottom = 2.dp)
            ) {
                for (i in 1..3) {
                    val isEarned = i <= earnedStars
                    Text(
                        text = if (isEarned) "⭐" else "☆",
                        fontSize = 9.sp,
                        color = if (isEarned) PixelColors.PixelYellowDark else Color(0xFF94A3B8)
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Kích thước nút và chiều cao 3D
        val orbSize = when (status) {
            JourneyNodeStatus.CURRENT -> 68.dp
            JourneyNodeStatus.NEEDS_REVIEW -> 62.dp
            JourneyNodeStatus.COMPLETED -> 60.dp
            JourneyNodeStatus.LOCKED -> 56.dp
        }
        val depth = 5.dp
        val nodeShape = RoundedCornerShape(14.dp)

        Box(
            modifier = Modifier
                .size(width = orbSize, height = orbSize + depth)
                .bounceClick(scaleDown = 0.93f, onClick = onClick),
            contentAlignment = Alignment.TopCenter
        ) {
            // Hào quang pixel phát sáng cho CURRENT hoặc NEEDS_REVIEW
            if (status == JourneyNodeStatus.CURRENT || status == JourneyNodeStatus.NEEDS_REVIEW) {
                val auraColor = if (status == JourneyNodeStatus.CURRENT) PixelColors.PixelYellow else PixelColors.PixelOrange
                Box(
                    modifier = Modifier
                        .size(orbSize + 12.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(auraColor.copy(alpha = breathingAlpha * 0.4f))
                )
            }

            // 1. Lớp bóng cứng 3D (Hard Shadow)
            Box(
                modifier = Modifier
                    .offset(x = 2.dp, y = depth)
                    .size(orbSize)
                    .clip(nodeShape)
                    .background(PixelColors.ShadowDark)
            )

            // 2. Lớp phím chính mặt trên có viền đen đậm 2dp và bevel highlight
            val (bgColor, iconTint) = when (status) {
                JourneyNodeStatus.CURRENT -> PixelColors.PixelYellow to PixelColors.BorderDark
                JourneyNodeStatus.NEEDS_REVIEW -> PixelColors.PixelOrange to Color.White
                JourneyNodeStatus.COMPLETED -> PixelColors.PixelGreen to Color.White
                JourneyNodeStatus.LOCKED -> Color(0xFFE2E8F0) to Color(0xFF94A3B8)
            }

            Box(
                modifier = Modifier
                    .size(orbSize)
                    .clip(nodeShape)
                    .background(bgColor)
                    .border(BorderStroke(2.dp, PixelColors.BorderDark), nodeShape),
                contentAlignment = Alignment.Center
            ) {
                // Bevel highlight cạnh trên
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(1.dp)
                        .border(
                            BorderStroke(1.2.dp, Color.White.copy(alpha = if (status == JourneyNodeStatus.LOCKED) 0.3f else 0.55f)),
                            RoundedCornerShape(12.dp)
                        )
                )

                when (status) {
                    JourneyNodeStatus.CURRENT -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = "Bắt đầu học",
                            tint = iconTint,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    JourneyNodeStatus.NEEDS_REVIEW -> {
                        Text(
                            text = "⚡",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    JourneyNodeStatus.COMPLETED -> {
                        Text(
                            text = "✓",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    JourneyNodeStatus.LOCKED -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lock),
                                contentDescription = "Chưa mở",
                                tint = iconTint,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = "%02d".format(lessonIndex),
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = iconTint
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Nhãn tên bài Pixel
        if (status == JourneyNodeStatus.NEEDS_REVIEW) {
            PixelBadge(
                text = "⚡ ÔN TẬP +15",
                backgroundColor = PixelColors.CardOrange,
                textColor = PixelColors.PixelOrange,
                borderColor = PixelColors.PixelOrange
            )
        } else {
            PixelBadge(
                text = "BÀI %02d".format(lessonIndex),
                backgroundColor = when (status) {
                    JourneyNodeStatus.CURRENT -> PixelColors.CardYellow
                    JourneyNodeStatus.COMPLETED -> PixelColors.CardGreen
                    else -> Color.White
                },
                textColor = when (status) {
                    JourneyNodeStatus.CURRENT -> PixelColors.BorderDark
                    JourneyNodeStatus.COMPLETED -> PixelColors.PixelGreenDark
                    else -> PixelColors.MutedText
                },
                borderColor = PixelColors.BorderDark
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

/**
 * Trạm Rương Báu 16-Bit Pixel Treasure Chest
 */
@Composable
private fun Tactile3DChestNode(
    chestIndex: Int,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(StationColumnWidth)
    ) {
        val chestSize = 58.dp
        val depth = 5.dp
        val chestShape = RoundedCornerShape(12.dp)

        Box(
            modifier = Modifier
                .size(width = chestSize, height = chestSize + depth)
                .bounceClick(scaleDown = 0.92f, onClick = onClick),
            contentAlignment = Alignment.TopCenter
        ) {
            // Lớp bóng cứng 3D
            Box(
                modifier = Modifier
                    .offset(x = 2.dp, y = depth)
                    .size(chestSize)
                    .clip(chestShape)
                    .background(PixelColors.ShadowDark)
            )

            // Lớp mặt rương
            Box(
                modifier = Modifier
                    .size(chestSize)
                    .clip(chestShape)
                    .background(if (isUnlocked) PixelColors.PixelYellow else Color(0xFFF1F5F9))
                    .border(BorderStroke(2.dp, PixelColors.BorderDark), chestShape),
                contentAlignment = Alignment.Center
            ) {
                // Bevel
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(1.dp)
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)), RoundedCornerShape(10.dp))
                )

                Text(
                    text = if (isUnlocked) "🎁" else "📦",
                    fontSize = 26.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        PixelBadge(
            text = if (isUnlocked) "★ +50 SPARKS" else "RƯƠNG $chestIndex",
            backgroundColor = if (isUnlocked) PixelColors.CardYellow else Color(0xFFF1F5F9),
            textColor = if (isUnlocked) PixelColors.PixelYellowDark else PixelColors.MutedText,
            borderColor = PixelColors.BorderDark
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

/**
 * Cổng thử thách vượt cấp HSK (Boss Castle Gate) phong cách 16-Bit RPG
 */
@Composable
private fun ConstellationBossGate(
    levelName: String,
    totalCount: Int,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (isUnlocked) PixelColors.RetroCream else Color.White,
        borderWidth = 2.dp,
        shadowOffset = 4.dp,
        headerTitle = "⚔️ CỔNG VƯỢT CẤP $levelName",
        headerBadge = if (isUnlocked) "SẴN SÀNG" else "KHÓA",
        headerColor = if (isUnlocked) PixelColors.PixelRed else Color(0xFF94A3B8),
        showGrille = true,
        showRivets = true,
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (isUnlocked) "🏆" else "🏰",
                fontSize = 38.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "ĐẠI CHIẾN VƯỢT CẤP $levelName",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.BorderDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = if (isUnlocked)
                    "Bạn đã hoàn thành đủ $totalCount bài! Sẵn sàng khiêu chiến Boss?"
                else
                    "Hoàn thành đủ $totalCount bài học để mở khóa Cổng Boss",
                fontFamily = PlusJakartaSans,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = PixelColors.MutedText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            PixelArcadeButton(
                text = if (isUnlocked) "⚔️ KHIÊU CHIẾN BOSS NGAY" else "🔒 CHƯA ĐỦ ĐIỀU KIỆN",
                onClick = onClick,
                color = if (isUnlocked) PixelColors.PixelRed else Color(0xFFCBD5E1),
                textColor = if (isUnlocked) Color.White else PixelColors.MutedText,
                fontSize = 12f,
                contentPadding = 9.dp
            )
        }
    }
}

/**
 * Con đường uốn lượn BÉZIER phong cách Arcade đậm chất Retro
 */
@Composable
private fun ConstellationBezierConnector(
    startBias: Float,
    endBias: Float,
    lineType: ConnectorLineType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 20.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val maxOffset = (size.width - StationColumnWidth.toPx()) / 2f

            val startX = centerX + (startBias * maxOffset)
            val endX = centerX + (endBias * maxOffset)

            val startY = 0f
            val endY = size.height
            val dy = endY - startY

            val path = Path().apply {
                moveTo(startX, startY)
                cubicTo(
                    startX, startY + dy * 0.55f,
                    endX, endY - dy * 0.45f,
                    endX, endY
                )
            }

            when (lineType) {
                ConnectorLineType.COMPLETED -> {
                    // Viền đen bóng đổ
                    drawPath(
                        path = path,
                        color = PixelColors.BorderDark,
                        style = Stroke(
                            width = 9.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                    // Ruột đường xanh lá 1-UP
                    drawPath(
                        path = path,
                        color = PixelColors.PixelGreen,
                        style = Stroke(
                            width = 5.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                ConnectorLineType.ACTIVE_FLOW -> {
                    drawPath(
                        path = path,
                        color = PixelColors.BorderDark,
                        style = Stroke(
                            width = 9.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(PixelColors.PixelGreen, PixelColors.PixelYellow)
                        ),
                        style = Stroke(
                            width = 5.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                ConnectorLineType.LOCKED -> {
                    drawPath(
                        path = path,
                        color = Color(0xFFCBD5E1),
                        style = Stroke(
                            width = 4.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }
        }
    }
}

/**
 * Thanh Tiến Độ Tinh Gọn (Slim Progress Capsule Bar) phong cách Pixel
 */
@Composable
private fun StageRealmHeaderCard(
    levelName: String,
    completedCount: Int,
    totalCount: Int,
    onOpenAnalytics: () -> Unit = {}
) {
    val progressRatio = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    val progressPercent = (progressRatio * 100).toInt()

    PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderWidth = 1.8.dp,
        shadowOffset = 2.5.dp,
        showGrille = false,
        showRivets = false,
        onClick = onOpenAnalytics
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Label mục tiêu Pixel RPG
            PixelBadge(
                text = "🧭 $levelName",
                backgroundColor = PixelColors.PixelYellow,
                textColor = PixelColors.BorderDark
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Thanh tiến độ chia vạch pixel
            PixelSegmentedProgressBar(
                progress = progressRatio,
                modifier = Modifier.weight(1f),
                fillColor = PixelColors.PixelGreen,
                emptyColor = Color(0xFFE2E8F0),
                segments = 10
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Số bài và %
            Text(
                text = "$completedCount/$totalCount ($progressPercent%)",
                fontFamily = PlusJakartaSans,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = if (progressPercent == 100) PixelColors.PixelGreenDark else PixelColors.BorderDark
            )

            Spacer(modifier = Modifier.width(6.dp))

            // Nút radar phân tích pixel
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(PixelColors.CardPurple)
                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📊", fontSize = 11.sp)
            }
        }
    }
}
