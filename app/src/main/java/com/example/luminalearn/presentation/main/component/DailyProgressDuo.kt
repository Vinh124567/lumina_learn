package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.EnergyPulseAura
import com.example.luminalearn.core.ui.effect.RisingFlameEmbers
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient
import com.example.luminalearn.core.ui.effect.bounceClick

private val GoldBadgeText = Color(0xFFFEF08A)
private val MintTextColor = Color(0xFF6EE7B7)
private val SkyAccentColor = Color(0xFF38BDF8)
private val AmberAccent = Color(0xFFF59E0B)
private val PrimaryIndigo = Color(0xFF5C50F6)

private val ProgressGradient = Brush.horizontalGradient(
    listOf(Color(0xFF38BDF8), Color(0xFF818CF8), Color(0xFFA855F7))
)

private val FlameGradient = Brush.horizontalGradient(
    listOf(Color(0xFFF59E0B), Color(0xFFEA580C))
)

/**
 * Cụm Chỉ Số & Mục Tiêu Ngày dạng Duo Split (2 cột song song).
 * Giúp màn hình không bị dài lê thê đơn điệu, tạo cấu trúc dashboard chuyên nghiệp và gọn gàng.
 */
@Composable
fun DailyProgressDuo(
    currentMinutes: Int = 10,
    targetMinutes: Int = 10,
    bonusSparks: Int = 30,
    streakDays: Int = 5,
    checkedDays: List<Boolean> = listOf(true, true, true, true, true, false, false),
    onStartLessonClick: () -> Unit = {},
    onClaimStreakClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cột Trái: Mục Tiêu Hằng Ngày (Goal Card)
        CompactGoalCard(
            currentMinutes = currentMinutes,
            targetMinutes = targetMinutes,
            bonusSparks = bonusSparks,
            onStartClick = onStartLessonClick,
            modifier = Modifier.weight(1f)
        )

        // Cột Phải: Chuỗi Cháy Lửa (Streak Card)
        CompactStreakCard(
            streakDays = streakDays,
            checkedDays = checkedDays,
            onClaimClick = onClaimStreakClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CompactGoalCard(
    currentMinutes: Int,
    targetMinutes: Int,
    bonusSparks: Int,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (targetMinutes > 0) (currentMinutes.toFloat() / targetMinutes).coerceIn(0f, 1f) else 0f
    val percentage = (progress * 100).toInt()

    Card(
        modifier = modifier.height(188.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, PrimaryIndigo.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .animatedMidnightGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1C59),
                        Color(0xFF2E2C80),
                        Color(0xFF3F3DBA),
                        Color(0xFF0F172A)
                    ),
                    durationMillis = 6000
                )
        ) {
            EnergyPulseAura(modifier = Modifier.matchParentSize())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(
                                BorderStroke(1.dp, GoldBadgeText.copy(alpha = 0.35f)),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "⚡ GOAL",
                            color = GoldBadgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "+$bonusSparks ⚡",
                        color = MintTextColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Middle Stat
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$currentMinutes",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "/$targetMinutes m",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyAccentColor,
                            modifier = Modifier.padding(bottom = 3.dp, start = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Progress Track
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(ProgressGradient)
                        )
                    }
                }

                // Action Button
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .bounceClick(scaleDown = 0.94f) { onStartClick() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = null,
                            tint = PrimaryIndigo,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.btn_start_arrow),
                            color = PrimaryIndigo,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactStreakCard(
    streakDays: Int,
    checkedDays: List<Boolean>,
    onClaimClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

    Card(
        modifier = modifier.height(188.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, AmberAccent.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .animatedMidnightGradient(
                    colors = listOf(
                        Color(0xFF1B112C),
                        Color(0xFF2E1538),
                        Color(0xFF4C1D38),
                        Color(0xFF38152E),
                        Color(0xFF1B112C)
                    ),
                    durationMillis = 6500
                )
        ) {
            RisingFlameEmbers(
                modifier = Modifier.matchParentSize(),
                particleCount = 18
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(
                                BorderStroke(1.dp, AmberAccent.copy(alpha = 0.4f)),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "🔥 STREAK",
                            color = GoldBadgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = stringResource(R.string.streak_active),
                        color = MintTextColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Middle Stat & Week Dots
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "$streakDays",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(R.string.streak_label),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 7-day mini dots
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        dayLabels.forEachIndexed { index, _ ->
                            val isChecked = checkedDays.getOrElse(index) { false }
                            Box(
                                modifier = Modifier
                                    .size(11.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isChecked) AmberAccent else Color.White.copy(alpha = 0.15f)
                                    )
                            )
                        }
                    }
                }

                // Action Button
                Surface(
                    shape = CircleShape,
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .clip(CircleShape)
                        .background(FlameGradient)
                        .bounceClick(scaleDown = 0.94f) { onClaimClick() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_streak),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.btn_claim_streak),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
