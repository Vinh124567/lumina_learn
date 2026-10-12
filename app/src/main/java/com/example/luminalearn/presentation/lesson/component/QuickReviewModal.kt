package com.example.luminalearn.presentation.lesson.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.core.util.GameSoundHelper
import androidx.compose.material3.CircularProgressIndicator
import com.example.luminalearn.data.model.QuickReviewQuestionDto
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val BrandAmber = Color(0xFFF59E0B)
private val BrandAmberLight = Color(0xFFFEF3C7)
private val BrandIndigo = Color(0xFF4F46E5)
private val SuccessGreen = Color(0xFF10B981)
private val ErrorRed = Color(0xFFEF4444)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)

/**
 * Modal Ôn tập nhanh Cắt đứt quên lãng (Spaced Repetition Quick Review).
 * Tải 100% câu hỏi từ Backend và đồng bộ lưu kết quả về DB máy chủ.
 */
@Composable
fun QuickReviewModal(
    lesson: ChineseLessonData,
    questions: List<QuickReviewQuestionDto> = emptyList(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onCompleted: (lessonId: String) -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .border(BorderStroke(1.5.dp, Color(0xFFFDE68A)), RoundedCornerShape(28.dp))
                .padding(24.dp)
        ) {
            if (isLoading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                ) {
                    CircularProgressIndicator(
                        color = BrandAmber,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Đang tải câu hỏi ôn tập từ máy chủ...",
                        fontFamily = PlusJakartaSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted
                    )
                }
            } else if (errorMessage != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Text(text = "⚠️", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage,
                        fontFamily = PlusJakartaSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ErrorRed,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .bounceClick { onDismiss() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Đóng",
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                        }
                    }
                }
            } else if (questions.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Text(text = "🎉", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Bài học này hiện chưa có nội dung cần ôn tập thêm!",
                        fontFamily = PlusJakartaSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMain,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = BrandIndigo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .bounceClick { onDismiss() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Quay lại",
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                if (!isFinished) {
                    // Header tiến độ ôn tập
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⚡", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ÔN TẬP NHANH",
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = BrandAmber,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Text(
                            text = "${currentIndex + 1}/${questions.size}",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { (currentIndex + 1).toFloat() / questions.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = BrandAmber,
                        trackColor = Color(0xFFFEF3C7)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    val currentQ = questions[currentIndex]

                    // Card hiển thị Hán tự & Pinyin
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFFFFBEB),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 18.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = currentQ.hanzi,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFB45309)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentQ.pinyin,
                                fontFamily = PlusJakartaSans,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFD97706)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = currentQ.question,
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Các lựa chọn trắc nghiệm
                    currentQ.options.forEachIndexed { optIndex, optText ->
                        val isSelected = selectedOption == optIndex
                        val isCorrect = optIndex == currentQ.correctIndex

                        val (borderCol, bgCol, textCol) = when {
                            !isAnswerSubmitted && isSelected -> Triple(BrandAmber, BrandAmberLight, Color(0xFF92400E))
                            isAnswerSubmitted && isCorrect -> Triple(SuccessGreen, Color(0xFFECFDF5), SuccessGreen)
                            isAnswerSubmitted && isSelected && !isCorrect -> Triple(ErrorRed, Color(0xFFFEF2F2), ErrorRed)
                            else -> Triple(Color(0xFFE2E8F0), Color(0xFFF8FAFC), TextMain)
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = bgCol,
                            border = BorderStroke(1.2.dp, borderCol),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .bounceClick(scaleDown = 0.96f) {
                                    if (!isAnswerSubmitted) {
                                        selectedOption = optIndex
                                        isAnswerSubmitted = true
                                        if (isCorrect) {
                                            correctCount++
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            GameSoundHelper.playCorrect()
                                        } else {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            GameSoundHelper.playIncorrect()
                                        }
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = ('A' + optIndex).toString(),
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = textCol
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = optText,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textCol
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Nút chuyển tiếp
                    if (isAnswerSubmitted) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = BrandAmber,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .bounceClick {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    GameSoundHelper.playClick()
                                    if (currentIndex < questions.size - 1) {
                                        currentIndex++
                                        selectedOption = null
                                        isAnswerSubmitted = false
                                    } else {
                                        isFinished = true
                                        GameSoundHelper.playVictoryFanfare()
                                    }
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (currentIndex < questions.size - 1) "CÂU TIẾP THEO ➔" else "XEM KẾT QUẢ 🏆",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    // Màn hình hoàn thành ôn tập
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                    ) {
                        Text(text = "✨", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "HÀN GẮN TRẠM THÀNH CÔNG!",
                            fontFamily = PlusJakartaSans,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFD97706),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Bạn đã ôn tập chính xác $correctCount/${questions.size} câu. Kiến thức đã được củng cố vào trí nhớ dài hạn!",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Box phần thưởng
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0xFFFEF3C7),
                            border = BorderStroke(1.2.dp, Color(0xFFFDE68A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "+15 ✨", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFD97706))
                                    Text(text = "Sparks Thưởng", fontSize = 11.sp, color = TextMuted)
                                }
                                Text(text = "⭐ 3/3", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFD97706))
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = BrandIndigo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .bounceClick {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onCompleted(lesson.id)
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "HOÀN TẤT & LƯU TIẾN ĐỘ",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                }
            }
        }
    }
}
