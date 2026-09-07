package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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

private const val DEFAULT_CARD_TYPE = "Khái niệm"

data class ToneRuleItem(
    val number: Int,
    val text: String
)

data class ToneCardData(
    val slideIndex: Int = 1,
    val category: String = "PHÁT ÂM PINYIN",
    val cardType: String = DEFAULT_CARD_TYPE,
    val subTitle: String = "四声与变调",
    val title: String = "4 Thanh điệu Pinyin & Quy tắc biến âm",
    val pinyinVariants: List<String> = listOf("mā", "má", "mǎ", "mà"),
    val hanziVariants: List<String> = listOf("妈", "麻", "马", "骂"),
    val hanViet: String = "Ma (Mẹ) - Ma (Gai) - Mã (Ngựa) - Mạ (Mắng)",
    val meaning: String = "Bốn ý nghĩa hoàn toàn khác nhau chỉ nhờ thay đổi thanh điệu!",
    val explanationText: String = "Bản đồ 4 Thanh điệu qua chữ \"ma\"",
    val explanationSubtitle: String = "Tiếng Trung có 4 thanh điệu chính đặc trưng:",
    val toneRules: List<ToneRuleItem> = listOf(
        ToneRuleItem(1, "Thanh 1 (mā - Mẹ): cao và bằng phẳng (cao độ 5-5)."),
        ToneRuleItem(2, "Thanh 2 (má - Cây gai): lên giọng như dấu sắc tiếng Việt (3-5)."),
        ToneRuleItem(3, "Thanh 3 (mǎ - Ngựa): hạ giọng xuống sâu rồi lên (2-1-4)."),
        ToneRuleItem(4, "Thanh 4 (mà - Mắng): dứt khoát rơi từ cao xuống thấp (5-1).")
    ),
    val audioUrl: String = "",
    val isPlaying: Boolean = false,
    val currentIndex: Int = slideIndex,
    val totalCount: Int = 4
)

@Composable
fun LessonDetailDialog(
    action: LessonAction,
    toneCardData: ToneCardData,
) {
    Dialog(
        onDismissRequest = action.onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    TopHeaderBar(
                        category = toneCardData.category,
                        subTitle = toneCardData.subTitle,
                        onAskAi = action.onAskAi,
                        onDismiss = action.onDismiss
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    TitleAndProgress(
                        title = toneCardData.title,
                        currentIndex = toneCardData.currentIndex,
                        totalCount = toneCardData.totalCount,
                        cardType = toneCardData.cardType
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    HanziAndPronunciationCard(
                        toneCardData = toneCardData,
                        onPlayAudio = action.onPlayAudio
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    ToneMapExplanationCard(
                        explanationTitle = toneCardData.explanationText,
                        explanationSubtitle = toneCardData.explanationSubtitle,
                        rules = toneCardData.toneRules
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                DialogBottomNavigation(
                    onPrev = action.onPrev,
                    onNext = action.onNext
                )
            }
        }
    }
}

@Composable
private fun TopHeaderBar(
    category: String,
    subTitle: String,
    onAskAi: () -> Unit,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f, fill = false),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Nút đóng X
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F5F9))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Đóng dialog",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(15.dp)
                )
            }

            // Badge PHÁT ÂM PINYIN
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFF5F3FF),
                border = BorderStroke(1.dp, Color(0xFFDDD6FE))
            ) {
                Text(
                    text = category,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.5.sp,
                    color = Color(0xFF6366F1),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    maxLines = 1
                )
            }

            // Chữ Hán phụ
            if (subTitle.isNotBlank()) {
                val cleanSubTitle = if (subTitle.contains("(")) subTitle.substringBefore("(").trim() else subTitle
                Text(
                    text = cleanSubTitle,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.5.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Nút Hỏi Gia sư AI
        Surface(
            onClick = onAskAi,
            shape = RoundedCornerShape(50),
            color = Color(0xFFF8FAFC),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_ai_chat),
                    contentDescription = null,
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "Hỏi Gia sư AI",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.5.sp,
                    color = Color(0xFF4F46E5)
                )
            }
        }
    }
}

@Composable
private fun TitleAndProgress(
    title: String,
    currentIndex: Int,
    totalCount: Int,
    cardType: String
) {
    Text(
        text = title,
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 17.5.sp,
        color = Color(0xFF0F172A),
        lineHeight = 23.sp
    )

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Thẻ $currentIndex / $totalCount",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color(0xFF64748B)
        )

        Surface(
            shape = RoundedCornerShape(50),
            color = Color(0xFFF3E8FF)
        ) {
            Text(
                text = cardType.ifBlank { DEFAULT_CARD_TYPE },
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 11.5.sp,
                color = Color(0xFF6366F1),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    val progressFraction = if (totalCount > 0) currentIndex.toFloat() / totalCount.toFloat() else 0.25f
    LinearProgressIndicator(
        progress = { progressFraction },
        modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
            .clip(RoundedCornerShape(50)),
        color = Color(0xFF5538EE),
        trackColor = Color(0xFFEDE9FE),
        strokeCap = StrokeCap.Round,
        gapSize = 0.dp,
        drawStopIndicator = {}
    )
}

@Composable
private fun HanziAndPronunciationCard(
    toneCardData: ToneCardData,
    onPlayAudio: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Hàng 4 ô vuông chữ Hán lớn kèm Pinyin dưới chân
            HanziBoxRow(
                hanziList = toneCardData.hanziVariants,
                pinyinList = toneCardData.pinyinVariants
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Hàng phát âm mā · má · mǎ · mà kèm nút Nghe đọc
            PronunciationAndAudioRow(
                pinyinList = toneCardData.pinyinVariants,
                onPlayAudio = onPlayAudio
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tag Hán Việt
            HanVietBadge(hanViet = toneCardData.hanViet)

            Spacer(modifier = Modifier.height(8.dp))

            // Dòng Ý nghĩa
            MeaningRow(meaning = toneCardData.meaning)
        }
    }
}

@Composable
private fun HanziBoxRow(
    hanziList: List<String>,
    pinyinList: List<String>
) {
    val hanzis = hanziList.ifEmpty { listOf("妈", "麻", "马", "骂") }
    val pinyins = pinyinList.ifEmpty { listOf("mā", "má", "mǎ", "mà") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        hanzis.forEachIndexed { index, hanzi ->
            val pinyin = pinyins.getOrElse(index) { "" }
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF5538EE),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .height(68.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = hanzi,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (pinyin.isNotBlank()) {
                        Text(
                            text = pinyin,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFDDD6FE)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PronunciationAndAudioRow(
    pinyinList: List<String>,
    onPlayAudio: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val pinyinText = if (pinyinList.isNotEmpty()) {
                    pinyinList.joinToString("  •  ")
                } else {
                    "mā  •  má  •  mǎ  •  mà"
                }
                Text(
                    text = pinyinText,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = Color(0xFF4338CA)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = null,
                    tint = Color(0xFF6366F1),
                    modifier = Modifier.size(13.dp)
                )
            }

            Surface(
                onClick = onPlayAudio,
                shape = RoundedCornerShape(50),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFF818CF8))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_speaker),
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Nghe đọc",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.5.sp,
                        color = Color(0xFF4F46E5)
                    )
                }
            }
        }
    }
}

@Composable
private fun HanVietBadge(hanViet: String) {
    val text = hanViet.ifBlank { "Ma (Mẹ) - Ma (Gai) - Mã (Ngựa) - Mạ (Mắng)" }
    val displayContent = if (text.startsWith("Hán Việt:")) text.substringAfter("Hán Việt:").trim() else text

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFFFFBEB),
        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hán Việt: ",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = Color(0xFF92400E)
            )
            Text(
                text = displayContent,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = Color(0xFF78350F),
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun MeaningRow(meaning: String) {
    val text = meaning.ifBlank { "Bốn ý nghĩa hoàn toàn khác nhau chỉ nhờ thay đổi thanh điệu!" }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ý nghĩa: ",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 11.5.sp,
            color = Color(0xFF0F172A)
        )
        Text(
            text = text,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 11.5.sp,
            color = Color(0xFF4338CA),
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun ToneMapExplanationCard(
    explanationTitle: String,
    explanationSubtitle: String,
    rules: List<ToneRuleItem>
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF4C35DE)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Tiêu đề có chấm tròn xanh ngọc
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                )
                Text(
                    text = explanationTitle.ifBlank { "Bản đồ 4 Thanh điệu qua chữ \"ma\"" },
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = explanationSubtitle.ifBlank { "Tiếng Trung có 4 thanh điệu chính đặc trưng:" },
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color(0xFFDDD6FE)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Box quy tắc thanh điệu
            rules.forEach { rule ->
                ToneRuleRowItem(rule = rule)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ToneRuleRowItem(rule: ToneRuleItem) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0x2EFFFFFF),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Vòng tròn số thứ tự
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rule.number.toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4C35DE)
                )
            }

            Text(
                text = rule.text,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Medium,
                fontSize = 11.5.sp,
                color = Color.White,
                lineHeight = 16.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DialogBottomNavigation(
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Thẻ trước
        Surface(
            onClick = onPrev,
            shape = RoundedCornerShape(50),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "← Thẻ trước",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Nút Thẻ tiếp theo
        Surface(
            onClick = onNext,
            shape = RoundedCornerShape(50),
            color = Color(0xFF5538EE),
            modifier = Modifier
                .weight(1.3f)
                .height(46.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Thẻ tiếp theo →",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LessonDetailDialogPreview() {
    LessonDetailDialog(
        action = LessonAction(),
        toneCardData = ToneCardData()
    )
}