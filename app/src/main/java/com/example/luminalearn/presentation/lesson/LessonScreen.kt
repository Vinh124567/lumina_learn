package com.example.luminalearn.presentation.lesson

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.common.LessonCardShimmerItem
import com.example.luminalearn.presentation.lesson.component.BossExamDialog
import com.example.luminalearn.presentation.lesson.component.ChestRewardDialog
import com.example.luminalearn.presentation.lesson.component.ChineseLessonData
import com.example.luminalearn.presentation.lesson.component.CollapsingLessonHeaderBar
import com.example.luminalearn.presentation.lesson.component.HeartRefillModal
import com.example.luminalearn.presentation.lesson.component.HskGrammarVaultModal
import com.example.luminalearn.presentation.lesson.component.LessonAction
import com.example.luminalearn.presentation.lesson.component.LessonCardItem
import com.example.luminalearn.presentation.lesson.component.LessonCatalogArchiveView
import com.example.luminalearn.presentation.lesson.component.LessonDetailDialog
import com.example.luminalearn.presentation.lesson.component.LessonJourneyMapView
import com.example.luminalearn.presentation.lesson.component.LessonPreviewModal
import com.example.luminalearn.presentation.lesson.component.QuickReviewModal
import com.example.luminalearn.presentation.lesson.component.SkillRadarModal
import com.example.luminalearn.presentation.lesson.component.ToneCardData
import com.example.luminalearn.presentation.lesson.component.UnitHandbookModal
import com.example.luminalearn.presentation.vocabulary.component.PaginationBar
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.launch
import java.util.Locale

private data class LessonActionCallbacks(
    val onSelectHskLevel: (String) -> Unit,
    val onSearchQueryChange: (String) -> Unit,
    val onSelectCategory: (String) -> Unit,
    val onPageChange: (Int) -> Unit,
    val onRetry: () -> Unit,
    val onSpeak: (String) -> Unit,
    val onStartLesson: (ChineseLessonData) -> Unit
)

private val FILTER_CATEGORIES = listOf(
    "Tất cả",
    "Phát âm Pinyin",
    "Ngữ pháp trọng điểm",
    "Giao tiếp thực tế",
    "Chữ Hán & Bộ thủ"
)

private val PrimaryIndigo = Color(0xFF5C50F6)
private val BrandIndigoLight = Color(0xFF6366F1)
private val BrandCyan = Color(0xFF06B6D4)
private val ScreenBackground = Color(0xFFF6F8FB)
private val BorderSubtle = Color(0xFFE2E8F0)

@Composable
fun LessonScreen(
    modifier: Modifier = Modifier,
    lessonViewModel: LessonViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by lessonViewModel.uiState.collectAsState()

    var activeLesson by remember { mutableStateOf<ChineseLessonData?>(null) }
    var currentSlideCard by remember { mutableStateOf<ToneCardData?>(null) }
    var previewModalLesson by remember { mutableStateOf<ChineseLessonData?>(null) }
    var chestRewardUnitNumber by remember { mutableStateOf<Int?>(null) }
    var selectedHandbookUnit by remember {
        mutableStateOf<Triple<Int, String, List<ChineseLessonData>>?>(null)
    }
    var quickReviewLesson by remember { mutableStateOf<ChineseLessonData?>(null) }
    var isBossExamOpen by remember { mutableStateOf(false) }
    var examQuestions by remember {
        mutableStateOf<List<com.example.luminalearn.presentation.lesson.component.BossQuestion>>(emptyList())
    }
    var isExamLoading by remember { mutableStateOf(false) }
    var examErrorMessage by remember { mutableStateOf<String?>(null) }

    var reviewQuestions by remember {
        mutableStateOf<List<com.example.luminalearn.data.model.QuickReviewQuestionDto>>(emptyList())
    }
    var isReviewLoading by remember { mutableStateOf(false) }
    var reviewErrorMessage by remember { mutableStateOf<String?>(null) }

    var isHeartModalOpen by remember { mutableStateOf(false) }
    var isAnalyticsModalOpen by remember { mutableStateOf(false) }
    var isGrammarVaultOpen by remember { mutableStateOf(false) }

    LaunchedEffect(isGrammarVaultOpen) {
        if (isGrammarVaultOpen && uiState.grammarStructures.isEmpty()) {
            lessonViewModel.loadGrammarStructures()
        }
    }

    LaunchedEffect(isBossExamOpen, uiState.selectedHskLevel) {
        if (isBossExamOpen) {
            isExamLoading = true
            examErrorMessage = null
            lessonViewModel.loadExamQuestions(
                level = uiState.selectedHskLevel,
                onSuccess = { list ->
                    examQuestions = list
                    isExamLoading = false
                },
                onError = { err ->
                    examErrorMessage = err
                    isExamLoading = false
                }
            )
        }
    }

    LaunchedEffect(quickReviewLesson) {
        val lesson = quickReviewLesson
        if (lesson != null) {
            isReviewLoading = true
            reviewErrorMessage = null
            reviewQuestions = emptyList()
            lessonViewModel.loadReviewQuestions(
                lessonId = lesson.id,
                onSuccess = { list ->
                    reviewQuestions = list
                    isReviewLoading = false
                },
                onError = { err ->
                    reviewErrorMessage = err
                    isReviewLoading = false
                }
            )
        }
    }

    // Chế độ xem: true -> Bản đồ lộ trình phiêu lưu, false -> Danh sách thẻ bài học
    var isJourneyView by rememberSaveable { mutableStateOf(true) }

    // Khởi tạo TextToSpeech hỗ trợ phát âm tiếng Trung
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.CHINESE
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val speakText: (String) -> Unit = { text ->
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        LessonMainContent(
            uiState = uiState,
            isJourneyView = isJourneyView,
            onToggleJourneyView = { isJourneyView = it },
            onSelectLessonForPreview = { lesson ->
                previewModalLesson = lesson
            },
            onOpenChest = { unitNumber, isUnlocked ->
                if (isUnlocked) {
                    chestRewardUnitNumber = unitNumber
                } else {
                    Toast.makeText(
                        context,
                        "Hoàn thành các bài học trong Phần $unitNumber để mở rương phần thưởng!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onOpenHandbook = { unitNum, title, unitLessons ->
                selectedHandbookUnit = Triple(unitNum, title, unitLessons)
            },
            onStartBossExam = { levelName, isUnlocked ->
                if (isUnlocked) {
                    isBossExamOpen = true
                } else {
                    Toast.makeText(
                        context,
                        "Hãy hoàn thành tất cả bài học trong $levelName để mở Cổng Thi Vượt Cấp!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onQuickReview = { lesson ->
                quickReviewLesson = lesson
            },
            onOpenHearts = { isHeartModalOpen = true },
            onOpenAnalytics = { isAnalyticsModalOpen = true },
            onOpenGrammarVault = { isGrammarVaultOpen = true },
            actions = LessonActionCallbacks(
                onSelectHskLevel = lessonViewModel::selectHskLevel,
                onSearchQueryChange = lessonViewModel::updateSearchQuery,
                onSelectCategory = lessonViewModel::selectCategory,
                onPageChange = lessonViewModel::changePage,
                onRetry = lessonViewModel::loadLessons,
                onSpeak = speakText,
                onStartLesson = { lesson ->
                    val hearts = uiState.heartStatus?.currentHearts ?: 5
                    if (hearts <= 0) {
                        isHeartModalOpen = true
                        Toast.makeText(
                            context,
                            "Bạn đã hết tim! Hãy nạp tim bằng Sparks hoặc ôn bài cũ để hồi phục nhé ❤️",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (lesson.slides.isNotEmpty()) {
                        activeLesson = lesson
                        currentSlideCard = lesson.slides.first()
                    } else {
                        Toast.makeText(
                            context,
                            "Bài học \"${lesson.title}\" đang hoàn thiện slide thực hành",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        )

        // Modal xem trước bài học khi người dùng bấm vào một trạm trên lộ trình
        previewModalLesson?.let { lesson ->
            LessonPreviewModal(
                lesson = lesson,
                onStartLesson = {
                    val targetLesson = previewModalLesson
                    val hearts = uiState.heartStatus?.currentHearts ?: 5
                    if (hearts <= 0) {
                        previewModalLesson = null
                        isHeartModalOpen = true
                        Toast.makeText(
                            context,
                            "Bạn đã hết tim! Hãy nạp tim bằng Sparks hoặc ôn bài cũ để hồi phục nhé ❤️",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        previewModalLesson = null
                        targetLesson?.let {
                            if (it.slides.isNotEmpty()) {
                                activeLesson = it
                                currentSlideCard = it.slides.first()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Bài học \"${it.title}\" đang hoàn thiện slide thực hành",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                onDismiss = { previewModalLesson = null },
                onSpeak = speakText
            )
        }

        // Modal Sổ tay kiến thức tóm tắt của từng Unit
        selectedHandbookUnit?.let { (unitNum, title, unitLessons) ->
            UnitHandbookModal(
                unitNumber = unitNum,
                unitTitle = title,
                lessons = unitLessons,
                onSpeak = speakText,
                onDismiss = { selectedHandbookUnit = null }
            )
        }

        // Modal Ôn tập nhanh Cắt đứt quên lãng (Spaced Repetition)
        quickReviewLesson?.let { lesson ->
            QuickReviewModal(
                lesson = lesson,
                questions = reviewQuestions,
                isLoading = isReviewLoading,
                errorMessage = reviewErrorMessage,
                onCompleted = { lessonId ->
                    lessonViewModel.submitReview(
                        lessonId = lessonId,
                        onSuccess = { sparksBonus, newStars, message ->
                            quickReviewLesson = null
                            Toast.makeText(
                                context,
                                message.ifBlank { "Hàn gắn trạm thành công! +$sparksBonus Sparks đã được cộng 🎉" },
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onError = { err ->
                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onDismiss = { quickReviewLesson = null }
            )
        }

        // Dialog mở rương báu nhận thưởng Sparks & EXP
        chestRewardUnitNumber?.let { unitNum ->
            ChestRewardDialog(
                unitNumber = unitNum,
                sparksEarned = 50,
                expEarned = 100,
                onClaim = {
                    lessonViewModel.claimChest(
                        unitNumber = unitNum,
                        onSuccess = { sparks, exp ->
                            Toast.makeText(
                                context,
                                "Chúc mừng! Bạn đã nhận được +$sparks Sparks và +$exp EXP từ máy chủ 🎉",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onDismiss = { chestRewardUnitNumber = null }
            )
        }

        // Dialog bài thi vượt cấp Boss Checkpoint
        if (isBossExamOpen) {
            BossExamDialog(
                levelName = uiState.selectedHskLevel.uppercase(),
                questions = examQuestions,
                isLoading = isExamLoading,
                errorMessage = examErrorMessage,
                onRetry = {
                    isExamLoading = true
                    examErrorMessage = null
                    lessonViewModel.loadExamQuestions(
                        level = uiState.selectedHskLevel,
                        onSuccess = { list ->
                            examQuestions = list
                            isExamLoading = false
                        },
                        onError = { err ->
                            examErrorMessage = err
                            isExamLoading = false
                        }
                    )
                },
                onDismiss = { isBossExamOpen = false },
                onPassExam = {
                    isBossExamOpen = false
                    Toast.makeText(context, "Chúc mừng bạn đã vượt cấp thành công! Mở khóa HSK 2! 🏆", Toast.LENGTH_LONG).show()
                    lessonViewModel.selectHskLevel("HSK 2")
                },
                onSpeak = speakText,
                onWrongAnswer = {
                    lessonViewModel.consumeHeart()
                }
            )
        }

        // Dialog học bài tương tác khi người dùng bắt đầu bài học
        if (currentSlideCard != null && activeLesson != null) {
            LessonInteractiveDialog(
                currentCard = currentSlideCard!!,
                lesson = activeLesson!!,
                onDismiss = {
                    currentSlideCard = null
                    activeLesson = null
                },
                onCardChange = { currentSlideCard = it },
                onComplete = { stars ->
                    val completedLessonId = activeLesson?.id
                    currentSlideCard = null
                    activeLesson = null
                    if (!completedLessonId.isNullOrBlank()) {
                        lessonViewModel.completeLesson(completedLessonId, stars)
                    }
                    Toast.makeText(context, "Chúc mừng bạn đã hoàn thành bài học đạt $stars sao! 🎉", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Modal Quản lý và Nạp Tim / Năng Lượng
        if (isHeartModalOpen) {
            val hearts = uiState.heartStatus ?: com.example.luminalearn.data.model.HeartStatusDto(
                currentHearts = 5,
                maxHearts = 5,
                nextRefillMinutes = 0,
                sparksRefillCost = 50,
                message = ""
            )
            HeartRefillModal(
                heartStatus = hearts,
                onRefillWithSparks = {
                    lessonViewModel.refillHearts(
                        onSuccess = { msg ->
                            isHeartModalOpen = false
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        },
                        onError = { err ->
                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onPracticeToEarn = {
                    isHeartModalOpen = false
                    Toast.makeText(
                        context,
                        "Hãy chọn một bài học đã qua để ôn tập và hồi tim miễn phí!",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onDismiss = { isHeartModalOpen = false }
            )
        }

        // Modal Biểu đồ Radar 5 Kỹ Năng & Thống kê cá nhân
        if (isAnalyticsModalOpen) {
            val analyticsData = uiState.analytics ?: com.example.luminalearn.data.model.AnalyticsSummaryDto(
                totalLessonsCompleted = uiState.allLessons.count { it.isCompleted },
                totalStars = uiState.allLessons.sumOf { it.stars },
                masteredVocabsCount = uiState.allLessons.sumOf { it.coreVocabularies.size },
                accuracyPercentage = 85,
                radarScores = com.example.luminalearn.data.model.SkillRadarScoreDto()
            )
            SkillRadarModal(
                analytics = analyticsData,
                onDismiss = { isAnalyticsModalOpen = false }
            )
        }

        // Modal Cẩm nang Ngữ pháp chuẩn HSK
        if (isGrammarVaultOpen) {
            HskGrammarVaultModal(
                grammarList = uiState.grammarStructures,
                isLoading = uiState.isGrammarLoading,
                onFilterLevel = { level ->
                    lessonViewModel.loadGrammarStructures(level)
                },
                onSpeak = speakText,
                onDismiss = { isGrammarVaultOpen = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LessonMainContent(
    uiState: LessonUiState,
    isJourneyView: Boolean,
    onToggleJourneyView: (Boolean) -> Unit,
    onSelectLessonForPreview: (ChineseLessonData) -> Unit,
    onOpenChest: (unitNumber: Int, isUnlocked: Boolean) -> Unit,
    onOpenHandbook: (unitNumber: Int, unitTitle: String, lessons: List<ChineseLessonData>) -> Unit,
    onStartBossExam: (levelName: String, isUnlocked: Boolean) -> Unit,
    onQuickReview: (ChineseLessonData) -> Unit,
    onOpenHearts: () -> Unit,
    onOpenAnalytics: () -> Unit,
    onOpenGrammarVault: () -> Unit,
    actions: LessonActionCallbacks
) {
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    val collapseThresholdPx = with(density) { 52.dp.toPx() }
    val lessonCollapseProgress by remember {
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

    val coroutineScope = rememberCoroutineScope()
    var isPullRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            isPullRefreshing = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .statusBarsPadding()
    ) {
        PullToRefreshBox(
            isRefreshing = isPullRefreshing,
            onRefresh = {
                isPullRefreshing = true
                actions.onRetry()
            },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 160.dp,
                    bottom = 130.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // ── NỘI DUNG CHÍNH THEO CHẾ ĐỘ XEM ──
                if (isJourneyView) {
                    // === CHẾ ĐỘ BẢN ĐỒ LỘ TRÌNH PHIÊU LƯU (GAMIFIED JOURNEY) ===
                    item(key = "journey_map_view", contentType = "journey_map") {
                        JourneyViewSection(
                            uiState = uiState,
                            onSelectLesson = onSelectLessonForPreview,
                            onOpenChest = onOpenChest,
                            onOpenHandbook = onOpenHandbook,
                            onStartBossExam = onStartBossExam,
                            onQuickReview = onQuickReview
                        )
                    }
                } else {
                    // === CHẾ ĐỘ DANH SÁCH BÀI HỌC (ARCHIVE CATALOG VIEW) ===
                    item(key = "lesson_catalog_archive", contentType = "catalog_archive") {
                        LessonCatalogArchiveView(
                            searchQuery = uiState.searchQuery,
                            onSearchQueryChange = actions.onSearchQueryChange,
                            categories = FILTER_CATEGORIES,
                            selectedCategory = uiState.selectedCategory,
                            onCategorySelect = actions.onSelectCategory,
                            lessons = uiState.filteredLessons,
                            isLoading = uiState.isLoading,
                            onSelectLesson = onSelectLessonForPreview
                        )
                    }
                }
            }
        }

        // ── 3. COLLAPSING STICKY HEADER + STICKY HSK FILTER CHIPS ──
        // Cụm Header ghim trên cùng: Khi scroll, tiêu đề co lại và dải chip HSK dừng lại ghim cố định, không bị che
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(ScreenBackground)
        ) {
            // Header bar co giãn mượt mà
            CollapsingLessonHeaderBar(
                collapseProgress = lessonCollapseProgress,
                isJourneyView = isJourneyView,
                onToggleJourneyView = onToggleJourneyView,
                isOfflineMode = uiState.isOfflineMode,
                currentHearts = uiState.heartStatus?.currentHearts ?: 5,
                maxHearts = uiState.heartStatus?.maxHearts ?: 5,
                onOpenHearts = onOpenHearts,
                onOpenAnalytics = onOpenAnalytics,
                onOpenGrammarVault = onOpenGrammarVault
            )

            // Dải thẻ chip HSK ghim cố định tại đây khi cuộn, đảm bảo luôn nhìn thấy và chọn được
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 2.dp, bottom = 8.dp)
            ) {
                HskLevelCapsuleSelector(
                    hskLevels = uiState.hskLevels,
                    selectedHskId = uiState.selectedHskLevel,
                    onSelectHskLevel = actions.onSelectHskLevel
                )
            }

            // Đường viền tóc phân tách nhẹ nhàng khi nội dung cuộn bên dưới chui qua
            if (lessonCollapseProgress > 0.3f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(BorderSubtle.copy(alpha = lessonCollapseProgress))
                )
            }
        }

        // ── 4. NÚT NỔI "VỀ BÀI ĐANG HỌC" (QUICK RESUME FAB) ──
        val showJumpFab by remember {
            derivedStateOf {
                isJourneyView && (listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 300)
            }
        }

        AnimatedVisibility(
            visible = showJumpFab,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 100.dp)
        ) {
            Surface(
                modifier = Modifier
                    .bounceClick()
                    .clickable {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                shape = RoundedCornerShape(24.dp),
                color = PrimaryIndigo,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_nav_roadmap),
                        contentDescription = "Về đầu lộ trình",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "Về bài đang học",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Thanh chọn cấp độ HSK dạng capsule cuộn ngang cao cấp.
 */
@Composable
private fun HskLevelCapsuleSelector(
    hskLevels: List<HskLevelCardData>,
    selectedHskId: String,
    onSelectHskLevel: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        hskLevels.forEach { level ->
            val isSelected = level.id.equals(selectedHskId, ignoreCase = true)
            Surface(
                modifier = Modifier
                    .bounceClick()
                    .clickable { onSelectHskLevel(level.id) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) PrimaryIndigo else Color.White,
                border = BorderStroke(
                    width = 0.5.dp,
                    color = if (isSelected) PrimaryIndigo else BorderSubtle
                ),
                shadowElevation = if (isSelected) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = level.title,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFF475569),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) Color.White.copy(alpha = 0.22f) else Color(0xFFF1F5F9),
                                shape = CircleShape
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${level.lessonCount}",
                            fontFamily = PlusJakartaSans,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF64748B),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

/**
 * Phần hiển thị Bản đồ lộ trình bài học (Gamified Journey Map View).
 */
@Composable
private fun JourneyViewSection(
    uiState: LessonUiState,
    onSelectLesson: (ChineseLessonData) -> Unit,
    onOpenChest: (unitNumber: Int, isUnlocked: Boolean) -> Unit,
    onOpenHandbook: (unitNumber: Int, unitTitle: String, lessons: List<ChineseLessonData>) -> Unit,
    onStartBossExam: (levelName: String, isUnlocked: Boolean) -> Unit,
    onQuickReview: (ChineseLessonData) -> Unit
) {
    when {
        uiState.isLoading && uiState.allLessons.isEmpty() -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) {
                    LessonCardShimmerItem()
                }
            }
        }
        uiState.currentLevelLessons.isEmpty() -> {
            LessonEmptyState(
                title = "Chưa có bài học trong cấp độ này",
                subtitle = "Vui lòng chọn cấp độ HSK khác hoặc thử lại sau"
            )
        }
        else -> {
            LessonJourneyMapView(
                lessons = uiState.currentLevelLessons,
                backendUnits = uiState.backendUnits,
                onSelectLesson = onSelectLesson,
                onOpenChest = onOpenChest,
                onOpenHandbook = onOpenHandbook,
                onStartBossExam = onStartBossExam,
                onQuickReview = onQuickReview,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Danh sách thẻ bài học dạng phân trang khi ở chế độ xem Danh sách.
 */
@Composable
private fun LessonCardsSection(
    uiState: LessonUiState,
    onSpeak: (String) -> Unit,
    onStartLesson: (ChineseLessonData) -> Unit,
    onPageChange: (Int) -> Unit
) {
    when {
        uiState.isLoading && uiState.allLessons.isEmpty() -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) {
                    LessonCardShimmerItem()
                }
            }
        }
        uiState.filteredLessons.isEmpty() -> {
            val isFiltering = uiState.searchQuery.isNotBlank() || uiState.selectedCategory != "Tất cả"
            LessonEmptyState(
                title = if (isFiltering) "Không tìm thấy bài học phù hợp" else "Chưa có bài học nào",
                subtitle = if (isFiltering) "Thử tìm kiếm với từ khóa khác hoặc đổi bộ lọc" else "Vuốt xuống để làm mới danh sách bài học"
            )
        }
        else -> {
            val containerWidth = with(LocalDensity.current) {
                LocalWindowInfo.current.containerSize.width.toDp()
            }
            val isWideScreen = containerWidth >= 540.dp
            Box(modifier = Modifier.fillMaxWidth()) {
                if (isWideScreen) {
                    val chunked = uiState.pagedLessons.chunked(2)
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        chunked.forEach { pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                pair.forEach { lesson ->
                                    LessonCardItem(
                                        lesson = lesson,
                                        modifier = Modifier.weight(1f),
                                        onActionClick = { onStartLesson(lesson) },
                                        onSpeakClick = onSpeak
                                    )
                                }
                                if (pair.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        uiState.pagedLessons.forEach { lesson ->
                            LessonCardItem(
                                lesson = lesson,
                                onActionClick = { onStartLesson(lesson) },
                                onSpeakClick = onSpeak
                            )
                        }
                    }
                }
            }

            if (uiState.filteredLessons.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                PaginationBar(
                    currentPage = uiState.safePage,
                    totalPages = uiState.totalPages,
                    totalItems = uiState.filteredLessons.size,
                    pageSize = LESSON_PAGE_SIZE,
                    itemUnit = "bài học",
                    onPageChange = onPageChange
                )
            }
        }
    }
}

@Composable
private fun LessonEmptyState(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color(0xFFEEF2FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = null,
                tint = BrandIndigoLight,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = title,
            fontFamily = PlusJakartaSans,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontFamily = PlusJakartaSans,
            fontSize = 12.sp,
            color = Color(0xFF94A3B8),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LessonInteractiveDialog(
    currentCard: ToneCardData,
    lesson: ChineseLessonData,
    onDismiss: () -> Unit,
    onCardChange: (ToneCardData?) -> Unit,
    onComplete: (Int) -> Unit
) {
    LessonDetailDialog(
        toneCardData = currentCard,
        lesson = lesson,
        action = LessonAction(
            onDismiss = onDismiss,
            onNext = {
                val nextIndex = currentCard.currentIndex
                onCardChange(lesson.slides.getOrNull(nextIndex))
            },
            onPrev = {
                val prevIndex = currentCard.currentIndex - 2
                if (prevIndex >= 0) {
                    onCardChange(lesson.slides.getOrNull(prevIndex))
                }
            },
            onComplete = { onComplete(3) },
            onCompleteWithStars = { stars -> onComplete(stars) }
        )
    )
}
