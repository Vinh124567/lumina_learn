package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
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
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(26.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header: Level Badge + Close (X)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEDE9FE))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${lesson.level} • ${lesson.category}",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6366F1)
                        )
                    }

                    // Nút đóng (X) tròn
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .bounceClick(scaleDown = 0.88f, onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✕",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

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
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (lesson.pinyinHanziTitle.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = lesson.pinyinHanziTitle,
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6366F1)
                            )
                        }
                    }

                    // Nút loa phát âm
                    if (onSpeak != null && lesson.pinyinHanziTitle.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEEF2FF))
                                .bounceClick(scaleDown = 0.88f) {
                                    onSpeak(lesson.pinyinHanziTitle.substringBefore("(").trim())
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_speaker),
                                contentDescription = "Nghe mẫu",
                                tint = Color(0xFF6366F1),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mô tả bài học
                if (lesson.description.isNotBlank()) {
                    Text(
                        text = lesson.description,
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Hàng thông số chỉ số: Thời lượng, Sparks thưởng, Slide tương tác
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetaStatCard(
                        modifier = Modifier.weight(1f),
                        icon = "⏱️",
                        title = "Thời lượng",
                        value = "${lesson.durationMins} phút"
                    )
                    MetaStatCard(
                        modifier = Modifier.weight(1f),
                        icon = "⚡",
                        title = "Phần thưởng",
                        value = "+${if (lesson.sparks > 0) lesson.sparks else 30} Sparks"
                    )
                    MetaStatCard(
                        modifier = Modifier.weight(1f),
                        icon = "🎴",
                        title = "Slide",
                        value = "${lesson.slides.size.coerceAtLeast(lesson.totalSlides)} trang"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Nút Bắt đầu học bài ngay
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                            )
                        )
                        .bounceClick(scaleDown = 0.96f) {
                            onDismiss()
                            onStartLesson()
                        }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Bắt đầu học bài ngay",
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "→",
                                fontSize = 16.sp,
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

@Composable
private fun MetaStatCard(
    icon: String,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontFamily = PlusJakartaSans,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = title,
                fontFamily = PlusJakartaSans,
                fontSize = 9.5.sp,
                color = Color(0xFF94A3B8),
                maxLines = 1
            )
        }
    }
}
