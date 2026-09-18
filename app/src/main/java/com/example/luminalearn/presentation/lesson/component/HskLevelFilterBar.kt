package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.lesson.HskLevelCardData

@Composable
fun HskLevelFilterBar(
    hskLevels: List<HskLevelCardData>,
    selectedHskId: String,
    totalLessonCount: Int,
    matchedLessonCount: Int,
    onSelectHskLevel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val subtitleText = if (selectedHskId == "all") {
        "$totalLessonCount bài học"
    } else {
        "$matchedLessonCount bài học phù hợp"
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_layers),
                contentDescription = null,
                tint = Color(0xFF6366F1),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "PHÂN LOẠI THEO CẤP ĐỘ & THANG ĐIỂM",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF475569),
                letterSpacing = 0.2.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            hskLevels.forEach { level ->
                if (level.id == "all") {
                    AllLevelsCard(
                        level = level,
                        isSelected = selectedHskId == "all",
                        onClick = { onSelectHskLevel("all") }
                    )
                } else {
                    HskSpecificLevelCard(
                        level = level,
                        isSelected = selectedHskId == level.id,
                        onClick = { onSelectHskLevel(level.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AllLevelsCard(
    level: HskLevelCardData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(18.dp)
    val bgColor = if (isSelected) Color(0xFF5C50F6) else Color.White
    val titleColor = if (isSelected) Color.White else Color(0xFF1E293B)
    val subtitleColor = if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0xFF64748B)

    val borderModifier = if (!isSelected) {
        Modifier.border(1.dp, Color(0xFFE2E8F0), cardShape)
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .width(108.dp)
            .height(94.dp)
            .clip(cardShape)
            .background(bgColor)
            .then(borderModifier)
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = level.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${level.lessonCount} bài học",
                fontSize = 11.5.sp,
                color = subtitleColor
            )
        }
    }
}

@Composable
private fun HskSpecificLevelCard(
    level: HskLevelCardData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(18.dp)
    val borderModifier = if (isSelected) {
        Modifier.border(2.dp, Color(0xFF0F172A), cardShape)
    } else {
        Modifier.border(1.dp, Color(0xFFE2E8F0), cardShape)
    }

    Box(
        modifier = Modifier
            .width(124.dp)
            .height(94.dp)
            .clip(cardShape)
            .background(Color.White)
            .then(borderModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(level.tagBgColor)
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = level.title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = level.tagTextColor
                    )
                }
                Text(
                    text = "${level.lessonCount} bài",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF94A3B8)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                level.maxScore?.let {
                    Text(
                        text = it,
                        fontSize = 10.5.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 14.sp,
                        maxLines = 1
                    )
                }
                level.passScore?.let {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF0FDF4))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = it,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D),
                            lineHeight = 14.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
