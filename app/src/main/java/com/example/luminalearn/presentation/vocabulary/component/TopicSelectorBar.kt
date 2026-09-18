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

        Row(
            modifier = Modifier
                .fillMaxWidth()
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
            text = "CHỦ ĐỀ TỪ VỰNG ($count)",
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
    val chipShape = RoundedCornerShape(12.dp)
    val backgroundModifier = if (isSelected) {
        Modifier.background(VocabColors.BrandPrimary, chipShape)
    } else {
        Modifier
            .background(Color.White, chipShape)
            .border(1.dp, VocabColors.BorderLight, chipShape)
    }
    val textColor = if (isSelected) Color.White else VocabColors.TextSecondary
    val countBgColor = if (isSelected) Color.White.copy(alpha = 0.22f) else VocabColors.BrandLight
    val countTextColor = if (isSelected) Color.White else VocabColors.BrandPrimary
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

    Box(
        modifier = Modifier
            .clip(chipShape)
            .then(backgroundModifier)
            .clickable(onClick = onSelect)
            .padding(horizontal = 14.dp, vertical = 8.dp)
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
                    .clip(RoundedCornerShape(10.dp))
                    .background(countBgColor)
                    .padding(horizontal = 6.dp, vertical = 1.5.dp)
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

