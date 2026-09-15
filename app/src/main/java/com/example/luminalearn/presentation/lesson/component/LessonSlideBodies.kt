package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
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

    val isToneLesson = currentSlide.category.contains("PINYIN", ignoreCase = true) &&
            (currentSlide.title.contains("Thanh điệu", ignoreCase = true) ||
             currentSlide.title.contains("biến âm", ignoreCase = true) ||
             currentSlide.explanationText.contains("Thanh điệu", ignoreCase = true))

    if (currentSlide.explanationContent.isNotBlank()) {
        SolidExplanationCard(
            title = currentSlide.explanationText.ifBlank { "Nội dung bài học" },
            content = currentSlide.explanationContent,
            backgroundColor = parseHexColor(currentSlide.boxColor, Color(0xFF5538EE))
        )
    } else {
        ToneMapExplanationCard(
            explanationTitle = currentSlide.explanationText,
            explanationSubtitle = currentSlide.explanationSubtitle,
            rules = currentSlide.toneRules,
            isToneType = isToneLesson
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
        title = currentSlide.explanationText.ifBlank { "Mẫu câu thực hành" },
        content = currentSlide.explanationContent.ifBlank {
            currentSlide.meaning.ifBlank { "Luyện tập phát âm và nắm vững cấu trúc bài học." }
        },
        backgroundColor = parseHexColor(currentSlide.boxColor, Color(0xFF0D9488))
    )
}

@Composable
internal fun QuizSlideBody(
    currentSlide: ToneCardData
) {
    var selectedOptionIndex by remember(currentSlide.slideIndex) { mutableStateOf<Int?>(null) }
    val optionLabels = remember { listOf("A", "B", "C", "D") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuizQuestionCard(
            subtitle = currentSlide.explanationSubtitle,
            questionText = currentSlide.question.ifBlank { currentSlide.title }
        )

        currentSlide.options.forEachIndexed { index, optionText ->
            val label = optionLabels.getOrElse(index) { "${index + 1}" }
            val theme = getOptionTheme(
                label = label,
                isSelected = selectedOptionIndex == index,
                hasAnswered = selectedOptionIndex != null,
                isCorrectAnswer = index == currentSlide.correctAnswerIndex
            )

            QuizOptionItem(
                optionText = optionText,
                theme = theme,
                onSelect = {
                    if (selectedOptionIndex == null) {
                        selectedOptionIndex = index
                    }
                }
            )
        }

        if (selectedOptionIndex != null && currentSlide.quizExplanation.isNotBlank()) {
            QuizExplanationCard(
                isCorrect = selectedOptionIndex == currentSlide.correctAnswerIndex,
                explanation = currentSlide.quizExplanation
            )
        }
    }
}

private data class OptionTheme(
    val backgroundColor: Color,
    val borderColor: Color,
    val textColor: Color,
    val badgeBgColor: Color,
    val badgeTextColor: Color,
    val badgeText: String,
    val isBold: Boolean
)

private fun getOptionTheme(
    label: String,
    isSelected: Boolean,
    hasAnswered: Boolean,
    isCorrectAnswer: Boolean
): OptionTheme {
    return when {
        !hasAnswered -> OptionTheme(
            backgroundColor = Color.White,
            borderColor = Color(0xFFE2E8F0),
            textColor = Color(0xFF334155),
            badgeBgColor = Color(0xFFF1F5F9),
            badgeTextColor = Color(0xFF64748B),
            badgeText = label,
            isBold = false
        )
        isCorrectAnswer -> OptionTheme(
            backgroundColor = Color(0xFFF0FDF4),
            borderColor = Color(0xFF22C55E),
            textColor = Color(0xFF15803D),
            badgeBgColor = Color(0xFF22C55E),
            badgeTextColor = Color.White,
            badgeText = "✓",
            isBold = true
        )
        isSelected -> OptionTheme(
            backgroundColor = Color(0xFFFEF2F2),
            borderColor = Color(0xFFEF4444),
            textColor = Color(0xFFB91C1C),
            badgeBgColor = Color(0xFFEF4444),
            badgeTextColor = Color.White,
            badgeText = "✕",
            isBold = true
        )
        else -> OptionTheme(
            backgroundColor = Color.White,
            borderColor = Color(0xFFE2E8F0),
            textColor = Color(0xFF64748B),
            badgeBgColor = Color(0xFFF1F5F9),
            badgeTextColor = Color(0xFF64748B),
            badgeText = label,
            isBold = false
        )
    }
}

@Composable
private fun QuizQuestionCard(
    subtitle: String,
    questionText: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color(0xFF6366F1)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = questionText,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF0F172A),
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun QuizOptionItem(
    optionText: String,
    theme: OptionTheme,
    onSelect: () -> Unit
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(14.dp),
        color = theme.backgroundColor,
        border = BorderStroke(1.dp, theme.borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(theme.badgeBgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = theme.badgeText,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = theme.badgeTextColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = optionText,
                fontFamily = PlusJakartaSans,
                fontWeight = if (theme.isBold) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.5.sp,
                color = theme.textColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuizExplanationCard(
    isCorrect: Boolean,
    explanation: String
) {
    val bgColor = if (isCorrect) Color(0xFFF0FDF4) else Color(0xFFFFFBEB)
    val borderColor = if (isCorrect) Color(0xFF86EFAC) else Color(0xFFFDE68A)
    val titleText = if (isCorrect) "🎉 Chính xác!" else "💡 Giải thích chi tiết"
    val titleColor = if (isCorrect) Color(0xFF15803D) else Color(0xFFB45309)
    val contentColor = if (isCorrect) Color(0xFF166534) else Color(0xFF78350F)

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = titleText,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = titleColor
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = explanation,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.5.sp,
                color = contentColor,
                lineHeight = 18.sp
            )
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
        color = parseHexColor(currentSlide.boxColor, Color(0xFF059669))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Text(
                text = currentSlide.explanationText.ifBlank { "Ghi nhớ sống còn" },
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentSlide.explanationContent.ifBlank {
                    currentSlide.meaning.ifBlank { "Ôn tập và áp dụng kiến thức này vào các bài học tiếp theo nhé!" }
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
