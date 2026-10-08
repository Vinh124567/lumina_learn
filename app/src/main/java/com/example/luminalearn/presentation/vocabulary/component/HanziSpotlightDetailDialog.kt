package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val BrandIndigo = Color(0xFF6366F1)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)

/**
 * Dialog hiển thị Chi Tiết Chiết Tự Chữ Hán chuyên sâu:
 * - Khung kẻ ô Mễ tự cách (米字格) chuẩn thư pháp truyền thống.
 * - Thông số: Bộ thủ, Số nét bút, Phiên âm, Âm Hán Việt.
 * - Giải mã chiết tự: Nguồn gốc tượng hình và triết lý nhân sinh.
 * - Từ vựng mở rộng kèm phiên âm & nghĩa tiếng Việt.
 * - Câu ví dụ thực tế trong đời sống kèm phát âm TTS.
 */
@Composable
fun HanziSpotlightDetailDialog(
    hanziData: HanziSpotlightData,
    onDismiss: () -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.86f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(0.5.dp, BorderSubtle),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ── 1. Top Header Bar (Tiêu đề & Nút đóng) ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEEF2FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "字",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandIndigo
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Chi Tiết Chiết Tự",
                                fontFamily = PlusJakartaSans,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                            Text(
                                text = "Văn hóa & Cội nguồn chữ Hán",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    // Nút đóng tròn
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .bounceClick(scaleDown = 0.88f, onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Đóng",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(BorderSubtle)
                )

                // ── 2. Nội dung chi tiết cuộn dọc mượt mà ──
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // ── Khối 1: Ô Mễ Tự Cách (米字格) & Âm đọc ──
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Khung ô Mễ tự cách kẻ nét đứt thư pháp
                        MiZiGeBox(
                            hanzi = hanziData.hanzi,
                            modifier = Modifier.size(96.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Thông số chi tiết: Pinyin, Hán Việt, Bộ thủ, Số nét
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = hanziData.pinyin,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BrandIndigo
                                )

                                // Nút phát âm loa
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEEF2FF))
                                        .bounceClick(scaleDown = 0.85f) {
                                            onSpeak(hanziData.hanzi)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_speaker),
                                        contentDescription = "Phát âm",
                                        tint = BrandIndigo,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Âm Hán Việt: ${hanziData.hanViet}",
                                fontFamily = PlusJakartaSans,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMain
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = hanziData.radical,
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = TextMuted
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Số nét bút: ${hanziData.strokeCount} nét",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.5.sp,
                                color = BrandIndigo
                            )
                        }
                    }

                    // ── Khối 2: Cấu tạo nét & Bộ phận cấu thành ──
                    if (hanziData.components.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(BorderStroke(0.5.dp, BorderSubtle), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "CẤU THÀNH TỪ CÁC BỘ PHẬN",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                hanziData.components.forEach { comp ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.White)
                                            .border(BorderStroke(0.5.dp, Color(0xFFCBD5E1)), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = comp,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextMain
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ── Khối 3: Câu chuyện chiết tự & Nguồn gốc triết lý ──
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFAF5FF))
                            .border(BorderStroke(0.5.dp, Color(0xFFE9D5FF)), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "📜 Ý NGHĨA TRIẾT LÝ CHIẾT TỰ",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7E22CE),
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = hanziData.story,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.5.sp,
                            color = Color(0xFF3B0764),
                            lineHeight = 18.5.sp
                        )
                    }

                    // ── Khối 4: Từ ghép thường dùng trong tiếng Trung ──
                    if (hanziData.commonWords.isNotEmpty()) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "TỪ GHÉP TIÊU BIỂU",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                hanziData.commonWords.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFF8FAFC))
                                            .border(BorderStroke(0.5.dp, BorderSubtle), RoundedCornerShape(12.dp))
                                            .padding(horizontal = 12.dp, vertical = 9.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = item.word,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextMain
                                            )
                                            Text(
                                                text = item.pinyin,
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = BrandIndigo
                                            )
                                            Text(
                                                text = "• ${item.meaning}",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 12.sp,
                                                color = Color(0xFF475569)
                                            )
                                        }

                                        // Phát âm từ ghép
                                        Box(
                                            modifier = Modifier
                                                .size(26.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFEEF2FF))
                                                .bounceClick(scaleDown = 0.85f) {
                                                    onSpeak(item.word)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_speaker),
                                                contentDescription = "Phát âm",
                                                tint = BrandIndigo,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ── Khối 5: Câu ví dụ thực tế trong đời sống ──
                    hanziData.exampleSentence?.let { sentence ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF0FDF4))
                                .border(BorderStroke(0.5.dp, Color(0xFFBBF7D0)), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "💬 CÂU VÍ DỤ THỰC TẾ",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D),
                                    letterSpacing = 0.5.sp
                                )

                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFDCFCE7))
                                        .bounceClick(scaleDown = 0.85f) {
                                            onSpeak(sentence.chinese)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_speaker),
                                        contentDescription = "Phát âm câu",
                                        tint = Color(0xFF16A34A),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = sentence.chinese,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = sentence.pinyin,
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF16A34A)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Dịch nghĩa: ${sentence.vietnamese}",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = Color(0xFF334155)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Khung kẻ ô Mễ tự cách (米字格) chuẩn thư pháp:
 * Vẽ bằng Canvas với các đường nét đứt ngang, dọc và 2 đường chéo,
 * đặt chữ Hán lớn ngay chính giữa.
 */
@Composable
private fun MiZiGeBox(
    hanzi: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFAFAFA))
            .border(BorderStroke(1.dp, Color(0xFFCBD5E1)), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeColor = Color(0xFFE2E8F0)
            val strokeWidth = 1.dp.toPx()
            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)

            // Đường chéo 1: góc trên trái -> dưới phải
            drawLine(
                color = strokeColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = strokeWidth,
                pathEffect = dashEffect
            )

            // Đường chéo 2: góc trên phải -> dưới trái
            drawLine(
                color = strokeColor,
                start = Offset(size.width, 0f),
                end = Offset(0f, size.height),
                strokeWidth = strokeWidth,
                pathEffect = dashEffect
            )

            // Đường ngang chính giữa
            drawLine(
                color = strokeColor,
                start = Offset(0f, size.height / 2f),
                end = Offset(size.width, size.height / 2f),
                strokeWidth = strokeWidth,
                pathEffect = dashEffect
            )

            // Đường dọc chính giữa
            drawLine(
                color = strokeColor,
                start = Offset(size.width / 2f, 0f),
                end = Offset(size.width / 2f, size.height),
                strokeWidth = strokeWidth,
                pathEffect = dashEffect
            )
        }

        // Chữ Hán thư pháp lớn ngay trung tâm ô Mễ tự cách
        Text(
            text = hanzi,
            fontSize = 54.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )
    }
}
