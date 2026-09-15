package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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

import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabConstants
import com.example.luminalearn.presentation.vocabulary.model.VocabShapes
import com.example.luminalearn.presentation.vocabulary.model.VocabStudyMode

data class VocabProgressData(
    val progressPercent: Int,
    val masteredCount: Int,
    val totalCount: Int,
    val selectedTopic: String,
    val currentHskTitle: String,
    val filteredCount: Int,
    val selectedMode: VocabStudyMode
)

@Composable
fun VocabProgressAndModeBar(
    data: VocabProgressData,
    onSelectMode: (VocabStudyMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val (progressPercent, masteredCount, totalCount, selectedTopic, currentHskTitle, filteredCount, selectedMode) = data
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, VocabShapes.Card)
            .border(1.dp, VocabColors.BorderLight, VocabShapes.Card)
            .padding(14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(42.dp)
                        .background(VocabColors.BrandLight, VocabShapes.Hanzi)
                ) {
                    Text(
                        text = "$progressPercent%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VocabColors.BrandPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tiến độ: $masteredCount / $totalCount từ đã thuộc",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = VocabColors.TextDark
                    )
                    Text(
                        text = "${if (selectedTopic == VocabConstants.ALL_TOPICS) "Toàn bộ chủ đề" else selectedTopic} • $currentHskTitle",
                        fontSize = 12.sp,
                        color = VocabColors.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                    .padding(3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ModeTabItem(
                        title = "${VocabStudyMode.LIST.title} ($filteredCount)",
                        iconRes = R.drawable.ic_layers,
                        isSelected = selectedMode == VocabStudyMode.LIST,
                        onClick = { onSelectMode(VocabStudyMode.LIST) },
                        modifier = Modifier.weight(1.15f)
                    )
                    ModeTabItem(
                        title = VocabStudyMode.FLASHCARD.title,
                        iconRes = R.drawable.ic_flashcard,
                        isSelected = selectedMode == VocabStudyMode.FLASHCARD,
                        onClick = { onSelectMode(VocabStudyMode.FLASHCARD) },
                        modifier = Modifier.weight(0.95f)
                    )
                    ModeTabItem(
                        title = VocabStudyMode.REFLEX.title,
                        iconRes = R.drawable.ic_reflect,
                        isSelected = selectedMode == VocabStudyMode.REFLEX,
                        onClick = { onSelectMode(VocabStudyMode.REFLEX) },
                        modifier = Modifier.weight(1.05f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeTabItem(
    title: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(VocabShapes.ModeTab)
            .background(if (isSelected) VocabColors.BrandPrimary else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = if (isSelected) Color.White else Color(0xFF64748B),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else VocabColors.TextSecondary,
                maxLines = 1
            )
        }
    }
}
