package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.bounceClick
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
    onQuickVoiceTest: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Card(
        modifier = modifier
            .fillMaxWidth()
            .bounceClick(scaleDown = 0.98f, onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0xFF2E3458).copy(alpha = 0.65f)),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF161A34),
                            Color(0xFF0F1326)
                        )
                    )
                )
        ) {
            CosmicStarfield(
                modifier = Modifier.matchParentSize(),
                particleCount = 20,
                focusCenterXRatio = 0.88f,
                focusCenterYRatio = 0.35f
            )

            Text(
                text = item.hanzi,
                fontSize = 80.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF6366F1).copy(alpha = 0.08f),
                maxLines = 1,
                softWrap = false,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 8.dp, y = 14.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                VocabCardHeader(item = item, onToggleMastered = onToggleMastered)

                Spacer(modifier = Modifier.height(12.dp))

                VocabCardMainRow(
                    item = item,
                    onSpeak = onSpeak,
                    onQuickVoiceTest = onQuickVoiceTest
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = item.meaning,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                VocabExampleBox(item = item, onSpeak = onSpeak)

                Spacer(modifier = Modifier.height(12.dp))

                VocabCardFooter(item = item, onToggleMastered = onToggleMastered)
            }
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
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF6366F1).copy(alpha = 0.2f))
                    .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.45f)), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = item.hskLevel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA5B4FC)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.07f))
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = item.topic,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            if (item.isCustom) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF43F5E).copy(alpha = 0.2f))
                        .border(BorderStroke(1.dp, Color(0xFFFB7185).copy(alpha = 0.45f)), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Tự thêm",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFDA4AF)
                    )
                }
            }
        }

        val checkboxBorder = if (item.isMastered) Color(0xFF34D399) else Color(0xFF475569)
        val checkboxBg = if (item.isMastered) Color(0xFF10B981) else Color.White.copy(alpha = 0.05f)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(checkboxBg)
                .border(width = 1.2.dp, color = checkboxBorder, shape = RoundedCornerShape(6.dp))
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
    onSpeak: (String) -> Unit,
    onQuickVoiceTest: () -> Unit
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
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF1E2448), Color(0xFF151934))
                    ),
                    RoundedCornerShape(14.dp)
                )
                .border(BorderStroke(1.dp, Color(0xFF6366F1).copy(alpha = 0.45f)), RoundedCornerShape(14.dp))
                .bounceClick(scaleDown = 0.92f) { onSpeak(item.hanzi) }
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = item.hanzi,
                fontSize = hanziFontSize,
                fontWeight = FontWeight.Bold,
                color = Color.White,
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
                    color = Color(0xFF38BDF8)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = "Speak",
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier
                        .size(16.dp)
                        .bounceClick(scaleDown = 0.88f) { onSpeak(item.hanzi) }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_waveform),
                    contentDescription = "Biểu đồ cao độ",
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier
                        .size(16.dp)
                        .bounceClick(scaleDown = 0.88f, onClick = onQuickVoiceTest)
                )
                Text(
                    text = "(${item.partOfSpeech})",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)), RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
            ) {
                Text(
                    text = "HV: ${item.hanViet}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE2E8F0),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        Text(
            text = radicalAndStrokesText,
            fontSize = 10.5.sp,
            color = Color(0xFF64748B),
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
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = Color(0xFFF1F5F9), fontSize = 13.sp)) {
                append(item.exampleHanzi)
            }
            append("\n")
            withStyle(SpanStyle(color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Medium)) {
                append(item.examplePinyin)
            }
            append("\n")
            withStyle(SpanStyle(color = Color(0xFF94A3B8), fontSize = 11.5.sp)) {
                append(item.exampleMeaning)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .border(BorderStroke(1.dp, Color(0xFF2E3458).copy(alpha = 0.6f)), RoundedCornerShape(14.dp))
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
                tint = Color(0xFF38BDF8),
                modifier = Modifier
                    .size(16.dp)
                    .bounceClick(scaleDown = 0.88f) { onSpeak(item.exampleHanzi) }
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
            color = Color(0xFF64748B)
        )

        if (item.isMastered) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF10B981).copy(alpha = 0.15f))
                    .border(BorderStroke(1.dp, Color(0xFF34D399).copy(alpha = 0.35f)), RoundedCornerShape(8.dp))
                    .clickable { onToggleMastered(item.id) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check_circle),
                    contentDescription = null,
                    tint = Color(0xFF34D399),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Đã thuộc",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF34D399)
                )
            }
        } else {
            Text(
                text = "Chưa thuộc",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onToggleMastered(item.id) }
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}
