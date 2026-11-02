package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
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
    PixelCardBox(
        backgroundColor = Color(0xFFFEF2F2),
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.5.dp,
        shadowOffset = 3.dp,
        cornerRadius = 6.dp,
        modifier = modifier
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
                PixelCardBox(
                    backgroundColor = Color(0xFFFEE2E2),
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
                        Text(text = "⚠️", fontSize = 16.sp)
                    }
                }
                Column {
                    Text(
                        text = "Chưa nhận diện được giọng nói",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark
                    )
                    Text(
                        text = errorMessage,
                        fontSize = 10.5.sp,
                        color = Color(0xFFB91C1C),
                        maxLines = 2
                    )
                }
            }
            PixelCardBox(
                backgroundColor = Color.White,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.2.dp,
                shadowOffset = 2.dp,
                cornerRadius = 4.dp,
                onClick = onRetry
            ) {
                Text(
                    text = "Thử lại ↺",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}

private data class ScoreStyle(
    val scoreColor: Color,
    val badgeText: String,
    val badgeTextColor: Color,
    val badgeBgColor: Color,
    val feedbackBgColor: Color,
    val feedbackBorderColor: Color
)

private fun getScoreStyle(score: Int): ScoreStyle = when {
    score >= 80 -> ScoreStyle(
        scoreColor = VocabColors.SuccessGreen,
        badgeText = "Xuất sắc 🌟",
        badgeTextColor = Color(0xFF16A34A),
        badgeBgColor = Color(0xFFDCFCE7),
        feedbackBgColor = VocabColors.BrandLight,
        feedbackBorderColor = Color(0xFFDCE7FE)
    )
    score >= 60 -> ScoreStyle(
        scoreColor = Color(0xFFF59E0B),
        badgeText = "Khá tốt 👍",
        badgeTextColor = Color(0xFFD97706),
        badgeBgColor = Color(0xFFFEF3C7),
        feedbackBgColor = Color(0xFFFFFBEB),
        feedbackBorderColor = Color(0xFFFDE68A)
    )
    else -> ScoreStyle(
        scoreColor = VocabColors.ErrorRed,
        badgeText = "Chưa chính xác ⚠️",
        badgeTextColor = Color(0xFFDC2626),
        badgeBgColor = Color(0xFFFEE2E2),
        feedbackBgColor = Color(0xFFFEF2F2),
        feedbackBorderColor = Color(0xFFFCA5A5)
    )
}

private data class SyllableScoreInfo(
    val score: Int,
    val badgeColor: Color,
    val badgeBg: Color,
    val comment: String
)

private fun calculateSyllableScore(score: Int, index: Int): SyllableScoreInfo {
    val sylScore = when {
        score >= 80 -> (score - (index * 2)).coerceIn(80, 100)
        score >= 60 -> (score - (index * 4)).coerceIn(55, 79)
        else -> (score - (index * 5)).coerceIn(20, 50)
    }
    return when {
        sylScore >= 80 -> SyllableScoreInfo(
            score = sylScore,
            badgeColor = Color(0xFF16A34A),
            badgeBg = Color(0xFFDCFCE7),
            comment = "Cao độ khớp ${sylScore}% mẫu bản xứ."
        )
        sylScore >= 60 -> SyllableScoreInfo(
            score = sylScore,
            badgeColor = Color(0xFFD97706),
            badgeBg = Color(0xFFFEF3C7),
            comment = "Cao độ lệch nhẹ, cần dứt khoát hơn."
        )
        else -> SyllableScoreInfo(
            score = sylScore,
            badgeColor = Color(0xFFDC2626),
            badgeBg = Color(0xFFFEE2E2),
            comment = "Sai thanh điệu, cao độ lệch chuẩn."
        )
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
    val style = getScoreStyle(score)

    PixelCardBox(
        backgroundColor = Color.White,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.5.dp,
        shadowOffset = 3.dp,
        cornerRadius = 6.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ScoreHeader(
                score = score,
                style = style,
                onRetry = onRetry
            )
            FeedbackBox(
                score = score,
                spoken = speechSuccess.spokenText,
                feedback = speechSuccess.feedback,
                word = word,
                style = style
            )
            SyllableAnalysisRow(
                score = score,
                syllables = syllables
            )
        }
    }
}

@Composable
private fun ScoreHeader(
    score: Int,
    style: ScoreStyle,
    onRetry: () -> Unit
) {
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
            PixelCardBox(
                backgroundColor = style.scoreColor,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.5.dp,
                shadowOffset = 2.dp,
                cornerRadius = 4.dp
            ) {
                Box(
                    modifier = Modifier.size(38.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$score",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Tương đồng cao độ",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PixelCardBox(
                        backgroundColor = style.badgeBgColor,
                        borderColor = PixelColors.BorderDark,
                        shadowColor = PixelColors.ShadowDark,
                        borderWidth = 1.dp,
                        shadowOffset = 1.dp,
                        cornerRadius = 3.dp
                    ) {
                        Text(
                            text = style.badgeText,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = style.badgeTextColor,
                            maxLines = 1,
                            softWrap = false,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "• Khớp: $score%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.BorderDark.copy(alpha = 0.7f),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        PixelCardBox(
            backgroundColor = Color.White,
            borderColor = PixelColors.BorderDark,
            shadowColor = PixelColors.ShadowDark,
            borderWidth = 1.2.dp,
            shadowOffset = 2.dp,
            cornerRadius = 4.dp,
            onClick = onRetry
        ) {
            Text(
                text = "Thử lại ↺",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.BorderDark,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FeedbackBox(
    score: Int,
    spoken: String,
    feedback: String,
    word: VocabWordItem,
    style: ScoreStyle
) {
    val iconRes = if (score >= 80) R.drawable.ic_sparkle else R.drawable.ic_mic

    PixelCardBox(
        backgroundColor = style.feedbackBgColor,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.2.dp,
        shadowOffset = 2.dp,
        cornerRadius = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = style.scoreColor,
                modifier = Modifier.size(16.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                if (spoken.isNotBlank()) {
                    SpokenFeedbackText(
                        score = score,
                        spoken = spoken,
                        word = word
                    )
                }
                Text(
                    text = feedback,
                    fontSize = 11.sp,
                    color = PixelColors.BorderDark,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun SpokenFeedbackText(
    score: Int,
    spoken: String,
    word: VocabWordItem
) {
    Text(
        text = buildAnnotatedString {
            append("Bạn đã đọc: ")
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Black,
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
        color = PixelColors.BorderDark
    )
}

@Composable
private fun SyllableAnalysisRow(
    score: Int,
    syllables: List<SyllableToneInfo>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        syllables.forEachIndexed { index, syl ->
            val sylInfo = calculateSyllableScore(score, index)
            SyllableCard(
                syl = syl,
                sylInfo = sylInfo,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SyllableCard(
    syl: SyllableToneInfo,
    sylInfo: SyllableScoreInfo,
    modifier: Modifier = Modifier
) {
    PixelCardBox(
        backgroundColor = Color(0xFFF8FAFC),
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.2.dp,
        shadowOffset = 1.5.dp,
        cornerRadius = 4.dp,
        modifier = modifier
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
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    maxLines = 1
                )
                PixelCardBox(
                    backgroundColor = sylInfo.badgeBg,
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.dp,
                    shadowOffset = 1.dp,
                    cornerRadius = 3.dp
                ) {
                    Text(
                        text = "${sylInfo.score}%",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = sylInfo.badgeColor,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
            Text(
                text = sylInfo.comment,
                fontSize = 9.5.sp,
                color = PixelColors.BorderDark.copy(alpha = 0.7f),
                maxLines = 1
            )
        }
    }
}
