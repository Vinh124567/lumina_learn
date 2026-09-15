package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
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

@Composable
fun VocabDetailCard(
    item: VocabWordItem,
    onSpeak: (String) -> Unit,
    onToggleMastered: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, VocabShapes.Card)
            .border(1.dp, Color(0xFFF1F5F9), VocabShapes.Card)
            .padding(14.dp)
    ) {
        Column {
            VocabCardHeader(item = item, onToggleMastered = onToggleMastered)

            Spacer(modifier = Modifier.height(10.dp))

            VocabCardMainRow(item = item, onSpeak = onSpeak)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.meaning,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = VocabColors.TextDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            VocabExampleBox(item = item, onSpeak = onSpeak)

            Spacer(modifier = Modifier.height(10.dp))

            VocabCardFooter(item = item, onToggleMastered = onToggleMastered)
        }
    }
}

@Composable
private fun VocabCardHeader(
    item: VocabWordItem,
    onToggleMastered: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(VocabColors.BrandLight, VocabShapes.Tag)
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Text(
                    text = item.hskLevel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.BrandPrimary
                )
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFFF1F5F9), VocabShapes.Tag)
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Text(
                    text = item.topic,
                    fontSize = 11.sp,
                    color = VocabColors.TextSecondary
                )
            }
        }

        val checkboxBorder = if (item.isMastered) VocabColors.BrandPrimary else Color(0xFFCBD5E1)
        val checkboxBg = if (item.isMastered) VocabColors.BrandPrimary else Color.Transparent

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .clip(VocabShapes.Checkbox)
                .background(checkboxBg)
                .border(width = 1.5.dp, color = checkboxBorder, shape = VocabShapes.Checkbox)
                .clickable { onToggleMastered(item.id) }
        ) {
            if (item.isMastered) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "Mastered",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun VocabCardMainRow(
    item: VocabWordItem,
    onSpeak: (String) -> Unit
) {
    val radicalAndStrokesText = remember(item.radical, item.strokes) {
        "${item.radical}\n${item.strokes}"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(62.dp)
                .background(Color(0xFFF8FAFC), VocabShapes.Hanzi)
                .border(1.dp, Color(0xFFEEF2F6), VocabShapes.Hanzi)
                .clickable { onSpeak(item.hanzi) }
        ) {
            Text(
                text = item.hanzi,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = VocabColors.TextDark
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.pinyin,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.BrandDark
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = "Speak",
                    tint = VocabColors.BrandPrimary,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onSpeak(item.hanzi) }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(VocabColors.HanVietBg, VocabShapes.Badge)
                        .border(1.dp, Color(0xFFFDE68A), VocabShapes.Badge)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Hán Việt: ${item.hanViet}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VocabColors.HanVietAmber
                    )
                }

                Text(
                    text = item.partOfSpeech,
                    fontSize = 11.sp,
                    color = VocabColors.TextMuted
                )
            }
        }

        Text(
            text = radicalAndStrokesText,
            fontSize = 10.sp,
            color = VocabColors.TextMuted,
            textAlign = TextAlign.End,
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun VocabExampleBox(
    item: VocabWordItem,
    onSpeak: (String) -> Unit
) {
    val exampleAnnotatedText = remember(item.exampleHanzi, item.examplePinyin, item.exampleMeaning) {
        buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = VocabColors.TextDark, fontSize = 13.sp)) {
                append(item.exampleHanzi)
            }
            append("\n")
            withStyle(SpanStyle(color = VocabColors.BrandDark, fontSize = 11.sp)) {
                append(item.examplePinyin)
            }
            append("\n")
            withStyle(SpanStyle(color = VocabColors.TextMuted, fontSize = 11.sp)) {
                append(item.exampleMeaning)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC), VocabShapes.Example)
            .border(1.dp, Color(0xFFF1F5F9), VocabShapes.Example)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = exampleAnnotatedText,
                lineHeight = 17.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_speaker),
                contentDescription = "Speak example",
                tint = Color(0xFF94A3B8),
                modifier = Modifier
                    .size(15.dp)
                    .clickable { onSpeak(item.exampleHanzi) }
            )
        }
    }
}

@Composable
private fun VocabCardFooter(
    item: VocabWordItem,
    onToggleMastered: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.targetScore,
            fontSize = 11.sp,
            color = VocabColors.TextMuted
        )

        if (item.isMastered) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(VocabShapes.Tag)
                    .clickable { onToggleMastered(item.id) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check_circle),
                    contentDescription = null,
                    tint = VocabColors.SuccessGreen,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Đã thuộc",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.SuccessGreen
                )
            }
        } else {
            Text(
                text = "Chưa thuộc",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = VocabColors.TextMuted,
                modifier = Modifier
                    .clip(VocabShapes.Tag)
                    .clickable { onToggleMastered(item.id) }
            )
        }
    }
}
