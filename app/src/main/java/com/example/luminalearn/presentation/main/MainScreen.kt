package com.example.luminalearn.presentation.main

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luminalearn.R
import com.example.luminalearn.data.model.toLessonCardData
import com.example.luminalearn.presentation.common.AppScaffold
import com.example.luminalearn.presentation.common.CelebrationEffect
import com.example.luminalearn.presentation.lesson.component.LessonAction
import com.example.luminalearn.presentation.lesson.component.LessonDetailDialog
import com.example.luminalearn.presentation.lesson.component.ToneCardData
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.luminalearn.presentation.main.component.AiRoleplayDialogueCard
import com.example.luminalearn.presentation.main.component.DailyProgressDuo
import com.example.luminalearn.presentation.main.component.DailyWisdomCard
import com.example.luminalearn.presentation.main.component.GreetingHeader
import com.example.luminalearn.presentation.main.component.LessonCard
import com.example.luminalearn.presentation.main.component.MainSectionHeader
import com.example.luminalearn.presentation.main.component.QuickActionBar
import com.example.luminalearn.presentation.main.component.SparkChallengeCard
import com.example.luminalearn.presentation.main.component.TopBar
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.abs
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavHostController,
    onNavigateToSparkAi: () -> Unit = { navController.navigate(AppDestination.SparkAI.route) },
    onNavigateToLesson: () -> Unit = { navController.navigate(AppDestination.Lesson.route) }
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()
    var confettiTrigger by remember { mutableIntStateOf(0) }
    var lessonContent by remember { mutableStateOf<ToneCardData?>(null) }

    LaunchedEffect(uiState.lessonSlides) {
        if (uiState.lessonSlides.isNotEmpty()) {
            lessonContent = uiState.lessonSlides.first()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            handleMainUiEffect(context, effect)
        }
    }

    AppScaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopBar(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp),
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // ── PHÂN KHU 1: Hero & Chỉ Số Mục Tiêu Ngày ──
                    GreetingHeader(
                        onAskAiClick = onNavigateToSparkAi
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    MainSectionHeader(
                        title = stringResource(R.string.section_daily_overview),
                        subtitle = stringResource(R.string.section_daily_overview_subtitle)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DailyProgressDuo(
                        currentMinutes = 10,
                        targetMinutes = 10,
                        bonusSparks = 30,
                        streakDays = 5,
                        checkedDays = listOf(true, true, true, true, true, false, false),
                        onStartLessonClick = onNavigateToLesson,
                        onClaimStreakClick = { confettiTrigger++ }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── PHÂN KHU 2: Phím Tắt Luyện Nhanh Vi Mô ──
                    MainSectionHeader(
                        title = stringResource(R.string.section_quick_shortcuts)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    QuickActionBar(
                        onRoleplayClick = onNavigateToSparkAi,
                        onPinyinClick = onNavigateToLesson,
                        onVocabClick = { navController.navigate(AppDestination.Vocabulary.route) },
                        onRadicalsClick = onNavigateToLesson,
                        onChallengeClick = onNavigateToSparkAi
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── PHÂN KHU 3: Tiêu Điểm Lumina AI & Thử Thách ──
                    MainSectionHeader(
                        title = stringResource(R.string.section_ai_spotlight),
                        subtitle = stringResource(R.string.section_ai_spotlight_subtitle)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AiRoleplayDialogueCard(
                        onStartRoleplayClick = onNavigateToSparkAi
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SparkChallengeCard(
                        bonusSparks = 20,
                        onCompleteClick = {
                            confettiTrigger++
                            Toast.makeText(
                                context,
                                context.getString(R.string.msg_challenge_completed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    // ── PHÂN KHU 4: Lộ Trình & Bài Học Đề Xuất (Vuốt Ngang Carousel) ──
                    RecommendedLessonsSection(
                        isLoading = uiState.isRecommendedLessonsLoading && uiState.recommendedLessons.isEmpty(),
                        lessons = uiState.recommendedLessons,
                        onViewAllClick = onNavigateToLesson,
                        onStartLessonClick = { lessonId ->
                            viewModel.loadLesson(lessonId)
                        }
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    // ── PHÂN KHU 5: Góc Danh Ngôn Cảm Hứng ──
                    MainSectionHeader(
                        title = stringResource(R.string.section_daily_wisdom)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DailyWisdomCard(
                        wisdom = uiState.dailyWisdom,
                        onRefreshClick = {
                            viewModel.refreshDailyWisdom()
                        }
                    )

                    Spacer(modifier = Modifier.height(96.dp))
                }
            }
            CelebrationEffect(
                triggerKey = confettiTrigger,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    lessonContent?.let { currentCard ->
        MainLessonDetailDialog(
            currentCard = currentCard,
            slides = uiState.lessonSlides,
            onDismiss = {
                lessonContent = null
                viewModel.clearLesson()
            },
            onCardChange = { nextCard ->
                lessonContent = nextCard
            },
            onComplete = {
                lessonContent = null
                viewModel.clearLesson()
                confettiTrigger++
            }
        )
    }
}

private fun handleMainUiEffect(context: android.content.Context, effect: MainUiEffect) {
    when (effect) {
        is MainUiEffect.ShowToast -> {
            Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun RecommendedLessonsSection(
    isLoading: Boolean,
    lessons: List<com.example.luminalearn.data.model.LessonDto>,
    onViewAllClick: () -> Unit,
    onStartLessonClick: (String) -> Unit
) {
    MainSectionHeader(
        title = stringResource(R.string.section_recommended_lessons),
        subtitle = stringResource(R.string.section_recommended_lessons_subtitle),
        actionText = stringResource(R.string.view_all),
        onActionClick = onViewAllClick
    )

    Spacer(modifier = Modifier.height(14.dp))

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = Color(0xFF5538EE)
            )
        }
    } else if (lessons.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { lessons.size })

        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(end = 42.dp),
                pageSpacing = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val lessonDto = lessons[page]

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            // Tính toán trực tiếp trong Render Phase (GraphicsLayer)
                            // Tránh hoàn toàn Recomposition khi vuốt kéo, đảm bảo mượt mà 120 FPS
                            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            val absOffset = abs(pageOffset).coerceIn(0f, 1f)

                            val scale = 1f - (absOffset * 0.06f)
                            scaleX = scale
                            scaleY = scale
                            translationY = absOffset * 10.dp.toPx()
                            alpha = 1f - (absOffset * 0.18f)
                        }
                ) {
                    LessonCard(
                        data = lessonDto.toLessonCardData(),
                        modifier = Modifier.fillMaxWidth(),
                        onStartClick = {
                            onStartLessonClick(lessonDto.id)
                        }
                    )
                }
            }

            if (lessons.size > 1) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(lessons.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .height(4.dp)
                                .width(if (isSelected) 18.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Color(0xFF6366F1) else Color(0xFFCBD5E1).copy(alpha = 0.5f)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainLessonDetailDialog(
    currentCard: ToneCardData,
    slides: List<ToneCardData>,
    onDismiss: () -> Unit,
    onCardChange: (ToneCardData?) -> Unit,
    onComplete: () -> Unit
) {
    LessonDetailDialog(
        toneCardData = currentCard,
        action = LessonAction(
            onDismiss = onDismiss,
            onNext = {
                onCardChange(slides.getOrNull(currentCard.currentIndex))
            },
            onPrev = {
                val prevIndex = currentCard.currentIndex - 2
                if (prevIndex >= 0) {
                    onCardChange(slides.getOrNull(prevIndex))
                }
            },
            onComplete = onComplete
        )
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        navController = androidx.navigation.compose.rememberNavController()
    )
}
