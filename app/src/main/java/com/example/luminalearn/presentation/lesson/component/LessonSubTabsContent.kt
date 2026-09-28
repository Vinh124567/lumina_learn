package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.vocabulary.component.VocabWritingPracticeTabContent
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Tab 2: Từ vựng cốt lõi
 */
@Composable
fun LessonVocabularyTabContent(
    vocabList: List<LessonCoreVocabData> = emptyList(),
    onSpeak: (String) -> Unit,
    onNavigateToWriting: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (vocabList.isEmpty()) {
        VocabEmptyState(modifier = modifier)
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        VocabHeaderSection(count = vocabList.size)

        Spacer(modifier = Modifier.height(2.dp))

        vocabList.forEach { item ->
            LessonVocabCardItem(item = item, onSpeak = onSpeak)
        }

        Spacer(modifier = Modifier.height(6.dp))

        NavigateToWritingButton(onClick = onNavigateToWriting)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun VocabEmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "📚", fontSize = 36.sp)
            Text(
                text = "Chưa có từ vựng cốt lõi",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                fontFamily = PlusJakartaSans
            )
            Text(
                text = "Bài học này tập trung vào lý thuyết và hiện chưa có danh sách từ vựng trọng tâm.",
                fontSize = 12.5.sp,
                color = Color(0xFF64748B),
                fontFamily = PlusJakartaSans,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun VocabHeaderSection(count: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Từ vựng cốt lõi của bài",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFEEF2FF))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "$count từ vựng then chốt",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F46E5)
                )
            }
        }

        Text(
            text = "Bấm vào loa để nghe phát âm chuẩn bản xứ từng từ và câu ví dụ",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
        )
    }
}

@Composable
private fun LessonVocabCardItem(
    item: LessonCoreVocabData,
    onSpeak: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Hàng trên: Hán tự + Pinyin/Loa/Hán Việt + Loại từ
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF5538EE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.hanzi,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = item.pinyin,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEEF2FF))
                                    .clickable { onSpeak(item.hanzi) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_speaker),
                                    contentDescription = "Phát âm từ",
                                    tint = Color(0xFF4F46E5),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        if (item.hanViet.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Hán Việt: ${item.hanViet}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            }
                        }
                    }
                }

                if (item.partOfSpeech.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = item.partOfSpeech,
                            fontSize = 10.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Text(
                text = item.meaning,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
            )

            if (item.exampleHanzi.isNotBlank() || item.exampleMeaning.isNotBlank()) {
                VocabExampleBox(item = item, onSpeak = onSpeak)
            }
        }
    }
}

@Composable
private fun VocabExampleBox(
    item: LessonCoreVocabData,
    onSpeak: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (item.exampleHanzi.isNotBlank()) {
                    Text(
                        text = item.exampleHanzi,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
                if (item.examplePinyin.isNotBlank()) {
                    Text(
                        text = item.examplePinyin,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F46E5)
                    )
                }
                if (item.exampleMeaning.isNotBlank()) {
                    Text(
                        text = item.exampleMeaning,
                        fontSize = 11.5.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (item.exampleHanzi.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                        .clickable { onSpeak(item.exampleHanzi) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_speaker),
                        contentDescription = "Phát âm câu",
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NavigateToWritingButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        shape = RoundedCornerShape(50),
        color = Color(0xFF5C50F6),
        shadowElevation = 1.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Luyện viết chữ Hán",
                fontFamily = PlusJakartaSans,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/**
 * Tab 3: Đoạn hội thoại mẫu
 */
@Composable
fun LessonDialogueTabContent(
    dialogueContext: String = "",
    dialogues: List<LessonDialogueData> = emptyList(),
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (dialogues.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "💬", fontSize = 36.sp)
                Text(
                    text = "Chưa có đoạn hội thoại",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontFamily = PlusJakartaSans
                )
                Text(
                    text = "Bài học này hiện chưa có đoạn hội thoại thực hành.",
                    fontSize = 12.5.sp,
                    color = Color(0xFF64748B),
                    fontFamily = PlusJakartaSans,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    val displayDialogues = dialogues
    val displayContext = dialogueContext

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (displayContext.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEF2FF))
                    .padding(12.dp)
            ) {
                Text(
                    text = if (displayContext.startsWith("💡")) displayContext else "💡 Ngữ cảnh: $displayContext",
                    fontSize = 11.5.sp,
                    color = Color(0xFF4338CA),
                    lineHeight = 16.sp
                )
            }
        }

        displayDialogues.forEach { line ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(line.badgeColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = line.speakerRole,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = line.badgeColor
                            )
                        }

                        Column {
                            Text(
                                text = line.speakerName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = line.chinese,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = line.pinyin,
                                fontSize = 12.sp,
                                color = Color(0xFF4F46E5),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = line.vietnamese,
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .clickable { onSpeak(line.chinese) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = "Nghe thoại",
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tab 4: Luyện viết Hán tự (米字格)
 */
@Composable
fun LessonWritingTabContent(
    vocabList: List<LessonCoreVocabData> = emptyList(),
    modifier: Modifier = Modifier
) {
    if (vocabList.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "✍️", fontSize = 36.sp)
                Text(
                    text = "Chưa có chữ Hán luyện viết",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontFamily = PlusJakartaSans
                )
                Text(
                    text = "Bài học này chưa có từ vựng chữ Hán cần luyện viết nét.",
                    fontSize = 12.5.sp,
                    color = Color(0xFF64748B),
                    fontFamily = PlusJakartaSans,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    var selectedWordIndex by remember { mutableIntStateOf(0) }
    val words = remember(vocabList) {
        vocabList.mapIndexed { index, v ->
            VocabWordItem(
                id = "w_$index",
                hanzi = v.hanzi,
                pinyin = v.pinyin,
                hanViet = v.hanViet,
                meaning = v.meaning,
                partOfSpeech = v.partOfSpeech,
                topic = "",
                strokes = "",
                radical = "",
                hskLevel = "",
                exampleHanzi = v.exampleHanzi,
                examplePinyin = v.examplePinyin,
                exampleMeaning = v.exampleMeaning
            )
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Selector chọn chữ cần viết
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            words.forEachIndexed { index, w ->
                val isSelected = index == selectedWordIndex
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clickable { selectedWordIndex = index },
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) Color(0xFF5538EE) else Color(0xFFF1F5F9)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${w.hanzi} (${w.pinyin})",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF334155)
                        )
                    }
                }
            }
        }

        // Tái sử dụng ô vẽ chữ Hán ô Mễ tự
        VocabWritingPracticeTabContent(
            word = words[selectedWordIndex],
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Tab 5: Luyện tập & Phản xạ (0/3)
 */
@Composable
fun LessonQuizTabContent(
    vocabList: List<LessonCoreVocabData> = emptyList(),
    modifier: Modifier = Modifier
) {
    if (vocabList.isEmpty()) {
        QuizEmptyState(modifier = modifier)
        return
    }

    val questions = remember(vocabList) { generateQuizQuestions(vocabList) }
    var selectedAnswers by remember { mutableStateOf(mapOf<Int, Int>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        QuizProgressHeader(
            completedCount = selectedAnswers.size,
            totalCount = questions.size
        )

        questions.forEachIndexed { qIndex, q ->
            QuizQuestionCard(
                qIndex = qIndex,
                q = q,
                userSelected = selectedAnswers[qIndex],
                onSelectOption = { qIdx, optIdx ->
                    selectedAnswers = selectedAnswers + (qIdx to optIdx)
                }
            )
        }
    }
}

private fun generateQuizQuestions(vocabList: List<LessonCoreVocabData>): List<QuizQuestion> {
    return vocabList.take(5).mapIndexed { index, word ->
        val otherMeanings = vocabList
            .filter { it.hanzi != word.hanzi }
            .map { it.meaning }
            .shuffled()
            .take(3)
        val fallbackDistractors = listOf("Xin chào", "Cảm ơn", "Tạm biệt", "Rất tốt")
            .filter { it != word.meaning && !otherMeanings.contains(it) }
        val wrongOptions = (otherMeanings + fallbackDistractors).take(3)
        val options = (wrongOptions + word.meaning).shuffled()
        val correctIndex = options.indexOf(word.meaning)

        QuizQuestion(
            title = "Câu ${index + 1}: Nghĩa của từ vựng",
            question = "Từ \"${word.hanzi}\" (${word.pinyin}) có nghĩa là gì?",
            options = options,
            correctIndex = correctIndex,
            explanation = "Chính xác! \"${word.hanzi}\" (${word.pinyin}) nghĩa là: ${word.meaning}."
        )
    }
}

@Composable
private fun QuizEmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "🎯", fontSize = 36.sp)
            Text(
                text = "Chưa có bài tập củng cố",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                fontFamily = PlusJakartaSans
            )
            Text(
                text = "Bài tập củng cố phản xạ cho bài học này hiện chưa được khởi tạo.",
                fontSize = 12.5.sp,
                color = Color(0xFF64748B),
                fontFamily = PlusJakartaSans,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun QuizProgressHeader(completedCount: Int, totalCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Bài tập củng cố phản xạ",
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFEEF2FF))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = "Tiến độ: $completedCount/$totalCount câu",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4F46E5)
            )
        }
    }
}

@Composable
private fun QuizQuestionCard(
    qIndex: Int,
    q: QuizQuestion,
    userSelected: Int?,
    onSelectOption: (Int, Int) -> Unit
) {
    val isAnswered = userSelected != null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = q.title,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6366F1)
            )
            Text(
                text = q.question,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            q.options.forEachIndexed { optIndex, optText ->
                val isChoice = userSelected == optIndex
                val isCorrect = optIndex == q.correctIndex
                val (bgColor, borderColor, textColor) = getQuizOptionColors(isAnswered, isChoice, isCorrect)

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isAnswered) {
                            onSelectOption(qIndex, optIndex)
                        },
                    shape = RoundedCornerShape(10.dp),
                    color = bgColor,
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                ) {
                    Text(
                        text = "${('A' + optIndex)}. $optText",
                        fontSize = 12.sp,
                        fontWeight = if (isChoice || isCorrect) FontWeight.Bold else FontWeight.Medium,
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)
                    )
                }
            }

            if (isAnswered) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = q.explanation,
                    fontSize = 11.sp,
                    color = if (userSelected == q.correctIndex) Color(0xFF16A34A) else Color(0xFFB45309),
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun getQuizOptionColors(
    isAnswered: Boolean,
    isChoice: Boolean,
    isCorrect: Boolean
): Triple<Color, Color, Color> {
    return when {
        !isAnswered -> Triple(Color(0xFFF8FAFC), Color(0xFFE2E8F0), Color(0xFF334155))
        isChoice && isCorrect -> Triple(Color(0xFFDCFCE7), Color(0xFF86EFAC), Color(0xFF16A34A))
        isChoice && !isCorrect -> Triple(Color(0xFFFEE2E2), Color(0xFFFCA5A5), Color(0xFFDC2626))
        isCorrect -> Triple(Color(0xFFDCFCE7), Color(0xFF86EFAC), Color(0xFF16A34A))
        else -> Triple(Color(0xFFF8FAFC), Color(0xFFE2E8F0), Color(0xFF94A3B8))
    }
}

private data class QuizQuestion(
    val title: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)
