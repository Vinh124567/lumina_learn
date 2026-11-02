package com.example.luminalearn.presentation.lesson.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.data.model.GrammarExampleDto
import com.example.luminalearn.data.model.GrammarStructureDto
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val HSK_LEVEL_FILTERS = listOf("Tất cả", "HSK 1", "HSK 2", "HSK 3", "HSK 4", "HSK 5", "HSK 6")

/**
 * Modal Tra cứu Cẩm nang Ngữ pháp chuẩn hóa theo khung HSK 1 - HSK 6.
 * Phong cách Retro 16-Bit Pixel Arcade Console: Viền đen 2dp, bóng cứng 3D, thẻ dập nổi.
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

    LaunchedEffect(Unit) {
        if (grammarList.isEmpty()) {
            onFilterLevel(null)
        }
    }

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
        containerColor = PixelColors.SurfaceLight,
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(horizontal = 16.dp)
        ) {
            // ── 1. HEADER MODAL PIXEL CARTRIDGE ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📜", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "KHO NGỮ PHÁP HSK",
                            fontFamily = PlusJakartaSans,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.TextDark,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "Quy chuẩn ngữ pháp HSK 1 đến HSK 6 chuẩn thi quốc tế",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(PixelColors.SurfaceDark)
                        .border(1.5.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                        .bounceClick { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            // ── 2. FILTER PILLS HSK PIXEL ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                HSK_LEVEL_FILTERS.forEach { level ->
                    val isSelected = selectedFilter == level
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(PixelColors.ShadowDark)
                            .padding(bottom = if (isSelected) 1.dp else 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) PixelColors.ArcadePurple else Color.White)
                                .border(1.5.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                                .clickable {
                                    selectedFilter = level
                                    val apiLevelParam = if (level == "Tất cả") null else level
                                    onFilterLevel(apiLevelParam)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = level,
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) Color.White else PixelColors.TextDark
                            )
                        }
                    }
                }
            }

            // ── 3. SEARCH BAR PIXEL CONSOLE ──
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Tìm cấu trúc, mẫu câu, từ khóa...",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PixelColors.TextMuted
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = PixelColors.BorderDark,
                    unfocusedBorderColor = PixelColors.BorderDark,
                    focusedTextColor = PixelColors.TextDark,
                    unfocusedTextColor = PixelColors.TextDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.5.dp, PixelColors.BorderDark, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ── 4. CONTENT LIST PIXEL ──
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = PixelColors.ArcadePurple,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(36.dp)
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
                        Text(text = "📚", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Không tìm thấy cấu trúc ngữ pháp phù hợp",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixelColors.TextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Hãy thử tìm bằng từ khóa khác hoặc chuyển cấp độ HSK.",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = PixelColors.TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
 * Thẻ hiển thị một điểm ngữ pháp HSK chi tiết dạng Pixel Arcade CardBox.
 */
@Composable
private fun HskGrammarItemCard(
    grammar: GrammarStructureDto,
    onSpeak: (String) -> Unit
) {
    PixelCardBox(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        borderColor = PixelColors.BorderDark,
        shadowColor = PixelColors.ShadowDark,
        cornerRadius = 10.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Tiêu đề và mã số thứ tự
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = grammar.title,
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.TextDark,
                    modifier = Modifier.weight(1f)
                )

                PixelBadge(
                    text = "#${grammar.structureOrder}",
                    color = PixelColors.SurfaceDark,
                    textColor = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Công thức (Formula) dạng Box dập nổi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFFAF5FF))
                    .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📐", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = grammar.formula,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.ArcadePurple
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
                    color = PixelColors.TextDark,
                    lineHeight = 17.sp
                )
            }

            // Mẹo thi HSK (Exam Tip)
            if (!grammar.examTip.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFEF3C7))
                        .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(text = "💡", fontSize = 12.sp, modifier = Modifier.padding(top = 1.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mẹo thi HSK: ${grammar.examTip}",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Danh sách câu ví dụ
            if (grammar.examples.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "VÍ DỤ CÂU THỰC TẾ:",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.TextMuted,
                    letterSpacing = 0.5.sp
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
 * Một dòng ví dụ câu kèm phát âm TTS và cảnh báo lỗi sai nếu có dạng Pixel Card.
 */
@Composable
private fun GrammarExampleItem(
    example: GrammarExampleDto,
    onSpeak: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = example.hanzi,
                        fontFamily = PlusJakartaSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.TextDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${example.pinyinActual.ifBlank { example.pinyinOriginal }} — ${example.meaning}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = PixelColors.TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(PixelColors.SurfaceDark)
                        .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                        .bounceClick { onSpeak(example.audioText ?: example.hanzi) },
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
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.ArcadeRed
                    )
                }
            }
        }
    }
}
