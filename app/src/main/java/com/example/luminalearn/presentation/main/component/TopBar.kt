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

import androidx.compose.ui.graphics.Brush

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
            modifier = Modifier.height(36.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Glassmorphic Streak Capsule (Midnight Ember)
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF1E1512).copy(alpha = 0.92f))
                    .border(
                        BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.45f)),
                        shape = CircleShape
                    )
                    .bounceClick(scaleDown = 0.92f)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_streak),
                    contentDescription = null,
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(15.dp)
                )
                AnimatedRollingCounter(
                    count = streakDays,
                    textStyle = TextStyle(
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD54F)
                    )
                )
            }

            // Glassmorphic Points/Energy Capsule (Midnight Violet)
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF13152C).copy(alpha = 0.92f))
                    .border(
                        BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.45f)),
                        shape = CircleShape
                    )
                    .bounceClick(scaleDown = 0.92f)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = null,
                    tint = Color(0xFF818CF8),
                    modifier = Modifier.size(15.dp)
                )
                AnimatedRollingCounter(
                    count = points,
                    textStyle = TextStyle(
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE0E7FF)
                    )
                )
            }

            // Speaker Button (Midnight Glass)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF151C30).copy(alpha = 0.92f))
                    .border(
                        BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.6f)),
                        shape = CircleShape
                    )
                    .bounceClick(scaleDown = 0.90f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = stringResource(R.string.cd_speaker),
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            // Avatar Button (Cosmic Glow)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF3730A3), Color(0xFF1E1B4B))
                        )
                    )
                    .border(
                        BorderStroke(1.2.dp, Color(0xFF818CF8).copy(alpha = 0.6f)),
                        shape = CircleShape
                    )
                    .bounceClick(scaleDown = 0.90f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_avatar_default),
                    contentDescription = stringResource(R.string.cd_avatar),
                    tint = Color(0xFFE0E7FF),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}