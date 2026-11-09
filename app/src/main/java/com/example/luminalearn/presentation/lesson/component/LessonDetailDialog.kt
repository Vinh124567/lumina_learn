package com.example.luminalearn.presentation.lesson.component

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans

enum class LessonViewMode {
    COMPREHENSIVE, // Chế độ "Toàn diện"
    SLIDE_CARDS    // Chế độ "Thẻ trượt"
}

/**
 * Màn hình học bài chi tiết (Phong cách Retro 16-Bit Pixel Arcade Console).
 * Đầy đủ 2 chế độ: Toàn diện và Thẻ trượt, 5 Tab kiến thức chuyên sâu.
 */
@Composable
fun LessonDetailDialog(
    action: LessonAction,
    toneCardData: ToneCardData,
    lesson: ChineseLessonData? = null,
    initialIsCompleted: Boolean = false,
    sparksReward: Int = 30
) {
    val context = LocalContext.current
    var isCompleted by remember(toneCardData.title) { mutableStateOf(initialIsCompleted) }
    var viewMode by remember { mutableStateOf(LessonViewMode.COMPREHENSIVE) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Bài giảng, 1: Từ vựng, 2: Hội thoại, 3: Luyện viết, 4: Luyện tập

    val effectiveLesson = remember(lesson, toneCardData) {
        lesson ?: ChineseLessonData(
            title = toneCardData.title,
            category = toneCardData.category,
            pinyinHanziTitle = toneCardData.subTitle,
            description = toneCardData.explanationContent,
            slides = listOf(toneCardData)
        )
    }

    Dialog(
        onDismissRequest = action.onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Hộp Pixel Arcade Console Bottom Sheet với bóng đổ 3D ở đỉnh
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    .background(PixelColors.ShadowDark)
                    .padding(top = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(PixelColors.SurfaceLight)
                        .border(
                            width = 2.dp,
                            color = PixelColors.BorderDark,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                    ) {
                        AnimatedContent(
                            targetState = isCompleted,
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(280, easing = LinearOutSlowInEasing)) +
                                 slideInHorizontally(animationSpec = tween(280, easing = FastOutSlowInEasing)) { it / 6 })
                                    .togetherWith(
                                        fadeOut(animationSpec = tween(180, easing = FastOutLinearInEasing))
                                    ).using(SizeTransform(clip = false))
                            },
                            label = "LessonCompletionTransition"
                        ) { completed ->
                            if (completed) {
                                LessonSuccessContent(
                                    toneCardData = toneCardData,
                                    sparksReward = sparksReward,
                                    action = action
                                )
                            } else {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    // ── 1. Header chung cố định ở đỉnh dạng Pixel ──
                                    LessonDialogHeader(
                                        lesson = effectiveLesson,
                                        currentViewMode = viewMode,
                                        onToggleViewMode = { viewMode = it },
                                        onAskAi = {
                                            Toast.makeText(context, "Gia sư AI đang chuẩn bị hỗ trợ bài này...", Toast.LENGTH_SHORT).show()
                                            action.onAskAi()
                                        },
                                        onDismiss = action.onDismiss
                                    )

                                    // ── 2. Nội dung theo chế độ Toàn diện hoặc Thẻ trượt ──
                                    if (viewMode == LessonViewMode.COMPREHENSIVE) {
                                        // Thanh 5 Tab Navigation dạng phím Arcade dập nổi
                                        LessonTabsNavigationRow(
                                            selectedTab = selectedTab,
                                            onSelectTab = { selectedTab = it }
                                        )

                                        // Nội dung cuộn của tab được chọn
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                                .clipToBounds()
                                        ) {
                                            when (selectedTab) {
                                                0 -> LessonComprehensiveTabContent(
                                                    lesson = effectiveLesson,
                                                    onSpeak = { text ->
                                                        Toast.makeText(context, "Phát âm: $text", Toast.LENGTH_SHORT).show()
                                                    },
                                                    onNavigateToVocab = { selectedTab = 1 },
                                                    onNavigateToQuiz = { selectedTab = 4 }
                                                )
                                                1 -> LessonVocabularyTabContent(
                                                    vocabList = effectiveLesson.coreVocabularies,
                                                    onSpeak = { text ->
                                                        Toast.makeText(context, "Phát âm: $text", Toast.LENGTH_SHORT).show()
                                                    },
                                                    onNavigateToWriting = { selectedTab = 3 }
                                                )
                                                2 -> LessonDialogueTabContent(
                                                    dialogueContext = effectiveLesson.dialogueContext,
                                                    dialogues = effectiveLesson.dialogues,
                                                    onSpeak = { text ->
                                                        Toast.makeText(context, "Phát âm hội thoại: $text", Toast.LENGTH_SHORT).show()
                                                    }
                                                )
                                                3 -> LessonWritingTabContent(
                                                    vocabList = effectiveLesson.coreVocabularies
                                                )
                                                4 -> LessonQuizTabContent(
                                                    vocabList = effectiveLesson.coreVocabularies
                                                )
                                            }
                                        }
                                    } else {
                                        // Chế độ Thẻ trượt
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            LessonSlideInteractiveBody(
                                                toneCardData = toneCardData,
                                                action = action,
                                                onComplete = { isCompleted = true }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Header chính của màn hình học bài dạng Pixel Console Cartridge
 */
@Composable
private fun LessonDialogHeader(
    lesson: ChineseLessonData,
    currentViewMode: LessonViewMode,
    onToggleViewMode: (LessonViewMode) -> Unit,
    onAskAi: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .drawBehind {
                drawLine(
                    color = PixelColors.BorderDark,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.5.dp.toPx()
                )
            }
            .padding(horizontal = 16.dp, vertical = 11.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ── Hàng 1: Tags chuyên mục & Level (Trái) | Nút đóng [✕] (Phải) ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PixelBadge(
                        text = lesson.category.ifBlank { "PHÁT ÂM PINYIN" }.uppercase(),
                        color = PixelColors.ArcadePurple,
                        textColor = Color.White
                    )

                    PixelBadge(
                        text = lesson.level.ifBlank { "Cơ bản" }.uppercase(),
                        color = PixelColors.ArcadeAmber,
                        textColor = PixelColors.BorderDark
                    )
                }

                // Nút đóng vuông Pixel
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(PixelColors.SurfaceDark)
                        .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                        .bounceClick(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            // ── Hàng 2: Pinyin Hanzi Title ──
            if (lesson.pinyinHanziTitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = lesson.pinyinHanziTitle,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PixelColors.ArcadePurple,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // ── Hàng 3: Tiêu đề bài học lớn ──
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = lesson.title.ifBlank { "4 Thanh điệu Pinyin & Quy tắc biến âm" },
                fontFamily = PlusJakartaSans,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.TextDark,
                lineHeight = 22.sp
            )

            // ── Hàng 4: 3 nút Arcade dập nổi (Toàn diện, Thẻ trượt, Hỏi Gia sư AI) ──
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeaderModePixelButton(
                    title = "Toàn diện",
                    iconRes = R.drawable.ic_layers,
                    isSelected = currentViewMode == LessonViewMode.COMPREHENSIVE,
                    onClick = { onToggleViewMode(LessonViewMode.COMPREHENSIVE) }
                )

                HeaderModePixelButton(
                    title = "Thẻ trượt",
                    iconRes = R.drawable.ic_slides,
                    isSelected = currentViewMode == LessonViewMode.SLIDE_CARDS,
                    onClick = { onToggleViewMode(LessonViewMode.SLIDE_CARDS) }
                )

                // Nút Hỏi Gia sư AI Arcade
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PixelColors.ShadowDark)
                        .padding(bottom = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFFFEF3C7))
                            .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(5.dp))
                            .bounceClick(onClick = onAskAi)
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "🤖", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Gia sư AI",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF92400E)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderModePixelButton(
    title: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(PixelColors.ShadowDark)
            .padding(bottom = if (isSelected) 1.dp else 2.dp)
    ) {
        Box(
            modifier = Modifier
                .height(30.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(if (isSelected) PixelColors.ArcadePurple else Color.White)
                .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(5.dp))
                .bounceClick(onClick = onClick)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = if (isSelected) Color.White else PixelColors.TextMuted,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSelected) Color.White else PixelColors.TextDark
                )
            }
        }
    }
}

/**
 * Thanh TabRow gồm 5 Tab dạng phím Arcade dập nổi cuộn ngang
 */
@Composable
private fun LessonTabsNavigationRow(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val tabs = remember {
        listOf(
            "📖 Bài giảng",
            "🔖 Từ vựng",
            "💬 Hội thoại",
            "✒️ Luyện viết",
            "⚡ Luyện tập"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC))
            .drawBehind {
                drawLine(
                    color = PixelColors.BorderDark,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.2.dp.toPx()
                )
            }
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PixelColors.ShadowDark)
                        .padding(bottom = if (isSelected) 1.dp else 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(7.dp))
                            .background(if (isSelected) PixelColors.ArcadePurple else Color.White)
                            .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(7.dp))
                            .clickable { onSelectTab(index) }
                            .padding(horizontal = 13.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabTitle,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) Color.White else PixelColors.TextDark
                        )
                    }
                }
            }
        }
    }
}

/**
 * Slide tương tác khi người dùng bật chế độ "Thẻ trượt"
 */
@Composable
private fun LessonSlideInteractiveBody(
    toneCardData: ToneCardData,
    action: LessonAction,
    onComplete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LessonProgressBar(
            currentIndex = toneCardData.currentIndex,
            totalCount = toneCardData.totalCount
        )

        Spacer(modifier = Modifier.height(12.dp))

        val scrollState = rememberScrollState()
        LaunchedEffect(toneCardData.slideIndex) {
            scrollState.scrollTo(0)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AnimatedContent(
                targetState = toneCardData,
                transitionSpec = {
                    val isForward = targetState.currentIndex >= initialState.currentIndex
                    if (isForward) {
                        (slideInHorizontally(animationSpec = tween(240, easing = FastOutSlowInEasing)) { width -> width / 6 } +
                         fadeIn(animationSpec = tween(220, easing = LinearOutSlowInEasing)))
                            .togetherWith(
                                slideOutHorizontally(animationSpec = tween(180, easing = FastOutSlowInEasing)) { width -> -width / 6 } +
                                fadeOut(animationSpec = tween(160, easing = FastOutLinearInEasing))
                            ).using(SizeTransform(clip = false))
                    } else {
                        (slideInHorizontally(animationSpec = tween(240, easing = FastOutSlowInEasing)) { width -> -width / 6 } +
                         fadeIn(animationSpec = tween(220, easing = LinearOutSlowInEasing)))
                            .togetherWith(
                                slideOutHorizontally(animationSpec = tween(180, easing = FastOutSlowInEasing)) { width -> width / 6 } +
                                fadeOut(animationSpec = tween(160, easing = FastOutLinearInEasing))
                            ).using(SizeTransform(clip = false))
                    }
                },
                label = "SlideTransition"
            ) { currentSlide ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    CategoryAndTopicRow(
                        category = currentSlide.category,
                        subTitle = currentSlide.subTitle
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TitleAndHeaderInfo(
                        title = currentSlide.title,
                        currentIndex = currentSlide.currentIndex,
                        totalCount = currentSlide.totalCount,
                        cardType = currentSlide.cardType
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    when (currentSlide.slideType) {
                        SlideType.INTERACTIVE -> {
                            InteractiveSlideBody(
                                currentSlide = currentSlide,
                                onPlayAudio = action.onPlayAudio
                            )
                        }
                        SlideType.QUIZ -> {
                            QuizSlideBody(
                                currentSlide = currentSlide
                            )
                        }
                        SlideType.TAKEAWAY -> {
                            TakeawaySlideBody(
                                currentSlide = currentSlide
                            )
                        }
                        SlideType.CONCEPT -> {
                            ConceptSlideBody(
                                currentSlide = currentSlide,
                                onPlayAudio = action.onPlayAudio
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        DialogBottomNavigation(
            isFirst = toneCardData.currentIndex <= 1,
            isLast = toneCardData.currentIndex >= toneCardData.totalCount,
            onPrev = action.onPrev,
            onNext = action.onNext,
            onComplete = onComplete
        )
    }
}