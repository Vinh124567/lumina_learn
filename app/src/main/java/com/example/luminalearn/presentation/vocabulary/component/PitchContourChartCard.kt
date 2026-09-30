package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.core.util.SpeechRecognitionState
import com.example.luminalearn.presentation.vocabulary.model.SyllableToneInfo
import com.example.luminalearn.presentation.vocabulary.model.VocabColors

@Composable
fun PitchContourChartCard(
    syllables: List<SyllableToneInfo>,
    speechSuccess: SpeechRecognitionState.Success?,
    isListening: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, VocabColors.BorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Title & Legend theo đúng ảnh thiết kế
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Thang Ngũ Độ (Chao 1–5)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp, 2.5.dp)
                                .background(Color(0xFF4F46E5), RoundedCornerShape(1.dp))
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Bản xứ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4F46E5),
                            maxLines = 1,
                            softWrap = false
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val legendColor = when {
                            isListening -> VocabColors.AccentCoral
                            speechSuccess == null -> Color(0xFF94A3B8)
                            speechSuccess.score >= 80 -> Color(0xFF00B67A)
                            speechSuccess.score >= 60 -> Color(0xFFF59E0B)
                            else -> VocabColors.ErrorRed
                        }
                        val legendText = when {
                            isListening -> "Đang thu..."
                            speechSuccess == null -> "Chờ thu âm"
                            else -> "Của bạn"
                        }
                        Box(
                            modifier = Modifier
                                .size(12.dp, 3.5.dp)
                                .background(legendColor, RoundedCornerShape(1.5.dp))
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = legendText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = legendColor,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // Canvas vẽ đồ thị cao độ 5 mức chuẩn ảnh
            PitchChartCanvas(
                syllables = syllables,
                speechSuccess = speechSuccess,
                isListening = isListening
            )

            // Dòng hướng dẫn tinh tế khi chưa có bản thu (không đè lên canvas)
            if (speechSuccess == null && !isListening) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF94A3B8))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Chưa có bản thu • Bấm \"Bật mic\" để nói & đối chiếu cao độ",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PitchChartCanvas(
    syllables: List<SyllableToneInfo>,
    speechSuccess: SpeechRecognitionState.Success?,
    isListening: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pitchWave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )

    // Xác định tone hiệu dụng (hỗ trợ quy tắc biến điệu thanh 3: 3 + 3 -> 2 + 3)
    val effectiveTones = remember(syllables) {
        val list = if (syllables.isEmpty()) {
            listOf(2, 3) // Mặc định mẫu "你好"
        } else {
            syllables.mapIndexed { idx, s ->
                if (s.toneNumber == 3 && idx < syllables.size - 1 && syllables[idx + 1].toneNumber == 3) {
                    2
                } else {
                    s.toneNumber
                }
            }
        }
        list
    }

    val hasRecorded = speechSuccess != null || isListening
    val score = speechSuccess?.score ?: if (isListening) 80 else 0
    val curveColor = when {
        isListening -> VocabColors.AccentCoral
        score >= 80 -> Color(0xFF00B67A)
        score >= 60 -> Color(0xFFF59E0B)
        else -> VocabColors.ErrorRed
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(14.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        // ── 1. Khối Canvas vẽ Grid, Divider và các đường cong cao độ ──
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val padTop = 14f
            val padBottom = 14f
            val chartH = h - padTop - padBottom

            // 5 mốc cao độ y-coordinates
            val y5 = padTop + chartH * 0.05f  // Level 5 (Sol / Cao)
            val y4 = padTop + chartH * 0.28f  // Level 4 (Fa / Nửa cao)
            val y3 = padTop + chartH * 0.50f  // Level 3 (Mi / Trung bình - Nét đứt chính giữa)
            val y2 = padTop + chartH * 0.73f  // Level 2 (Re / Nửa thấp)
            val y1 = padTop + chartH * 0.95f  // Level 1 (Do / Thấp nhất)

            // Vẽ 5 đường kẻ ngang mốc cao độ
            drawLine(Color(0xFFF1F5F9), Offset(0f, y5), Offset(w, y5), strokeWidth = 1.2f)
            drawLine(Color(0xFFF1F5F9), Offset(0f, y4), Offset(w, y4), strokeWidth = 1.2f)
            drawLine(Color(0xFFF1F5F9), Offset(0f, y2), Offset(w, y2), strokeWidth = 1.2f)
            drawLine(Color(0xFFF1F5F9), Offset(0f, y1), Offset(w, y1), strokeWidth = 1.2f)

            // Level 3: Nét đứt làm mốc trung tâm
            drawLine(
                color = Color(0xFFCBD5E1),
                start = Offset(0f, y3),
                end = Offset(w, y3),
                strokeWidth = 1.2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
            )

            // Vẽ đường phân cách âm tiết (Vertical dashed divider)
            val segmentCount = maxOf(1, effectiveTones.size)
            for (s in 1 until segmentCount) {
                val divX = w * s / segmentCount
                drawLine(
                    color = Color(0xFFCBD5E1),
                    start = Offset(divX, 0f),
                    end = Offset(divX, h),
                    strokeWidth = 1.2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                )
            }

            // ── 2. Xây dựng đường Pitch chuẩn bản xứ và đường người học ──
            val nativePath = Path()
            val userPath = Path()
            val userFillPath = Path()
            val pillPoints = mutableListOf<Offset>()

            val jitter = if (isListening) (waveOffset * 8f - 4f) else 0f
            userFillPath.moveTo(0f, h)

            for (s in 0 until segmentCount) {
                val startX = w * s / segmentCount
                val endX = w * (s + 1) / segmentCount
                val span = endX - startX
                val tone = effectiveTones.getOrElse(s) { 1 }

                // Tính toán điểm bắt đầu và kết thúc của âm tiết theo thanh điệu
                val (toneStartY, toneEndY) = when (tone) {
                    1 -> Pair(y5, y5)
                    2 -> Pair(y3, y5)
                    3 -> Pair(y2, y5)
                    4 -> Pair(y5, y1)
                    else -> Pair(y3, y2)
                }

                // Nối đường nét bản xứ (Native reference curve)
                if (s == 0) {
                    nativePath.moveTo(startX, toneStartY)
                } else {
                    nativePath.lineTo(startX, toneStartY)
                }

                when (tone) {
                    1 -> {
                        nativePath.quadraticTo(startX + span * 0.5f, y5 - 2f, endX, y5)
                    }
                    2 -> {
                        nativePath.cubicTo(
                            startX + span * 0.30f, y3 + 4f,
                            startX + span * 0.65f, y4,
                            endX, y5
                        )
                    }
                    3 -> {
                        nativePath.cubicTo(
                            startX + span * 0.25f, y1 + 5f,
                            startX + span * 0.65f, y3,
                            endX, y5
                        )
                    }
                    4 -> {
                        nativePath.cubicTo(
                            startX + span * 0.25f, y5,
                            startX + span * 0.55f, y3,
                            endX, y1
                        )
                    }
                    else -> {
                        nativePath.quadraticTo(startX + span * 0.5f, y3, endX, y2)
                    }
                }

                // Nối đường nét người học (User contour ribbon)
                if (score >= 60) {
                    val uStartY = toneStartY + jitter
                    val uEndY = toneEndY + jitter

                    if (s == 0) {
                        userPath.moveTo(startX, uStartY)
                        userFillPath.lineTo(startX, uStartY)
                    } else {
                        userPath.lineTo(startX, uStartY)
                        userFillPath.lineTo(startX, uStartY)
                        // Viên nhộng trắng định vị ngay tại bước chuyển âm
                        pillPoints.add(Offset(startX, uStartY))
                    }

                    when (tone) {
                        1 -> {
                            userPath.quadraticTo(startX + span * 0.5f, y5 - 2f + jitter, endX, uEndY)
                            userFillPath.quadraticTo(startX + span * 0.5f, y5 - 2f + jitter, endX, uEndY)

                            pillPoints.add(Offset(startX + span * 0.15f, y5 + jitter))
                            pillPoints.add(Offset(startX + span * 0.50f, y5 - 2f + jitter))
                            pillPoints.add(Offset(startX + span * 0.85f, y5 + jitter))
                        }
                        2 -> {
                            userPath.cubicTo(
                                startX + span * 0.30f, y3 + 4f + jitter,
                                startX + span * 0.65f, y4 + jitter,
                                endX, uEndY
                            )
                            userFillPath.cubicTo(
                                startX + span * 0.30f, y3 + 4f + jitter,
                                startX + span * 0.65f, y4 + jitter,
                                endX, uEndY
                            )

                            pillPoints.add(Offset(startX + span * 0.10f, y3 + jitter))
                            pillPoints.add(Offset(startX + span * 0.38f, y3 + (y4 - y3) * 0.45f + jitter))
                            pillPoints.add(Offset(startX + span * 0.70f, y4 + jitter))
                            pillPoints.add(Offset(endX - 8f, uEndY))
                        }
                        3 -> {
                            userPath.cubicTo(
                                startX + span * 0.25f, y1 + 5f + jitter,
                                startX + span * 0.65f, y3 + jitter,
                                endX, uEndY
                            )
                            userFillPath.cubicTo(
                                startX + span * 0.25f, y1 + 5f + jitter,
                                startX + span * 0.65f, y3 + jitter,
                                endX, uEndY
                            )

                            pillPoints.add(Offset(startX + span * 0.10f, y2 + jitter))
                            pillPoints.add(Offset(startX + span * 0.35f, y1 + 5f + jitter))
                            pillPoints.add(Offset(startX + span * 0.70f, y3 + jitter))
                            pillPoints.add(Offset(endX - 8f, uEndY))
                        }
                        4 -> {
                            // Đường lượn rơi mạnh mẽ chuẩn thanh 4 (51)
                            userPath.cubicTo(
                                startX + span * 0.25f, y5 + jitter,
                                startX + span * 0.55f, y3 + jitter,
                                endX, uEndY
                            )
                            userFillPath.cubicTo(
                                startX + span * 0.25f, y5 + jitter,
                                startX + span * 0.55f, y3 + jitter,
                                endX, uEndY
                            )

                            pillPoints.add(Offset(startX + span * 0.10f, y5 + jitter))
                            pillPoints.add(Offset(startX + span * 0.38f, y4 + jitter))
                            pillPoints.add(Offset(startX + span * 0.68f, y2 + jitter))
                            pillPoints.add(Offset(endX - 8f, uEndY))
                        }
                        else -> {
                            userPath.quadraticTo(startX + span * 0.5f, y3 + jitter, endX, uEndY)
                            userFillPath.quadraticTo(startX + span * 0.5f, y3 + jitter, endX, uEndY)

                            pillPoints.add(Offset(startX + span * 0.30f, y3 + jitter))
                            pillPoints.add(Offset(startX + span * 0.70f, y2 + jitter))
                        }
                    }
                } else {
                    // Khi đọc sai (score < 60): Đường cao độ bị lệch ngược hướng
                    val uStartY = when (tone) {
                        4 -> y2 + jitter
                        2 -> y4 + jitter
                        else -> y3 + jitter
                    }
                    val uEndY = when (tone) {
                        4 -> y4 + jitter
                        2 -> y1 + jitter
                        else -> y2 + jitter
                    }

                    if (s == 0) {
                        userPath.moveTo(startX, uStartY)
                        userFillPath.lineTo(startX, uStartY)
                    } else {
                        userPath.lineTo(startX, uStartY)
                        userFillPath.lineTo(startX, uStartY)
                    }
                    userPath.quadraticTo(startX + span * 0.5f, (uStartY + uEndY) / 2f, endX, uEndY)
                    userFillPath.quadraticTo(startX + span * 0.5f, (uStartY + uEndY) / 2f, endX, uEndY)

                    pillPoints.add(Offset(startX + span * 0.3f, uStartY))
                    pillPoints.add(Offset(startX + span * 0.7f, uEndY))
                }
            }

            if (hasRecorded) {
                userFillPath.lineTo(w, h)
                userFillPath.close()

                // ── 3. Gradient bóng mềm mại từ đường vẽ xuống đáy ──
                drawPath(
                    path = userFillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            curveColor.copy(alpha = 0.38f),
                            curveColor.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )

                // ── 4. Dải nét cao độ người học dày 13f bo tròn ──
                drawPath(
                    path = userPath,
                    color = curveColor,
                    style = Stroke(width = 13f, cap = StrokeCap.Round)
                )
            }

            // ── 5. Đường chuẩn bản xứ nét đứt màu xanh tím #4F46E5 vẽ phủ trên để đối chiếu ──
            drawPath(
                path = nativePath,
                color = Color(0xFF4F46E5),
                style = Stroke(
                    width = 3.5f,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))
                )
            )

            // ── 6. Viên nhộng trắng định vị trên đường cong ──
            if (hasRecorded && speechSuccess != null) {
                val pillW = 15f
                val pillH = 6.5f
                val pillR = 3.25f
                pillPoints.forEach { pt ->
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(pt.x - pillW / 2f, pt.y - pillH / 2f),
                        size = Size(pillW, pillH),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(pillR, pillR)
                    )
                }
            }
        }

        // ── 3. Lớp nhãn Y-Axis 2 bên & nhãn "Âm 2" ──
        val leftLabels = listOf(
            "5 (Cao / Sol - 高)",
            "4 (Nửa cao / Fa - 半高)",
            "3 (Trung bình / Mi - 中)",
            "2 (Nửa thấp / Re - 半低)",
            "1 (Thấp nhất / Do - 低)"
        )
        val rightLabels = listOf(
            "Level 5",
            "Level 4",
            "Level 3",
            "Level 2",
            "Level 1"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0..4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = leftLabels[i],
                        fontSize = 8.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        maxLines = 1
                    )
                    Text(
                        text = rightLabels[i],
                        fontSize = 8.sp,
                        color = Color(0xFF94A3B8),
                        maxLines = 1
                    )
                }
            }
        }

        if (effectiveTones.size >= 2) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 2.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Âm 2",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(start = 18.dp)
                )
            }
        }
    }
}
