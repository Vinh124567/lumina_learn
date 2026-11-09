package com.example.luminalearn.presentation.spark_ai_lab.component

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape

/**
 * Giao diện Bento Grid Portal cho Spark AI Lab:
 * - Khối Hero Bento trên cùng: Phòng Hội Thoại AI (chức năng chính).
 * - Hai khối Bento 2 cột bên dưới: Trợ Lý Soạn Bài & Đấu Trường Phản Xạ.
 * - Chạm vào bất kỳ khối nào sẽ chuyển hẳn sang màn hình riêng biệt của tính năng đó.
 */
@Composable
fun SparkAiBentoGrid(
    onNavigateToRoleplay: () -> Unit,
    onNavigateToPromptStudio: () -> Unit,
    onNavigateToReflexQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tiêu đề khu vực Bento
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TRẠM TRÍ TUỆ NHÂN TẠO SPARK AI",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.BorderDark,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "3 TRẠM HUẤN LUYỆN",
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
        }

        // ── 1. KHỐI HERO BENTO: PHÒNG HỘI THOẠI AI BẢN XỨ ──
        PixelCardBox(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onNavigateToRoleplay),
            backgroundColor = PixelColors.CardBlue,
            headerTitle = "🎙️ AI DIALOGUE STUDIO",
            headerBadge = "VOICE LIVE 1:1",
            headerColor = PixelColors.PixelBlue
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Khung Icon lớn dập nổi
                    val iconShape = PixelStepCornerShape(2.dp)
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(iconShape)
                            .background(Color.White)
                            .border(BorderStroke(1.2.dp, PixelColors.BorderDark), iconShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🤖", fontSize = 26.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Phòng Hội Thoại Bản Xứ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.BorderDark,
                            letterSpacing = 0.2.sp
                        )

                        Text(
                            text = "Thực hành giao tiếp thực tế: Gọi HeyTea, bắt taxi, mua sắm...",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            lineHeight = 15.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "3 Chủ đề • 4 Kịch bản đời sống",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.PixelBlue
                        )
                    }
                }

                // Nút trượt chuyển màu tương tác: Kéo sang phải để chuyển màn
                ArcadeSwipeButton(
                    text = "KÉO SANG PHẢI ĐỂ VÀO THOẠI",
                    height = 42.dp,
                    thumbIcon = "🎙️",
                    activeGradient = listOf(
                        Color(0xFF0284C7), // Xanh Sapphire Cyan
                        Color(0xFF38BDF8), // Cyan sáng
                        Color(0xFF10B981)  // Xanh Ngọc Lục Bảo
                    ),
                    onSwiped = onNavigateToRoleplay
                )
            }
        }

        // ── 2. HÀNG BENTO 2 CỘT: TRỢ LÝ SOẠN BÀI & ĐẤU TRƯỜNG PHẢN XẠ ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Bento Trái: Trợ Lý Soạn Bài
            CompactBentoCard(
                modifier = Modifier.weight(1f),
                iconEmoji = "⚡",
                title = "Trợ Lý Soạn Bài",
                subtitle = "Sinh 3 bài học vi mô siêu tốc từ mọi ý tưởng",
                tagText = "2S",
                headerTitle = "PROMPT",
                cardBgColor = PixelColors.CardYellow,
                headerColor = PixelColors.PixelYellow,
                buttonText = "TRƯỢT SOẠN BÀI",
                activeGradient = listOf(
                    Color(0xFFEAB308), // Vàng Amber
                    Color(0xFFFBBF24), // Vàng Chanh
                    Color(0xFF10B981)  // Xanh Neon
                ),
                onClick = onNavigateToPromptStudio
            )

            // Bento Phải: Đấu Trường Phản Xạ
            CompactBentoCard(
                modifier = Modifier.weight(1f),
                iconEmoji = "🎯",
                title = "Đấu Trường 5s",
                subtitle = "Luyện phản xạ nghe-hiểu tức thì không cần dịch",
                tagText = "5S",
                headerTitle = "REFLEX",
                cardBgColor = PixelColors.CardOrange,
                headerColor = PixelColors.PixelOrange,
                buttonText = "TRƯỢT VÀO THI",
                activeGradient = listOf(
                    Color(0xFFEA580C), // Cam Hổ Phách
                    Color(0xFFF97316), // Cam Sáng
                    Color(0xFFEAB308)  // Vàng Rực Rỡ
                ),
                onClick = onNavigateToReflexQuiz
            )
        }
    }
}

@Composable
private fun CompactBentoCard(
    iconEmoji: String,
    title: String,
    subtitle: String,
    tagText: String,
    headerTitle: String,
    cardBgColor: Color,
    headerColor: Color,
    buttonText: String,
    activeGradient: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PixelCardBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        backgroundColor = cardBgColor,
        headerTitle = "$iconEmoji $headerTitle",
        headerBadge = tagText,
        headerColor = headerColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val iconShape = PixelStepCornerShape(1.5.dp)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(iconShape)
                        .background(Color.White)
                        .border(BorderStroke(1.dp, PixelColors.BorderDark), iconShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = iconEmoji, fontSize = 17.sp)
                }

                Text(
                    text = title,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    letterSpacing = 0.2.sp
                )
            }

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color(0xFF334155),
                lineHeight = 13.5.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )

            // Nút trượt chuyển màu mini
            ArcadeSwipeButton(
                text = buttonText,
                height = 36.dp,
                thumbIcon = iconEmoji,
                activeGradient = activeGradient,
                onSwiped = onClick
            )
        }
    }
}
