package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.ui.theme.PlusJakartaSans

internal fun parseHexColor(colorHex: String, defaultColor: Color): Color {
    if (colorHex.isBlank()) return defaultColor
    return try {
        val cleanHex = if (colorHex.startsWith("#")) colorHex.substring(1) else colorHex
        val colorInt = cleanHex.toLong(16).toInt()
        val fullAlpha = if (cleanHex.length <= 6) (0xFF000000 or colorInt.toLong()).toInt() else colorInt
        Color(fullAlpha)
    } catch (_: Exception) {
        defaultColor
    }
}

@Composable
internal fun SolidExplanationCard(
    title: String,
    content: String,
    backgroundColor: Color = Color(0xFF5538EE),
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.5.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = content,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 13.5.sp,
                color = Color.White.copy(alpha = 0.95f),
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
internal fun ConceptSlideBody(
    currentSlide: ToneCardData,
    onPlayAudio: () -> Unit
) {
    HanziAndPronunciationCard(
        toneCardData = currentSlide,
        onPlayAudio = onPlayAudio
    )

    Spacer(modifier = Modifier.height(16.dp))

    if (currentSlide.explanationContent.isNotBlank()) {
        SolidExplanationCard(
            title = currentSlide.explanationText.ifBlank { "Bản đồ 4 Thanh điệu" },
            content = currentSlide.explanationContent,
            backgroundColor = parseHexColor(currentSlide.boxColor, Color(0xFF5538EE))
        )
    } else {
        ToneMapExplanationCard(
            explanationTitle = currentSlide.explanationText,
            explanationSubtitle = currentSlide.explanationSubtitle,
            rules = currentSlide.toneRules
        )
    }
}

@Composable
internal fun InteractiveSlideBody(
    currentSlide: ToneCardData,
    onPlayAudio: () -> Unit
) {
    HanziAndPronunciationCard(
        toneCardData = currentSlide,
        onPlayAudio = onPlayAudio
    )

    Spacer(modifier = Modifier.height(16.dp))

    SolidExplanationCard(
        title = currentSlide.explanationText.ifBlank { "Quy tắc Biến điệu" },
        content = currentSlide.explanationContent.ifBlank {
            "Khi hai thanh 3 đi liền nhau, thanh 3 thứ nhất ĐỌC THÀNH THANH 2. Ví dụ: Chữ \"Nǐ\" (thanh 3) + \"hǎo\" (thanh 3) sẽ phát âm thành \"Ní hǎo\"!"
        },
        backgroundColor = parseHexColor(currentSlide.boxColor, Color(0xFFE06A3B))
    )
}

@Composable
internal fun QuizSlideBody(
    currentSlide: ToneCardData
) {
    var selectedOptionIndex by remember(currentSlide.slideIndex) { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Box tím đậm
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = parseHexColor(currentSlide.boxColor, Color(0xFF5538EE))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = currentSlide.explanationText.ifBlank { "Kiểm tra phản xạ thanh điệu" },
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                if (currentSlide.explanationSubtitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentSlide.explanationSubtitle,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.95f),
                        lineHeight = 19.sp
                    )
                }
            }
        }

        // Question box
        if (currentSlide.question.isNotBlank()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Text(
                    text = currentSlide.question,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = Color(0xFF0F172A),
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                )
            }
        }

        // Options list
        currentSlide.options.forEachIndexed { index, optionText ->
            val isSelected = selectedOptionIndex == index
            val hasAnswered = selectedOptionIndex != null
            val isCorrectAnswer = index == currentSlide.correctAnswerIndex

            val optionBg = when {
                !hasAnswered -> Color.White
                isSelected && isCorrectAnswer -> Color(0xFFF0FDF4)
                isSelected && !isCorrectAnswer -> Color(0xFFFEF2F2)
                hasAnswered && isCorrectAnswer -> Color(0xFFF0FDF4)
                else -> Color.White
            }

            val optionBorder = when {
                !hasAnswered -> Color(0xFFE2E8F0)
                isSelected && isCorrectAnswer -> Color(0xFF22C55E)
                isSelected && !isCorrectAnswer -> Color(0xFFEF4444)
                hasAnswered && isCorrectAnswer -> Color(0xFF22C55E)
                else -> Color(0xFFE2E8F0)
            }

            val optionTextColor = when {
                !hasAnswered -> Color(0xFF334155)
                isSelected && isCorrectAnswer -> Color(0xFF15803D)
                isSelected && !isCorrectAnswer -> Color(0xFFB91C1C)
                hasAnswered && isCorrectAnswer -> Color(0xFF15803D)
                else -> Color(0xFF64748B)
            }

            Surface(
                onClick = {
                    if (selectedOptionIndex == null) {
                        selectedOptionIndex = index
                    }
                },
                shape = RoundedCornerShape(14.dp),
                color = optionBg,
                border = BorderStroke(1.dp, optionBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = optionText,
                        fontFamily = PlusJakartaSans,
                        fontWeight = if (isSelected || (hasAnswered && isCorrectAnswer)) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        color = optionTextColor,
                        modifier = Modifier.weight(1f)
                    )

                    if (hasAnswered) {
                        if (isCorrectAnswer) {
                            Text(
                                text = "✓",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF15803D),
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        } else if (isSelected) {
                            Text(
                                text = "✗",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFB91C1C),
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Explanation chip when answered
        if (selectedOptionIndex != null && currentSlide.quizExplanation.isNotBlank()) {
            val isCorrect = selectedOptionIndex == currentSlide.correctAnswerIndex
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isCorrect) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                border = BorderStroke(1.dp, if (isCorrect) Color(0xFF86EFAC) else Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isCorrect) "🎉 Chính xác!" else "💡 Giải thích:",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = if (isCorrect) Color(0xFF166534) else Color(0xFF92400E)
                    )
                    Text(
                        text = currentSlide.quizExplanation,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = if (isCorrect) Color(0xFF14532D) else Color(0xFF78350F),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
internal fun TakeawaySlideBody(
    currentSlide: ToneCardData
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = parseHexColor(currentSlide.boxColor, Color(0xFF5538EE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Text(
                text = currentSlide.explanationText.ifBlank { "Thực hành hôm nay" },
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.5.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentSlide.explanationContent.ifBlank {
                    "Hãy đứng trước gương và phát âm to 5 lần: \"Nǐ hǎo\" (đọc là Ní hǎo) và \"Hěn hǎo\" (đọc là Hén hǎo) để cơ miệng quen với nhịp điệu nhé!"
                },
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 13.5.sp,
                color = Color.White.copy(alpha = 0.95f),
                lineHeight = 22.sp
            )
        }
    }
}
