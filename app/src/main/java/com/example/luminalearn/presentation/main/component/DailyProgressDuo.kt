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
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelCornerStyle
import com.example.luminalearn.ui.theme.PlusJakartaSans

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

    PixelCardBox(
        modifier = modifier.height(198.dp),
        backgroundColor = Color(0xFFFEFCE8), // Vàng kem GameBoy
        headerTitle = "⚡ DAILY EXP",
        headerColor = PixelColors.PixelYellow,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 2.dp,
        shadowOffset = 4.dp,
        cornerStyle = PixelCornerStyle.Sparkles,
        onClick = onStartClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Coin bonus
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                com.example.luminalearn.presentation.vocabulary.component.PixelCoinBadge(amount = bonusSparks)
            }

            // Middle Stat
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$currentMinutes",
                        fontFamily = PlusJakartaSans,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark
                    )
                    Text(
                        text = "/$targetMinutes m",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.PixelYellowDark,
                        modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Progress Track RPG
                com.example.luminalearn.presentation.vocabulary.component.PixelRpgProgressBar(
                    label = "EXP",
                    progress = progress,
                    barColor = PixelColors.PixelYellow,
                    labelColor = PixelColors.PixelYellow,
                    emptyColor = Color(0xFFFEF08A),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Action Button
            PixelArcadeButton(
                text = "TRAIN EXP ▶",
                onClick = onStartClick,
                modifier = Modifier.fillMaxWidth(),
                fillMaxWidth = true,
                backgroundColor = PixelColors.PixelYellow,
                textColor = PixelColors.BorderDark,
                fontSize = 11f,
                contentPadding = 6.dp,
                shadowOffset = 2.5.dp
            )
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

    PixelCardBox(
        modifier = modifier.height(198.dp),
        backgroundColor = Color(0xFFFFF7ED), // Cam retro nhạt
        headerTitle = "🔥 COMBO CHAIN",
        headerColor = PixelColors.PixelOrange,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 2.dp,
        shadowOffset = 4.dp,
        cornerStyle = PixelCornerStyle.Gems,
        onClick = onClaimClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Coin Loot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                com.example.luminalearn.presentation.vocabulary.component.PixelCoinBadge(amount = 30)
            }

            // Middle Stat & Week Hearts
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "$streakDays",
                        fontFamily = PlusJakartaSans,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark
                    )
                    Text(
                        text = "NGÀY CHÁY",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFC2410C)
                    )
                }

                Spacer(modifier = Modifier.height(7.dp))

                // 7-day mini RPG hearts row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    dayLabels.forEachIndexed { index, label ->
                        val isChecked = checkedDays.getOrElse(index) { false }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = if (isChecked) "❤️" else "🖤",
                                fontSize = 10.sp
                            )
                            Text(
                                text = label,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isChecked) Color(0xFFC2410C) else Color(0xFF9CA3AF)
                            )
                        }
                    }
                }
            }

            // Action Button
            PixelArcadeButton(
                text = "CLAIM LOOT 🪙",
                onClick = onClaimClick,
                modifier = Modifier.fillMaxWidth(),
                fillMaxWidth = true,
                backgroundColor = PixelColors.PixelOrange,
                textColor = Color.White,
                fontSize = 10.5f,
                contentPadding = 6.dp,
                shadowOffset = 2.5.dp
            )
        }
    }
}
