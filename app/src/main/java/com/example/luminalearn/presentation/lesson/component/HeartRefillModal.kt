package com.example.luminalearn.presentation.lesson.component

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
import androidx.compose.material3.Surface
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
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.data.model.HeartStatusDto
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val HeartRed = Color(0xFFEF4444)
private val HeartRedLight = Color(0xFFFEE2E2)
private val BrandIndigo = Color(0xFF4F46E5)
private val BrandAmber = Color(0xFFF59E0B)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderHairline = Color(0xFFE2E8F0)

/**
 * Modal hiển thị trạng thái Trái Tim & Nạp Năng Lượng (Hearts & Energy).
 * Chuẩn phong cách iOS Luxury Mobile: Hairline 0.5dp, borderless contrast, soft elevation.
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
                .clip(RoundedCornerShape(26.dp))
                .background(Color.White)
                .border(BorderStroke(0.5.dp, Color(0xFFFECDD3).copy(alpha = 0.6f)), RoundedCornerShape(26.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header Icon quả tim với nền hào quang pastel
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(HeartRedLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "❤️", fontSize = 34.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "${heartStatus.currentHearts} / ${heartStatus.maxHearts} TRÁI TIM",
                    fontFamily = PlusJakartaSans,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = HeartRed,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isFull) {
                        "Năng lượng của bạn đang dồi dào! Bạn có thể học bài và làm bài tập không giới hạn."
                    } else {
                        "Mỗi câu trả lời sai sẽ tiêu hao 1 tim. Hãy nạp năng lượng để tiếp tục hành trình nhé!"
                    },
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                // Badge đếm ngược hồi tim tự động
                if (!isFull && heartStatus.nextRefillMinutes > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFFFF1F2))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "⏱️ Tự hồi 1 tim sau ${heartStatus.nextRefillMinutes} phút nữa",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = HeartRed
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Dãy 5 quả tim trực quan với hiệu ứng Hairline
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..heartStatus.maxHearts) {
                        val isFilled = i <= heartStatus.currentHearts
                        Surface(
                            shape = CircleShape,
                            color = if (isFilled) HeartRedLight else Color(0xFFF8FAFC),
                            border = BorderStroke(0.5.dp, if (isFilled) HeartRed.copy(alpha = 0.6f) else BorderHairline.copy(alpha = 0.8f)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isFilled) "❤️" else "🖤",
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nút Nạp đầy bằng Sparks (Tự động disable khi tim đã đầy 5/5)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isFull) Color(0xFFF1F5F9) else BrandAmber,
                    shadowElevation = if (isFull) 0.dp else 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .then(if (isFull) Modifier else Modifier.bounceClick { onRefillWithSparks() })
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isFull) {
                            Text(
                                text = "NĂNG LƯỢNG ĐÃ ĐẦY (5/5 ❤️)",
                                fontFamily = PlusJakartaSans,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "NẠP ĐẦY NGAY (+$heartsNeeded ❤️)",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(-${heartStatus.sparksRefillCost} ✨)",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Nút Ôn tập bài cũ để hồi tim miễn phí
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BrandIndigo,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .bounceClick {
                            onPracticeToEarn()
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isFull) "LUYỆN TẬP ÔN BÀI CŨ ⚡" else "ÔN TẬP ĐỂ HỒI TIM MIỄN PHÍ ⚡",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Nút Đóng
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(0.5.dp, BorderHairline.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .bounceClick { onDismiss() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Để sau",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}
