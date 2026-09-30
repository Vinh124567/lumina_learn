package com.example.luminalearn.presentation.vocabulary.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem

@Composable
fun VocabDetailDialog(
    word: VocabWordItem,
    onDismiss: () -> Unit,
    onSpeak: (String) -> Unit,
    onSpeakSlow: (String) -> Unit,
    onToggleMastered: (String) -> Unit,
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember(initialTab) { mutableIntStateOf(initialTab) } // 0: Chi tiết, 1: Luyện viết, 2: Chấm điểm giọng nói

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ── 1. Top Header Bar (Chip Cấp độ, Chủ đề & Nút đóng) ──
                DialogTopHeader(
                    word = word,
                    onDismiss = onDismiss
                )

                // ── 2. Tab Navigation (Chi tiết vs Luyện viết vs Chấm điểm giọng nói) ──
                DialogTabRow(
                    selectedTab = selectedTab,
                    onSelectTab = { selectedTab = it }
                )

                // ── 3. Nội dung cuộn chính ──
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (selectedTab) {
                        0 -> {
                            VocabDetailTabContent(
                                word = word,
                                onSpeak = onSpeak,
                                onSpeakSlow = onSpeakSlow,
                                onCopy = { text ->
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Chinese Word", text)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Đã sao chép: $text", Toast.LENGTH_SHORT).show()
                                },
                                onNavigateToPitchContour = { selectedTab = 2 }
                            )
                        }
                        1 -> {
                            VocabWritingPracticeTabContent(word = word)
                        }
                        else -> {
                            VocabTonePitchContourTabContent(
                                word = word,
                                onSpeakSample = onSpeak,
                                onSpeakSlow = onSpeakSlow
                            )
                        }
                    }
                }

                // ── 4. Thanh Bottom Bar cố định (Nghe lại & Thuộc từ này) ──
                DialogBottomBar(
                    word = word,
                    onSpeak = { onSpeak(word.hanzi) },
                    onToggleMastered = { onToggleMastered(word.id) }
                )
            }
        }
    }
}

@Composable
private fun DialogTopHeader(
    word: VocabWordItem,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 12.dp, top = 14.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Chip Cấp độ HSK
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFDCFCE7))
                    .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = word.hskLevel,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF16A34A)
                )
            }

            // Chip Chủ đề
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF3E8FF))
                    .border(1.dp, Color(0xFFE9D5FF), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = word.topic,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF7E22CE)
                )
            }

            if (word.isCustom) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(VocabColors.AccentCoralBg)
                        .border(1.dp, Color(0xFFFFD7C9), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Tự thêm",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = VocabColors.AccentCoral
                    )
                }
            }
        }

        // Nút đóng Dialog tròn
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F9))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Đóng",
                tint = Color(0xFF64748B),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private data class DialogTabItemData(val index: Int, val title: String, val indicatorWidth: androidx.compose.ui.unit.Dp)

private val DIALOG_TAB_ITEMS = listOf(
    DialogTabItemData(0, "📖 Chi tiết", 70.dp),
    DialogTabItemData(1, "✏️ Luyện viết", 80.dp),
    DialogTabItemData(2, "📈 Sóng âm & Cao độ", 115.dp)
)

@Composable
private fun DialogTabRow(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DIALOG_TAB_ITEMS.forEach { item ->
            val isSelected = selectedTab == item.index
            Column(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onSelectTab(item.index) }
                    )
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) VocabColors.BrandPrimary else Color(0xFF64748B),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .height(2.5.dp)
                        .width(item.indicatorWidth)
                        .background(if (isSelected) VocabColors.BrandPrimary else Color.Transparent)
                )
            }
        }
    }
}

@Composable
private fun DialogBottomBar(
    word: VocabWordItem,
    onSpeak: () -> Unit,
    onToggleMastered: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Nút Nghe lại
        Surface(
            modifier = Modifier
                .height(40.dp)
                .clickable(onClick = onSpeak),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF8FAFC),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = null,
                    tint = VocabColors.BrandPrimary,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Nghe lại (${word.pinyin})",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = VocabColors.BrandPrimary,
                    maxLines = 1
                )
            }
        }

        // Nút Thuộc từ này (+5 Tia Sáng)
        val isMastered = word.isMastered
        val btnBgColor = if (isMastered) Color(0xFF059669) else VocabColors.BrandPrimary
        val btnText = if (isMastered) "✓ Đã thuộc" else "✓ Thuộc từ (+5)"

        Surface(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clickable(onClick = onToggleMastered),
            shape = RoundedCornerShape(12.dp),
            color = btnBgColor
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = btnText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}
