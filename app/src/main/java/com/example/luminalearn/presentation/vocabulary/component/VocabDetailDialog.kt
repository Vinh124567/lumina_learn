package com.example.luminalearn.presentation.vocabulary.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem

@Composable
fun VocabDetailDialog(
    word: VocabWordItem,
    onDismiss: () -> Unit,
    onSpeak: (String) -> Unit,
    onSpeakSlow: (String) -> Unit,
    onToggleMastered: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Chi tiết, 1: Luyện viết

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

                // ── 2. Tab Navigation (Chi tiết Từ vựng vs Luyện viết Hán tự) ──
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
                    if (selectedTab == 0) {
                        VocabDetailTabContent(
                            word = word,
                            onSpeak = onSpeak,
                            onSpeakSlow = onSpeakSlow,
                            onCopy = { text ->
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Chinese Word", text)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Đã sao chép: $text", Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        VocabWritingPracticeTabContent(word = word)
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

@Composable
private fun DialogTabRow(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Tab 1: Chi tiết Từ vựng
        Column(
            modifier = Modifier
                .clickable { onSelectTab(0) }
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "📖 Chi tiết Từ vựng",
                fontSize = 13.5.sp,
                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                color = if (selectedTab == 0) Color(0xFF5C50F6) else Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .height(2.5.dp)
                    .width(110.dp)
                    .background(if (selectedTab == 0) Color(0xFF5C50F6) else Color.Transparent)
            )
        }

        // Tab 2: Luyện viết Hán tự
        Column(
            modifier = Modifier
                .clickable { onSelectTab(1) }
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "✏️ Luyện viết Hán tự",
                fontSize = 13.5.sp,
                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                color = if (selectedTab == 1) Color(0xFF5C50F6) else Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .height(2.5.dp)
                    .width(110.dp)
                    .background(if (selectedTab == 1) Color(0xFF5C50F6) else Color.Transparent)
            )
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
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Nút Nghe lại
        Surface(
            modifier = Modifier
                .height(44.dp)
                .clickable(onClick = onSpeak),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF8FAFC),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = null,
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Nghe lại",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F46E5)
                )
            }
        }

        // Nút Thuộc từ này (+5 Tia Sáng)
        val isMastered = word.isMastered
        val btnBgColor = if (isMastered) Color(0xFF059669) else Color(0xFF5538EE)
        val btnText = if (isMastered) "✓ Đã thuộc từ này" else "✓ Thuộc từ này (+5 Tia Sáng)"

        Surface(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
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
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
