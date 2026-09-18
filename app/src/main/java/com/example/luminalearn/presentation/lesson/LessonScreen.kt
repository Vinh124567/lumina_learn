package com.example.luminalearn.presentation.lesson

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luminalearn.presentation.lesson.component.ChineseLessonData
import com.example.luminalearn.presentation.lesson.component.HskLevelDetailBanner
import com.example.luminalearn.presentation.lesson.component.HskLevelFilterBar
import com.example.luminalearn.presentation.lesson.component.LessonAction
import com.example.luminalearn.presentation.lesson.component.LessonCardItem
import com.example.luminalearn.presentation.lesson.component.LessonDetailDialog
import com.example.luminalearn.presentation.lesson.component.LessonFilterRow
import com.example.luminalearn.presentation.lesson.component.LessonHeader
import com.example.luminalearn.presentation.lesson.component.LessonSearchBar
import com.example.luminalearn.presentation.lesson.component.ToneCardData
import com.example.luminalearn.presentation.vocabulary.component.PaginationBar
import kotlinx.coroutines.launch
import java.util.Locale

private val FILTER_CATEGORIES = listOf(
    "Tất cả",
    "Phát âm Pinyin",
    "Ngữ pháp trọng điểm",
    "Giao tiếp thực tế",
    "Chữ Hán & Bộ thủ"
)

@Composable
fun LessonScreen(
    modifier: Modifier = Modifier,
    lessonViewModel: LessonViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by lessonViewModel.uiState.collectAsState()

    var activeLesson by remember { mutableStateOf<ChineseLessonData?>(null) }
    var currentSlideCard by remember { mutableStateOf<ToneCardData?>(null) }

    // TextToSpeech hỗ trợ phát âm tiếng Trung
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
            .background(Color(0xFFF8FAFC))
    ) {
        LessonMainContent(
            uiState = uiState,
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
                        "Bài học \"${lesson.title}\" đang cập nhật slide tương tác",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        // Dialog học bài tương tác khi người dùng bấm vào bài học
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
                    Toast.makeText(context, "Chúc mừng bạn đã hoàn thành bài học!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun LessonMainContent(
    uiState: LessonUiState,
    onSelectHskLevel: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (String) -> Unit,
    onPageChange: (Int) -> Unit,
    onRetry: () -> Unit,
    onSpeak: (String) -> Unit,
    onStartLesson: (ChineseLessonData) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // 1. Header: Thư viện bài học & Luyện thi HSK (Cố định, không scroll)
        LessonHeader(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFC))
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        // 2. Nội dung bên dưới được scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Phân loại cấp độ & Thang điểm HSK
            HskLevelFilterBar(
                hskLevels = uiState.hskLevels,
                selectedHskId = uiState.selectedHskLevel,
                totalLessonCount = uiState.allLessons.size,
                matchedLessonCount = uiState.filteredLessons.size,
                onSelectHskLevel = onSelectHskLevel
            )

        // Banner Tiêu chuẩn Thang điểm HSK chi tiết khi chọn 1 cấp độ (HSK 1, HSK 2, ...)
        if (uiState.selectedHskLevel != "all") {
            val levelLessons = uiState.allLessons.filter {
                it.level.equals(uiState.selectedHskLevel, ignoreCase = true)
            }
            val completed = levelLessons.count { it.isCompleted }
            val total = levelLessons.size.coerceAtLeast(1)

            Spacer(modifier = Modifier.height(16.dp))

            HskLevelDetailBanner(
                level = uiState.selectedHskLevel,
                completedCount = completed,
                totalCount = total,
                onViewVocabClick = { level ->
                    Toast.makeText(context, "Mở danh sách từ vựng $level", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3. Thanh tìm kiếm bài học
        LessonSearchBar(
            query = uiState.searchQuery,
            onQueryChange = onSearchQueryChange
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Hàng Filter chip theo thể loại
        LessonFilterRow(
            filters = FILTER_CATEGORIES,
            selectedFilter = uiState.selectedCategory,
            onFilterSelect = onSelectCategory
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Tiêu đề danh sách bài học & số lượng
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Danh sách bài học đề xuất",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155)
            )
            Text(
                text = "${uiState.filteredLessons.size} bài",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6366F1)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 5. Trạng thái tải / lỗi / Danh sách các thẻ bài học
        LessonCardsSection(
            uiState = uiState,
            onRetry = onRetry,
            onSpeak = onSpeak,
            onStartLesson = onStartLesson,
            onPageChange = { targetPage ->
                onPageChange(targetPage)
                coroutineScope.launch {
                    scrollState.animateScrollTo(0)
                }
            }
        )

        // Khoảng đệm đáy 96dp để không bị Bottom Bar che khuất
        Spacer(modifier = Modifier.height(96.dp))
    }
}
}

@Composable
private fun LessonCardsSection(
    uiState: LessonUiState,
    onRetry: () -> Unit,
    onSpeak: (String) -> Unit,
    onStartLesson: (ChineseLessonData) -> Unit,
    onPageChange: (Int) -> Unit
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF5C50F6),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        uiState.errorMessage != null -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.errorMessage,
                    color = Color(0xFFEF4444),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C50F6)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Thử lại", color = Color.White)
                }
            }
        }
        uiState.filteredLessons.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không tìm thấy bài học nào phù hợp.",
                    color = Color(0xFF94A3B8),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        else -> {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val isWideScreen = maxWidth >= 540.dp
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

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun LessonScreenPreview() {
    LessonScreen()
}
