package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.EnergyPulseAura
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.core.ui.effect.breathingGlow
import com.example.luminalearn.presentation.vocabulary.model.TopicItem
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val PrimaryIndigo = Color(0xFF5C50F6)
private val BrandIndigoLight = Color(0xFF6366F1)
private val SkyAccentColor = Color(0xFF38BDF8)
private val GoldBadgeText = Color(0xFFFEF08A)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)

/**
 * Component Bento Section chủ đề từ vựng:
 * - Đồng bộ 100% với Design System của LuminaLearn:
 *   + Nền Banner Midnight Cosmos chuyển động (animatedMidnightGradient) với hạt bụi sao (CosmicStarfield).
 *   + Huy hiệu ánh vàng GoldBadgeText & Hào quang năng lượng EnergyPulseAura.
 *   + Sheet trắng bo góc 24dp viền hairline siêu mảnh 0.5dp.
 *   + Tabs Capsule Indigo chuyển động đàn hồi haptic bounceClick.
 *   + Thẻ từ vựng 2 cột với chữ Hán phát quang & nút capsule "Học ngay" tinh xảo.
 */
@Composable
fun ThematicVocabBentoSection(
    topics: List<TopicItem>,
    selectedTopic: String,
    vocabList: List<VocabWordItem>,
    onSelectTopic: (String) -> Unit,
    onWordClick: (VocabWordItem) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Chỉ lấy 2 từ tiêu biểu nhất (1 hàng 2 cột cân đối thanh thoát như ảnh mẫu)
    val displayedWords = vocabList
        .filter { word ->
            selectedTopic.isBlank() ||
                selectedTopic == "Tất cả" ||
                selectedTopic == "Tất cả chủ đề" ||
                selectedTopic.equals("all", ignoreCase = true) ||
                word.topic.contains(selectedTopic, ignoreCase = true) ||
                word.matchesCategory(selectedTopic)
        }
        .take(2)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, PrimaryIndigo.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ── 1. BANNER MIDNIGHT AURORA THU GỌN TINH TẾ (76dp) ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp)
                    .animatedMidnightGradient(
                        colors = listOf(
                            Color(0xFF0F172A),
                            Color(0xFF1E1C59),
                            Color(0xFF2E2C80),
                            Color(0xFF3B2874),
                            Color(0xFF0F172A)
                        ),
                        durationMillis = 8000
                    )
            ) {
                // Hiệu ứng hạt bụi sao vũ trụ CosmicStarfield ở góc phải
                CosmicStarfield(
                    modifier = Modifier.matchParentSize(),
                    particleCount = 20,
                    focusCenterXRatio = 0.90f,
                    focusCenterYRatio = 0.40f
                )

                // Nội dung text bên trái
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth(0.72f)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    // Badge thương hiệu ánh vàng siêu mảnh
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(BorderStroke(0.5.dp, GoldBadgeText.copy(alpha = 0.40f)), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "✨ CHỦ ĐỀ THỰC TẾ",
                            color = GoldBadgeText,
                            fontFamily = PlusJakartaSans,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = if (selectedTopic == "Tất cả" || selectedTopic == "all") "Ngữ cảnh ứng dụng" else selectedTopic,
                        fontFamily = PlusJakartaSans,
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = "Phản xạ giao tiếp tự nhiên",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF94A3B8)
                    )
                }

                // Cụm biểu tượng năng lượng EnergyPulseAura nhỏ gọn bên phải
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 14.dp)
                        .size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    EnergyPulseAura(
                        modifier = Modifier.matchParentSize()
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .border(BorderStroke(0.5.dp, SkyAccentColor.copy(alpha = 0.5f)), CircleShape)
                            .breathingGlow(minScale = 0.94f, maxScale = 1.06f, durationMillis = 2200),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_nav_explore),
                            contentDescription = null,
                            tint = SkyAccentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // ── 2. PHẦN SHEET TRẮNG BENTO GỌN GÀNG ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 10.dp, bottom = 6.dp)
            ) {
                // ── Hàng Tab Capsule (Pill Chips) cuộn ngang ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    topics.forEach { topic ->
                        val isSelected = topic.name == selectedTopic ||
                            (selectedTopic.isBlank() && topic.id == "all")

                        ThematicTopicChip(
                            title = topic.name,
                            isSelected = isSelected,
                            onClick = { onSelectTopic(topic.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ── Lưới thẻ từ vựng 1 hàng 2 cột gọn gàng ──
                AnimatedContent(
                    targetState = displayedWords,
                    transitionSpec = {
                        fadeIn(tween(200)) togetherWith fadeOut(tween(160))
                    },
                    label = "thematic_words_grid",
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) { words ->
                    if (words.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8FAFC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Đang cập nhật từ vựng cho chủ đề này...",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            words.forEach { word ->
                                ThematicWordCard(
                                    word = word,
                                    onClick = { onWordClick(word) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (words.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ── FOOTER: XEM TẤT CẢ > GỌN GÀNG ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick()
                        .clickable(onClick = onSeeAllClick)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Xem tất cả",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryIndigo
                        )

                        Spacer(modifier = Modifier.width(3.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_right),
                            contentDescription = null,
                            tint = PrimaryIndigo,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Chip tab Capsule theo chuẩn Design System Lumina (nhỏ gọn).
 */
@Composable
private fun ThematicTopicChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundBrush = if (isSelected) {
        Brush.horizontalGradient(listOf(BrandIndigoLight, PrimaryIndigo))
    } else {
        Brush.horizontalGradient(listOf(Color(0xFFF1F5F9), Color(0xFFF1F5F9)))
    }

    val contentColor = if (isSelected) Color.White else Color(0xFF334155)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundBrush)
            .border(
                BorderStroke(
                    0.5.dp,
                    if (isSelected) PrimaryIndigo.copy(alpha = 0.5f) else Color.Transparent
                ),
                RoundedCornerShape(16.dp)
            )
            .bounceClick()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 5.5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontFamily = PlusJakartaSans,
            fontSize = 11.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
            color = contentColor
        )
    }
}

/**
 * Thẻ từ vựng con (Word Card) nhỏ gọn, thanh thoát:
 * - Khung trên: height 64dp, chữ Hán 18sp.
 * - Dưới: Nghĩa từ vựng 12sp & Nút pill 27dp "Học ngay".
 */
@Composable
private fun ThematicWordCard(
    word: VocabWordItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .bounceClick()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Khung hình chữ Hán phía trên (bo góc 10dp, height 64dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E1C59),
                                Color(0xFF1E1B4B)
                            )
                        )
                    )
                    .border(BorderStroke(0.5.dp, PrimaryIndigo.copy(alpha = 0.35f)), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Badge HSK nhỏ góc trên bên trái
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(BorderStroke(0.5.dp, GoldBadgeText.copy(alpha = 0.40f)), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = word.hskLevel.ifBlank { "HSK" },
                        fontFamily = PlusJakartaSans,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldBadgeText
                    )
                }

                // Chữ Hán to bản & Pinyin
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = word.hanzi,
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        text = word.pinyin,
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SkyAccentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(7.dp))

            // Tiêu đề & Nghĩa từ vựng
            Text(
                text = "${word.hanzi} ➔ ${word.meaning}",
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(1.dp))

            // Metadata phụ (Loại từ hoặc chủ đề)
            Text(
                text = if (word.partOfSpeech.isNotBlank()) word.partOfSpeech else "Từ vựng ứng dụng",
                fontFamily = PlusJakartaSans,
                fontSize = 10.5.sp,
                color = TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(7.dp))

            // Nút pill viền hairline Indigo "Học ngay" (Chiều cao 27dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(27.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFEEF2FF))
                    .border(BorderStroke(0.6.dp, PrimaryIndigo.copy(alpha = 0.6f)), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Học ngay",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryIndigo
                )
            }
        }
    }
}

