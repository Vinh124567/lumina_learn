package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.presentation.lesson.HskLevelCardData

@Composable
fun HskLevelFilterBar(
    hskLevels: List<HskLevelCardData>,
    selectedHskId: String,
    onSelectHskLevel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cấp độ HSK & Bài học",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B),
                letterSpacing = 0.3.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

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
    val cardShape = RoundedCornerShape(16.dp)
    val bgColor = if (isSelected) Color(0xFF5C50F6) else Color.White
    val titleColor = if (isSelected) Color.White else Color(0xFF0F172A)
    val subtitleColor = if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0xFF64748B)

    androidx.compose.material3.Card(
        modifier = Modifier
            .width(104.dp)
            .height(72.dp)
            .clickable(onClick = onClick),
        shape = cardShape,
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = bgColor),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = level.title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${level.lessonCount} bài học",
                    fontSize = 11.sp,
                    color = subtitleColor
                )
            }
        }
    }
}

@Composable
private fun HskSpecificLevelCard(
    level: HskLevelCardData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)
    val borderStroke = if (isSelected) {
        androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF5C50F6))
    } else null

    androidx.compose.material3.Card(
        modifier = Modifier
            .width(116.dp)
            .height(72.dp)
            .clickable(onClick = onClick),
        shape = cardShape,
        border = borderStroke,
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp)
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
                            .background(if (isSelected) Color(0xFFEDE9FE) else level.tagBgColor)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = level.title,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF5C50F6) else level.tagTextColor
                        )
                    }

                    Text(
                        text = "${level.lessonCount} bài",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B)
                    )
                }

                Text(
                    text = level.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    maxLines = 1
                )
            }
        }
    }
}
