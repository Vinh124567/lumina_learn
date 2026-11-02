package com.example.luminalearn.presentation.lesson.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.core.util.GameSoundHelper
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

/**
 * Dialog mở rương nhận quà khi học viên hoàn thành Unit trên lộ trình.
 * Phong cách Retro 16-Bit Pixel RPG Victory Chest.
 */
@Composable
fun ChestRewardDialog(
    unitNumber: Int,
    sparksEarned: Int = 50,
    expEarned: Int = 100,
    onClaim: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        GameSoundHelper.playChestOpen()
    }
    val scaleAnim = remember { Animatable(0.7f) }
    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "chestAura")
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraAlpha"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .scale(scaleAnim.value),
            contentAlignment = Alignment.Center
        ) {
            PixelCardBox(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White,
                borderWidth = 1.5.dp,
                shadowOffset = 3.dp,
                cornerRadius = 12.dp,
                headerTitle = "★ VICTORY CHEST ★",
                headerBadge = "PHẦN $unitNumber",
                headerColor = PixelColors.PixelYellow,
                showGrille = true,
                showRivets = false
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Rương báu pixel hào quang
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Hào quang vàng neon phía sau
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(PixelColors.PixelYellow.copy(alpha = auraAlpha * 0.35f))
                        )

                        // Biểu tượng Rương vàng mở nắp
                        Text(
                            text = "🎁",
                            fontSize = 58.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Tiêu đề chúc mừng
                    Text(
                        text = "MỞ KHÓA RƯƠNG BÁU!",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Chúc mừng bạn đã hoàn thành xuất sắc tất cả bài học trong Phần $unitNumber!",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        color = PixelColors.MutedText,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // 3. Khối phần thưởng thu thập được (2 thẻ pixel)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Thẻ Sparks Pixel
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PixelColors.CardYellow)
                                .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(8.dp))
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "⚡", fontSize = 22.sp)
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "+$sparksEarned",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.PixelYellowDark
                                )
                                Text(
                                    text = "SPARKS",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.BorderDark,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        // Thẻ EXP Pixel
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PixelColors.CardPurple)
                                .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(8.dp))
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "🌟", fontSize = 22.sp)
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "+$expEarned",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.PixelPurpleDark
                                )
                                Text(
                                    text = "EXP",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.BorderDark,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 4. Nút Arcade nhận quà
                    PixelArcadeButton(
                        text = "🎁 THU THẬP PHẦN THƯỞNG",
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            GameSoundHelper.playClick()
                            onClaim()
                            onDismiss()
                        },
                        color = PixelColors.PixelYellow,
                        fontSize = 12.5f,
                        height = 46.dp,
                        shadowOffset = 2.5.dp,
                        fillMaxWidth = true
                    )
                }
            }
        }
    }
}
