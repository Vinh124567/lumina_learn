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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient

private val GoldBadgeText = Color(0xFFFEF08A)
private val MintTextColor = Color(0xFF6EE7B7)

/**
 * Banner với dải màu Midnight chuyển động động cho Tab 3 (Trắc Nghiệm).
 */
@Composable
fun ReflexQuizStudioBanner(
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
                .animatedMidnightGradient(
                    colors = listOf(
                        Color(0xFF14123E),
                        Color(0xFF2A0845),
                        Color(0xFF4B1248),
                        Color(0xFF1E1C59),
                        Color(0xFF14123E)
                    )
                )
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
                        text = "🎯 SPEED QUIZ 4.0",
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
                    Text(
                        text = "⏱️ Phản xạ 5s",
                        color = MintTextColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Đấu Trường Phản Xạ Nghe - Hiểu",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Thử thách tốc độ tư duy ngôn ngữ không cần dịch sang tiếng Việt. Rèn luyện phản xạ tự nhiên như người bản xứ.",
                fontSize = 12.sp,
                lineHeight = 16.5.sp,
                color = Color.White.copy(alpha = 0.78f)
            )
        }
    }
}
}
