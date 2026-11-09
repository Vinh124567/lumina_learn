package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.data.model.LessonDto

import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelCornerStyle
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Thẻ Hero Bài Học HSK Trọng Tâm phong cách Pixel Art 8-bit Main Quest:
 * - Khung PixelCardBox màu Xanh Cyber Cyan viền đen 2dp, bóng cứng 3D 4dp.
 * - Badge [ ★ MAIN QUEST ] và nút [ ALL MAPS ▶ ].
 * - Thanh đo tiến độ bài học phân đoạn pixel.
 * - Phím bấm Arcade dập nổi [ RESUME ▶ ].
 */
@Composable
fun HeroLessonCard(
    lesson: LessonDto?,
    onStartLessonClick: (String) -> Unit,
    onViewAllLessonsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lessonId = lesson?.id ?: "1"

    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color(0xFF0284C7), // Xanh Cyber Cyan đậm đà
        headerTitle = "⚔️ MAIN QUEST CARTRIDGE",
        headerBadge = "👑 HSK 1",
        headerColor = PixelColors.PixelYellow,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 2.dp,
        shadowOffset = 4.dp,
        showGrille = true,
        cornerStyle = PixelCornerStyle.Rivets,
        onClick = { onStartLessonClick(lessonId) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Hàng 1: Coin loot + EXP Badge + Nút xem tất cả bài học
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    com.example.luminalearn.presentation.vocabulary.component.PixelCoinBadge(amount = 50)
                    PixelBadge(
                        text = "⭐ +100 EXP",
                        backgroundColor = Color(0xFFFEF08A),
                        textColor = PixelColors.BorderDark
                    )
                }

                val mapsShape = PixelStepCornerShape(1.8.dp)
                Box(
                    modifier = Modifier
                        .clip(mapsShape)
                        .background(Color.White)
                        .border(BorderStroke(1.2.dp, PixelColors.BorderDark), mapsShape)
                        .bounceClick(scaleDown = 0.92f) { onViewAllLessonsClick() }
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ALL MAPS 🗺️",
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tiêu đề bài học kèm icon cuộn thư 📜
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "📜 ",
                    fontSize = 15.sp
                )
                Text(
                    text = lesson?.title?.ifBlank { null } ?: stringResource(R.string.bento_hero_title),
                    fontFamily = PlusJakartaSans,
                    color = Color.White,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.2.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Hàng 3: RPG Progress Bar + Nút Resume dập nổi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                com.example.luminalearn.presentation.vocabulary.component.PixelRpgProgressBar(
                    label = "⚡ EXP 65%",
                    progress = 0.65f,
                    barColor = PixelColors.PixelYellow,
                    labelColor = PixelColors.PixelYellow,
                    emptyColor = Color(0xFF0369A1),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Nút tiếp tục Arcade
                PixelArcadeButton(
                    text = "RESUME ▶",
                    onClick = { onStartLessonClick(lessonId) },
                    backgroundColor = PixelColors.PixelYellow,
                    textColor = PixelColors.BorderDark,
                    fontSize = 11.5f,
                    contentPadding = 6.dp,
                    shadowOffset = 2.5.dp
                )
            }
        }
    }
}
