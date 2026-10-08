package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.AnimatedRollingCounter
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.core.ui.effect.breathingGlow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * TopBar phong cách Luxury Luminous cho màn hình chính và các phân hệ học tập.
 * - Trái: Avatar người dùng với gradient ring + Typography Brandmark "Lumina" sắc sảo & Sparkle.
 * - Phải: Bộ capsule sáng pastel (Streak cam đào + Điểm năng lượng tím pastel) và Nút chuông thông báo Bento.
 */
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    streakDays: Int = 5,
    points: Int = 240,
    hasUnreadNotification: Boolean = true,
    onStreakClick: (() -> Unit)? = null,
    onPointsClick: (() -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null,
    onAvatarClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ── Trái: Avatar Người Dùng + Brandmark Lumina & Lời Chào ──
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.bounceClick(scaleDown = 0.95f) { onAvatarClick?.invoke() }
        ) {
            // Avatar tròn sang trọng với viền Gradient thời thượng
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFFEC4899))
                        )
                    )
                    .padding(1.5.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFFF8FAFC)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_avatar_default),
                        contentDescription = stringResource(R.string.cd_avatar),
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Brandmark Lumina & Lời Chào
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Lumina",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = Color(0xFF0F172A),
                        letterSpacing = (-0.3).sp
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sparkle),
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier
                            .size(13.dp)
                            .breathingGlow(minScale = 0.9f, maxScale = 1.25f, durationMillis = 1500)
                    )
                }

                Text(
                    text = stringResource(R.string.greeting_nihao_sub),
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.5.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // ── Phải: Bộ Capsule Trạng Thái (Streak, Points, Notification) ──
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // 1. Capsule Chuỗi Ngày (Streak - Cam Đào Pastel)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFF7ED))
                    .border(
                        BorderStroke(1.dp, Color(0xFFFED7AA)),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .bounceClick(scaleDown = 0.92f) { onStreakClick?.invoke() }
                    .padding(horizontal = 8.5.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_streak),
                    contentDescription = "Chuỗi ngày",
                    tint = Color(0xFFEA580C),
                    modifier = Modifier
                        .size(14.dp)
                        .breathingGlow(minScale = 0.94f, maxScale = 1.14f, durationMillis = 1400)
                )
                AnimatedRollingCounter(
                    count = streakDays,
                    textStyle = TextStyle(
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFC2410C)
                    )
                )
            }

            // 2. Capsule Điểm Năng Lượng (Points/Energy - Tím Pastel Lumina)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEF2FF))
                    .border(
                        BorderStroke(1.dp, Color(0xFFC7D2FE)),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .bounceClick(scaleDown = 0.92f) { onPointsClick?.invoke() }
                    .padding(horizontal = 8.5.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = "Điểm tích lũy",
                    tint = Color(0xFF6366F1),
                    modifier = Modifier
                        .size(14.dp)
                        .breathingGlow(minScale = 0.95f, maxScale = 1.10f, durationMillis = 1800)
                )
                AnimatedRollingCounter(
                    count = points,
                    textStyle = TextStyle(
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF4338CA)
                    )
                )
            }

            // 3. Nút Chuông Thông Báo Bento
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(
                        BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        shape = CircleShape
                    )
                    .bounceClick(scaleDown = 0.90f) { onNotificationClick?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = "Thông báo",
                    tint = Color(0xFF475569),
                    modifier = Modifier.size(16.dp)
                )

                if (hasUnreadNotification) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-5).dp, y = 5.dp)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                            .border(1.dp, Color.White, CircleShape)
                            .breathingGlow(minScale = 0.85f, maxScale = 1.25f, durationMillis = 1100)
                    )
                }
            }
        }
    }
}

/**
 * Collapsing Home TopBar phong cách Large Morphing Header (chuẩn iOS / Duolingo):
 * - Khi ở đỉnh (progress = 0): Chiều cao 138dp, Avatar lớn 48dp, Large Title 20sp, Badge HSK 1 rực rỡ, nền trong suốt.
 * - Khi cuộn (progress -> 1): Khối Hero to scale & parallax bay lên TopBar mượt mà, chuyển sang nền kính mờ trắng đục.
 * - Khi cuộn sâu (progress = 1): Dừng đóng và ghim cố định ở độ cao 58dp (Sticky Pinned), không bao giờ bị che mất.
 */
@Composable
fun CollapsingHomeTopBar(
    collapseProgress: Float,
    modifier: Modifier = Modifier,
    streakDays: Int = 5,
    points: Int = 240,
    hasUnreadNotification: Boolean = true,
    onStreakClick: (() -> Unit)? = null,
    onPointsClick: (() -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null,
    onAvatarClick: (() -> Unit)? = null,
) {
    val progress = collapseProgress.coerceIn(0f, 1f)
    val headerHeight = androidx.compose.ui.unit.lerp(138.dp, 58.dp, progress)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(Color.White.copy(alpha = progress * 0.98f))
    ) {
        // ── 1. KHỐI EXPANDED HERO PROFILE (Avatar to 48dp + Large Title + Badge HSK) ──
        if (progress < 0.95f) {
            val heroAlpha = (1f - progress * 1.5f).coerceIn(0f, 1f)
            val heroScale = 1f - progress * 0.15f

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomStart)
                    .padding(bottom = 12.dp)
                    .graphicsLayer {
                        alpha = heroAlpha
                        scaleX = heroScale
                        scaleY = heroScale
                        translationY = -progress * 70f
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Avatar lớn 48dp viền gradient phát sáng
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFFEC4899))
                            )
                        )
                        .padding(2.dp)
                        .bounceClick(scaleDown = 0.93f) { onAvatarClick?.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFFF8FAFC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_avatar_default),
                            contentDescription = stringResource(R.string.cd_avatar),
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // Cụm Large Title & Badge cấp độ
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.large_greeting_name),
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color(0xFF0F172A),
                        letterSpacing = (-0.4).sp,
                        maxLines = 1
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEEF2FF))
                                .border(BorderStroke(0.8.dp, Color(0xFFC7D2FE)), RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.large_greeting_badge),
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF4F46E5)
                            )
                        }

                        Text(
                            text = "• Mục tiêu 10 phút/ngày",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        // ── 2. THANH STICKY ACTION BAR TRÊN ĐỈNH (Cao 58dp cố định) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Bên trái khi Collapsed: Avatar nhỏ (34dp) + Brandmark Lumina (Hiện dần khi progress > 0.4)
            val collapsedLeftAlpha = ((progress - 0.35f) / 0.65f).coerceIn(0f, 1f)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .graphicsLayer {
                        alpha = collapsedLeftAlpha
                    }
                    .bounceClick(scaleDown = 0.95f) { onAvatarClick?.invoke() }
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFFEC4899))
                            )
                        )
                        .padding(1.5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFFF8FAFC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_avatar_default),
                            contentDescription = stringResource(R.string.cd_avatar),
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Lumina",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color(0xFF0F172A),
                        letterSpacing = (-0.3).sp
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sparkle),
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier
                            .size(12.dp)
                            .breathingGlow(minScale = 0.9f, maxScale = 1.25f, durationMillis = 1500)
                    )
                }
            }

            // Bên phải: Bộ 3 Capsule chỉ số (Streak, Points, Chuông) — CỐ ĐỊNH VỮNG VÀNG
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                // Streak Capsule
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFF7ED))
                        .border(
                            BorderStroke(1.dp, Color(0xFFFED7AA)),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .bounceClick(scaleDown = 0.92f) { onStreakClick?.invoke() }
                        .padding(horizontal = 8.5.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_streak),
                        contentDescription = "Chuỗi ngày",
                        tint = Color(0xFFEA580C),
                        modifier = Modifier
                            .size(14.dp)
                            .breathingGlow(minScale = 0.94f, maxScale = 1.14f, durationMillis = 1400)
                    )
                    AnimatedRollingCounter(
                        count = streakDays,
                        textStyle = TextStyle(
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFC2410C)
                        )
                    )
                }

                // Points Capsule
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEEF2FF))
                        .border(
                            BorderStroke(1.dp, Color(0xFFC7D2FE)),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .bounceClick(scaleDown = 0.92f) { onPointsClick?.invoke() }
                        .padding(horizontal = 8.5.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bolt),
                        contentDescription = "Điểm tích lũy",
                        tint = Color(0xFF6366F1),
                        modifier = Modifier
                            .size(14.dp)
                            .breathingGlow(minScale = 0.95f, maxScale = 1.10f, durationMillis = 1800)
                    )
                    AnimatedRollingCounter(
                        count = points,
                        textStyle = TextStyle(
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4338CA)
                        )
                    )
                }

                // Bento Notification Bell
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(
                            BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            shape = CircleShape
                        )
                        .bounceClick(scaleDown = 0.90f) { onNotificationClick?.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = "Thông báo",
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(16.dp)
                    )

                    if (hasUnreadNotification) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-5).dp, y = 5.dp)
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444))
                                .border(1.dp, Color.White, CircleShape)
                                .breathingGlow(minScale = 0.85f, maxScale = 1.25f, durationMillis = 1100)
                        )
                    }
                }
            }
        }
    }
}