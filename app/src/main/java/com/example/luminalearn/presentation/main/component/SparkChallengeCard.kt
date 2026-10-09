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
import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient
import com.example.luminalearn.core.ui.effect.bounceClick

private val GoldBadgeText = Color(0xFFFEF08A)
private val MintTextColor = Color(0xFF6EE7B7)
private val AccentBorder = Color(0xFF818CF8)

private val ChallengeBtnGradient = Brush.horizontalGradient(
    listOf(Color(0xFFF59E0B), Color(0xFFEA580C))
)

/**
 * Thẻ Thử Thách Hằng Ngày (Spark Challenge) chuẩn Midnight Cosmic Gradient đồng bộ theo VocabHeroBanner.
 */
@Composable
fun SparkChallengeCard(
    modifier: Modifier = Modifier,
    bonusSparks: Int = 20,
    title: String = stringResource(R.string.challenge_title),
    description: String = stringResource(R.string.challenge_desc),
    onCompleteClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, AccentBorder.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animatedMidnightGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF312E81),
                        Color(0xFF4338CA),
                        Color(0xFF1E1B4B),
                        Color(0xFF0F172A)
                    ),
                    durationMillis = 6000
                )
        ) {
            CosmicStarfield(
                modifier = Modifier.matchParentSize(),
                particleCount = 30,
                focusCenterXRatio = 0.85f,
                focusCenterYRatio = 0.38f
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
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
                            Text(
                                text = stringResource(R.string.challenge_badge_title),
                                color = GoldBadgeText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.4.sp
                            )
                        }

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
                                    text = stringResource(R.string.challenge_sparks_badge, bonusSparks),
                                    color = MintTextColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtitle / Description
                Text(
                    text = description,
                    color = Color(0xFFE0E7FF).copy(alpha = 0.85f),
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action Button: Complete & Earn
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ChallengeBtnGradient)
                        .bounceClick(scaleDown = 0.96f) { onCompleteClick() }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_puzzle),
                                contentDescription = stringResource(R.string.cd_puzzle),
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = stringResource(R.string.btn_complete_challenge, bonusSparks),
                                style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp
                                ),
                                color = Color.White,
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
