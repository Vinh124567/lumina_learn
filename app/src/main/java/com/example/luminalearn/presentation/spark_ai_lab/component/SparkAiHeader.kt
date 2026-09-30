package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val BADGE_LABEL = "✨ LUMINA SPARK AI LAB • V4.0 •"
private const val TITLE_LABEL = "Lumina Spark AI Lab"
private const val DESC_LABEL =
    "Luyện phản xạ giao tiếp và đối thoại tình huống thực tế cùng Lumina AI."

private val PrimaryPurple = Color(0xFF5C50F6)
private val DarkTitleColor = Color(0xFF0F172A)
private val SubtitleColor = Color(0xFF64748B)

@Composable
fun SparkAiHeader(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFEEF2FF))
                .border(
                    BorderStroke(1.dp, Color(0xFFE0E7FF)),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 9.dp, vertical = 4.dp)
        ) {
            Text(
                text = BADGE_LABEL,
                color = PrimaryPurple,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = TITLE_LABEL,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DarkTitleColor,
            letterSpacing = (-0.5).sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = DESC_LABEL,
            fontSize = 13.sp,
            color = SubtitleColor,
            lineHeight = 18.sp
        )
    }
}
