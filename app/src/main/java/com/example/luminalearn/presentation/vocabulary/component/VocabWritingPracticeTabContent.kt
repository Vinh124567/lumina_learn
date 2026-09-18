package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem

private data class DrawingStroke(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun VocabWritingPracticeTabContent(
    word: VocabWordItem,
    modifier: Modifier = Modifier
) {
    var showHint by remember { mutableStateOf(true) }
    var selectedColor by remember { mutableStateOf(Color(0xFF1E293B)) }
    var strokes by remember { mutableStateOf(listOf<DrawingStroke>()) }
    var currentPoints by remember { mutableStateOf(listOf<Offset>()) }

    var selectedCharIndex by remember(word.hanzi) { mutableIntStateOf(0) }
    val safeCharIndex = selectedCharIndex.coerceIn(0, maxOf(0, word.hanzi.length - 1))
    val currentChar = word.hanzi.getOrNull(safeCharIndex)?.toString() ?: word.hanzi

    val inkColors = listOf(
        Color(0xFF1E293B), // Đen mực tàu
        Color(0xFFDC2626), // Đỏ son
        Color(0xFF4F46E5)  // Xanh chàm / Tím
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── 1. Header: Tiêu đề + 2 Nút (Nét mờ & Xóa viết lại) ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Luyện viết Hán tự trên ô Mễ tự cách (米字格)",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Dùng ngón tay hoặc chuột để viết theo chuẩn quy tắc nét bút tiếng Trung.",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Nút bật/tắt nét mờ
                Surface(
                    modifier = Modifier.clickable { showHint = !showHint },
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6366F1))
                ) {
                    Text(
                        text = if (showHint) "👁 Nét mờ: BẬT" else "👁 Nét mờ: TẮT",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6366F1),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }

                // Nút Xóa viết lại
                Surface(
                    modifier = Modifier.clickable {
                        strokes = emptyList()
                        currentPoints = emptyList()
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6366F1))
                ) {
                    Text(
                        text = "🔄 Xóa viết lại",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6366F1),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bộ chọn chữ Hán khi từ có nhiều chữ (ví dụ: 对不起 -> chọn [对] [不] [起])
        if (word.hanzi.length > 1) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                word.hanzi.forEachIndexed { index, ch ->
                    val isSelected = index == safeCharIndex
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFF6366F1) else Color(0xFFF1F5F9))
                            .clickable {
                                selectedCharIndex = index
                                strokes = emptyList()
                                currentPoints = emptyList()
                            }
                    ) {
                        Text(
                            text = ch.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF334155)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // ── 2. Khung ô Mễ tự cách (米字格) tương tác vẽ ──
        Box(
            modifier = Modifier
                .size(260.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(2.dp, Color(0xFFEF4444), RoundedCornerShape(16.dp))
                .padding(4.dp)
                .border(0.8.dp, Color(0xFFEF4444).copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Lớp 1: Lưới Mễ tự (米字格) nét đứt màu đỏ nhạt
            Canvas(modifier = Modifier.fillMaxSize()) {
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                val gridColor = Color(0xFFFCA5A5).copy(alpha = 0.7f)

                // Đường ngang giữa
                drawLine(
                    color = gridColor,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    pathEffect = dashEffect,
                    strokeWidth = 1.5f
                )
                // Đường dọc giữa
                drawLine(
                    color = gridColor,
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height),
                    pathEffect = dashEffect,
                    strokeWidth = 1.5f
                )
                // Hai đường chéo góc
                drawLine(
                    color = gridColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height),
                    pathEffect = dashEffect,
                    strokeWidth = 1.2f
                )
                drawLine(
                    color = gridColor,
                    start = Offset(size.width, 0f),
                    end = Offset(0f, size.height),
                    pathEffect = dashEffect,
                    strokeWidth = 1.2f
                )
            }

            // Lớp 2: Chữ Hán mờ gợi ý (khi BẬT)
            if (showHint) {
                Text(
                    text = currentChar,
                    fontSize = 120.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFCBD5E1).copy(alpha = 0.65f),
                    textAlign = TextAlign.Center
                )
            }

            // Lớp 3: Canvas nhận cử chỉ vuốt vẽ nét bút của người dùng
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(selectedColor) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPoints = listOf(offset)
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                currentPoints = currentPoints + change.position
                            },
                            onDragEnd = {
                                if (currentPoints.isNotEmpty()) {
                                    strokes = strokes + DrawingStroke(currentPoints, selectedColor, 12f)
                                    currentPoints = emptyList()
                                }
                            },
                            onDragCancel = {
                                currentPoints = emptyList()
                            }
                        )
                    }
            ) {
                // Vẽ các nét đã hoàn thành
                strokes.forEach { stroke ->
                    if (stroke.points.size > 1) {
                        val path = Path().apply {
                            moveTo(stroke.points.first().x, stroke.points.first().y)
                            for (i in 1 until stroke.points.size) {
                                lineTo(stroke.points[i].x, stroke.points[i].y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = stroke.color,
                            style = Stroke(
                                width = stroke.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    } else if (stroke.points.size == 1) {
                        drawCircle(
                            color = stroke.color,
                            radius = stroke.strokeWidth / 2,
                            center = stroke.points.first()
                        )
                    }
                }

                // Vẽ nét đang kéo dở
                if (currentPoints.size > 1) {
                    val path = Path().apply {
                        moveTo(currentPoints.first().x, currentPoints.first().y)
                        for (i in 1 until currentPoints.size) {
                            lineTo(currentPoints[i].x, currentPoints[i].y)
                        }
                    }
                    drawPath(
                        path = path,
                        color = selectedColor,
                        style = Stroke(
                            width = 12f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                } else if (currentPoints.size == 1) {
                    drawCircle(
                        color = selectedColor,
                        radius = 6f,
                        center = currentPoints.first()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── 3. Thanh chọn Màu mực ──
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Màu mực:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B)
            )

            inkColors.forEach { color ->
                val isSelected = selectedColor == color
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(color)
                        .then(
                            if (isSelected) {
                                Modifier.border(2.5.dp, Color(0xFF6366F1), CircleShape)
                            } else {
                                Modifier.border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            }
                        )
                        .clickable { selectedColor = color }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── 4. Card Quy tắc nét bút chuẩn phía dưới ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF8FAFC))
                .border(1.dp, Color(0xFFEEF2FF), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sparkle),
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Quy tắc nét bút chuẩn của chữ \"${word.hanzi}\":",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4338CA)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Quy tắc vàng: Viết từ trên xuống dưới, từ trái qua phải, ngang trước sổ sau, giữa trước hai bên sau.",
                    fontSize = 11.5.sp,
                    color = Color(0xFF475569),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Tổng số nét: ${word.strokes} • ${word.radical}",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
