package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.luminalearn.presentation.vocabulary.ReflexQuizQuestion
import com.example.luminalearn.presentation.vocabulary.ReflexQuizState
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabShapes

data class QuizActions(
    val onSelectOption: (String) -> Unit,
    val onRestart: () -> Unit,
    val onBackToList: () -> Unit
)

@Composable
fun VocabReflexQuizSection(
    quizState: ReflexQuizState,
    actions: QuizActions,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Tự động phát âm khi chuyển sang câu hỏi mới
    LaunchedEffect(quizState.currentIndex, quizState.isFinished) {
        val q = quizState.currentQuestion
        if (!quizState.isFinished && q != null) {
            onSpeak(q.word.hanzi)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            quizState.questions.isEmpty() -> {
                QuizEmptyView(onBackToList = actions.onBackToList)
            }
            quizState.isFinished -> {
                QuizFinishedView(
                    quizState = quizState,
                    onRestart = actions.onRestart,
                    onBackToList = actions.onBackToList
                )
            }
            else -> {
                quizState.currentQuestion?.let { question ->
                    QuizQuestionView(
                        question = question,
                        quizState = quizState,
                        onSelectOption = actions.onSelectOption,
                        onSpeak = onSpeak
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizEmptyView(
    onBackToList: () -> Unit
) {
    Surface(
        shape = VocabShapes.BigCard,
        color = Color.White,
        border = BorderStroke(1.dp, VocabColors.BorderLight),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Không có đủ từ vựng để luyện phản xạ",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = VocabColors.TextMuted
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onBackToList,
                colors = ButtonDefaults.buttonColors(containerColor = VocabColors.BrandDark)
            ) {
                Text("Quay về danh sách từ")
            }
        }
    }
}

@Composable
private fun QuizFinishedView(
    quizState: ReflexQuizState,
    onRestart: () -> Unit,
    onBackToList: () -> Unit
) {
    Surface(
        shape = VocabShapes.BigCard,
        color = Color.White,
        border = BorderStroke(1.dp, VocabColors.BorderLight),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color(0xFFEEF2FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_nav_trophy),
                    contentDescription = "Trophy",
                    tint = VocabColors.BrandDark,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Hoàn thành bài luyện tập!",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = VocabColors.TextDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Bạn đã hoàn thành toàn bộ 10 câu hỏi phản xạ nhanh.",
                fontSize = 13.sp,
                color = VocabColors.TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Khối thống kê kết quả
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(VocabColors.SuccessBg, RoundedCornerShape(16.dp))
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${quizState.correctCount} / ${quizState.questions.size}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = VocabColors.SuccessGreen
                        )
                        Text(
                            text = "Câu trả lời đúng",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF047857)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFEEF2FF), RoundedCornerShape(16.dp))
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "+${quizState.score}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = VocabColors.BrandDark
                        )
                        Text(
                            text = "Điểm tích lũy",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4338CA)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBackToList,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.2.dp, VocabColors.BorderLight),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Text(
                        text = "Về danh sách",
                        color = Color(0xFF334155),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = onRestart,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VocabColors.BrandDark),
                    modifier = Modifier
                        .weight(1.2f)
                        .height(44.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_refresh),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Luyện lại",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizQuestionView(
    question: ReflexQuizQuestion,
    quizState: ReflexQuizState,
    onSelectOption: (String) -> Unit,
    onSpeak: (String) -> Unit
) {
    Surface(
        shape = VocabShapes.BigCard,
        color = Color.White,
        border = BorderStroke(1.dp, VocabColors.BorderLight),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dòng Header: Câu hỏi X / N & Điểm hiện tại
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Câu hỏi ")
                        withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, color = VocabColors.TextDark)) {
                            append("${quizState.currentIndex + 1}")
                        }
                        append(" / ${quizState.questions.size}")
                    },
                    fontSize = 13.sp,
                    color = VocabColors.TextMuted
                )

                Text(
                    text = "Điểm hiện tại: ${quizState.score}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.BrandDark
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "CHỌN NGHĨA TIẾNG VIỆT CHÍNH XÁC CỦA CHỮ HÁN:",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF94A3B8),
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = question.word.hanzi,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1B4B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onSpeak(question.word.hanzi) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = question.word.pinyin,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VocabColors.BrandDark
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = "Phát âm",
                    tint = VocabColors.BrandDark,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "(Âm Hán Việt: ${question.word.hanViet})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = VocabColors.HanVietAmber,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4 Lựa chọn trắc nghiệm
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                question.options.forEach { option ->
                    QuizOptionRow(
                        option = option,
                        correctAnswer = question.correctAnswer,
                        selectedOption = quizState.selectedOption,
                        isAnswerChecked = quizState.isAnswerChecked,
                        onSelectOption = onSelectOption
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizOptionRow(
    option: String,
    correctAnswer: String,
    selectedOption: String?,
    isAnswerChecked: Boolean,
    onSelectOption: (String) -> Unit
) {
    val isThisSelected = selectedOption == option
    val isCorrectAnswer = option == correctAnswer

    val bgColor = when {
        !isAnswerChecked -> Color(0xFFFAFAFA)
        isCorrectAnswer -> VocabColors.SuccessBg
        isThisSelected -> VocabColors.ErrorBg
        else -> Color(0xFFFAFAFA)
    }

    val borderColor = when {
        !isAnswerChecked -> VocabColors.BorderLight
        isCorrectAnswer -> VocabColors.SuccessGreen
        isThisSelected -> VocabColors.ErrorRed
        else -> VocabColors.BorderLight
    }

    val textColor = when {
        !isAnswerChecked -> Color(0xFF1E293B)
        isCorrectAnswer -> Color(0xFF065F46)
        isThisSelected -> Color(0xFF991B1B)
        else -> VocabColors.TextMuted
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(VocabShapes.Option)
            .background(bgColor)
            .border(1.dp, borderColor, VocabShapes.Option)
            .clickable(enabled = !isAnswerChecked) {
                onSelectOption(option)
            }
            .padding(horizontal = 18.dp, vertical = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = option,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                modifier = Modifier.weight(1f)
            )

            if (isAnswerChecked) {
                if (isCorrectAnswer) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check_circle),
                        contentDescription = "Đúng",
                        tint = VocabColors.SuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                } else if (isThisSelected) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Sai",
                        tint = VocabColors.ErrorRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
