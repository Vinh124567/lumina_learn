package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.lesson.HskLevelCardData
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Thanh chọn cấp độ HSK dạng Mini Game Cartridge Pixel 16-Bit Arcade:
 * - Viền đen đậm đặc trưng console retro, bóng đổ cứng 3D dập nổi.
 * - Trạng thái Active: Màu vàng Coin Mario dập nổi, phản hồi tactile chân thực.
 */
@Composable
fun HskLevelCapsuleSelector(
    hskLevels: List<HskLevelCardData>,
    selectedHskId: String,
    onSelectHskLevel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        hskLevels.forEach { level ->
            val isSelected = level.id.equals(selectedHskId, ignoreCase = true)
            val chipBg = if (isSelected) PixelColors.PixelYellow else Color.White
            val textColor = PixelColors.BorderDark
            val badgeBg = if (isSelected) PixelColors.BorderDark else Color(0xFFE2E8F0)
            val badgeTextColor = if (isSelected) PixelColors.PixelYellow else Color(0xFF475569)

            Box(
                modifier = Modifier
                    .padding(bottom = 3.dp, end = 2.dp)
                    .bounceClick(scaleDown = 0.94f) { onSelectHskLevel(level.id) }
            ) {
                // 1. Bóng cứng 3D phía sau (Hard Shadow)
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .offset(x = 2.5.dp, y = 2.5.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(PixelColors.ShadowDark)
                )

                // 2. Mặt thẻ Cartridge Pixel
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(chipBg)
                        .border(BorderStroke(1.8.dp, PixelColors.BorderDark), RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Text(
                                text = "▶ ",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark
                            )
                        }

                        Text(
                            text = level.title,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.5.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = textColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // Huy hiệu số bài học kiểu Pixel Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(badgeBg)
                                .padding(horizontal = 5.dp, vertical = 1.5.dp)
                        ) {
                            Text(
                                text = "${level.lessonCount}",
                                fontFamily = PlusJakartaSans,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = badgeTextColor,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
