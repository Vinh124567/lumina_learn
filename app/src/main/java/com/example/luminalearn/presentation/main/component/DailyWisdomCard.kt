package com.example.luminalearn.presentation.main.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.ZenCalligraphyAura
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.data.model.DailyWisdomDto
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val GoldBadgeText = Color(0xFFFEF08A)
private val MintTextColor = Color(0xFF6EE7B7)
private val AccentBorder = Color(0xFF6366F1)

/**
 * Thẻ Danh Ngôn / Châm Ngôn Hán Ngữ thiết kế tinh gọn (Compact & Poetic).
 * Tối ưu chiều cao vừa vặn, không bị chiếm diện tích màn hình mà vẫn đầy đủ chữ Hán, Pinyin và bản dịch.
 */
@Composable
fun DailyWisdomCard(
    modifier: Modifier = Modifier,
    wisdom: DailyWisdomDto? = null,
    quote: String = stringResource(R.string.daily_wisdom_quote),
    author: String = stringResource(R.string.daily_wisdom_author),
    onRefreshClick: () -> Unit = {}
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 400),
        label = "WisdomRefreshRotation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, AccentBorder.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animatedMidnightGradient(
                    colors = listOf(
                        Color(0xFF0B101D),
                        Color(0xFF151C30),
                        Color(0xFF1E2640),
                        Color(0xFF151C30),
                        Color(0xFF0B101D)
                    ),
                    durationMillis = 8000
                )
        ) {
            ZenCalligraphyAura(
                modifier = Modifier.matchParentSize(),
                moteCount = 14
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 13.dp)
            ) {
                // Header Row: Badge bên trái + Tác giả & Nút đổi bên phải
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(
                                BorderStroke(1.dp, GoldBadgeText.copy(alpha = 0.35f)),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.daily_wisdom_badge),
                            color = GoldBadgeText,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val authorName = wisdom?.author ?: author
                        Text(
                            text = formatAuthor(authorName),
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = GoldBadgeText
                        )

                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.12f),
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .border(
                                    BorderStroke(0.8.dp, Color.White.copy(alpha = 0.2f)),
                                    shape = CircleShape
                                )
                                .bounceClick(scaleDown = 0.9f) {
                                    rotationAngle += 360f
                                    onRefreshClick()
                                }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_refresh),
                                    contentDescription = stringResource(R.string.cd_refresh),
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(13.dp)
                                        .rotate(animatedRotation)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Content: Chữ Hán nổi bật
                if (wisdom != null && wisdom.chinese.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = wisdom.chinese,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.5.sp,
                            color = Color.White
                        )

                        if (wisdom.pinyin.isNotBlank()) {
                            Text(
                                text = wisdom.pinyin,
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.5.sp,
                                color = MintTextColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    val meaningText = wisdom.vietnamese.ifBlank { wisdom.meaning }
                    if (meaningText.isNotBlank()) {
                        Text(
                            text = "“$meaningText”",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp,
                            color = Color(0xFFE0E7FF)
                        )
                    }
                } else {
                    Text(
                        text = quote,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp,
                            lineHeight = 19.sp
                        ),
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun formatAuthor(author: String): String =
    if (author.startsWith("—")) author else "— $author"
