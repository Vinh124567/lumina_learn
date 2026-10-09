package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.util.SpeechRecognitionState
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun SoundWaveComparisonCard(
    speechSuccess: SpeechRecognitionState.Success?,
    isListening: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audioWavePulse")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "animatedProgress"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = Color(0x0F000000),
                ambientColor = Color(0x0A000000)
            ),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Title & Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_waveform),
                            contentDescription = null,
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Text(
                        text = "Phổ Sóng Âm (Waveform Spectrum)",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "Tiết tấu & Biên độ",
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Medium
                )
            }

            // ── Kênh 1: Sóng âm Bản Xứ (Mẫu chuẩn Apple Audio Pill Bar) ──
            WaveformChannelCard(
                title = "Bản xứ",
                badgeText = "Tiết tấu chuẩn",
                badgeBgColor = Color(0xFFEEF2FF),
                badgeTextColor = Color(0xFF4F46E5),
                dotColor = Color(0xFF6366F1),
                gradientColors = listOf(Color(0xFF6366F1), Color(0xFF06B6D4)),
                barHeights = NATIVE_SAMPLE_BAR_WEIGHTS,
                isAnimated = false,
                animatedProgress = 0f,
                showTimeRuler = true
            )

            // ── Kênh 2: Sóng âm Của Bạn (Dynamic Pill Bar) ──
            val hasRecorded = speechSuccess != null || isListening
            val userScore = speechSuccess?.score ?: if (isListening) 80 else 0

            val userBadgeBgColor = when {
                isListening -> Color(0xFFFFF7ED)
                speechSuccess == null -> Color(0xFFF8FAFC)
                userScore >= 80 -> Color(0xFFECFDF5)
                userScore >= 60 -> Color(0xFFFFFBEB)
                else -> Color(0xFFFEF2F2)
            }

            val userBadgeTextColor = when {
                isListening -> Color(0xFFC2410C)
                speechSuccess == null -> Color(0xFF64748B)
                userScore >= 80 -> Color(0xFF047857)
                userScore >= 60 -> Color(0xFFB45309)
                else -> Color(0xFFB91C1C)
            }

            val userDotColor = when {
                isListening -> VocabColors.AccentCoral
                speechSuccess == null -> Color(0xFF94A3B8)
                userScore >= 80 -> Color(0xFF10B981)
                userScore >= 60 -> Color(0xFFF59E0B)
                else -> Color(0xFFEF4444)
            }

            val userBadgeText = when {
                isListening -> "Đang thu âm..."
                speechSuccess == null -> "Chờ phát âm"
                else -> "${userScore}% Khớp trường độ"
            }

            val userGradients = when {
                isListening -> listOf(Color(0xFFF97316), Color(0xFFFBBF24))
                speechSuccess == null -> listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1))
                userScore >= 80 -> listOf(Color(0xFF10B981), Color(0xFF34D399))
                userScore >= 60 -> listOf(Color(0xFFF59E0B), Color(0xFFFDE68A))
                else -> listOf(Color(0xFFEF4444), Color(0xFFFCA5A5))
            }

            val userHeights = if (!hasRecorded) {
                EMPTY_BAR_WEIGHTS
            } else if (isListening) {
                DYNAMIC_LIVE_WEIGHTS
            } else {
                // Biến thiên biên độ tương ứng theo điểm số
                if (userScore >= 80) USER_HIGH_SCORE_WEIGHTS else USER_LOW_SCORE_WEIGHTS
            }

            WaveformChannelCard(
                title = "Của bạn",
                badgeText = userBadgeText,
                badgeBgColor = userBadgeBgColor,
                badgeTextColor = userBadgeTextColor,
                dotColor = userDotColor,
                gradientColors = userGradients,
                barHeights = userHeights,
                isAnimated = isListening,
                animatedProgress = animatedProgress,
                showTimeRuler = false
            )
        }
    }
}

@Composable
private fun WaveformChannelCard(
    title: String,
    badgeText: String,
    badgeBgColor: Color,
    badgeTextColor: Color,
    dotColor: Color,
    gradientColors: List<Color>,
    barHeights: List<Float>,
    isAnimated: Boolean,
    animatedProgress: Float,
    showTimeRuler: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFD),
        border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Label Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeBgColor
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = badgeTextColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Pill Bar Waveform Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(0.5.dp, Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val barCount = 34
                    val spacing = size.width / barCount
                    val barWidth = spacing * 0.58f

                    val brush = Brush.verticalGradient(
                        colors = gradientColors,
                        startY = 0f,
                        endY = size.height
                    )

                    for (i in 0 until barCount) {
                        val baseWeight = barHeights.getOrElse(i % barHeights.size) { 0.2f }

                        val calculatedHeight = if (isAnimated) {
                            val phase = ((i.toFloat() / barCount) + animatedProgress) * (2f * Math.PI.toFloat())
                            val dynamicFactor = 0.25f + 0.65f * abs(sin(phase))
                            (size.height * dynamicFactor).coerceIn(4f, size.height)
                        } else {
                            (size.height * baseWeight).coerceIn(4f, size.height)
                        }

                        val x = i * spacing + (spacing - barWidth) / 2f
                        val y = (size.height - calculatedHeight) / 2f

                        drawRoundRect(
                            brush = brush,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, calculatedHeight),
                            cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                        )
                    }
                }
            }

            // Thước đo mốc thời gian (Time ruler)
            if (showTimeRuler) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "0.0s", fontSize = 8.5.sp, color = Color(0xFF94A3B8))
                    Text(text = "0.4s", fontSize = 8.5.sp, color = Color(0xFF94A3B8))
                    Text(text = "0.8s", fontSize = 8.5.sp, color = Color(0xFF94A3B8))
                    Text(text = "1.2s", fontSize = 8.5.sp, color = Color(0xFF94A3B8))
                }
            }
        }
    }
}

// ── Dữ liệu mẫu chuẩn âm thanh thực tế (Trọng âm & Trường độ tự nhiên) ──
private val NATIVE_SAMPLE_BAR_WEIGHTS = listOf(
    0.15f, 0.22f, 0.38f, 0.58f, 0.76f, 0.92f, 0.85f, 0.68f,
    0.42f, 0.25f, 0.18f, 0.22f, 0.40f, 0.65f, 0.88f, 0.96f,
    0.90f, 0.72f, 0.48f, 0.28f, 0.18f, 0.24f, 0.45f, 0.70f,
    0.82f, 0.64f, 0.40f, 0.22f, 0.16f, 0.20f, 0.30f, 0.18f,
    0.14f, 0.10f
)

private val USER_HIGH_SCORE_WEIGHTS = listOf(
    0.16f, 0.24f, 0.36f, 0.55f, 0.72f, 0.88f, 0.82f, 0.65f,
    0.39f, 0.22f, 0.16f, 0.20f, 0.38f, 0.62f, 0.85f, 0.92f,
    0.86f, 0.69f, 0.45f, 0.25f, 0.16f, 0.22f, 0.42f, 0.68f,
    0.78f, 0.60f, 0.38f, 0.20f, 0.14f, 0.18f, 0.26f, 0.15f,
    0.12f, 0.10f
)

private val USER_LOW_SCORE_WEIGHTS = listOf(
    0.30f, 0.55f, 0.70f, 0.80f, 0.50f, 0.25f, 0.20f, 0.40f,
    0.60f, 0.75f, 0.45f, 0.20f, 0.15f, 0.25f, 0.40f, 0.50f,
    0.70f, 0.85f, 0.60f, 0.35f, 0.20f, 0.15f, 0.30f, 0.45f,
    0.60f, 0.50f, 0.30f, 0.20f, 0.15f, 0.12f, 0.10f, 0.08f,
    0.08f, 0.06f
)

private val EMPTY_BAR_WEIGHTS = List(34) { 0.08f }

private val DYNAMIC_LIVE_WEIGHTS = List(34) { 0.45f }
