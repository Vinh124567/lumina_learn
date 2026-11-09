package com.example.luminalearn.presentation.main.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.data.model.DailyWisdomDto
import com.example.luminalearn.data.model.LessonDto

/**
 * Bento Grid Dashboard (Modular layout).
 * Tổ chức trung tâm điều khiển học tập thành các khối Widget Bento tỷ lệ vàng,
 * hiển thị trọn vẹn và vừa vặn trên màn hình.
 */
@Composable
fun BentoDashboard(
    dailyWisdom: DailyWisdomDto?,
    recommendedLesson: LessonDto? = null,
    onStartLessonClick: (String) -> Unit,
    onViewAllLessonsClick: () -> Unit,
    onStartRoleplayClick: () -> Unit,
    onVocabClick: () -> Unit,
    onChallengeClick: () -> Unit,
    onRefreshWisdomClick: () -> Unit,
    onClaimSparksClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ── 1. Hero Widget: Bài Học Đang Học (Chiếm trọn bề ngang) ──
        BentoHeroLessonWidget(
            lesson = recommendedLesson,
            onStartLessonClick = { onStartLessonClick(recommendedLesson?.id ?: "1") },
            onViewAllLessonsClick = onViewAllLessonsClick
        )

        // ── 2. Middle Row: Mục Tiêu & Streak (Trái) + AI Spark Studio (Phải) ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BentoEnergyStreakWidget(
                streakDays = 5,
                onClaimSparksClick = onClaimSparksClick,
                modifier = Modifier.weight(1f)
            )

            BentoAiStudioWidget(
                onStartRoleplayClick = onStartRoleplayClick,
                modifier = Modifier.weight(1f)
            )
        }

        // ── 3. Lower Row: Kho Từ Vựng (Trái) + Thử Thách 60s (Phải) ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BentoVocabWidget(
                onVocabClick = onVocabClick,
                modifier = Modifier.weight(1f)
            )

            BentoChallengeWidget(
                onChallengeClick = onChallengeClick,
                modifier = Modifier.weight(1f)
            )
        }

        // ── 4. Bottom Strip: Dải Danh Ngôn Tĩnh Lặng ──
        BentoWisdomStrip(
            dailyWisdom = dailyWisdom,
            onRefreshWisdomClick = onRefreshWisdomClick
        )
    }
}

/**
 * Widget Bento 1: Hero bài học HSK đang học.
 */
@Composable
private fun BentoHeroLessonWidget(
    lesson: LessonDto?,
    onStartLessonClick: () -> Unit,
    onViewAllLessonsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(scaleDown = 0.98f) { onStartLessonClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            1.2.dp,
            Brush.linearGradient(
                listOf(Color(0xFF00F2FE).copy(alpha = 0.6f), Color(0xFF6366F1).copy(alpha = 0.3f))
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF0F1532), Color(0xFF161E48), Color(0xFF10132B))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge trạng thái
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFF00F2FE).copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF00F2FE), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.bento_hero_badge),
                            color = Color(0xFF00F2FE),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Nút xem tất cả
                    Text(
                        text = stringResource(R.string.bento_hero_all_lessons),
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onViewAllLessonsClick() }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = lesson?.title?.ifBlank { null } ?: stringResource(R.string.bento_hero_title),
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Thanh tiến độ bài học
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.bento_hero_progress),
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF1E293B))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.65f)
                                    .height(6.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF00F2FE), Color(0xFF8B5CF6))
                                        )
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Nút tiếp tục nổi bật
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                                )
                            )
                            .padding(horizontal = 14.dp, vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.bento_hero_btn_resume),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Widget Bento 2: Năng Lượng & Chuỗi Ngày Học (Streak + Goal).
 */
@Composable
private fun BentoEnergyStreakWidget(
    streakDays: Int,
    onClaimSparksClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(154.dp)
            .bounceClick(scaleDown = 0.97f) { onClaimSparksClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.35f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1C1318), Color(0xFF14101A))
                    )
                )
                .padding(13.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_streak),
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.bento_energy_title),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Middle: Vòng tiến độ + Số ngày
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .drawBehind {
                                drawArc(
                                    color = Color(0xFF332025),
                                    startAngle = 0f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    style = Stroke(width = 3.5.dp.toPx())
                                )
                                drawArc(
                                    color = Color(0xFFFF9800),
                                    startAngle = -90f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    style = Stroke(width = 3.5.dp.toPx())
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = stringResource(R.string.bento_energy_progress),
                            color = Color(0xFFFFD54F),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.bento_streak_days, streakDays),
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
                }

                // Footer button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFFFF9800).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.bento_claim_ready),
                        color = Color(0xFFFFB74D),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Widget Bento 3: AI Spark Studio (Hội thoại nhập vai).
 */
@Composable
private fun BentoAiStudioWidget(
    onStartRoleplayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bento_soundwave")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_scale"
    )

    Card(
        modifier = modifier
            .height(154.dp)
            .bounceClick(scaleDown = 0.97f) { onStartRoleplayClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.4f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF14132B), Color(0xFF0F182E))
                    )
                )
                .padding(13.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header: Icon + Title
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_spark_ai),
                        contentDescription = null,
                        tint = Color(0xFFA78BFA),
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.bento_ai_title),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Middle: Sóng âm + Kịch bản
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.5.dp)
                            .height((18 * waveScale).dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF00F2FE))
                    )
                    Box(
                        modifier = Modifier
                            .width(3.5.dp)
                            .height((30 * (1.2f - waveScale * 0.4f)).dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFFA78BFA))
                    )
                    Box(
                        modifier = Modifier
                            .width(3.5.dp)
                            .height((22 * waveScale).dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFFEC4899))
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = stringResource(R.string.bento_ai_scenario),
                        color = Color(0xFFCBD5E1),
                        fontSize = 11.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.5.sp
                    )
                }

                // Footer button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFF8B5CF6).copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.bento_ai_btn),
                        color = Color(0xFFDDD6FE),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Widget Bento 4: Kho Từ Vựng HSK.
 */
@Composable
private fun BentoVocabWidget(
    onVocabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(154.dp)
            .bounceClick(scaleDown = 0.97f) { onVocabClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.35f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0F1E1E), Color(0xFF0D1624))
                    )
                )
                .padding(13.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header: Icon + Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_book),
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.bento_vocab_title),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Middle: Chữ Hán & Số lượng
                Column {
                    Text(
                        text = stringResource(R.string.bento_vocab_hanzi),
                        color = Color(0xFF6EE7B7),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.bento_vocab_count),
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }

                // Footer button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFF10B981).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.bento_vocab_btn),
                        color = Color(0xFF6EE7B7),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Widget Bento 5: Thử Thách Spark 60s.
 */
@Composable
private fun BentoChallengeWidget(
    onChallengeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(154.dp)
            .bounceClick(scaleDown = 0.97f) { onChallengeClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0xFFEC4899).copy(alpha = 0.35f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF221124), Color(0xFF140D22))
                    )
                )
                .padding(13.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header: Icon + Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bolt),
                        contentDescription = null,
                        tint = Color(0xFFF472B6),
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.bento_challenge_title),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Middle: Chủ đề & Phần thưởng
                Column {
                    Text(
                        text = stringResource(R.string.bento_challenge_topic),
                        color = Color(0xFFFBCFE8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.bento_challenge_reward),
                        color = Color(0xFFF472B6),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Footer button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFFEC4899).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.bento_challenge_btn),
                        color = Color(0xFFFBCFE8),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Widget Bento 6: Dải Danh Ngôn Tĩnh Lặng (Zen Wisdom Strip).
 */
@Composable
private fun BentoWisdomStrip(
    dailyWisdom: DailyWisdomDto?,
    onRefreshWisdomClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(scaleDown = 0.98f) { onRefreshWisdomClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A).copy(alpha = 0.7f))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_sparkle),
                contentDescription = null,
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                val quoteText = when {
                    dailyWisdom != null && dailyWisdom.chinese.isNotBlank() ->
                        "${dailyWisdom.chinese} • ${dailyWisdom.vietnamese.ifBlank { dailyWisdom.meaning }}"
                    else -> stringResource(R.string.daily_wisdom_quote)
                }
                Text(
                    text = quoteText,
                    color = Color(0xFFE2E8F0),
                    fontSize = 11.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val authorText = when {
                    dailyWisdom != null && dailyWisdom.author.isNotBlank() -> dailyWisdom.author
                    else -> stringResource(R.string.daily_wisdom_author)
                }
                Text(
                    text = authorText,
                    color = Color(0xFF64748B),
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = painterResource(R.drawable.ic_refresh),
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
