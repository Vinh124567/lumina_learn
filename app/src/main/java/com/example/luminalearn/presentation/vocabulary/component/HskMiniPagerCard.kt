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
import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Thẻ Lộ trình HSK 1–6 dạng thanh ngang Bento Full Width:
 * - Rộng tràn ngang cân xứng hoàn hảo với 2 ô phía trên.
 * - HorizontalPager cuộn ngang qua các cấp độ HSK 1 đến 6.
 * - Gradient vũ trụ Cosmos chuyển màu tự động theo từng cấp độ.
 * - Hàng tab indicator chấm tròn hoạt họa đàn hồi ở đáy thẻ.
 */
@Composable
fun HskMiniPagerCard(
    hskLevels: List<HskLevelCardData>,
    onOpenHskLevel: (HskLevelCardData) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { hskLevels.size })
    val currentLevel = hskLevels.getOrNull(pagerState.currentPage) ?: hskLevels.first()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(108.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(0.5.dp, currentLevel.accentColor.copy(alpha = 0.40f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // HorizontalPager cuộn ngang qua các cấp độ HSK 1 - 6
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val level = hskLevels[page]

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(level.gradientColors))
                    .bounceClick(scaleDown = 0.98f) {
                        onOpenHskLevel(level)
                    }
            ) {
                // Hạt bụi sao vũ trụ nhẹ nhàng
                CosmicStarfield(
                    modifier = Modifier.matchParentSize(),
                    particleCount = 18,
                    focusCenterXRatio = 0.88f,
                    focusCenterYRatio = 0.45f
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // ── Thân thẻ ngang: Thông tin HSK bên trái + Nút hành động bên phải ──
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Cột thông tin bên trái
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Badge HSK
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(level.badgeBgColor)
                                        .border(
                                            BorderStroke(0.5.dp, level.accentColor.copy(alpha = 0.50f)),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = level.title,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = level.accentColor
                                    )
                                }

                                Text(
                                    text = level.stageName,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = "${level.standardWordCount} từ chuẩn • ${level.targetScore}",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.5.sp,
                                color = Color(0xFFCBD5E1),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Nút hành động pill bên phải
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(level.accentColor.copy(alpha = 0.18f))
                                .border(
                                    BorderStroke(0.5.dp, level.accentColor.copy(alpha = 0.55f)),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Mở kho từ",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = level.accentColor
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "→",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = level.accentColor
                                )
                            }
                        }
                    }

                    // ── Tab Indicator cuộn ngang ở đáy thẻ ──
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 1.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(hskLevels.size) { index ->
                            val isSelected = pagerState.currentPage == index
                            val dotWidth by animateDpAsState(
                                targetValue = if (isSelected) 14.dp else 4.dp,
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                label = "dotWidth"
                            )
                            val dotColor by animateColorAsState(
                                targetValue = if (isSelected) level.accentColor else Color.White.copy(alpha = 0.25f),
                                label = "dotColor"
                            )

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp)
                                    .height(3.5.dp)
                                    .width(dotWidth)
                                    .clip(CircleShape)
                                    .background(dotColor)
                            )
                        }
                    }
                }
            }
        }
    }
}
