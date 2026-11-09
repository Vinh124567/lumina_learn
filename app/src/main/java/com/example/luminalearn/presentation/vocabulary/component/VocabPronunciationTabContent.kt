package com.example.luminalearn.presentation.vocabulary.component

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.luminalearn.R
import com.example.luminalearn.core.util.SpeechRecognitionHelper
import com.example.luminalearn.core.util.SpeechRecognitionState
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem

@Composable
fun VocabPronunciationTabContent(
    word: VocabWordItem,
    onSpeakSample: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechRecognitionHelper(context) }
    val speechState by speechHelper.state.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            speechHelper.destroy()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            speechHelper.startListening(word.hanzi)
        } else {
            Toast.makeText(context, "Cần cấp quyền Microphone để chấm điểm giọng nói", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleMicClick() {
        if (speechState is SpeechRecognitionState.Listening) {
            speechHelper.stopListening()
            return
        }

        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            speechHelper.startListening(word.hanzi)
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── 1. Top Header Card (Luyện phát âm & Chấm điểm giọng nói + Nghe mẫu) ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
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
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(VocabColors.BrandLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mic),
                            contentDescription = null,
                            tint = VocabColors.BrandPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Luyện phát âm & Chấm điểm giọng nói",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = VocabColors.TextDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Nhận diện thanh điệu và đối chiếu âm bản xứ",
                            fontSize = 11.sp,
                            color = VocabColors.TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Nút Nghe mẫu
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSpeakSample(word.hanzi) },
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, VocabColors.BrandPrimary)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = null,
                            tint = VocabColors.BrandPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Nghe mẫu",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = VocabColors.BrandPrimary
                        )
                    }
                }
            }
        }

        // ── 2. Card hiển thị chữ Hán to, Pinyin và Nghĩa ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, VocabColors.BorderLight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = word.hanzi,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.TextDark,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = word.pinyin,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.BrandPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = word.meaning,
                    fontSize = 13.5.sp,
                    color = VocabColors.TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ── 3. Nút bấm Micro trung tâm & Hiệu ứng sóng âm ──
        val isListening = speechState is SpeechRecognitionState.Listening
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isListening) 1.22f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseScale"
        )

        Box(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .size(110.dp),
            contentAlignment = Alignment.Center
        ) {
            // Vòng sóng âm lan tỏa khi đang nghe
            if (isListening) {
                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(VocabColors.BrandPrimary.copy(alpha = 0.2f))
                )
            }

            // Nút tròn chính "NÓI NGAY"
            Surface(
                modifier = Modifier
                    .size(86.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .clickable { handleMicClick() },
                shape = CircleShape,
                color = if (isListening) VocabColors.AccentCoral else VocabColors.BrandPrimary
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (speechState) {
                        is SpeechRecognitionState.Processing -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ĐANG CHẤM",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        is SpeechRecognitionState.Listening -> {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mic),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "ĐANG NGHE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        else -> {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mic),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "NÓI NGAY",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // ── 4. Hint text hoặc kết quả chấm điểm ──
        when (val state = speechState) {
            is SpeechRecognitionState.Idle -> {
                Text(
                    text = "Chạm vào micro và đọc to rõ chữ Hán để máy chấm điểm",
                    fontSize = 12.5.sp,
                    color = VocabColors.TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
            is SpeechRecognitionState.Listening -> {
                Text(
                    text = "Đang lắng nghe... Hãy đọc to và rõ chữ Hán!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VocabColors.BrandPrimary,
                    textAlign = TextAlign.Center
                )
            }
            is SpeechRecognitionState.Processing -> {
                Text(
                    text = "Đang phân tích âm điệu và đối chiếu phát âm...",
                    fontSize = 13.sp,
                    color = VocabColors.TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
            is SpeechRecognitionState.Success -> {
                PronunciationScoreResultCard(
                    state = state,
                    onRetry = { speechHelper.resetState() }
                )
            }
            is SpeechRecognitionState.Error -> {
                PronunciationErrorCard(
                    message = state.message,
                    onRetry = { speechHelper.resetState() }
                )
            }
        }
    }
}

@Composable
private fun PronunciationScoreResultCard(
    state: SpeechRecognitionState.Success,
    onRetry: () -> Unit
) {
    val scoreColor = if (state.score >= 80) VocabColors.SuccessGreen else if (state.score >= 60) VocabColors.AccentCoral else VocabColors.ErrorRed
    val cardBg = if (state.score >= 80) VocabColors.SuccessBg else if (state.score >= 60) Color(0xFFFFFBEB) else VocabColors.ErrorBg
    val cardBorder = if (state.score >= 80) Color(0xFFA7F3D0) else if (state.score >= 60) Color(0xFFFDE68A) else Color(0xFFFECACA)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = cardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "${state.score}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = scoreColor
                )
                Text(
                    text = "/100 điểm",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = scoreColor
                )
            }

            Text(
                text = state.feedback,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = VocabColors.TextDark,
                textAlign = TextAlign.Center
            )

            if (state.spokenText.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Bạn vừa đọc: \"${state.spokenText}\"",
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onRetry),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
            ) {
                Text(
                    text = "Đọc lại ↺",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.TextDark,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun PronunciationErrorCard(
    message: String,
    onRetry: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = VocabColors.ErrorBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = message,
                fontSize = 12.5.sp,
                color = VocabColors.ErrorRed,
                textAlign = TextAlign.Center
            )
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onRetry),
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
            ) {
                Text(
                    text = "Thử lại ↺",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.TextDark,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }
    }
}
