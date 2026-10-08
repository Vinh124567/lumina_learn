package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val BrandIndigo = Color(0xFF6366F1)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)

/**
 * Thẻ Bản Đồ Năng Lực Từ Vựng HSK 1–6 (Mastery Breakdown):
 * - Thống kê tỷ lệ làm chủ từ vựng theo từng cấp độ HSK 1..6.
 * - Thanh tiến độ mini 2 cột gọn gàng, chạm vào cấp độ để mở kho từ tương ứng.
 */
@Composable
fun HskMasteryBreakdownCard(
    vocabList: List<VocabWordItem>,
    masteredCount: Int,
    totalCount: Int,
    progressPercent: Int,
    onOpenHskLevel: (HskLevelCardData) -> Unit,
    modifier: Modifier = Modifier
) {
    // Tính toán số từ đã thuộc cho từng cấp độ HSK
    val hskStats = remember(vocabList) {
        HSK_LEVEL_INFOS.map { levelInfo ->
            val levelWords = vocabList.filter { it.hskLevel.contains("HSK ${levelInfo.levelNumber}", ignoreCase = true) }
            val masteredInLevel = levelWords.count { it.isMastered }
            val totalInLevel = if (levelWords.isNotEmpty()) levelWords.size else levelInfo.standardWordCount
            val pct = if (totalInLevel > 0) (masteredInLevel * 100 / totalInLevel).coerceIn(0, 100) else 0
            Triple(levelInfo, masteredInLevel to totalInLevel, pct)
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ── Header: Tiêu đề & Tổng tiến độ ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Năng Lực Từ Vựng HSK",
                        fontFamily = PlusJakartaSans,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Đã làm chủ $masteredCount/$totalCount từ ($progressPercent%)",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.5.sp,
                        color = TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFEEF2FF))
                        .border(BorderStroke(0.5.dp, Color(0xFFC7D2FE)), RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "🎯 HSK 1–6",
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandIndigo
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Body: Lưới 2 Cột x 3 Hàng tiến độ HSK ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Cột trái: HSK 1, 2, 3
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    hskStats.take(3).forEach { (levelInfo, counts, pct) ->
                        HskLevelProgressRow(
                            levelInfo = levelInfo,
                            mastered = counts.first,
                            total = counts.second,
                            percent = pct,
                            onClick = { onOpenHskLevel(levelInfo) }
                        )
                    }
                }

                // Cột phải: HSK 4, 5, 6
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    hskStats.drop(3).forEach { (levelInfo, counts, pct) ->
                        HskLevelProgressRow(
                            levelInfo = levelInfo,
                            mastered = counts.first,
                            total = counts.second,
                            percent = pct,
                            onClick = { onOpenHskLevel(levelInfo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HskLevelProgressRow(
    levelInfo: HskLevelCardData,
    mastered: Int,
    total: Int,
    percent: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .bounceClick(scaleDown = 0.96f, onClick = onClick)
            .padding(vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(levelInfo.accentColor.copy(alpha = 0.15f))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "HSK ${levelInfo.levelNumber}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = levelInfo.accentColor
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$mastered/$total",
                    fontFamily = PlusJakartaSans,
                    fontSize = 9.sp,
                    color = TextMuted
                )
            }

            Text(
                text = "$percent%",
                fontFamily = PlusJakartaSans,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = levelInfo.accentColor
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Thanh progress bar mini
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFFF1F5F9))
        ) {
            val fraction = (percent / 100f).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(2.dp))
                    .background(levelInfo.accentColor)
            )
        }
    }
}
