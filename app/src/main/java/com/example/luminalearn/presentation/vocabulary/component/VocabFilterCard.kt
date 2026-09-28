package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.vocabulary.model.HskLevelFilter
import com.example.luminalearn.presentation.vocabulary.model.TopicItem
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabSourceFilter
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Card Bộ lọc đa tầng chuyên nghiệp cho màn hình Từ vựng:
 * - Tầng 1: NGUỒN TỪ (Tất cả kho từ, Từ tôi đã thêm, Đã thuộc)
 * - Tầng 2: CẤP ĐỘ HSK (Tất cả cấp độ, HSK 1, HSK 2...)
 * - Tầng 3: CHỦ ĐỀ TỪ VỰNG (Phân loại: Động từ, Danh từ...)
 */
@Composable
fun VocabFilterCard(
    selectedSource: VocabSourceFilter,
    onSelectSource: (VocabSourceFilter) -> Unit,
    allCount: Int,
    customCount: Int,
    masteredCount: Int,
    hskLevels: List<HskLevelFilter>,
    selectedHskIndex: Int,
    onSelectHskLevel: (Int, HskLevelFilter) -> Unit,
    topicsList: List<TopicItem>,
    selectedTopic: String,
    onSelectTopic: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            // ── TẦNG 1: NGUỒN TỪ (Tất cả kho từ, Từ tôi đã thêm, Đã thuộc) ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nhãn bên trái: [ic_book] Nguồn từ:
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_book),
                        contentDescription = null,
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Nguồn từ:",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B)
                    )
                }

                // Danh sách chip nguồn từ cuộn ngang
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Chip 1: Tất cả kho từ
                    SourceChip(
                        title = "Tất cả kho từ",
                        count = allCount,
                        isSelected = selectedSource == VocabSourceFilter.ALL,
                        onClick = { onSelectSource(VocabSourceFilter.ALL) }
                    )

                    // Chip 2: Từ tôi đã thêm (kèm icon bút ✍️)
                    SourceChip(
                        title = "Từ tôi đã thêm",
                        count = customCount,
                        icon = "✍️",
                        isSelected = selectedSource == VocabSourceFilter.CUSTOM,
                        onClick = { onSelectSource(VocabSourceFilter.CUSTOM) }
                    )

                    // Chip 3: Đã thuộc (kèm icon check circle)
                    SourceChip(
                        title = "Đã thuộc",
                        count = masteredCount,
                        drawableIconId = R.drawable.ic_check_circle,
                        isSelected = selectedSource == VocabSourceFilter.MASTERED,
                        onClick = { onSelectSource(VocabSourceFilter.MASTERED) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                color = Color(0xFFF1F5F9),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 14.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // ── TẦNG 2: CẤP ĐỘ HSK ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nhãn bên trái: [ic_filter] Cấp độ:
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = null,
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Cấp độ:",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B)
                    )
                }

                // Danh sách cấp độ HSK cuộn ngang
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    hskLevels.forEachIndexed { index, level ->
                        HskChip(
                            level = level,
                            isSelected = index == selectedHskIndex,
                            onClick = { onSelectHskLevel(index, level) }
                        )
                    }
                }
            }

            // ── TẦNG 3: PHÂN LOẠI TỪ VỰNG (Nếu có topics) ──
            if (topicsList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(
                    color = Color(0xFFF1F5F9),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 14.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_nav_explore),
                            contentDescription = null,
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Chủ đề:",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        topicsList.forEach { topic ->
                            TopicChip(
                                topic = topic,
                                isSelected = topic.name == selectedTopic,
                                onSelect = { onSelectTopic(topic.name) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SourceChip(
    title: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: String? = null,
    @androidx.annotation.DrawableRes drawableIconId: Int? = null
) {
    val chipShape = RoundedCornerShape(14.dp)
    val backgroundModifier = if (isSelected) {
        Modifier.background(Color(0xFF5538EE), chipShape)
    } else {
        Modifier
            .background(Color(0xFFF8FAFC), chipShape)
            .border(1.dp, Color(0xFFE2E8F0), chipShape)
    }

    val textColor = if (isSelected) Color.White else Color(0xFF334155)
    val countBgColor = if (isSelected) Color.White.copy(alpha = 0.22f) else Color(0xFFEDE9FE)
    val countTextColor = if (isSelected) Color.White else Color(0xFF5538EE)

    Box(
        modifier = Modifier
            .clip(chipShape)
            .then(backgroundModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                Text(text = icon, fontSize = 12.sp)
            } else if (drawableIconId != null) {
                Icon(
                    painter = painterResource(id = drawableIconId),
                    contentDescription = null,
                    tint = if (isSelected) Color.White else Color(0xFF64748B),
                    modifier = Modifier.size(13.dp)
                )
            }

            Text(
                text = title,
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(countBgColor)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = count.toString(),
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = countTextColor
                )
            }
        }
    }
}

@Composable
private fun HskChip(
    level: HskLevelFilter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val chipShape = RoundedCornerShape(14.dp)
    val isAllLevels = level.title.contains("Tất cả")

    val backgroundModifier = if (isSelected) {
        val selectedColor = if (isAllLevels) Color(0xFF0F172A) else Color(0xFF5538EE)
        Modifier.background(selectedColor, chipShape)
    } else {
        Modifier
            .background(Color(0xFFF8FAFC), chipShape)
            .border(1.dp, Color(0xFFE2E8F0), chipShape)
    }

    Box(
        modifier = Modifier
            .clip(chipShape)
            .then(backgroundModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = level.title,
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isSelected) Color.White else Color(0xFF334155)
            )

            level.scoreRange?.let { score ->
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .background(
                            if (isSelected) Color.White.copy(alpha = 0.22f) else Color(0xFFEEF2FF),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = score,
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) Color.White else Color(0xFF6366F1)
                    )
                }
            }
        }
    }
}

@Composable
private fun TopicChip(
    topic: TopicItem,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val chipShape = RoundedCornerShape(14.dp)
    val backgroundModifier = if (isSelected) {
        Modifier.background(VocabColors.BrandPrimary, chipShape)
    } else {
        Modifier
            .background(Color(0xFFF8FAFC), chipShape)
            .border(1.dp, Color(0xFFE2E8F0), chipShape)
    }
    val textColor = if (isSelected) Color.White else VocabColors.TextSecondary
    val countBgColor = if (isSelected) Color.White.copy(alpha = 0.22f) else Color(0xFFEEF2FF)
    val countTextColor = if (isSelected) Color.White else VocabColors.BrandPrimary
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold

    Box(
        modifier = Modifier
            .clip(chipShape)
            .then(backgroundModifier)
            .clickable(onClick = onSelect)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = topic.icon, fontSize = 13.sp)
            Text(
                text = topic.name,
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                fontWeight = fontWeight,
                color = textColor
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(countBgColor)
                    .padding(horizontal = 5.5.dp, vertical = 1.5.dp)
            ) {
                Text(
                    text = topic.count.toString(),
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = countTextColor
                )
            }
        }
    }
}
