package com.example.luminalearn.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.main.AppDestination

private const val TAB_ANIM_DURATION = 280

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    currentRoute: String = AppDestination.Main.route,
    onNavigate: (String) -> Unit = {},
) {
    val navItems = remember {
        listOf(
            BottomNavItem(R.drawable.ic_nav_home, R.string.nav_home, AppDestination.Main.route, 104.dp),
            BottomNavItem(R.drawable.ic_nav_vocabulary, R.string.nav_vocabulary, AppDestination.Vocabulary.route, 116.dp),
            BottomNavItem(R.drawable.ic_nav_explore, R.string.nav_lessons, AppDestination.Lesson.route, 116.dp),
            BottomNavItem(R.drawable.ic_nav_bulb, R.string.nav_spark_ai, AppDestination.SparkAI.route, 110.dp),
            BottomNavItem(R.drawable.ic_nav_trophy, R.string.nav_rewards, AppDestination.Reward.route, 116.dp)
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .height(64.dp)
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = Color(0x1F1E293B),
                ambientColor = Color(0x12000000)
            ),
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
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
    val interactionSource = remember { MutableInteractionSource() }

    // Chiều rộng hoạt ảnh: Tab không chọn = 42dp (tròn), Tab chọn = activeWidth
    val animatedWidth by animateDpAsState(
        targetValue = if (isSelected) activeWidth else 42.dp,
        animationSpec = tween(durationMillis = TAB_ANIM_DURATION, easing = FastOutSlowInEasing),
        label = "tabWidth"
    )

    // Màu nền chuyển sang màu tím thương hiệu thực tế của app Color(0xFF5C50F6)
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF5C50F6) else Color.Transparent,
        animationSpec = tween(durationMillis = TAB_ANIM_DURATION, easing = FastOutSlowInEasing),
        label = "tabBgColor"
    )

    // Màu icon chuyển trắng / slate đậm
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF334155),
        animationSpec = tween(durationMillis = TAB_ANIM_DURATION, easing = FastOutSlowInEasing),
        label = "tabContentColor"
    )

    // Độ mờ của chữ (chỉ hiện dần khi viên thuốc đã mở đủ rộng)
    val textAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(
            durationMillis = if (isSelected) 180 else 100,
            delayMillis = if (isSelected) 70 else 0,
            easing = FastOutSlowInEasing
        ),
        label = "tabTextAlpha"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 4.dp else 0.dp,
        animationSpec = tween(durationMillis = TAB_ANIM_DURATION, easing = FastOutSlowInEasing),
        label = "tabElevation"
    )

    Box(
        modifier = modifier
            .width(animatedWidth)
            .height(42.dp)
            .shadow(
                elevation = elevation,
                shape = CircleShape,
                spotColor = Color(0x405C50F6),
                ambientColor = Color(0x205C50F6)
            )
            .background(color = backgroundColor, shape = CircleShape)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = if (isSelected) Color.White else Color(0xFF5C50F6)),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = if (isSelected) 12.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )

            if (isSelected || animatedWidth > 64.dp) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    color = Color.White.copy(alpha = textAlpha),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier.alpha(textAlpha)
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
