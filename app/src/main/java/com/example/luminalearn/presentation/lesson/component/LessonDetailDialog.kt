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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.luminalearn.R

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

enum class LessonViewMode {
    COMPREHENSIVE, // Chế độ "Toàn diện"
    SLIDE_CARDS    // Chế độ "Thẻ trượt"
}

/**
 * Màn hình học bài chi tiết (Dạng Bottom Sheet bo góc tròn, nằm đè lên toàn bộ app kể cả Bottom Navigation Bar).
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

    // Dữ liệu tổng hợp
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
                .background(Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White,
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                ) {
                    // ── Thanh kéo Drag handle bar ở đỉnh ──
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 38.dp, height = 4.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFCBD5E1))
                        )
                    }

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
                                // ── 1. Header chung cố định ở đỉnh ──
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
                                    // Thanh 5 Tab Navigation
                                    LessonTabsNavigationRow(
                                        selectedTab = selectedTab,
                                        onSelectTab = { selectedTab = it }
                                    )

                                    // Nội dung cuộn của tab được chọn
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
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
                                            3 -> LessonWritingTabContent()
                                            4 -> LessonQuizTabContent()
                                        }
                                    }
                                } else {
                                    // Chế độ Thẻ trượt (Giữ nguyên luồng slide tương tác cũ)
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

/**
 * Header chính của màn hình học bài (Chuẩn thiết kế gốc)
 * Hàng 1: [Icon tím] [Tag chuyên mục] [Tag Level] ------------- [✕]
 * Hàng 2: [Pinyin Hanzi Subtitle]
 * Hàng 3: [Tiêu đề bài học lớn]
 * Hàng 4: [⊞ Toàn diện] [🂠 Thẻ trượt] [💬 Hỏi Gia sư AI]
 */
@Composable
private fun LessonDialogHeader(
    lesson: ChineseLessonData,
    currentViewMode: LessonViewMode,
    onToggleViewMode: (LessonViewMode) -> Unit,
    onAskAi: () -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
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
                    // Badge chuyên mục (nền tím nhạt, viền tím, chữ tím)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEEF2FF))
                            .border(0.8.dp, Color(0xFFC7D2FE), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = lesson.category.ifBlank { "PHÁT ÂM PINYIN" },
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4F46E5)
                        )
                    }

                    // Badge Level (nền vàng cam nhạt, chữ nâu cam)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = lesson.level.ifBlank { "Cơ bản" },
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                    }
                }

                // Nút đóng tròn xoe ở góc trên phải
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Đóng",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // ── Hàng 2: Pinyin Hanzi Title (nhỏ hơn, màu tím xám) ──
            if (lesson.pinyinHanziTitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lesson.pinyinHanziTitle,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6366F1),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // ── Hàng 3: Tiêu đề bài học lớn trang trọng ──
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lesson.title.ifBlank { "4 Thanh điệu Pinyin & Quy tắc biến âm" },
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                lineHeight = 23.sp
            )

            // ── Hàng 4: Hàng 3 nút Pills (Toàn diện, Thẻ trượt, Hỏi Gia sư AI) ──
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Nút Toàn diện
                HeaderModePill(
                    title = "Toàn diện",
                    iconRes = R.drawable.ic_layers,
                    isSelected = currentViewMode == LessonViewMode.COMPREHENSIVE,
                    onClick = { onToggleViewMode(LessonViewMode.COMPREHENSIVE) }
                )

                // Nút Thẻ trượt
                HeaderModePill(
                    title = "Thẻ trượt",
                    iconRes = R.drawable.ic_slides,
                    isSelected = currentViewMode == LessonViewMode.SLIDE_CARDS,
                    onClick = { onToggleViewMode(LessonViewMode.SLIDE_CARDS) }
                )

                // Nút Hỏi Gia sư AI
                Surface(
                    modifier = Modifier
                        .height(32.dp)
                        .clickable(onClick = onAskAi),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFEEF2FF),
                    border = BorderStroke(1.dp, Color(0xFFC7D2FE))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 11.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_ai_chat),
                            contentDescription = null,
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Hỏi Gia sư AI",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4F46E5)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderModePill(
    title: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(32.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFFEEF2FF) else Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color(0xFFC7D2FE) else Color(0xFFE2E8F0)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = if (isSelected) Color(0xFF4F46E5) else Color(0xFF64748B),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = title,
                fontSize = 11.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color(0xFF4F46E5) else Color(0xFF64748B)
            )
        }
    }
}

/**
 * Thanh TabRow gồm 5 Tab đúng như ảnh thiết kế:
 * 1. 📖 Bài giảng & Ngữ pháp
 * 2. 🔖 Từ vựng cốt lõi (6)
 * 3. 💬 Đoạn hội thoại mẫu
 * 4. ✒️ Luyện viết Hán tự (米字格)
 * 5. ⚡ Luyện tập & Phản xạ (0/3)
 */
@Composable
private fun LessonTabsNavigationRow(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val tabs = remember {
        listOf(
            "📖 Bài giảng & Ngữ pháp",
            "🔖 Từ vựng cốt lõi (6)",
            "💬 Đoạn hội thoại mẫu",
            "✒️ Luyện viết Hán tự (米字格)",
            "⚡ Luyện tập & Phản xạ (0/3)"
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                val isSelected = selectedTab == index
                Column(
                    modifier = Modifier
                        .clickable { onSelectTab(index) }
                        .padding(top = 10.dp, bottom = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = tabTitle,
                        fontSize = 12.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color(0xFF5C50F6) else Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .height(2.5.dp)
                            .width(80.dp)
                            .background(if (isSelected) Color(0xFF5C50F6) else Color.Transparent)
                    )
                }
            }
        }
    }
}



/**
 * Slide tương tác cũ khi người dùng bật chế độ "Thẻ trượt"
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

        Spacer(modifier = Modifier.height(14.dp))

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

                    Spacer(modifier = Modifier.height(12.dp))

                    TitleAndHeaderInfo(
                        title = currentSlide.title,
                        currentIndex = currentSlide.currentIndex,
                        totalCount = currentSlide.totalCount,
                        cardType = currentSlide.cardType
                    )

                    Spacer(modifier = Modifier.height(14.dp))

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

                    Spacer(modifier = Modifier.height(24.dp))
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