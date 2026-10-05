package com.example.luminalearn.presentation.main.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick

enum class MainHubTab(val titleRes: Int) {
    TODAY(R.string.hub_tab_bento),
    LESSONS(R.string.hub_tab_lessons),
    AI_LAB(R.string.hub_tab_ai)
}

/**
 * Thanh Chuyển Nhánh Bảng Điều Khiển (Segmented Hub Tabs).
 * Cho phép chuyển đổi nhanh 3 không gian: 'Hôm nay', 'Bài học HSK' và 'Trợ lý AI'
 * Vừa vặn trong 1 màn hình mà không cần cuộn dài mệt mỏi.
 */
@Composable
fun MainHubTabs(
    selectedTab: MainHubTab,
    onTabSelected: (MainHubTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainHubTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab

                val bgTargetColor = if (isSelected) Color.White else Color.Transparent
                val animatedBgColor by animateColorAsState(
                    targetValue = bgTargetColor,
                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                    label = "tab_bg_anim"
                )

                val textTargetColor = if (isSelected) Color(0xFF5C50F6) else Color(0xFF64748B)
                val animatedTextColor by animateColorAsState(
                    targetValue = textTargetColor,
                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                    label = "tab_text_anim"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(animatedBgColor)
                        .then(
                            if (isSelected) {
                                Modifier.border(
                                    BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            } else {
                                Modifier
                            }
                        )
                        .bounceClick(scaleDown = 0.95f) { onTabSelected(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = tab.titleRes),
                        fontSize = 12.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = animatedTextColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
