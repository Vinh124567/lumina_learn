package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelSegmentedProgressBar
import com.example.luminalearn.ui.theme.PlusJakartaSans

@Composable
internal fun CategoryAndTopicRow(
    category: String,
    subTitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PixelBadge(
            text = category.ifBlank { "PHÁT ÂM PINYIN" }.uppercase(),
            color = PixelColors.ArcadePurple,
            textColor = Color.White
        )

        if (subTitle.isNotBlank()) {
            val cleanSubTitle = if (subTitle.contains("(")) subTitle.substringBefore("(").trim() else subTitle
            Text(
                text = cleanSubTitle,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = PixelColors.TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun LessonProgressBar(
    currentIndex: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    PixelSegmentedProgressBar(
        current = currentIndex,
        total = totalCount,
        activeColor = PixelColors.ArcadePurple,
        height = 8.dp,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
internal fun TitleAndHeaderInfo(
    title: String,
    currentIndex: Int,
    totalCount: Int,
    cardType: String
) {
    val formattedTitle = if (title.contains(" & ")) {
        title.replace(" & ", "\n& ")
    } else {
        title
    }

    Text(
        text = formattedTitle,
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Black,
        fontSize = 18.sp,
        color = PixelColors.TextDark,
        lineHeight = 24.sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PixelBadge(
            text = "THẺ $currentIndex / $totalCount",
            color = PixelColors.ArcadeAmber,
            textColor = PixelColors.BorderDark
        )

        if (cardType.isNotBlank()) {
            PixelBadge(
                text = cardType.uppercase(),
                color = PixelColors.SurfaceDark,
                textColor = Color.White
            )
        }
    }
}

@Composable
internal fun DialogBottomNavigation(
    isFirst: Boolean,
    isLast: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onComplete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút thẻ trước
        if (!isFirst) {
            PixelArcadeButton(
                text = "◀ THẺ TRƯỚC",
                onClick = onPrev,
                color = PixelColors.SurfaceLight,
                textColor = PixelColors.TextDark,
                fillMaxWidth = false,
                height = 46.dp,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Disabled placeholder
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFE2E8F0))
                    .border(1.2.dp, Color(0xFFCBD5E1), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "◀ THẺ TRƯỚC",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Nút tiếp theo hoặc hoàn thành
        PixelArcadeButton(
            text = if (isLast) "HOÀN THÀNH BÀI HỌC 🏆" else "THẺ TIẾP THEO ▶",
            onClick = if (isLast) onComplete else onNext,
            color = if (isLast) PixelColors.ArcadeGreen else PixelColors.ArcadePurple,
            textColor = Color.White,
            fillMaxWidth = false,
            height = 46.dp,
            modifier = Modifier.weight(1.3f)
        )
    }
}
