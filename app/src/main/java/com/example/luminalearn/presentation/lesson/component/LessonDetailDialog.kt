package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R
import com.example.luminalearn.ui.theme.PlusJakartaSans

data class LessonAction(
    val onNext: () -> Unit = {},
    val onPrev: () -> Unit = {},
    val onPlayAudio: () -> Unit = {},
    val onDismiss: () -> Unit = {},
    val onAskAi: () -> Unit = {},
    val onReport: () -> Unit = {},
    val onFavorite: () -> Unit = {},
    val onShare: () -> Unit = {}
)

data class ToneCardData(
    val category: String = "PHÁT ÂM PINYIN",
    val subTitle: String = "四声与变调 (Sì shēng yǔ biàntiào)",
    val title: String = "4 Thanh điệu Pinyin & Quy tắc biến âm",
    val pinyinVariants: List<String> = emptyList(),      // mā, má, mǎ, mà
    val hanViet: String = "",
    val meaning: String = "",
    val explanationText: String = "",
    val audioUrl: String = "",
    val isPlaying: Boolean = false,
    val cardType: String = "Khái niệm",
    val currentIndex: Int = 1,
    val totalCount: Int = 4
)

@Composable
fun LessonDetailDialog(
    action: LessonAction, toneCardData: ToneCardData
) {
    Dialog(
        onDismissRequest = action.onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.92f),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                // 1. Hàng 1: Nút "Hỏi Gia sư AI" và Nút X cùng một hàng (căn góc phải)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Nút "Hỏi Gia sư AI"
                    Surface(
                        onClick = action.onAskAi,
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .clickable(
                                    onClick = action.onAskAi
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_ai_chat),
                                contentDescription = null,
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Hỏi Gia sư AI",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                color = Color(0xFF4F46E5)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Nút X đóng dialog
                    IconButton(
                        onClick = action.onDismiss, modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Đóng dialog",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 2. Hàng 2: [Tag danh mục] và [Pinyin/Hán tự] (riêng biệt, không chung hàng với nút X)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tag "PHÁT ÂM PINYIN"
                    Surface(
                        shape = RoundedCornerShape(6.dp), color = Color(0xFFF3E8FF)
                    ) {
                        Text(
                            text = toneCardData.category,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color(0xFF7C3AED),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }

                    // Subtitle "四声与变调 (Sì shēng yǔ biàntiào)"
                    Text(
                        text = toneCardData.subTitle,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = Color(0xFF6366F1),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 3. Hàng 3: Tiêu đề bài học in đậm
                Text(
                    text = toneCardData.title,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0xFF0F172A),
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LessonDetailDialogPreview() {
    LessonDetailDialog(
        action = LessonAction(), toneCardData = ToneCardData(
            category = "PHÁT ÂM PINYIN",
            subTitle = "四声与变调 (Sì shēng yǔ biàntiào)",
            title = "4 Thanh điệu Pinyin & Quy tắc biến âm",
            pinyinVariants = listOf("mā", "má", "mǎ", "mà"),
            hanViet = "Ma (Mẹ) · Ma (Gái) · Mã (Ngựa) · Mắng",
            meaning = "Bốn ý nghĩa hoàn toàn khác nhau chỉ nhờ thay đổi thanh điệu!",
            explanationText = "Tiếng Trung có 4 thanh điệu chính...",
            audioUrl = "",
            isPlaying = false,
            cardType = "Khái niệm",
            currentIndex = 1,
            totalCount = 4
        )
    )
}