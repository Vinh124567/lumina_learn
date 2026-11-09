package com.example.luminalearn.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.main.AppDestination
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape

/**
 * Thanh Bottom Navigation Bar phong cách Băng Game 16-Bit Console / Arcade Controller Dock:
 * - Khung ngoài: PixelCardBox viền đôi chỉ vàng hoàng gia, góc bậc thang 90° PixelStepCornerShape.
 * - 4 ốc vít đinh tán kim loại bạc dập nổi ở 4 góc dock.
 * - Các nút tab: Thiết kế dạng gạch Arcade Cartridge, phím dập nổi 3 tầng (Top bevel highlight, Body, Shadow bottom).
 * - Tab được chọn (Active): Nổi bật với nền tím PixelPurple, viền đen 1.5dp, bóng 2dp và text in đậm rõ nét.
 * - Tab nhàn rỗi (Inactive): Icon màu tối tương phản cao, click có phản hồi nảy xúc giác.
 */
@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    currentRoute: String = AppDestination.Main.route,
    onNavigate: (String) -> Unit = {},
) {
    val navItems = remember {
        listOf(
            BottomNavItem(R.drawable.ic_nav_home, R.string.nav_home, AppDestination.Main.route, 110.dp),
            BottomNavItem(R.drawable.ic_nav_vocabulary, R.string.nav_vocabulary, AppDestination.Vocabulary.route, 106.dp),
            BottomNavItem(R.drawable.ic_nav_explore, R.string.nav_lessons, AppDestination.Lesson.route, 104.dp),
            BottomNavItem(R.drawable.ic_nav_bulb, R.string.nav_spark_ai, AppDestination.SparkAI.route, 102.dp),
            BottomNavItem(R.drawable.ic_nav_trophy, R.string.nav_rewards, AppDestination.Reward.route, 114.dp)
        )
    }

    PixelCardBox(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        backgroundColor = PixelColors.CardBg,
        borderColor = PixelColors.BorderDark,
        innerBorderColor = PixelColors.BorderGold,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.8.dp,
        shadowOffset = 3.dp,
        stepSize = 4.dp,
        showRivets = false,
        showGrille = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            navItems.forEach { item ->
                NavTabItem(
                    iconRes = item.iconRes,
                    title = stringResource(item.titleRes),
                    activeWidth = item.activeWidth,
                    isSelected = currentRoute == item.route,
                    onClick = { onNavigate(item.route) }
                )
            }
        }
    }
}

private data class BottomNavItem(
    val iconRes: Int,
    val titleRes: Int,
    val route: String,
    val activeWidth: Dp
)

@Composable
private fun NavTabItem(
    iconRes: Int,
    title: String,
    activeWidth: Dp,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Chiều rộng hoạt ảnh: Spring physics co giãn mượt mà
    val animatedWidth by animateDpAsState(
        targetValue = if (isSelected) activeWidth else 42.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "tabWidth"
    )

    // Hiệu ứng micro-bounce cho icon khi tab được chọn
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.12f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "tabIconScale"
    )

    // Độ mờ của nhãn chữ (chỉ hiện khi đã mở đủ rộng)
    val textAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(
            durationMillis = if (isSelected) 160 else 80,
            delayMillis = if (isSelected) 60 else 0,
            easing = FastOutSlowInEasing
        ),
        label = "tabTextAlpha"
    )

    val activeShape = PixelStepCornerShape(2.5.dp)
    val inactiveShape = PixelStepCornerShape(2.dp)

    Box(
        modifier = modifier
            .width(animatedWidth)
            .height(42.dp)
    ) {
        if (isSelected) {
            // Lớp bóng cứng 3D kim loại ánh bạc Slate phía sau
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = 1.8.dp, y = 1.8.dp)
                    .clip(activeShape)
                    .background(PixelColors.ShadowDark)
                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), activeShape)
            )

            // Mặt trước của phím bấm Arcade Active
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(activeShape)
                    .background(PixelColors.PixelBlue)
                    .border(BorderStroke(1.5.dp, PixelColors.BorderDark), activeShape)
                    .bounceClick(scaleDown = 0.92f, onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                // Đường gờ Bevel phản quang trên nóc phím
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(top = 1.dp, start = 1.dp, end = 1.dp)
                        .border(
                            BorderStroke(1.dp, Color.White.copy(alpha = 0.45f)),
                            PixelStepCornerShape(1.8.dp)
                        )
                )

                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier
                            .size(19.dp)
                            .graphicsLayer {
                                scaleX = iconScale
                                scaleY = iconScale
                            }
                    )

                    if (animatedWidth > 60.dp) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = title,
                            color = Color.White.copy(alpha = textAlpha),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            maxLines = 1,
                            softWrap = false,
                            modifier = Modifier.alpha(textAlpha)
                        )
                    }
                }
            }
        } else {
            // Phím bấm Arcade Inactive: Nút phẳng gọn gàng với góc pixel 90°
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(inactiveShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = PixelColors.PixelBlue),
                        onClick = onClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(21.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBottomBarPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F5F9))
            .padding(16.dp)
    ) {
        AppBottomBar(
            currentRoute = AppDestination.Vocabulary.route
        )
    }
}
