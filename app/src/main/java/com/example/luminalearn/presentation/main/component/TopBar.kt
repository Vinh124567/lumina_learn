package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.AnimatedRollingCounter
import com.example.luminalearn.core.ui.effect.bounceClick

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    streakDays: Int = 5,
    points: Int = 240,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo Header
        Image(
            painter = painterResource(id = R.drawable.ic_header),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.height(34.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Glassmorphic Streak Capsule
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFFEF3C7).copy(alpha = 0.9f))
                    .border(
                        BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.35f)),
                        shape = CircleShape
                    )
                    .bounceClick(scaleDown = 0.92f)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_streak),
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(14.dp)
                )
                AnimatedRollingCounter(
                    count = streakDays,
                    textStyle = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                )
                Text(
                    text = "Ngày",
                    color = Color(0xFFB45309),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Glassmorphic Points/Energy Capsule
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFEDE9FE).copy(alpha = 0.9f))
                    .border(
                        BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.35f)),
                        shape = CircleShape
                    )
                    .bounceClick(scaleDown = 0.92f)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = null,
                    tint = Color(0xFF6366F1),
                    modifier = Modifier.size(14.dp)
                )
                AnimatedRollingCounter(
                    count = points,
                    textStyle = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6366F1)
                    )
                )
            }

            // Speaker Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF8FAFC))
                    .border(
                        BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        shape = CircleShape
                    )
                    .bounceClick(scaleDown = 0.90f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = stringResource(R.string.cd_speaker),
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(16.dp)
                )
            }

            // Avatar Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(
                        BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.3f)),
                        shape = CircleShape
                    )
                    .bounceClick(scaleDown = 0.90f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_avatar_default),
                    contentDescription = stringResource(R.string.cd_avatar),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}