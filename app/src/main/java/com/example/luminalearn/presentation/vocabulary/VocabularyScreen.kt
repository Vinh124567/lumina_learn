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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import com.example.luminalearn.core.ui.effect.bounceClick
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luminalearn.R
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import com.example.luminalearn.presentation.vocabulary.component.AddVocabBottomSheet
import com.example.luminalearn.presentation.vocabulary.component.FlashcardActions
import com.example.luminalearn.presentation.vocabulary.component.FlashcardPracticeDialog
import com.example.luminalearn.presentation.vocabulary.component.HskLevelCardData
import com.example.luminalearn.presentation.vocabulary.component.HskLevelCoverFlowCarousel
import com.example.luminalearn.presentation.vocabulary.component.HskVocabGridScreen
import com.example.luminalearn.presentation.vocabulary.component.QuizActions
import com.example.luminalearn.presentation.vocabulary.component.ReflexQuizPracticeDialog
import com.example.luminalearn.presentation.vocabulary.component.VocabBentoPracticeHub
import com.example.luminalearn.presentation.vocabulary.component.VocabDetailDialog
import com.example.luminalearn.presentation.vocabulary.component.VocabHeroBanner
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.luminalearn.presentation.main.component.MainSectionHeader
import com.example.luminalearn.presentation.main.component.TopBar
import com.example.luminalearn.ui.theme.PlusJakartaSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyScreen(
    modifier: Modifier = Modifier,
    viewModel: VocabularyViewModel = viewModel()
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val uiState by viewModel.uiState.collectAsState()

    var isFlashcardPracticeOpen by remember { mutableStateOf(false) }
    var isReflexQuizPracticeOpen by remember { mutableStateOf(false) }
    var selectedHskDetailData by remember { mutableStateOf<HskLevelCardData?>(null) }

    BackHandler(enabled = selectedHskDetailData != null) {
        selectedHskDetailData = null
    }

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

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = selectedHskDetailData,
            transitionSpec = {
                if (targetState != null) {
                    (slideInHorizontally { width -> width } + fadeIn(tween(250)))
                        .togetherWith(slideOutHorizontally { width -> -width / 3 } + fadeOut(tween(250)))
                } else {
                    (slideInHorizontally { width -> -width / 3 } + fadeIn(tween(250)))
                        .togetherWith(slideOutHorizontally { width -> width } + fadeOut(tween(250)))
                }
            },
            label = "hsk_vocab_screen_transition",
            modifier = Modifier.fillMaxSize()
        ) { hskData ->
            if (hskData != null) {
                HskVocabGridScreen(
                    hskData = hskData,
                    vocabList = uiState.vocabList,
                    dueTodayCount = uiState.dueTodayCount,
                    isLoading = uiState.isLoadingVocab,
                    onBack = { selectedHskDetailData = null },
                    onSpeakWord = onSpeakWord,
                    onSpeakWordSlow = onSpeakWordSlow,
                    onToggleMastered = { wordId ->
                        viewModel.processIntent(VocabularyUiIntent.ToggleMastered(wordId))
                    },
                    onToggleEnrollSrs = { wordId ->
                        viewModel.processIntent(VocabularyUiIntent.ToggleEnrollSrs(wordId))
                    },
                    onOpenWordDetail = { word ->
                        viewModel.processIntent(VocabularyUiIntent.OpenWordDetail(word))
                    },
                    onAddVocabClick = {
                        viewModel.processIntent(VocabularyUiIntent.SetAddVocabSheetVisible(true))
                    }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF6F8FB))
                ) {
                    TopBar(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        streakDays = 5,
                        points = 240
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
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 110.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // ── 1. HERO BANNER TIẾN ĐỘ TỔNG QUAN ──
                            item(key = "vocab_hero_banner", contentType = "hero") {
                                VocabHeroBanner(
                                    masteredCount = uiState.masteredCount,
                                    totalCount = uiState.totalCount,
                                    progressPercent = uiState.progressPercent
                                )
                            }

                            // ── 2. CAROUSEL 3D CẤP ĐỘ HSK 1–6 ──
                            item(key = "vocab_hsk_carousel_header", contentType = "header") {
                                MainSectionHeader(
                                    title = "Lộ trình cấp độ HSK",
                                    subtitle = "Vuốt ngang & chạm thẻ để mở kho từ vựng HSK",
                                    accentColors = listOf(Color(0xFF6366F1), Color(0xFF00F2FE))
                                )
                            }

                            item(key = "vocab_hsk_carousel", contentType = "carousel") {
                                HskLevelCoverFlowCarousel(
                                    hskLevels = uiState.hskLevels,
                                    selectedHskIndex = uiState.selectedHskIndex,
                                    vocabList = uiState.vocabList,
                                    onSelectHskLevel = { _, _ -> },
                                    onOpenHskLevel = { levelData ->
                                        selectedHskDetailData = levelData
                                    }
                                )
                            }

                            // ── 3. CỤM BENTO HUBS: LUYỆN TẬP VI MÔ (SRS & QUIZ) ──
                            item(key = "vocab_bento_header", contentType = "header") {
                                MainSectionHeader(
                                    title = "Luyện tập vi mô",
                                    subtitle = "Ôn tập ngắt quãng SRS Ebbinghaus & Thử thách phản xạ",
                                    accentColors = listOf(Color(0xFF6366F1), Color(0xFF00F2FE))
                                )
                            }

                            item(key = "vocab_bento_hub", contentType = "bento") {
                                VocabBentoPracticeHub(
                                    dueTodayCount = uiState.dueTodayCount,
                                    quizQuestionCount = 10,
                                    onStartFlashcard = {
                                        if (uiState.dueTodayCount > 0) {
                                            viewModel.processIntent(VocabularyUiIntent.SelectSource(com.example.luminalearn.presentation.vocabulary.model.VocabSourceFilter.DUE_TODAY))
                                        } else {
                                            viewModel.processIntent(VocabularyUiIntent.SelectSource(com.example.luminalearn.presentation.vocabulary.model.VocabSourceFilter.ALL))
                                        }
                                        isFlashcardPracticeOpen = true
                                    },
                                    onStartQuiz = {
                                        viewModel.processIntent(VocabularyUiIntent.StartReflexQuiz)
                                        isReflexQuizPracticeOpen = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Dialog Luyện Flashcard SRS tập trung ──
        FlashcardPracticeDialog(
            isOpen = isFlashcardPracticeOpen,
            currentWord = uiState.currentFlashcardWord,
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
                onOpenDetail = { word ->
                    viewModel.processIntent(VocabularyUiIntent.OpenWordDetail(word))
                },
                onVoiceTest = { voiceWord ->
                    viewModel.processIntent(VocabularyUiIntent.OpenWordDetail(voiceWord, initialTab = 2))
                },
                onRateSrs = { rating ->
                    uiState.currentFlashcardWord?.let { w ->
                        viewModel.processIntent(VocabularyUiIntent.RateWordSrs(w.id, rating))
                    }
                }
            ),
            onDismiss = {
                isFlashcardPracticeOpen = false
                viewModel.processIntent(VocabularyUiIntent.SelectSource(com.example.luminalearn.presentation.vocabulary.model.VocabSourceFilter.ALL))
            }
        )

        // ── Dialog Trắc nghiệm phản xạ HSK ──
        ReflexQuizPracticeDialog(
            isOpen = isReflexQuizPracticeOpen,
            quizState = uiState.quizState,
            actions = QuizActions(
                onSelectOption = { option ->
                    viewModel.processIntent(VocabularyUiIntent.SelectQuizOption(option))
                },
                onRestart = {
                    viewModel.processIntent(VocabularyUiIntent.RestartReflexQuiz)
                },
                onBackToList = {
                    isReflexQuizPracticeOpen = false
                    viewModel.processIntent(VocabularyUiIntent.RestartReflexQuiz)
                }
            ),
            onSpeak = onSpeakWord,
            onDismiss = { isReflexQuizPracticeOpen = false }
        )

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
                onToggleEnrollSrs = { wordId ->
                    viewModel.processIntent(VocabularyUiIntent.ToggleEnrollSrs(wordId))
                },
                initialTab = uiState.activeDetailTab
            )
        }

        if (uiState.isAddVocabSheetOpen) {
            AddVocabBottomSheet(
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
