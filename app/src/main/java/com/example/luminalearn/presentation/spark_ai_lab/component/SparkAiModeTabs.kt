package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import com.example.luminalearn.core.ui.effect.bounceClick

import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape

enum class SparkAiMode(val title: String, val shortTitle: String) {
    ROLEPLAY("Hội Thoại", "Hội Thoại"),
    MICRO_PROMPT("Trợ Lý AI", "Trợ Lý AI"),
    REFLEX_QUIZ("Trắc Nghiệm", "Trắc Nghiệm")
}

@Composable
fun SparkAiModeTabs(
    selectedMode: SparkAiMode,
    onSelectMode: (SparkAiMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val barShape = PixelStepCornerShape(3.dp)

    Box(modifier = modifier.fillMaxWidth()) {
        // Gờ bóng máy kim loại 3D phía sau
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 2.dp, y = 2.dp)
                .clip(barShape)
                .background(PixelColors.ShadowDark)
                .border(BorderStroke(1.2.dp, PixelColors.BorderDark), barShape)
        )

        // Bề mặt Dock chính
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(barShape)
                .background(Color(0xFFF1F5F9))
                .border(BorderStroke(1.5.dp, PixelColors.BorderDark), barShape)
                .padding(3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SparkAiMode.entries.forEach { mode ->
                    val isSelected = mode == selectedMode
                    PixelModeTabItem(
                        mode = mode,
                        isSelected = isSelected,
                        onClick = { onSelectMode(mode) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PixelModeTabItem(
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

    val tabShape = PixelStepCornerShape(2.dp)

    Box(
        modifier = modifier
            .clip(tabShape)
            .then(
                if (isSelected) {
                    Modifier
                        .background(PixelColors.PixelBlue)
                        .border(BorderStroke(1.2.dp, PixelColors.BorderDark), tabShape)
                } else {
                    Modifier
                        .background(Color.Transparent)
                }
            )
            .bounceClick(scaleDown = 0.95f, onClick = onClick)
            .padding(vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        // Highlight bevel trên nóc nút khi đang chọn
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(top = 1.dp, start = 1.dp, end = 1.dp)
                    .border(BorderStroke(0.8.dp, Color.White.copy(alpha = 0.5f)), tabShape)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = if (isSelected) Color.White else Color(0xFF64748B),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = mode.shortTitle,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                color = if (isSelected) Color.White else Color(0xFF334155),
                letterSpacing = 0.3.sp,
                maxLines = 1
            )
        }
    }
}
