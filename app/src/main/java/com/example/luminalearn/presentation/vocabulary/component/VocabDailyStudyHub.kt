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
import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient
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
 * Cụm 2 ô luyện tập hàng ngày (SRS Flashcard & Phản xạ HSK) giữ trọn vẹn Gradient vũ trụ:
 * - Ô 1 (Trái): Flashcard SRS Ebbinghaus (Midnight Cosmos Indigo huyền ảo).
 * - Ô 2 (Phải): Phản xạ nhanh HSK (Midnight Amber Cam-Tím ấm áp & sinh động).
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
            .height(140.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Ô 1 (Trái): SRS Flashcard (Midnight Cosmos Indigo)
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .bounceClick(scaleDown = 0.97f, onClick = onStartFlashcard),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(0.5.dp, PrimaryIndigo.copy(alpha = 0.35f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .animatedMidnightGradient(
                        colors = listOf(
                            Color(0xFF0F172A),
                            Color(0xFF1E1C59),
                            Color(0xFF2E2C80),
                            Color(0xFF0F172A)
                        ),
                        durationMillis = 8000
                    )
            ) {
                CosmicStarfield(
                    modifier = Modifier.matchParentSize(),
                    particleCount = 16,
                    focusCenterXRatio = 0.85f,
                    focusCenterYRatio = 0.35f
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header tag
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White.copy(alpha = 0.12f))
                                .border(
                                    BorderStroke(0.5.dp, GoldBadgeText.copy(alpha = 0.40f)),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "✨ SRS ÔN TẬP",
                                fontFamily = PlusJakartaSans,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldBadgeText
                            )
                        }
                    }

                    // Thân thẻ: Số từ cần ôn
                    Column {
                        if (dueTodayCount > 0) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$dueTodayCount",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "từ đến hạn",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MintTextColor,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "Đã xong hôm nay! 👏",
                                fontFamily = PlusJakartaSans,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintTextColor
                            )
                        }
                        Text(
                            text = if (dueTodayCount > 0) "Ôn ngắt quãng Ebbinghaus" else "Sẵn sàng để ôn",
                            fontFamily = PlusJakartaSans,
                            fontSize = 9.5.sp,
                            color = Color(0xFF94A3B8),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Nút hành động pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(PrimaryIndigo, Color(0xFF4F46E5))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (dueTodayCount > 0) "Luyện ngay" else "Luyện tự do",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "→",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Ô 2 (Phải): Phản xạ nhanh HSK (Midnight Amber Cam-Tím ấm áp)
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .bounceClick(scaleDown = 0.97f, onClick = onStartQuiz),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(0.5.dp, AmberAccent.copy(alpha = 0.40f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .animatedMidnightGradient(
                        colors = listOf(
                            Color(0xFF181028),
                            Color(0xFF2E1065),
                            Color(0xFF3B1745),
                            Color(0xFF4A1D2F),
                            Color(0xFF181028)
                        ),
                        durationMillis = 6500
                    )
            ) {
                CosmicStarfield(
                    modifier = Modifier.matchParentSize(),
                    particleCount = 16,
                    focusCenterXRatio = 0.82f,
                    focusCenterYRatio = 0.38f
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header tag
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White.copy(alpha = 0.12f))
                                .border(
                                    BorderStroke(0.5.dp, AmberAccent.copy(alpha = 0.50f)),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "⚡ PHẢN XẠ HSK",
                                fontFamily = PlusJakartaSans,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberAccent
                            )
                        }
                    }

                    // Thân thẻ: 10 câu trắc nghiệm
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "10 câu",
                                fontFamily = PlusJakartaSans,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "nhanh",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = AmberAccent,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                        Text(
                            text = "Thử thách nhận diện",
                            fontFamily = PlusJakartaSans,
                            fontSize = 9.5.sp,
                            color = Color(0xFFCBD5E1),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Nút hành động pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(AmberAccent, Color(0xFFEA580C))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Vào thi",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "→",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
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

