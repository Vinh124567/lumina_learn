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
import com.example.luminalearn.ui.theme.PlusJakartaSans

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
    LOCKED
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
                text = "Không có bài học trong cấp độ này",
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

    val completedCount = lessons.count { it.isCompleted }
    val totalCount = lessons.size
    val currentLevel = lessons.firstOrNull()?.level ?: "HSK"

    // Nhóm bài học thành các Unit (mỗi Unit gồm 3 bài học)
    val units = remember(lessons) {
        buildJourneyUnits(lessons)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── 1. BANNER TỔNG QUAN CẤP ĐỘ HSK (SLIM CAPSULE) ──
        StageRealmHeaderCard(
            levelName = currentLevel,
            completedCount = completedCount,
            totalCount = totalCount
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
                modifier = Modifier.padding(bottom = 18.dp)
            )

            // Danh sách các trạm trong Unit (Bài học + Rương báu ở cuối)
            val unitStations = mutableListOf<JourneyStationItem>()
            unitLessons.forEach { lesson ->
                globalLessonIndex++
                val status = when {
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
                                onClick = { onSelectLesson(station.lesson) }
                            )
                        }
                        is JourneyStationItem.Chest -> {
                            Tactile3DChestNode(
                                chestIndex = station.chestIndex,
                                isUnlocked = station.isUnlocked,
                                onClick = { onSelectLesson(station.linkedLesson) }
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
                    lessons.lastOrNull()?.let { onSelectLesson(it) }
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * Xây dựng danh sách Unit thông minh từ danh sách bài học
 */
private fun buildJourneyUnits(lessons: List<ChineseLessonData>): List<JourneyUnitData> {
    val unitThemes = listOf(
        Triple("CỬA NGÕ PHÁT ÂM", "Làm quen Pinyin, 4 thanh điệu & chào hỏi cơ bản", "🗣️") to (Color(0xFF4F46E5) to Color(0xFF6366F1)),
        Triple("GIAO TIẾP HÀNG NGÀY", "Số đếm, làm quen bạn mới & hỏi thăm sức khỏe", "💬") to (Color(0xFF0D9488) to Color(0xFF10B981)),
        Triple("MUA SẮM & ĐỜI SỐNG", "Hỏi giá tiền, đi chợ & các món ăn phổ biến", "🛍️") to (Color(0xFFD97706) to Color(0xFFF59E0B)),
        Triple("THỜI GIAN & HẸN HÒ", "Ngày tháng, giờ giấc & các hoạt động cuối tuần", "⏰") to (Color(0xFF0284C7) to Color(0xFF06B6D4)),
        Triple("CHINH PHỤC NÂNG CAO", "Mở rộng vốn từ vựng & cấu trúc ngữ pháp chặng cuối", "🌟") to (Color(0xFF7C3AED) to Color(0xFFA855F7))
    )

    val chunkedLessons = lessons.chunked(3)
    return chunkedLessons.mapIndexed { index, lessonGroup ->
        val themeIndex = index % unitThemes.size
        val (meta, colors) = unitThemes[themeIndex]
        val (title, desc, emoji) = meta
        val (primary, secondary) = colors

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
 * - Tạo nhịp điệu sinh động cho toàn bộ hành trình phiêu lưu.
 * - Hiển thị cấp độ, tên chương, mô tả và tiến độ hoàn thành.
 */
@Composable
private fun UnitSectionBannerCard(
    unitData: JourneyUnitData,
    completedCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    val isUnitDone = completedCount == totalCount

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 6.dp,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(unitData.primaryColor, unitData.secondaryColor)
                    )
                )
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.22f)
                        ) {
                            Text(
                                text = "PHẦN ${unitData.unitNumber}",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = 0.6.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Text(
                            text = if (isUnitDone) "✓ Đã hoàn thành" else "$completedCount/$totalCount bài",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.90f)
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = unitData.title,
                        fontFamily = PlusJakartaSans,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.2.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = unitData.description,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.85f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Icon huy hiệu đại diện cho chương
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.20f))
                        .border(BorderStroke(1.5.dp, Color.White.copy(alpha = 0.40f)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = unitData.iconEmoji, fontSize = 22.sp)
                }
            }
        }
    }
}

/**
 * Nút Học 3D Dày Nẩy Chuẩn Duolingo (Tactile 3D Extruded Button):
 * - Có lớp đáy 3D sẫm màu phía dưới (extrude depth).
 * - Khi bấm, nút có độ nẩy lún cơ học cực kỳ chân thực.
 * - Bài CURRENT có vòng phát sáng nhịp thở và badge "BẮT ĐẦU".
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
        initialValue = 0.25f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingAlpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(StationColumnWidth)
    ) {
        // Tag "BẮT ĐẦU" nổi trên đỉnh nút đang học
        if (status == JourneyNodeStatus.CURRENT) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BrandPurple,
                shadowElevation = 3.dp,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Text(
                    text = "BẮT ĐẦU",
                    fontFamily = PlusJakartaSans,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                )
            }
        } else if (status == JourneyNodeStatus.COMPLETED) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                repeat(3) {
                    Text(text = "⭐", fontSize = 10.sp)
                }
            }
        } else {
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Kích thước nút và độ dày 3D
        val orbSize = when (status) {
            JourneyNodeStatus.CURRENT -> 70.dp
            JourneyNodeStatus.COMPLETED -> 62.dp
            JourneyNodeStatus.LOCKED -> 56.dp
        }
        val depth = when (status) {
            JourneyNodeStatus.CURRENT -> 8.dp
            JourneyNodeStatus.COMPLETED -> 7.dp
            JourneyNodeStatus.LOCKED -> 6.dp
        }

        // Bọc nút Tactile 3D
        Box(
            modifier = Modifier
                .size(width = orbSize, height = orbSize + depth)
                .bounceClick(scaleDown = 0.93f, onClick = onClick),
            contentAlignment = Alignment.TopCenter
        ) {
            // Hào quang phát sáng cho bài đang học
            if (status == JourneyNodeStatus.CURRENT) {
                Box(
                    modifier = Modifier
                        .size(orbSize + 16.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    BrandPurple.copy(alpha = breathingAlpha),
                                    BrandCyan.copy(alpha = breathingAlpha * 0.5f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            // 1. LỚP ĐÁY 3D EXTRUSION (Tạo chiều sâu vật lý)
            val bottomColor = when (status) {
                JourneyNodeStatus.CURRENT -> Color(0xFF3730A3)
                JourneyNodeStatus.COMPLETED -> Color(0xFF047857)
                JourneyNodeStatus.LOCKED -> Color(0xFFCBD5E1)
            }

            Box(
                modifier = Modifier
                    .offset(y = depth)
                    .size(orbSize)
                    .clip(CircleShape)
                    .background(bottomColor)
            )

            // 2. LỚP MẶT TRÊN NÚT BẤM (Mặt phím)
            when (status) {
                JourneyNodeStatus.CURRENT -> {
                    Box(
                        modifier = Modifier
                            .size(orbSize)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF6366F1), Color(0xFF4F46E5), Color(0xFF06B6D4))
                                )
                            )
                            .border(BorderStroke(2.5.dp, Color.White), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = "Bắt đầu học",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                JourneyNodeStatus.COMPLETED -> {
                    Box(
                        modifier = Modifier
                            .size(orbSize)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF10B981), Color(0xFF059669))
                                )
                            )
                            .border(BorderStroke(2.5.dp, Color(0xFFD1FAE5)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                JourneyNodeStatus.LOCKED -> {
                    Box(
                        modifier = Modifier
                            .size(orbSize)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .border(BorderStroke(1.5.dp, Color(0xFFE2E8F0)), CircleShape),
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
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = "%02d".format(lessonIndex),
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Nhãn tên bài dưới chân nút
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (status == JourneyNodeStatus.CURRENT) BrandPurple.copy(alpha = 0.10f) else Color.White,
            border = BorderStroke(
                0.8.dp,
                if (status == JourneyNodeStatus.CURRENT) BrandPurple.copy(alpha = 0.35f) else BorderHairline
            )
        ) {
            Text(
                text = "Bài %02d".format(lessonIndex),
                fontFamily = PlusJakartaSans,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = when (status) {
                    JourneyNodeStatus.CURRENT -> BrandPurple
                    JourneyNodeStatus.COMPLETED -> SuccessGreen
                    JourneyNodeStatus.LOCKED -> TextMuted
                },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.5.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

/**
 * Trạm Rương Báu Mốc Chặng 3D Tactile
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
        val chestSize = 60.dp
        val depth = 7.dp

        Box(
            modifier = Modifier
                .size(width = chestSize, height = chestSize + depth)
                .bounceClick(scaleDown = 0.92f, onClick = onClick),
            contentAlignment = Alignment.TopCenter
        ) {
            // Lớp đáy 3D
            val bottomColor = if (isUnlocked) Color(0xFFD97706) else Color(0xFFCBD5E1)
            Box(
                modifier = Modifier
                    .offset(y = depth)
                    .size(chestSize)
                    .clip(CircleShape)
                    .background(bottomColor)
            )

            // Lớp mặt bệ rương
            Box(
                modifier = Modifier
                    .size(chestSize)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) {
                            Brush.linearGradient(listOf(Color(0xFFFEF08A), Color(0xFFFACC15)))
                        } else {
                            Brush.linearGradient(listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0)))
                        }
                    )
                    .border(
                        BorderStroke(2.dp, if (isUnlocked) Color(0xFFFDE047) else BorderHairline),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isUnlocked) "🎁" else "📦",
                    fontSize = 27.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isUnlocked) Color(0xFFFEF3C7) else Color(0xFFF1F5F9),
            border = BorderStroke(0.6.dp, if (isUnlocked) Color(0xFFFCD34D) else BorderHairline)
        ) {
            Text(
                text = if (isUnlocked) "+50 SPARKS" else "Mốc thưởng $chestIndex",
                fontFamily = PlusJakartaSans,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isUnlocked) Color(0xFFB45309) else TextMuted,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

/**
 * Cổng thử thách vượt cấp HSK (Boss Checkpoint Shrine) ở cuối hành trình
 */
@Composable
private fun ConstellationBossGate(
    levelName: String,
    totalCount: Int,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.bounceClick(scaleDown = 0.95f, onClick = onClick)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isUnlocked) Color(0xFFFEF3C7) else Color(0xFFF1F5F9),
            border = BorderStroke(0.8.dp, if (isUnlocked) Color(0xFFFCD34D) else BorderHairline),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Text(
                text = "CỔNG VƯỢT CẤP $levelName",
                fontFamily = PlusJakartaSans,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isUnlocked) Color(0xFFB45309) else TextMuted,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
            )
        }

        // Đài Cúp Vàng 3D Tactile
        val bossSize = 74.dp
        val bossDepth = 8.dp

        Box(
            modifier = Modifier
                .size(width = bossSize, height = bossSize + bossDepth),
            contentAlignment = Alignment.TopCenter
        ) {
            // Lớp đáy 3D
            val bottomColor = if (isUnlocked) Color(0xFFB45309) else Color(0xFFCBD5E1)
            Box(
                modifier = Modifier
                    .offset(y = bossDepth)
                    .size(bossSize)
                    .clip(CircleShape)
                    .background(bottomColor)
            )

            // Lớp mặt đài cúp
            Box(
                modifier = Modifier
                    .size(bossSize)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) {
                            Brush.linearGradient(
                                listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFF59E0B))
                            )
                        } else {
                            Brush.linearGradient(
                                listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0))
                            )
                        }
                    )
                    .border(
                        BorderStroke(2.5.dp, if (isUnlocked) Color(0xFFFDE047) else BorderHairline),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isUnlocked) "🏆" else "🔒",
                    fontSize = 32.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Thử Thách Vượt Cấp $levelName",
            fontFamily = PlusJakartaSans,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextMain,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(5.dp))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (isUnlocked) BrandPurple else Color.White,
            border = BorderStroke(1.dp, if (isUnlocked) BrandPurple else BorderHairline),
            shadowElevation = if (isUnlocked) 4.dp else 1.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                if (isUnlocked) {
                    Text(
                        text = "Vào thi ngay",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "→",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Hoàn thành đủ $totalCount bài để mở khóa",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

/**
 * Con đường uốn lượn BÉZIER S-CURVE DÀY DẶN (6.5dp)
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
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                SuccessGreen.copy(alpha = 0.25f),
                                BrandCyan.copy(alpha = 0.25f)
                            )
                        ),
                        style = Stroke(
                            width = 13.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(SuccessGreen, BrandCyan)
                        ),
                        style = Stroke(
                            width = 6.5.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                ConnectorLineType.ACTIVE_FLOW -> {
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                SuccessGreen.copy(alpha = 0.25f),
                                BrandPurple.copy(alpha = 0.30f),
                                BrandCyan.copy(alpha = 0.15f)
                            )
                        ),
                        style = Stroke(
                            width = 13.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(SuccessGreen, BrandPurple, BrandCyan)
                        ),
                        style = Stroke(
                            width = 6.5.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                ConnectorLineType.LOCKED -> {
                    drawPath(
                        path = path,
                        color = Color(0xFFE2E8F0),
                        style = Stroke(
                            width = 5.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }
        }
    }
}

/**
 * Thanh Tiến Độ Tinh Gọn (Slim Progress Capsule Bar):
 * - Chiều cao chỉ ~36dp, thu gọn trên 1 hàng ngang duy nhất.
 * - Tiết kiệm hơn 60% diện tích màn hình so với card cồng kềnh cũ.
 */
@Composable
private fun StageRealmHeaderCard(
    levelName: String,
    completedCount: Int,
    totalCount: Int
) {
    val progressRatio = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    val progressPercent = (progressRatio * 100).toInt()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(0.6.dp, BorderHairline),
        shadowElevation = 1.5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Label mục tiêu ngắn gọn
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = "🧭", fontSize = 12.sp)
                Text(
                    text = "Mục tiêu $levelName",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextMain
                )
            }

            // Thanh tiến độ mảnh mai ở giữa
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFF1F5F9))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progressRatio.coerceIn(0.02f, 1f))
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(BrandCyan, BrandPurple)
                            )
                        )
                )
            }

            // Số bài và tỷ lệ % bên phải
            Text(
                text = "$completedCount/$totalCount bài ($progressPercent%)",
                fontFamily = PlusJakartaSans,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (progressPercent == 100) Color(0xFF16A34A) else BrandPurple
            )
        }
    }
}
