package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R

data class RoleplayFilterOption(
    val id: String,
    val title: String,
    val dotColor: Color? = null
)

val ROLEPLAY_FILTER_OPTIONS = listOf(
    RoleplayFilterOption("all", "Tất cả tình huống"),
    RoleplayFilterOption("hsk12", "HSK 1–2 Cơ bản", Color(0xFF22C55E)),
    RoleplayFilterOption("hsk23", "HSK 2–3 Giao tiếp", Color(0xFF3B82F6)),
    RoleplayFilterOption("hsk34", "HSK 3–4 Nâng cao", Color(0xFFF97316))
)

private val ActiveFilterBg = Color(0xFF4338CA)
private val InactiveFilterBorder = Color(0xFFE2E8F0)
private val SearchBgColor = Color(0xFFF8FAFC)
private val TextDark = Color(0xFF334155)

@Composable
fun RoleplayFilterAndSearchBar(
    selectedFilterId: String,
    onSelectFilter: (String) -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val isWide = maxWidth >= 680.dp

        if (isWide) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChipsRow(
                    selectedFilterId = selectedFilterId,
                    onSelectFilter = onSelectFilter,
                    modifier = Modifier.weight(1f).padding(end = 16.dp)
                )

                SearchBarField(
                    searchQuery = searchQuery,
                    onSearchChange = onSearchChange,
                    modifier = Modifier.width(240.dp)
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                FilterChipsRow(
                    selectedFilterId = selectedFilterId,
                    onSelectFilter = onSelectFilter,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                SearchBarField(
                    searchQuery = searchQuery,
                    onSearchChange = onSearchChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun FilterChipsRow(
    selectedFilterId: String,
    onSelectFilter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier.horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ROLEPLAY_FILTER_OPTIONS.forEach { option ->
            val isSelected = option.id == selectedFilterId
            FilterChipItem(
                option = option,
                isSelected = isSelected,
                onClick = { onSelectFilter(option.id) }
            )
        }
    }
}

private val ActiveFilterGradient = Brush.horizontalGradient(
    listOf(Color(0xFF4F46E5), Color(0xFF6366F1))
)

@Composable
private fun FilterChipItem(
    option: RoleplayFilterOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgModifier = if (isSelected) {
        Modifier.background(ActiveFilterGradient)
    } else {
        Modifier.background(Color.White)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .then(bgModifier)
            .then(
                if (isSelected) Modifier
                else Modifier.border(BorderStroke(1.dp, InactiveFilterBorder), RoundedCornerShape(50.dp))
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (option.dotColor != null) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(option.dotColor)
                )
                Spacer(modifier = Modifier.width(7.dp))
            }

            Text(
                text = option.title,
                fontSize = 12.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else TextDark
            )
        }
    }
}

@Composable
private fun SearchBarField(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        color = SearchBgColor,
        border = BorderStroke(1.dp, InactiveFilterBorder)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = TextDark,
                    fontWeight = FontWeight.Normal
                ),
                cursorBrush = SolidColor(ActiveFilterBg),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Tìm kịch bản, nhân vật...",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
