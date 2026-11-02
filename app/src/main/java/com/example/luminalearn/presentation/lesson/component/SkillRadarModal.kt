package com.example.luminalearn.presentation.lesson.component

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.data.model.AnalyticsSummaryDto
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlin.math.cos
import kotlin.math.sin

/**
 * Modal Phân tích Kỹ năng Học viên với Biểu đồ Radar 5 Cánh & Chỉ số Thống kê.
 * Phong cách Retro 16-Bit Pixel Arcade Console: Viền đen 2dp, bóng cứng 3D, dập nổi phím bấm.
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
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            PixelCardBox(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                headerTitle = "📊 RADAR SKILLS • PHÂN TÍCH",
                headerColor = PixelColors.ArcadeBlue
            ) {
                // Nút đóng pixel X ở góc trên
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .offset(y = (-4).dp)
                            .size(28.dp)
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

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Header phụ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "NĂNG LỰC THỰC CHIẾN",
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.TextDark
                            )
                            Text(
                                text = "Đo lường 5 chỉ số RPG học tập",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                color = PixelColors.TextMuted
                            )
                        }

                        PixelBadge(
                            text = "CHUẨN: ${analytics.accuracyPercentage}%",
                            color = PixelColors.ArcadeGreen,
                            textColor = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val skillItems = listOf(
                        SkillBadgeItem("Ngữ âm", analytics.radarScores.phonetics, "🗣️"),
                        SkillBadgeItem("Từ vựng", analytics.radarScores.vocabulary, "📚"),
                        SkillBadgeItem("Ngữ pháp", analytics.radarScores.grammar, "🧩"),
                        SkillBadgeItem("Hội thoại", analytics.radarScores.dialogue, "💬"),
                        SkillBadgeItem("Độ chuẩn", analytics.radarScores.accuracy, "🎯")
                    )

                    // Canvas Biểu đồ Radar 5 cánh Pixel
                    Box(
                        modifier = Modifier
                            .size(190.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(170.dp)) {
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
                                    color = PixelColors.BorderDark.copy(alpha = 0.2f),
                                    style = Stroke(width = 1.2.dp.toPx())
                                )
                            }

                            // Vẽ các tia trục từ tâm
                            for (i in 0 until numSides) {
                                val angle = (i * angleStep - Math.PI / 2).toFloat()
                                val x = center.x + maxRadius * cos(angle)
                                val y = center.y + maxRadius * sin(angle)
                                drawLine(
                                    color = PixelColors.BorderDark.copy(alpha = 0.25f),
                                    start = center,
                                    end = Offset(x, y),
                                    strokeWidth = 1.2.dp.toPx()
                                )
                            }

                            // Vẽ đa giác vùng kỹ năng
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

                            // Tô màu diện tích kỹ năng
                            drawPath(
                                path = dataPath,
                                color = PixelColors.ArcadeBlue.copy(alpha = 0.35f)
                            )

                            // Viền ngoài của diện tích kỹ năng
                            drawPath(
                                path = dataPath,
                                color = PixelColors.ArcadeBlue,
                                style = Stroke(width = 2.dp.toPx())
                            )

                            // Vẽ các điểm mút nút pixel vuông/tròn
                            skillItems.forEachIndexed { i, item ->
                                val scoreFraction = (item.score.coerceIn(10, 100) / 100f)
                                val r = maxRadius * scoreFraction
                                val angle = (i * angleStep - Math.PI / 2).toFloat()
                                val x = center.x + r * cos(angle)
                                val y = center.y + r * sin(angle)
                                drawCircle(color = PixelColors.BorderDark, radius = 4.dp.toPx(), center = Offset(x, y))
                                drawCircle(color = PixelColors.ArcadeAmber, radius = 2.5.dp.toPx(), center = Offset(x, y))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Dải thẻ 5 kỹ năng chi tiết dạng Arcade Box
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        skillItems.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PixelColors.ShadowDark)
                                    .padding(bottom = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                                        .padding(vertical = 5.dp, horizontal = 2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = item.icon, fontSize = 11.sp)
                                        Spacer(modifier = Modifier.height(1.dp))
                                        Text(
                                            text = "${item.score}",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PixelColors.ArcadeBlue
                                        )
                                        Text(
                                            text = item.name,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 8.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PixelColors.TextMuted,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Các thẻ KPI thống kê tổng hợp Pixel
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PixelStatCard(
                            title = "ĐÃ HỌC",
                            value = "${analytics.totalLessonsCompleted}",
                            sub = "Bài hoàn thành",
                            modifier = Modifier.weight(1f)
                        )
                        PixelStatCard(
                            title = "TỔNG SAO",
                            value = "${analytics.totalStars}",
                            sub = "Sao ⭐",
                            modifier = Modifier.weight(1f)
                        )
                        PixelStatCard(
                            title = "TỪ VỰNG",
                            value = "${analytics.masteredVocabsCount}",
                            sub = "Từ nắm vững",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    PixelArcadeButton(
                        text = "ĐÃ HIỂU ▶",
                        onClick = onDismiss,
                        color = PixelColors.ArcadeBlue,
                        height = 46.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
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
private fun PixelStatCard(
    title: String,
    value: String,
    sub: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PixelColors.ShadowDark)
            .padding(bottom = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFF8FAFC))
                .border(1.2.dp, PixelColors.BorderDark, RoundedCornerShape(6.dp))
                .padding(vertical = 8.dp, horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    fontFamily = PlusJakartaSans,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.TextMuted
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    fontFamily = PlusJakartaSans,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.TextDark
                )
                Text(
                    text = sub,
                    fontFamily = PlusJakartaSans,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = PixelColors.TextMuted
                )
            }
        }
    }
}
