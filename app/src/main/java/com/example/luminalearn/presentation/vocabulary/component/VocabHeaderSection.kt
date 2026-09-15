package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.luminalearn.presentation.vocabulary.model.HskLevelFilter
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabShapes

@Composable
fun VocabTopBar(
    onAskAiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .background(VocabColors.BrandLight, VocabShapes.Tab)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_nav_vocabulary),
                    contentDescription = null,
                    tint = VocabColors.BrandPrimary,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "KHO TỪ VỰNG THEO THANG ĐIỂM",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = VocabColors.BrandPrimary,
                    letterSpacing = 0.4.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(VocabColors.BrandPrimary)
                .clickable(onClick = onAskAiClick)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "+ AI thêm từ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun VocabHeaderSection(
    hskLevels: List<HskLevelFilter>,
    selectedHskIndex: Int,
    onSelectHskLevel: (Int, HskLevelFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Từ vựng trọng tâm bám sát mục tiêu thi",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = VocabColors.TextDark,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Học kèm âm Hán Việt độc quyền, phát âm bản xứ và chế độ Flashcard tương tác.",
            fontSize = 13.sp,
            color = VocabColors.TextMuted,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        HskLevelSelector(
            hskLevels = hskLevels,
            selectedHskIndex = selectedHskIndex,
            onSelectHskLevel = onSelectHskLevel
        )
    }
}

@Composable
private fun HskLevelSelector(
    hskLevels: List<HskLevelFilter>,
    selectedHskIndex: Int,
    onSelectHskLevel: (Int, HskLevelFilter) -> Unit
) {
    Surface(
        shape = VocabShapes.Card,
        color = Color.White,
        border = BorderStroke(1.dp, VocabColors.BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 6.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            hskLevels.forEachIndexed { index, level ->
                HskLevelTabItem(
                    level = level,
                    isSelected = index == selectedHskIndex,
                    onClick = { onSelectHskLevel(index, level) }
                )
            }
        }
    }
}

@Composable
private fun HskLevelTabItem(
    level: HskLevelFilter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(VocabShapes.Tab)
            .background(if (isSelected) VocabColors.BrandPrimary else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = level.title,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else VocabColors.TextSecondary
            )

            level.scoreRange?.let { score ->
                HskScoreTag(score = score, isSelected = isSelected)
            }
        }
    }
}

@Composable
private fun HskScoreTag(
    score: String,
    isSelected: Boolean
) {
    Spacer(modifier = Modifier.width(6.dp))
    Box(
        modifier = Modifier
            .background(
                if (isSelected) Color.White.copy(alpha = 0.22f) else VocabColors.BrandLight,
                VocabShapes.Tag
            )
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(
            text = score,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) Color.White else VocabColors.BrandPrimary
        )
    }
}

