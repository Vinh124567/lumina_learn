package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.vocabulary.model.SyllableToneInfo
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem

@Composable
fun VocabTonePracticeView(
    word: VocabWordItem,
    syllables: List<SyllableToneInfo>,
    onSpeakSample: (String) -> Unit,
    onSpeakSlow: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ── 1. Header Card ──
        PixelCardBox(
            backgroundColor = Color.White,
            borderColor = PixelColors.BorderDark,
            shadowColor = PixelColors.ShadowDark,
            borderWidth = 1.5.dp,
            shadowOffset = 3.dp,
            cornerRadius = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PixelCardBox(
                    backgroundColor = Color(0xFFFEF3C7),
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.2.dp,
                    shadowOffset = 1.5.dp,
                    cornerRadius = 4.dp
                ) {
                    Box(
                        modifier = Modifier.size(34.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Luyện thanh điệu từng chữ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark
                    )
                    Text(
                        text = "Luyện tai nghe và kiểm soát cao độ theo chuẩn Chao",
                        fontSize = 10.5.sp,
                        color = PixelColors.BorderDark.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // ── 2. Quy tắc biến điệu thanh 3 (nếu có) ──
        val hasToneSandhi = syllables.size >= 2 && syllables.take(2).all { it.toneNumber == 3 }
        if (hasToneSandhi) {
            ToneSandhiRuleCard(word = word)
        }

        // ── 3. Thẻ luyện tập phát âm từng âm tiết ──
        syllables.forEachIndexed { index, syl ->
            SyllableToneCard(
                index = index + 1,
                syllable = syl,
                onPlayNormal = { onSpeakSample(syl.hanzi) },
                onPlaySlow = { onSpeakSlow(syl.hanzi) }
            )
        }

        // ── 4. Thang Ngũ Độ Chao tham chiếu 4 thanh ──
        ChaoScaleReferenceCard()
    }
}

@Composable
private fun ToneSandhiRuleCard(word: VocabWordItem) {
    PixelCardBox(
        backgroundColor = Color(0xFFFFFBEB),
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.5.dp,
        shadowOffset = 3.dp,
        cornerRadius = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "⚡", fontSize = 14.sp)
                Text(
                    text = "Quy tắc biến điệu thanh 3 (3 + 3 → 2 + 3)",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFB45309)
                )
            }

            Text(
                text = "Khi hai âm tiết thanh 3 đi liền nhau như trong \"${word.hanzi}\", âm tiết đầu tiên sẽ tự động biến thành thanh 2 (đọc vút lên cao) để câu nói mượt mà hơn.",
                fontSize = 11.sp,
                color = Color(0xFF78350F),
                lineHeight = 16.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PixelCardBox(
                    backgroundColor = Color.White,
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.dp,
                    shadowOffset = 1.dp,
                    cornerRadius = 3.dp
                ) {
                    Text(
                        text = "Viết: ${word.pinyin}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Text(text = "→", fontSize = 12.sp, color = PixelColors.BorderDark, fontWeight = FontWeight.Black)
                PixelCardBox(
                    backgroundColor = Color(0xFFDCFCE7),
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.dp,
                    shadowOffset = 1.dp,
                    cornerRadius = 3.dp
                ) {
                    Text(
                        text = "Đọc: Thanh 2 + Thanh 3",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF166534),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SyllableToneCard(
    index: Int,
    syllable: SyllableToneInfo,
    onPlayNormal: () -> Unit,
    onPlaySlow: () -> Unit
) {
    val toneColor = when (syllable.toneNumber) {
        1 -> Color(0xFF4F46E5)
        2 -> Color(0xFF16A34A)
        3 -> Color(0xFFD97706)
        4 -> Color(0xFFDC2626)
        else -> Color(0xFF64748B)
    }

    PixelCardBox(
        backgroundColor = Color.White,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.5.dp,
        shadowOffset = 3.dp,
        cornerRadius = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PixelCardBox(
                        backgroundColor = toneColor.copy(alpha = 0.12f),
                        borderColor = PixelColors.BorderDark,
                        shadowColor = PixelColors.ShadowDark,
                        borderWidth = 1.2.dp,
                        shadowOffset = 1.5.dp,
                        cornerRadius = 4.dp
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = syllable.hanzi,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = toneColor
                            )
                        }
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = syllable.pinyin,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            PixelCardBox(
                                backgroundColor = toneColor.copy(alpha = 0.15f),
                                borderColor = PixelColors.BorderDark,
                                shadowColor = PixelColors.ShadowDark,
                                borderWidth = 1.dp,
                                shadowOffset = 1.dp,
                                cornerRadius = 3.dp
                            ) {
                                Text(
                                    text = "${syllable.toneName} (${syllable.toneCode})",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = toneColor,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Âm tiết $index • Cao độ Chao: ${syllable.toneCode}",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.BorderDark.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Nút nghe phát âm tốc độ chuẩn & chậm
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PixelCardBox(
                    backgroundColor = PixelColors.PixelGreen.copy(alpha = 0.15f),
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.2.dp,
                    shadowOffset = 2.dp,
                    cornerRadius = 4.dp,
                    onClick = onPlayNormal,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = null,
                            tint = PixelColors.BorderDark,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Nghe âm (1.0x)",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.BorderDark
                        )
                    }
                }

                PixelCardBox(
                    backgroundColor = PixelColors.PixelYellow.copy(alpha = 0.25f),
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.2.dp,
                    shadowOffset = 2.dp,
                    cornerRadius = 4.dp,
                    onClick = onPlaySlow,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🐢 Chậm (0.65x)",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.BorderDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChaoScaleReferenceCard() {
    PixelCardBox(
        backgroundColor = Color.White,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.5.dp,
        shadowOffset = 3.dp,
        cornerRadius = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "📊 Thang ngũ độ Chao (Mốc 1 – 5)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.BorderDark
            )

            val tones = listOf(
                "Thanh 1 (阴平)" to "55 (Cao bằng)",
                "Thanh 2 (阳平)" to "35 (Vút lên)",
                "Thanh 3 (上声)" to "214 (Hạ sâu rồi lên)",
                "Thanh 4 (去声)" to "51 (Đổ dốc dứt khoát)"
            )

            tones.forEach { (name, desc) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PixelColors.BorderDark)
                    Text(text = desc, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = PixelColors.BorderDark.copy(alpha = 0.7f))
                }
            }
        }
    }
}
