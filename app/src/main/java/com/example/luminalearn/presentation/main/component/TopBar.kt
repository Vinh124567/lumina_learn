package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * TopBar phong cách Luminous Bento cao cấp cho màn hình chính (5 Tab).
 * - Trái: Logo thương hiệu LuminaLearn sắc nét, tỉ lệ chuẩn.
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
        // Logo Header thương hiệu LuminaLearn
        Image(
            painter = painterResource(id = R.drawable.ic_header),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .height(34.dp)
                .bounceClick(scaleDown = 0.96f) { onAvatarClick?.invoke() },
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ── 1. Capsule Chuỗi Ngày (Streak - Cam Đào Pastel) ──
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFF7ED))
                    .border(
                        BorderStroke(1.dp, Color(0xFFFED7AA)),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .bounceClick(scaleDown = 0.92f) { onStreakClick?.invoke() }
                    .padding(horizontal = 9.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_streak),
                    contentDescription = "Chuỗi ngày",
                    tint = Color(0xFFEA580C),
                    modifier = Modifier.size(15.dp)
                )
                AnimatedRollingCounter(
                    count = streakDays,
                    textStyle = TextStyle(
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFC2410C)
                    )
                )
            }

            // ── 2. Capsule Điểm Năng Lượng (Points/Energy - Tím Pastel Lumina) ──
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEF2FF))
                    .border(
                        BorderStroke(1.dp, Color(0xFFC7D2FE)),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .bounceClick(scaleDown = 0.92f) { onPointsClick?.invoke() }
                    .padding(horizontal = 9.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = "Điểm tích lũy",
                    tint = Color(0xFF6366F1),
                    modifier = Modifier.size(15.dp)
                )
                AnimatedRollingCounter(
                    count = points,
                    textStyle = TextStyle(
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF4338CA)
                    )
                )
            }

            // ── 3. Nút Chuông Thông Báo Bento (Thay cho nút loa vô nghĩa) ──
            Box(
                modifier = Modifier
                    .size(35.dp)
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
                    modifier = Modifier.size(17.dp)
                )

                if (hasUnreadNotification) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-6).dp, y = 6.dp)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                            .border(1.dp, Color.White, CircleShape)
                    )
                }
            }

            // ── 4. Avatar Người Dùng (Nếu có callback hoặc muốn hiển thị) ──
            if (onAvatarClick != null) {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF5F3FF))
                        .border(
                            BorderStroke(1.2.dp, Color(0xFFDDD6FE)),
                            shape = CircleShape
                        )
                        .bounceClick(scaleDown = 0.90f) { onAvatarClick.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_avatar_default),
                        contentDescription = stringResource(R.string.cd_avatar),
                        tint = Color(0xFF7C3AED),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}