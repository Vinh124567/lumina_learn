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
import com.example.luminalearn.presentation.vocabulary.model.VocabStudyMode

@Composable
fun VocabStudyModeTabs(
    selectedMode: VocabStudyMode,
    filteredCount: Int,
    onSelectMode: (VocabStudyMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .padding(4.dp)
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

@Composable
fun VocabProgressBar(
    masteredCount: Int,
    totalCount: Int,
    progressPercent: Int,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        shadowElevation = 1.5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tiến độ ghi nhớ",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEDE9FE))
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$masteredCount / $totalCount từ ($progressPercent%)",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = VocabColors.BrandPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFF1F5F9))
            ) {
                val progressFraction = (progressPercent.coerceIn(0, 100) / 100f)
                if (progressFraction > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(VocabColors.BrandPrimary)
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
    val tabShape = RoundedCornerShape(12.dp)
    val backgroundModifier = if (isSelected) {
        Modifier.background(Color.White, tabShape)
    } else {
        Modifier.background(Color.Transparent)
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(tabShape)
            .then(backgroundModifier)
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
                tint = if (isSelected) VocabColors.BrandPrimary else Color(0xFF64748B),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = title,
                fontSize = 11.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) VocabColors.BrandPrimary else Color(0xFF64748B),
                maxLines = 1
            )
        }
    }
}
