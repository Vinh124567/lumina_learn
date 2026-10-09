package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.model.HskLevelFilter
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlin.math.abs

data class HskLevelCardData(
    val levelNumber: Int,
    val title: String,
    val stageName: String,
    val targetScore: String,
    val standardWordCount: Int,
    val gradientColors: List<Color>,
    val accentColor: Color,
    val badgeBgColor: Color
)

val HSK_LEVEL_INFOS = listOf(
    HskLevelCardData(
        levelNumber = 1,
        title = "HSK 1",
        stageName = "Nhập Môn Căn Bản",
        targetScore = "Mục tiêu 180–200đ",
        standardWordCount = 150,
        gradientColors = listOf(Color(0xFF0F172A), Color(0xFF064E3B), Color(0xFF065F46), Color(0xFF0F172A)),
        accentColor = Color(0xFF34D399),
        badgeBgColor = Color(0xFF059669).copy(alpha = 0.25f)
    ),
    HskLevelCardData(
        levelNumber = 2,
        title = "HSK 2",
        stageName = "Sơ Cấp Giao Tiếp",
        targetScore = "Mục tiêu 180–200đ",
        standardWordCount = 300,
        gradientColors = listOf(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF2E2C80), Color(0xFF0F172A)),
        accentColor = Color(0xFF818CF8),
        badgeBgColor = Color(0xFF6366F1).copy(alpha = 0.25f)
    ),
    HskLevelCardData(
        levelNumber = 3,
        title = "HSK 3",
        stageName = "Giao Tiếp Nâng Cao",
        targetScore = "Mục tiêu 210–250đ",
        standardWordCount = 600,
        gradientColors = listOf(Color(0xFF0F172A), Color(0xFF1E1C59), Color(0xFF1D4ED8), Color(0xFF0F172A)),
        accentColor = Color(0xFF60A5FA),
        badgeBgColor = Color(0xFF2563EB).copy(alpha = 0.25f)
    ),
    HskLevelCardData(
        levelNumber = 4,
        title = "HSK 4",
        stageName = "Trung Cấp Học Thuật",
        targetScore = "Mục tiêu 210–250đ",
        standardWordCount = 1200,
        gradientColors = listOf(Color(0xFF0F172A), Color(0xFF2E1065), Color(0xFF581C87), Color(0xFF0F172A)),
        accentColor = Color(0xFFC084FC),
        badgeBgColor = Color(0xFF7E22CE).copy(alpha = 0.25f)
    ),
    HskLevelCardData(
        levelNumber = 5,
        title = "HSK 5",
        stageName = "Cao Cấp Chuyên Sâu",
        targetScore = "Mục tiêu 210–250đ",
        standardWordCount = 2500,
        gradientColors = listOf(Color(0xFF0F172A), Color(0xFF451A03), Color(0xFF78350F), Color(0xFF0F172A)),
        accentColor = Color(0xFFFBBF24),
        badgeBgColor = Color(0xFFD97706).copy(alpha = 0.25f)
    ),
    HskLevelCardData(
        levelNumber = 6,
        title = "HSK 6",
        stageName = "Tinh Thông Bản Xứ",
        targetScore = "Mục tiêu 210–250đ",
        standardWordCount = 5000,
        gradientColors = listOf(Color(0xFF0F172A), Color(0xFF3F0B10), Color(0xFF7F1D1D), Color(0xFF0F172A)),
        accentColor = Color(0xFFF87171),
        badgeBgColor = Color(0xFFDC2626).copy(alpha = 0.25f)
    )
)

/**
 * Thẻ cấp độ HSK 1–6 dạng 3D Carousel cuộn ngang với hiệu ứng Cover Flow.
 * Đồng bộ với phong cách HorizontalPager 3D của màn Trang chủ.
 */
@Composable
fun HskLevelCoverFlowCarousel(
    hskLevels: List<HskLevelFilter>,
    selectedHskIndex: Int,
    vocabList: List<VocabWordItem>,
    onSelectHskLevel: (Int, HskLevelFilter) -> Unit,
    onOpenHskLevel: (HskLevelCardData) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialPage = (selectedHskIndex - 1).coerceIn(0, HSK_LEVEL_INFOS.size - 1)
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { HSK_LEVEL_INFOS.size }
    )

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 14.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val info = HSK_LEVEL_INFOS[page]
            val isSelected = pagerState.currentPage == page

            // Tính số từ thuộc HSK này trong kho từ hiện tại
            val wordsInLevel = vocabList.filter {
                it.hskLevel.contains("HSK ${info.levelNumber}", ignoreCase = true)
            }
            val masteredCount = wordsInLevel.count { it.isMastered }
            val totalInDataset = wordsInLevel.size.coerceAtLeast(info.standardWordCount)
            val progressPercent = if (totalInDataset > 0) (masteredCount * 100 / totalInDataset) else 0

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        // Tính toán độ lệch trang trực tiếp trong Render Phase (120 FPS không Recomposition)
                        val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                        val absOffset = abs(pageOffset).coerceIn(0f, 1f)

                        // Khoảng cách camera 3D tạo chiều sâu không gian
                        cameraDistance = 18f * density

                        // Hiệu ứng 3D Cover Flow: Xoay quanh trục Y nghiêng góc 20 độ
                        rotationY = -pageOffset.coerceIn(-1f, 1f) * 20f

                        // Điểm tựa xoay hướng về trung tâm
                        transformOrigin = TransformOrigin(
                            pivotFractionX = if (pageOffset < 0f) 0.05f else 0.95f,
                            pivotFractionY = 0.5f
                        )

                        // Thu phóng và độ mờ theo chiều sâu
                        val scale = 1f - (absOffset * 0.08f)
                        scaleX = scale
                        scaleY = scale
                        alpha = 1f - (absOffset * 0.22f)
                    }
            ) {
                HskLevelStageCard(
                    data = info,
                    masteredCount = masteredCount,
                    totalCount = totalInDataset,
                    progressPercent = progressPercent,
                    isSelected = isSelected,
                    onClick = {
                        onOpenHskLevel(info)
                    }
                )
            }
        }

        // Indicator Dots bên dưới
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HSK_LEVEL_INFOS.forEachIndexed { index, _ ->
                val isActive = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .height(4.dp)
                        .width(if (isActive) 18.dp else 5.dp)
                        .clip(CircleShape)
                        .background(if (isActive) Color(0xFF6366F1) else Color(0xFFCBD5E1))
                )
            }
        }
    }
}

@Composable
private fun HskLevelStageCard(
    data: HskLevelCardData,
    masteredCount: Int,
    totalCount: Int,
    progressPercent: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .bounceClick(scaleDown = 0.97f, onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) data.accentColor else data.accentColor.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(data.gradientColors)
                )
        ) {
            CosmicStarfield(
                modifier = Modifier.matchParentSize(),
                particleCount = 25,
                focusCenterXRatio = 0.88f,
                focusCenterYRatio = 0.3f
            )

            // Số HSK in chìm nghệ thuật ở góc phải dưới
            Text(
                text = "${data.levelNumber}",
                fontFamily = PlusJakartaSans,
                fontSize = 110.sp,
                fontWeight = FontWeight.Black,
                color = data.accentColor.copy(alpha = 0.08f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 0.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header: Badge HSK + Target Score
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(data.badgeBgColor)
                            .border(BorderStroke(1.dp, data.accentColor.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                            .padding(horizontal = 9.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = data.title,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = data.accentColor
                        )
                    }

                    Text(
                        text = data.targetScore,
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFCBD5E1)
                    )
                }

                // Middle: Title & Words Count + Mở từ vựng CTA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = data.stageName,
                            fontFamily = PlusJakartaSans,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "Kho từ chuẩn: ${data.standardWordCount} từ vựng",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(data.accentColor.copy(alpha = 0.18f))
                            .border(BorderStroke(1.dp, data.accentColor.copy(alpha = 0.4f)), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mở kho từ",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = data.accentColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_right),
                            contentDescription = null,
                            tint = data.accentColor,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Bottom: Progress bar & Action Button
                Column {
                    val pctString = if (masteredCount <= 0 || totalCount <= 0) {
                        "0%"
                    } else {
                        val pct = (masteredCount.toDouble() / totalCount) * 100.0
                        when {
                            pct >= 100.0 -> "100%"
                            pct >= 10.0 -> "${pct.toInt()}%"
                            pct >= 0.1 -> String.format(java.util.Locale.US, "%.1f%%", pct)
                            else -> "<0.1%"
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Đã thuộc: $masteredCount / $totalCount từ",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFCBD5E1)
                        )
                        Text(
                            text = pctString,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = data.accentColor
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    // Progress track
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        val rawFraction = if (totalCount > 0) (masteredCount.toFloat() / totalCount).coerceIn(0f, 1f) else 0f
                        val progressFraction = if (masteredCount > 0) maxOf(rawFraction, 0.035f) else 0f
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressFraction)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(data.accentColor)
                        )
                    }
                }
            }
        }
    }
}
