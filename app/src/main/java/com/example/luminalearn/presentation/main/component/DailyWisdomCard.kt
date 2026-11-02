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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.data.model.DailyWisdomDto
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Thẻ Danh Ngôn / Châm Ngôn Hán Ngữ chuẩn Pixel Retro 16-Bit / Ancient Scroll:
 * - Khung giấy da cổ dập nổi với con dấu triện đỏ thư pháp Trung Hoa.
 * - Trình bày đầy đặn, trang trọng với chữ Hán lớn, Pinyin ngọc lục bảo, nghĩa tiếng Việt và xuất xứ tác phẩm.
 * - Tích hợp nút Re-roll arcade cùng huy hiệu thể loại.
 */
@Composable
fun DailyWisdomCard(
    modifier: Modifier = Modifier,
    wisdom: DailyWisdomDto? = null,
    quote: String = stringResource(R.string.daily_wisdom_quote),
    author: String = stringResource(R.string.daily_wisdom_author),
    onRefreshClick: () -> Unit = {}
) {
    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = PixelColors.CardGreen,
        headerTitle = "📜 ANCIENT SCROLL",
        headerBadge = "WISDOM",
        headerColor = PixelColors.PixelGreen
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Khung Cuộn Giấy Da Cổ (Scroll Paper Deck)
            PixelCardBox(
                backgroundColor = PixelColors.RetroCream,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.2.dp,
                shadowOffset = 2.dp,
                cornerRadius = 6.dp,
                showRivets = false
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    // 1. Dải Tiêu Đề Cuộn Cổ + Nút Re-Roll
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "🏮 DANH NGÔN KINH ĐIỂN",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF065F46),
                                letterSpacing = 0.5.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFD1FAE5))
                                    .border(BorderStroke(0.8.dp, Color(0xFF059669)), RoundedCornerShape(3.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "HSK 4+",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                            }
                        }

                        PixelArcadeButton(
                            text = "↻ ĐỔI CÂU",
                            color = PixelColors.PixelYellow,
                            onClick = onRefreshClick
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Nội dung Châm Ngôn
                    val chineseText = if (wisdom != null && wisdom.chinese.isNotBlank()) wisdom.chinese else "机不可失，时不再来"
                    val pinyinText = if (wisdom != null && wisdom.pinyin.isNotBlank()) wisdom.pinyin else "Jī bù kě shī, shí bù zài lái"
                    val meaningText = if (wisdom != null) {
                        wisdom.vietnamese.ifBlank { wisdom.meaning }
                    } else quote
                    val authorName = if (wisdom != null) {
                        wisdom.author.ifBlank { author }
                    } else author

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Chữ Hán lớn
                        Text(
                            text = chineseText,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.5.sp,
                            lineHeight = 25.sp,
                            color = PixelColors.BorderDark,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Dấu triện đỏ thư pháp Trung Hoa [古训]
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFFDC2626))
                                .border(BorderStroke(1.dp, Color(0xFF991B1B)), RoundedCornerShape(3.dp))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "古训",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Phiên âm Pinyin
                    if (pinyinText.isNotBlank()) {
                        Text(
                            text = pinyinText,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = Color(0xFF047857)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Dịch nghĩa tiếng Việt trong dấu ngoặc kép trang trọng
                    if (meaningText.isNotBlank()) {
                        Text(
                            text = "“$meaningText”",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp,
                            color = Color(0xFF374151)
                        )
                    }

                    // Tác giả & Xuất xứ
                    if (authorName.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatAuthor(authorName),
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                color = Color(0xFF1E293B)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatAuthor(author: String): String =
    if (author.startsWith("—")) author else "— $author"
