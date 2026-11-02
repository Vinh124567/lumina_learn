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

import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape

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

                Spacer(modifier = Modifier.height(10.dp))

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
        horizontalArrangement = Arrangement.spacedBy(6.dp),
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

@Composable
private fun FilterChipItem(
    option: RoleplayFilterOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val chipShape = PixelStepCornerShape(2.dp)

    Box(
        modifier = Modifier
            .clip(chipShape)
            .background(if (isSelected) PixelColors.PixelBlue else Color.White)
            .border(
                BorderStroke(
                    if (isSelected) 1.2.dp else 1.dp,
                    if (isSelected) PixelColors.BorderDark else Color(0xFFCBD5E1)
                ),
                chipShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (option.dotColor != null) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(PixelStepCornerShape(1.dp))
                        .background(if (isSelected) Color.White else option.dotColor)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }

            Text(
                text = option.title,
                fontSize = 11.5.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                color = if (isSelected) Color.White else Color(0xFF334155),
                letterSpacing = 0.2.sp
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
    val searchShape = PixelStepCornerShape(2.dp)

    Box(
        modifier = modifier
            .clip(searchShape)
            .background(Color.White)
            .border(BorderStroke(1.2.dp, PixelColors.BorderDark), searchShape)
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(15.dp)
            )

            Spacer(modifier = Modifier.width(7.dp))

            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                textStyle = TextStyle(
                    fontSize = 12.sp,
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(PixelColors.PixelBlue),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Tìm kiếm kịch bản, nhân vật...",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.weight(1f)
            )

            if (searchQuery.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(PixelStepCornerShape(1.dp))
                        .background(Color(0xFFE2E8F0))
                        .clickable { onSearchChange("") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF475569)
                    )
                }
            }
        }
    }
}
