package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape
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

private const val BADGE_TEXT = "AI MICRO LESSON GENERATOR"
private const val TITLE_TEXT = "Nhập chủ đề bạn muốn học hôm nay:"
private const val PLACEHOLDER_TEXT = "Ví dụ: Gọi món trong quán lẩu Tứ Xuyên..."

@Composable
fun SparkPromptCard(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onGenerateClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color(0xFFFFFBEB), // Kem phiêu lưu RPG
        headerTitle = "⚡ SPARK PROMPT STUDIO",
        headerBadge = "AI V4.0",
        headerColor = PixelColors.PixelYellow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // Tiêu đề hướng dẫn
            Text(
                text = TITLE_TEXT,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.BorderDark,
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Ô nhập liệu Pixel
            val inputShape = PixelStepCornerShape(2.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(inputShape)
                    .background(Color.White)
                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), inputShape)
                    .padding(horizontal = 10.dp, vertical = 9.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (prompt.isEmpty()) {
                    Text(
                        text = PLACEHOLDER_TEXT,
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }

                BasicTextField(
                    value = prompt,
                    onValueChange = onPromptChange,
                    textStyle = TextStyle(
                        fontSize = 12.5.sp,
                        color = PixelColors.BorderDark,
                        fontWeight = FontWeight.Bold
                    ),
                    cursorBrush = SolidColor(PixelColors.PixelBlue),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Nút tạo bài học Arcade
            PixelArcadeButton(
                text = if (isLoading) "LUMINA ĐANG TẠO... ⏳" else "TẠO 3 TIA SÁNG ▶",
                color = PixelColors.PixelOrange,
                fillMaxWidth = true,
                onClick = onGenerateClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Vạch ngăn cách pixel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFFDE68A))
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Gợi ý chủ đề nhanh
            QuickSuggestionsSection(
                onSelectSuggestion = { suggestion ->
                    onPromptChange("${suggestion.title} (${suggestion.hanzi})")
                }
            )
        }
    }
}

@Composable
private fun QuickSuggestionsSection(
    onSelectSuggestion: (PromptSuggestion) -> Unit
) {
    FlowChipsLayout(
        modifier = Modifier.fillMaxWidth(),
        horizontalSpacing = 6.dp,
        verticalSpacing = 6.dp
    ) {
        Text(
            text = "Gợi ý nhanh:",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF78350F)
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
    val chipShape = PixelStepCornerShape(1.5.dp)
    Box(
        modifier = Modifier
            .clip(chipShape)
            .background(Color.White)
            .border(BorderStroke(1.dp, Color(0xFFFDE68A)), chipShape)
            .bounceClick(scaleDown = 0.94f, onClick = onClick)
            .padding(horizontal = 7.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = suggestion.title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4338CA)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "(${suggestion.hanzi})",
                fontSize = 9.sp,
                color = Color(0xFF64748B)
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
