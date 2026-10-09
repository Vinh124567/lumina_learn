package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.data.model.LessonDto

/**
 * Thẻ Hero Bài Học HSK Trọng Tâm trên cùng.
 * Thiết kế Midnight Indigo sang trọng với tiến độ học neon và nút học tiếp nổi bật.
 */
@Composable
fun HeroLessonCard(
    lesson: LessonDto?,
    onStartLessonClick: (String) -> Unit,
    onViewAllLessonsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lessonId = lesson?.id ?: "1"
    Card(
        modifier = modifier
            .fillMaxWidth()
            .bounceClick(scaleDown = 0.98f) { onStartLessonClick(lessonId) },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            1.2.dp,
            Brush.linearGradient(
                listOf(Color(0xFF00F2FE).copy(alpha = 0.6f), Color(0xFF6366F1).copy(alpha = 0.3f))
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF0F1532), Color(0xFF161E48), Color(0xFF10132B))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge trạng thái
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFF00F2FE).copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF00F2FE), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.bento_hero_badge),
                            color = Color(0xFF00F2FE),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Nút xem tất cả
                    Text(
                        text = stringResource(R.string.bento_hero_all_lessons),
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onViewAllLessonsClick() }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = lesson?.title?.ifBlank { null } ?: stringResource(R.string.bento_hero_title),
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Thanh tiến độ bài học
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.bento_hero_progress),
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF1E293B))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.65f)
                                    .height(6.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF00F2FE), Color(0xFF8B5CF6))
                                        )
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Nút tiếp tục nổi bật
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                                )
                            )
                            .padding(horizontal = 14.dp, vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.bento_hero_btn_resume),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
