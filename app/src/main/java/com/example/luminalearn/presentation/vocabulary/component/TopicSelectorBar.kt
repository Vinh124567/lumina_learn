package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.luminalearn.presentation.vocabulary.model.TopicItem
import com.example.luminalearn.presentation.vocabulary.model.VocabColors

@Composable
fun TopicSelectorBar(
    topicsList: List<TopicItem>,
    selectedTopic: String,
    onSelectTopic: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TopicHeader(topicsList.size)

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFEEF2F6)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 8.dp),
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

@Composable
private fun TopicHeader(count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_nav_explore),
            contentDescription = null,
            tint = VocabColors.BrandPrimary,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "CHỌN TỪ VỰNG THEO CHỦ ĐỀ ($count CHỦ ĐỀ)",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF334155),
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
private fun TopicChip(
    topic: TopicItem,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val chipShape = RoundedCornerShape(14.dp)
    val bgColor = if (isSelected) Color(0xFF181829) else Color(0xFFF8FAFC)
    val textColor = if (isSelected) Color.White else Color(0xFF334155)
    val countBgColor = if (isSelected) Color(0xFF374151) else Color(0xFFEEF2FF)
    val countTextColor = if (isSelected) Color(0xFFF1F5F9) else Color(0xFF6366F1)
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

    val borderModifier = if (!isSelected) {
        Modifier.border(1.dp, Color(0xFFE2E8F0), chipShape)
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .clip(chipShape)
            .background(bgColor)
            .then(borderModifier)
            .clickable(onClick = onSelect)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = topic.icon, fontSize = 14.sp)
            Text(
                text = topic.name,
                fontSize = 13.sp,
                fontWeight = fontWeight,
                color = textColor
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(countBgColor)
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(
                    text = topic.count.toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = countTextColor
                )
            }
        }
    }
}
