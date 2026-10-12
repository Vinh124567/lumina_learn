package com.example.luminalearn.presentation.lesson.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R
import com.example.luminalearn.core.util.GameSoundHelper
import com.example.luminalearn.presentation.common.CelebrationEffect
import com.example.luminalearn.ui.theme.PlusJakartaSans
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

data class BossQuestion(
    val id: Int,
    val typeTitle: String,
    val question: String,
    val pinyinHint: String? = null,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

/**
 * Dialog Bài Thi Vượt Cấp HSK (Boss Checkpoint Exam)
 * Kiểm tra toàn diện kiến thức của toàn bộ Level để cấp chứng chỉ và mở khóa Level kế tiếp.
 */
@Composable
fun BossExamDialog(
    levelName: String,
    questions: List<BossQuestion>,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onRetry: () -> Unit = {},
    onDismiss: () -> Unit,
    onPassExam: () -> Unit,
    onSpeak: ((String) -> Unit)? = null,
    onWrongAnswer: (() -> Unit)? = null
) {
    BackHandler(onBack = onDismiss)

    val haptic = LocalHapticFeedback.current

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }
    var isExamFinished by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White,
                shadowElevation = 20.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                ) {
                    // Thanh gạt Drag handle
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 40.dp, height = 4.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0))
                        )
                    }

                    if (isLoading) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "🏆", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Đang tải đề thi từ máy chủ...",
                                fontFamily = PlusJakartaSans,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4F46E5)
                            )
                        }
                    } else if (!errorMessage.isNullOrBlank() || questions.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "⚠️", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = errorMessage ?: "Chưa có đề thi cho cấp độ này trên máy chủ",
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Surface(
                                modifier = Modifier
                                    .clickable(onClick = onRetry)
                                    .padding(horizontal = 24.dp, vertical = 10.dp),
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFF4F46E5)
                            ) {
                                Text(
                                    text = "Thử lại 🔄",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                                )
                            }
                        }
                    } else if (!isExamFinished) {
                        // ── HEADER BÀI THI ──
                        BossExamHeader(
                            levelName = levelName,
                            currentIndex = currentQuestionIndex + 1,
                            totalCount = questions.size,
                            onDismiss = onDismiss
                        )

                        val currentQuestion = questions[currentQuestionIndex]
                        val progress = (currentQuestionIndex + 1).toFloat() / questions.size

                        // Thanh tiến độ bài thi
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
                            color = Color(0xFF6366F1),
                            trackColor = Color(0xFFEEF2FF),
                            strokeCap = StrokeCap.Round,
                            drawStopIndicator = {}
                        )

                        // ── NỘI DUNG CÂU HỎI ──
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Badge loại câu hỏi
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFEEF2FF),
                                border = BorderStroke(0.6.dp, Color(0xFFC7D2FE))
                            ) {
                                Text(
                                    text = currentQuestion.typeTitle,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4F46E5),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Câu hỏi lớn
                            Text(
                                text = currentQuestion.question,
                                fontFamily = PlusJakartaSans,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0F172A),
                                lineHeight = 28.sp
                            )

                            if (!currentQuestion.pinyinHint.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = currentQuestion.pinyinHint,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF6366F1)
                                    )
                                    if (onSpeak != null) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFFEEF2FF),
                                            modifier = Modifier
                                                .size(26.dp)
                                                .clickable { onSpeak(currentQuestion.question) }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_speaker),
                                                    contentDescription = "Nghe phát âm",
                                                    tint = Color(0xFF6366F1),
                                                    modifier = Modifier.size(13.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Danh sách 4 đáp án lựa chọn
                            currentQuestion.options.forEachIndexed { optIndex, optionText ->
                                val isSelected = selectedOptionIndex == optIndex
                                val isCorrect = optIndex == currentQuestion.correctIndex

                                val (cardBg, cardBorder, textCol) = when {
                                    isAnswerSubmitted && isCorrect -> Triple(
                                        Color(0xFFDCFCE7),
                                        Color(0xFF22C55E),
                                        Color(0xFF15803D)
                                    )
                                    isAnswerSubmitted && isSelected && !isCorrect -> Triple(
                                        Color(0xFFFEE2E2),
                                        Color(0xFFEF4444),
                                        Color(0xFFB91C1C)
                                    )
                                    isSelected -> Triple(
                                        Color(0xFFEEF2FF),
                                        Color(0xFF6366F1),
                                        Color(0xFF4338CA)
                                    )
                                    else -> Triple(
                                        Color(0xFFF8FAFC),
                                        Color(0xFFE2E8F0),
                                        Color(0xFF1E293B)
                                    )
                                }

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                        .clickable(enabled = !isAnswerSubmitted) {
                                            selectedOptionIndex = optIndex
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    color = cardBg,
                                    border = BorderStroke(if (isSelected || (isAnswerSubmitted && isCorrect)) 1.5.dp else 0.8.dp, cardBorder),
                                    shadowElevation = if (isSelected) 2.dp else 0.dp
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val prefixLetter = ('A' + optIndex).toString()
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isSelected || (isAnswerSubmitted && isCorrect)) cardBorder else Color(0xFFE2E8F0)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = prefixLetter,
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected || (isAnswerSubmitted && isCorrect)) Color.White else Color(0xFF64748B)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Text(
                                            text = optionText,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 14.5.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = textCol,
                                            modifier = Modifier.weight(1f)
                                        )

                                        if (isAnswerSubmitted) {
                                            if (isCorrect) {
                                                Text(text = "✓", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF15803D))
                                            } else if (isSelected) {
                                                Text(text = "✗", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFB91C1C))
                                            }
                                        }
                                    }
                                }
                            }

                            // Giải thích đáp án sau khi bấm nộp
                            if (isAnswerSubmitted && currentQuestion.explanation.isNotBlank()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFF1F5F9),
                                    border = BorderStroke(0.6.dp, Color(0xFFCBD5E1))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "💡 Giải thích:",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF334155)
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = currentQuestion.explanation,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 12.5.sp,
                                            color = Color(0xFF475569),
                                            lineHeight = 17.sp
                                        )
                                    }
                                }
                            }
                        }

                        // ── THANH HÀNH ĐỘNG ĐÁY ──
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            shadowElevation = 8.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 12.dp)
                            ) {
                                if (!isAnswerSubmitted) {
                                    val isEnabled = selectedOptionIndex >= 0
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .clickable(enabled = isEnabled) {
                                                isAnswerSubmitted = true
                                                if (selectedOptionIndex == currentQuestion.correctIndex) {
                                                    correctAnswersCount++
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    GameSoundHelper.playCorrect()
                                                } else {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    GameSoundHelper.playIncorrect()
                                                    onWrongAnswer?.invoke()
                                                }
                                            },
                                        shape = RoundedCornerShape(24.dp),
                                        color = if (isEnabled) Color(0xFF4F46E5) else Color(0xFFCBD5E1),
                                        shadowElevation = if (isEnabled) 3.dp else 0.dp
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "Kiểm tra đáp án",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 14.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                } else {
                                    val isLastQuestion = currentQuestionIndex >= questions.size - 1
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .clickable {
                                                if (isLastQuestion) {
                                                    isExamFinished = true
                                                } else {
                                                    currentQuestionIndex++
                                                    selectedOptionIndex = -1
                                                    isAnswerSubmitted = false
                                                }
                                            },
                                        shape = RoundedCornerShape(24.dp),
                                        color = if (isLastQuestion) Color(0xFFD97706) else Color(0xFF4F46E5),
                                        shadowElevation = 3.dp
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = if (isLastQuestion) "Xem kết quả thi 🏆" else "Câu tiếp theo →",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 14.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // ── MÀN HÌNH KẾT QUẢ THI (RESULT SUMMARY) ──
                        val scorePercent = ((correctAnswersCount.toFloat() / questions.size) * 100).toInt()
                        val isPassed = scorePercent >= 80

                        BossExamResultView(
                            levelName = levelName,
                            scorePercent = scorePercent,
                            correctCount = correctAnswersCount,
                            totalCount = questions.size,
                            isPassed = isPassed,
                            onRetry = {
                                currentQuestionIndex = 0
                                selectedOptionIndex = -1
                                isAnswerSubmitted = false
                                correctAnswersCount = 0
                                isExamFinished = false
                            },
                            onClose = onDismiss,
                            onPassAndUnlock = {
                                onPassExam()
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BossExamHeader(
    levelName: String,
    currentIndex: Int,
    totalCount: Int,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "🏆", fontSize = 20.sp)
            Column {
                Text(
                    text = "Vượt Cấp $levelName",
                    fontFamily = PlusJakartaSans,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "Câu $currentIndex/$totalCount",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF6366F1)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F9))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Đóng",
                tint = Color(0xFF64748B),
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

@Composable
private fun BossExamResultView(
    levelName: String,
    scorePercent: Int,
    correctCount: Int,
    totalCount: Int,
    isPassed: Boolean,
    onRetry: () -> Unit,
    onClose: () -> Unit,
    onPassAndUnlock: () -> Unit
) {
    androidx.compose.runtime.LaunchedEffect(isPassed) {
        if (isPassed) {
            GameSoundHelper.playVictoryFanfare()
        } else {
            GameSoundHelper.playIncorrect()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Biểu tượng Cúp Vàng hoặc Huy hiệu cố gắng
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPassed) {
                            Brush.radialGradient(listOf(Color(0xFFFEF08A), Color(0xFFFDE047)))
                        } else {
                            Brush.radialGradient(listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0)))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isPassed) "🏆" else "💪",
                    fontSize = 44.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = if (isPassed) "Chúc Mừng Vượt Cấp $levelName Thành Công!" else "Chưa Đạt Chuẩn Vượt Cấp $levelName",
                fontFamily = PlusJakartaSans,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isPassed)
                    "Bạn đã xuất sắc vượt qua bài thi chuẩn hóa và sẵn sàng chinh phục các nấc thang cao hơn!"
                else
                    "Bạn cần đạt từ 80% trở lên (hiện đạt $scorePercent%). Hãy xem lại sổ tay hoặc ôn lại các bài học để thử sức lại nhé!",
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Khung thống kê điểm số
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(0.8.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$scorePercent%",
                            fontFamily = PlusJakartaSans,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isPassed) Color(0xFF16A34A) else Color(0xFFEA580C)
                        )
                        Text(
                            text = "Độ chính xác",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(32.dp)
                            .background(Color(0xFFE2E8F0))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$correctCount/$totalCount",
                            fontFamily = PlusJakartaSans,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF4F46E5)
                        )
                        Text(
                            text = "Câu đúng",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    }

                    if (isPassed) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(Color(0xFFE2E8F0))
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "+200",
                                fontFamily = PlusJakartaSans,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFD97706)
                            )
                            Text(
                                text = "Tia Sáng Sparks",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Các nút hành động
            if (isPassed) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable(onClick = onPassAndUnlock),
                    shape = RoundedCornerShape(25.dp),
                    color = Color(0xFF4F46E5),
                    shadowElevation = 3.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Nhận thưởng & Mở khóa cấp độ tiếp theo 🎉",
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable(onClick = onRetry),
                    shape = RoundedCornerShape(25.dp),
                    color = Color(0xFF4F46E5),
                    shadowElevation = 3.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Làm lại bài thi ngay 🔄",
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable(onClick = onClose),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFF1F5F9),
                    border = BorderStroke(0.6.dp, Color(0xFFE2E8F0))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Ôn tập lại các bài học",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569)
                        )
                    }
                }
            }
        }

        // Bắn pháo hoa rực rỡ khi vượt cấp thành công
        if (isPassed) {
            CelebrationEffect(
                triggerKey = 2,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
