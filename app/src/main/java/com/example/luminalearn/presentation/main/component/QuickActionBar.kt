package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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

private data class QuickHubTile(
    val titleRes: Int,
    val subtitle: String,
    val badge: String,
    val iconRes: Int,
    val accentColor: Color,
    val onClick: () -> Unit
)

/**
 * Thanh Phím Tắt Luyện Nhanh (Quick Practice Hub) thiết kế dạng thẻ Kính mờ Midnight (Glassmorphic Midnight Tiles).
 * Đồng bộ hoàn hảo với hệ thống thẻ chính, viền vi mô neon phát sáng và typography sắc nét.
 */
@Composable
fun QuickActionBar(
    modifier: Modifier = Modifier,
    onRoleplayClick: () -> Unit = {},
    onPinyinClick: () -> Unit = {},
    onVocabClick: () -> Unit = {},
    onRadicalsClick: () -> Unit = {},
    onChallengeClick: () -> Unit = {}
) {
    val tiles = listOf(
        QuickHubTile(
            titleRes = R.string.shortcut_ai_roleplay,
            subtitle = "Phản xạ 1-1",
            badge = "✨ AI",
            iconRes = R.drawable.ic_ai_chat,
            accentColor = Color(0xFF818CF8),
            onClick = onRoleplayClick
        ),
        QuickHubTile(
            titleRes = R.string.shortcut_pinyin,
            subtitle = "4 Thanh điệu",
            badge = "🎧 Audio",
            iconRes = R.drawable.ic_speaker,
            accentColor = Color(0xFF38BDF8),
            onClick = onPinyinClick
        ),
        QuickHubTile(
            titleRes = R.string.shortcut_vocab,
            subtitle = "Nhớ dài hạn",
            badge = "🎴 SRS",
            iconRes = R.drawable.ic_nav_vocabulary,
            accentColor = Color(0xFF34D399),
            onClick = onVocabClick
        ),
        QuickHubTile(
            titleRes = R.string.shortcut_radicals,
            subtitle = "Bộ thủ cốt lõi",
            badge = "✍️ Hanzi",
            iconRes = R.drawable.ic_slides,
            accentColor = Color(0xFFA78BFA),
            onClick = onRadicalsClick
        ),
        QuickHubTile(
            titleRes = R.string.shortcut_challenge,
            subtitle = "Nhận +Sparks",
            badge = "⚡ Quest",
            iconRes = R.drawable.ic_puzzle,
            accentColor = Color(0xFFFBBF24),
            onClick = onChallengeClick
        )
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(tiles) { tile ->
            QuickHubCard(tile = tile)
        }
    }
}

@Composable
private fun QuickHubCard(tile: QuickHubTile) {
    Card(
        modifier = Modifier
            .width(136.dp)
            .height(86.dp)
            .bounceClick(scaleDown = 0.94f) { tile.onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, tile.accentColor.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF161B38),
                            Color(0xFF0F1328)
                        )
                    )
                )
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row: Mini Icon + Mini Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(tile.accentColor.copy(alpha = 0.16f))
                            .border(
                                BorderStroke(0.8.dp, tile.accentColor.copy(alpha = 0.35f)),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = tile.iconRes),
                            contentDescription = null,
                            tint = tile.accentColor,
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Text(
                        text = tile.badge,
                        color = tile.accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                }

                // Bottom Content: Title + Subtitle
                Column {
                    Text(
                        text = stringResource(id = tile.titleRes),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = tile.subtitle,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF94A3B8),
                        maxLines = 1
                    )
                }
            }
        }
    }
}
