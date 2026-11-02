package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.data.model.HeartStatusDto
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Modal hiển thị trạng thái Trái Tim & Nạp Năng Lượng (Hearts & Energy).
 * Phong cách Retro 16-Bit Pixel RPG Console.
 */
@Composable
fun HeartRefillModal(
    heartStatus: HeartStatusDto,
    onRefillWithSparks: () -> Unit,
    onPracticeToEarn: () -> Unit,
    onDismiss: () -> Unit
) {
    val isFull = heartStatus.currentHearts >= heartStatus.maxHearts
    val heartsNeeded = (heartStatus.maxHearts - heartStatus.currentHearts).coerceAtLeast(0)

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
                borderWidth = 2.dp,
                shadowOffset = 4.dp,
                headerTitle = "HP RECOVERY • HỒI TIM",
                headerBadge = "${heartStatus.currentHearts}/${heartStatus.maxHearts} HP",
                headerColor = PixelColors.PixelRed,
                showGrille = true,
                showRivets = false
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {
                    // Header Icon quả tim pixel
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PixelColors.RetroCream)
                            .border(BorderStroke(2.dp, PixelColors.BorderDark), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "❤️", fontSize = 34.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${heartStatus.currentHearts} / ${heartStatus.maxHearts} TRÁI TIM HP",
                        fontFamily = PlusJakartaSans,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isFull) {
                            "Năng lượng của bạn đang dồi dào! Bạn có thể học bài và làm bài tập không giới hạn."
                        } else {
                            "Mỗi câu trả lời sai sẽ tiêu hao 1 tim. Hãy nạp năng lượng để tiếp tục hành trình nhé!"
                        },
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        color = PixelColors.MutedText,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp
                    )

                    // Badge đếm ngược hồi tim tự động
                    if (!isFull && heartStatus.nextRefillMinutes > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        PixelBadge(
                            text = "⏱️ TỰ HỒI 1 TIM SAU ${heartStatus.nextRefillMinutes} PHÚT",
                            backgroundColor = PixelColors.CardOrange,
                            textColor = PixelColors.PixelOrange,
                            borderColor = PixelColors.BorderDark
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dãy 5 ô tim pixel
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..heartStatus.maxHearts) {
                            val isFilled = i <= heartStatus.currentHearts
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isFilled) PixelColors.CardYellow else Color(0xFFF1F5F9))
                                    .border(
                                        BorderStroke(1.8.dp, PixelColors.BorderDark),
                                        RoundedCornerShape(6.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isFilled) "❤️" else "🖤",
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Nút Arcade Nạp đầy bằng Sparks
                    PixelArcadeButton(
                        text = if (isFull) "NĂNG LƯỢNG ĐÃ ĐẦY (5/5 ❤️)" else "⚡ NẠP ĐẦY (+$heartsNeeded ❤️) • -${heartStatus.sparksRefillCost} SPK",
                        onClick = {
                            if (!isFull) onRefillWithSparks()
                        },
                        color = if (isFull) Color(0xFFCBD5E1) else PixelColors.PixelYellow,
                        textColor = if (isFull) PixelColors.MutedText else PixelColors.BorderDark,
                        fontSize = 12f,
                        contentPadding = 9.dp,
                        shadowOffset = 2.5.dp,
                        fillMaxWidth = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Nút Arcade Ôn tập bài cũ để hồi tim miễn phí
                    PixelArcadeButton(
                        text = if (isFull) "📖 LUYỆN TẬP ÔN BÀI CŨ" else "📖 ÔN TẬP ĐỂ HỒI TIM MIỄN PHÍ",
                        onClick = onPracticeToEarn,
                        color = PixelColors.PixelGreen,
                        textColor = Color.White,
                        fontSize = 12f,
                        contentPadding = 9.dp,
                        shadowOffset = 2.5.dp,
                        fillMaxWidth = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Nút Đóng
                    PixelArcadeButton(
                        text = "ĐỂ SAU",
                        onClick = onDismiss,
                        color = Color.White,
                        textColor = PixelColors.MutedText,
                        fontSize = 11.5f,
                        contentPadding = 7.dp,
                        shadowOffset = 2.dp,
                        fillMaxWidth = true
                    )
                }
            }
        }
    }
}
