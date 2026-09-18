package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabShapes
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem

data class FlashcardActions(
    val onFlip: () -> Unit,
    val onPrevious: () -> Unit,
    val onNext: () -> Unit,
    val onSpeak: (String) -> Unit,
    val onToggleMastered: (String) -> Unit
)

@Composable
fun FlashcardInteractiveSection(
    currentWord: VocabWordItem,
    currentIndex: Int,
    totalCount: Int,
    isFlipped: Boolean,
    actions: FlashcardActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlashcardIndexBar(
            currentIndex = currentIndex,
            totalCount = totalCount,
            targetScore = currentWord.targetScore
        )

        Spacer(modifier = Modifier.height(6.dp))

        FlashcardCardSurface(
            word = currentWord,
            isFlipped = isFlipped,
            onFlip = actions.onFlip,
            onSpeak = actions.onSpeak,
            onToggleMastered = actions.onToggleMastered
        )

        Spacer(modifier = Modifier.height(18.dp))

        FlashcardNavigationButtons(
            onPrevious = actions.onPrevious,
            onNext = actions.onNext
        )
    }
}

@Composable
private fun FlashcardIndexBar(
    currentIndex: Int,
    totalCount: Int,
    targetScore: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = buildAnnotatedString {
                append("Thẻ ")
                withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, color = VocabColors.TextDark)) {
                    append("${currentIndex + 1}")
                }
                append(" / $totalCount")
            },
            fontSize = 13.sp,
            color = VocabColors.TextMuted
        )

        Text(
            text = targetScore,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = VocabColors.BrandDark
        )
    }
}

@Composable
private fun FlashcardCardSurface(
    word: VocabWordItem,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    onSpeak: (String) -> Unit,
    onToggleMastered: (String) -> Unit
) {
    Surface(
        shape = VocabShapes.BigCard,
        color = Color.White,
        border = BorderStroke(1.dp, VocabColors.BorderLight),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clip(VocabShapes.BigCard)
            .clickable(onClick = onFlip)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlashcardHeader(
                hskLevel = word.hskLevel,
                topic = word.topic,
                isFlipped = isFlipped
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (!isFlipped) {
                FlashcardFront(word)
            } else {
                FlashcardBack(word)
            }

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFF1F5F9))
            )

            Spacer(modifier = Modifier.height(14.dp))

            FlashcardActionFooter(
                word = word,
                onSpeak = onSpeak,
                onToggleMastered = onToggleMastered
            )
        }
    }
}

@Composable
private fun FlashcardHeader(
    hskLevel: String,
    topic: String,
    isFlipped: Boolean
) {
    val flipHint = if (isFlipped) "Chạm để lật lại ↺" else "Chạm để lật thẻ ↻"

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFEEF2FF), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$hskLevel • $topic",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VocabColors.BrandDark
            )
        }

        Text(
            text = flipHint,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF94A3B8)
        )
    }
}

@Composable
private fun FlashcardFront(word: VocabWordItem) {
    val hanziFontSize = remember(word.hanzi) {
        when {
            word.hanzi.length <= 1 -> 54.sp
            word.hanzi.length == 2 -> 46.sp
            word.hanzi.length == 3 -> 36.sp
            else -> 30.sp
        }
    }

    Text(
        text = word.hanzi,
        fontSize = hanziFontSize,
        fontWeight = FontWeight.Bold,
        color = VocabColors.TextDark,
        textAlign = TextAlign.Center,
        maxLines = 1,
        softWrap = false
    )

    Spacer(modifier = Modifier.height(14.dp))

    Box(
        modifier = Modifier
            .background(VocabColors.HanVietBg, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 5.dp)
    ) {
        Text(
            text = "Hán Việt: ${word.hanViet}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = VocabColors.HanVietAmber
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = word.radical,
        fontSize = 12.sp,
        color = VocabColors.TextMuted,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun FlashcardBack(word: VocabWordItem) {
    Text(
        text = word.pinyin,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = VocabColors.BrandDark,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = word.meaning,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = VocabColors.TextDark,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = "(${word.partOfSpeech})",
        fontSize = 12.sp,
        color = VocabColors.TextMuted,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(12.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = word.exampleHanzi,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = VocabColors.TextDark
            )
            Text(
                text = word.examplePinyin,
                fontSize = 11.sp,
                color = VocabColors.BrandDark
            )
            Text(
                text = word.exampleMeaning,
                fontSize = 11.sp,
                color = VocabColors.TextMuted
            )
        }
    }
}

@Composable
private fun FlashcardActionFooter(
    word: VocabWordItem,
    onSpeak: (String) -> Unit,
    onToggleMastered: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .clip(VocabShapes.Option)
                .background(Color.White)
                .border(1.2.dp, VocabColors.BrandDark, VocabShapes.Option)
                .clickable { onSpeak(word.hanzi) }
                .padding(horizontal = 16.dp, vertical = 7.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = "Nghe đọc",
                    tint = VocabColors.BrandDark,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Nghe đọc",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.BrandDark
                )
            }
        }

        MasteredButton(
            isMastered = word.isMastered,
            onClick = { onToggleMastered(word.id) }
        )
    }
}

@Composable
private fun MasteredButton(
    isMastered: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isMastered) Color(0xFF15803D) else Color(0xFFF8FAFC)
    val contentColor = if (isMastered) Color.White else Color(0xFF64748B)
    val iconRes = if (isMastered) R.drawable.ic_check else R.drawable.ic_check_circle
    val label = if (isMastered) "Đã thuộc" else "Chưa thuộc"

    Box(
        modifier = Modifier
            .clip(VocabShapes.Option)
            .background(bgColor)
            .then(
                if (!isMastered) Modifier.border(1.dp, Color(0xFFCBD5E1), VocabShapes.Option)
                else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

@Composable
private fun FlashcardNavigationButtons(
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    val shapeNav = RoundedCornerShape(22.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .clip(shapeNav)
                .background(Color.White)
                .border(1.5.dp, VocabColors.BrandDark, shapeNav)
                .clickable(onClick = onPrevious),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "←  Thẻ trước",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = VocabColors.BrandDark
            )
        }

        Box(
            modifier = Modifier
                .weight(1.15f)
                .height(46.dp)
                .clip(shapeNav)
                .background(VocabColors.BrandDark)
                .clickable(onClick = onNext),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Thẻ tiếp theo  →",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
