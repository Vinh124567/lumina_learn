package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.data.model.AnalyticsSummaryDto
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlin.math.cos
import kotlin.math.sin

private val BrandIndigo = Color(0xFF4F46E5)
private val BrandIndigoLight = Color(0xFF6366F1)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderHairline = Color(0xFFE2E8F0)

/**
 * Modal Phân tích Kỹ năng Học viên với Biểu đồ Radar 5 Cánh & Chỉ số Thống kê.
 * Chuẩn phong cách Luxury Aesthetics: Hairline 0.5dp, Soft Elevation, Nhãn kỹ năng tương phản cao.
 */
@Composable
fun SkillRadarModal(
    analytics: AnalyticsSummaryDto,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(26.dp))
                .background(Color.White)
                .border(BorderStroke(0.5.dp, Color(0xFFC7D2FE).copy(alpha = 0.6f)), RoundedCornerShape(26.dp))
                .padding(22.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PHÂN TÍCH NĂNG LỰC 📊",
                            fontFamily = PlusJakartaSans,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = BrandIndigo
                        )
                        Text(
                            text = "Dữ liệu đo lường 5 kỹ năng thực tế",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            color = TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEEF2FF))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Chuẩn xác: ${analytics.accuracyPercentage}%",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandIndigo
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                val skillItems = listOf(
                    SkillBadgeItem("Ngữ âm", analytics.radarScores.phonetics, "🗣️"),
                    SkillBadgeItem("Từ vựng", analytics.radarScores.vocabulary, "📚"),
                    SkillBadgeItem("Ngữ pháp", analytics.radarScores.grammar, "🧩"),
                    SkillBadgeItem("Hội thoại", analytics.radarScores.dialogue, "💬"),
                    SkillBadgeItem("Độ chuẩn", analytics.radarScores.accuracy, "🎯")
                )

                // Canvas Biểu đồ Radar 5 cánh
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(180.dp)) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val maxRadius = size.width / 2.2f
                        val numSides = skillItems.size
                        val angleStep = (2.0 * Math.PI / numSides).toFloat()

                        // Vẽ các vòng lưới đa giác tỷ lệ (25%, 50%, 75%, 100%)
                        val levels = listOf(0.25f, 0.5f, 0.75f, 1f)
                        levels.forEach { levelFrac ->
                            val gridPath = Path()
                            val r = maxRadius * levelFrac
                            for (i in 0 until numSides) {
                                val angle = (i * angleStep - Math.PI / 2).toFloat()
                                val x = center.x + r * cos(angle)
                                val y = center.y + r * sin(angle)
                                if (i == 0) gridPath.moveTo(x, y) else gridPath.lineTo(x, y)
                            }
                            gridPath.close()
                            drawPath(
                                path = gridPath,
                                color = Color(0xFFE2E8F0).copy(alpha = 0.8f),
                                style = Stroke(width = 0.8.dp.toPx())
                            )
                        }

                        // Vẽ các tia trục từ tâm
                        for (i in 0 until numSides) {
                            val angle = (i * angleStep - Math.PI / 2).toFloat()
                            val x = center.x + maxRadius * cos(angle)
                            val y = center.y + maxRadius * sin(angle)
                            drawLine(
                                color = Color(0xFFCBD5E1).copy(alpha = 0.7f),
                                start = center,
                                end = Offset(x, y),
                                strokeWidth = 0.8.dp.toPx()
                            )
                        }

                        // Vẽ đa giác vùng kỹ năng của học viên
                        val dataPath = Path()
                        skillItems.forEachIndexed { i, item ->
                            val scoreFraction = (item.score.coerceIn(10, 100) / 100f)
                            val r = maxRadius * scoreFraction
                            val angle = (i * angleStep - Math.PI / 2).toFloat()
                            val x = center.x + r * cos(angle)
                            val y = center.y + r * sin(angle)
                            if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
                        }
                        dataPath.close()

                        // Tô màu diện tích kỹ năng với gradient alpha mềm mại
                        drawPath(
                            path = dataPath,
                            color = BrandIndigoLight.copy(alpha = 0.32f)
                        )

                        // Viền ngoài của diện tích kỹ năng
                        drawPath(
                            path = dataPath,
                            color = BrandIndigo,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Vẽ các điểm nút tròn
                        skillItems.forEachIndexed { i, item ->
                            val scoreFraction = (item.score.coerceIn(10, 100) / 100f)
                            val r = maxRadius * scoreFraction
                            val angle = (i * angleStep - Math.PI / 2).toFloat()
                            val x = center.x + r * cos(angle)
                            val y = center.y + r * sin(angle)
                            drawCircle(color = BrandIndigo, radius = 3.5.dp.toPx(), center = Offset(x, y))
                            drawCircle(color = Color.White, radius = 1.8.dp.toPx(), center = Offset(x, y))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Dải thẻ đo 5 kỹ năng chi tiết với điểm số trực quan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    skillItems.forEach { item ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(0.5.dp, BorderHairline.copy(alpha = 0.5f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)
                            ) {
                                Text(text = item.icon, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${item.score}",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = BrandIndigo
                                )
                                Text(
                                    text = item.name,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 9.sp,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Các thẻ KPI thống kê tổng hợp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Đã học",
                        value = "${analytics.totalLessonsCompleted}",
                        sub = "Bài học",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Tổng sao",
                        value = "${analytics.totalStars}",
                        sub = "Ngôi sao ⭐",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Từ vựng",
                        value = "${analytics.masteredVocabsCount}",
                        sub = "Từ nắm vững",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Nút Đóng
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BrandIndigo,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .bounceClick { onDismiss() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "ĐÃ HIỂU",
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

private data class SkillBadgeItem(
    val name: String,
    val score: Int,
    val icon: String
)

@Composable
private fun StatCard(
    title: String,
    value: String,
    sub: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(0.5.dp, BorderHairline.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp)
        ) {
            Text(text = title, fontFamily = PlusJakartaSans, fontSize = 11.sp, color = TextMuted)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontFamily = PlusJakartaSans, fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextMain)
            Text(text = sub, fontFamily = PlusJakartaSans, fontSize = 10.sp, color = TextMuted)
        }
    }
}
