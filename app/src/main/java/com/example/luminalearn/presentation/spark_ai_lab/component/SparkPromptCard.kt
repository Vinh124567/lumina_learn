package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick

data class PromptSuggestion(
    val title: String,
    val hanzi: String
)

val QUICK_SUGGESTIONS = listOf(
    PromptSuggestion("Gọi trà sữa & Cà phê", "点奶茶咖啡"),
    PromptSuggestion("Gọi món lẩu & Món ăn", "餐厅点菜"),
    PromptSuggestion("Hỏi đường & Bắt xe", "问路打车"),
    PromptSuggestion("Mua sắm & Trả giá", "购物砍价"),
    PromptSuggestion("Giới thiệu bản thân & HSK 2", "自我介绍")
)

private val PrimaryPurple = Color(0xFF5C50F6)
private val DarkTitleColor = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderColor = Color(0xFFE2E8F0)
private val DividerColor = Color(0xFFF1F5F9)
private val InputBgColor = Color(0xFFF8FAFC)

private val ButtonGradient = Brush.horizontalGradient(
    listOf(Color(0xFF4F46E5), Color(0xFF5C50F6))
)

private const val BADGE_TEXT = "SINH BÀI HỌC VI MÔ CÁ NHÂN HÓA BẰNG AI"
private const val TITLE_TEXT = "Nhập chủ đề bạn muốn học hôm nay:"
private const val PLACEHOLDER_TEXT = "Ví dụ: Gọi món trong quán lẩu Tứ Xuyên..."
private const val BUTTON_TEXT = "Tạo 3 Tia Sáng"
private const val BUTTON_LOADING_TEXT = "Lumina đang tạo..."

@Composable
fun SparkPromptCard(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onGenerateClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(14.dp))

            InputAndButtonSection(
                prompt = prompt,
                onPromptChange = onPromptChange,
                onGenerateClick = onGenerateClick,
                isLoading = isLoading
            )

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            QuickSuggestionsSection(
                onSelectSuggestion = { suggestion ->
                    onPromptChange("${suggestion.title} (${suggestion.hanzi})")
                }
            )
        }
    }
}

@Composable
private fun HeaderSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sparkle),
                contentDescription = null,
                tint = PrimaryPurple,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = BADGE_TEXT,
                color = PrimaryPurple,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = TITLE_TEXT,
            fontSize = 16.5.sp,
            fontWeight = FontWeight.Bold,
            color = DarkTitleColor
        )
    }
}

@Composable
private fun InputAndButtonSection(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onGenerateClick: () -> Unit,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PromptTextField(
            prompt = prompt,
            onPromptChange = onPromptChange,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        GenerateButton(
            isLoading = isLoading,
            onClick = onGenerateClick
        )
    }
}

@Composable
private fun PromptTextField(
    prompt: String,
    onPromptChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        color = InputBgColor,
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 11.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (prompt.isEmpty()) {
                Text(
                    text = PLACEHOLDER_TEXT,
                    color = TextMuted,
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp,
                    maxLines = 1
                )
            }

            BasicTextField(
                value = prompt,
                onValueChange = onPromptChange,
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = DarkTitleColor,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(PrimaryPurple),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun GenerateButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spin_transition")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    Surface(
        shape = RoundedCornerShape(50.dp),
        shadowElevation = 2.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .then(
                if (!isLoading) Modifier.bounceClick(scaleDown = 0.94f, onClick = onClick)
                else Modifier
            )
    ) {
        Row(
            modifier = Modifier
                .background(ButtonGradient)
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(14.dp)
                        .rotate(rotationAngle)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = BUTTON_LOADING_TEXT,
                    color = Color.White,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sparkle),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = BUTTON_TEXT,
                    color = Color.White,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuickSuggestionsSection(
    onSelectSuggestion: (PromptSuggestion) -> Unit
) {
    FlowChipsLayout(
        modifier = Modifier.fillMaxWidth(),
        horizontalSpacing = 8.dp,
        verticalSpacing = 8.dp
    ) {
        Text(
            text = "Gợi ý nhanh:",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextMuted
        )

        QUICK_SUGGESTIONS.forEach { suggestion ->
            SuggestionChip(
                suggestion = suggestion,
                onClick = { onSelectSuggestion(suggestion) }
            )
        }
    }
}

@Composable
private fun SuggestionChip(
    suggestion: PromptSuggestion,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier.bounceClick(scaleDown = 0.92f, onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = suggestion.title,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4338CA)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "(${suggestion.hanzi})",
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun FlowChipsLayout(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val hSpacingPx = horizontalSpacing.roundToPx()
        val vSpacingPx = verticalSpacing.roundToPx()

        val rows = mutableListOf<List<Placeable>>()
        val rowHeights = mutableListOf<Int>()
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0
        var currentRowHeight = 0

        for (measurable in measurables) {
            val placeable = measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
            if (currentRow.isNotEmpty() && currentRowWidth + hSpacingPx + placeable.width > constraints.maxWidth) {
                rows.add(currentRow)
                rowHeights.add(currentRowHeight)
                currentRow = mutableListOf()
                currentRowWidth = 0
                currentRowHeight = 0
            }
            if (currentRow.isNotEmpty()) {
                currentRowWidth += hSpacingPx
            }
            currentRow.add(placeable)
            currentRowWidth += placeable.width
            currentRowHeight = maxOf(currentRowHeight, placeable.height)
        }
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
            rowHeights.add(currentRowHeight)
        }

        val totalHeight = (rowHeights.sum() + (rowHeights.size - 1).coerceAtLeast(0) * vSpacingPx)
            .coerceIn(constraints.minHeight, constraints.maxHeight)
        val totalWidth = constraints.maxWidth

        layout(totalWidth, totalHeight) {
            var y = 0
            for (i in rows.indices) {
                var x = 0
                val row = rows[i]
                val rHeight = rowHeights[i]
                for (placeable in row) {
                    val yOffset = y + (rHeight - placeable.height) / 2
                    placeable.placeRelative(x, yOffset)
                    x += placeable.width + hSpacingPx
                }
                y += rHeight + vSpacingPx
            }
        }
    }
}
