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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.shadow
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
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.93f)
                .fillMaxHeight(0.88f)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            color = Color(0xFFF6F8FB)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // ── 1. Top Header Bar (Tiêu đề, Badges & Nút đóng) ──
                DialogTopHeader(
                    word = word,
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

                // ── 4. Thanh Bottom Bar tập trung: Ôn SRS & Đánh dấu thuộc từ ──
                DialogBottomBar(
                    word = word,
                    onToggleEnrollSrs = { onToggleEnrollSrs(word.id) },
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
            .padding(start = 18.dp, end = 14.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Chi tiết từ vựng",
                fontFamily = PlusJakartaSans,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
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

        Spacer(modifier = Modifier.width(10.dp))

        // Nút Đóng (X) tròn thanh lịch
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(BorderStroke(0.5.dp, Color(0xFFE2E8F0)), CircleShape)
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
                            0.5.dp,
                            if (isSelected) Color.Transparent else Color(0xFFE2E8F0)
                        ),
                        RoundedCornerShape(12.dp)
                    )
                    .bounceClick(scaleDown = 0.94f) { onSelectTab(item.index) }
                    .padding(horizontal = 13.dp, vertical = 7.dp),
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
    onToggleEnrollSrs: () -> Unit,
    onToggleMastered: () -> Unit
) {
    val isInSrs = word.isInSrs
    val isMastered = word.isMastered

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(0.5.dp, Color(0xFFE2E8F0).copy(alpha = 0.6f)),
                    RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Nút Bật/Tắt Ôn SRS
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isInSrs) Color(0xFFFEF3C7) else Color(0xFFF1F5F9)
                    )
                    .bounceClick(scaleDown = 0.94f, onClick = onToggleEnrollSrs),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bolt),
                        contentDescription = null,
                        tint = if (isInSrs) Color(0xFFD97706) else Color(0xFF64748B),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isInSrs) "Đang ôn SRS" else "+ Ôn SRS",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isInSrs) Color(0xFFD97706) else Color(0xFF475569),
                        maxLines = 1
                    )
                }
            }

            // Nút Đánh dấu Đã thuộc
            val btnBgColor = if (isMastered) Color(0xFF059669) else VocabColors.BrandPrimary
            val btnText = if (isMastered) "✓ Đã thuộc từ này" else "✓ Thuộc từ này (+5)"

            Box(
                modifier = Modifier
                    .weight(1.3f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(14.dp))
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

