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
import androidx.compose.foundation.shape.CutCornerShape
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
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans

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
    PixelCardBox(
        modifier = Modifier
            .width(138.dp)
            .height(98.dp),
        backgroundColor = Color.White,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.8.dp,
        shadowOffset = 3.dp,
        headerTitle = tile.badge,
        headerColor = tile.accentColor.copy(alpha = 0.25f),
        showGrille = true,
        onClick = tile.onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 9.dp, vertical = 7.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon ô vuông Pixel RPG dập nổi
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .background(
                        tile.accentColor,
                        shape = CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp)
                    )
                    .border(
                        BorderStroke(1.2.dp, PixelColors.BorderDark),
                        shape = CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = tile.iconRes),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }

            // Bottom Content: Title + Subtitle
            Column {
                Text(
                    text = stringResource(id = tile.titleRes),
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = tile.subtitle,
                    fontFamily = PlusJakartaSans,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = PixelColors.MutedText,
                    maxLines = 1
                )
            }
        }
    }
}
