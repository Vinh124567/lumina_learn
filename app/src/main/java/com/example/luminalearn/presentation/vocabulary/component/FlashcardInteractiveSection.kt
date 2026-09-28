package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
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
import com.example.luminalearn.presentation.vocabulary.model.SrsRating
import com.example.luminalearn.presentation.vocabulary.model.SrsScheduler
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabShapes
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import kotlinx.coroutines.launch

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

        Spacer(modifier = Modifier.height(6.dp))

        FlashcardCardSurface(
            word = currentWord,
            isFlipped = isFlipped,
            actions = actions
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Khối đánh giá SRS luôn hiển thị dưới thẻ (như thiết kế chuẩn Spaced Repetition)
        SrsRatingCard(
            word = currentWord,
            onRate = actions.onRateSrs
        )

        Spacer(modifier = Modifier.height(14.dp))

        FlashcardNavigationButtons(
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
                withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, color = VocabColors.TextDark)) {
                    append("${currentIndex + 1}")
                }
                append(" / $totalCount")
            },
            fontSize = 13.sp,
            color = VocabColors.TextMuted
        )

        Text(
            text = targetScore,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = VocabColors.BrandDark
        )
    }
}

@Composable
private fun FlashcardCardSurface(
    word: VocabWordItem,
    isFlipped: Boolean,
    actions: FlashcardActions
) {
    val interactionSource = remember { MutableInteractionSource() }
    var pressOffset by remember { mutableStateOf(Offset.Zero) }

    val rippleRadius = remember { Animatable(0f) }
    val rippleAlpha = remember { Animatable(0f) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    pressOffset = interaction.pressPosition
                    launch {
                        rippleAlpha.snapTo(0.12f)
                        rippleRadius.snapTo(0f)
                        rippleRadius.animateTo(
                            targetValue = 1200f,
                            animationSpec = tween(
                                durationMillis = 350,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }
                is PressInteraction.Release,
                is PressInteraction.Cancel -> {
                    launch {
                        rippleAlpha.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = LinearEasing
                            )
                        )
                        rippleRadius.snapTo(0f)
                    }
                }
            }
        }
    }

    androidx.compose.material3.Card(
        shape = VocabShapes.BigCard,
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(VocabShapes.BigCard)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = actions.onFlip
            )
            .drawWithContent {
                drawContent()
                if (rippleAlpha.value > 0f) {
                    val cornerRadiusPx = 24.dp.toPx()
                    clipPath(
                        Path().apply {
                            addRoundRect(
                                RoundRect(
                                    rect = Rect(Offset.Zero, size),
                                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                                )
                            )
                        }
                    ) {
                        drawCircle(
                            color = VocabColors.BrandPrimary.copy(alpha = rippleAlpha.value),
                            radius = rippleRadius.value,
                            center = pressOffset
                        )
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlashcardHeader(
                hskLevel = word.hskLevel,
                topic = word.topic,
                isDueToday = word.isDueToday,
                isFlipped = isFlipped
            )

            Spacer(modifier = Modifier.height(14.dp))

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

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFF1F5F9))
            )

            Spacer(modifier = Modifier.height(10.dp))

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

@Composable
private fun FlashcardHeader(
    hskLevel: String,
    topic: String,
    isDueToday: Boolean,
    isFlipped: Boolean
) {
    val flipHint = if (isFlipped) "Chạm để lật lại ↺" else "Chạm để lật thẻ ↻"

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .background(Color(0xFFEEF2FF), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$hskLevel • $topic",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.BrandDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isDueToday) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEF3C7))
                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Cần ôn hôm nay",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309),
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = flipHint,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF94A3B8),
            maxLines = 1,
            softWrap = false
        )
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
            word.hanzi.length <= 1 -> 48.sp
            word.hanzi.length == 2 -> 40.sp
            word.hanzi.length == 3 -> 32.sp
            else -> 26.sp
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = word.hanzi,
            fontSize = hanziFontSize,
            fontWeight = FontWeight.Bold,
            color = VocabColors.TextDark,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.clickable { onSpeak(word.hanzi) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_speaker),
            contentDescription = "Phát âm",
            tint = VocabColors.BrandPrimary,
            modifier = Modifier
                .size(24.dp)
                .clickable { onSpeak(word.hanzi) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_mic),
            contentDescription = "Thử giọng nhanh",
            tint = VocabColors.BrandPrimary,
            modifier = Modifier
                .size(22.dp)
                .clickable(onClick = onVoiceTest)
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Box(
        modifier = Modifier
            .background(VocabColors.HanVietBg, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 4.dp)
    ) {
        Text(
            text = "Hán Việt: ${word.hanViet}",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = VocabColors.HanVietAmber
        )
    }

    if (word.radical.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = word.radical,
            fontSize = 11.5.sp,
            color = VocabColors.TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FlashcardBack(
    word: VocabWordItem,
    onSpeak: (String) -> Unit,
    onVoiceTest: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = word.pinyin,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = VocabColors.BrandPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable { onSpeak(word.hanzi) }
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_speaker),
            contentDescription = "Phát âm",
            tint = VocabColors.BrandPrimary,
            modifier = Modifier
                .size(18.dp)
                .clickable { onSpeak(word.hanzi) }
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_mic),
            contentDescription = "Thử giọng nhanh",
            tint = VocabColors.BrandPrimary,
            modifier = Modifier
                .size(18.dp)
                .clickable(onClick = onVoiceTest)
        )
    }

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = word.meaning,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A),
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = "(${word.partOfSpeech})",
        fontSize = 12.sp,
        color = VocabColors.TextMuted,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(12.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
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
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.TextDark
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = word.examplePinyin,
                    fontSize = 11.5.sp,
                    color = VocabColors.BrandPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = word.exampleMeaning,
                    fontSize = 11.5.sp,
                    color = VocabColors.TextMuted
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_speaker),
                contentDescription = "Nghe ví dụ",
                tint = Color(0xFF6366F1),
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onSpeak(word.exampleHanzi) }
            )
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
            .background(Color.White)
            .border(1.2.dp, VocabColors.BrandPrimary, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
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
                tint = VocabColors.BrandPrimary,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VocabColors.BrandPrimary,
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
    val bgColor = if (isMastered) VocabColors.BrandLight else Color(0xFFF8FAFC)
    val contentColor = if (isMastered) VocabColors.BrandPrimary else Color(0xFF475569)
    val borderColor = if (isMastered) Color(0xFFBFDBFE) else Color(0xFFCBD5E1)
    val label = if (isMastered) "✓ Đã thuộc" else "✓ Đánh dấu đã thuộc"

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
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
    androidx.compose.material3.Card(
        shape = VocabShapes.BigCard,
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                        tint = VocabColors.BrandPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Mức độ ghi nhớ (SRS):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VocabColors.BrandDark,
                        maxLines = 1
                    )
                }

                Text(
                    text = "Tự động tính chu kỳ ôn",
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
                    bgColor = Color(0xFFFFE4E6),
                    borderColor = Color(0xFFFECDD3),
                    contentColor = Color(0xFFE11D48),
                    modifier = Modifier.weight(1f),
                    onClick = { onRate(SrsRating.AGAIN) }
                )
                SrsRatingButton(
                    title = "Khó",
                    subtitle = SrsScheduler.getRatingSubtitle(word.srsState, SrsRating.HARD),
                    bgColor = Color(0xFFFEF3C7),
                    borderColor = Color(0xFFFDE68A),
                    contentColor = Color(0xFFD97706),
                    modifier = Modifier.weight(1f),
                    onClick = { onRate(SrsRating.HARD) }
                )
                SrsRatingButton(
                    title = "Tốt",
                    subtitle = SrsScheduler.getRatingSubtitle(word.srsState, SrsRating.GOOD),
                    bgColor = Color(0xFFDBEAFE),
                    borderColor = Color(0xFFBFDBFE),
                    contentColor = Color(0xFF2563EB),
                    modifier = Modifier.weight(1f),
                    onClick = { onRate(SrsRating.GOOD) }
                )
                SrsRatingButton(
                    title = "Dễ",
                    subtitle = SrsScheduler.getRatingSubtitle(word.srsState, SrsRating.EASY),
                    bgColor = Color(0xFFDCFCE7),
                    borderColor = Color(0xFFBBF7D0),
                    contentColor = Color(0xFF16A34A),
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
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun FlashcardNavigationButtons(
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    val shapeNav = RoundedCornerShape(22.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .clip(shapeNav)
                .background(Color.White)
                .border(1.5.dp, VocabColors.BrandDark, shapeNav)
                .clickable(onClick = onPrevious),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "←  Thẻ trước",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = VocabColors.BrandDark
            )
        }

        Box(
            modifier = Modifier
                .weight(1.15f)
                .height(46.dp)
                .clip(shapeNav)
                .background(VocabColors.BrandDark)
                .clickable(onClick = onNext),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Thẻ tiếp theo  →",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
