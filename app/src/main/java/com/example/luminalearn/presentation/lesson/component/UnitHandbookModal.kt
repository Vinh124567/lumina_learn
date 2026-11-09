package com.example.luminalearn.presentation.lesson.component

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton

private val BrandIndigo = Color(0xFF5C50F6)
private val BrandIndigoLight = Color(0xFF6366F1)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)
private val CardBg = Color(0xFFF8FAFC)

private enum class HandbookTab(val title: String) {
    GRAMMAR("NGỮ PHÁP"),
    VOCABULARY("TỪ VỰNG"),
    DIALOGUE("HỘI THOẠI")
}

/**
 * Sổ tay kiến thức bỏ túi theo từng Unit (Unit Grammar Handbook).
 * Hiển thị tóm tắt lý thuyết ngữ pháp, từ mới và đàm thoại cốt lõi phong cách Pixel 16-Bit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitHandbookModal(
    unitNumber: Int,
    unitTitle: String,
    lessons: List<ChineseLessonData>,
    onSpeak: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableStateOf(HandbookTab.GRAMMAR) }

    val allGrammars = remember(lessons) {
        lessons.flatMap { it.grammarStructures }
    }
    val allVocabs = remember(lessons) {
        lessons.flatMap { it.coreVocabularies }
    }
    val allDialogues = remember(lessons) {
        lessons.flatMap { it.dialogues }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(horizontal = 18.dp)
        ) {
            // ── 1. HEADER MODAL PIXEL ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(PixelColors.RetroCream)
                            .border(BorderStroke(1.8.dp, PixelColors.BorderDark), RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📖", fontSize = 18.sp)
                    }
                    Column {
                        Text(
                            text = "SỔ TAY PHẦN $unitNumber",
                            fontFamily = PlusJakartaSans,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.BorderDark
                        )
                        Text(
                            text = unitTitle,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = PixelColors.MutedText
                        )
                    }
                }

                // Nút Đóng Pixel
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PixelColors.RetroCream)
                        .border(BorderStroke(1.5.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✕", fontSize = 12.sp, color = PixelColors.BorderDark, fontWeight = FontWeight.Black)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── 2. TAB SELECTOR PIXEL ARCADE ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                HandbookTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(modifier = Modifier.weight(1f)) {
                        PixelArcadeButton(
                            text = tab.title,
                            onClick = { selectedTab = tab },
                            color = if (isSelected) PixelColors.PixelYellow else Color.White,
                            textColor = if (isSelected) PixelColors.BorderDark else PixelColors.MutedText,
                            fontSize = 10.5f,
                            contentPadding = 6.dp,
                            shadowOffset = if (isSelected) 2.5.dp else 1.dp,
                            fillMaxWidth = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── 3. NỘI DUNG THEO TAB ──
            when (selectedTab) {
                HandbookTab.GRAMMAR -> {
                    if (allGrammars.isEmpty()) {
                        EmptyHandbookNotice(message = "Chưa có cấu trúc ngữ pháp nào trong phần này.")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(allGrammars) { grammar ->
                                GrammarCardItem(grammar = grammar, onSpeak = onSpeak)
                            }
                            item { Spacer(modifier = Modifier.height(24.dp)) }
                        }
                    }
                }
                HandbookTab.VOCABULARY -> {
                    if (allVocabs.isEmpty()) {
                        EmptyHandbookNotice(message = "Chưa có danh sách từ vựng trong phần này.")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(allVocabs) { vocab ->
                                VocabCardItem(vocab = vocab, onSpeak = onSpeak)
                            }
                            item { Spacer(modifier = Modifier.height(24.dp)) }
                        }
                    }
                }
                HandbookTab.DIALOGUE -> {
                    if (allDialogues.isEmpty()) {
                        EmptyHandbookNotice(message = "Chưa có đoạn đàm thoại trong phần này.")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(allDialogues) { dialogue ->
                                DialogueCardItem(dialogue = dialogue, onSpeak = onSpeak)
                            }
                            item { Spacer(modifier = Modifier.height(24.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GrammarCardItem(
    grammar: GrammarStructureData,
    onSpeak: (String) -> Unit
) {
    PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderWidth = 1.8.dp,
        shadowOffset = 2.5.dp,
        showGrille = false,
        showRivets = false
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = grammar.title,
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.BorderDark
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Công thức Pixel Badge
            PixelBadge(
                text = "⚡ CÔNG THỨC: ${grammar.formula}",
                backgroundColor = PixelColors.CardPurple,
                textColor = PixelColors.PixelPurpleDark,
                borderColor = PixelColors.BorderDark
            )

            if (grammar.explanation.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = grammar.explanation,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = PixelColors.MutedText,
                    lineHeight = 17.sp
                )
            }

            if (!grammar.examTip.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                PixelBadge(
                    text = "💡 MẸO THI HSK: ${grammar.examTip}",
                    backgroundColor = PixelColors.CardYellow,
                    textColor = PixelColors.PixelYellowDark,
                    borderColor = PixelColors.BorderDark
                )
            }

            // Các câu ví dụ
            if (grammar.examples.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                grammar.examples.forEach { example ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = example.hanzi,
                                fontFamily = PlusJakartaSans,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.BorderDark
                            )
                            Text(
                                text = "${example.pinyinActual.ifBlank { example.pinyinOriginal }} — ${example.meaning}",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                color = PixelColors.MutedText
                            )
                            if (!example.warning.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "⚠️ ${example.warning}",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.5.sp,
                                    color = PixelColors.PixelRedDark
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(PixelColors.CardPurple)
                                .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                                .clickable { onSpeak(example.audioText ?: example.hanzi) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🔊", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VocabCardItem(
    vocab: LessonCoreVocabData,
    onSpeak: (String) -> Unit
) {
    PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderWidth = 1.8.dp,
        shadowOffset = 2.5.dp,
        showGrille = false,
        showRivets = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = vocab.hanzi,
                        fontFamily = PlusJakartaSans,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark
                    )
                    Text(
                        text = "[${vocab.pinyin}]",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.PixelPurpleDark
                    )
                }
                Text(
                    text = "${vocab.hanViet} · ${vocab.meaning}",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    color = PixelColors.MutedText
                )
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(PixelColors.CardPurple)
                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                    .clickable { onSpeak(vocab.hanzi) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔊", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun DialogueCardItem(
    dialogue: LessonDialogueData,
    onSpeak: (String) -> Unit
) {
    PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderWidth = 1.8.dp,
        shadowOffset = 2.5.dp,
        showGrille = false,
        showRivets = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${dialogue.speakerName}: ${dialogue.chinese}",
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark
                )
                Text(
                    text = "${dialogue.pinyin} — ${dialogue.vietnamese}",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    color = PixelColors.MutedText
                )
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(PixelColors.CardPurple)
                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                    .clickable { onSpeak(dialogue.chinese) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔊", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun EmptyHandbookNotice(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            fontFamily = PlusJakartaSans,
            fontSize = 13.sp,
            color = TextMuted
        )
    }
}
