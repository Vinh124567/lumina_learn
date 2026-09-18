package com.example.luminalearn.presentation.vocabulary

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luminalearn.R
import com.example.luminalearn.presentation.vocabulary.component.FlashcardActions
import com.example.luminalearn.presentation.vocabulary.component.FlashcardInteractiveSection
import com.example.luminalearn.presentation.vocabulary.component.PaginationBar
import com.example.luminalearn.presentation.vocabulary.component.QuizActions
import com.example.luminalearn.presentation.vocabulary.component.TopicSelectorBar
import com.example.luminalearn.presentation.vocabulary.component.VocabDetailCard
import com.example.luminalearn.presentation.vocabulary.component.VocabDetailDialog
import com.example.luminalearn.presentation.vocabulary.component.VocabFilterCard
import com.example.luminalearn.presentation.vocabulary.component.VocabHeaderSection
import com.example.luminalearn.presentation.vocabulary.component.VocabHeaderTitle
import com.example.luminalearn.presentation.vocabulary.component.VocabProgressBar
import com.example.luminalearn.presentation.vocabulary.component.VocabReflexQuizSection
import com.example.luminalearn.presentation.vocabulary.component.VocabStudyModeTabs
import com.example.luminalearn.presentation.vocabulary.component.VocabTopBar
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabStudyMode
import kotlinx.coroutines.launch
import java.util.Locale

private const val PAGE_SIZE = 15

@Composable
fun VocabularyScreen(
    modifier: Modifier = Modifier,
    viewModel: VocabularyViewModel = viewModel()
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    // ── TextToSpeech phát âm tiếng Trung (View-only concern) ──
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.SIMPLIFIED_CHINESE
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val onSpeakWord: (String) -> Unit = remember(tts) {
        { text: String ->
            tts?.setSpeechRate(1.0f)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
                ?: Toast.makeText(context, "Phát âm: $text", Toast.LENGTH_SHORT).show()
        }
    }

    val onSpeakWordSlow: (String) -> Unit = remember(tts) {
        { text: String ->
            tts?.setSpeechRate(0.6f)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
            tts?.setSpeechRate(1.0f)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VocabColors.ScreenBg)
    ) {
        VocabTopBar(
            onAskAiClick = {
                Toast.makeText(context, "Mở AI tạo bộ từ vựng theo chủ đề...", Toast.LENGTH_SHORT).show()
            }
        )

        // ── 1. Chọn 3 chế độ học (Ghim ngay dưới TopBar chuẩn UX) ──
        VocabStudyModeTabs(
            selectedMode = uiState.selectedMode,
            filteredCount = uiState.filteredList.size,
            onSelectMode = { mode ->
                viewModel.processIntent(VocabularyUiIntent.SelectMode(mode))
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 110.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item(key = "vocab_header", contentType = "header") {
                Column {
                    // 1. Tiêu đề màn hình
                    VocabHeaderTitle()

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Card Bộ lọc: Cấp độ HSK + Chủ đề từ vựng
                    VocabFilterCard(
                        hskLevels = uiState.hskLevels,
                        selectedHskIndex = uiState.selectedHskIndex,
                        onSelectHskLevel = { index, _ ->
                            viewModel.processIntent(VocabularyUiIntent.SelectHskLevel(index))
                        },
                        topicsList = uiState.topics,
                        selectedTopic = uiState.selectedTopic,
                        onSelectTopic = { topicName ->
                            viewModel.processIntent(VocabularyUiIntent.SelectTopic(topicName))
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Thanh Tiến độ học
                    VocabProgressBar(
                        masteredCount = uiState.masteredCount,
                        totalCount = uiState.totalCount,
                        progressPercent = uiState.progressPercent
                    )

                    // 4. Thanh Tìm kiếm từ vựng (Chỉ hiển thị khi ở chế độ Danh sách)
                    if (uiState.selectedMode == VocabStudyMode.LIST) {
                        VocabSearchField(
                            searchQuery = uiState.searchQuery,
                            onSearchQueryChange = { query ->
                                viewModel.processIntent(VocabularyUiIntent.UpdateSearchQuery(query))
                            }
                        )
                    }
                }
            }

            vocabStudyModeContent(
                uiState = uiState,
                viewModel = viewModel,
                onSpeakWord = onSpeakWord,
                onScrollToTop = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            )
        }

        // ── Dialog Chi tiết Từ vựng khi người dùng ấn vào 1 từ ──
        if (uiState.activeDetailWord != null) {
            VocabDetailDialog(
                word = uiState.activeDetailWord!!,
                onDismiss = {
                    viewModel.processIntent(VocabularyUiIntent.DismissWordDetail)
                },
                onSpeak = onSpeakWord,
                onSpeakSlow = onSpeakWordSlow,
                onToggleMastered = { wordId ->
                    viewModel.processIntent(VocabularyUiIntent.ToggleMastered(wordId))
                }
            )
        }
    }
}

@Composable
private fun VocabSearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    val searchShape = RoundedCornerShape(16.dp)
    Spacer(modifier = Modifier.height(14.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.White, searchShape)
            .border(1.dp, Color(0xFFF1F5F9), searchShape)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search",
                tint = VocabColors.BrandPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Tìm kiếm Hán tự, Pinyin, Hán Việt hoặc nghĩa...",
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    textStyle = TextStyle(
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0F172A)
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(VocabColors.BrandPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun LazyListScope.vocabStudyModeContent(
    uiState: VocabularyUiState,
    viewModel: VocabularyViewModel,
    onSpeakWord: (String) -> Unit,
    onScrollToTop: () -> Unit
) {
    if (uiState.isLoadingVocab) {
        item(key = "loading_state") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Đang tải từ vựng...",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = VocabColors.BrandPrimary
                )
            }
        }
        return
    }

    if (uiState.filteredList.isEmpty()) {
        item(key = "empty_state") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 36.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Chưa có từ vựng nào phù hợp",
                    fontSize = 14.sp,
                    color = VocabColors.TextMuted
                )
            }
        }
        return
    }

    when (uiState.selectedMode) {
        VocabStudyMode.FLASHCARD -> flashcardModeContent(uiState, viewModel, onSpeakWord)
        VocabStudyMode.REFLEX -> reflexModeContent(uiState, viewModel, onSpeakWord)
        VocabStudyMode.LIST -> listModeContent(uiState, viewModel, onSpeakWord, onScrollToTop)
    }
}

private fun LazyListScope.flashcardModeContent(
    uiState: VocabularyUiState,
    viewModel: VocabularyViewModel,
    onSpeakWord: (String) -> Unit
) {
    uiState.currentFlashcardWord?.let { word ->
        item(key = "flashcard_card_item") {
            FlashcardInteractiveSection(
                currentWord = word,
                currentIndex = uiState.safeFlashcardIndex,
                totalCount = uiState.filteredList.size,
                isFlipped = uiState.isCardFlipped,
                actions = FlashcardActions(
                    onFlip = { viewModel.processIntent(VocabularyUiIntent.FlipFlashcard) },
                    onPrevious = { viewModel.processIntent(VocabularyUiIntent.PreviousFlashcard) },
                    onNext = { viewModel.processIntent(VocabularyUiIntent.NextFlashcard) },
                    onSpeak = onSpeakWord,
                    onToggleMastered = { wordId ->
                        viewModel.processIntent(VocabularyUiIntent.ToggleMastered(wordId))
                    }
                )
            )
        }
    }
}

private fun LazyListScope.reflexModeContent(
    uiState: VocabularyUiState,
    viewModel: VocabularyViewModel,
    onSpeakWord: (String) -> Unit
) {
    item(key = "reflex_quiz_item") {
        VocabReflexQuizSection(
            quizState = uiState.quizState,
            actions = QuizActions(
                onSelectOption = { option ->
                    viewModel.processIntent(VocabularyUiIntent.SelectQuizOption(option))
                },
                onRestart = {
                    viewModel.processIntent(VocabularyUiIntent.RestartReflexQuiz)
                },
                onBackToList = {
                    viewModel.processIntent(VocabularyUiIntent.SelectMode(VocabStudyMode.LIST))
                }
            ),
            onSpeak = onSpeakWord
        )
    }
}

private fun LazyListScope.listModeContent(
    uiState: VocabularyUiState,
    viewModel: VocabularyViewModel,
    onSpeakWord: (String) -> Unit,
    onScrollToTop: () -> Unit
) {
    items(
        items = uiState.pagedList,
        key = { it.id },
        contentType = { "vocab_card" }
    ) { wordItem ->
        VocabDetailCard(
            item = wordItem,
            onSpeak = onSpeakWord,
            onToggleMastered = { wordId ->
                viewModel.processIntent(VocabularyUiIntent.ToggleMastered(wordId))
            },
            onClick = {
                viewModel.processIntent(VocabularyUiIntent.OpenWordDetail(wordItem))
            }
        )
    }

    item(key = "pagination_bar", contentType = "pagination") {
        PaginationBar(
            currentPage = uiState.safePage,
            totalPages = uiState.totalPages,
            totalItems = uiState.filteredList.size,
            pageSize = PAGE_SIZE,
            onPageChange = { targetPage ->
                if (targetPage in 1..uiState.totalPages && targetPage != uiState.currentPage) {
                    viewModel.processIntent(VocabularyUiIntent.ChangePage(targetPage))
                    onScrollToTop()
                }
            }
        )
    }
}
