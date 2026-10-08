package com.example.luminalearn.presentation.vocabulary.component

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val GoldBadgeText = Color(0xFFFEF08A)
private val NeonCyan = Color(0xFF38BDF8)
private val NeonPurple = Color(0xFFA855F7)
private val AccentOrange = Color(0xFFFB923C)

/**
 * Banner Spotlight Thử thách Gamification "Đấu Phản Xạ Ghép Nhanh 60s":
 * - Thiết kế sang trọng đồng bộ với AI Spotlight Banner trên Trang chủ.
 * - Nền Midnight Aurora tím huyền ảo + Hạt bụi sao CosmicStarfield.
 * - Nút "Vào đấu ngay" chạm mở sàn đấu ghép từ tương tác.
 */
@Composable
fun SpeedWordMatchCard(
    onStartMatch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .bounceClick(scaleDown = 0.98f, onClick = onStartMatch),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(0.5.dp, NeonPurple.copy(alpha = 0.40f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animatedMidnightGradient(
                    colors = listOf(
                        Color(0xFF130E26),
                        Color(0xFF2E1065),
                        Color(0xFF3B0764),
                        Color(0xFF1E1B4B),
                        Color(0xFF130E26)
                    ),
                    durationMillis = 7500
                )
        ) {
            CosmicStarfield(
                modifier = Modifier.matchParentSize(),
                particleCount = 20,
                focusCenterXRatio = 0.85f,
                focusCenterYRatio = 0.50f
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // ── Hàng 1: Badge Gamification + Thưởng điểm ──
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
                                BorderStroke(0.5.dp, GoldBadgeText.copy(alpha = 0.45f)),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "⚡ ĐẤU PHẢN XẠ 60S",
                            fontFamily = PlusJakartaSans,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldBadgeText
                        )
                    }

                    // Badge thưởng Sparks
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentOrange.copy(alpha = 0.18f))
                            .border(
                                BorderStroke(0.5.dp, AccentOrange.copy(alpha = 0.45f)),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "+30 Sparks ⚡",
                            fontFamily = PlusJakartaSans,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentOrange
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ── Hàng 2: Tiêu đề & Minh họa 2 chip ghép từ ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Đấu Ghép Từ Siêu Tốc",
                            fontFamily = PlusJakartaSans,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Nối chữ Hán & Nghĩa tương ứng thật nhanh, bứt phá phản xạ tức thì",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            color = Color(0xFFCBD5E1),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Minh họa trực quan cặp từ mini phát sáng
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .border(BorderStroke(0.5.dp, NeonCyan.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "你好",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                        }

                        Text(
                            text = "↕",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .border(BorderStroke(0.5.dp, NeonPurple.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Xin chào",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ── Hàng 3: Nút vào đấu ngay dạng Capsule nổi bật ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF7C3AED), Color(0xFFDB2777))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Vào đấu ghép từ ngay",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "→",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
