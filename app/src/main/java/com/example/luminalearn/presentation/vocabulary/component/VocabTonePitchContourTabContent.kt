package com.example.luminalearn.presentation.vocabulary.component

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.luminalearn.R
import com.example.luminalearn.core.util.SpeechRecognitionHelper
import com.example.luminalearn.core.util.SpeechRecognitionState
import com.example.luminalearn.presentation.vocabulary.model.ContourViewMode
import com.example.luminalearn.presentation.vocabulary.model.SyllableToneInfo
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabPitchSubTab
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.presentation.vocabulary.model.parseSyllables

@Composable
fun VocabTonePitchContourTabContent(
    word: VocabWordItem,
    onSpeakSample: (String) -> Unit,
    onSpeakSlow: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechRecognitionHelper(context) }
    val speechState by speechHelper.state.collectAsState()

    var activeSubTab by remember { mutableStateOf(VocabPitchSubTab.PITCH_CONTOUR) }
    var viewMode by remember { mutableStateOf(ContourViewMode.ALL) }
    var selectedSpeed by remember { mutableFloatStateOf(1.0f) }

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
            Toast.makeText(context, "Cần cấp quyền Microphone để ghi âm đối chiếu cao độ", Toast.LENGTH_SHORT).show()
        }
    }

    val isListening = speechState is SpeechRecognitionState.Listening

    val syllables = remember(word.hanzi, word.pinyin) {
        parseSyllables(word.hanzi, word.pinyin)
    }

    val handleMicClick: () -> Unit = {
        handleMicrophoneRequest(
            context = context,
            wordHanzi = word.hanzi,
            isListening = isListening,
            speechHelper = speechHelper,
            onPermissionRequired = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SubNavigationPills(
            activeSubTab = activeSubTab,
            onSelectSubTab = { activeSubTab = it }
        )

        when (activeSubTab) {
            VocabPitchSubTab.PITCH_CONTOUR -> {
                PitchContourTabSection(
                    word = word,
                    syllables = syllables,
                    uiState = PitchContourUiState(
                        viewMode = viewMode,
                        selectedSpeed = selectedSpeed,
                        speechState = speechState
                    ),
                    actions = PitchContourActions(
                        onSelectViewMode = { viewMode = it },
                        onToggleSpeed = {
                            selectedSpeed = if (selectedSpeed == 1.0f) 0.65f else 1.0f
                        },
                        onPlaySample = {
                            if (selectedSpeed == 1.0f) onSpeakSample(word.hanzi) else onSpeakSlow(word.hanzi)
                        },
                        onMicClick = handleMicClick
                    )
                )
            }

            VocabPitchSubTab.DIAGNOSTIC -> {
                VocabDiagnosticView(
                    word = word,
                    syllables = syllables,
                    speechState = speechState,
                    onMicClick = handleMicClick
                )
            }

            VocabPitchSubTab.TONE_PRACTICE -> {
                VocabTonePracticeView(
                    word = word,
                    syllables = syllables,
                    onSpeakSample = onSpeakSample,
                    onSpeakSlow = onSpeakSlow
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun handleMicrophoneRequest(
    context: Context,
    wordHanzi: String,
    isListening: Boolean,
    speechHelper: SpeechRecognitionHelper,
    onPermissionRequired: () -> Unit
) {
    if (isListening) {
        speechHelper.stopListening()
        return
    }
    val hasPerm = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED
    if (hasPerm) {
        speechHelper.startListening(wordHanzi)
    } else {
        onPermissionRequired()
    }
}

private data class PitchContourUiState(
    val viewMode: ContourViewMode,
    val selectedSpeed: Float,
    val speechState: SpeechRecognitionState
)

private data class PitchContourActions(
    val onSelectViewMode: (ContourViewMode) -> Unit,
    val onToggleSpeed: () -> Unit,
    val onPlaySample: () -> Unit,
    val onMicClick: () -> Unit
)

@Composable
private fun PitchContourTabSection(
    word: VocabWordItem,
    syllables: List<SyllableToneInfo>,
    uiState: PitchContourUiState,
    actions: PitchContourActions
) {
    val speechSuccess = uiState.speechState as? SpeechRecognitionState.Success
    val speechError = uiState.speechState as? SpeechRecognitionState.Error
    val isListening = uiState.speechState is SpeechRecognitionState.Listening
    val isProcessing = uiState.speechState is SpeechRecognitionState.Processing

    // ── 2. Top Header Card (Biểu đồ Sóng âm & Cao độ Thanh điệu) ──
    TopContourHeaderCard(
        viewMode = uiState.viewMode,
        onSelectViewMode = actions.onSelectViewMode
    )

    // ── 3. Syllable Tone Breakdown Card (Phân tích từng chữ & thanh điệu chuẩn) ──
    SyllableToneSummaryCard(word = word, syllables = syllables)

    // ── 4. Action Card: Nghe mẫu bản xứ, chỉnh tốc độ & Bật Mic ──
    PitchContourActionCard(
        word = word,
        selectedSpeed = uiState.selectedSpeed,
        isListening = isListening,
        isProcessing = isProcessing,
        onPlaySample = actions.onPlaySample,
        onToggleSpeed = actions.onToggleSpeed,
        onMicClick = actions.onMicClick
    )

    // ── 5. Khối Biểu đồ Thang Ngũ Độ (Chao 5-Level Scale: 1 - 5) ──
    val showPitchChart = uiState.viewMode != ContourViewMode.WAVE_ONLY
    if (showPitchChart) {
        PitchContourChartCard(
            syllables = syllables,
            speechSuccess = speechSuccess,
            isListening = isListening
        )
    }

    // ── 6. Khối Biểu đồ Dạng Sóng Âm (Sound Wave Oscilloscope) ──
    val showWaveChart = uiState.viewMode != ContourViewMode.PITCH_ONLY
    if (showWaveChart) {
        SoundWaveComparisonCard(
            speechSuccess = speechSuccess,
            isListening = isListening
        )
    }

    // ── 7. Báo lỗi nhận diện Microphone nếu có ──
    if (speechError != null) {
        TonePitchErrorCard(
            errorMessage = speechError.message,
            onRetry = actions.onMicClick
        )
    }

    // ── 8. Thẻ đánh giá độ tương đồng cao độ thanh điệu ──
    if (speechSuccess != null) {
        TonePitchScoreCard(
            word = word,
            syllables = syllables,
            speechSuccess = speechSuccess,
            onRetry = actions.onMicClick
        )
    }
}

private data class SubTabPillItemData(
    val tab: VocabPitchSubTab,
    val title: String,
    val iconRes: Int? = null
)

private val SUB_TAB_PILL_ITEMS = listOf(
    SubTabPillItemData(VocabPitchSubTab.PITCH_CONTOUR, "Biểu đồ Pitch Contour", R.drawable.ic_waveform),
    SubTabPillItemData(VocabPitchSubTab.DIAGNOSTIC, "Chuẩn đoán âm"),
    SubTabPillItemData(VocabPitchSubTab.TONE_PRACTICE, "Luyện thanh điệu")
)

@Composable
private fun SubNavigationPills(
    activeSubTab: VocabPitchSubTab,
    onSelectSubTab: (VocabPitchSubTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SUB_TAB_PILL_ITEMS.forEach { item ->
            SubNavPillItem(
                item = item,
                isSelected = activeSubTab == item.tab,
                onSelect = { onSelectSubTab(item.tab) }
            )
        }
    }
}

@Composable
private fun SubNavPillItem(
    item: SubTabPillItemData,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val bgColor = if (isSelected) VocabColors.BrandPrimary else Color.White
    val contentColor = if (isSelected) Color.White else VocabColors.TextMuted
    val border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    val elevation = if (isSelected) 2.dp else 0.dp
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            ),
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        border = border,
        shadowElevation = elevation
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.iconRes != null) {
                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = item.title,
                fontSize = 11.5.sp,
                fontWeight = fontWeight,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TopContourHeaderCard(
    viewMode: ContourViewMode,
    onSelectViewMode: (ContourViewMode) -> Unit
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
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEF2FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_waveform),
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(19.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Biểu đồ Sóng âm & Cao độ Thanh điệu",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = VocabColors.TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEEF2FF))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Voice Pitch Contour",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4F46E5),
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Đối chiếu đường uốn lượn thanh điệu (ngũ độ) liệu nguyên bản xứ và giọng của bạn",
                        fontSize = 10.5.sp,
                        color = VocabColors.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Bộ lọc: Tất cả | Cao độ | Sóng âm
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ViewFilterChip(
                    label = "Tất cả",
                    isSelected = viewMode == ContourViewMode.ALL,
                    onClick = { onSelectViewMode(ContourViewMode.ALL) }
                )
                ViewFilterChip(
                    label = "Cao độ",
                    isSelected = viewMode == ContourViewMode.PITCH_ONLY,
                    onClick = { onSelectViewMode(ContourViewMode.PITCH_ONLY) }
                )
                ViewFilterChip(
                    label = "Sóng âm",
                    isSelected = viewMode == ContourViewMode.WAVE_ONLY,
                    onClick = { onSelectViewMode(ContourViewMode.WAVE_ONLY) }
                )
            }
        }
    }
}

@Composable
private fun ViewFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) VocabColors.BrandPrimary else Color(0xFFF1F5F9),
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF475569),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            maxLines = 1
        )
    }
}

@Composable
private fun SyllableToneSummaryCard(
    word: VocabWordItem,
    syllables: List<SyllableToneInfo>
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, VocabColors.BorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = word.hanzi,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.TextDark
                )
                Text(
                    text = word.pinyin,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.BrandPrimary
                )
                Text(
                    text = word.meaning,
                    fontSize = 11.5.sp,
                    color = VocabColors.TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                syllables.forEachIndexed { _, syl ->
                    val badgeBg = when (syl.toneNumber) {
                        1 -> Color(0xFFE0E7FF)
                        2 -> Color(0xFFDCFCE7)
                        3 -> Color(0xFFFEF3C7)
                        4 -> Color(0xFFFFE4E6)
                        else -> Color(0xFFF1F5F9)
                    }
                    val badgeText = when (syl.toneNumber) {
                        1 -> Color(0xFF3730A3)
                        2 -> Color(0xFF16A34A)
                        3 -> Color(0xFFB45309)
                        4 -> Color(0xFFE11D48)
                        else -> Color(0xFF64748B)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(badgeBg)
                            .padding(horizontal = 7.dp, vertical = 4.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${syl.hanzi} ${syl.pinyin}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeText,
                                maxLines = 1
                            )
                            Text(
                                text = "${syl.toneName} (${syl.toneCode})",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = badgeText.copy(alpha = 0.85f),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PitchContourActionCard(
    word: VocabWordItem,
    selectedSpeed: Float,
    isListening: Boolean,
    isProcessing: Boolean,
    onPlaySample: () -> Unit,
    onToggleSpeed: () -> Unit,
    onMicClick: () -> Unit
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
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Hàng 1: Nút nghe phát âm mẫu & Nút tốc độ
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(onClick = onPlaySample),
                    shape = RoundedCornerShape(20.dp),
                    color = VocabColors.BrandLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, VocabColors.BrandPrimary.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = null,
                            tint = VocabColors.BrandPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Nghe mẫu chuẩn (${word.pinyin})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = VocabColors.BrandPrimary,
                            maxLines = 1
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onToggleSpeed),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF1F5F9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Text(
                        text = "Tốc độ: ${if (selectedSpeed == 1.0f) "1.0x" else "0.65x"}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                        maxLines = 1
                    )
                }
            }

            // Hàng 2: Nút bật mic & thu âm đối chiếu
            val buttonColor = when {
                isListening -> VocabColors.AccentCoral
                isProcessing -> Color(0xFFF59E0B)
                else -> VocabColors.BrandPrimary
            }
            val buttonText = when {
                isListening -> "Đang lắng nghe... (Bấm dừng)"
                isProcessing -> "Đang đối chiếu cao độ & âm điệu..."
                else -> "Bật mic & thu âm đối chiếu ngay"
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clickable(enabled = !isProcessing, onClick = onMicClick),
                shape = RoundedCornerShape(10.dp),
                color = buttonColor
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_mic),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = buttonText,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
