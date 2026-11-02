package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.ui.theme.PlusJakartaSans

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

    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.8.dp,
        shadowOffset = 3.dp,
        headerTitle = "PAGINATION CONTROL",
        headerColor = PixelColors.PixelYellow.copy(alpha = 0.35f),
        showGrille = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 12.dp)
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

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "TRANG $currentPage / $totalPages · $startItem–$endItem TRÊN $totalItems $itemUnit",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                color = PixelColors.MutedText,
                letterSpacing = 0.3.sp
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
    val shape = CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp)
    val bgColor = if (enabled) Color(0xFFF8FAFC) else Color(0xFFE2E8F0)
    val tintColor = if (enabled) PixelColors.BorderDark else Color(0xFF94A3B8)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(34.dp)
            .clip(shape)
            .background(bgColor)
            .border(BorderStroke(1.5.dp, PixelColors.BorderDark), shape)
            .bounceClick(scaleDown = 0.90f, onClick = if (enabled) onClick else null)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = tintColor,
            modifier = Modifier.size(15.dp)
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
    val items = remember(currentPage, totalPages) {
        buildPageList(currentPage, totalPages)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
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
                        modifier = Modifier.size(width = 20.dp, height = 34.dp)
                    ) {
                        Text(
                            text = "••",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.BorderDark
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
    val shape = CutCornerShape(topStart = 3.dp, bottomEnd = 3.dp)
    val bgColor = if (isCurrentPage) PixelColors.PixelYellow else Color(0xFFF8FAFC)
    val textColor = PixelColors.BorderDark
    val fontWeight = if (isCurrentPage) FontWeight.Black else FontWeight.Bold

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(34.dp)
            .clip(shape)
            .background(bgColor)
            .border(BorderStroke(1.5.dp, PixelColors.BorderDark), shape)
            .bounceClick(scaleDown = 0.90f, onClick = onClick)
    ) {
        Text(
            text = pageIndex.toString(),
            fontFamily = PlusJakartaSans,
            fontSize = 12.5.sp,
            fontWeight = fontWeight,
            color = textColor
        )
    }
}

