package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick

private val GoldBadgeText = Color(0xFFFEF08A)
private val MintTextColor = Color(0xFF6EE7B7)
private val PrimaryIndigo = Color(0xFF5C50F6)

data class LessonCardData(
    val category: String,
    val categoryBgColor: Color,
    val categoryTextColor: Color,
    val durationMins: Int,
    val sparks: Int,
    val title: String,
    val description: String,
    val slideCount: Int = 4
)

/**
 * Thẻ Bài Học Đề Xuất tối ưu hóa cao độ (Lightweight & Lag-Free).
 * Sử dụng nền tĩnh cao cấp không có animation liên tục để danh sách lướt mượt mà 120fps, không hao pin, không nóng máy.
 */
@Composable
fun LessonCard(
    data: LessonCardData,
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0xFF2E3458).copy(alpha = 0.65f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF161A34),
                            Color(0xFF0F1326)
                        )
                    )
                )
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header Row: Category Badge + Duration & Sparks
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.09f))
                            .border(
                                BorderStroke(1.dp, GoldBadgeText.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = data.category,
                            color = GoldBadgeText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.4.sp
                            )
                        )
                    }

                    Text(
                        text = stringResource(R.string.lesson_meta_format, data.durationMins, data.sparks),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MintTextColor
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        lineHeight = 23.sp
                    ),
                    color = Color.White,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Description
                Text(
                    text = data.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.5.sp,
                        lineHeight = 18.sp
                    ),
                    color = Color(0xFFCBD5E1),
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Footer Row: Slides Count + Start Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_slides),
                            contentDescription = stringResource(R.string.cd_slides),
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = stringResource(R.string.interactive_slides_format, data.slideCount),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp
                            ),
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .bounceClick(scaleDown = 0.94f) { onStartClick() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.btn_start_arrow),
                                color = PrimaryIndigo,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
