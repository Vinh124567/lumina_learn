package com.example.luminalearn.presentation.lesson

import android.speech.tts.TextToSpeech
import android.widget.Toast
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
import com.example.luminalearn.presentation.lesson.component.ChineseLessonData
import com.example.luminalearn.presentation.lesson.component.CollapsingLessonHeaderBar
import com.example.luminalearn.presentation.lesson.component.LessonAction
import com.example.luminalearn.presentation.lesson.component.LessonCardItem
import com.example.luminalearn.presentation.lesson.component.LessonDetailDialog
import com.example.luminalearn.presentation.lesson.component.LessonFilterRow
import com.example.luminalearn.presentation.lesson.component.LessonJourneyMapView
import com.example.luminalearn.presentation.lesson.component.LessonPreviewModal
import com.example.luminalearn.presentation.lesson.component.LessonSearchBar
import com.example.luminalearn.presentation.lesson.component.LessonTimelineListView
import com.example.luminalearn.presentation.lesson.component.ToneCardData
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
            actions = LessonActionCallbacks(
                onSelectHskLevel = lessonViewModel::selectHskLevel,
                onSearchQueryChange = lessonViewModel::updateSearchQuery,
                onSelectCategory = lessonViewModel::selectCategory,
                onPageChange = lessonViewModel::changePage,
                onRetry = lessonViewModel::loadLessons,
                onSpeak = speakText,
                onStartLesson = { lesson ->
                    if (lesson.slides.isNotEmpty()) {
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
                },
                onDismiss = { previewModalLesson = null },
                onSpeak = speakText
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
                onComplete = {
                    currentSlideCard = null
                    activeLesson = null
                    Toast.makeText(context, "Chúc mừng bạn đã hoàn thành xuất sắc bài học!", Toast.LENGTH_SHORT).show()
                }
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
                            onSelectLesson = onSelectLessonForPreview
                        )
                    }
                } else {
                    // === CHẾ ĐỘ DANH SÁCH BÀI HỌC (TIMELINE STEPPER DETAIL) ===
                    item(key = "lesson_search_bar", contentType = "search_bar") {
                        LessonSearchBar(
                            query = uiState.searchQuery,
                            onQueryChange = actions.onSearchQueryChange
                        )
                    }

                    item(key = "lesson_filter_row", contentType = "filter_row") {
                        LessonFilterRow(
                            filters = FILTER_CATEGORIES,
                            selectedFilter = uiState.selectedCategory,
                            onFilterSelect = actions.onSelectCategory
                        )
                    }

                    item(key = "lesson_timeline_list", contentType = "timeline_list") {
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
                                LessonTimelineListView(
                                    lessons = uiState.filteredLessons,
                                    onSelectLesson = onSelectLessonForPreview,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
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
                onToggleJourneyView = onToggleJourneyView
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
    onSelectLesson: (ChineseLessonData) -> Unit
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
            LessonEmptyState(
                title = "Chưa có bài học trong cấp độ này",
                subtitle = "Vui lòng chọn cấp độ HSK khác hoặc thử lại sau"
            )
        }
        else -> {
            LessonJourneyMapView(
                lessons = uiState.filteredLessons,
                onSelectLesson = onSelectLesson,
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
    onComplete: () -> Unit
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
            onComplete = onComplete
        )
    )
}
