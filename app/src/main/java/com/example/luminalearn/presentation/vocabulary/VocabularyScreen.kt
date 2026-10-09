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
import com.example.luminalearn.core.ui.effect.staggeredEntrance
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
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.luminalearn.presentation.main.component.MainSectionHeader
import com.example.luminalearn.presentation.vocabulary.component.CollapsingVocabHeaderBar
import com.example.luminalearn.ui.theme.PlusJakartaSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyScreen(
    modifier: Modifier = Modifier,
    viewModel: VocabularyViewModel = viewModel()
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    val collapseThresholdPx = with(density) { 52.dp.toPx() }
    val vocabCollapseProgress by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) {
                1f
            } else if (collapseThresholdPx > 0) {
                (listState.firstVisibleItemScrollOffset / collapseThresholdPx).coerceIn(0f, 1f)
            } else {
                0f
            }
        }
    }
    val uiState by viewModel.uiState.collectAsState()

    var isFlashcardPracticeOpen by remember { mutableStateOf(false) }
    var isReflexQuizPracticeOpen by remember { mutableStateOf(false) }
    var isSpeedMatchOpen by remember { mutableStateOf(false) }
    var selectedHskDetailData by remember { mutableStateOf<HskLevelCardData?>(null) }
    var selectedHanziDetailData by remember { mutableStateOf<com.example.luminalearn.presentation.vocabulary.component.HanziSpotlightData?>(null) }

    BackHandler(enabled = selectedHskDetailData != null || selectedHanziDetailData != null) {
        if (selectedHanziDetailData != null) {
            selectedHanziDetailData = null
        } else {
            selectedHskDetailData = null
        }
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
                    // Mở màn hình con: Trượt vào với độ nảy vật lý Spring nhẹ và zoom từ 94%
                    (slideInHorizontally(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ) { width -> (width * 0.92f).toInt() } + fadeIn(tween(280)) + scaleIn(
                        initialScale = 0.94f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )).togetherWith(
                        slideOutHorizontally(
                            animationSpec = tween(280)
                        ) { width -> (-width * 0.25f).toInt() } + fadeOut(tween(220)) + scaleOut(
                            targetScale = 0.96f,
                            animationSpec = tween(280)
                        )
                    )
                } else {
                    // Đóng màn hình con quay lại: Màn hình cha hồi phục mượt mà
                    (slideInHorizontally(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ) { width -> (-width * 0.25f).toInt() } + fadeIn(tween(280)) + scaleIn(
                        initialScale = 0.96f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )).togetherWith(
                        slideOutHorizontally(
                            animationSpec = tween(280)
                        ) { width -> (width * 0.92f).toInt() } + fadeOut(tween(220)) + scaleOut(
                            targetScale = 0.94f,
                            animationSpec = tween(280)
                        )
                    )
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF6F8FB))
                        .statusBarsPadding()
                ) {
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
                                top = 114.dp,
                                bottom = 130.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {

                            // ── 2. PHÂN KHU 1: NHỊP HỌC HÔM NAY (SRS, PHẢN XẠ & ĐẤU GHÉP 60S) ──
                            item(key = "section_daily_practice_header", contentType = "section_header") {
                                MainSectionHeader(
                                    title = "Nhịp học hôm nay",
                                    subtitle = "Ôn tập ngắt quãng SRS và thử thách phản xạ nhanh",
                                    accentColors = listOf(Color(0xFF6366F1), Color(0xFF00F2FE)),
                                    modifier = Modifier.staggeredEntrance(index = 1)
                                )
                            }

                            item(key = "vocab_daily_practice_duo", contentType = "practice_duo") {
                                com.example.luminalearn.presentation.vocabulary.component.VocabDailyPracticeDuo(
                                    dueTodayCount = uiState.dueTodayCount,
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
                                    },
                                    modifier = Modifier.staggeredEntrance(index = 1)
                                )
                            }

                            item(key = "speed_word_match_card", contentType = "gamification") {
                                com.example.luminalearn.presentation.vocabulary.component.SpeedWordMatchCard(
                                    onStartMatch = { isSpeedMatchOpen = true },
                                    modifier = Modifier.staggeredEntrance(index = 2)
                                )
                            }

                            // ── 3. PHÂN KHU 2: LỘ TRÌNH CẤP ĐỘ HSK ──
                            item(key = "section_hsk_route_header", contentType = "section_header") {
                                MainSectionHeader(
                                    title = "Lộ trình cấp độ HSK",
                                    subtitle = "Từ vựng chuẩn HSK 1 đến 6 theo khung quốc tế",
                                    actionText = "Mở kho từ",
                                    onActionClick = {
                                        selectedHskDetailData = com.example.luminalearn.presentation.vocabulary.component.HSK_LEVEL_INFOS.firstOrNull()
                                    },
                                    accentColors = listOf(Color(0xFF06B6D4), Color(0xFF3B82F6)),
                                    modifier = Modifier.staggeredEntrance(index = 3)
                                )
                            }

                            item(key = "hsk_mini_pager", contentType = "hsk_pager") {
                                com.example.luminalearn.presentation.vocabulary.component.HskMiniPagerCard(
                                    hskLevels = com.example.luminalearn.presentation.vocabulary.component.HSK_LEVEL_INFOS,
                                    onOpenHskLevel = { levelData ->
                                        selectedHskDetailData = levelData
                                    },
                                    modifier = Modifier.staggeredEntrance(index = 3)
                                )
                            }

                            item(key = "hsk_mastery_breakdown", contentType = "mastery_breakdown") {
                                com.example.luminalearn.presentation.vocabulary.component.HskMasteryBreakdownCard(
                                    vocabList = uiState.vocabList,
                                    masteredCount = uiState.masteredCount,
                                    totalCount = uiState.totalCount,
                                    progressPercent = uiState.progressPercent,
                                    onOpenHskLevel = { levelData ->
                                        selectedHskDetailData = levelData
                                    },
                                    modifier = Modifier.staggeredEntrance(index = 4)
                                )
                            }

                            // ── 4. PHÂN KHU 3: KHÁM PHÁ & CHIẾT TỰ ──
                            item(key = "section_hanzi_spotlight_header", contentType = "section_header") {
                                MainSectionHeader(
                                    title = "Chiết tự tâm điểm",
                                    subtitle = "Thấu hiểu triết lý và kết cấu chữ Hán mỗi ngày",
                                    accentColors = listOf(Color(0xFF10B981), Color(0xFF059669)),
                                    modifier = Modifier.staggeredEntrance(index = 5)
                                )
                            }

                            item(key = "daily_hanzi_spotlight", contentType = "hanzi_spotlight") {
                                com.example.luminalearn.presentation.vocabulary.component.DailyHanziSpotlightCard(
                                    onSpeakWord = onSpeakWord,
                                    onOpenDetail = { hanziData ->
                                        selectedHanziDetailData = hanziData
                                    },
                                    modifier = Modifier.staggeredEntrance(index = 5)
                                )
                            }
                        }
                    }

                    // ── COLLAPSING STICKY HEADER (Cùng màu với Body, liền mạch tuyệt đối) ──
                    CollapsingVocabHeaderBar(
                        collapseProgress = vocabCollapseProgress,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }

        // ── Dialog Luyện Flashcard SRS tập trung ──
        FlashcardPracticeDialog(
            isOpen = isFlashcardPracticeOpen,
            currentWord = uiState.currentFlashcardWord,
            nextWord = uiState.nextFlashcardWord,
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

        // ── Dialog Thử thách Đấu ghép từ 60s Gamification ──
        com.example.luminalearn.presentation.vocabulary.component.SpeedWordMatchDialog(
            isOpen = isSpeedMatchOpen,
            vocabList = uiState.vocabList,
            onDismiss = { isSpeedMatchOpen = false }
        )

        // ── Dialog Chi Tiết Chiết Tự Chữ Hán Chuyên Sâu (Ô Mễ Tự Cách & Cội Nguồn) ──
        if (selectedHanziDetailData != null) {
            com.example.luminalearn.presentation.vocabulary.component.HanziSpotlightDetailDialog(
                hanziData = selectedHanziDetailData!!,
                onDismiss = { selectedHanziDetailData = null },
                onSpeak = onSpeakWord
            )
        }
    }
}
