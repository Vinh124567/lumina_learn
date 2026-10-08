package com.example.luminalearn.presentation.vocabulary.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Màn hình Chi tiết Từ vựng (Modal toàn màn hình).
 * Thiết kế đồng bộ hoàn hảo với hệ thống Bento và ngôn ngữ thiết kế LuminaLearn.
 */
@Composable
fun VocabDetailDialog(
    word: VocabWordItem,
    onDismiss: () -> Unit,
    onSpeak: (String) -> Unit,
    onSpeakSlow: (String) -> Unit,
    onToggleMastered: (String) -> Unit,
    onToggleEnrollSrs: (String) -> Unit = {},
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember(initialTab) { mutableIntStateOf(initialTab) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color(0xFFF6F8FB)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // ── 1. Top Header Bar (Nút quay lại, Tiêu đề, Badges & Nút SRS) ──
                DialogTopHeader(
                    word = word,
                    onToggleEnrollSrs = { onToggleEnrollSrs(word.id) },
                    onDismiss = onDismiss
                )

                // ── 2. Tab Navigation (Tổng quan vs Luyện viết vs Sóng âm & Cao độ) ──
                DialogTabRow(
                    selectedTab = selectedTab,
                    onSelectTab = { selectedTab = it }
                )

                // ── 3. Nội dung cuộn chính theo Tab ──
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

                // ── 4. Thanh Bottom Bar cố định (Nghe lại & Đánh dấu thuộc từ) ──
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
    onToggleEnrollSrs: () -> Unit,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Bên trái: Nút quay lại dạng tròn + Tiêu đề
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), CircleShape)
                    .bounceClick(scaleDown = 0.88f, onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_left),
                    contentDescription = "Quay lại",
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = "Chi tiết từ vựng",
                    fontFamily = PlusJakartaSans,
                    fontSize = 17.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = word.hskLevel,
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = VocabColors.BrandPrimary
                    )
                    Text(
                        text = "•",
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = word.topic,
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Cụm action bên phải: Nút Bật/Tắt Ôn SRS
        val isInSrs = word.isInSrs
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isInSrs) Color(0xFFFEF3C7) else Color.White)
                .border(
                    BorderStroke(
                        1.dp,
                        if (isInSrs) Color(0xFFF59E0B).copy(alpha = 0.5f) else Color(0xFFE2E8F0)
                    ),
                    RoundedCornerShape(10.dp)
                )
                .bounceClick(scaleDown = 0.90f, onClick = onToggleEnrollSrs)
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = null,
                    tint = if (isInSrs) Color(0xFFD97706) else Color(0xFF64748B),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (isInSrs) "Đang ôn SRS" else "+ Ôn SRS",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isInSrs) Color(0xFFD97706) else Color(0xFF334155)
                )
            }
        }
    }
}

private data class DialogTabItemData(
    val index: Int,
    val title: String,
    val iconRes: Int
)

private val DIALOG_TAB_ITEMS = listOf(
    DialogTabItemData(0, "Tổng quan", R.drawable.ic_book),
    DialogTabItemData(1, "Luyện viết", R.drawable.ic_sparkle),
    DialogTabItemData(2, "Sóng âm & Cao độ", R.drawable.ic_waveform)
)

@Composable
private fun DialogTabRow(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DIALOG_TAB_ITEMS.forEach { item ->
            val isSelected = selectedTab == item.index

            val bgAnimation by animateColorAsState(
                targetValue = if (isSelected) VocabColors.BrandPrimary else Color.White,
                animationSpec = tween(durationMillis = 200),
                label = "tab_bg"
            )
            val contentColorAnimation by animateColorAsState(
                targetValue = if (isSelected) Color.White else Color(0xFF475569),
                animationSpec = tween(durationMillis = 200),
                label = "tab_color"
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgAnimation)
                    .border(
                        BorderStroke(
                            1.dp,
                            if (isSelected) VocabColors.BrandPrimary else Color(0xFFE2E8F0)
                        ),
                        RoundedCornerShape(12.dp)
                    )
                    .bounceClick(scaleDown = 0.94f) { onSelectTab(item.index) }
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = null,
                        tint = contentColorAnimation,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.title,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = contentColorAnimation,
                        maxLines = 1
                    )
                }
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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color(0xFFE2E8F0)))
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Nút Nghe lại
            Box(
                modifier = Modifier
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(BorderStroke(1.2.dp, Color(0xFFE2E8F0)), RoundedCornerShape(12.dp))
                .bounceClick(scaleDown = 0.94f, onClick = onSpeak)
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_speaker),
                        contentDescription = null,
                        tint = VocabColors.BrandPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Nghe (${word.pinyin})",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = VocabColors.BrandPrimary,
                        maxLines = 1
                    )
                }
            }

            // Nút Thuộc từ này (+5 Tia Sáng)
            val isMastered = word.isMastered
            val btnBgColor = if (isMastered) Color(0xFF059669) else VocabColors.BrandPrimary
            val btnText = if (isMastered) "✓ Đã thuộc từ này" else "✓ Thuộc từ này (+5)"

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(btnBgColor)
                    .bounceClick(scaleDown = 0.94f, onClick = onToggleMastered),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = btnText,
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

