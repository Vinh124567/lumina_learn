package com.example.luminalearn.presentation.lesson.component

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.ui.theme.PlusJakartaSans

@Composable
fun LessonComprehensiveTabContent(
    lesson: ChineseLessonData,
    onSpeak: (String) -> Unit,
    onNavigateToVocab: () -> Unit = {},
    onNavigateToQuiz: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ── 1. Khung Mục tiêu bài học (Khung chuẩn Cơ bản) - RÚT GỌN GỌN GÀNG ──
        LessonObjectivesCompactCard(objectives = lesson.objectives)

        // ── 2. Banner Tổng quan chuyên đề ──
        LessonTopicOverviewBanner(
            title = lesson.title,
            description = lesson.description
        )

        // ── 3. Khối Điểm ngữ pháp & Cấu trúc cốt lõi ──
        LessonGrammarStructuresSection(
            structures = lesson.grammarStructures,
            onSpeak = onSpeak
        )

        // ── 4. Hai nút điều hướng nhanh ở cuối trang ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Nút viền tím bo tròn pill: Học từ vựng bài này
            Surface(
                onClick = onNavigateToVocab,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(50),
                color = Color.White,
                border = BorderStroke(1.2.dp, Color(0xFF5C50F6))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Học từ vựng bài này",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5C50F6)
                    )
                }
            }

            // Nút nền tím bo tròn pill: Làm bài tập củng cố
            Surface(
                onClick = onNavigateToQuiz,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(50),
                color = Color(0xFF5C50F6),
                shadowElevation = 1.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Làm bài tập củng cố",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Khung Mục tiêu bài học (Khung chuẩn Cơ bản)
 * Rút gọn diện tích: Thiết kế 2 hàng gọn gàng, hỗ trợ thu gọn/mở rộng.
 */
@Composable
private fun LessonObjectivesCompactCard(
    objectives: List<String>
) {
    if (objectives.isEmpty()) return

    var isExpanded by remember { mutableStateOf(false) }
    val visibleItems = if (isExpanded) objectives else objectives.take(2)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ObjectivesHeader(
                isExpanded = isExpanded,
                totalCount = objectives.size,
                onToggle = { isExpanded = !isExpanded }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                visibleItems.forEach { obj ->
                    ObjectiveRowItem(text = obj, isExpanded = isExpanded)
                }
            }
        }
    }
}

@Composable
private fun ObjectivesHeader(
    isExpanded: Boolean,
    totalCount: Int,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🎯", fontSize = 13.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "MỤC TIÊU BÀI HỌC (KHUNG CHUẨN CƠ BẢN)",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4338CA),
                letterSpacing = 0.2.sp
            )
        }

        val toggleText = if (isExpanded) "Thu gọn ▲" else "Xem đủ ($totalCount) ▼"
        Text(
            text = toggleText,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6366F1)
        )
    }
}

@Composable
private fun ObjectiveRowItem(
    text: String,
    isExpanded: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "✓",
            color = Color(0xFF16A34A),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 1.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 11.5.sp,
            color = Color(0xFF334155),
            lineHeight = 16.sp,
            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Banner Tổng quan chuyên đề
 */
@Composable
private fun LessonTopicOverviewBanner(
    title: String,
    description: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF5538EE), Color(0xFF4338CA))
                )
            )
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "TỔNG QUAN CHUYÊN ĐỀ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.4.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 16.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description.ifBlank { "Nắm chắc 4 thanh điệu tiếng Trung và quy tắc biến điệu hai thanh 3 kinh điển giúp nói tự nhiên như người bản xứ." },
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 17.sp
            )
        }
    }
}

/**
 * Khối Điểm ngữ pháp & Cấu trúc cốt lõi
 */
@Composable
private fun LessonGrammarStructuresSection(
    structures: List<GrammarStructureData>,
    onSpeak: (String) -> Unit
) {
    if (structures.isEmpty()) return
    val displayStructures = structures

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Tiêu đề khối
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "📖", fontSize = 13.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Điểm ngữ pháp & Cấu trúc cốt lõi",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            Text(
                text = "${displayStructures.size} cấu trúc",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B),
                maxLines = 1
            )
        }

        // Render từng card Cấu trúc
        displayStructures.forEach { structure ->
            GrammarStructureCard(
                structure = structure,
                onSpeak = onSpeak
            )
        }
    }
}

@Composable
private fun GrammarStructureCard(
    structure: GrammarStructureData,
    onSpeak: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Tag CẤU TRÚC #X
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFEEF2FF))
                    .border(1.dp, Color(0xFFC7D2FE), RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
            ) {
                Text(
                    text = "CẤU TRÚC #${structure.structureOrder}",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F46E5)
                )
            }

            // Tên cấu trúc
            Text(
                text = structure.title,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            // Box Công thức
            if (structure.formula.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFAF5FF))
                        .border(1.dp, Color(0xFFE9D5FF), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color(0xFF5538EE))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Công thức",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = structure.formula,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4338CA),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Giải thích
            if (structure.explanation.isNotBlank()) {
                Text(
                    text = structure.explanation,
                    fontSize = 12.sp,
                    color = Color(0xFF475569),
                    lineHeight = 17.sp
                )
            }

            // Ví dụ phân tích
            if (structure.examples.isNotEmpty()) {
                Text(
                    text = "Ví dụ phân tích:",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )

                structure.examples.forEach { eg ->
                    GrammarExampleItemCard(example = eg, onSpeak = onSpeak)
                }
            }

            // Chiến thuật phòng thi HSK
            if (!structure.examTip.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFFFBEB))
                        .border(1.dp, Color(0xFFFEF3C7), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(text = "⚠️", fontSize = 11.sp, modifier = Modifier.padding(top = 1.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Chiến thuật phòng thi HSK:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = structure.examTip,
                                fontSize = 11.sp,
                                color = Color(0xFF78350F),
                                lineHeight = 15.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GrammarExampleItemCard(
    example: GrammarExampleData,
    onSpeak: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = example.hanzi,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                // Nút loa phát âm
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                        .clickable { onSpeak(example.audioText ?: example.hanzi) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_speaker),
                        contentDescription = "Phát âm",
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            if (example.pinyinActual.isNotBlank()) {
                Text(
                    text = example.pinyinActual,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F46E5)
                )
            }

            if (example.meaning.isNotBlank()) {
                Text(
                    text = example.meaning,
                    fontSize = 11.5.sp,
                    color = Color(0xFF64748B)
                )
            }

            // Tip phát âm
            if (!example.tip.isNullOrBlank()) {
                Text(
                    text = example.tip,
                    fontSize = 11.sp,
                    color = Color(0xFFB45309),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 15.sp
                )
            }

            // Cảnh báo lỗi sai thường gặp
            if (!example.warning.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFEF2F2))
                        .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = example.warning,
                        fontSize = 10.5.sp,
                        color = Color(0xFFDC2626),
                        lineHeight = 14.5.sp
                    )
                }
            }
        }
    }
}
