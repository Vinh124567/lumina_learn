package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R

enum class SparkAiMode(val title: String, val shortTitle: String) {
    ROLEPLAY("Hội Thoại", "Hội Thoại"),
    MICRO_PROMPT("Trợ Lý AI", "Trợ Lý AI"),
    REFLEX_QUIZ("Trắc Nghiệm", "Trắc Nghiệm")
}

private val ActiveGradient = Brush.horizontalGradient(
    listOf(Color(0xFF4F46E5), Color(0xFF5C50F6))
)
private val TextDark = Color(0xFF334155)
private val TextMuted = Color(0xFF64748B)

@Composable
fun SparkAiModeTabs(
    selectedMode: SparkAiMode,
    onSelectMode: (SparkAiMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SparkAiMode.entries.forEach { mode ->
                val isSelected = mode == selectedMode
                SegmentedPillItem(
                    mode = mode,
                    isSelected = isSelected,
                    onClick = { onSelectMode(mode) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SegmentedPillItem(
    mode: SparkAiMode,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconRes = when (mode) {
        SparkAiMode.ROLEPLAY -> R.drawable.ic_ai_chat
        SparkAiMode.MICRO_PROMPT -> R.drawable.ic_bolt
        SparkAiMode.REFLEX_QUIZ -> R.drawable.ic_reflect
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .then(
                if (isSelected) Modifier.background(ActiveGradient)
                else Modifier.background(Color.Transparent)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = if (isSelected) Color.White else TextMuted,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = mode.shortTitle,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else TextDark,
                maxLines = 1
            )
            if (mode == SparkAiMode.ROLEPLAY && !isSelected) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEA580C))
                )
            }
        }
    }
}
