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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Trạm Đấu Trường Phản Xạ Arcade 16-Bit (Gamification Battle Station):
 * - Tích hợp 3 Minigame:
 *   1. ⚡ Đấu Ghép Từ Siêu Tốc 60s
 *   2. 🚀 Pháo Thủ Bắn Hán Tự (Laser Space Shooter)
 *   3. 🧩 Xếp Từ Thành Câu Ngữ Pháp (Sentence Builder Puzzle)
 * - Thiết kế PixelCardBox dập nổi viền đen 2dp, bóng cứng 3D chuẩn Retro Arcade.
 */
@Composable
fun SpeedWordMatchCard(
    onStartMatch: () -> Unit,
    onStartShooter: () -> Unit = onStartMatch,
    onStartSentenceOrder: () -> Unit = onStartMatch,
    modifier: Modifier = Modifier
) {
    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color(0xFF7C3AED), // Tím Arcade Neon
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 2.dp,
        shadowOffset = 4.dp,
        cornerRadius = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // ── Hàng 1: Badge Gamification 8-bit + Thưởng Coins ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PixelBadge(
                    text = "🕹️ ARCADE BATTLE ARENA",
                    backgroundColor = PixelColors.PixelYellow,
                    textColor = PixelColors.BorderDark,
                    borderColor = PixelColors.BorderDark
                )

                PixelBadge(
                    text = "+40 COINS 🪙",
                    backgroundColor = Color.White,
                    textColor = PixelColors.BorderDark,
                    borderColor = PixelColors.BorderDark
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Hàng 2: Tiêu đề Arcade & Minh họa 3 chế độ ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Đấu Trường Phản Xạ 16-Bit",
                        fontFamily = PlusJakartaSans,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Ghép từ siêu tốc, Bắn phi thuyền Hán tự & Xếp từ thành câu ngữ pháp",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.90f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Các icon mini biểu trưng 3 chế độ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = "⚡", fontSize = 11.sp)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = "🚀", fontSize = 11.sp)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = "🧩", fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Hàng 3: 2 Phím Chọn Chế Độ Ghép Từ & Pháo Thủ ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    PixelArcadeButton(
                        text = "⚡ GHÉP TỪ 60S",
                        onClick = onStartMatch,
                        color = PixelColors.PixelYellow,
                        textColor = PixelColors.BorderDark,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    PixelArcadeButton(
                        text = "🚀 BẮN HÁN TỰ",
                        onClick = onStartShooter,
                        color = PixelColors.PixelRed,
                        textColor = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Hàng 4: Chế Độ 3 Mới: XẾP TỪ THÀNH CÂU ──
            PixelArcadeButton(
                text = "🧩 XẾP TỪ THÀNH CÂU NGỮ PHÁP",
                onClick = onStartSentenceOrder,
                color = PixelColors.PixelGreen,
                textColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
