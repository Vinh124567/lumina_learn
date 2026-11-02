package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val BackgroundColor = Color(0xFFF6F8FB)

/**
 * Collapsing Header phong cách Retro 16-Bit Pixel Arcade đồng nhất toàn ứng dụng:
 * - Đồng bộ với Cartridge Quest & Pixel Card Box của Tab Trang Chủ và Từ Vựng.
 * - Co giãn mượt mà từ 106dp về 56dp khi cuộn, ghim cố định che chắn nội dung cuộn bên dưới.
 * - Các nút hành động Tim HP, Kho Ngữ Pháp và Chuyển Đổi View chuẩn phím Arcade dập nổi 3D.
 */
@Composable
fun CollapsingLessonHeaderBar(
    collapseProgress: Float,
    isJourneyView: Boolean,
    onToggleJourneyView: (Boolean) -> Unit,
    isOfflineMode: Boolean = false,
    currentHearts: Int = 5,
    maxHearts: Int = 5,
    onOpenHearts: () -> Unit = {},
    onOpenAnalytics: () -> Unit = {},
    onOpenGrammarVault: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val progress = collapseProgress.coerceIn(0f, 1f)
    val headerHeight = lerp(106.dp, 56.dp, progress)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(BackgroundColor)
    ) {
        // ── 1. KHỐI EXPANDED TITLE (Tiêu đề 16-Bit Arcade + Subtitle) ──
        if (progress < 0.95f) {
            val titleAlpha = (1f - progress * 1.5f).coerceIn(0f, 1f)
            val titleScale = 1f - progress * 0.15f

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomStart)
                    .padding(bottom = 10.dp)
                    .graphicsLayer {
                        alpha = titleAlpha
                        scaleX = titleScale
                        scaleY = titleScale
                        translationY = -progress * 25f
                    }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "LỘ TRÌNH BÀI HỌC",
                        fontFamily = PlusJakartaSans,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Badge Quest HSK kiểu Pixel
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(PixelColors.PixelYellow)
                            .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "★ QUEST",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.BorderDark
                        )
                    }

                    if (isOfflineMode) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFE0F2FE))
                                .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "OFFLINE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = if (isOfflineMode) {
                        "Đang học ở chế độ ngoại tuyến từ bộ nhớ đệm"
                    } else {
                        "Chinh phục các chương bài học tương tác theo chuẩn quốc tế"
                    },
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B)
                )
            }
        }

        // ── 2. THANH STICKY ACTION BAR TRÊN ĐỈNH (Cao 56dp) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val collapsedTitleAlpha = ((progress - 0.35f) / 0.65f).coerceIn(0f, 1f)

            // Tiêu đề mini khi cuộn sâu
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.graphicsLayer {
                    alpha = collapsedTitleAlpha
                }
            ) {
                Text(
                    text = "LỘ TRÌNH BÀI HỌC",
                    fontFamily = PlusJakartaSans,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    letterSpacing = 0.3.sp
                )
            }

            // Góc phải: Cụm Tim HP + Nút Chuyển Mode Arcade
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Badge Tim HP phong cách Pixel Retro dập nổi
                Box(
                    modifier = Modifier
                        .padding(bottom = 2.dp, end = 2.dp)
                        .bounceClick(scaleDown = 0.92f) { onOpenHearts() }
                ) {
                    // Bóng cứng 3D
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(x = 2.dp, y = 2.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(PixelColors.ShadowDark)
                    )
                    // Mặt thẻ
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFEE2E2))
                            .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "❤️", fontSize = 12.sp)
                            Text(
                                text = "$currentHearts",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark
                            )
                        }
                    }
                }

                // 2. Nút Chuyển Đổi View phong cách Phím Bấm Console 16-Bit
                Box(
                    modifier = Modifier
                        .padding(bottom = 2.dp, end = 2.dp)
                        .bounceClick(scaleDown = 0.90f) { onToggleJourneyView(!isJourneyView) }
                ) {
                    // Bóng cứng 3D
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(x = 2.dp, y = 2.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(PixelColors.ShadowDark)
                    )
                    // Mặt phím
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isJourneyView) PixelColors.PixelGreen else PixelColors.PixelBlue)
                            .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(6.dp))
                            .padding(horizontal = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (isJourneyView) "🗺️" else "📄",
                                fontSize = 11.5.sp
                            )
                            Text(
                                text = if (isJourneyView) "BẢN ĐỒ" else "DANH SÁCH",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark
                            )
                        }
                    }
                }
            }
        }
    }
}
