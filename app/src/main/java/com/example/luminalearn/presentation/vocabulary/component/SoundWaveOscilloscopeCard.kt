package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.util.SpeechRecognitionState
import com.example.luminalearn.presentation.vocabulary.model.VocabColors

@Composable
fun SoundWaveComparisonCard(
    speechSuccess: SpeechRecognitionState.Success?,
    isListening: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audioWave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, VocabColors.BorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_waveform),
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Biểu đồ Sóng Âm (Oscilloscope)",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = VocabColors.TextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "Biên độ & trường độ",
                    fontSize = 9.5.sp,
                    color = VocabColors.TextMuted,
                    maxLines = 1,
                    softWrap = false
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sóng âm Bản xứ (Chuẩn)
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFEEF2FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBEAFE))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF4F46E5))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Bản xứ",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E3A8A),
                                    maxLines = 1
                                )
                            }
                            Text(
                                text = "Tiết tấu chuẩn",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF4F46E5),
                                maxLines = 1
                            )
                        }

                        // Hộp màn hình hiển thị sóng âm nền trắng chuẩn oscilloscope
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val barCount = 28
                                val spacing = size.width / barCount
                                val barW = spacing * 0.55f
                                val heights = listOf(
                                    0.18f, 0.25f, 0.40f, 0.55f, 0.70f, 0.85f, 0.75f, 0.50f,
                                    0.35f, 0.20f, 0.15f, 0.25f, 0.45f, 0.65f, 0.90f, 0.80f,
                                    0.60f, 0.40f, 0.25f, 0.20f, 0.35f, 0.55f, 0.75f, 0.60f,
                                    0.45f, 0.30f, 0.20f, 0.12f
                                )
                                for (i in 0 until barCount) {
                                    val hFrac = heights[i % heights.size]
                                    val barH = size.height * hFrac
                                    val x = i * spacing + (spacing - barW) / 2f
                                    val y = (size.height - barH) / 2f
                                    drawRoundRect(
                                        color = Color(0xFF3B82F6),
                                        topLeft = Offset(x, y),
                                        size = Size(barW, barH),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f)
                                    )
                                }
                            }
                        }
                    }
                }

                // Sóng âm Của bạn
                val hasRecorded = speechSuccess != null || isListening
                val userWaveColor = when {
                    isListening -> VocabColors.AccentCoral
                    speechSuccess == null -> Color(0xFF94A3B8)
                    speechSuccess.score >= 80 -> Color(0xFF00B67A)
                    speechSuccess.score >= 60 -> Color(0xFFF59E0B)
                    else -> VocabColors.ErrorRed
                }
                val userBgColor = when {
                    isListening -> Color(0xFFFFF7ED)
                    speechSuccess == null -> Color(0xFFF8FAFC)
                    speechSuccess.score >= 80 -> Color(0xFFECFDF5)
                    speechSuccess.score >= 60 -> Color(0xFFFFFBEB)
                    else -> Color(0xFFFEF2F2)
                }
                val userBorderColor = when {
                    isListening -> Color(0xFFFED7AA)
                    speechSuccess == null -> Color(0xFFE2E8F0)
                    speechSuccess.score >= 80 -> Color(0xFFA7F3D0)
                    speechSuccess.score >= 60 -> Color(0xFFFDE68A)
                    else -> Color(0xFFFECACA)
                }
                val userStatusText = when {
                    isListening -> "Đang thu..."
                    speechSuccess != null -> "Đã phân tích"
                    else -> "Chờ thu âm"
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = userBgColor,
                    border = androidx.compose.foundation.BorderStroke(1.dp, userBorderColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(userWaveColor)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Của bạn",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        isListening -> Color(0xFF9A3412)
                                        speechSuccess == null -> Color(0xFF64748B)
                                        speechSuccess.score < 60 -> Color(0xFF991B1B)
                                        else -> Color(0xFF064E3B)
                                    },
                                    maxLines = 1
                                )
                            }
                            Text(
                                text = userStatusText,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = userWaveColor,
                                maxLines = 1
                            )
                        }

                        // Hộp màn hình hiển thị sóng âm nền trắng chuẩn oscilloscope
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val barCount = 30
                                val spacing = size.width / barCount
                                val barW = spacing * 0.65f
                                val heights = listOf(
                                    0.20f, 0.35f, 0.60f, 0.85f, 0.95f, 0.92f, 0.88f, 0.85f,
                                    0.70f, 0.45f, 0.25f, 0.18f, 0.22f, 0.40f, 0.70f, 0.90f,
                                    0.95f, 0.92f, 0.88f, 0.82f, 0.70f, 0.55f, 0.35f, 0.20f,
                                    0.25f, 0.45f, 0.70f, 0.85f, 0.65f, 0.45f
                                )
                                for (i in 0 until barCount) {
                                    val barH = if (!hasRecorded) {
                                        3f
                                    } else if (isListening) {
                                        val phase = ((i.toFloat() / barCount) + waveOffset) * (2f * Math.PI.toFloat())
                                        val dynamicH = 0.2f + 0.55f * kotlin.math.abs(kotlin.math.sin(phase))
                                        size.height * dynamicH
                                    } else {
                                        size.height * heights[i % heights.size]
                                    }
                                    val barColor = if (!hasRecorded) Color(0xFFCBD5E1) else userWaveColor
                                    val x = i * spacing + (spacing - barW) / 2f
                                    val y = (size.height - barH) / 2f
                                    drawRoundRect(
                                        color = barColor,
                                        topLeft = Offset(x, y),
                                        size = Size(barW, barH),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.5f, 1.5f)
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
