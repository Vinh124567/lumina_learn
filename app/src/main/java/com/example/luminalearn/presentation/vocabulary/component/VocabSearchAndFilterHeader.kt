package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.model.HskLevelFilter
import com.example.luminalearn.presentation.vocabulary.model.VocabSourceFilter
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Thanh tìm kiếm & Chip lọc HSK 1 dòng tinh gọn, sang trọng.
 * Thay thế hoàn toàn bộ lọc 3 tầng cồng kềnh cũ.
 */
@Composable
fun VocabSearchAndFilterHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddVocabClick: () -> Unit,
    selectedSource: VocabSourceFilter,
    onSelectSource: (VocabSourceFilter) -> Unit,
    counts: VocabFilterCounts,
    hskLevels: List<HskLevelFilter>,
    selectedHskIndex: Int,
    onSelectHskLevel: (Int, HskLevelFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ── HÀNG 1: Ô TÌM KIẾM + NÚT THÊM TỪ ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Khung tìm kiếm màu trắng
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Tìm chữ Hán, Pinyin, nghĩa...",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            textStyle = TextStyle(
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF0F172A)
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(Color(0xFF6366F1)),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onSearchQueryChange("") }
                        )
                    }
                }
            }

            // Nút [+ Thêm từ]
            Box(
                modifier = Modifier
                    .height(44.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF6366F1), Color(0xFF4F46E5))
                        )
                    )
                    .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
                    .bounceClick(scaleDown = 0.95f, onClick = onAddVocabClick)
                    .padding(horizontal = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Thêm từ",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
        }

        // ── HÀNG 2: 1 DÒNG CHIP CUỘN NGANG TINH TẾ (HSK & BỘ LỌC) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Chip 1: Tất cả kho từ
            QuickFilterChip(
                label = "Tất cả",
                count = counts.allCount,
                isSelected = selectedSource == VocabSourceFilter.ALL && selectedHskIndex == 0,
                onClick = {
                    onSelectSource(VocabSourceFilter.ALL)
                    if (hskLevels.isNotEmpty()) {
                        onSelectHskLevel(0, hskLevels[0])
                    }
                }
            )

            // Các Chip HSK 1 -> HSK 6
            hskLevels.drop(1).forEachIndexed { dropIndex, hskLevel ->
                val actualIndex = dropIndex + 1
                val isSelected = selectedHskIndex == actualIndex && selectedSource == VocabSourceFilter.ALL
                QuickFilterChip(
                    label = hskLevel.title,
                    scoreRange = hskLevel.scoreRange,
                    isSelected = isSelected,
                    onClick = {
                        onSelectSource(VocabSourceFilter.ALL)
                        onSelectHskLevel(actualIndex, hskLevel)
                    }
                )
            }

            // Chip Cần ôn hôm nay (SRS)
            QuickFilterChip(
                label = "Cần ôn",
                count = counts.dueTodayCount,
                iconRes = R.drawable.ic_clock,
                isSelected = selectedSource == VocabSourceFilter.DUE_TODAY,
                onClick = { onSelectSource(VocabSourceFilter.DUE_TODAY) }
            )

            // Chip Đã thuộc
            QuickFilterChip(
                label = "Đã thuộc",
                count = counts.masteredCount,
                iconRes = R.drawable.ic_check_circle,
                isSelected = selectedSource == VocabSourceFilter.MASTERED,
                onClick = { onSelectSource(VocabSourceFilter.MASTERED) }
            )

            // Chip Từ tự thêm
            QuickFilterChip(
                label = "Tự thêm",
                count = counts.customCount,
                isSelected = selectedSource == VocabSourceFilter.CUSTOM,
                onClick = { onSelectSource(VocabSourceFilter.CUSTOM) }
            )
        }
    }
}

@Composable
private fun QuickFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    count: Int? = null,
    scoreRange: String? = null,
    iconRes: Int? = null
) {
    val chipShape = RoundedCornerShape(14.dp)

    val backgroundModifier = if (isSelected) {
        Modifier
            .background(
                Brush.horizontalGradient(listOf(Color(0xFF6366F1), Color(0xFF4F46E5))),
                chipShape
            )
            .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.6f)), chipShape)
    } else {
        Modifier
            .background(Color.White, chipShape)
            .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), chipShape)
    }

    val textColor = if (isSelected) Color.White else Color(0xFF475569)
    val countBgColor = if (isSelected) Color.White.copy(alpha = 0.22f) else Color(0xFFEEF2F6)
    val countTextColor = if (isSelected) Color.White else Color(0xFF6366F1)

    Box(
        modifier = Modifier
            .clip(chipShape)
            .then(backgroundModifier)
            .bounceClick(scaleDown = 0.95f, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = if (isSelected) Color.White else Color(0xFF64748B),
                    modifier = Modifier.size(13.dp)
                )
            }

            Text(
                text = label,
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = textColor
            )

            if (count != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(countBgColor)
                        .padding(horizontal = 5.dp, vertical = 1.5.dp)
                ) {
                    Text(
                        text = count.toString(),
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = countTextColor
                    )
                }
            } else if (scoreRange != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color.White.copy(alpha = 0.22f) else Color(0xFFE0F2FE))
                        .padding(horizontal = 5.dp, vertical = 1.5.dp)
                ) {
                    Text(
                        text = scoreRange,
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) Color.White else Color(0xFF0284C7)
                    )
                }
            }
        }
    }
}
