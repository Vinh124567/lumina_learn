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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

/**
 * Màn hình danh sách từ vựng chi tiết theo cấp độ HSK / Tất cả từ vựng dạng Grid 2 cột.
 * Thiết kế giao diện Luminous Bento cao cấp, đồng bộ hoàn hảo với Home và Vocab Tab.
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
    val progressPercent = remember(levelWords.size, masteredCount) {
        if (levelWords.isNotEmpty()) (masteredCount * 100 / levelWords.size) else 0
    }

    val filterOptions = remember(levelWords.size, masteredCount, srsEnrolledCount, dueInLevelCount, customCount) {
        listOf(
            "Tất cả (${levelWords.size})",
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
    ) {
        // ── 1. APP BAR MÀN CON CHUYÊN BIỆT: CHỈ 1 DÒNG DUY NHẤT, THOÁNG ĐÃNG ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút quay lại dạng < (chevron) phẳng
            Box(
                modifier = Modifier
                    .size(40.dp)
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

            Spacer(modifier = Modifier.width(6.dp))

            // Tiêu đề duy nhất 1 dòng phẳng đẹp, không badge thừa
            Text(
                text = if (selectedHskLevelNumber == 0) "Tất cả từ vựng" else currentHskData.stageName,
                fontFamily = PlusJakartaSans,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        // ── 2. DẢI CHỌN CẤP ĐỘ HSK (CHUYỂN NHANH HSK 1–6 HOẶC XEM TẤT CẢ) ──
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            items(hskLevelPills) { (levelNum, label) ->
                val isSelected = selectedHskLevelNumber == levelNum
                val pillGradient = getHskGradientColors(levelNum)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) {
                                Brush.horizontalGradient(pillGradient)
                            } else {
                                SolidColor(Color.White)
                            }
                        )
                        .border(
                            BorderStroke(
                                1.dp,
                                if (isSelected) Color.Transparent
                                else Color(0xFFE2E8F0)
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .bounceClick(scaleDown = 0.94f) {
                            selectedHskLevelNumber = levelNum
                            selectedFilterIndex = 0
                        }
                        .padding(horizontal = 13.dp, vertical = 6.5.dp)
                ) {
                    Text(
                        text = label,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) Color.White else Color(0xFF475569)
                    )
                }
            }
        }

        // ── 3. TIẾN ĐỘ GHI NHỚ TRONG BODY ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "Tiến độ: $masteredCount / ${levelWords.size} từ đã thuộc",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(2.5.dp))
                        .background(Color(0xFFE2E8F0))
                ) {
                    val progressFraction = (progressPercent.coerceIn(0, 100) / 100f)
                    if (progressFraction > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressFraction)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(2.5.dp))
                                .background(Brush.horizontalGradient(currentGradient))
                        )
                    }
                }

                Text(
                    text = "$progressPercent%",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (selectedHskLevelNumber == 0) Color(0xFF6366F1) else currentHskData.accentColor
                )
            }
        }

        // ── 4. KHUNG TÌM KIẾM ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 3.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Tìm chữ Hán, Pinyin, nghĩa...",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.5.sp,
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
                                fontSize = 12.5.sp,
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
                                .size(16.dp)
                                .clickable { searchQuery = "" }
                        )
                    }
                }
            }
        }

        // ── 5. BỘ LỌC PHÂN LOẠI & TRẠNG THÁI (STATUS / POS) ──
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            itemsIndexed(filterOptions) { index, title ->
                val isSelected = selectedFilterIndex == index
                val activeAccent = if (selectedHskLevelNumber == 0) Color(0xFF6366F1) else currentHskData.accentColor
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) activeAccent.copy(alpha = 0.12f)
                            else Color.White
                        )
                        .border(
                            BorderStroke(
                                1.dp,
                                if (isSelected) activeAccent.copy(alpha = 0.45f)
                                else Color(0xFFE2E8F0)
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .bounceClick(scaleDown = 0.94f) {
                            selectedFilterIndex = index
                        }
                        .padding(horizontal = 11.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = title,
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) activeAccent else Color(0xFF64748B)
                    )
                }
            }
        }

        // ── 6. NỘI DUNG GRID 2 CỘT ──
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
                    if (selectedFilterIndex == 6) {
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
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 6.dp,
                    bottom = 120.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = displayWords,
                    key = { it.id }
                ) { wordItem ->
                    val wordAccent = getWordLevelAccent(wordItem.hskLevel, currentHskData.accentColor)
                    val wordGradient = getWordLevelGradient(wordItem.hskLevel, currentGradient)

                    VocabGridCard(
                        item = wordItem,
                        accentColor = wordAccent,
                        accentGradient = wordGradient,
                        onSpeak = onSpeakWord,
                        onToggleMastered = { onToggleMastered(wordItem.id) },
                        onToggleEnrollSrs = { onToggleEnrollSrs(wordItem.id) },
                        onClick = { onOpenWordDetail(wordItem) }
                    )
                }
            }
        }
    }
}

/**
 * Thẻ từ vựng hiển thị dạng Grid 2 cột phong cách Deep Indigo Quartz cao cấp.
 * Nền tím chàm hoàng gia, chữ Hán trắng tuyết, Pinyin chàm phấn, Hán-Việt hổ phách.
 */
@Composable
private fun VocabGridCard(
    item: VocabWordItem,
    accentColor: Color,
    accentGradient: List<Color>,
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
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            1.2.dp,
            if (item.isMastered) Color(0xFF10B981).copy(alpha = 0.65f)
            else Color(0xFF818CF8).copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF161338),
                            Color(0xFF221C52),
                            Color(0xFF1A1442)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // ── Vạch màu Gradient mỏng ở mép trên nhận diện cấp độ HSK ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.5.dp)
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .background(Brush.horizontalGradient(accentGradient))
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 11.dp, vertical = 9.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header: Loại từ badge bên trái + Cụm 3 nút hành động bên phải
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val posLabel = item.partOfSpeech.ifBlank { "Từ vựng" }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(
                                    BorderStroke(0.8.dp, accentColor.copy(alpha = 0.45f)),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
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

                        // Cụm nút: Loa phát âm & Ôn SRS & Đánh dấu thuộc từ
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            // Nút loa phát âm
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.10f))
                                    .border(BorderStroke(0.8.dp, Color(0xFF818CF8).copy(alpha = 0.35f)), CircleShape)
                                    .bounceClick(scaleDown = 0.85f) {
                                        onSpeak(item.hanzi)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_speaker),
                                    contentDescription = "Phát âm",
                                    tint = Color(0xFFC7D2FE),
                                    modifier = Modifier.size(13.dp)
                                )
                            }

                            // Nút thêm/bớt khỏi lộ trình ôn SRS
                            val isInSrs = item.isInSrs
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isInSrs) Color(0xFFF59E0B).copy(alpha = 0.20f)
                                        else Color.White.copy(alpha = 0.08f)
                                    )
                                    .border(
                                        BorderStroke(
                                            0.8.dp,
                                            if (isInSrs) Color(0xFFFDE68A).copy(alpha = 0.50f)
                                            else Color.White.copy(alpha = 0.15f)
                                        ),
                                        CircleShape
                                    )
                                    .bounceClick(scaleDown = 0.85f, onClick = onToggleEnrollSrs),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_bolt),
                                    contentDescription = if (isInSrs) "Đang trong lộ trình SRS" else "Thêm vào lộ trình SRS",
                                    tint = if (isInSrs) Color(0xFFFDE68A) else Color(0xFF94A3B8),
                                    modifier = Modifier.size(13.dp)
                                )
                            }

                            // Nút đánh dấu Đã thuộc / Chưa thuộc ở góc phải
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (item.isMastered) Color(0xFF10B981).copy(alpha = 0.20f)
                                        else Color.White.copy(alpha = 0.08f)
                                    )
                                    .border(
                                        BorderStroke(
                                            0.8.dp,
                                            if (item.isMastered) Color(0xFF34D399).copy(alpha = 0.55f)
                                            else Color.White.copy(alpha = 0.20f)
                                        ),
                                        CircleShape
                                    )
                                    .bounceClick(scaleDown = 0.85f, onClick = onToggleMastered),
                                contentAlignment = Alignment.Center
                            ) {
                                if (item.isMastered) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_check),
                                        contentDescription = "Đã thuộc",
                                        tint = Color(0xFF34D399),
                                        modifier = Modifier.size(13.dp)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(9.dp)
                                            .border(1.2.dp, Color(0xFF94A3B8), CircleShape)
                                    )
                                }
                            }
                        }
                    }

                    // Centerpiece: Chữ Hán to rõ 35sp trắng tuyết + Pinyin & Hán Việt hổ phách
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.hanzi,
                            fontFamily = PlusJakartaSans,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
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
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC7D2FE),
                                maxLines = 1
                            )

                            if (item.hanViet.isNotBlank()) {
                                Text(
                                    text = " • ",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.sp,
                                    color = Color(0xFF818CF8)
                                )
                                Text(
                                    text = item.hanViet,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFFDE68A),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Bottom: Nghĩa tiếng Việt sáng rõ, tương phản sắc nét
                    Text(
                        text = item.meaning,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFE2E8F0),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 2.dp)
                    )
                }
            }
        }
    }
}
