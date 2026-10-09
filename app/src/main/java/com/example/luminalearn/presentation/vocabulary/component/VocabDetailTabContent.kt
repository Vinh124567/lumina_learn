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
        // ── Card 1: Thẻ Porcelain Luxury Tổng quan chữ Hán & Thanh thao tác tập trung ──
        VocabMainInfoCard(
            word = word,
            onSpeak = { onSpeak(word.hanzi) },
            onSpeakSlow = { onSpeakSlow(word.hanzi) },
            onCopy = { onCopy(word.hanzi) },
            onNavigateToPitchContour = onNavigateToPitchContour
        )

        // ── Card 2: Câu ví dụ thực tế & Ngữ cảnh ──
        VocabExampleCard(
            word = word,
            onSpeakSentence = { onSpeak(word.exampleHanzi) }
        )

        // ── Card 3: Từ ghép thông dụng liên quan ──
        VocabCompoundsCard(
            word = word,
            onSpeakCompound = onSpeak
        )

        // ── Card 4: Chiến thuật phòng thi HSK ──
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
            1 -> Color(0xFF059669)
            2 -> Color(0xFF4F46E5)
            3 -> Color(0xFF2563EB)
            4 -> Color(0xFF7E22CE)
            5 -> Color(0xFFD97706)
            6 -> Color(0xFFDC2626)
            else -> Color(0xFF6366F1)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color(0xFFE2E8F0).copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Vạch Gradient trên mép nhận diện cấp độ HSK
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
                // 1. Header Badges: Cấp độ HSK, Loại từ & Hán Việt
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
                                .background(accentColor.copy(alpha = 0.10f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = word.hskLevel,
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                        }

                        // Badge Từ loại
                        val posLabel = word.partOfSpeech.ifBlank { "Từ vựng" }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF1F5F9))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = posLabel,
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF475569)
                            )
                        }

                        // Badge Chủ đề
                        if (word.topic.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = word.topic,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    // Badge Hán Việt
                    if (word.hanViet.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "HV: ${word.hanViet}",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 2. Centerpiece: Chữ Hán to 44sp mực than sâu + Pinyin nổi bật
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = word.hanzi,
                        fontFamily = PlusJakartaSans,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A),
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
                            color = accentColor
                        )

                        if (word.hanViet.isNotBlank()) {
                            Text(
                                text = " • ",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = Color(0xFFCBD5E1)
                            )
                            Text(
                                text = word.hanViet,
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B)
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
                        color = Color(0xFF334155),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.fillMaxWidth(0.92f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Thanh thao tác âm thanh & tiện ích tập trung (Action Bar)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Nút Phát âm chính
                    Box(
                        modifier = Modifier
                            .weight(1.3f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(accentColor)
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
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEEF2FF))
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
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Chậm",
                                fontFamily = PlusJakartaSans,
                                color = Color(0xFF4F46E5),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Nút Sóng âm & Cao độ
                    Box(
                        modifier = Modifier
                            .weight(1.2f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF0FDF4))
                            .bounceClick(scaleDown = 0.92f, onClick = onNavigateToPitchContour),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_waveform),
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Sóng âm",
                                fontFamily = PlusJakartaSans,
                                color = Color(0xFF059669),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Nút Sao chép
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF1F5F9))
                            .bounceClick(scaleDown = 0.90f, onClick = onCopy),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📋", fontSize = 15.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Ba ô Bento chỉ số cấu tạo tích hợp: Bộ thủ, Số nét bút, Mục tiêu HSK
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricSmallLightCard(
                        label = "Bộ thủ",
                        value = word.radical.replace("Bộ: ", ""),
                        modifier = Modifier.weight(1.2f)
                    )
                    MetricSmallLightCard(
                        label = "Số nét bút",
                        value = word.strokes,
                        modifier = Modifier.weight(1f)
                    )
                    MetricSmallLightCard(
                        label = "Mục tiêu HSK",
                        value = word.hskLevel,
                        valueColor = accentColor,
                        modifier = Modifier.weight(1.1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricSmallLightCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color(0xFF0F172A)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(
                BorderStroke(0.5.dp, Color(0xFFE2E8F0).copy(alpha = 0.7f)),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 9.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = label,
                fontFamily = PlusJakartaSans,
                fontSize = 10.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                color = valueColor,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
        border = BorderStroke(0.5.dp, Color(0xFFE2E8F0).copy(alpha = 0.7f)),
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
                        text = "CÂU VÍ DỤ THỰC TẾ",
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
                        .padding(horizontal = 10.dp, vertical = 5.dp)
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

            Spacer(modifier = Modifier.height(4.dp))

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
        border = BorderStroke(0.5.dp, Color(0xFFE2E8F0).copy(alpha = 0.7f)),
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
                        .border(
                            BorderStroke(0.5.dp, Color(0xFFE2E8F0).copy(alpha = 0.5f)),
                            RoundedCornerShape(12.dp)
                        )
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
                            .border(BorderStroke(0.5.dp, Color(0xFFE2E8F0)), CircleShape)
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)),
        border = BorderStroke(0.5.dp, Color(0xFFFDE68A).copy(alpha = 0.7f)),
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
                    text = "Chiến thuật phòng thi HSK:",
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

