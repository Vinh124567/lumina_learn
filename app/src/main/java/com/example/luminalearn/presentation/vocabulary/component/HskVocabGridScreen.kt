package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelHeartIcon
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Trả về dải màu gradient nhận diện thương hiệu cho từng cấp độ HSK.
 */
private fun getHskGradientColors(levelNumber: Int): List<Color> {
    return when (levelNumber) {
        1 -> listOf(Color(0xFF10B981), Color(0xFF06B6D4))
        2 -> listOf(Color(0xFF6366F1), Color(0xFF38BDF8))
        3 -> listOf(Color(0xFF2563EB), Color(0xFF60A5FA))
        4 -> listOf(Color(0xFF9333EA), Color(0xFFC084FC))
        5 -> listOf(Color(0xFFD97706), Color(0xFFFBBF24))
        6 -> listOf(Color(0xFFDC2626), Color(0xFFF87171))
        else -> listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
    }
}

/**
 * Trả về màu điểm nhấn Accent tương ứng với nhãn HSK của từ vựng.
 */
private fun getWordLevelAccent(hskLevelStr: String, defaultColor: Color): Color {
    return when {
        hskLevelStr.contains("1") -> Color(0xFF059669)
        hskLevelStr.contains("2") -> Color(0xFF4F46E5)
        hskLevelStr.contains("3") -> Color(0xFF2563EB)
        hskLevelStr.contains("4") -> Color(0xFF7E22CE)
        hskLevelStr.contains("5") -> Color(0xFFD97706)
        hskLevelStr.contains("6") -> Color(0xFFDC2626)
        else -> defaultColor
    }
}

/**
 * Trả về gradient thanh viền trên cho thẻ từ vựng dựa trên cấp độ HSK.
 */
private fun getWordLevelGradient(hskLevelStr: String, defaultGradient: List<Color>): List<Color> {
    return when {
        hskLevelStr.contains("1") -> listOf(Color(0xFF10B981), Color(0xFF06B6D4))
        hskLevelStr.contains("2") -> listOf(Color(0xFF6366F1), Color(0xFF38BDF8))
        hskLevelStr.contains("3") -> listOf(Color(0xFF2563EB), Color(0xFF60A5FA))
        hskLevelStr.contains("4") -> listOf(Color(0xFF9333EA), Color(0xFFC084FC))
        hskLevelStr.contains("5") -> listOf(Color(0xFFD97706), Color(0xFFFBBF24))
        hskLevelStr.contains("6") -> listOf(Color(0xFFDC2626), Color(0xFFF87171))
        else -> defaultGradient
    }
}

private const val PAGE_SIZE = 10

/**
 * Màn hình danh sách từ vựng chi tiết theo cấp độ HSK / Tất cả từ vựng dạng Grid 2 cột.
 * Hỗ trợ phân trang 10 từ/trang với PaginationBar điều hướng trang trước/sau.
 * Header tinh gọn thoáng đãng, tích hợp sẵn tiến độ và tìm kiếm realtime.
 */
@Composable
fun HskVocabGridScreen(
    hskData: HskLevelCardData,
    vocabList: List<VocabWordItem>,
    dueTodayCount: Int,
    isLoading: Boolean,
    onBack: () -> Unit,
    onSpeakWord: (String) -> Unit,
    onSpeakWordSlow: (String) -> Unit,
    onToggleMastered: (String) -> Unit,
    onToggleEnrollSrs: (String) -> Unit = {},
    onOpenWordDetail: (VocabWordItem) -> Unit,
    onAddVocabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedHskLevelNumber by remember(hskData.levelNumber) { mutableIntStateOf(hskData.levelNumber) }
    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    var isGridView by remember { mutableStateOf(true) }

    val currentHskData = remember(selectedHskLevelNumber) {
        HSK_LEVEL_INFOS.find { it.levelNumber == selectedHskLevelNumber } ?: hskData
    }

    val currentGradient = remember(selectedHskLevelNumber) {
        getHskGradientColors(selectedHskLevelNumber)
    }

    // 1. Lọc từ theo cấp độ HSK đã chọn (0 = Tất cả)
    val levelWords = remember(vocabList, selectedHskLevelNumber) {
        if (selectedHskLevelNumber == 0) {
            vocabList
        } else {
            val hskTag = "HSK $selectedHskLevelNumber"
            vocabList.filter { it.hskLevel.contains(hskTag, ignoreCase = true) }
        }
    }

    // 2. Thống kê số lượng & tiến độ
    val masteredCount = remember(levelWords) { levelWords.count { it.isMastered } }
    val srsEnrolledCount = remember(levelWords) { levelWords.count { it.isInSrs } }
    val dueInLevelCount = remember(levelWords) { levelWords.count { it.isDueToday } }
    val customCount = remember(levelWords) { levelWords.count { it.isCustom } }

    val filterOptions = remember(levelWords.size, masteredCount, srsEnrolledCount, dueInLevelCount, customCount) {
        listOf(
            "Toàn bộ (${levelWords.size})",
            "Đã thuộc ($masteredCount)",
            "Đang ôn SRS ($srsEnrolledCount)",
            "Cần ôn ($dueInLevelCount)",
            "Động từ",
            "Danh từ",
            "Tính từ",
            "Tự thêm ($customCount)"
        )
    }

    // Danh sách cấp độ HSK chuyển nhanh
    val hskLevelPills = remember {
        listOf(
            Pair(0, "Tất cả"),
            Pair(1, "HSK 1"),
            Pair(2, "HSK 2"),
            Pair(3, "HSK 3"),
            Pair(4, "HSK 4"),
            Pair(5, "HSK 5"),
            Pair(6, "HSK 6")
        )
    }

    // 3. Lọc theo tìm kiếm và bộ lọc đã chọn
    val displayWords = remember(levelWords, searchQuery, selectedFilterIndex) {
        levelWords.filter { item ->
            val matchesSearch = if (searchQuery.isBlank()) {
                true
            } else {
                val q = searchQuery.trim().lowercase()
                item.hanzi.lowercase().contains(q) ||
                    item.pinyin.lowercase().contains(q) ||
                    item.meaning.lowercase().contains(q) ||
                    item.hanViet.lowercase().contains(q)
            }

            val matchesFilter = when (selectedFilterIndex) {
                0 -> true
                1 -> item.isMastered
                2 -> item.isInSrs
                3 -> item.isDueToday
                4 -> item.partOfSpeech.contains("động từ", ignoreCase = true)
                5 -> item.partOfSpeech.contains("danh từ", ignoreCase = true)
                6 -> item.partOfSpeech.contains("tính từ", ignoreCase = true)
                7 -> item.isCustom
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    // 4. Phân trang tối đa 10 từ mỗi trang
    var currentPage by remember(selectedHskLevelNumber, searchQuery, selectedFilterIndex) { mutableIntStateOf(1) }
    val totalFilteredWords = displayWords.size
    val totalPages = maxOf(1, (totalFilteredWords + PAGE_SIZE - 1) / PAGE_SIZE)
    val safeCurrentPage = currentPage.coerceIn(1, totalPages)
    val paginatedWords = remember(displayWords, safeCurrentPage) {
        val startIndex = (safeCurrentPage - 1) * PAGE_SIZE
        displayWords.drop(startIndex).take(PAGE_SIZE)
    }

    val gridState = rememberLazyGridState()
    LaunchedEffect(safeCurrentPage) {
        gridState.animateScrollToItem(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
    ) {
        // ── 1. APP BAR PHONG CÁCH PIXEL RETRO CART & NÚT TÁC VỤ ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 14.dp, end = 16.dp, top = 8.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút quay lại Pixel Box 36dp
            PixelCardBox(
                modifier = Modifier.size(36.dp),
                backgroundColor = Color.White,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.5.dp,
                shadowOffset = 2.dp,
                cornerRadius = 4.dp,
                showRivets = false,
                onClick = onBack
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_left),
                        contentDescription = "Quay lại",
                        tint = PixelColors.BorderDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Tiêu đề & Cấp độ HSK Cartridge
            Text(
                text = if (selectedHskLevelNumber == 0) "KHO TỪ VỰNG" else currentHskData.stageName.uppercase(),
                fontFamily = PlusJakartaSans,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.BorderDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = (-0.2).sp,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Nút chuyển đổi chế độ xem Grid / List Pixel Box 34dp
            PixelCardBox(
                modifier = Modifier.size(34.dp),
                backgroundColor = Color.White,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.5.dp,
                shadowOffset = 2.dp,
                cornerRadius = 4.dp,
                showRivets = false,
                onClick = { isGridView = !isGridView }
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(
                            if (isGridView) R.drawable.ic_view_list else R.drawable.ic_view_grid
                        ),
                        contentDescription = stringResource(
                            if (isGridView) R.string.content_desc_switch_to_list else R.string.content_desc_switch_to_grid
                        ),
                        tint = PixelColors.BorderDark,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(7.dp))

            // Nút + Thêm từ nhanh Pixel Box 34dp màu vàng
            PixelCardBox(
                modifier = Modifier.size(34.dp),
                backgroundColor = PixelColors.PixelYellow,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.5.dp,
                shadowOffset = 2.dp,
                cornerRadius = 4.dp,
                showRivets = false,
                onClick = onAddVocabClick
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Thêm từ",
                        tint = PixelColors.BorderDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // ── 2. KHUNG TÌM KIẾM PIXEL RETRO CONSOLE INPUT ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp)
        ) {
            PixelCardBox(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.8.dp,
                shadowOffset = 2.5.dp,
                cornerRadius = 5.dp,
                showRivets = false
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = PixelColors.BorderDark,
                        modifier = Modifier.size(15.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Tìm chữ Hán, Pinyin, nghĩa...",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = PixelColors.MutedText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = TextStyle(
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PixelColors.BorderDark
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(PixelColors.BorderDark),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Clear",
                            tint = PixelColors.BorderDark,
                            modifier = Modifier
                                .size(15.dp)
                                .bounceClick { searchQuery = "" }
                        )
                    }
                }
            }
        }

        // ── 3. DẢI CHỌN CẤP ĐỘ HSK (LEVEL CARTRIDGE CHIPS) ──
        LazyRow(
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            items(hskLevelPills) { (levelNum, label) ->
                val isSelected = selectedHskLevelNumber == levelNum
                val pillColor = when (levelNum) {
                    0 -> PixelColors.PixelYellow
                    1 -> PixelColors.CardGreen
                    2 -> PixelColors.CardBlue
                    3 -> Color(0xFF60A5FA)
                    4 -> PixelColors.CardPurple
                    5 -> PixelColors.CardOrange
                    6 -> Color(0xFFF87171)
                    else -> PixelColors.PixelYellow
                }
                val shape = CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp)
                Box(
                    modifier = Modifier
                        .clip(shape)
                        .background(if (isSelected) pillColor else Color.White)
                        .border(BorderStroke(1.5.dp, PixelColors.BorderDark), shape)
                        .bounceClick(scaleDown = 0.92f) {
                            selectedHskLevelNumber = levelNum
                            selectedFilterIndex = 0
                        }
                        .padding(horizontal = 11.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = label.uppercase(),
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.5.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        color = PixelColors.BorderDark
                    )
                }
            }
        }

        // ── 4. BỘ LỌC PHÂN LOẠI & TRẠNG THÁI (PIXEL FILTER CHIPS) ──
        LazyRow(
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            itemsIndexed(filterOptions) { index, title ->
                val isSelected = selectedFilterIndex == index
                val shape = CutCornerShape(topStart = 3.dp, bottomEnd = 3.dp)
                Box(
                    modifier = Modifier
                        .clip(shape)
                        .background(if (isSelected) PixelColors.PixelYellow else Color.White)
                        .border(
                            BorderStroke(1.2.dp, if (isSelected) PixelColors.BorderDark else Color(0xFFCBD5E1)),
                            shape
                        )
                        .bounceClick(scaleDown = 0.92f) {
                            selectedFilterIndex = index
                        }
                        .padding(horizontal = 9.5.dp, vertical = 4.5.dp)
                ) {
                    Text(
                        text = title,
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        color = if (isSelected) PixelColors.BorderDark else Color(0xFF64748B)
                    )
                }
            }
        }

        // ── 5. NỘI DUNG GRID 2 CỘT (PHÂN TRANG TỐI ĐA 10 TỪ / TRANG) ──
        if (isLoading && levelWords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                    color = currentHskData.accentColor
                )
            }
        } else if (displayWords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_book),
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Không tìm thấy từ vựng nào",
                        fontFamily = PlusJakartaSans,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Thử tìm kiếm với từ khóa khác hoặc đặt lại bộ lọc",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )
                    if (searchQuery.isNotEmpty() || selectedFilterIndex != 0) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(currentHskData.accentColor.copy(alpha = 0.12f))
                                .border(
                                    BorderStroke(1.dp, currentHskData.accentColor.copy(alpha = 0.3f)),
                                    RoundedCornerShape(10.dp)
                                )
                                .bounceClick {
                                    searchQuery = ""
                                    selectedFilterIndex = 0
                                }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = "Đặt lại bộ lọc",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = currentHskData.accentColor
                            )
                        }
                    }
                    if (selectedFilterIndex == 7) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF6366F1))
                                .bounceClick(onClick = onAddVocabClick)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "+ Thêm từ vựng mới",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (isGridView) 2 else 1),
                state = gridState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 6.dp,
                    bottom = 140.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = paginatedWords,
                    key = { it.id }
                ) { wordItem ->
                    val wordAccent = getWordLevelAccent(wordItem.hskLevel, currentHskData.accentColor)

                    if (isGridView) {
                        VocabGridCard(
                            item = wordItem,
                            accentColor = wordAccent,
                            onSpeak = onSpeakWord,
                            onToggleMastered = { onToggleMastered(wordItem.id) },
                            onToggleEnrollSrs = { onToggleEnrollSrs(wordItem.id) },
                            onClick = { onOpenWordDetail(wordItem) }
                        )
                    } else {
                        VocabListCard(
                            item = wordItem,
                            accentColor = wordAccent,
                            onSpeak = onSpeakWord,
                            onToggleMastered = { onToggleMastered(wordItem.id) },
                            onToggleEnrollSrs = { onToggleEnrollSrs(wordItem.id) },
                            onClick = { onOpenWordDetail(wordItem) }
                        )
                    }
                }

                // ── THANH PHÂN TRANG PAGINATION BAR DƯỚI CÙNG (10 TỪ/TRANG) ──
                if (totalPages > 1 || totalFilteredWords > 0) {
                    item(span = { GridItemSpan(if (isGridView) 2 else 1) }, key = "pagination_bar") {
                        PaginationBar(
                            currentPage = safeCurrentPage,
                            totalPages = totalPages,
                            totalItems = totalFilteredWords,
                            pageSize = PAGE_SIZE,
                            onPageChange = { newPage ->
                                currentPage = newPage
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Thẻ từ vựng hiển thị dạng Grid 2 cột phong cách Pixel Retro RPG Item Cartridge:
 * - Header: Nhãn loại từ và nhãn HSK dập trên dải Cartridge Banner có rãnh khía console.
 * - Centerpiece: Chữ Hán to rõ 33sp mực đậm, Pinyin sắc nét và Hán Việt tinh tế.
 * - Footer: Nghĩa tiếng Việt, cụm phím cơ Loa, SRS (tia sét vàng) và Trái tim RPG (Đã thuộc).
 */
@Composable
private fun VocabGridCard(
    item: VocabWordItem,
    accentColor: Color,
    onSpeak: (String) -> Unit,
    onToggleMastered: () -> Unit,
    onToggleEnrollSrs: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val posLabel = item.partOfSpeech.ifBlank { "TỪ VỰNG" }
    val hskBadge = item.hskLevel.ifBlank { null }

    PixelCardBox(
        modifier = modifier
            .fillMaxWidth()
            .height(188.dp),
        backgroundColor = if (item.isMastered) Color(0xFFF9FDFB) else Color.White,
        borderColor = if (item.isMastered) Color(0xFF059669) else PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.8.dp,
        shadowOffset = 3.dp,
        headerTitle = posLabel.uppercase(),
        headerBadge = hskBadge,
        headerColor = accentColor.copy(alpha = 0.25f),
        showGrille = true,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── 1. Top row: Nút Loa góc phải ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PixelCardBox(
                    modifier = Modifier.size(26.dp),
                    backgroundColor = Color(0xFFF8FAFC),
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.2.dp,
                    shadowOffset = 1.5.dp,
                    cornerRadius = 3.dp,
                    showRivets = false,
                    onClick = { onSpeak(item.hanzi) }
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.ic_speaker),
                            contentDescription = "Phát âm",
                            tint = PixelColors.BorderDark,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            // ── 2. Centerpiece: Chữ Hán to rõ 33sp + Pinyin & Hán Việt ──
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.hanzi,
                    fontFamily = PlusJakartaSans,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PixelColors.BorderDark,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.pinyin,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black,
                        color = accentColor,
                        maxLines = 1
                    )

                    if (item.hanViet.isNotBlank()) {
                        Text(
                            text = " • ",
                            fontFamily = PlusJakartaSans,
                            fontSize = 9.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = item.hanViet,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.MutedText,
                            maxLines = 1
                        )
                    }
                }
            }

            // ── 3. Footer: Nghĩa tiếng Việt bên trái + Nút SRS & Đã thuộc bên phải ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.meaning,
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PixelColors.BorderDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Nút SRS Pixel Box
                    val isInSrs = item.isInSrs
                    PixelCardBox(
                        modifier = Modifier.size(26.dp),
                        backgroundColor = if (isInSrs) PixelColors.CardOrange else Color(0xFFF1F5F9),
                        borderColor = PixelColors.BorderDark,
                        shadowColor = PixelColors.ShadowDark,
                        borderWidth = 1.2.dp,
                        shadowOffset = 1.5.dp,
                        cornerRadius = 3.dp,
                        showRivets = false,
                        onClick = onToggleEnrollSrs
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(R.drawable.ic_bolt),
                                contentDescription = if (isInSrs) "Đang ôn SRS" else "Thêm SRS",
                                tint = if (isInSrs) PixelColors.BorderDark else Color(0xFF94A3B8),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    // Nút Đã thuộc (Mastered) với Tim RPG
                    PixelCardBox(
                        modifier = Modifier.size(26.dp),
                        backgroundColor = if (item.isMastered) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
                        borderColor = PixelColors.BorderDark,
                        shadowColor = PixelColors.ShadowDark,
                        borderWidth = 1.2.dp,
                        shadowOffset = 1.5.dp,
                        cornerRadius = 3.dp,
                        showRivets = false,
                        onClick = onToggleMastered
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            PixelHeartIcon(isFilled = item.isMastered, size = 13.dp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Thẻ từ vựng hiển thị dạng List 1 cột nằm ngang phong cách Pixel Retro RPG Item Cartridge:
 * - Bên trái: Khối chữ Hán to rõ trong khung Pixel nổi bật.
 * - Ở giữa: Pinyin, loại từ, nghĩa tiếng Việt.
 * - Bên phải: Phím cơ Loa phát âm, phím SRS và Tim RPG Đã thuộc.
 */
@Composable
private fun VocabListCard(
    item: VocabWordItem,
    accentColor: Color,
    onSpeak: (String) -> Unit,
    onToggleMastered: () -> Unit,
    onToggleEnrollSrs: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val posLabel = item.partOfSpeech.ifBlank { "TỪ" }

    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = if (item.isMastered) Color(0xFFF9FDFB) else Color.White,
        borderColor = if (item.isMastered) Color(0xFF059669) else PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        borderWidth = 1.8.dp,
        shadowOffset = 2.5.dp,
        cornerRadius = 6.dp,
        showRivets = false,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── 1. Khối Hán tự bên trái: Hộp pixel dập nổi ──
            PixelCardBox(
                modifier = Modifier.size(52.dp),
                backgroundColor = accentColor.copy(alpha = 0.20f),
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.5.dp,
                shadowOffset = 2.dp,
                cornerRadius = 4.dp,
                showRivets = false
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = item.hanzi,
                        fontFamily = PlusJakartaSans,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // ── 2. Nội dung ở giữa: Pinyin, Hán Việt & Nghĩa ──
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.pinyin,
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = accentColor,
                        maxLines = 1
                    )

                    PixelBadge(
                        text = posLabel.uppercase(),
                        backgroundColor = Color(0xFFF1F5F9),
                        textColor = PixelColors.BorderDark
                    )

                    if (item.hanViet.isNotBlank()) {
                        Text(
                            text = item.hanViet,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.MutedText,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = item.meaning,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PixelColors.BorderDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // ── 3. Cụm nút tác vụ bên phải: Loa, SRS, Đã thuộc ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Nút loa phát âm
                PixelCardBox(
                    modifier = Modifier.size(28.dp),
                    backgroundColor = Color(0xFFF8FAFC),
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.2.dp,
                    shadowOffset = 1.5.dp,
                    cornerRadius = 3.dp,
                    showRivets = false,
                    onClick = { onSpeak(item.hanzi) }
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.ic_speaker),
                            contentDescription = "Phát âm",
                            tint = PixelColors.BorderDark,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                // Nút SRS
                val isInSrs = item.isInSrs
                PixelCardBox(
                    modifier = Modifier.size(28.dp),
                    backgroundColor = if (isInSrs) PixelColors.CardOrange else Color(0xFFF1F5F9),
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.2.dp,
                    shadowOffset = 1.5.dp,
                    cornerRadius = 3.dp,
                    showRivets = false,
                    onClick = onToggleEnrollSrs
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bolt),
                            contentDescription = if (isInSrs) "Đang ôn SRS" else "Thêm SRS",
                            tint = if (isInSrs) PixelColors.BorderDark else Color(0xFF94A3B8),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                // Nút Đã thuộc (Heart)
                PixelCardBox(
                    modifier = Modifier.size(28.dp),
                    backgroundColor = if (item.isMastered) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.2.dp,
                    shadowOffset = 1.5.dp,
                    cornerRadius = 3.dp,
                    showRivets = false,
                    onClick = onToggleMastered
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        PixelHeartIcon(isFilled = item.isMastered, size = 13.dp)
                    }
                }
            }
        }
    }
}
