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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val GoldBadgeText = Color(0xFFFEF08A)
private val MintTextColor = Color(0xFF6EE7B7)
private val PrimaryIndigo = Color(0xFF5C50F6)

data class LessonCardData(
    val category: String,
    val categoryBgColor: Color,
    val categoryTextColor: Color,
    val durationMins: Int,
    val sparks: Int,
    val title: String,
    val description: String,
    val slideCount: Int = 4
)

/**
 * Thẻ Bài Học Đề Xuất phong cách Pixel Art 8-bit Quest Card:
 * - Khung PixelCardBox viền đen 2dp, bóng cứng 3D 4dp.
 * - Badge Pixel, phông chữ sắc nét, nút bấm Arcade dập nổi [ HỌC NGAY ▶ ].
 */
@Composable
fun LessonCard(
    data: LessonCardData,
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit = {}
) {
    PixelCardBox(
        modifier = modifier,
        backgroundColor = Color.White,
        headerTitle = "QUEST CARTRIDGE",
        headerBadge = data.category,
        headerColor = PixelColors.PixelYellow,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 2.dp,
        shadowOffset = 4.dp,
        onClick = onStartClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Header Row: Duration & Coin Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "⏱️ ${data.durationMins} MINS",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.MutedText
                )

                com.example.luminalearn.presentation.vocabulary.component.PixelCoinBadge(amount = data.sparks)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = data.title,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = PixelColors.BorderDark,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Description
            Text(
                text = data.description,
                fontFamily = PlusJakartaSans,
                fontSize = 11.5.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF475569),
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer Row: Slides Count + Start Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_slides),
                        contentDescription = stringResource(R.string.cd_slides),
                        tint = PixelColors.BorderDark,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${data.slideCount} SLIDES",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.MutedText
                    )
                }

                PixelArcadeButton(
                    text = "HỌC NGAY ▶",
                    onClick = onStartClick,
                    backgroundColor = PixelColors.PixelYellow,
                    textColor = PixelColors.BorderDark,
                    fontSize = 11f,
                    contentPadding = 6.dp,
                    shadowOffset = 2.5.dp
                )
            }
        }
    }
}
