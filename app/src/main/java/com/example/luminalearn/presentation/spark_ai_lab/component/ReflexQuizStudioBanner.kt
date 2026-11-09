package com.example.luminalearn.presentation.spark_ai_lab.component

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
 * Banner chuẩn Pixel 16-Bit JRPG cho Tab 3 (Trắc Nghiệm).
 */
@Composable
fun ReflexQuizStudioBanner(
    modifier: Modifier = Modifier
) {
    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = PixelColors.CardOrange, // Cam phiêu lưu RPG ấm áp, không tím than
        headerTitle = "🎯 SPEED QUIZ ARENA",
        headerBadge = "5S REFLEX",
        headerColor = PixelColors.PixelOrange
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // Tiêu đề
            Text(
                text = "Đấu Trường Phản Xạ Nghe - Hiểu",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.BorderDark,
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Mô tả
            Text(
                text = "Thử thách tốc độ tư duy ngôn ngữ không cần dịch sang tiếng Việt. Rèn luyện phản xạ tự nhiên như người bản xứ.",
                fontSize = 11.5.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF334155)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tag hỗ trợ
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(PixelStepCornerShape(1.dp))
                        .background(PixelColors.PixelOrange)
                )
                Text(
                    text = "Luyện chọn đáp án chuẩn xác trong vòng 5 giây",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = PixelColors.BorderDark
                )
            }
        }
    }
}
