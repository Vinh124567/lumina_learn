package com.example.luminalearn.presentation.main.component

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R

@Composable
fun DailyGoalCard(
    modifier: Modifier = Modifier,
    currentMinutes: Int = 10,
    targetMinutes: Int = 10,
    bonusSparks: Int = 30,
    onStartLessonClick: () -> Unit = {}
) {
    val progress = if (targetMinutes > 0) (currentMinutes.toFloat() / targetMinutes).coerceIn(0f, 1f) else 0f
    val percentage = (progress * 100).toInt()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.daily_goal_tracker_title),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color(0xFF64748B)
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFEDE9FE))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.goal_mins_format, currentMinutes, targetMinutes),
                        color = Color(0xFF5C50F6),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Percentage + Text: "100% completed today"
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = stringResource(R.string.completed_today),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = Color(0xFF5C50F6),
                trackColor = Color(0xFFEDE9FE),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Footer Row: Spark Bonus Info + "Start 3-min Lesson" Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left bonus note
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bolt),
                        contentDescription = stringResource(R.string.cd_sparks),
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier
                            .size(14.dp)
                            .padding(top = 2.dp)
                    )
                    Text(
                        text = stringResource(R.string.earn_sparks_bonus_format, bonusSparks),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        ),
                        color = Color(0xFF64748B)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right Button "Start 3-min Lesson"
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFF5C50F6),
                            shape = CircleShape
                        )
                        .background(Color.White)
                        .clickable { onStartLessonClick() }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = stringResource(R.string.cd_play),
                            tint = Color(0xFF5C50F6),
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = stringResource(R.string.btn_start_lesson),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                lineHeight = 13.sp,
                                textAlign = TextAlign.Center
                            ),
                            color = Color(0xFF5C50F6)
                        )
                    }
                }
            }
        }
    }
}
