package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.VoiceWaveformVisualizer
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient

private val BannerGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF14123E),
        Color(0xFF1E1C59),
        Color(0xFF2E2C80),
        Color(0xFF3F3DBA)
    )
)
private val GoldBadgeText = Color(0xFFFEF08A)
private val GreenDotColor = Color(0xFF22C55E)
private val MintTextColor = Color(0xFF6EE7B7)

private const val BADGE_STUDIO = "✨ AI DIALOGUE 4.0"
private const val BADGE_VOICE = "Giọng Bắc Kinh"
private const val BANNER_TITLE = "Phòng Luyện Hội Thoại AI"
private const val BANNER_DESC =
    "Thực hành giao tiếp bản xứ theo ngữ cảnh đời sống thực tế: gọi trà sữa, nhà hàng, bắt taxi, mua sắm..."

@Composable
fun RoleplayStudioBanner(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0xFF5C50F6).copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animatedMidnightGradient()
        ) {
            CosmicStarfield(
                modifier = Modifier.matchParentSize(),
                particleCount = 45,
                focusCenterXRatio = 0.85f,
                focusCenterYRatio = 0.35f
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 15.dp)
            ) {
                BannerHeaderBadges()
                Spacer(modifier = Modifier.height(10.dp))
                BannerTitleAndDescription()
                Spacer(modifier = Modifier.height(12.dp))
                BannerMiniChips()
            }
        }
    }
}

@Composable
private fun BannerHeaderBadges() {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
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
                text = BADGE_STUDIO,
                color = GoldBadgeText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp,
                maxLines = 1
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .border(
                    BorderStroke(1.dp, GreenDotColor.copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoiceWaveformVisualizer(
                    isSpeaking = true,
                    barCount = 4,
                    maxHeight = 12.dp,
                    minHeight = 4.dp,
                    barWidth = 2.5.dp,
                    barSpacing = 2.dp,
                    gradient = Brush.verticalGradient(
                        listOf(Color(0xFF22C55E), Color(0xFF6EE7B7))
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = BADGE_VOICE,
                    color = MintTextColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun BannerTitleAndDescription() {
    Text(
        text = BANNER_TITLE,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = BANNER_DESC,
        color = Color.White.copy(alpha = 0.85f),
        fontSize = 12.5.sp,
        lineHeight = 17.sp
    )
}

@Composable
private fun BannerMiniChips() {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MiniChip(icon = "🎙️", label = "1 Chạm nói ngay")
        MiniChip(icon = "💡", label = "Sửa lỗi câu chuẩn")
        MiniChip(icon = "⚡", label = "Thưởng +30 Tia Sáng")
    }
}

@Composable
private fun MiniChip(
    icon: String,
    label: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.10f))
            .border(
                BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 9.dp, vertical = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 11.5.sp)
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                color = Color(0xFFE2E8F0),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}
