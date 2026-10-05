package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.CosmicStarfield
import com.example.luminalearn.core.ui.effect.animatedMidnightGradient
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.model.SrsRating
import com.example.luminalearn.presentation.vocabulary.model.SrsScheduler
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.launch
import kotlin.math.hypot

data class FlashcardActions(
    val onFlip: () -> Unit,
    val onPrevious: () -> Unit,
    val onNext: () -> Unit,
    val onSpeak: (String) -> Unit,
    val onSpeakSlow: (String) -> Unit = {},
    val onToggleMastered: (String) -> Unit,
    val onOpenDetail: (VocabWordItem) -> Unit = {},
    val onVoiceTest: (VocabWordItem) -> Unit = {},
    val onRateSrs: (SrsRating) -> Unit = {}
)

@Composable
fun FlashcardInteractiveSection(
    currentWord: VocabWordItem,
    currentIndex: Int,
    totalCount: Int,
    isFlipped: Boolean,
    actions: FlashcardActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlashcardIndexBar(
            currentIndex = currentIndex,
            totalCount = totalCount,
            targetScore = currentWord.targetScore
        )

        Spacer(modifier = Modifier.height(10.dp))

        FlashcardCardSurface(
            word = currentWord,
            isFlipped = isFlipped,
            actions = actions
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Chuẩn Active Recall: Chỉ hiển thị khối đánh giá SRS khi thẻ ĐÃ LẬT sang mặt sau
        if (isFlipped) {
            SrsRatingCard(
                word = currentWord,
                onRate = actions.onRateSrs
            )
        } else {
            // Khi chưa lật: nút bấm lật thẻ chuẩn phong cách ứng dụng
            Surface(
                onClick = actions.onFlip,
                shape = RoundedCornerShape(50),
                color = Color(0xFF5538EE),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_flashcard),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lật xem đáp án",
                        fontFamily = PlusJakartaSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        FlashcardNavigationButtons(
            isFirst = currentIndex == 0,
            onPrevious = actions.onPrevious,
            onNext = actions.onNext
        )
    }
}

@Composable
private fun FlashcardIndexBar(
    currentIndex: Int,
    totalCount: Int,
    targetScore: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = buildAnnotatedString {
                append("Thẻ ")
                withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))) {
                    append("${currentIndex + 1}")
                }
                append(" / $totalCount")
            },
            fontFamily = PlusJakartaSans,
            fontSize = 13.sp,
            color = Color(0xFF64748B)
        )

        Text(
            text = targetScore,
            fontFamily = PlusJakartaSans,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4F46E5)
        )
    }
}

/**
 * Thẻ Flashcard chính theo phong cách Midnight Cosmic Hero đồng bộ với HeroLessonCard và VocabHeroBanner.
 * Hỗ trợ cử chỉ vuốt tự do đa hướng (2D Drag Gesture):
 * - Vuốt Trái: Thẻ tiếp theo
 * - Vuốt Phải: Thẻ phía trước
 * - Vuốt Lên / Xuống / Chạm: Lật thẻ
 */
@Composable
private fun FlashcardCardSurface(
    word: VocabWordItem,
    isFlipped: Boolean,
    actions: FlashcardActions
) {
    val dragOffsetX = remember { Animatable(0f) }
    val dragOffsetY = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    var totalDragX by remember { mutableFloatStateOf(0f) }
    var totalDragY by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(word.id) {
        dragOffsetX.snapTo(0f)
        dragOffsetY.snapTo(0f)
        totalDragX = 0f
        totalDragY = 0f
    }

    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(
            1.5.dp,
            Brush.linearGradient(
                listOf(
                    Color(0xFF818CF8).copy(alpha = 0.65f),
                    Color(0xFF5538EE).copy(alpha = 0.40f),
                    Color(0xFFC7D2FE).copy(alpha = 0.50f)
                )
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationX = dragOffsetX.value
                translationY = dragOffsetY.value
                rotationZ = (dragOffsetX.value / 60f).coerceIn(-14f, 14f)
                val distance = hypot(dragOffsetX.value, dragOffsetY.value)
                val scale = (1f - (distance / 3500f)).coerceIn(0.93f, 1f)
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(word.id) {
                detectDragGestures(
                    onDragStart = {
                        totalDragX = 0f
                        totalDragY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        totalDragX += dragAmount.x
                        totalDragY += dragAmount.y
                        coroutineScope.launch {
                            dragOffsetX.snapTo(totalDragX)
                            dragOffsetY.snapTo(totalDragY)
                        }
                    },
                    onDragEnd = {
                        val thresholdPx = with(density) { 55.dp.toPx() }
                        val tapThresholdPx = with(density) { 15.dp.toPx() }
                        val absX = kotlin.math.abs(totalDragX)
                        val absY = kotlin.math.abs(totalDragY)

                        if (absX < tapThresholdPx && absY < tapThresholdPx) {
                            // Chạm nhẹ -> Lật thẻ
                            actions.onFlip()
                            coroutineScope.launch {
                                dragOffsetX.snapTo(0f)
                                dragOffsetY.snapTo(0f)
                            }
                        } else if (absX > absY && absX > thresholdPx) {
                            // Vuốt ngang
                            if (totalDragX < 0) {
                                // Sang trái -> Thẻ tiếp theo
                                coroutineScope.launch {
                                    dragOffsetX.animateTo(-1200f, tween(130))
                                    actions.onNext()
                                    dragOffsetX.snapTo(0f)
                                    dragOffsetY.snapTo(0f)
                                }
                            } else {
                                // Sang phải -> Thẻ trước
                                coroutineScope.launch {
                                    dragOffsetX.animateTo(1200f, tween(130))
                                    actions.onPrevious()
                                    dragOffsetX.snapTo(0f)
                                    dragOffsetY.snapTo(0f)
                                }
                            }
                        } else if (absY >= absX && absY > thresholdPx) {
                            // Vuốt dọc (Lên hoặc Xuống) -> Lật thẻ
                            actions.onFlip()
                            coroutineScope.launch {
                                launch {
                                    dragOffsetX.animateTo(
                                        0f,
                                        spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                                launch {
                                    dragOffsetY.animateTo(
                                        0f,
                                        spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                            }
                        } else {
                            // Chưa tới ngưỡng -> Đàn hồi về tâm
                            coroutineScope.launch {
                                launch {
                                    dragOffsetX.animateTo(
                                        0f,
                                        spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                                launch {
                                    dragOffsetY.animateTo(
                                        0f,
                                        spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                            }
                        }
                    },
                    onDragCancel = {
                        coroutineScope.launch {
                            dragOffsetX.animateTo(0f)
                            dragOffsetY.animateTo(0f)
                        }
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animatedMidnightGradient(
                    colors = listOf(
                        Color(0xFF161338),
                        Color(0xFF221C52),
                        Color(0xFF2B2068),
                        Color(0xFF1C1646),
                        Color(0xFF161338)
                    ),
                    durationMillis = 8000
                )
        ) {
            CosmicStarfield(
                modifier = Modifier.matchParentSize(),
                particleCount = 26,
                focusCenterXRatio = 0.5f,
                focusCenterYRatio = 0.4f
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlashcardHeader(
                    hskLevel = word.hskLevel,
                    topic = word.topic,
                    isDueToday = word.isDueToday,
                    isFlipped = isFlipped
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (!isFlipped) {
                    FlashcardFront(
                        word = word,
                        onSpeak = actions.onSpeak,
                        onVoiceTest = { actions.onVoiceTest(word) }
                    )
                } else {
                    FlashcardBack(
                        word = word,
                        onSpeak = actions.onSpeak,
                        onVoiceTest = { actions.onVoiceTest(word) }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.12f))
                )

                Spacer(modifier = Modifier.height(12.dp))

                FlashcardActionFooter(
                    word = word,
                    onSpeak = actions.onSpeak,
                    onSpeakSlow = actions.onSpeakSlow,
                    onToggleMastered = actions.onToggleMastered,
                    onOpenDetail = { actions.onOpenDetail(word) },
                    onVoiceTest = { actions.onVoiceTest(word) }
                )
            }
        }
    }
}

@Composable
private fun FlashcardHeader(
    hskLevel: String,
    topic: String,
    isDueToday: Boolean,
    isFlipped: Boolean
) {
    val flipHint = if (isFlipped) "Vuốt 4 hướng ⊹ Lật lại ↺" else "Vuốt 4 hướng ⊹ Chạm lật ↻"

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Indigo Quartz Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF818CF8).copy(alpha = 0.15f))
                    .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.4f)), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF818CF8), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$hskLevel • $topic",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC7D2FE),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isDueToday) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF59E0B).copy(alpha = 0.20f))
                        .border(BorderStroke(1.dp, Color(0xFFFDE68A).copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            tint = Color(0xFFFDE68A),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "⚡ Cần ôn",
                            fontFamily = PlusJakartaSans,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFDE68A),
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.10f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = flipHint,
                fontFamily = PlusJakartaSans,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE2E8F0),
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

@Composable
private fun FlashcardFront(
    word: VocabWordItem,
    onSpeak: (String) -> Unit,
    onVoiceTest: () -> Unit = {}
) {
    val hanziFontSize = remember(word.hanzi) {
        when {
            word.hanzi.length <= 1 -> 56.sp
            word.hanzi.length == 2 -> 46.sp
            word.hanzi.length == 3 -> 36.sp
            else -> 30.sp
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = word.hanzi,
                fontFamily = PlusJakartaSans,
                fontSize = hanziFontSize,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1,
                softWrap = false
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.45f)), CircleShape)
                    .bounceClick(scaleDown = 0.88f) { onSpeak(word.hanzi) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = "Phát âm",
                    tint = Color(0xFFC7D2FE),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.45f)), CircleShape)
                    .bounceClick(scaleDown = 0.88f, onClick = onVoiceTest),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mic),
                    contentDescription = "Thử giọng",
                    tint = Color(0xFFC7D2FE),
                    modifier = Modifier.size(17.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (word.hanViet.isNotBlank()) {
            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF59E0B).copy(alpha = 0.18f))
                    .border(BorderStroke(1.dp, Color(0xFFFDE68A).copy(alpha = 0.45f)), RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "Hán-Việt: ${word.hanViet}",
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFDE68A)
                )
            }
        }

        if (word.radical.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            val radicalText = if (word.radical.startsWith("Bộ")) word.radical else "Bộ thủ: ${word.radical}"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(Color(0xFF818CF8), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = radicalText,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFA5B4FC),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun FlashcardBack(
    word: VocabWordItem,
    onSpeak: (String) -> Unit,
    onVoiceTest: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = word.pinyin,
                fontFamily = PlusJakartaSans,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFC7D2FE),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.45f)), CircleShape)
                    .bounceClick(scaleDown = 0.88f) { onSpeak(word.hanzi) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = "Phát âm",
                    tint = Color(0xFFC7D2FE),
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.45f)), CircleShape)
                    .bounceClick(scaleDown = 0.88f, onClick = onVoiceTest),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mic),
                    contentDescription = "Thử giọng",
                    tint = Color(0xFFC7D2FE),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = word.meaning,
            fontFamily = PlusJakartaSans,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "(${word.partOfSpeech})",
            fontFamily = PlusJakartaSans,
            fontSize = 12.sp,
            color = Color(0xFF94A3B8),
            textAlign = TextAlign.Center
        )

        if (word.exampleHanzi.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.35f)), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = word.exampleHanzi,
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAFC)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = word.examplePinyin,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = Color(0xFFA5B4FC)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = word.exampleMeaning,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                            .bounceClick(scaleDown = 0.88f) { onSpeak(word.exampleHanzi) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = "Nghe ví dụ",
                            tint = Color(0xFFC7D2FE),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FlashcardActionFooter(
    word: VocabWordItem,
    onSpeak: (String) -> Unit,
    onSpeakSlow: (String) -> Unit,
    onToggleMastered: (String) -> Unit,
    onOpenDetail: () -> Unit,
    onVoiceTest: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionChip(
                iconRes = R.drawable.ic_speaker,
                label = "Nghe",
                modifier = Modifier.weight(1f),
                onClick = { onSpeak(word.hanzi) }
            )
            ActionChip(
                iconRes = R.drawable.ic_mic,
                label = "Thử giọng",
                modifier = Modifier.weight(1.2f),
                onClick = onVoiceTest
            )
            ActionChip(
                iconRes = R.drawable.ic_book,
                label = "Chi tiết",
                modifier = Modifier.weight(1.1f),
                onClick = onOpenDetail
            )
            ActionChip(
                iconRes = R.drawable.ic_clock,
                label = "Chậm",
                modifier = Modifier.weight(0.9f),
                onClick = { onSpeakSlow(word.hanzi) }
            )
        }

        MasteredButton(
            isMastered = word.isMastered,
            onClick = { onToggleMastered(word.id) }
        )
    }
}

@Composable
private fun ActionChip(
    @androidx.annotation.DrawableRes iconRes: Int,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.35f)), RoundedCornerShape(20.dp))
            .bounceClick(scaleDown = 0.92f, onClick = onClick)
            .padding(vertical = 6.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color(0xFFC7D2FE),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = label,
                fontFamily = PlusJakartaSans,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC7D2FE),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun MasteredButton(
    isMastered: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isMastered) Color(0xFF10B981).copy(alpha = 0.22f) else Color.White.copy(alpha = 0.08f)
    val contentColor = if (isMastered) Color(0xFF34D399) else Color(0xFFC7D2FE)
    val borderColor = if (isMastered) Color(0xFF34D399).copy(alpha = 0.5f) else Color(0xFF818CF8).copy(alpha = 0.35f)
    val label = if (isMastered) "✓ Đã thuộc từ này" else "✓ Đánh dấu đã thuộc"

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(20.dp))
            .bounceClick(scaleDown = 0.95f, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontFamily = PlusJakartaSans,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            maxLines = 1
        )
    }
}

@Composable
private fun SrsRatingCard(
    word: VocabWordItem,
    onRate: (SrsRating) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.2.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = null,
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Mức độ ghi nhớ (SRS):",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                }

                Text(
                    text = "Tự động tính chu kỳ ôn",
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    softWrap = false
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SrsRatingButton(
                    title = "Quên",
                    subtitle = SrsScheduler.getRatingSubtitle(word.srsState, SrsRating.AGAIN),
                    bgColor = Color(0xFFFFF1F2),
                    borderColor = Color(0xFFFECDD3),
                    contentColor = Color(0xFFE11D48),
                    modifier = Modifier.weight(1f),
                    onClick = { onRate(SrsRating.AGAIN) }
                )
                SrsRatingButton(
                    title = "Khó",
                    subtitle = SrsScheduler.getRatingSubtitle(word.srsState, SrsRating.HARD),
                    bgColor = Color(0xFFFFFBEB),
                    borderColor = Color(0xFFFDE68A),
                    contentColor = Color(0xFFD97706),
                    modifier = Modifier.weight(1f),
                    onClick = { onRate(SrsRating.HARD) }
                )
                SrsRatingButton(
                    title = "Tốt",
                    subtitle = SrsScheduler.getRatingSubtitle(word.srsState, SrsRating.GOOD),
                    bgColor = Color(0xFFEFF6FF),
                    borderColor = Color(0xFFBFDBFE),
                    contentColor = Color(0xFF2563EB),
                    modifier = Modifier.weight(1f),
                    onClick = { onRate(SrsRating.GOOD) }
                )
                SrsRatingButton(
                    title = "Dễ",
                    subtitle = SrsScheduler.getRatingSubtitle(word.srsState, SrsRating.EASY),
                    bgColor = Color(0xFFECFDF5),
                    borderColor = Color(0xFFA7F3D0),
                    contentColor = Color(0xFF059669),
                    modifier = Modifier.weight(1f),
                    onClick = { onRate(SrsRating.EASY) }
                )
            }
        }
    }
}

@Composable
private fun SrsRatingButton(
    title: String,
    subtitle: String,
    bgColor: Color,
    borderColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(12.dp))
            .bounceClick(scaleDown = 0.92f, onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontFamily = PlusJakartaSans,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun FlashcardNavigationButtons(
    isFirst: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Thẻ trước (Chuẩn thiết kế LessonDialogNavigation)
        Surface(
            onClick = onPrevious,
            enabled = !isFirst,
            shape = RoundedCornerShape(50),
            color = if (isFirst) Color(0xFFF8FAFC) else Color.White,
            border = BorderStroke(1.dp, if (isFirst) Color(0xFFF1F5F9) else Color(0xFFE2E8F0)),
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Thẻ trước",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = if (isFirst) Color(0xFF94A3B8) else Color(0xFF64748B)
                )
            }
        }

        // Nút Thẻ tiếp theo (Chuẩn thiết kế LessonDialogNavigation)
        Surface(
            onClick = onNext,
            shape = RoundedCornerShape(50),
            color = Color(0xFF5538EE),
            modifier = Modifier
                .weight(1.3f)
                .height(46.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Thẻ tiếp theo",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = Color.White
                )
            }
        }
    }
}
