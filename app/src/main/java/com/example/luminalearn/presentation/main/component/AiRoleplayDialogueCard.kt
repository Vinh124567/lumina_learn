package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R

private val CardBackgroundBrush = Brush.horizontalGradient(
    listOf(Color(0xFF5C50F6), Color(0xFF4F46E5))
)
private val PrimaryIndigo = Color(0xFF5C50F6)
private val BadgeGold = Color(0xFFFEF08A)
private val SubtitleLavender = Color(0xFFE0E7FF)

private const val BADGE_TEXT = "✨ TÍNH NĂNG MỚI 4.0"
private const val SUBTITLE_TEXT = "Lumina AI Roleplay"
private const val TITLE_TEXT = "Hội Thoại Tình Huống Nhập Vai Cùng Lumina AI"
private const val DESC_TEXT =
    "Luyện phản xạ giao tiếp trong đời sống: gọi trà sữa, nhà hàng, bắt taxi, mặc cả mua sắm... AI đóng vai bản xứ, phân tích ngữ pháp và gợi ý cách nói tự nhiên nhất!"
private const val ACTION_BUTTON_TEXT = "Nhập vai trò chuyện ngay"

@Composable
fun AiRoleplayDialogueCard(
    modifier: Modifier = Modifier,
    onStartRoleplayClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = CardBackgroundBrush)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            val isWideLayout = maxWidth >= 540.dp

            if (isWideLayout) {
                WideCardLayout(onStartRoleplayClick = onStartRoleplayClick)
            } else {
                CompactCardLayout(onStartRoleplayClick = onStartRoleplayClick)
            }
        }
    }
}

@Composable
private fun WideCardLayout(
    onStartRoleplayClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 20.dp)
        ) {
            RoleplayHeaderBadge()
            Spacer(modifier = Modifier.height(10.dp))
            RoleplayTitleAndDesc()
        }

        RoleplayActionButton(onClick = onStartRoleplayClick)
    }
}

@Composable
private fun CompactCardLayout(
    onStartRoleplayClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        RoleplayHeaderBadge()
        Spacer(modifier = Modifier.height(10.dp))
        RoleplayTitleAndDesc()
        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            RoleplayActionButton(onClick = onStartRoleplayClick)
        }
    }
}

@Composable
private fun RoleplayHeaderBadge() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .border(
                    BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = BADGE_TEXT,
                color = BadgeGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp
            )
        }

        Text(
            text = SUBTITLE_TEXT,
            color = SubtitleLavender,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun RoleplayTitleAndDesc() {
    Text(
        text = TITLE_TEXT,
        color = Color.White,
        fontSize = 17.5.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 23.sp
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = DESC_TEXT,
        color = Color.White.copy(alpha = 0.88f),
        fontSize = 12.5.sp,
        lineHeight = 18.sp
    )
}

@Composable
private fun RoleplayActionButton(
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_ai_chat),
                contentDescription = null,
                tint = PrimaryIndigo,
                modifier = Modifier.size(15.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = ACTION_BUTTON_TEXT,
                color = PrimaryIndigo,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "→",
                color = PrimaryIndigo,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
