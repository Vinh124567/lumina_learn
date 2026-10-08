package com.example.luminalearn.presentation.vocabulary.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.ui.theme.PlusJakartaSans

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.lerp

private val PrimaryIndigo = Color(0xFF5C50F6)
private val BrandIndigoLight = Color(0xFF6366F1)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)

/**
 * Thanh tiêu đề thanh lịch của Kho Từ Vựng:
 * - Bên trái: Tiêu đề "Kho Từ Vựng" & phụ đề mô tả.
 * - Bên phải: Nút Tra cứu nhanh (kính lúp) & Nút Thêm từ mới.
 */
@Composable
fun VocabHeaderBar(
    onSearchClick: () -> Unit,
    onAddVocabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Kho Từ Vựng",
                fontFamily = PlusJakartaSans,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Luyện tập SRS & Lộ trình HSK chuẩn",
                fontFamily = PlusJakartaSans,
                fontSize = 11.5.sp,
                color = TextMuted
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Nút Kính lúp tra cứu (mở kho từ kèm thanh tìm kiếm)
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(BorderStroke(0.5.dp, BorderSubtle), RoundedCornerShape(12.dp))
                    .bounceClick(scaleDown = 0.90f, onClick = onSearchClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Vocab",
                    tint = BrandIndigoLight,
                    modifier = Modifier.size(17.dp)
                )
            }

            // Nút [+ Thêm từ]
            Box(
                modifier = Modifier
                    .height(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(PrimaryIndigo, Color(0xFF4F46E5))
                        )
                    )
                    .border(
                        BorderStroke(0.5.dp, Color(0xFF818CF8).copy(alpha = 0.45f)),
                        RoundedCornerShape(12.dp)
                    )
                    .bounceClick(scaleDown = 0.92f, onClick = onAddVocabClick)
                    .padding(horizontal = 11.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Thêm từ",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Collapsing Header chuyên dụng cho Màn Tổng quan Từ Vựng (Chuẩn Apple Books / Spotify Library):
 * - Khi ở đỉnh (progress = 0): Chiều cao 110dp, Tiêu đề to 22sp "Kho Từ Vựng HSK", Subtitle đầy đủ, nút Thêm từ viên nhộng.
 * - Khi cuộn (progress -> 1): Co mượt về 58dp, tiêu đề to scale trượt lên TopBar, nút Thêm từ co gọn thành nút tròn (+).
 * - Khi cuộn sâu: Dừng đóng và ghim cố định ở độ cao 58dp (Sticky Pinned), nền kính mờ trắng đục liền mạch.
 */
@Composable
fun CollapsingVocabHeaderBar(
    collapseProgress: Float,
    onSearchClick: () -> Unit,
    onAddVocabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = collapseProgress.coerceIn(0f, 1f)
    val headerHeight = lerp(110.dp, 58.dp, progress)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(Color.White.copy(alpha = progress * 0.98f))
    ) {
        // ── 1. KHỐI EXPANDED TITLE (Tiêu đề to 22sp + Subtitle) ──
        if (progress < 0.95f) {
            val titleAlpha = (1f - progress * 1.5f).coerceIn(0f, 1f)
            val titleScale = 1f - progress * 0.15f

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomStart)
                    .padding(bottom = 10.dp)
                    .graphicsLayer {
                        alpha = titleAlpha
                        scaleX = titleScale
                        scaleY = titleScale
                        translationY = -progress * 25f
                    }
            ) {
                Text(
                    text = "Kho Từ Vựng HSK",
                    fontFamily = PlusJakartaSans,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextMain,
                    letterSpacing = (-0.4).sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Hệ thống hóa 5,000+ từ chuẩn đề thi HSK 1–6",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    color = TextMuted
                )
            }
        }

        // ── 2. THANH STICKY ACTION BAR TRÊN ĐỈNH (Cao 58dp) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Bên trái khi Collapsed: Tiêu đề mini (17sp) mờ hiện dần khi progress > 0.35
            val collapsedTitleAlpha = ((progress - 0.35f) / 0.65f).coerceIn(0f, 1f)

            Text(
                text = "Kho Từ Vựng HSK",
                fontFamily = PlusJakartaSans,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                letterSpacing = (-0.3).sp,
                modifier = Modifier.graphicsLayer {
                    alpha = collapsedTitleAlpha
                }
            )

            // Bên phải: Cụm nút (Kính lúp + Thêm từ)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Nút Kính lúp tra cứu (Bento 38dp)
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(BorderStroke(0.5.dp, BorderSubtle), RoundedCornerShape(12.dp))
                        .bounceClick(scaleDown = 0.90f, onClick = onSearchClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search Vocab",
                        tint = BrandIndigoLight,
                        modifier = Modifier.size(17.dp)
                    )
                }

                // Nút Thêm từ: Khi Expanded thì là pill "Thêm từ", khi Collapsed thì co gọn thành icon (+)
                val isPill = progress < 0.5f

                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(PrimaryIndigo, Color(0xFF4F46E5))
                            )
                        )
                        .border(
                            BorderStroke(0.5.dp, Color(0xFF818CF8).copy(alpha = 0.45f)),
                            RoundedCornerShape(12.dp)
                        )
                        .bounceClick(scaleDown = 0.92f, onClick = onAddVocabClick)
                        .padding(horizontal = if (isPill) 11.dp else 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        if (isPill) {
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Thêm từ",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.5.sp,
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
