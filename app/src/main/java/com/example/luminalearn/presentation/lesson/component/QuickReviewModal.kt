package com.example.luminalearn.presentation.lesson.component

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.core.util.GameSoundHelper
import com.example.luminalearn.data.model.QuickReviewQuestionDto
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelSegmentedProgressBar
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Modal Ôn tập nhanh Cắt đứt quên lãng (Spaced Repetition Quick Review).
 * Phong cách Retro 16-Bit Pixel Arcade Console: viền đen 2dp, bóng đổ cứng 3D, phím dập nổi.
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
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            PixelCardBox(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                headerTitle = "⚡ QUICK REVIEW • ÔN TẬP",
                headerColor = PixelColors.ArcadeAmber
            ) {
                // Nút đóng pixel X ở góc trên
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .offset(y = (-4).dp)
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(PixelColors.SurfaceDark)
                            .border(1.5.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                            .bounceClick { onDismiss() },
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

                if (isLoading) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 28.dp)
                    ) {
                        CircularProgressIndicator(
                            color = PixelColors.ArcadeAmber,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "ĐANG TẢI DỮ LIỆU TỪ SERVER...",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.TextMuted,
                            letterSpacing = 0.5.sp
                        )
                    }
                } else if (errorMessage != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Text(text = "⚠️", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.ArcadeRed,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PixelArcadeButton(
                            text = "ĐÓNG",
                            onClick = onDismiss,
                            color = PixelColors.SurfaceDark,
                            height = 42.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else if (questions.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Text(text = "🎉", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Bài học này hiện chưa có nội dung cần ôn tập thêm!",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.TextDark,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PixelArcadeButton(
                            text = "QUAY LẠI",
                            onClick = onDismiss,
                            color = PixelColors.ArcadeBlue,
                            height = 42.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (!isFinished) {
                            // Header tiến độ dạng 16-bit
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                PixelBadge(
                                    text = "LEVEL QUEST",
                                    color = PixelColors.ArcadeAmber,
                                    textColor = Color.White
                                )
                                Text(
                                    text = "CÂU ${currentIndex + 1}/${questions.size}",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.BorderDark
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Thanh tiến độ phân đoạn Pixel
                            PixelSegmentedProgressBar(
                                current = currentIndex + 1,
                                total = questions.size,
                                activeColor = PixelColors.ArcadeAmber,
                                height = 8.dp,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            val currentQ = questions[currentIndex]

                            // Thẻ Hán tự & Pinyin phong cách Pixel Cartridge
                            PixelCardBox(
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = Color(0xFFFFFBEB),
                                borderColor = PixelColors.BorderDark,
                                shadowColor = PixelColors.ShadowDark,
                                cornerRadius = 12.dp
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 12.dp)
                                ) {
                                    Text(
                                        text = currentQ.hanzi,
                                        fontSize = 34.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFB45309)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = currentQ.pinyin,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PixelColors.ArcadeAmber
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = currentQ.question,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = PixelColors.TextDark,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 4 Đáp án lựa chọn dạng phím Arcade
                            currentQ.options.forEachIndexed { optIndex, optText ->
                                val isSelected = selectedOption == optIndex
                                val isCorrect = optIndex == currentQ.correctIndex

                                val (btnColor: Color, textColor: Color) = when {
                                    !isAnswerSubmitted && isSelected -> Pair(PixelColors.ArcadeAmber, Color.White)
                                    isAnswerSubmitted && isCorrect -> Pair(PixelColors.ArcadeGreen, Color.White)
                                    isAnswerSubmitted && isSelected && !isCorrect -> Pair(PixelColors.ArcadeRed, Color.White)
                                    else -> Pair(Color(0xFFF8FAFC), PixelColors.TextDark)
                                }

                                val prefixLetter = ('A' + optIndex).toString()

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(PixelColors.ShadowDark)
                                        .padding(bottom = 2.5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(btnColor)
                                            .border(1.8.dp, PixelColors.BorderDark, RoundedCornerShape(8.dp))
                                            .bounceClick(scaleDown = 0.97f) {
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
                                            .padding(horizontal = 14.dp, vertical = 11.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            // Badge ký tự A, B, C, D
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(if (isSelected || (isAnswerSubmitted && isCorrect)) Color.White.copy(alpha = 0.25f) else Color(0xFFE2E8F0))
                                                    .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(4.dp)),
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
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = optText,
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 13.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = textColor
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Nút chuyển tiếp
                            if (isAnswerSubmitted) {
                                PixelArcadeButton(
                                    text = if (currentIndex < questions.size - 1) "CÂU TIẾP THEO ▶" else "XEM KẾT QUẢ 🏆",
                                    onClick = {
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
                                    },
                                    color = PixelColors.ArcadeAmber,
                                    height = 46.dp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
                            // Màn hình hoàn thành ôn tập Arcade
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(text = "✨", fontSize = 44.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "★ HÀN GẮN TRẠM THÀNH CÔNG ★",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.ArcadeAmber,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Đã củng cố chính xác $correctCount/${questions.size} câu hỏi vào trí nhớ dài hạn!",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.5.sp,
                                    color = PixelColors.TextMuted,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Khung phần thưởng Pixel
                                PixelCardBox(
                                    modifier = Modifier.fillMaxWidth(),
                                    backgroundColor = Color(0xFFFEF3C7),
                                    borderColor = PixelColors.BorderDark,
                                    shadowColor = PixelColors.ShadowDark,
                                    cornerRadius = 10.dp
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "+15 ✨",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFFD97706)
                                            )
                                            Text(
                                                text = "Sparks Thưởng",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 11.sp,
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
                                                text = "⭐ 3/3",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFFD97706)
                                            )
                                            Text(
                                                text = "Hoàn Hảo",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PixelColors.TextMuted
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                PixelArcadeButton(
                                    text = "✔ HOÀN TẤT & LƯU TIẾN ĐỘ",
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onCompleted(lesson.id)
                                    },
                                    color = PixelColors.ArcadeGreen,
                                    height = 48.dp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
