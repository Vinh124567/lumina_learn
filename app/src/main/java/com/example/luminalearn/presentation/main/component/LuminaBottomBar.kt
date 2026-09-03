package com.example.luminalearn.presentation.main.component

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.main.AppDestination

/**
 * Floating Bottom Navigation Bar phong cách hiện đại (hình viên thuốc / floating pill).
 * Bao gồm các tab: Home, Explore, nút nổi bật Spark AI, và Trophy/Reward.
 */
@Composable
fun LuminaBottomBar(
    currentRoute: String = AppDestination.Main.route,
    onNavigate: (String) -> Unit = {},
    onSparkAiClick: () -> Unit = { onNavigate(AppDestination.SparkAI.route) },
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = Color(0x261E293B),
                    ambientColor = Color(0x1A000000)
                ),
            shape = RoundedCornerShape(32.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Home tab
                NavIconButton(
                    iconRes = R.drawable.ic_nav_home,
                    contentDescription = "Home",
                    isSelected = currentRoute == AppDestination.Main.route,
                    onClick = { onNavigate(AppDestination.Main.route) }
                )

                // 2. Explore tab
                NavIconButton(
                    iconRes = R.drawable.ic_nav_explore,
                    contentDescription = "Explore",
                    isSelected = currentRoute == AppDestination.Lesson.route,
                    onClick = { onNavigate(AppDestination.Lesson.route) }
                )

                // 3. Highlighted Spark AI Pill Button
                SparkAiPillButton(
                    onClick = onSparkAiClick
                )

                // 4. Reward / Trophy tab
                NavIconButton(
                    iconRes = R.drawable.ic_nav_trophy,
                    contentDescription = "Rewards",
                    isSelected = currentRoute == AppDestination.Reward.route,
                    onClick = { onNavigate(AppDestination.Reward.route) }
                )
            }
        }
    }
}

@Composable
private fun NavIconButton(
    iconRes: Int,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val tintColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF94A3B8)

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, radius = 22.dp),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = tintColor,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun SparkAiPillButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier
            .height(44.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x665C50F6),
                ambientColor = Color(0x335C50F6)
            )
            .clip(RoundedCornerShape(22.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = Color.White),
                onClick = onClick
            ),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF5C50F6)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_nav_bulb),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Spark AI",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LuminaBottomBarPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F5F9))
            .padding(16.dp)
    ) {
        LuminaBottomBar()
    }
}
