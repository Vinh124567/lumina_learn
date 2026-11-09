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
import androidx.compose.foundation.shape.CutCornerShape
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
        PixelCardBox(
            modifier = modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f),
            backgroundColor = Color(0xFFF8FAFC),
            borderColor = PixelColors.BorderDark,
            shadowColor = PixelColors.ShadowDark,
            borderWidth = 2.dp,
            shadowOffset = 4.dp,
            cornerRadius = 8.dp,
            showRivets = false,
            showGrille = false
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
            .background(Color.White)
            .border(BorderStroke(1.5.dp, PixelColors.BorderDark), CutCornerShape(topStart = 6.dp, topEnd = 6.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Cartridge Badge HSK
            Box(
                modifier = Modifier
                    .clip(CutCornerShape(4.dp))
                    .background(PixelColors.PixelYellow)
                    .border(BorderStroke(1.5.dp, PixelColors.BorderDark), CutCornerShape(4.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Text(
                    text = word.hskLevel,
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark
                )
            }

            Column {
                Text(
                    text = "CHI TIẾT TỪ VỰNG",
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    letterSpacing = (-0.2).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (word.topic.isNotBlank()) {
                    Text(
                        text = "CHỦ ĐỀ: ${word.topic.uppercase()}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelColors.MutedText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Nút Đóng [ X ] Arcade Pixel Box
        PixelCardBox(
            modifier = Modifier.size(32.dp),
            backgroundColor = Color.White,
            borderColor = PixelColors.BorderDark,
            shadowColor = PixelColors.ShadowDark,
            borderWidth = 1.5.dp,
            shadowOffset = 2.dp,
            cornerRadius = 4.dp,
            showRivets = false,
            onClick = onDismiss
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Đóng",
                    tint = PixelColors.BorderDark,
                    modifier = Modifier.size(15.dp)
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
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DIALOG_TAB_ITEMS.forEach { item ->
            val isSelected = selectedTab == item.index

            PixelCardBox(
                backgroundColor = if (isSelected) PixelColors.PixelYellow else Color.White,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.5.dp,
                shadowOffset = if (isSelected) 2.5.dp else 1.5.dp,
                cornerRadius = 4.dp,
                showRivets = false,
                onClick = { onSelectTab(item.index) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = null,
                        tint = PixelColors.BorderDark,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.title,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        color = PixelColors.BorderDark,
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(BorderStroke(1.5.dp, PixelColors.BorderDark), CutCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Nút Bật/Tắt Ôn SRS
            PixelCardBox(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp),
                backgroundColor = if (isInSrs) PixelColors.CardOrange else Color.White,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.5.dp,
                shadowOffset = 2.dp,
                cornerRadius = 4.dp,
                showRivets = false,
                onClick = onToggleEnrollSrs
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isInSrs) "⚡" else "⚡",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isInSrs) "Đang ôn SRS" else "+ Ôn SRS",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark,
                        maxLines = 1
                    )
                }
            }

            // Nút Đánh dấu Đã thuộc
            val btnBgColor = if (isMastered) PixelColors.CardGreen else PixelColors.PixelYellow
            val btnText = if (isMastered) "✓ ĐÃ THUỘC" else "✓ THUỘC TỪ NÀY (+5)"

            PixelCardBox(
                modifier = Modifier
                    .weight(1.3f)
                    .height(42.dp),
                backgroundColor = btnBgColor,
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.8.dp,
                shadowOffset = 2.5.dp,
                cornerRadius = 4.dp,
                showRivets = false,
                onClick = onToggleMastered
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = btnText,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

