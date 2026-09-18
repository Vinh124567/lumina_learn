package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem

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
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ── Card 1: Tổng quan chữ Hán, Pinyin, Hán Việt, Nghĩa & 3 ô thông số ──
        VocabMainInfoCard(
            word = word,
            onSpeak = { onSpeak(word.hanzi) },
            onSpeakSlow = { onSpeakSlow(word.hanzi) },
            onCopy = { onCopy(word.hanzi) }
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

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun VocabMainInfoCard(
    word: VocabWordItem,
    onSpeak: () -> Unit,
    onSpeakSlow: () -> Unit,
    onCopy: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFEFF6FF), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val (dialogHanziFontSize, dialogBoxWidth) = remember(word.hanzi) {
                    when {
                        word.hanzi.length <= 1 -> Pair(28.sp, 64.dp)
                        word.hanzi.length == 2 -> Pair(22.sp, 64.dp)
                        word.hanzi.length == 3 -> Pair(17.sp, 76.dp)
                        else -> Pair(14.sp, 88.dp)
                    }
                }

                // Box chữ Hán to nền tím squircle
                Box(
                    modifier = Modifier
                        .height(64.dp)
                        .width(dialogBoxWidth)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF5538EE))
                        .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = word.hanzi,
                        color = Color.White,
                        fontSize = dialogHanziFontSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Cột Pinyin, Hán Việt, Loại từ, Nghĩa
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = word.pinyin,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4338CA)
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Hán Việt: ${word.hanViet}",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 5.dp, vertical = 1.5.dp)
                    ) {
                        Text(
                            text = word.partOfSpeech,
                            fontSize = 10.5.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = word.meaning,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3 ô thông số nhỏ: Bộ thủ, Số nét bút, Mục tiêu HSK
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricSmallCard(
                    label = "Bộ thủ",
                    value = word.radical.replace("Bộ: ", ""),
                    modifier = Modifier.weight(1.3f)
                )
                MetricSmallCard(
                    label = "Số nét bút",
                    value = word.strokes,
                    modifier = Modifier.weight(1f)
                )
                MetricSmallCard(
                    label = "Mục tiêu",
                    value = word.hskLevel,
                    valueColor = Color(0xFF5C50F6),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hàng 3 nút: Phát âm, Chậm, Sao chép
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút Phát âm chính
                Surface(
                    modifier = Modifier
                        .weight(1.2f)
                        .height(36.dp)
                        .clickable(onClick = onSpeak),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF5538EE)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Phát âm",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Nút Chậm (0.6x)
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clickable(onClick = onSpeakSlow),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF1F5F9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Chậm",
                            color = Color(0xFF475569),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Nút Sao chép
                Surface(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(onClick = onCopy),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "📋", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricSmallCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color(0xFF0F172A)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 9.5.sp,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 11.5.sp,
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sparkle),
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "GIẢI NGHĨA CHIẾT TỰ & CẤU TẠO CHỮ HÁN",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155),
                    letterSpacing = 0.2.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Chữ Hán \"${word.hanzi}\" (Âm Hán Việt: ${word.hanViet}) gồm ${word.radical} với ${word.strokes} chuẩn. Đây là từ vựng thuộc cấp độ ${word.hskLevel}, xuất hiện rất thường xuyên trong đời sống và đề thi HSK.",
                fontSize = 12.sp,
                color = Color(0xFF475569),
                lineHeight = 17.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Box cấu trúc bộ phận
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cấu trúc: ${word.radical} • ${word.strokes}",
                    fontSize = 11.5.sp,
                    color = Color(0xFF334155),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f, fill = false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(horizontal = 6.dp, vertical = 2.5.dp)
                ) {
                    Text(
                        text = "CHUẨN NÉT",
                        fontSize = 9.5.sp,
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_slides),
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CÂU VÍ DỤ THỰC TẾ & NGỮ CẢNH",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )
                }

                Surface(
                    modifier = Modifier.clickable(onClick = onSpeakSentence),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFEEF2FF)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = null,
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Nghe câu",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4F46E5)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = word.exampleHanzi,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = word.examplePinyin,
                fontSize = 12.sp,
                color = Color(0xFF6366F1),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = word.exampleMeaning,
                fontSize = 12.5.sp,
                color = Color(0xFF64748B)
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
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
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TỪ GHÉP THÔNG DỤNG",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155),
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Nhấn loa để nghe",
                    fontSize = 10.5.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    softWrap = false
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            compoundWords.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = item.hanzi,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.pinyin,
                                fontSize = 12.sp,
                                color = Color(0xFF6366F1)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.meaning,
                            fontSize = 11.5.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .clickable { onSpeakCompound(item.hanzi) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = "Phát âm",
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VocabExamStrategyCard(word: VocabWordItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFBEB))
            .border(1.dp, Color(0xFFFEF3C7), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Chiến thuật phòng thi HSK cho từ \"${word.hanzi}\":",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Trong bài thi ${word.hskLevel} (${word.targetScore}), từ \"${word.hanzi}\" đóng vai trò là ${word.partOfSpeech}. Hãy chú ý nhận diện mặt chữ và ngữ cảnh kết hợp với lượng từ hoặc trợ từ thích hợp.",
                fontSize = 11.5.sp,
                color = Color(0xFF78350F),
                lineHeight = 16.5.sp
            )
        }
    }
}
