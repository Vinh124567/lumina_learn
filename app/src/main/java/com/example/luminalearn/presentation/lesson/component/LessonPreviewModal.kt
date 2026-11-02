package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans

@Composable
fun LessonPreviewModal(
    lesson: ChineseLessonData,
    onStartLesson: () -> Unit,
    onDismiss: () -> Unit,
    onSpeak: ((String) -> Unit)? = null
) {
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
                headerTitle = "BÀI HỌC • ${lesson.level.uppercase()} ★ QUEST",
                headerBadge = lesson.category,
                headerColor = PixelColors.PixelYellow,
                showGrille = true,
                showRivets = false
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Hàng trên: Nút Đóng (✕) phong cách Pixel
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(PixelColors.RetroCream)
                                .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                                .bounceClick(scaleDown = 0.88f, onClick = onDismiss),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✕",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Title Chữ Hán & Loa phát âm
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = lesson.title,
                                fontFamily = PlusJakartaSans,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            if (lesson.pinyinHanziTitle.isNotBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = lesson.pinyinHanziTitle,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PixelColors.PixelPurpleDark
                                )
                            }
                        }

                        // Nút loa phát âm pixel
                        if (onSpeak != null && lesson.pinyinHanziTitle.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(PixelColors.CardPurple)
                                    .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                                    .bounceClick(scaleDown = 0.88f) {
                                        onSpeak(lesson.pinyinHanziTitle.substringBefore("(").trim())
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_speaker),
                                    contentDescription = "Nghe mẫu",
                                    tint = PixelColors.BorderDark,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mô tả bài học
                    if (lesson.description.isNotBlank()) {
                        Text(
                            text = lesson.description,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.5.sp,
                            color = PixelColors.MutedText,
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Hàng thông số chỉ số: Thời lượng, Sparks thưởng, Slide tương tác (3 hộp Pixel)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetaStatPixelCard(
                            modifier = Modifier.weight(1f),
                            icon = "⏱️",
                            title = "Thời lượng",
                            value = "${lesson.durationMins} phút"
                        )
                        MetaStatPixelCard(
                            modifier = Modifier.weight(1f),
                            icon = "⚡",
                            title = "Thưởng",
                            value = "+${if (lesson.sparks > 0) lesson.sparks else 30} SPK"
                        )
                        MetaStatPixelCard(
                            modifier = Modifier.weight(1f),
                            icon = "🎴",
                            title = "Slide",
                            value = "${lesson.slides.size.coerceAtLeast(lesson.totalSlides)} trang"
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Nút Bắt đầu học bài ngay (Arcade Button dập nổi)
                    PixelArcadeButton(
                        text = "▶ BẮT ĐẦU HỌC BÀI NGAY",
                        onClick = onStartLesson,
                        color = PixelColors.PixelYellow,
                        fontSize = 13f,
                        height = 46.dp,
                        shadowOffset = 2.5.dp,
                        fillMaxWidth = true
                    )
                }
            }
        }
    }
}

@Composable
private fun MetaStatPixelCard(
    icon: String,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(PixelColors.RetroCream)
            .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontFamily = PlusJakartaSans,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.BorderDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = title,
                fontFamily = PlusJakartaSans,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = PixelColors.MutedText,
                maxLines = 1
            )
        }
    }
}
