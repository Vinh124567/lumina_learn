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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.ui.theme.PlusJakartaSans

@Composable
internal fun HanziAndPronunciationCard(
    toneCardData: ToneCardData,
    onPlayAudio: () -> Unit
) {
    com.example.luminalearn.presentation.vocabulary.component.PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderColor = com.example.luminalearn.presentation.vocabulary.component.PixelColors.BorderDark,
        shadowColor = com.example.luminalearn.presentation.vocabulary.component.PixelColors.ShadowDark,
        cornerRadius = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Hàng 4 ô vuông chữ Hán lớn kèm Pinyin dưới chân
            HanziBoxRow(
                hanziList = toneCardData.hanziVariants,
                pinyinList = toneCardData.pinyinVariants
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Hàng phát âm mā · má · mǎ · mà kèm nút Nghe đọc
            PronunciationAndAudioRow(
                pinyinList = toneCardData.pinyinVariants,
                onPlayAudio = onPlayAudio
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Box vàng chứa cả Hán Việt và Ý nghĩa
            HanVietAndMeaningBox(
                hanViet = toneCardData.hanViet,
                meaning = toneCardData.meaning
            )
        }
    }
}

@Composable
internal fun HanziBoxRow(
    hanziList: List<String>,
    pinyinList: List<String>
) {
    if (hanziList.isEmpty()) return

    if (hanziList.size == 1) {
        val hanzi = hanziList.first()
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(com.example.luminalearn.presentation.vocabulary.component.PixelColors.ShadowDark)
                    .padding(bottom = 2.5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(116.dp)
                        .height(64.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(com.example.luminalearn.presentation.vocabulary.component.PixelColors.ArcadePurple)
                        .border(1.5.dp, com.example.luminalearn.presentation.vocabulary.component.PixelColors.BorderDark, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = hanzi,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        hanziList.forEachIndexed { index, hanzi ->
            val pinyin = pinyinList.getOrElse(index) { "" }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 3.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(com.example.luminalearn.presentation.vocabulary.component.PixelColors.ShadowDark)
                    .padding(bottom = 2.5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(com.example.luminalearn.presentation.vocabulary.component.PixelColors.ArcadePurple)
                        .border(1.5.dp, com.example.luminalearn.presentation.vocabulary.component.PixelColors.BorderDark, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = hanzi,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        if (pinyin.isNotBlank()) {
                            Text(
                                text = pinyin,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.luminalearn.presentation.vocabulary.component.PixelColors.ArcadeAmber
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun PronunciationAndAudioRow(
    pinyinList: List<String>,
    onPlayAudio: () -> Unit
) {
    if (pinyinList.isEmpty()) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.2.dp, com.example.luminalearn.presentation.vocabulary.component.PixelColors.BorderDark, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val pinyinText = pinyinList.joinToString("  •  ")
            Text(
                text = pinyinText,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = com.example.luminalearn.presentation.vocabulary.component.PixelColors.ArcadePurple,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            // Nút Nghe đọc Arcade Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(com.example.luminalearn.presentation.vocabulary.component.PixelColors.ShadowDark)
                    .padding(bottom = 1.5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White)
                        .border(1.2.dp, com.example.luminalearn.presentation.vocabulary.component.PixelColors.BorderDark, RoundedCornerShape(5.dp))
                        .clickable(onClick = onPlayAudio)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "🔊", fontSize = 11.sp)
                        Text(
                            text = "Nghe đọc",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            color = com.example.luminalearn.presentation.vocabulary.component.PixelColors.TextDark,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun HanVietAndMeaningBox(
    hanViet: String,
    meaning: String
) {
    if (hanViet.isBlank() && meaning.isBlank()) return

    val displayHanViet = if (hanViet.startsWith("Hán Việt:")) hanViet.substringAfter("Hán Việt:").trim() else hanViet

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFEF3C7))
            .border(1.2.dp, com.example.luminalearn.presentation.vocabulary.component.PixelColors.BorderDark, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (displayHanViet.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Hán Việt: ",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.5.sp,
                        color = Color(0xFF92400E)
                    )
                    Text(
                        text = displayHanViet,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.5.sp,
                        color = Color(0xFF78350F),
                        lineHeight = 16.sp
                    )
                }
            }

            if (meaning.isNotBlank()) {
                if (displayHanViet.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Ý nghĩa: ",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.5.sp,
                        color = Color(0xFF92400E)
                    )
                    Text(
                        text = meaning,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.5.sp,
                        color = Color(0xFF78350F),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
internal fun ToneMapExplanationCard(
    explanationTitle: String,
    explanationSubtitle: String,
    rules: List<ToneRuleItem>,
    isToneType: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                )
                Text(
                    text = explanationTitle.ifBlank { "Quy tắc & Giải thích" },
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
                    color = Color(0xFF0F172A),
                    lineHeight = 20.sp
                )
            }

            if (explanationSubtitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = explanationSubtitle,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Các mục quy tắc / điểm ngữ pháp
            rules.forEach { rule ->
                ToneRuleRowItem(rule = rule, isToneType = isToneType)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
internal fun ToneRuleRowItem(
    rule: ToneRuleItem,
    isToneType: Boolean = false
) {
    val symbol = if (isToneType) {
        when (rule.number) {
            1 -> "—"
            2 -> "↗"
            3 -> "V"
            4 -> "↘"
            else -> "${rule.number}"
        }
    } else {
        "${rule.number}"
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFEDE9FE),
                modifier = Modifier.size(26.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = symbol,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF5538EE)
                    )
                }
            }

            val text = rule.text
            val colonIndex = text.indexOf(':')
            if (colonIndex != -1) {
                val prefix = text.substring(0, colonIndex + 1)
                val suffix = text.substring(colonIndex + 1)
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        ) {
                            append(prefix)
                        }
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF64748B)
                            )
                        ) {
                            append(suffix)
                        }
                    },
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Text(
                    text = rule.text,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFF1E293B),
                    lineHeight = 17.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
