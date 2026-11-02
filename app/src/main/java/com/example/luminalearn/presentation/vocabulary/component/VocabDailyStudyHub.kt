package com.example.luminalearn.presentation.vocabulary.component

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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val PrimaryIndigo = Color(0xFF5C50F6)
private val BrandIndigoLight = Color(0xFF6366F1)
private val GoldBadgeText = Color(0xFFFEF08A)
private val SkyAccentColor = Color(0xFF38BDF8)
private val MintTextColor = Color(0xFF6EE7B7)
private val AmberAccent = Color(0xFFF59E0B)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)

/**
 * Cụm 2 ô luyện tập hàng ngày (SRS Flashcard & Phản xạ HSK) phong cách Pixel Art / Retro Arcade:
 * - Ô 1 (Trái): Flashcard SRS Ebbinghaus (Băng Quest 8-bit, Badge SRS QUEST, nút START ▶).
 * - Ô 2 (Phải): Phản xạ nhanh HSK (Băng Arcade Fighter, Badge REFLEX ⚡, nút FIGHT ▶).
 */
@Composable
fun VocabDailyPracticeDuo(
    dueTodayCount: Int,
    onStartFlashcard: () -> Unit,
    onStartQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(156.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Ô 1 (Trái): SRS Flashcard (Pixel Quest Box màu vàng retro)
        PixelCardBox(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            backgroundColor = Color(0xFFFEFCE8), // Kem vàng GameBoy
            borderColor = PixelColors.BorderDark,
            shadowColor = PixelColors.ShadowDark,
            borderWidth = 2.dp,
            shadowOffset = 4.dp,
            cornerRadius = 8.dp,
            onClick = onStartFlashcard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header: Badge Pixel + Icon khối vuông retro
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PixelBadge(
                        text = "SRS QUEST",
                        backgroundColor = PixelColors.PixelYellow,
                        textColor = PixelColors.BorderDark,
                        borderColor = PixelColors.BorderDark
                    )

                    // Icon pixel square box
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(PixelColors.PixelYellow)
                            .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_flashcard),
                            contentDescription = null,
                            tint = PixelColors.BorderDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Thân thẻ: Chỉ số từ vựng phong cách Arcade Score
                Column {
                    if (dueTodayCount > 0) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$dueTodayCount",
                                fontFamily = PlusJakartaSans,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "TỪ CẦN ÔN",
                                fontFamily = PlusJakartaSans,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF15803D),
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "CLEAR! 100%",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF15803D)
                        )
                    }
                    Text(
                        text = if (dueTodayCount > 0) "Ebbinghaus +20 XP" else "Đã xong quest hôm nay",
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.MutedText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Nút hành động Arcade dập nổi
                PixelArcadeButton(
                    text = if (dueTodayCount > 0) "START ▶" else "PRACTICE ▶",
                    onClick = onStartFlashcard,
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = PixelColors.PixelYellow,
                    textColor = PixelColors.BorderDark,
                    fontSize = 10.5f,
                    contentPadding = 6.dp,
                    shadowOffset = 2.5.dp
                )
            }
        }

        // Ô 2 (Phải): Phản xạ nhanh HSK (Pixel Arcade Fighter màu cam neon)
        PixelCardBox(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            backgroundColor = Color(0xFFFFF7ED), // Cam retro nhạt
            borderColor = PixelColors.BorderDark,
            shadowColor = PixelColors.ShadowDark,
            borderWidth = 2.dp,
            shadowOffset = 4.dp,
            cornerRadius = 8.dp,
            onClick = onStartQuiz
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header: Badge Pixel + Icon sấm sét retro
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PixelBadge(
                        text = "REFLEX ⚡",
                        backgroundColor = PixelColors.PixelOrange,
                        textColor = Color.White,
                        borderColor = PixelColors.BorderDark
                    )

                    // Icon sấm sét vuông
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(PixelColors.PixelOrange)
                            .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bolt),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Thân thẻ: Thử thách phản xạ
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "10 CÂU",
                            fontFamily = PlusJakartaSans,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.BorderDark
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "QUIZ",
                            fontFamily = PlusJakartaSans,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFC2410C),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    Text(
                        text = "Phản xạ 3s • Combo x2",
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.MutedText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Nút hành động Arcade Fighter dập nổi
                PixelArcadeButton(
                    text = "FIGHT ▶",
                    onClick = onStartQuiz,
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = PixelColors.PixelRed,
                    textColor = Color.White,
                    fontSize = 10.5f,
                    contentPadding = 6.dp,
                    shadowOffset = 2.5.dp
                )
            }
        }
    }
}

/**
 * Hub luyện tập & Lộ trình dạng Bento Grid 3 ô giữ trọn vẹn Gradient vũ trụ:
 * - Hàng 1: VocabDailyPracticeDuo (SRS Flashcard & Phản xạ nhanh HSK).
 * - Hàng 2: HskMiniPagerCard (Cấp độ HSK 1->6).
 */
@Composable
fun VocabDailyStudyHub(
    dueTodayCount: Int,
    masteredCount: Int,
    totalCount: Int,
    progressPercent: Int,
    onStartFlashcard: () -> Unit,
    onStartQuiz: () -> Unit,
    onOpenHskLevel: (HskLevelCardData) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        VocabDailyPracticeDuo(
            dueTodayCount = dueTodayCount,
            onStartFlashcard = onStartFlashcard,
            onStartQuiz = onStartQuiz,
            modifier = Modifier.fillMaxWidth()
        )

        HskMiniPagerCard(
            hskLevels = HSK_LEVEL_INFOS,
            onOpenHskLevel = onOpenHskLevel,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

