package com.example.luminalearn.presentation.lesson.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.core.util.GameSoundHelper
import com.example.luminalearn.presentation.common.CelebrationEffect
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelSegmentedProgressBar
import com.example.luminalearn.ui.theme.PlusJakartaSans

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
 * Phong cách Retro 16-Bit Pixel Arcade / RPG Boss Fight: Viền đen 2dp, bóng cứng 3D, phím dập nổi.
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
                .background(Color.Black.copy(alpha = 0.65f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.94f)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(PixelColors.ShadowDark)
                    .padding(top = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .background(PixelColors.SurfaceLight)
                        .border(
                            width = 2.dp,
                            color = PixelColors.BorderDark,
                            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                    ) {
                        // ── 1. HEADER ARCADE CARTRIDGE ──
                        BossExamHeader(
                            levelName = levelName,
                            currentIndex = currentQuestionIndex + 1,
                            totalCount = questions.size,
                            onDismiss = onDismiss
                        )

                        if (isLoading) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = PixelColors.ArcadePurple,
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "ĐANG TẢI ĐỀ THI TỪ MÁY CHỦ...",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.ArcadePurple,
                                    letterSpacing = 0.5.sp
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
                                Text(text = "⚠️", fontSize = 44.sp)
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = errorMessage ?: "Chưa có đề thi cho cấp độ này trên máy chủ",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PixelColors.TextDark,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                PixelArcadeButton(
                                    text = "THỬ LẠI 🔄",
                                    onClick = onRetry,
                                    color = PixelColors.ArcadePurple,
                                    height = 44.dp
                                )
                            }
                        } else if (!isExamFinished) {
                            val currentQuestion = questions[currentQuestionIndex]

                            // Thanh tiến độ Pixel phân đoạn
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                PixelSegmentedProgressBar(
                                    current = currentQuestionIndex + 1,
                                    total = questions.size,
                                    activeColor = PixelColors.ArcadePurple,
                                    height = 8.dp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            // ── 2. NỘI DUNG CÂU HỎI ──
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                // Badge loại câu hỏi
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    PixelBadge(
                                        text = "⚔️ " + currentQuestion.typeTitle.uppercase(),
                                        color = PixelColors.ArcadePurple,
                                        textColor = Color.White
                                    )
                                    PixelBadge(
                                        text = "👑 BOSS QUEST",
                                        color = PixelColors.ArcadeAmber,
                                        textColor = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Khung câu hỏi chính Pixel (Card lớn có 4 đinh tán ở các góc)
                                PixelCardBox(
                                    modifier = Modifier.fillMaxWidth(),
                                    backgroundColor = Color(0xFFFAF5FF),
                                    borderColor = PixelColors.BorderDark,
                                    shadowColor = PixelColors.ShadowDark,
                                    cornerRadius = 12.dp,
                                    showRivets = true
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp)
                                    ) {
                                        Text(
                                            text = currentQuestion.question,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 19.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PixelColors.TextDark,
                                            lineHeight = 26.sp
                                        )

                                        if (!currentQuestion.pinyinHint.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = currentQuestion.pinyinHint,
                                                    fontFamily = PlusJakartaSans,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = PixelColors.ArcadePurple
                                                )
                                                if (onSpeak != null) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(PixelColors.SurfaceDark)
                                                            .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                                                            .bounceClick { onSpeak(currentQuestion.question) },
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(text = "🔊", fontSize = 12.sp)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Danh sách 4 đáp án Arcade Keys
                                currentQuestion.options.forEachIndexed { optIndex, optionText ->
                                    val isSelected = selectedOptionIndex == optIndex
                                    val isCorrect = optIndex == currentQuestion.correctIndex

                                    val (keyBgColor, keyTextColor) = when {
                                        isAnswerSubmitted && isCorrect -> Pair(PixelColors.ArcadeGreen, Color.White)
                                        isAnswerSubmitted && isSelected && !isCorrect -> Pair(PixelColors.ArcadeRed, Color.White)
                                        isSelected -> Pair(PixelColors.ArcadePurple, Color.White)
                                        else -> Pair(Color.White, PixelColors.TextDark)
                                    }

                                    val prefixLetter = ('A' + optIndex).toString()

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.5.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(PixelColors.ShadowDark)
                                            .padding(bottom = 2.5.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(keyBgColor)
                                                .border(1.8.dp, PixelColors.BorderDark, RoundedCornerShape(8.dp))
                                                .clickable(enabled = !isAnswerSubmitted) {
                                                    selectedOptionIndex = optIndex
                                                }
                                                .padding(horizontal = 14.dp, vertical = 12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                // Nút chữ cái A, B, C, D
                                                Box(
                                                    modifier = Modifier
                                                        .size(26.dp)
                                                        .clip(RoundedCornerShape(5.dp))
                                                        .background(if (isSelected || (isAnswerSubmitted && isCorrect)) Color.White.copy(alpha = 0.25f) else Color(0xFFF1F5F9))
                                                        .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(5.dp)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = prefixLetter,
                                                        fontFamily = PlusJakartaSans,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = if (isSelected || (isAnswerSubmitted && isCorrect)) Color.White else PixelColors.TextDark
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(12.dp))

                                                Text(
                                                    text = optionText,
                                                    fontFamily = PlusJakartaSans,
                                                    fontSize = 14.5.sp,
                                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                                    color = keyTextColor,
                                                    modifier = Modifier.weight(1f)
                                                )

                                                if (isAnswerSubmitted) {
                                                    if (isCorrect) {
                                                        Text(text = "✓", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                                                    } else if (isSelected) {
                                                        Text(text = "✗", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // Khung giải thích pixel sau khi nộp bài
                                if (isAnswerSubmitted && currentQuestion.explanation.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    PixelCardBox(
                                        modifier = Modifier.fillMaxWidth(),
                                        backgroundColor = Color(0xFFFEF3C7),
                                        borderColor = PixelColors.BorderDark,
                                        shadowColor = PixelColors.ShadowDark,
                                        cornerRadius = 10.dp
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(text = "💡", fontSize = 14.sp)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "GIẢI THÍCH CHI TIẾT:",
                                                    fontFamily = PlusJakartaSans,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFF92400E)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = currentQuestion.explanation,
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF78350F),
                                                lineHeight = 18.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // ── 3. THANH HÀNH ĐỘNG DƯỚI ĐÁY ──
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(PixelColors.SurfaceLight)
                                    .border(
                                        width = 1.5.dp,
                                        color = PixelColors.BorderDark,
                                        shape = RoundedCornerShape(0.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                if (!isAnswerSubmitted) {
                                    val isEnabled = selectedOptionIndex >= 0
                                    PixelArcadeButton(
                                        text = "KIỂM TRA ĐÁP ÁN ▶",
                                        onClick = {
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
                                        color = if (isEnabled) PixelColors.ArcadePurple else PixelColors.SurfaceDark,
                                        height = 48.dp,
                                        fillMaxWidth = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    val isLastQuestion = currentQuestionIndex >= questions.size - 1
                                    PixelArcadeButton(
                                        text = if (isLastQuestion) "XEM KẾT QUẢ THI 🏆" else "CÂU TIẾP THEO ▶",
                                        onClick = {
                                            if (isLastQuestion) {
                                                isExamFinished = true
                                            } else {
                                                currentQuestionIndex++
                                                selectedOptionIndex = -1
                                                isAnswerSubmitted = false
                                            }
                                        },
                                        color = if (isLastQuestion) PixelColors.ArcadeAmber else PixelColors.ArcadePurple,
                                        height = 48.dp,
                                        fillMaxWidth = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        } else {
                            // ── 4. MÀN HÌNH KẾT QUẢ THI RETRO BOSS VICTORY / DEFEAT ──
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
}

@Composable
private fun BossExamHeader(
    levelName: String,
    currentIndex: Int,
    totalCount: Int,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(PixelColors.SurfaceDark)
            .border(
                width = 1.5.dp,
                color = PixelColors.BorderDark,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "⚔️", fontSize = 22.sp)
                Column {
                    Text(
                        text = "VƯỢT CẤP $levelName",
                        fontFamily = PlusJakartaSans,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.TextLight
                    )
                    Text(
                        text = "TIẾN TRÌNH: CÂU $currentIndex/$totalCount",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.ArcadeAmber
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                    .bounceClick(onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✕",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
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
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Biểu tượng pixel
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isPassed) PixelColors.ArcadeAmber else PixelColors.SurfaceDark)
                    .border(2.dp, PixelColors.BorderDark, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isPassed) "🏆" else "💀",
                    fontSize = 44.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isPassed) "★ VƯỢT CẤP $levelName THÀNH CÔNG! ★" else "CHƯA ĐẠT CHUẨN $levelName",
                fontFamily = PlusJakartaSans,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = if (isPassed) PixelColors.ArcadeAmber else PixelColors.ArcadeRed,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isPassed)
                    "Bạn đã xuất sắc đánh bại Boss đợt này và mở khóa chặng đường tiếp theo!"
                else
                    "Bạn cần đạt tối thiểu 80% để qua bài (hiện đạt $scorePercent%). Hãy ôn luyện lại sổ tay và thử sức lại nhé!",
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                color = PixelColors.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Bảng điểm Pixel RPG
            PixelCardBox(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                headerTitle = "BẢNG TỔNG KẾT THI",
                headerColor = if (isPassed) PixelColors.ArcadeGreen else PixelColors.SurfaceDark
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$scorePercent%",
                            fontFamily = PlusJakartaSans,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isPassed) PixelColors.ArcadeGreen else PixelColors.ArcadeRed
                        )
                        Text(
                            text = "ĐỘ CHUẨN",
                            fontFamily = PlusJakartaSans,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.5.dp)
                            .height(28.dp)
                            .background(PixelColors.BorderDark)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$correctCount/$totalCount",
                            fontFamily = PlusJakartaSans,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.ArcadePurple
                        )
                        Text(
                            text = "CÂU ĐÚNG",
                            fontFamily = PlusJakartaSans,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.TextMuted
                        )
                    }

                    if (isPassed) {
                        Box(
                            modifier = Modifier
                                .width(1.5.dp)
                                .height(28.dp)
                                .background(PixelColors.BorderDark)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "+200 ✨",
                                fontFamily = PlusJakartaSans,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.ArcadeAmber
                            )
                            Text(
                                text = "SPARKS",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PixelColors.TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nút hành động
            if (isPassed) {
                PixelArcadeButton(
                    text = "🎁 NHẬN THƯỞNG & MỞ KHÓA NGAY",
                    onClick = onPassAndUnlock,
                    color = PixelColors.ArcadeAmber,
                    height = 50.dp,
                    fillMaxWidth = true,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                PixelArcadeButton(
                    text = "🔄 THỬ THÁCH LẠI NGAY",
                    onClick = onRetry,
                    color = PixelColors.ArcadePurple,
                    height = 50.dp,
                    fillMaxWidth = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                PixelArcadeButton(
                    text = "ÔN TẬP LẠI BÀI HỌC",
                    onClick = onClose,
                    color = PixelColors.SurfaceDark,
                    height = 44.dp,
                    fillMaxWidth = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (isPassed) {
            CelebrationEffect(
                triggerKey = 2,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
