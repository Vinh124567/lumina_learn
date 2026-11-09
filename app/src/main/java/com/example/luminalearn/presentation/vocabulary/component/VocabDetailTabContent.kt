package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
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

    val levelCartridgeColor = remember(levelNumber) {
        when (levelNumber) {
            1 -> PixelColors.CardGreen
            2 -> PixelColors.CardBlue
            3 -> Color(0xFFBAE6FD)
            4 -> PixelColors.CardPurple
            5 -> PixelColors.CardOrange
            6 -> Color(0xFFFECACA)
            else -> PixelColors.CardBlue
        }
    }

    val posLabel = word.partOfSpeech.ifBlank { "TỪ VỰNG" }.uppercase()

    PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 2.dp,
        shadowOffset = 3.5.dp,
        cornerRadius = 6.dp,
        headerTitle = posLabel,
        headerBadge = word.hskLevel,
        headerColor = levelCartridgeColor,
        showGrille = true,
        showRivets = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 1. Chữ Hán to bản trung tâm + Pinyin + Hán Việt
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = word.hanzi,
                    fontFamily = PlusJakartaSans,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = word.pinyin,
                        fontFamily = PlusJakartaSans,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark
                    )

                    if (word.hanViet.isNotBlank()) {
                        Text(
                            text = " • ",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = PixelColors.MutedText
                        )
                        Text(
                            text = word.hanViet,
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.MutedText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Nghĩa tiếng Việt đóng khung màn hình console
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CutCornerShape(4.dp))
                        .background(Color(0xFFF3F4F6))
                        .border(BorderStroke(1.2.dp, PixelColors.BorderDark), CutCornerShape(4.dp))
                        .padding(horizontal = 14.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = word.meaning,
                        fontFamily = PlusJakartaSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.BorderDark,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Cụm 4 phím bấm cơ học Arcade (Phát âm, Chậm, Sóng âm, Copy)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút Phát âm chính màu Vàng Pixel
                PixelCardBox(
                    modifier = Modifier
                        .weight(1.3f)
                        .height(38.dp),
                    backgroundColor = PixelColors.PixelYellow,
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.5.dp,
                    shadowOffset = 2.dp,
                    cornerRadius = 4.dp,
                    showRivets = false,
                    onClick = onSpeak
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = null,
                            tint = PixelColors.BorderDark,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Phát âm",
                            fontFamily = PlusJakartaSans,
                            color = PixelColors.BorderDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                // Nút Phát âm Chậm
                PixelCardBox(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    backgroundColor = Color.White,
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.5.dp,
                    shadowOffset = 2.dp,
                    cornerRadius = 4.dp,
                    showRivets = false,
                    onClick = onSpeakSlow
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🐢", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Chậm",
                            fontFamily = PlusJakartaSans,
                            color = PixelColors.BorderDark,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Nút Sóng âm
                PixelCardBox(
                    modifier = Modifier
                        .weight(1.2f)
                        .height(38.dp),
                    backgroundColor = PixelColors.CardBlue,
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.5.dp,
                    shadowOffset = 2.dp,
                    cornerRadius = 4.dp,
                    showRivets = false,
                    onClick = onNavigateToPitchContour
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_waveform),
                            contentDescription = null,
                            tint = PixelColors.BorderDark,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sóng âm",
                            fontFamily = PlusJakartaSans,
                            color = PixelColors.BorderDark,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Nút Sao chép
                PixelCardBox(
                    modifier = Modifier.size(38.dp),
                    backgroundColor = Color.White,
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.5.dp,
                    shadowOffset = 2.dp,
                    cornerRadius = 4.dp,
                    showRivets = false,
                    onClick = onCopy
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "📋", fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Ba ô chỉ số cấu tạo Pixel Boxes: Bộ thủ, Số nét bút, Mục tiêu HSK
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricSmallLightCard(
                    label = "BỘ THỦ",
                    value = word.radical.replace("Bộ: ", "").ifBlank { "—" },
                    modifier = Modifier.weight(1f)
                )
                MetricSmallLightCard(
                    label = "NÉT BÚT",
                    value = word.strokes.ifBlank { "—" },
                    modifier = Modifier.weight(1f)
                )
                MetricSmallLightCard(
                    label = "CẤP ĐỘ",
                    value = word.hskLevel,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MetricSmallLightCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = PixelColors.BorderDark
) {
    Box(
        modifier = modifier
            .clip(CutCornerShape(4.dp))
            .background(Color(0xFFF9FAFB))
            .border(BorderStroke(1.2.dp, PixelColors.BorderDark), CutCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 7.dp)
    ) {
        Column {
            Text(
                text = label,
                fontFamily = PlusJakartaSans,
                fontSize = 9.sp,
                color = PixelColors.MutedText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                color = valueColor,
                fontWeight = FontWeight.Black,
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
    PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 2.dp,
        shadowOffset = 3.dp,
        cornerRadius = 6.dp,
        headerTitle = "CÂU VÍ DỤ THỰC TẾ",
        headerColor = PixelColors.CardBlue,
        showGrille = true,
        showRivets = false
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "NGỮ CẢNH HSK",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.MutedText
                )

                // Nút Nghe câu Arcade Pixel
                PixelCardBox(
                    modifier = Modifier.height(28.dp),
                    backgroundColor = PixelColors.PixelYellow,
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.2.dp,
                    shadowOffset = 1.5.dp,
                    cornerRadius = 3.dp,
                    showRivets = false,
                    onClick = onSpeakSentence
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = null,
                            tint = PixelColors.BorderDark,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Nghe câu",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.BorderDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Khung hiển thị câu ví dụ kiểu màn hình console
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CutCornerShape(4.dp))
                    .background(Color(0xFFF9FAFB))
                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), CutCornerShape(4.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = word.exampleHanzi,
                        fontFamily = PlusJakartaSans,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.BorderDark,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = word.examplePinyin,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        color = PixelColors.BorderDark,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = word.exampleMeaning,
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        color = PixelColors.MutedText,
                        lineHeight = 18.sp
                    )
                }
            }
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

    PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 2.dp,
        shadowOffset = 3.dp,
        cornerRadius = 6.dp,
        headerTitle = "TỪ GHÉP THÔNG DỤNG",
        headerColor = PixelColors.CardPurple,
        showGrille = true,
        showRivets = false
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            compoundWords.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(CutCornerShape(4.dp))
                        .background(Color(0xFFF9FAFB))
                        .border(BorderStroke(1.2.dp, PixelColors.BorderDark), CutCornerShape(4.dp))
                        .padding(horizontal = 11.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = item.hanzi,
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = item.pinyin,
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = PixelColors.BorderDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.meaning,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = PixelColors.MutedText
                        )
                    }

                    // Nút Loa Pixel vuông vắn
                    PixelCardBox(
                        modifier = Modifier.size(28.dp),
                        backgroundColor = PixelColors.PixelYellow,
                        borderColor = PixelColors.BorderDark,
                        shadowColor = PixelColors.ShadowDark,
                        borderWidth = 1.2.dp,
                        shadowOffset = 1.5.dp,
                        cornerRadius = 3.dp,
                        showRivets = false,
                        onClick = { onSpeakCompound(item.hanzi) }
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_speaker),
                                contentDescription = "Phát âm",
                                tint = PixelColors.BorderDark,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VocabExamStrategyCard(word: VocabWordItem) {
    PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = PixelColors.RetroCream,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 2.dp,
        shadowOffset = 3.dp,
        cornerRadius = 6.dp,
        headerTitle = "CHIẾN THUẬT PHÒNG THI HSK",
        headerBadge = "PRO TIP",
        headerColor = PixelColors.PixelYellow,
        showGrille = true,
        showRivets = false
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Trong bài thi ${word.hskLevel} (${word.targetScore}), từ \"${word.hanzi}\" đóng vai trò là ${word.partOfSpeech}. Hãy chú ý nhận diện mặt chữ và ngữ cảnh kết hợp với lượng từ hoặc trợ từ thích hợp.",
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = PixelColors.BorderDark,
                lineHeight = 18.sp
            )
        }
    }
}

