package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.util.SpeechRecognitionState
import com.example.luminalearn.presentation.vocabulary.model.SyllableToneInfo
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem

@Composable
fun VocabDiagnosticView(
    word: VocabWordItem,
    syllables: List<SyllableToneInfo>,
    speechState: SpeechRecognitionState,
    onMicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isListening = speechState is SpeechRecognitionState.Listening
    val isProcessing = speechState is SpeechRecognitionState.Processing
    val speechSuccess = speechState as? SpeechRecognitionState.Success
    val speechError = (speechState as? SpeechRecognitionState.Error)?.message

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ── 1. Header Card Chẩn đoán âm vị ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, VocabColors.BorderLight)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_waveform),
                            contentDescription = null,
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Chẩn đoán âm vị & Độ chuẩn xác",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = VocabColors.TextDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Phân tích khẩu hình, trường độ & cao độ thanh điệu",
                            fontSize = 11.sp,
                            color = VocabColors.TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // ── 2. Báo lỗi nhận diện Microphone nếu có ──
        if (speechError != null) {
            TonePitchErrorCard(
                errorMessage = speechError,
                onRetry = onMicClick
            )
        }

        // ── 3. Kết quả chẩn đoán nếu đã ghi âm / Lời nhắc nếu chưa ──
        if (speechSuccess != null) {
            DiagnosticResultCard(
                score = speechSuccess.score,
                spokenText = speechSuccess.spokenText,
                feedback = speechSuccess.feedback,
                targetText = word.hanzi,
                onRetry = onMicClick
            )
        } else {
            DiagnosticPromptCard(
                isListening = isListening,
                isProcessing = isProcessing,
                onMicClick = onMicClick
            )
        }

        // ── 4. Chẩn đoán chi tiết từng âm tiết ──
        DiagnosticSyllableBreakdownCard(
            syllables = syllables,
            speechSuccess = speechSuccess
        )

        // ── 5. Cẩm nang mẹo phát âm & Lỗi phổ biến ──
        PhoneticGuidanceCard(syllables = syllables)
    }
}

@Composable
private fun DiagnosticPromptCard(
    isListening: Boolean,
    isProcessing: Boolean,
    onMicClick: () -> Unit
) {
    val buttonColor = when {
        isListening -> VocabColors.AccentCoral
        isProcessing -> Color(0xFFF59E0B)
        else -> VocabColors.BrandPrimary
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isListening -> VocabColors.AccentCoral
                                isProcessing -> Color(0xFFF59E0B)
                                else -> Color(0xFF94A3B8)
                            }
                        )
                )
                Text(
                    text = when {
                        isListening -> "Đang lắng nghe giọng bạn..."
                        isProcessing -> "Đang đối chiếu âm vị & cao độ..."
                        else -> "Chưa có bản ghi âm chẩn đoán"
                    },
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isListening -> VocabColors.AccentCoral
                        isProcessing -> Color(0xFFD97706)
                        else -> Color(0xFF475569)
                    }
                )
            }

            Text(
                text = "Hệ thống sẽ đối chiếu khẩu hình, phụ âm đầu, vần và đường cao độ 5 mức của bạn so với phát âm chuẩn Bắc Kinh.",
                fontSize = 11.5.sp,
                color = Color(0xFF64748B),
                lineHeight = 16.sp
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(10.dp),
                color = buttonColor,
                onClick = onMicClick,
                enabled = !isProcessing
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Đang chẩn đoán âm điệu...",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    } else if (isListening) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mic),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Đang thu âm... Bấm để dừng",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mic),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Bật mic để chẩn đoán giọng đọc ngay",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiagnosticResultCard(
    score: Int,
    spokenText: String,
    feedback: String,
    targetText: String,
    onRetry: () -> Unit
) {
    val scoreColor = when {
        score >= 80 -> VocabColors.SuccessGreen
        score >= 60 -> Color(0xFFF59E0B)
        else -> VocabColors.ErrorRed
    }
    val badgeBg = when {
        score >= 80 -> Color(0xFFDCFCE7)
        score >= 60 -> Color(0xFFFEF3C7)
        else -> Color(0xFFFEE2E2)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, VocabColors.BorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(scoreColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$score",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    Column {
                        Text(
                            text = "Kết quả chẩn đoán âm",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = VocabColors.TextDark
                        )
                        Text(
                            text = "Bạn đọc: \"$spokenText\" • Chuẩn: \"$targetText\"",
                            fontSize = 11.sp,
                            color = VocabColors.TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onRetry),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Text(
                        text = "Thử lại ↺",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VocabColors.TextDark,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                    )
                }
            }

            // Nhận xét chi tiết
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(badgeBg, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = feedback,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E293B),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun DiagnosticSyllableBreakdownCard(
    syllables: List<SyllableToneInfo>,
    speechSuccess: SpeechRecognitionState.Success?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, VocabColors.BorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Phân tích âm vị từng chữ",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = VocabColors.TextDark
            )

            syllables.forEachIndexed { idx, s ->
                val sylStatus = when {
                    speechSuccess == null -> "Chờ kiểm tra"
                    speechSuccess.score >= 80 -> "Phát âm chuẩn xác"
                    speechSuccess.score >= 60 -> "Cao độ lệch nhẹ"
                    else -> "Cần sửa âm & thanh"
                }
                val sylColor = when {
                    speechSuccess == null -> Color(0xFF64748B)
                    speechSuccess.score >= 80 -> Color(0xFF16A34A)
                    speechSuccess.score >= 60 -> Color(0xFFD97706)
                    else -> Color(0xFFDC2626)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(VocabColors.BrandLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = s.hanzi,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = VocabColors.BrandPrimary
                            )
                        }

                        Column {
                            Text(
                                text = "${s.pinyin} • ${s.toneName} (${s.toneCode})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = VocabColors.TextDark
                            )
                            Text(
                                text = "Âm tiết #${idx + 1}",
                                fontSize = 10.sp,
                                color = VocabColors.TextMuted
                            )
                        }
                    }

                    Text(
                        text = sylStatus,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = sylColor
                    )
                }
            }
        }
    }
}

@Composable
private fun PhoneticGuidanceCard(syllables: List<SyllableToneInfo>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, VocabColors.BorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "💡 Lưu ý ngữ âm & Mẹo phát âm",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = VocabColors.TextDark
            )

            syllables.forEach { s ->
                val tip = when (s.toneNumber) {
                    1 -> "Thanh 1 (55): Giữ cao độ ổn định ngang bằng mức Sol, không hạ giọng ở đuôi âm."
                    2 -> "Thanh 2 (35): Khởi đầu ở tầm trung (Mi) và vút nhanh lên cao (Sol), tương tự dấu sắc dứt khoát."
                    3 -> "Thanh 3 (214): Xuống thấp nhất ở đáy cổ họng (mốc 1) rồi mới nhấc nhẹ lên mốc 4."
                    4 -> "Thanh 4 (51): Đổ dốc thật dứt khoát từ cao nhất (Sol) rơi thẳng xuống thấp nhất (Do)."
                    else -> "Khinh thanh: Đọc thật nhẹ, ngắn và tự nhiên."
                }
                Text(
                    text = "• ${s.hanzi} (${s.pinyin}): $tip",
                    fontSize = 11.sp,
                    color = Color(0xFF475569),
                    lineHeight = 15.sp
                )
            }
        }
    }
}
