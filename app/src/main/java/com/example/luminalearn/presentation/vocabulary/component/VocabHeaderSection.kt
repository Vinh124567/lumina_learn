package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFEDE9FE))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_nav_vocabulary),
                    contentDescription = null,
                    tint = VocabColors.BrandPrimary,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "THƯ VIỆN TỪ VỰNG",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
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
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun VocabHeaderTitle(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(top = 2.dp)) {
        Text(
            text = "Từ vựng HSK 3.0",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "3.249 từ phân cấp • Phát âm chuẩn & Hán Việt",
            fontSize = 12.5.sp,
            color = Color(0xFF64748B)
        )
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
        VocabHeaderTitle()

        Spacer(modifier = Modifier.height(12.dp))

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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

@Composable
private fun HskLevelTabItem(
    level: HskLevelFilter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val chipShape = RoundedCornerShape(12.dp)
    val backgroundModifier = if (isSelected) {
        Modifier.background(VocabColors.BrandPrimary, chipShape)
    } else {
        Modifier
            .background(Color.White, chipShape)
            .border(1.dp, VocabColors.BorderLight, chipShape)
    }

    Box(
        modifier = Modifier
            .clip(chipShape)
            .then(backgroundModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
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
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = score,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) Color.White else VocabColors.BrandPrimary
        )
    }
}

