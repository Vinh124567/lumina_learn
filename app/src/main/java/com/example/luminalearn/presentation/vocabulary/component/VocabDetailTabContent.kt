package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.ui.theme.PlusJakartaSans

data class CompoundWordItem(
    val hanzi: String,
    val pinyin: String,
    val meaning: String
)

@Composable
fun VocabDetailTabContent(
    word: VocabWordItem,
    onSpeak: (String) -> Unit,
    onSpeakSlow: (String) -> Unit,
    onCopy: (String) -> Unit,
    onNavigateToPitchContour: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ── Card 1: Thẻ Hero Luminous Midnight Bento Tổng quan chữ Hán ──
        VocabMainInfoCard(
            word = word,
            onSpeak = { onSpeak(word.hanzi) },
            onSpeakSlow = { onSpeakSlow(word.hanzi) },
            onCopy = { onCopy(word.hanzi) },
            onNavigateToPitchContour = onNavigateToPitchContour
        )

        // ── Card 2: Giải nghĩa chiết tự & Cấu tạo chữ Hán ──
        VocabDecompositionCard(word = word)

        // ── Card 3: Câu ví dụ thực tế & Ngữ cảnh ──
        VocabExampleCard(
            word = word,
            onSpeakSentence = { onSpeak(word.exampleHanzi) }
        )

        // ── Card 4: Từ ghép thông dụng liên quan ──
        VocabCompoundsCard(
            word = word,
            onSpeakCompound = onSpeak
        )

        // ── Card 5: Chiến thuật phòng thi HSK ──
        VocabExamStrategyCard(word = word)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun VocabMainInfoCard(
    word: VocabWordItem,
    onSpeak: () -> Unit,
    onSpeakSlow: () -> Unit,
    onCopy: () -> Unit,
    onNavigateToPitchContour: () -> Unit = {}
) {
    val levelNumber = remember(word.hskLevel) {
        word.hskLevel.filter { it.isDigit() }.toIntOrNull() ?: 2
    }

    val accentGradient = remember(levelNumber) {
        when (levelNumber) {
            1 -> listOf(Color(0xFF10B981), Color(0xFF06B6D4))
            2 -> listOf(Color(0xFF6366F1), Color(0xFF38BDF8))
            3 -> listOf(Color(0xFF2563EB), Color(0xFF60A5FA))
            4 -> listOf(Color(0xFF9333EA), Color(0xFFC084FC))
            5 -> listOf(Color(0xFFD97706), Color(0xFFFBBF24))
            6 -> listOf(Color(0xFFDC2626), Color(0xFFF87171))
            else -> listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
        }
    }

    val accentColor = remember(levelNumber) {
        when (levelNumber) {
            1 -> Color(0xFF34D399)
            2 -> Color(0xFF818CF8)
            3 -> Color(0xFF60A5FA)
            4 -> Color(0xFFC084FC)
            5 -> Color(0xFFFBBF24)
            6 -> Color(0xFFF87171)
            else -> Color(0xFFA5B4FC)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.2.dp, Color(0xFF818CF8).copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF161338),
                            Color(0xFF221C52),
                            Color(0xFF1A1442)
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // ── Vạch Gradient trên mép nhận diện cấp độ HSK ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.5.dp)
                        .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                        .background(Brush.horizontalGradient(accentGradient))
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // 1. Header Badges: Cấp độ HSK, Chủ đề & Hán Việt
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Badge HSK
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White.copy(alpha = 0.10f))
                                    .border(
                                        BorderStroke(0.8.dp, accentColor.copy(alpha = 0.45f)),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
                            ) {
                                Text(
                                    text = word.hskLevel,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                )
                            }

                            // Badge Từ loại
                            val posLabel = word.partOfSpeech.ifBlank { "Từ vựng" }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White.copy(alpha = 0.08f))
                                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
                            ) {
                                Text(
                                    text = posLabel,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFC7D2FE)
                                )
                            }
                        }

                        // Badge Hán Việt
                        if (word.hanViet.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF59E0B).copy(alpha = 0.20f))
                                    .border(
                                        BorderStroke(0.8.dp, Color(0xFFFDE68A).copy(alpha = 0.40f)),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.5.dp)
                            ) {
                                Text(
                                    text = "HV: ${word.hanViet}",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFDE68A)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Centerpiece: Chữ Hán to 44sp trắng tuyết + Pinyin phát sáng
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = word.hanzi,
                            fontFamily = PlusJakartaSans,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = word.pinyin,
                                fontFamily = PlusJakartaSans,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )

                            if (word.hanViet.isNotBlank()) {
                                Text(
                                    text = " • ",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    color = Color(0xFF818CF8)
                                )
                                Text(
                                    text = word.hanViet,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFFDE68A)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Nghĩa tiếng Việt
                        Text(
                            text = word.meaning,
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE2E8F0),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.fillMaxWidth(0.92f)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 3. Ba ô Bento chỉ số nhỏ (Bộ thủ, Nét bút, Mục tiêu)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricSmallDarkCard(
                            label = "Bộ thủ",
                            value = word.radical.replace("Bộ: ", ""),
                            modifier = Modifier.weight(1.2f)
                        )
                        MetricSmallDarkCard(
                            label = "Số nét bút",
                            value = word.strokes,
                            modifier = Modifier.weight(1f)
                        )
                        MetricSmallDarkCard(
                            label = "Mục tiêu",
                            value = word.hskLevel,
                            valueColor = accentColor,
                            modifier = Modifier.weight(1.1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4. Hàng nút thao tác nhanh (Phát âm, Chậm 0.6x, Sao chép)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Nút Phát âm chính
                        Box(
                            modifier = Modifier
                                .weight(1.2f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(VocabColors.BrandPrimary)
                                .bounceClick(scaleDown = 0.92f, onClick = onSpeak),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_speaker),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Phát âm",
                                    fontFamily = PlusJakartaSans,
                                    color = Color.White,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Nút Phát âm Chậm (0.6x)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.10f))
                                .border(
                                    BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
                                    RoundedCornerShape(12.dp)
                                )
                                .bounceClick(scaleDown = 0.92f, onClick = onSpeakSlow),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_clock),
                                    contentDescription = null,
                                    tint = Color(0xFFC7D2FE),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Chậm",
                                    fontFamily = PlusJakartaSans,
                                    color = Color(0xFFC7D2FE),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Nút Sao chép
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(
                                    BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                                    RoundedCornerShape(12.dp)
                                )
                                .bounceClick(scaleDown = 0.90f, onClick = onCopy),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📋", fontSize = 15.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 5. Nút tắt đến tab Biểu đồ cao độ
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF38BDF8).copy(alpha = 0.15f))
                            .border(
                                BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.35f)),
                                RoundedCornerShape(12.dp)
                            )
                            .bounceClick(scaleDown = 0.94f, onClick = onNavigateToPitchContour),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_waveform),
                                contentDescription = null,
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = "Biểu đồ cao độ & Sóng âm",
                                fontFamily = PlusJakartaSans,
                                color = Color(0xFF38BDF8),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricSmallDarkCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .border(
                BorderStroke(0.8.dp, Color.White.copy(alpha = 0.12f)),
                RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 8.dp, vertical = 7.dp)
    ) {
        Column {
            Text(
                text = label,
                fontFamily = PlusJakartaSans,
                fontSize = 10.sp,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                color = valueColor,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun VocabDecompositionCard(word: VocabWordItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sparkle),
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = "GIẢI NGHĨA CHIẾT TỰ & CẤU TẠO HÁN TỰ",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B),
                    letterSpacing = 0.2.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Chữ Hán \"${word.hanzi}\" (Âm Hán Việt: ${word.hanViet}) gồm ${word.radical} với ${word.strokes} chuẩn. Đây là từ vựng thuộc cấp độ ${word.hskLevel}, xuất hiện rất thường xuyên trong đời sống và đề thi HSK.",
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                color = Color(0xFF475569),
                lineHeight = 18.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Box cấu trúc bộ phận
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cấu trúc: ${word.radical} • ${word.strokes}",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = Color(0xFF334155),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f, fill = false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "CHUẨN NÉT",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2563EB),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Composable
private fun VocabExampleCard(
    word: VocabWordItem,
    onSpeakSentence: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_slides),
                        contentDescription = null,
                        tint = VocabColors.BrandPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "CÂU VÍ DỤ THỰC TẾ & NGỮ CẢNH",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEEF2FF))
                        .bounceClick(scaleDown = 0.90f, onClick = onSpeakSentence)
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = null,
                            tint = VocabColors.BrandPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Nghe câu",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = VocabColors.BrandPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = word.exampleHanzi,
                fontFamily = PlusJakartaSans,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = word.examplePinyin,
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                color = VocabColors.BrandPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = word.exampleMeaning,
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun VocabCompoundsCard(
    word: VocabWordItem,
    onSpeakCompound: (String) -> Unit
) {
    val compoundWords = remember(word) {
        when (word.hanzi) {
            "便宜" -> listOf(
                CompoundWordItem("便宜", "piányi", "Rẻ, giá cả phải chăng"),
                CompoundWordItem("便宜人", "piányi rén", "Người liên quan đến Rẻ, giá cả phải chăng")
            )
            "你好" -> listOf(
                CompoundWordItem("你好", "nǐ hǎo", "Xin chào, chào bạn"),
                CompoundWordItem("您好", "nín hǎo", "Kính chào ngài/bạn (lịch sự)")
            )
            "谢谢" -> listOf(
                CompoundWordItem("谢谢", "xièxie", "Cảm ơn"),
                CompoundWordItem("不客气", "bú kèqi", "Đừng khách sáo, không có chi")
            )
            else -> listOf(
                CompoundWordItem(word.hanzi, word.pinyin, word.meaning)
            )
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_layers),
                        contentDescription = null,
                        tint = Color(0xFF7E22CE),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "TỪ GHÉP THÔNG DỤNG",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B),
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Nhấn loa để nghe",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    softWrap = false
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            compoundWords.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(BorderStroke(1.dp, Color(0xFFF1F5F9)), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = item.hanzi,
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = item.pinyin,
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.5.sp,
                                color = VocabColors.BrandPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.meaning,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), CircleShape)
                            .bounceClick(scaleDown = 0.88f) { onSpeakCompound(item.hanzi) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = "Phát âm",
                            tint = VocabColors.BrandPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VocabExamStrategyCard(word: VocabWordItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = "Chiến thuật phòng thi HSK cho từ \"${word.hanzi}\":",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Trong bài thi ${word.hskLevel} (${word.targetScore}), từ \"${word.hanzi}\" đóng vai trò là ${word.partOfSpeech}. Hãy chú ý nhận diện mặt chữ và ngữ cảnh kết hợp với lượng từ hoặc trợ từ thích hợp.",
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                color = Color(0xFF78350F),
                lineHeight = 17.5.sp
            )
        }
    }
}

