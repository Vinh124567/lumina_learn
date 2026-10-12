package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.luminalearn.data.model.GrammarExampleDto
import com.example.luminalearn.data.model.GrammarStructureDto
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val BrandIndigo = Color(0xFF5C50F6)
private val BrandIndigoLight = Color(0xFF6366F1)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)
private val CardBg = Color(0xFFF8FAFC)
private val TipAmberBg = Color(0xFFFFFBEB)
private val TipAmberBorder = Color(0xFFFDE68A)
private val TipAmberText = Color(0xFFB45309)

private val HSK_LEVEL_FILTERS = listOf("Tất cả", "HSK 1", "HSK 2", "HSK 3", "HSK 4", "HSK 5", "HSK 6")

/**
 * Modal Tra cứu Cẩm nang Ngữ pháp chuẩn hóa theo khung HSK 1 - HSK 3.
 * Tích hợp tìm kiếm nhanh, lọc theo cấp độ HSK, công thức chuẩn, ví dụ phát âm và mẹo thi.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HskGrammarVaultModal(
    grammarList: List<GrammarStructureDto>,
    isLoading: Boolean,
    onFilterLevel: (String?) -> Unit,
    onSpeak: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedFilter by remember { mutableStateOf("Tất cả") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = remember(grammarList, searchQuery) {
        if (searchQuery.isBlank()) {
            grammarList
        } else {
            val query = searchQuery.trim().lowercase()
            grammarList.filter { item ->
                item.title.lowercase().contains(query) ||
                        item.formula.lowercase().contains(query) ||
                        item.explanation.lowercase().contains(query) ||
                        item.examples.any { ex ->
                            ex.hanzi.contains(query) ||
                                    ex.pinyinOriginal.lowercase().contains(query) ||
                                    ex.meaning.lowercase().contains(query)
                        }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(horizontal = 20.dp)
        ) {
            // ── 1. HEADER MODAL ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Cẩm Nang Ngữ Pháp HSK",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextMain
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Quy chuẩn ngữ pháp HSK 1 đến HSK 6 chuẩn thi quốc tế",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                        .bounceClick { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Đóng",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // ── 2. FILTER PILLS (HSK 1, HSK 2, HSK 3) ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HSK_LEVEL_FILTERS.forEach { level ->
                    val isSelected = selectedFilter == level
                    Surface(
                        modifier = Modifier
                            .bounceClick(scaleDown = 0.95f) {
                                selectedFilter = level
                                val apiLevelParam = if (level == "Tất cả") null else level
                                onFilterLevel(apiLevelParam)
                            },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) BrandIndigo else Color(0xFFF1F5F9),
                        border = if (isSelected) null else BorderStroke(0.5.dp, BorderSubtle)
                    ) {
                        Text(
                            text = level,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextMuted,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            // ── 3. SEARCH BAR ──
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Tìm cấu trúc, mẫu câu, từ khóa...",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        color = TextMuted
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBg,
                    unfocusedContainerColor = CardBg,
                    focusedBorderColor = BrandIndigoLight,
                    unfocusedBorderColor = BorderSubtle
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // ── 4. CONTENT LIST ──
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = BrandIndigo,
                        strokeWidth = 2.5.dp,
                        modifier = Modifier.size(32.dp)
                    )
                }
            } else if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(text = "📚", fontSize = 38.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Không tìm thấy cấu trúc ngữ pháp phù hợp",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMain
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Hãy thử tìm bằng từ khóa khác hoặc chuyển cấp độ HSK.",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredList) { item ->
                        HskGrammarItemCard(
                            grammar = item,
                            onSpeak = onSpeak
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }
            }
        }
    }
}

/**
 * Thẻ hiển thị một điểm ngữ pháp HSK chi tiết.
 */
@Composable
private fun HskGrammarItemCard(
    grammar: GrammarStructureDto,
    onSpeak: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CardBg,
        border = BorderStroke(0.6.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Tiêu đề
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = grammar.title,
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = "#${grammar.structureOrder}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Công thức (Formula)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFEEF2FF),
                border = BorderStroke(0.5.dp, Color(0xFFC7D2FE))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📐",
                        fontSize = 13.sp,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = grammar.formula,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandIndigo
                    )
                }
            }

            // Giải thích chi tiết
            if (grammar.explanation.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = grammar.explanation,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            }

            // Mẹo thi HSK (Exam Tip)
            if (!grammar.examTip.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = TipAmberBg,
                    border = BorderStroke(0.5.dp, TipAmberBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "💡",
                            fontSize = 13.sp,
                            modifier = Modifier.padding(end = 6.dp, top = 1.dp)
                        )
                        Text(
                            text = "Mẹo thi HSK: ${grammar.examTip}",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = TipAmberText,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Danh sách câu ví dụ
            if (grammar.examples.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Ví dụ câu thực tế:",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                Spacer(modifier = Modifier.height(6.dp))

                grammar.examples.forEach { example ->
                    GrammarExampleItem(example = example, onSpeak = onSpeak)
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

/**
 * Một dòng ví dụ câu kèm phát âm TTS và cảnh báo lỗi sai nếu có.
 */
@Composable
private fun GrammarExampleItem(
    example: GrammarExampleDto,
    onSpeak: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = BorderStroke(0.5.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = example.hanzi,
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${example.pinyinActual.ifBlank { example.pinyinOriginal }} — ${example.meaning}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        color = TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEF2FF))
                        .border(BorderStroke(0.5.dp, BrandIndigoLight.copy(alpha = 0.3f)), CircleShape)
                        .clickable { onSpeak(example.audioText ?: example.hanzi) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🔊", fontSize = 12.sp)
                }
            }

            if (!example.warning.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⚠️", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = example.warning,
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.5.sp,
                        color = Color(0xFFDC2626)
                    )
                }
            }
        }
    }
}
