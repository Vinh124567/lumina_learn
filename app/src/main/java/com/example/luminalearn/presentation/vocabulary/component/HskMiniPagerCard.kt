package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Thẻ Lộ trình HSK 1–6 dạng thanh ngang Bento Full Width:
 * - Rộng tràn ngang cân xứng hoàn hảo với 2 ô phía trên.
 * - HorizontalPager cuộn ngang qua các cấp độ HSK 1 đến 6.
 * - Gradient vũ trụ Cosmos chuyển màu tự động theo từng cấp độ.
 * - Hàng tab indicator chấm tròn hoạt họa đàn hồi ở đáy thẻ.
 */
/**
 * Thẻ Lộ trình HSK 1–6 phong cách Băng Game Retro (Cartridge Stage Pager):
 * - Rộng tràn ngang với PixelCardBox viền đen 2dp, bóng cứng 3D 4dp.
 * - Huy hiệu Hán tự khối vuông Pixel 8-bit sắc sảo (一, 二, 三, 四, 五, 六).
 * - Badge STAGE X và thanh đo dung lượng từ phân đoạn pixel.
 * - Nút bấm Arcade dập nổi [ VÀO MAP ▶ ].
 */
@Composable
fun HskMiniPagerCard(
    hskLevels: List<HskLevelCardData>,
    onOpenHskLevel: (HskLevelCardData) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { hskLevels.size })
    val currentLevel = hskLevels.getOrNull(pagerState.currentPage) ?: hskLevels.first()

    PixelCardBox(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp),
        backgroundColor = Color.White,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 2.dp,
        shadowOffset = 4.dp,
        cornerRadius = 8.dp
    ) {
        // HorizontalPager cuộn ngang qua các cấp độ HSK 1 - 6
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val level = hskLevels[page]
            val hanziNumber = when (level.levelNumber) {
                1 -> "一"
                2 -> "二"
                3 -> "三"
                4 -> "四"
                5 -> "五"
                6 -> "六"
                else -> "级"
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .bounceClick(scaleDown = 0.985f) {
                        onOpenHskLevel(level)
                    }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // ── Thân thẻ ngang: Huy hiệu HSK + Thông tin + Nút hành động ──
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Cụm thông tin & Huy hiệu bên trái
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            // Huy hiệu khối vuông Pixel Hán tự cấp độ
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(level.badgeBgColor)
                                    .border(
                                        BorderStroke(1.8.dp, PixelColors.BorderDark),
                                        RoundedCornerShape(4.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = hanziNumber,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.BorderDark
                                )
                            }

                            // Cột text thông tin
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    PixelBadge(
                                        text = "STAGE ${level.levelNumber}",
                                        backgroundColor = level.accentColor,
                                        textColor = Color.White,
                                        borderColor = PixelColors.BorderDark
                                    )

                                    Text(
                                        text = level.stageName,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PixelColors.BorderDark,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "${level.standardWordCount} từ chuẩn • ${level.targetScore}",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PixelColors.MutedText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Nút hành động Arcade bên phải
                        PixelArcadeButton(
                            text = "VÀO MAP ▶",
                            onClick = { onOpenHskLevel(level) },
                            backgroundColor = level.accentColor,
                            textColor = Color.White,
                            fontSize = 10f,
                            contentPadding = 6.dp,
                            shadowOffset = 2.5.dp
                        )
                    }

                    // ── Thanh tiến độ mini trực quan + Tab Indicator đáy thẻ ──
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Thanh đo dung lượng từ chuẩn HSK phân đoạn pixel
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(72.dp)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color(0xFFE4E4E7))
                                    .border(BorderStroke(1.dp, PixelColors.BorderDark), RoundedCornerShape(2.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(fraction = (level.levelNumber * 0.16f).coerceIn(0.16f, 1f))
                                        .background(level.accentColor)
                                )
                            }
                            Text(
                                text = "BẬC ${level.levelNumber}/6",
                                fontFamily = PlusJakartaSans,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark
                            )
                        }

                        // Tab Indicator cuộn ngang dạng khối pixel
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(hskLevels.size) { index ->
                                val isSelected = pagerState.currentPage == index
                                Box(
                                    modifier = Modifier
                                        .height(5.dp)
                                        .width(if (isSelected) 14.dp else 5.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(if (isSelected) level.accentColor else Color(0xFFD4D4D8))
                                        .border(BorderStroke(0.8.dp, PixelColors.BorderDark), RoundedCornerShape(1.dp))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
