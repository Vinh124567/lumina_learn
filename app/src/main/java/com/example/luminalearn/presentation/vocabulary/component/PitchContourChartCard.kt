package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
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
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = Color(0x0F000000),
                ambientColor = Color(0x0A000000)
            ),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Title & Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6366F1))
                    )
                    Text(
                        text = "Thang Ngũ Độ (Chao 1–5)",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Legend Bản xứ
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp, 3.dp)
                                .clip(RoundedCornerShape(1.5.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF6366F1), Color(0xFF06B6D4))
                                    )
                                )
                        )
                        Text(
                            text = "Bản xứ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569)
                        )
                    }

                    // Legend Người học / Trạng thái
                    val userLegendColor = when {
                        isListening -> VocabColors.AccentCoral
                        speechSuccess == null -> Color(0xFF94A3B8)
                        speechSuccess.score >= 80 -> Color(0xFF10B981)
                        speechSuccess.score >= 60 -> Color(0xFFF59E0B)
                        else -> Color(0xFFEF4444)
                    }
                    val userLegendText = when {
                        isListening -> "Đang thu..."
                        speechSuccess == null -> "Chờ thu"
                        else -> "${speechSuccess.score}% Khớp"
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp, 3.dp)
                                .clip(RoundedCornerShape(1.5.dp))
                                .background(userLegendColor)
                        )
                        Text(
                            text = userLegendText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = userLegendColor
                        )
                    }
                }
            }

            // Canvas đồ thị cao độ phong cách Apple Audio / ELSA Speak
            PitchChartCanvas(
                syllables = syllables,
                speechSuccess = speechSuccess,
                isListening = isListening
            )

            // Thanh nhãn âm tiết (Pinyin & Hanzi Capsules) dưới chân biểu đồ
            if (syllables.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Khoảng đệm ứng với cột thước đo Chao bên trái (~28dp)
                    Spacer(modifier = Modifier.width(28.dp))

                    syllables.forEach { syl ->
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 6.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = syl.hanzi,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = syl.pinyin,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF6366F1)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "(${syl.toneCode})",
                                    fontSize = 9.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            }

            // Hướng dẫn nhẹ nhàng khi chưa thu âm
            if (speechSuccess == null && !isListening) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(0.5.dp, Color(0xFFF1F5F9))
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
                            text = "Bấm \"Bật mic\" để phát âm & đối chiếu độ cao thanh điệu",
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
    val infiniteTransition = rememberInfiniteTransition(label = "pitchPulse")
    val animatedPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "animatedPhase"
    )

    // Xác định tone hiệu dụng (hỗ trợ quy tắc biến điệu thanh 3: 3 + 3 -> 2 + 3)
    val effectiveTones = remember(syllables) {
        if (syllables.isEmpty()) {
            listOf(2, 3) // Mẫu mặc định "你好"
        } else {
            syllables.mapIndexed { idx, s ->
                if (s.toneNumber == 3 && idx < syllables.size - 1 && syllables[idx + 1].toneNumber == 3) {
                    2
                } else {
                    s.toneNumber
                }
            }
        }
    }

    val hasRecorded = speechSuccess != null || isListening
    val score = speechSuccess?.score ?: if (isListening) 80 else 0
    val userCurveColor = when {
        isListening -> VocabColors.AccentCoral
        score >= 80 -> Color(0xFF10B981)
        score >= 60 -> Color(0xFFF59E0B)
        else -> Color(0xFFEF4444)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color(0xFFFAFAFD), RoundedCornerShape(14.dp))
            .border(0.5.dp, Color(0xFFF1F5F9), RoundedCornerShape(14.dp))
            .padding(top = 10.dp, bottom = 10.dp, end = 12.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Cột thước đo Chao 1-5 tối giản bên trái
            Column(
                modifier = Modifier
                    .width(28.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val levels = listOf(
                    "5" to Color(0xFF6366F1),
                    "4" to Color(0xFF64748B),
                    "3" to Color(0xFF94A3B8),
                    "2" to Color(0xFF64748B),
                    "1" to Color(0xFF64748B)
                )
                levels.forEach { (lvl, col) ->
                    Text(
                        text = lvl,
                        fontSize = 10.sp,
                        fontWeight = if (lvl == "5" || lvl == "3") FontWeight.Bold else FontWeight.Medium,
                        color = col
                    )
                }
            }

            // Vùng đồ thị Canvas chính
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Tọa độ Y cho 5 mốc ngũ độ Chao (Level 5 ở trên cùng, Level 1 ở dưới cùng)
                    val y5 = h * 0.08f
                    val y4 = h * 0.29f
                    val y3 = h * 0.50f
                    val y2 = h * 0.71f
                    val y1 = h * 0.92f

                    // 1. Vẽ các đường gióng ngang mờ nhẹ
                    val gridColor = Color(0xFFE2E8F0).copy(alpha = 0.5f)
                    drawLine(gridColor, Offset(0f, y5), Offset(w, y5), strokeWidth = 1f)
                    drawLine(gridColor, Offset(0f, y4), Offset(w, y4), strokeWidth = 1f)
                    drawLine(gridColor, Offset(0f, y2), Offset(w, y2), strokeWidth = 1f)
                    drawLine(gridColor, Offset(0f, y1), Offset(w, y1), strokeWidth = 1f)

                    // Đường trung hòa Level 3 (Nét đứt mờ)
                    drawLine(
                        color = Color(0xFFCBD5E1),
                        start = Offset(0f, y3),
                        end = Offset(w, y3),
                        strokeWidth = 1.2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                    )

                    // 2. Đường phân chia giữa các âm tiết (Vertical dashed divider)
                    val segmentCount = maxOf(1, effectiveTones.size)
                    for (s in 1 until segmentCount) {
                        val divX = w * s / segmentCount
                        drawLine(
                            color = Color(0xFFE2E8F0),
                            start = Offset(divX, 0f),
                            end = Offset(divX, h),
                            strokeWidth = 1.2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                        )
                    }

                    // 3. Xây dựng đường Pitch chuẩn bản xứ và đường người học
                    val nativePath = Path()
                    val userPath = Path()
                    val userFillPath = Path()
                    val nativeGlowPoints = mutableListOf<Offset>()
                    val userGlowPoints = mutableListOf<Offset>()

                    val jitter = if (isListening) (kotlin.math.sin(animatedPhase * 2f * Math.PI.toFloat()) * 6f) else 0f
                    userFillPath.moveTo(0f, h)

                    for (s in 0 until segmentCount) {
                        val startX = w * s / segmentCount + 10f
                        val endX = w * (s + 1) / segmentCount - 10f
                        val span = endX - startX
                        val tone = effectiveTones.getOrElse(s) { 1 }

                        // Tọa độ xuất phát và đích đến theo thanh điệu
                        val (toneStartY, toneEndY) = when (tone) {
                            1 -> Pair(y5, y5)
                            2 -> Pair(y3, y5)
                            3 -> Pair(y2, y4)
                            4 -> Pair(y5, y1)
                            else -> Pair(y3, y2)
                        }

                        // Điểm neo của bản xứ
                        nativeGlowPoints.add(Offset(startX, toneStartY))
                        nativeGlowPoints.add(Offset(endX, toneEndY))

                        // Đường cong chuẩn bản xứ (Native Bezier Spline)
                        if (s == 0) {
                            nativePath.moveTo(startX, toneStartY)
                        } else {
                            nativePath.lineTo(startX, toneStartY)
                        }

                        when (tone) {
                            1 -> {
                                // Thanh 1: Ngang phẳng cao vút (55)
                                nativePath.quadraticTo(startX + span * 0.5f, y5 - 2f, endX, y5)
                            }
                            2 -> {
                                // Thanh 2: Đi lên vút từ giữa lên đỉnh (35)
                                nativePath.cubicTo(
                                    startX + span * 0.35f, y3 + 4f,
                                    startX + span * 0.65f, y4,
                                    endX, y5
                                )
                            }
                            3 -> {
                                // Thanh 3: Uốn cong trũng xuống đáy rồi vểnh lên (214)
                                nativePath.cubicTo(
                                    startX + span * 0.30f, y1 + 4f,
                                    startX + span * 0.70f, y1 + 2f,
                                    endX, y4
                                )
                            }
                            4 -> {
                                // Thanh 4: Rơi dốc dứt khoát từ 5 xuống 1 (51)
                                nativePath.cubicTo(
                                    startX + span * 0.25f, y5,
                                    startX + span * 0.60f, y3,
                                    endX, y1
                                )
                            }
                            else -> {
                                // Thanh nhẹ: Lơ lửng nhẹ nhàng
                                nativePath.quadraticTo(startX + span * 0.5f, y3, endX, y2)
                            }
                        }

                        // Đường cong người học (User Ribbon)
                        if (hasRecorded) {
                            val uStartY = toneStartY + jitter
                            val uEndY = toneEndY + jitter

                            if (s == 0) {
                                userPath.moveTo(startX, uStartY)
                                userFillPath.lineTo(startX, uStartY)
                            } else {
                                userPath.lineTo(startX, uStartY)
                                userFillPath.lineTo(startX, uStartY)
                            }

                            userGlowPoints.add(Offset(startX, uStartY))
                            userGlowPoints.add(Offset(endX, uEndY))

                            if (score >= 60 || isListening) {
                                when (tone) {
                                    1 -> {
                                        userPath.quadraticTo(startX + span * 0.5f, y5 - 2f + jitter, endX, uEndY)
                                        userFillPath.quadraticTo(startX + span * 0.5f, y5 - 2f + jitter, endX, uEndY)
                                    }
                                    2 -> {
                                        userPath.cubicTo(
                                            startX + span * 0.35f, y3 + 4f + jitter,
                                            startX + span * 0.65f, y4 + jitter,
                                            endX, uEndY
                                        )
                                        userFillPath.cubicTo(
                                            startX + span * 0.35f, y3 + 4f + jitter,
                                            startX + span * 0.65f, y4 + jitter,
                                            endX, uEndY
                                        )
                                    }
                                    3 -> {
                                        userPath.cubicTo(
                                            startX + span * 0.30f, y1 + 4f + jitter,
                                            startX + span * 0.70f, y1 + 2f + jitter,
                                            endX, uEndY
                                        )
                                        userFillPath.cubicTo(
                                            startX + span * 0.30f, y1 + 4f + jitter,
                                            startX + span * 0.70f, y1 + 2f + jitter,
                                            endX, uEndY
                                        )
                                    }
                                    4 -> {
                                        userPath.cubicTo(
                                            startX + span * 0.25f, y5 + jitter,
                                            startX + span * 0.60f, y3 + jitter,
                                            endX, uEndY
                                        )
                                        userFillPath.cubicTo(
                                            startX + span * 0.25f, y5 + jitter,
                                            startX + span * 0.60f, y3 + jitter,
                                            endX, uEndY
                                        )
                                    }
                                    else -> {
                                        userPath.quadraticTo(startX + span * 0.5f, y3 + jitter, endX, uEndY)
                                        userFillPath.quadraticTo(startX + span * 0.5f, y3 + jitter, endX, uEndY)
                                    }
                                }
                            } else {
                                // Lệch cao độ khi điểm thấp
                                val badEndY = if (tone == 4) y4 + jitter else y2 + jitter
                                userPath.quadraticTo(startX + span * 0.5f, (uStartY + badEndY) / 2f, endX, badEndY)
                                userFillPath.quadraticTo(startX + span * 0.5f, (uStartY + badEndY) / 2f, endX, badEndY)
                            }
                        }
                    }

                    // A. Vẽ vùng Area Fill gradient cho giọng người học
                    if (hasRecorded) {
                        userFillPath.lineTo(w, h)
                        userFillPath.close()

                        drawPath(
                            path = userFillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    userCurveColor.copy(alpha = 0.22f),
                                    userCurveColor.copy(alpha = 0.04f),
                                    Color.Transparent
                                )
                            )
                        )
                    }

                    // B. Vẽ đường cong Bản xứ phát sáng (Lớp Glow + Lớp Gradient chính)
                    // Glow mờ phía sau
                    drawPath(
                        path = nativePath,
                        color = Color(0xFF6366F1).copy(alpha = 0.25f),
                        style = Stroke(width = 8f, cap = StrokeCap.Round)
                    )
                    // Nét chính
                    drawPath(
                        path = nativePath,
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF6366F1), Color(0xFF06B6D4))
                        ),
                        style = Stroke(
                            width = 3.5f,
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))
                        )
                    )

                    // Glow Dots trên đường bản xứ
                    nativeGlowPoints.forEach { pt ->
                        drawCircle(
                            color = Color(0xFF6366F1),
                            radius = 3.5f,
                            center = pt
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 1.8f,
                            center = pt
                        )
                    }

                    // C. Vẽ đường người học (User Curve)
                    if (hasRecorded) {
                        // Glow mờ phía sau
                        drawPath(
                            path = userPath,
                            color = userCurveColor.copy(alpha = 0.35f),
                            style = Stroke(width = 9f, cap = StrokeCap.Round)
                        )
                        // Nét nét chính bóng bẩy
                        drawPath(
                            path = userPath,
                            color = userCurveColor,
                            style = Stroke(width = 4.5f, cap = StrokeCap.Round)
                        )

                        // Các điểm neo nổi bật như ngọc
                        userGlowPoints.forEach { pt ->
                            drawCircle(
                                color = userCurveColor,
                                radius = 4.5f,
                                center = pt
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 2.2f,
                                center = pt
                            )
                        }
                    }
                }
            }
        }
    }
}
