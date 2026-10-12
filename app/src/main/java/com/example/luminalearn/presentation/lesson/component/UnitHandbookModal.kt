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

private val BrandIndigo = Color(0xFF5C50F6)
private val BrandIndigoLight = Color(0xFF6366F1)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)
private val CardBg = Color(0xFFF8FAFC)

private enum class HandbookTab(val title: String) {
    GRAMMAR("Ngữ pháp"),
    VOCABULARY("Từ vựng"),
    DIALOGUE("Hội thoại")
}

/**
 * Sổ tay kiến thức bỏ túi theo từng Unit (Unit Grammar Handbook).
 * Hiển thị tóm tắt lý thuyết ngữ pháp, từ mới và đàm thoại cốt lõi.
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
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 20.dp)
        ) {
            // ── 1. HEADER MODAL ──
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
                            .clip(CircleShape)
                            .background(Color(0xFFEEF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📖", fontSize = 18.sp)
                    }
                    Column {
                        Text(
                            text = "Sổ Tay Phần $unitNumber",
                            fontFamily = PlusJakartaSans,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextMain
                        )
                        Text(
                            text = unitTitle,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    }
                }

                // Nút Đóng
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✕", fontSize = 13.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── 2. TAB SELECTOR ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                HandbookTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color.White else Color.Transparent)
                            .clickable { selectedTab = tab },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.title,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) BrandIndigo else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CardBg,
        border = BorderStroke(0.6.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = grammar.title,
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Công thức
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFEEF2FF)
            ) {
                Text(
                    text = grammar.formula,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandIndigo,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            if (grammar.explanation.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = grammar.explanation,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 17.sp
                )
            }

            if (!grammar.examTip.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFFBEB),
                    border = BorderStroke(0.5.dp, Color(0xFFFDE68A))
                ) {
                    Text(
                        text = "💡 Mẹo thi HSK: ${grammar.examTip}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        color = Color(0xFFB45309),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            // Các câu ví dụ
            if (grammar.examples.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                grammar.examples.forEach { example ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = example.hanzi,
                                fontFamily = PlusJakartaSans,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                            Text(
                                text = "${example.pinyinActual.ifBlank { example.pinyinOriginal }} — ${example.meaning}",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                            if (!example.warning.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "⚠️ ${example.warning}",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.5.sp,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(BorderStroke(0.6.dp, BorderSubtle), CircleShape)
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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CardBg,
        border = BorderStroke(0.6.dp, BorderSubtle)
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextMain
                    )
                    Text(
                        text = "[${vocab.pinyin}]",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandIndigo
                    )
                }
                Text(
                    text = "${vocab.hanViet} · ${vocab.meaning}",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    color = TextMuted
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(BorderStroke(0.6.dp, BorderSubtle), CircleShape)
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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CardBg,
        border = BorderStroke(0.6.dp, BorderSubtle)
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
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                Text(
                    text = "${dialogue.pinyin} — ${dialogue.vietnamese}",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    color = TextMuted
                )
            }
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(BorderStroke(0.6.dp, BorderSubtle), CircleShape)
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
