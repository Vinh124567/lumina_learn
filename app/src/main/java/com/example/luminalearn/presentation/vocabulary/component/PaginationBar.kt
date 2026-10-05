package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick

@Composable
fun PaginationBar(
    currentPage: Int,
    totalPages: Int,
    totalItems: Int,
    pageSize: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    itemUnit: String = "từ"
) {
    val startItem = if (totalItems > 0) (currentPage - 1) * pageSize + 1 else 0
    val endItem = minOf(currentPage * pageSize, totalItems)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(18.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                PageNavButton(
                    enabled = currentPage > 1,
                    iconRes = R.drawable.ic_chevron_left,
                    contentDescription = "Trang trước",
                    onClick = { onPageChange(currentPage - 1) }
                )

                Spacer(modifier = Modifier.width(8.dp))

                PageNumbersRow(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPageChange = onPageChange
                )

                Spacer(modifier = Modifier.width(8.dp))

                PageNavButton(
                    enabled = currentPage < totalPages,
                    iconRes = R.drawable.ic_chevron_right,
                    contentDescription = "Trang sau",
                    onClick = { onPageChange(currentPage + 1) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Trang $currentPage / $totalPages · Hiển thị $startItem–$endItem trong số $totalItems $itemUnit",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun PageNavButton(
    enabled: Boolean,
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    val bgColor = if (enabled) Color(0xFFF8FAFC) else Color(0xFFF1F5F9)
    val borderColor = if (enabled) Color(0xFFE2E8F0) else Color(0xFFE2E8F0).copy(alpha = 0.5f)
    val tintColor = if (enabled) Color(0xFF0F172A) else Color(0xFF94A3B8)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(10.dp))
            .bounceClick(scaleDown = 0.92f, onClick = if (enabled) onClick else null)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = tintColor,
            modifier = Modifier.size(16.dp)
        )
    }
}

private sealed interface PageNumberItem {
    data class Page(val number: Int) : PageNumberItem
    data object Ellipsis : PageNumberItem
}

private fun buildPageList(currentPage: Int, totalPages: Int): List<PageNumberItem> {
    if (totalPages <= 7) {
        return (1..totalPages).map { PageNumberItem.Page(it) }
    }
    val list = mutableListOf<PageNumberItem>()
    list.add(PageNumberItem.Page(1))

    if (currentPage <= 4) {
        for (p in 2..5) {
            list.add(PageNumberItem.Page(p))
        }
        list.add(PageNumberItem.Ellipsis)
        list.add(PageNumberItem.Page(totalPages))
    } else if (currentPage >= totalPages - 3) {
        list.add(PageNumberItem.Ellipsis)
        for (p in (totalPages - 4) until totalPages) {
            list.add(PageNumberItem.Page(p))
        }
        list.add(PageNumberItem.Page(totalPages))
    } else {
        list.add(PageNumberItem.Ellipsis)
        list.add(PageNumberItem.Page(currentPage - 1))
        list.add(PageNumberItem.Page(currentPage))
        list.add(PageNumberItem.Page(currentPage + 1))
        list.add(PageNumberItem.Ellipsis)
        list.add(PageNumberItem.Page(totalPages))
    }
    return list
}

@Composable
private fun PageNumbersRow(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    val items = androidx.compose.runtime.remember(currentPage, totalPages) {
        buildPageList(currentPage, totalPages)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items.forEach { item ->
            when (item) {
                is PageNumberItem.Page -> {
                    PageNumberButton(
                        pageIndex = item.number,
                        isCurrentPage = item.number == currentPage,
                        onClick = { onPageChange(item.number) }
                    )
                }
                is PageNumberItem.Ellipsis -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(width = 24.dp, height = 36.dp)
                    ) {
                        Text(
                            text = "•••",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PageNumberButton(
    pageIndex: Int,
    isCurrentPage: Boolean,
    onClick: () -> Unit
) {
    val backgroundModifier = if (isCurrentPage) {
        Modifier
            .background(
                Brush.horizontalGradient(listOf(Color(0xFF6366F1), Color(0xFF4F46E5))),
                RoundedCornerShape(10.dp)
            )
            .border(BorderStroke(1.dp, Color(0xFF818CF8).copy(alpha = 0.6f)), RoundedCornerShape(10.dp))
    } else {
        Modifier
            .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
            .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(10.dp))
    }
    val textColor = if (isCurrentPage) Color.White else Color(0xFF475569)
    val fontWeight = if (isCurrentPage) FontWeight.Bold else FontWeight.Medium

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .then(backgroundModifier)
            .bounceClick(scaleDown = 0.92f, onClick = onClick)
    ) {
        Text(
            text = pageIndex.toString(),
            fontSize = 13.sp,
            fontWeight = fontWeight,
            color = textColor
        )
    }
}

