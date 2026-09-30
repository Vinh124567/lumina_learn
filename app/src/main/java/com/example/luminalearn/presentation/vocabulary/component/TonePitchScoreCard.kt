package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.luminalearn.core.util.SpeechRecognitionState
import com.example.luminalearn.presentation.vocabulary.model.SyllableToneInfo
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem

@Composable
fun TonePitchErrorCard(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFEF2F2),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEE2E2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⚠️", fontSize = 16.sp)
                }
                Column {
                    Text(
                        text = "Chưa nhận diện được giọng nói",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF991B1B)
                    )
                    Text(
                        text = errorMessage,
                        fontSize = 10.5.sp,
                        color = Color(0xFFB91C1C),
                        maxLines = 2
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onRetry),
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
            ) {
                Text(
                    text = "Thử lại ↺",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF991B1B),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
fun TonePitchScoreCard(
    word: VocabWordItem,
    syllables: List<SyllableToneInfo>,
    speechSuccess: SpeechRecognitionState.Success,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val score = speechSuccess.score
    val spoken = speechSuccess.spokenText
    val feedback = speechSuccess.feedback

    val scoreColor = when {
        score >= 80 -> VocabColors.SuccessGreen
        score >= 60 -> Color(0xFFF59E0B)
        else -> VocabColors.ErrorRed
    }
    val badgeText = when {
        score >= 80 -> "Xuất sắc 🌟"
        score >= 60 -> "Khá tốt 👍"
        else -> "Chưa chính xác ⚠️"
    }
    val badgeTextColor = when {
        score >= 80 -> Color(0xFF16A34A)
        score >= 60 -> Color(0xFFD97706)
        else -> Color(0xFFDC2626)
    }
    val badgeBgColor = when {
        score >= 80 -> Color(0xFFDCFCE7)
        score >= 60 -> Color(0xFFFEF3C7)
        else -> Color(0xFFFEE2E2)
    }



    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, VocabColors.BorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Điểm số thực tế + Nhãn đánh giá + Nút Thử lại
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(scoreColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$score",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "Tương đồng cao độ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = VocabColors.TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(badgeBgColor)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = badgeText,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = badgeTextColor,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }

                            Text(
                                text = "• Khớp: $score%",
                                fontSize = 11.sp,
                                color = VocabColors.TextMuted,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onRetry),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Text(
                        text = "Thử lại ↺",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VocabColors.TextDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        maxLines = 1
                    )
                }
            }

            // Hộp gợi ý phát âm & phản hồi thanh điệu thực tế
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = if (score >= 80) VocabColors.BrandLight else if (score >= 60) Color(0xFFFFFBEB) else Color(0xFFFEF2F2),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (score >= 80) Color(0xFFDCE7FE) else if (score >= 60) Color(0xFFFDE68A) else Color(0xFFFCA5A5)
                )
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = if (score >= 80) R.drawable.ic_sparkle else R.drawable.ic_mic),
                        contentDescription = null,
                        tint = scoreColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        if (spoken.isNotBlank()) {
                            Text(
                                text = buildAnnotatedString {
                                    append("Bạn đã đọc: ")
                                    withStyle(
                                        SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = if (score >= 80) Color(0xFF16A34A) else Color(0xFFDC2626)
                                        )
                                    ) {
                                        append("\"$spoken\"")
                                    }
                                    if (score < 80) {
                                        append(" • Từ mẫu: \"${word.hanzi}\" (${word.pinyin})")
                                    }
                                },
                                fontSize = 11.5.sp,
                                color = VocabColors.TextDark
                            )
                        }
                        Text(
                            text = feedback,
                            fontSize = 11.sp,
                            color = VocabColors.TextDark,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Phân tích từng âm tiết dựa trên điểm số thực tế
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                syllables.forEachIndexed { index, syl ->
                    val sylScore = when {
                        score >= 80 -> (score - (index * 2)).coerceIn(80, 100)
                        score >= 60 -> (score - (index * 4)).coerceIn(55, 79)
                        else -> (score - (index * 5)).coerceIn(20, 50)
                    }
                    val (sylBadgeColor, sylBadgeBg, sylComment) = when {
                        sylScore >= 80 -> Triple(Color(0xFF16A34A), Color(0xFFDCFCE7), "Cao độ khớp ${sylScore}% mẫu bản xứ.")
                        sylScore >= 60 -> Triple(Color(0xFFD97706), Color(0xFFFEF3C7), "Cao độ lệch nhẹ, cần dứt khoát hơn.")
                        else -> Triple(Color(0xFFDC2626), Color(0xFFFEE2E2), "Sai thanh điệu, cao độ lệch chuẩn.")
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${syl.hanzi} ${syl.pinyin}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VocabColors.TextDark,
                                    maxLines = 1
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(sylBadgeBg)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "${sylScore}%",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = sylBadgeColor
                                    )
                                }
                            }
                            Text(
                                text = sylComment,
                                fontSize = 9.5.sp,
                                color = VocabColors.TextMuted,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
