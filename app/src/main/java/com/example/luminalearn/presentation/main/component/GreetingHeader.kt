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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient
import com.example.luminalearn.core.ui.effect.bounceClick

private val GoldBadgeText = Color(0xFFFEF08A)
private val MintTextColor = Color(0xFF6EE7B7)
private val PrimaryIndigo = Color(0xFF5C50F6)
private val SkyAccentColor = Color(0xFF38BDF8)

/**
 * Thẻ Chào Mừng Đầu Trang (Greeting Hero Card) phong cách Midnight Cosmic & Glassmorphism.
 * Giúp giao diện trang chủ đồng nhất 100% từ trên xuống dưới, không bị lệch pha với các thẻ bên dưới.
 */
@Composable
fun GreetingHeader(
    modifier: Modifier = Modifier,
    userName: String = "Alex Vance!",
    onAskAiClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, PrimaryIndigo.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animatedMidnightGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1C59),
                        Color(0xFF2E2C80),
                        Color(0xFF3B1E6D),
                        Color(0xFF1E1C59),
                        Color(0xFF0F172A)
                    ),
                    durationMillis = 7000
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                // Header Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Badge: WELCOME BACK
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.12f))
                                .border(
                                    BorderStroke(1.dp, GoldBadgeText.copy(alpha = 0.35f)),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_star),
                                    contentDescription = stringResource(R.string.cd_welcome),
                                    tint = GoldBadgeText,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = stringResource(R.string.welcome_back),
                                    color = GoldBadgeText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.4.sp
                                )
                            }
                        }

                        // Badge: HSK Active Status
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.12f))
                                .border(
                                    BorderStroke(1.dp, MintTextColor.copy(alpha = 0.35f)),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF10B981))
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Lumina Pro",
                                    color = MintTextColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Heading Greeting: "Nǐ hǎo! Sẵn sàng học tiếng Trung,\nAlex Vance!"
                val titlePart1 = stringResource(R.string.greeting_title_part1)
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 23.sp
                            )
                        ) {
                            append(titlePart1)
                        }
                        withStyle(
                            style = SpanStyle(
                                color = SkyAccentColor,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 23.sp
                            )
                        ) {
                            append(userName)
                        }
                    },
                    lineHeight = 31.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Subtitle
                Text(
                    text = stringResource(R.string.daily_goal_done_subtitle),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    ),
                    color = Color(0xFFE0E7FF).copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Action Button: "Luyện cùng Spark AI →"
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .bounceClick(scaleDown = 0.96f) { onAskAiClick() }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_spark_ai),
                            contentDescription = stringResource(R.string.cd_spark_ai),
                            tint = PrimaryIndigo,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.btn_ask_spark_ai),
                            color = PrimaryIndigo,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
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
        }
    }
}