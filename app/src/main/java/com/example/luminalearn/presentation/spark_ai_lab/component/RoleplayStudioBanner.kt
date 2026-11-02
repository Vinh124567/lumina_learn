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
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape

import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.VoiceWaveformVisualizer
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient

private const val BADGE_VOICE = "Giọng Bắc Kinh"
private const val BANNER_TITLE = "Phòng Luyện Hội Thoại AI"
private const val BANNER_DESC =
    "Thực hành giao tiếp bản xứ theo ngữ cảnh đời sống thực tế: gọi trà sữa, nhà hàng, bắt taxi, mua sắm..."

@Composable
fun RoleplayStudioBanner(
    modifier: Modifier = Modifier
) {
    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = PixelColors.CardBlue, // Sky Blue Arcade tươi sáng, không tím than
        headerTitle = "✨ AI DIALOGUE STUDIO",
        headerBadge = "VOICE 1:1",
        headerColor = PixelColors.PixelBlue
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // 1. Dải trạng thái Mic và Giọng Bắc Kinh
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Waveform live
                val micShape = PixelStepCornerShape(1.5.dp)
                Box(
                    modifier = Modifier
                        .clip(micShape)
                        .background(Color.White)
                        .border(BorderStroke(1.dp, PixelColors.BorderDark), micShape)
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        VoiceWaveformVisualizer(
                            isSpeaking = true,
                            barCount = 4,
                            maxHeight = 11.dp,
                            minHeight = 3.dp,
                            barWidth = 2.dp,
                            barSpacing = 2.dp,
                            gradient = Brush.verticalGradient(
                                listOf(Color(0xFF16A34A), Color(0xFF22C55E))
                            )
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = BADGE_VOICE,
                            color = Color(0xFF15803D),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }

                // Nhãn thời gian thực
                Box(
                    modifier = Modifier
                        .clip(PixelStepCornerShape(1.5.dp))
                        .background(Color(0xFFFEF3C7))
                        .border(BorderStroke(1.dp, PixelColors.BorderDark), PixelStepCornerShape(1.5.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "REALTIME 🎙️",
                        color = Color(0xFF92400E),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2. Tiêu đề & Giới thiệu
            Text(
                text = BANNER_TITLE,
                color = PixelColors.BorderDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = BANNER_DESC,
                color = Color(0xFF334155),
                fontSize = 11.5.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Mini Chips tính năng dạng cartridge
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PixelBannerMiniChip(icon = "🎙️", label = "1 Chạm nói ngay")
                PixelBannerMiniChip(icon = "🎯", label = "Sửa phát âm chuẩn")
                PixelBannerMiniChip(icon = "⚡", label = "+30 Sparks / Lượt")
            }
        }
    }
}

@Composable
private fun PixelBannerMiniChip(
    icon: String,
    label: String
) {
    val chipShape = PixelStepCornerShape(1.5.dp)
    Box(
        modifier = Modifier
            .clip(chipShape)
            .background(Color.White)
            .border(BorderStroke(1.dp, PixelColors.BorderDark), chipShape)
            .padding(horizontal = 7.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 10.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = PixelColors.BorderDark,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}
