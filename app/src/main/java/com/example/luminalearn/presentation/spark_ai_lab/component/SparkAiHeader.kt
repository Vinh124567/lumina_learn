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

import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelColors

private const val BADGE_LABEL = "✨ LUMINA SPARK AI LAB • V4.0"
private const val TITLE_LABEL = "Lumina Spark AI Lab"
private const val DESC_LABEL =
    "Luyện phản xạ giao tiếp và đối thoại tình huống thực tế cùng Lumina AI."

@Composable
fun SparkAiHeader(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        PixelBadge(
            text = BADGE_LABEL,
            backgroundColor = Color(0xFFE0F2FE),
            textColor = Color(0xFF0369A1)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = TITLE_LABEL,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = PixelColors.BorderDark,
            letterSpacing = 0.3.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = DESC_LABEL,
            fontSize = 12.sp,
            color = Color(0xFF64748B),
            lineHeight = 16.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
