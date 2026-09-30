package com.example.luminalearn.presentation.vocabulary

import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.example.luminalearn.core.util.TextToSpeechHelper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.example.luminalearn.presentation.common.VocabCardShimmerItem
import com.example.luminalearn.presentation.vocabulary.component.FlashcardActions
import com.example.luminalearn.presentation.vocabulary.component.FlashcardInteractiveSection
import com.example.luminalearn.presentation.vocabulary.component.PaginationBar
import com.example.luminalearn.presentation.vocabulary.component.QuizActions
import com.example.luminalearn.presentation.vocabulary.component.TopicSelectorBar
import com.example.luminalearn.presentation.vocabulary.component.VocabDetailCard
import com.example.luminalearn.presentation.vocabulary.component.VocabDetailDialog
import com.example.luminalearn.presentation.vocabulary.component.VocabFilterCard
import com.example.luminalearn.presentation.vocabulary.component.VocabFilterCounts
import com.example.luminalearn.presentation.vocabulary.component.VocabHeaderSection
import com.example.luminalearn.presentation.vocabulary.component.VocabHeaderTitle
import com.example.luminalearn.presentation.vocabulary.component.VocabProgressBar
import com.example.luminalearn.presentation.vocabulary.component.VocabReflexQuizSection
import com.example.luminalearn.presentation.vocabulary.component.VocabStudyModeTabs
import com.example.luminalearn.presentation.vocabulary.component.VocabTopBar
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabSourceFilter
import com.example.luminalearn.presentation.vocabulary.model.VocabStudyMode
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.launch
import java.util.Locale

private const val PAGE_SIZE = 15

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyScreen(
    modifier: Modifier = Modifier,
    viewModel: VocabularyViewModel = viewModel()
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initSrs(context)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is VocabularyUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val ttsHelper = remember { TextToSpeechHelper(context) }
    DisposableEffect(Unit) {
        onDispose {
            ttsHelper.shutdown()
        }
    }

    val onSpeakWord: (String) -> Unit = remember(ttsHelper) {
        { text: String -> ttsHelper.speak(text) }
    }

    val onSpeakWordSlow: (String) -> Unit = remember(ttsHelper) {
        { text: String -> ttsHelper.speakSlow(text) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VocabColors.ScreenBg)
    ) {
        VocabTopBar(
            onAddVocabClick = {
                viewModel.processIntent(VocabularyUiIntent.SetAddVocabSheetVisible(true))
            },
            onAskAiClick = {
                viewModel.processIntent(VocabularyUiIntent.SetAddVocabSheetVisible(true))
            }
        )

        // ── 1. Chọn 3 chế độ học (Ghim ngay dưới TopBar chuẩn UX) ──
        VocabStudyModeTabs(
            selectedMode = uiState.selectedMode,
            filteredCount = uiState.filteredList.size,
            onSelectMode = { mode ->
                viewModel.processIntent(VocabularyUiIntent.SelectMode(mode))
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )

        var isPullRefreshing by remember { mutableStateOf(false) }

        LaunchedEffect(uiState.isLoadingVocab) {
            if (!uiState.isLoadingVocab) {
                isPullRefreshing = false
            }
        }

        PullToRefreshBox(
            isRefreshing = isPullRefreshing,
            onRefresh = {
                isPullRefreshing = true
                viewModel.processIntent(VocabularyUiIntent.LoadInitialData)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
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

                        // 2. Card Bộ lọc đa tầng: Nguồn từ + Cấp độ HSK + Chủ đề từ vựng
                        VocabFilterCard(
                            selectedSource = uiState.selectedSource,
                            onSelectSource = { source ->
                                viewModel.processIntent(VocabularyUiIntent.SelectSource(source))
                            },
                            counts = VocabFilterCounts(
                                allCount = uiState.vocabList.size,
                                dueTodayCount = uiState.dueTodayCount,
                                customCount = uiState.vocabList.count { it.isCustom },
                                masteredCount = uiState.masteredCount
                            ),
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

                        // 4. Thanh Tìm kiếm từ vựng & Nút Thêm từ mới (Chỉ hiển thị khi ở chế độ Danh sách)
                        if (uiState.selectedMode == VocabStudyMode.LIST) {
                            VocabSearchField(
                                searchQuery = uiState.searchQuery,
                                onSearchQueryChange = { query ->
                                    viewModel.processIntent(VocabularyUiIntent.UpdateSearchQuery(query))
                                },
                                onAddVocabClick = {
                                    viewModel.processIntent(VocabularyUiIntent.SetAddVocabSheetVisible(true))
                                },
                                onAskAiClick = {
                                    viewModel.processIntent(VocabularyUiIntent.SetAddVocabSheetVisible(true))
                                }
                            )
                        }
                    }
                }

                vocabStudyModeContent(
                    uiState = uiState,
                    viewModel = viewModel,
                    onSpeakWord = onSpeakWord,
                    onSpeakWordSlow = onSpeakWordSlow,
                    onScrollToTop = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                )
            }
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
                },
                initialTab = uiState.activeDetailTab
            )
        }

        if (uiState.isAddVocabSheetOpen) {
            com.example.luminalearn.presentation.vocabulary.component.AddVocabBottomSheet(
                isOpen = uiState.isAddVocabSheetOpen,
                isSubmitting = uiState.isSubmittingVocab,
                isAiLookingUp = uiState.isAiLookingUp,
                onDismiss = {
                    viewModel.processIntent(VocabularyUiIntent.SetAddVocabSheetVisible(false))
                },
                onLookupAi = { query, callback ->
                    viewModel.lookupAi(query, callback)
                },
                onSubmit = { request ->
                    viewModel.processIntent(VocabularyUiIntent.AddNewVocabulary(request) {})
                }
            )
        }
    }
}

@Composable
private fun VocabSearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddVocabClick: () -> Unit,
    onAskAiClick: () -> Unit
) {
    val searchShape = RoundedCornerShape(16.dp)
    Spacer(modifier = Modifier.height(14.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Khung tìm kiếm từ vựng
        androidx.compose.material3.Card(
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            shape = searchShape,
            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = VocabColors.BrandPrimary,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Tìm kiếm từ vựng...",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        textStyle = TextStyle(
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.5.sp,
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

        // Nút [✦ Tạo bộ từ AI]
        Surface(
            modifier = Modifier
                .height(44.dp)
                .clickable(onClick = onAskAiClick),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFD6E0FF)),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sparkle),
                    contentDescription = null,
                    tint = VocabColors.BrandPrimary,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "AI gợi ý",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.BrandPrimary,
                    maxLines = 1
                )
            }
        }

        // Nút [+ Thêm từ]
        Surface(
            modifier = Modifier
                .height(44.dp)
                .clickable(onClick = onAddVocabClick),
            shape = RoundedCornerShape(16.dp),
            color = VocabColors.BrandPrimary,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Thêm từ",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
            }
        }
    }
}

private fun LazyListScope.vocabStudyModeContent(
    uiState: VocabularyUiState,
    viewModel: VocabularyViewModel,
    onSpeakWord: (String) -> Unit,
    onSpeakWordSlow: (String) -> Unit,
    onScrollToTop: () -> Unit
) {
    if (uiState.isLoadingVocab && uiState.vocabList.isEmpty()) {
        items(count = 4, key = { "vocab_shimmer_$it" }) {
            VocabCardShimmerItem()
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
                    text = if (uiState.selectedSource == VocabSourceFilter.DUE_TODAY) {
                        "🎉 Bạn đã ôn tập xong tất cả từ cần ôn hôm nay!"
                    } else {
                        "Chưa có từ vựng nào phù hợp"
                    },
                    fontSize = 14.sp,
                    color = VocabColors.TextMuted
                )
            }
        }
        return
    }

    when (uiState.selectedMode) {
        VocabStudyMode.FLASHCARD -> flashcardModeContent(uiState, viewModel, onSpeakWord, onSpeakWordSlow)
        VocabStudyMode.REFLEX -> reflexModeContent(uiState, viewModel, onSpeakWord)
        VocabStudyMode.LIST -> listModeContent(uiState, viewModel, onSpeakWord, onScrollToTop)
    }
}

private fun LazyListScope.flashcardModeContent(
    uiState: VocabularyUiState,
    viewModel: VocabularyViewModel,
    onSpeakWord: (String) -> Unit,
    onSpeakWordSlow: (String) -> Unit
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
                    onSpeakSlow = onSpeakWordSlow,
                    onToggleMastered = { wordId ->
                        viewModel.processIntent(VocabularyUiIntent.ToggleMastered(wordId))
                    },
                    onOpenDetail = {
                        viewModel.processIntent(VocabularyUiIntent.OpenWordDetail(word))
                    },
                    onVoiceTest = { voiceWord ->
                        viewModel.processIntent(VocabularyUiIntent.OpenWordDetail(voiceWord, initialTab = 2))
                    },
                    onRateSrs = { rating ->
                        viewModel.processIntent(VocabularyUiIntent.RateWordSrs(word.id, rating))
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
            },
            onQuickVoiceTest = {
                viewModel.processIntent(VocabularyUiIntent.OpenWordDetail(wordItem, initialTab = 2))
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
