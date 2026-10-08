package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.graphics.TransformOrigin
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
    nextWord: VocabWordItem? = null,
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
            nextWord = nextWord,
            isFlipped = isFlipped,
            actions = actions,
            remainingCards = (totalCount - 1 - currentIndex).coerceAtLeast(0)
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

        val cleanScore = if (targetScore.contains("(")) {
            targetScore.substringBefore(" (")
        } else {
            targetScore
        }
        if (cleanScore.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFEEF2FF),
                border = BorderStroke(1.dp, Color(0xFFC7D2FE))
            ) {
                Text(
                    text = cleanScore,
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F46E5),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
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
    nextWord: VocabWordItem? = null,
    isFlipped: Boolean,
    actions: FlashcardActions,
    remainingCards: Int = 0
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

    val dragProgress = (kotlin.math.abs(dragOffsetX.value) / 320f).coerceIn(0f, 1f)

    val startPadding = when {
        remainingCards >= 2 -> 18.dp
        remainingCards >= 1 -> 10.dp
        else -> 0.dp
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = startPadding, end = if (remainingCards > 0) 4.dp else 0.dp),
        contentAlignment = Alignment.Center
    ) {
        // ── THẺ THỨ 3 (DƯỚI CÙNG - XÒE RỘNG NHẤT Ở PHẦN TRÊN, CHỤM SÁT Ở ĐÁY) ──
        if (remainingCards >= 2) {
            val layer3Rotation = -7.0f + (3.5f * dragProgress)
            val layer3ScaleX = 0.97f + (0.015f * dragProgress)
            val layer3ScaleY = 0.98f + (0.01f * dragProgress)
            val layer3Alpha = 0.85f + (0.12f * dragProgress)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = BorderStroke(1.dp, Color(0xFFC7D2FE).copy(alpha = 0.6f)),
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0.5f, 1f)
                        rotationZ = layer3Rotation
                        scaleX = layer3ScaleX
                        scaleY = layer3ScaleY
                        alpha = layer3Alpha
                    }
            ) {
                Box(modifier = Modifier.fillMaxSize())
            }
        }

        // ── THẺ THỨ 2 (Ở GIỮA - XÒE VỪA PHẢI Ở TRÊN, CHỤM SÁT Ở ĐÁY, HIỂN THỊ TỪ TIẾP THEO) ──
        if (remainingCards >= 1 && nextWord != null) {
            val layer2Rotation = -3.5f + (3.5f * dragProgress)
            val layer2ScaleX = 0.985f + (0.015f * dragProgress)
            val layer2ScaleY = 0.99f + (0.01f * dragProgress)
            val layer2Alpha = 0.94f + (0.06f * dragProgress)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF5538EE).copy(alpha = 0.35f),
                            Color(0xFF818CF8).copy(alpha = 0.35f)
                        )
                    )
                ),
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0.5f, 1f)
                        rotationZ = layer2Rotation
                        scaleX = layer2ScaleX
                        scaleY = layer2ScaleY
                        alpha = layer2Alpha
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FlashcardCleanHeader(
                        word = nextWord,
                        onSpeak = { actions.onSpeak(nextWord.hanzi) },
                        onToggleMastered = { actions.onToggleMastered(nextWord.id) }
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    FlashcardCleanFront(word = nextWord)

                    Spacer(modifier = Modifier.height(26.dp))

                    Text(
                        text = "↻ Chạm để xem đáp án",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        // ── THẺ CHÍNH (TRÊN CÙNG - TRẮNG SÁNG TINH KHÔI, VIỀN GRADIENT TÍM THƯƠNG HIỆU NỔI BẬT) ──
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                    listOf(
                        Color(0xFF5538EE),
                        Color(0xFF818CF8)
                    )
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0.5f, 1f)
                    translationX = dragOffsetX.value
                    translationY = dragOffsetY.value
                    rotationZ = (dragOffsetX.value / 35f).coerceIn(-12f, 12f)
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
                            totalDragY += dragAmount.y * 0.15f
                            coroutineScope.launch {
                                dragOffsetX.snapTo(totalDragX)
                                dragOffsetY.snapTo(totalDragY)
                            }
                        },
                        onDragEnd = {
                            val swipeThresholdPx = with(density) { 95.dp.toPx() }
                            val tapThresholdPx = with(density) { 15.dp.toPx() }
                            val absX = kotlin.math.abs(totalDragX)
                            val absY = kotlin.math.abs(totalDragY)

                            if (absX < tapThresholdPx && absY < tapThresholdPx) {
                                actions.onFlip()
                                coroutineScope.launch {
                                    dragOffsetX.snapTo(0f)
                                    dragOffsetY.snapTo(0f)
                                }
                            } else if (absX >= swipeThresholdPx) {
                                val targetX = if (totalDragX < 0) -1100f else 1100f
                                coroutineScope.launch {
                                    dragOffsetX.animateTo(
                                        targetValue = targetX,
                                        animationSpec = tween(180, easing = FastOutLinearInEasing)
                                    )
                                    if (totalDragX < 0) {
                                        actions.onNext()
                                    } else {
                                        actions.onPrevious()
                                    }
                                    dragOffsetX.snapTo(0f)
                                    dragOffsetY.snapTo(0f)
                                }
                            } else {
                                coroutineScope.launch {
                                    dragOffsetX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                            stiffness = Spring.StiffnessMediumLow
                                        )
                                    )
                                }
                                coroutineScope.launch {
                                    dragOffsetY.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                            stiffness = Spring.StiffnessMediumLow
                                        )
                                    )
                                }
                            }
                        },
                        onDragCancel = {
                            coroutineScope.launch {
                                dragOffsetX.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessMediumLow
                                    )
                                )
                            }
                            coroutineScope.launch {
                                dragOffsetY.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessMediumLow
                                    )
                                )
                            }
                        }
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header gọn gàng
                FlashcardCleanHeader(
                    word = word,
                    onSpeak = { actions.onSpeak(word.hanzi) },
                    onToggleMastered = { actions.onToggleMastered(word.id) }
                )

                Spacer(modifier = Modifier.height(26.dp))

                if (!isFlipped) {
                    FlashcardCleanFront(word = word)
                } else {
                    FlashcardCleanBack(
                        word = word,
                        onSpeak = actions.onSpeak
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                // Bottom hint
                Text(
                    text = if (isFlipped) "↺ Chạm để xem mặt trước" else "↻ Chạm để xem đáp án",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

@Composable
private fun FlashcardCleanHeader(
    word: VocabWordItem,
    onSpeak: () -> Unit,
    onToggleMastered: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Tag HSK & Topic bên trái
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF1F5F9),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF5538EE))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${word.hskLevel} • ${word.topic}",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // 2 nút Loa & Thuộc từ bên phải
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Nút Loa
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEF2FF))
                    .bounceClick(scaleDown = 0.88f, onClick = onSpeak),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = "Phát âm",
                    tint = Color(0xFF5538EE),
                    modifier = Modifier.size(16.dp)
                )
            }

            // Nút Thuộc từ
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (word.isMastered) Color(0xFF10B981) else Color(0xFFF1F5F9))
                    .border(
                        BorderStroke(1.dp, if (word.isMastered) Color(0xFF059669) else Color(0xFFE2E8F0)),
                        CircleShape
                    )
                    .bounceClick(scaleDown = 0.88f, onClick = onToggleMastered),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "Trạng thái thuộc",
                    tint = if (word.isMastered) Color.White else Color(0xFF94A3B8),
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Composable
private fun FlashcardCleanFront(word: VocabWordItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Chữ Hán to, thanh lịch
        Text(
            text = word.hanzi,
            fontFamily = PlusJakartaSans,
            fontSize = if (word.hanzi.length <= 2) 58.sp else 44.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Pinyin • Hán-Việt
        val pinyinHanViet = buildString {
            if (word.pinyin.isNotBlank()) append(word.pinyin)
            if (word.pinyin.isNotBlank() && word.hanViet.isNotBlank()) append(" • ")
            if (word.hanViet.isNotBlank()) append(word.hanViet)
        }
        if (pinyinHanViet.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Text(
                    text = pinyinHanViet,
                    fontFamily = PlusJakartaSans,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF5538EE),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }

        if (word.radical.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            val radicalText = if (word.radical.startsWith("Bộ")) word.radical else "Bộ thủ: ${word.radical}"
            Text(
                text = radicalText,
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FlashcardCleanBack(
    word: VocabWordItem,
    onSpeak: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Nghĩa tiếng Việt
        Text(
            text = word.meaning,
            fontFamily = PlusJakartaSans,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )

        if (word.partOfSpeech.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "(${word.partOfSpeech})",
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )
        }

        if (word.exampleHanzi.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = word.exampleHanzi,
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        if (word.examplePinyin.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = word.examplePinyin,
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = Color(0xFF5538EE)
                            )
                        }
                        if (word.exampleMeaning.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = word.exampleMeaning,
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEF2FF))
                            .bounceClick(scaleDown = 0.88f) { onSpeak(word.exampleHanzi) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = "Nghe ví dụ",
                            tint = Color(0xFF5538EE),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
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
