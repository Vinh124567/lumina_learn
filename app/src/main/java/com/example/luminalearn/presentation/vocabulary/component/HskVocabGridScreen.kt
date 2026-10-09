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
        // ── 1. APP BAR THANH LỊCH & NÚT THÊM TỪ ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 8.dp, end = 16.dp, top = 6.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút quay lại dạng < (chevron) phẳng
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .bounceClick(scaleDown = 0.86f, onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = "Quay lại",
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Tiêu đề màn hình
            Text(
                text = if (selectedHskLevelNumber == 0) "Tất cả từ vựng" else currentHskData.stageName,
                fontFamily = PlusJakartaSans,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // Nút chuyển đổi chế độ xem (Lưới 2 cột <-> Danh sách 1 cột)
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(BorderStroke(0.5.dp, Color(0xFFE2E8F0)), CircleShape)
                    .bounceClick(scaleDown = 0.88f) {
                        isGridView = !isGridView
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        if (isGridView) R.drawable.ic_view_list else R.drawable.ic_view_grid
                    ),
                    contentDescription = stringResource(
                        if (isGridView) R.string.content_desc_switch_to_list else R.string.content_desc_switch_to_grid
                    ),
                    tint = Color(0xFF475569),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Nút + Thêm từ nhanh
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEF2FF))
                    .border(BorderStroke(0.5.dp, Color(0xFFC7D2FE)), CircleShape)
                    .bounceClick(scaleDown = 0.88f, onClick = onAddVocabClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Thêm từ",
                    tint = Color(0xFF6366F1),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // ── 2. KHUNG TÌM KIẾM MỎNG GỌN ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 3.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(0.5.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(15.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Tìm chữ Hán, Pinyin, nghĩa...",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8),
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
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF0F172A)
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(Color(0xFF6366F1)),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Clear",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier
                                .size(15.dp)
                                .clickable { searchQuery = "" }
                        )
                    }
                }
            }
        }

        // ── 3. DẢI CHỌN CẤP ĐỘ HSK (CHUYỂN NHANH HSK 1–6 HOẶC XEM TẤT CẢ) ──
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
        ) {
            items(hskLevelPills) { (levelNum, label) ->
                val isSelected = selectedHskLevelNumber == levelNum
                val pillGradient = getHskGradientColors(levelNum)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) {
                                Brush.horizontalGradient(pillGradient)
                            } else {
                                SolidColor(Color.White)
                            }
                        )
                        .border(
                            BorderStroke(
                                0.5.dp,
                                if (isSelected) Color.Transparent
                                else Color(0xFFE2E8F0)
                            ),
                            RoundedCornerShape(10.dp)
                        )
                        .bounceClick(scaleDown = 0.94f) {
                            selectedHskLevelNumber = levelNum
                            selectedFilterIndex = 0
                        }
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = label,
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) Color.White else Color(0xFF475569)
                    )
                }
            }
        }

        // ── 4. BỘ LỌC PHÂN LOẠI & TRẠNG THÁI (STATUS / POS) ──
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
        ) {
            itemsIndexed(filterOptions) { index, title ->
                val isSelected = selectedFilterIndex == index
                val activeAccent = if (selectedHskLevelNumber == 0) Color(0xFF6366F1) else currentHskData.accentColor
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) activeAccent.copy(alpha = 0.12f)
                            else Color.White
                        )
                        .border(
                            BorderStroke(
                                0.5.dp,
                                if (isSelected) activeAccent.copy(alpha = 0.45f)
                                else Color(0xFFE2E8F0)
                            ),
                            RoundedCornerShape(10.dp)
                        )
                        .bounceClick(scaleDown = 0.94f) {
                            selectedFilterIndex = index
                        }
                        .padding(horizontal = 10.dp, vertical = 4.5.dp)
                ) {
                    Text(
                        text = title,
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) activeAccent else Color(0xFF64748B)
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
 * Thẻ từ vựng hiển thị dạng Grid 2 cột phong cách Luxury Flashcard:
 * - Nền trắng sứ tinh tế, bo góc 20dp, viền siêu mỏng hairline 0.5dp.
 * - Header: Nhãn loại từ bên trái & Duy nhất nút Loa phát âm bên phải (thoáng đãng, không chật chội).
 * - Center: Chữ Hán to rõ 35sp mực than sâu, Pinyin & Hán Việt hài hòa.
 * - Footer: Nghĩa tiếng Việt cùng cụm nút hành động SRS và Đánh dấu thuộc cân xứng ở đáy.
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(176.dp)
            .bounceClick(scaleDown = 0.96f, onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isMastered) Color(0xFFF9FDFB) else Color.White
        ),
        border = BorderStroke(
            0.5.dp,
            if (item.isMastered) Color(0xFF10B981).copy(alpha = 0.35f)
            else Color(0xFFE2E8F0).copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── 1. Header: Loại từ badge bên trái + Duy nhất nút Loa phát âm bên phải ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val posLabel = item.partOfSpeech.ifBlank { "Từ vựng" }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor.copy(alpha = 0.08f))
                        .padding(horizontal = 7.dp, vertical = 2.5.dp)
                ) {
                    Text(
                        text = posLabel,
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Nút loa phát âm tròn thoáng đãng
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF8FAFC))
                        .border(BorderStroke(0.5.dp, Color(0xFFE2E8F0)), CircleShape)
                        .bounceClick(scaleDown = 0.85f) {
                            onSpeak(item.hanzi)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_speaker),
                        contentDescription = "Phát âm",
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // ── 2. Centerpiece: Chữ Hán to rõ 35sp mực than sâu + Pinyin & Hán Việt ──
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.hanzi,
                    fontFamily = PlusJakartaSans,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A),
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
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        maxLines = 1
                    )

                    if (item.hanViet.isNotBlank()) {
                        Text(
                            text = " • ",
                            fontFamily = PlusJakartaSans,
                            fontSize = 10.sp,
                            color = Color(0xFFCBD5E1)
                        )
                        Text(
                            text = item.hanViet,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B),
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
                    lineHeight = 14.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Nút thêm/bớt khỏi lộ trình ôn SRS
                    val isInSrs = item.isInSrs
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (isInSrs) Color(0xFFFEF3C7)
                                else Color(0xFFF1F5F9)
                            )
                            .bounceClick(scaleDown = 0.85f, onClick = onToggleEnrollSrs),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bolt),
                            contentDescription = if (isInSrs) "Đang ôn SRS" else "Thêm SRS",
                            tint = if (isInSrs) Color(0xFFD97706) else Color(0xFF94A3B8),
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    // Nút đánh dấu Đã thuộc / Chưa thuộc
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (item.isMastered) Color(0xFFDCFCE7)
                                else Color(0xFFF1F5F9)
                            )
                            .bounceClick(scaleDown = 0.85f, onClick = onToggleMastered),
                        contentAlignment = Alignment.Center
                    ) {
                        if (item.isMastered) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = "Đã thuộc",
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(13.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .border(1.dp, Color(0xFF94A3B8), CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Thẻ từ vựng hiển thị dạng List 1 cột nằm ngang:
 * - Bên trái: Khối chữ Hán to rõ trên nền nhẹ.
 * - Ở giữa: Pinyin, loại từ, nghĩa tiếng Việt.
 * - Bên phải: Nút Loa phát âm, nút SRS và nút Đánh dấu thuộc.
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .bounceClick(scaleDown = 0.98f, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isMastered) Color(0xFFF9FDFB) else Color.White
        ),
        border = BorderStroke(
            0.5.dp,
            if (item.isMastered) Color(0xFF10B981).copy(alpha = 0.35f)
            else Color(0xFFE2E8F0).copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── 1. Khối Hán tự bên trái ──
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.hanzi,
                    fontFamily = PlusJakartaSans,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

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
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        maxLines = 1
                    )

                    if (item.partOfSpeech.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFF1F5F9))
                                .padding(horizontal = 5.dp, vertical = 1.5.dp)
                        ) {
                            Text(
                                text = item.partOfSpeech,
                                fontFamily = PlusJakartaSans,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    if (item.hanViet.isNotBlank()) {
                        Text(
                            text = item.hanViet,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF94A3B8),
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = item.meaning,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // ── 3. Cụm nút tác vụ bên phải: Loa, SRS, Đã thuộc ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Nút loa phát âm
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF8FAFC))
                        .border(BorderStroke(0.5.dp, Color(0xFFE2E8F0)), CircleShape)
                        .bounceClick(scaleDown = 0.85f) {
                            onSpeak(item.hanzi)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_speaker),
                        contentDescription = "Phát âm",
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(13.dp)
                    )
                }

                // Nút SRS
                val isInSrs = item.isInSrs
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            if (isInSrs) Color(0xFFFEF3C7)
                            else Color(0xFFF1F5F9)
                        )
                        .bounceClick(scaleDown = 0.85f, onClick = onToggleEnrollSrs),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bolt),
                        contentDescription = if (isInSrs) "Đang ôn SRS" else "Thêm SRS",
                        tint = if (isInSrs) Color(0xFFD97706) else Color(0xFF94A3B8),
                        modifier = Modifier.size(13.dp)
                    )
                }

                // Nút Đã thuộc
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            if (item.isMastered) Color(0xFFDCFCE7)
                            else Color(0xFFF1F5F9)
                        )
                        .bounceClick(scaleDown = 0.85f, onClick = onToggleMastered),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.isMastered) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = "Đã thuộc",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(13.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .border(1.dp, Color(0xFF94A3B8), CircleShape)
                        )
                    }
                }
            }
        }
    }
}
