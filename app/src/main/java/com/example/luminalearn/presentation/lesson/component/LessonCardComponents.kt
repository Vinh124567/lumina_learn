package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
            // Hàng 4 ô vuông chữ Hán lớn kèm Pinyin dưới chân
            HanziBoxRow(
                hanziList = toneCardData.hanziVariants,
                pinyinList = toneCardData.pinyinVariants
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Hàng phát âm mā · má · mǎ · mà kèm nút Nghe đọc
            PronunciationAndAudioRow(
                pinyinList = toneCardData.pinyinVariants,
                onPlayAudio = onPlayAudio
            )

            Spacer(modifier = Modifier.height(12.dp))

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
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF5538EE),
                modifier = Modifier
                    .width(116.dp)
                    .height(64.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = hanzi,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_speaker),
                        contentDescription = null,
                        tint = Color(0xFFDDD6FE),
                        modifier = Modifier.size(15.dp)
                    )
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
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF5538EE),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .height(68.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = hanzi,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (pinyin.isNotBlank()) {
                        Text(
                            text = pinyin,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFDDD6FE)
                        )
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

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val pinyinText = pinyinList.joinToString("  •  ")
            Text(
                text = pinyinText,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF4338CA),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
            )

            Surface(
                onClick = onPlayAudio,
                shape = RoundedCornerShape(50),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFF818CF8))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_speaker),
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Nghe đọc",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.5.sp,
                        color = Color(0xFF4F46E5),
                        maxLines = 1
                    )
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

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFFFFBEB),
        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            if (displayHanViet.isNotBlank()) {
                // Dòng 1: Hán Việt
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Hán Việt: ",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.5.sp,
                        color = Color(0xFF92400E)
                    )
                    Text(
                        text = displayHanViet,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.5.sp,
                        color = Color(0xFF78350F),
                        lineHeight = 16.sp
                    )
                }
            }

            if (meaning.isNotBlank()) {
                if (displayHanViet.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                }
                // Dòng 2: Ý nghĩa
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
    rules: List<ToneRuleItem>
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

            // 4 Box quy tắc thanh điệu
            rules.forEach { rule ->
                ToneRuleRowItem(rule = rule)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
internal fun ToneRuleRowItem(rule: ToneRuleItem) {
    val symbol = when (rule.number) {
        1 -> "—"
        2 -> "↗"
        3 -> "V"
        4 -> "↘"
        else -> "${rule.number}"
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
