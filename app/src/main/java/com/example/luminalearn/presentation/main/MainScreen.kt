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
import com.example.luminalearn.core.ui.effect.staggeredEntrance
import com.example.luminalearn.data.model.toLessonCardData
import com.example.luminalearn.presentation.common.AppScaffold
import com.example.luminalearn.presentation.common.CelebrationEffect
import com.example.luminalearn.presentation.lesson.component.LessonAction
import com.example.luminalearn.presentation.lesson.component.LessonDetailDialog
import com.example.luminalearn.presentation.lesson.component.ToneCardData
import com.example.luminalearn.presentation.main.component.AiRoleplayDialogueCard
import com.example.luminalearn.presentation.main.component.DailyProgressDuo
import com.example.luminalearn.presentation.main.component.DailyWisdomCard
import com.example.luminalearn.presentation.main.component.HeroLessonCard
import com.example.luminalearn.presentation.main.component.LessonCard
import com.example.luminalearn.presentation.main.component.MainSectionHeader
import com.example.luminalearn.presentation.main.component.QuickActionBar
import com.example.luminalearn.presentation.main.component.SparkChallengeCard
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.platform.LocalDensity
import com.example.luminalearn.presentation.main.component.CollapsingHomeTopBar
import com.example.luminalearn.presentation.main.component.TopBar
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.graphics.TransformOrigin
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

    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val collapseThresholdPx = with(density) { 80.dp.toPx() }
    val collapseProgress by remember {
        derivedStateOf {
            if (collapseThresholdPx > 0) {
                (scrollState.value / collapseThresholdPx).coerceIn(0f, 1f)
            } else 0f
        }
    }

    AppScaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Khoảng đệm đỉnh bằng đúng chiều cao mở rộng của Large Header (138dp) + 8dp thở
                Spacer(modifier = Modifier.height(146.dp))

                // ── 1. HERO CARD: TÂM ĐIỂM BÀI HỌC HSK HIỆN TẠI ──
                HeroLessonCard(
                    lesson = uiState.recommendedLessons.firstOrNull(),
                    onStartLessonClick = { lessonId -> viewModel.loadLesson(lessonId) },
                    onViewAllLessonsClick = onNavigateToLesson,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 1)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // ── 2. MICRO-DOCK: HÀNG PHÍM TẮT LUYỆN TẬP VI MÔ ──
                MainSectionHeader(
                    title = stringResource(R.string.section_quick_shortcuts),
                    subtitle = stringResource(R.string.section_quick_shortcuts_subtitle),
                    accentColors = listOf(Color(0xFF6366F1), Color(0xFF00F2FE)),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 2)
                )

                Spacer(modifier = Modifier.height(12.dp))

                QuickActionBar(
                    onRoleplayClick = onNavigateToSparkAi,
                    onPinyinClick = onNavigateToLesson,
                    onVocabClick = { navController.navigate(AppDestination.Vocabulary.route) },
                    onRadicalsClick = onNavigateToLesson,
                    onChallengeClick = {
                        confettiTrigger++
                        Toast.makeText(
                            context,
                            context.getString(R.string.msg_challenge_completed),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 2)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── 3. KHỐI CHỈ SỐ KÉP: MỤC TIÊU & CHUỖI STREAK SONG HÀNH (1:1) ──
                MainSectionHeader(
                    title = stringResource(R.string.section_daily_overview),
                    subtitle = stringResource(R.string.section_daily_overview_subtitle),
                    accentColors = listOf(Color(0xFFFF9800), Color(0xFFEA580C)),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 3)
                )

                Spacer(modifier = Modifier.height(12.dp))

                DailyProgressDuo(
                    currentMinutes = 10,
                    targetMinutes = 10,
                    bonusSparks = 30,
                    streakDays = 5,
                    checkedDays = listOf(true, true, true, true, true, false, false),
                    onStartLessonClick = onNavigateToLesson,
                    onClaimStreakClick = {
                        confettiTrigger++
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_streak_bonus_claimed, 30),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 3)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── 4. SÂN KHẤU BÀI HỌC 3D COVER FLOW (CUỘN NGANG NHẤP NHÔ) ──
                RecommendedLessonsSection(
                    isLoading = uiState.isRecommendedLessonsLoading && uiState.recommendedLessons.isEmpty(),
                    lessons = uiState.recommendedLessons,
                    onViewAllClick = onNavigateToLesson,
                    onStartLessonClick = { lessonId -> viewModel.loadLesson(lessonId) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 4)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── 5. AI SPOTLIGHT BANNER: PHÒNG LAB HỘI THOẠI BẢN XỨ ──
                MainSectionHeader(
                    title = stringResource(R.string.section_ai_spotlight),
                    subtitle = stringResource(R.string.section_ai_spotlight_subtitle),
                    accentColors = listOf(Color(0xFF6366F1), Color(0xFF00F2FE)),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 5)
                )

                Spacer(modifier = Modifier.height(12.dp))

                AiRoleplayDialogueCard(
                    onStartRoleplayClick = onNavigateToSparkAi,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 5)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── 6. THỬ THÁCH SPARK ──
                MainSectionHeader(
                    title = stringResource(R.string.section_spark_challenge),
                    subtitle = stringResource(R.string.section_spark_challenge_subtitle),
                    accentColors = listOf(Color(0xFFF59E0B), Color(0xFFEF4444)),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 6)
                )

                Spacer(modifier = Modifier.height(12.dp))

                SparkChallengeCard(
                    bonusSparks = 20,
                    onCompleteClick = {
                        confettiTrigger++
                        Toast.makeText(
                            context,
                            context.getString(R.string.msg_challenge_completed),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 6)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── 7. GÓC DANH NGÔN CẢM HỨNG ──
                MainSectionHeader(
                    title = stringResource(R.string.section_daily_wisdom),
                    subtitle = stringResource(R.string.section_daily_wisdom_subtitle),
                    accentColors = listOf(Color(0xFF10B981), Color(0xFF06B6D4)),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 7)
                )

                Spacer(modifier = Modifier.height(12.dp))

                DailyWisdomCard(
                    wisdom = uiState.dailyWisdom,
                    onRefreshClick = { viewModel.refreshDailyWisdom() },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .staggeredEntrance(index = 7)
                )

                Spacer(modifier = Modifier.height(130.dp))
            }

            // ── TOPBAR COLLAPSING STICKY: Tự động co dần và dừng đóng cố định ở mép trên màn hình ──
            CollapsingHomeTopBar(
                collapseProgress = collapseProgress,
                streakDays = 5,
                points = 240,
                hasUnreadNotification = true,
                onStreakClick = {
                    Toast.makeText(context, context.getString(R.string.msg_streak_claimed), Toast.LENGTH_SHORT).show()
                },
                onPointsClick = {
                    Toast.makeText(context, "Năng lượng: 240 Sparks", Toast.LENGTH_SHORT).show()
                },
                onNotificationClick = {
                    Toast.makeText(context, "Bạn không có thông báo mới", Toast.LENGTH_SHORT).show()
                },
                onAvatarClick = {
                    navController.navigate(AppDestination.Reward.route)
                },
                modifier = Modifier.align(Alignment.TopCenter)
            )

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
    onStartLessonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        MainSectionHeader(
            title = stringResource(R.string.section_recommended_lessons),
            subtitle = stringResource(R.string.section_recommended_lessons_subtitle),
            actionText = stringResource(R.string.view_all),
            onActionClick = onViewAllClick,
            accentColors = listOf(Color(0xFF6366F1), Color(0xFFA855F7))
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
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    pageSpacing = 14.dp,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val lessonDto = lessons[page]

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                // Tính toán độ lệch trang trực tiếp trong Render Phase (120 FPS không Recomposition)
                                val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                val absOffset = abs(pageOffset).coerceIn(0f, 1f)

                                // Khoảng cách camera 3D tạo chiều sâu không gian
                                cameraDistance = 18f * density

                                // Hiệu ứng 3D Cover Flow: Xoay quanh trục Y nghiêng góc 22 độ
                                rotationY = -pageOffset.coerceIn(-1f, 1f) * 22f

                                // Điểm tựa xoay (Pivot) tạo cảm giác lật mở tự nhiên hướng về trung tâm
                                transformOrigin = TransformOrigin(
                                    pivotFractionX = if (pageOffset < 0f) 0.05f else 0.95f,
                                    pivotFractionY = 0.5f
                                )

                                // Thu phóng và độ mờ theo chiều sâu
                                val scale = 1f - (absOffset * 0.08f)
                                scaleX = scale
                                scaleY = scale
                                alpha = 1f - (absOffset * 0.22f)
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
