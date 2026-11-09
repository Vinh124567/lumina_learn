package com.example.luminalearn.presentation.lesson.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.common.LessonCardShimmerItem
import com.example.luminalearn.presentation.lesson.LessonUiState
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.launch

private val FILTER_CATEGORIES = listOf(
    "Tất cả",
    "Phát âm Pinyin",
    "Ngữ pháp trọng điểm",
    "Giao tiếp thực tế",
    "Chữ Hán & Bộ thủ"
)

private val PrimaryIndigo = Color(0xFF5C50F6)
private val BrandIndigoLight = Color(0xFF6366F1)
private val ScreenBackground = Color(0xFFF6F8FB)
private val BorderSubtle = Color(0xFFE2E8F0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonMainContent(
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
    onSelectHskLevel: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (String) -> Unit,
    onRetry: () -> Unit
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
                onRetry()
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
                            onQuickReview = onQuickReview,
                            onOpenAnalytics = onOpenAnalytics
                        )
                    }
                } else {
                    // === CHẾ ĐỘ DANH SÁCH BÀI HỌC (ARCHIVE CATALOG VIEW) ===
                    item(key = "lesson_catalog_archive", contentType = "catalog_archive") {
                        LessonCatalogArchiveView(
                            searchQuery = uiState.searchQuery,
                            onSearchQueryChange = onSearchQueryChange,
                            categories = FILTER_CATEGORIES,
                            selectedCategory = uiState.selectedCategory,
                            onCategorySelect = onSelectCategory,
                            lessons = uiState.filteredLessons,
                            isLoading = uiState.isLoading,
                            onSelectLesson = onSelectLessonForPreview
                        )
                    }
                }
            }
        }

        // ── COLLAPSING STICKY HEADER + STICKY HSK FILTER CHIPS ──
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

            // Dải thẻ chip HSK ghim cố định tại đây khi cuộn
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 2.dp, bottom = 8.dp)
            ) {
                HskLevelCapsuleSelector(
                    hskLevels = uiState.hskLevels,
                    selectedHskId = uiState.selectedHskLevel,
                    onSelectHskLevel = onSelectHskLevel
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

        // ── NÚT NỔI "VỀ BÀI ĐANG HỌC" (QUICK RESUME FAB) ──
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
            PixelArcadeButton(
                text = "📍 VỀ BÀI ĐANG HỌC",
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                color = PixelColors.PixelYellow,
                fontSize = 11.5f,
                contentPadding = 8.dp,
                shadowOffset = 3.dp
            )
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
    onQuickReview: (ChineseLessonData) -> Unit,
    onOpenAnalytics: () -> Unit = {}
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
                onOpenAnalytics = onOpenAnalytics,
                modifier = Modifier.fillMaxWidth()
            )
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
