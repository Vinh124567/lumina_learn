package com.example.luminalearn.presentation.lesson.component

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LessonDetailDialog(
    action: LessonAction,
    toneCardData: ToneCardData,
    initialIsCompleted: Boolean = false,
    sparksReward: Int = 30
) {
    var isCompleted by remember(toneCardData.title) { mutableStateOf(initialIsCompleted) }

    Dialog(
        onDismissRequest = action.onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
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
                    LessonSlideContent(
                        toneCardData = toneCardData,
                        action = action,
                        onComplete = { isCompleted = true }
                    )
                }
            }
        }
    }
}

@Composable
private fun LessonSlideContent(
    toneCardData: ToneCardData,
    action: LessonAction,
    onComplete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 1. Cố định ở đỉnh: Header và Thanh tiến trình
        TopHeaderBar(
            onDismiss = action.onDismiss,
            onAskAi = action.onAskAi
        )

        Spacer(modifier = Modifier.height(12.dp))

        LessonProgressBar(
            currentIndex = toneCardData.currentIndex,
            totalCount = toneCardData.totalCount
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Nội dung slide trượt mượt mà
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

@Preview(showBackground = true)
@Composable
fun LessonDetailDialogPreview() {
    LessonDetailDialog(
        action = LessonAction(),
        toneCardData = ToneCardData(
            category = "PHÁT ÂM PINYIN",
            cardType = "Khái niệm",
            subTitle = "四声与变调",
            title = "4 Thanh điệu Pinyin & Quy tắc biến âm",
            pinyinVariants = listOf("mā", "má", "mǎ", "mà"),
            hanziVariants = listOf("妈", "麻", "马", "骂"),
            hanViet = "Ma (Mẹ) - Ma (Gai) - Mã (Ngựa) - Mạ (Mắng)",
            meaning = "Bốn ý nghĩa hoàn toàn khác nhau chỉ nhờ thay đổi thanh điệu!"
        )
    )
}