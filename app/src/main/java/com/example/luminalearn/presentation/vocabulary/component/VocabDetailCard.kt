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
import androidx.compose.ui.text.style.TextOverflow
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
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        shadowElevation = 1.5.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            VocabCardHeader(item = item, onToggleMastered = onToggleMastered)

            Spacer(modifier = Modifier.height(12.dp))

            VocabCardMainRow(item = item, onSpeak = onSpeak)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.meaning,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(10.dp))

            VocabExampleBox(item = item, onSpeak = onSpeak)

            Spacer(modifier = Modifier.height(12.dp))

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

    val (hanziFontSize, boxWidth) = remember(item.hanzi) {
        when {
            item.hanzi.length <= 1 -> Pair(28.sp, 62.dp)
            item.hanzi.length == 2 -> Pair(22.sp, 62.dp)
            item.hanzi.length == 3 -> Pair(17.sp, 74.dp)
            else -> Pair(14.sp, 86.dp)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(62.dp)
                .width(boxWidth)
                .clip(VocabShapes.Hanzi)
                .background(Color(0xFFF8FAFC), VocabShapes.Hanzi)
                .border(1.dp, Color(0xFFEEF2F6), VocabShapes.Hanzi)
                .clickable { onSpeak(item.hanzi) }
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = item.hanzi,
                fontSize = hanziFontSize,
                fontWeight = FontWeight.Bold,
                color = VocabColors.TextDark,
                maxLines = 1,
                softWrap = false,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = item.pinyin,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.BrandDark
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = "Speak",
                    tint = VocabColors.BrandPrimary,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onSpeak(item.hanzi) }
                )
                Text(
                    text = "(${item.partOfSpeech})",
                    fontSize = 11.sp,
                    color = VocabColors.TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
            ) {
                Text(
                    text = "HV: ${item.hanViet}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        Text(
            text = radicalAndStrokesText,
            fontSize = 10.5.sp,
            color = Color(0xFF94A3B8),
            textAlign = TextAlign.End,
            lineHeight = 15.sp
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
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B), fontSize = 13.sp)) {
                append(item.exampleHanzi)
            }
            append("\n")
            withStyle(SpanStyle(color = Color(0xFF5C50F6), fontSize = 11.sp, fontWeight = FontWeight.Medium)) {
                append(item.examplePinyin)
            }
            append("\n")
            withStyle(SpanStyle(color = Color(0xFF64748B), fontSize = 11.5.sp)) {
                append(item.exampleMeaning)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = exampleAnnotatedText,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_speaker),
                contentDescription = "Speak example",
                tint = Color(0xFF94A3B8),
                modifier = Modifier
                    .size(16.dp)
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
