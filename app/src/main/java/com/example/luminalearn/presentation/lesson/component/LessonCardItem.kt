package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R

data class GrammarExampleData(
    val hanzi: String = "",
    val pinyinOriginal: String = "",
    val pinyinActual: String = "",
    val meaning: String = "",
    val tip: String? = null,
    val warning: String? = null,
    val audioText: String? = null
)

data class GrammarStructureData(
    val structureOrder: Int = 1,
    val title: String = "",
    val formula: String = "",
    val explanation: String = "",
    val examples: List<GrammarExampleData> = emptyList(),
    val examTip: String? = null
)

data class LessonDialogueData(
    val speakerRole: String = "A",
    val speakerName: String = "",
    val chinese: String = "",
    val pinyin: String = "",
    val vietnamese: String = "",
    val badgeColor: Color = Color(0xFF5538EE)
)

data class LessonCoreVocabData(
    val hanzi: String = "",
    val pinyin: String = "",
    val hanViet: String = "",
    val meaning: String = "",
    val partOfSpeech: String = "",
    val exampleHanzi: String = "",
    val examplePinyin: String = "",
    val exampleMeaning: String = ""
)

data class ChineseLessonData(
    val id: String = "",
    val category: String = "",
    val level: String = "Cơ bản",
    val pinyinHanziTitle: String = "",
    val title: String = "",
    val description: String = "",
    val durationMins: Int = 0,
    val sparks: Int = 0,
    val isCompleted: Boolean = false,
    val totalSlides: Int = 0,
    val slides: List<ToneCardData> = emptyList(),
    val categoryBgColor: Color = Color(0xFFF3E8FF),
    val categoryTextColor: Color = Color(0xFF7E22CE),
    val objectives: List<String> = emptyList(),
    val grammarStructures: List<GrammarStructureData> = emptyList(),
    val dialogueContext: String = "",
    val dialogues: List<LessonDialogueData> = emptyList(),
    val coreVocabularies: List<LessonCoreVocabData> = emptyList()
)

@Composable
fun LessonCardItem(
    lesson: ChineseLessonData,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSpeakClick: ((String) -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0x141E293B),
                ambientColor = Color(0x08000000)
            ),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 1. Hàng trên cùng: Badges và Trạng thái hoàn thành / Thời lượng
            CardTopHeader(
                category = lesson.category,
                categoryBgColor = lesson.categoryBgColor,
                categoryTextColor = lesson.categoryTextColor,
                level = lesson.level,
                isCompleted = lesson.isCompleted,
                durationMins = lesson.durationMins
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Dòng chữ Hán kèm phiên âm Pinyin và loa phát âm
            val hanziPart = lesson.pinyinHanziTitle.substringBefore("(").trim()
            val pinyinPart = if (lesson.pinyinHanziTitle.contains("(")) {
                "(" + lesson.pinyinHanziTitle.substringAfter("(").trim()
            } else ""

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = hanziPart,
                        color = Color(0xFF581C87),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (pinyinPart.isNotBlank()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = pinyinPart,
                            color = Color(0xFF64748B),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (onSpeakClick != null) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFEEF2FF),
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                onSpeakClick(hanziPart.ifBlank { lesson.title })
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_speaker),
                                contentDescription = "Phát âm",
                                tint = Color(0xFF6366F1),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 3. Tiêu đề chính của bài học
            Text(
                text = lesson.title,
                color = Color(0xFF0F172A),
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 4. Mô tả tóm tắt
            Text(
                text = lesson.description,
                color = Color(0xFF64748B),
                fontSize = 12.5.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 5. Hàng đáy: Điểm Sparks + Nút Bắt đầu / Ôn lại
            CardBottomAction(
                sparks = lesson.sparks,
                isCompleted = lesson.isCompleted,
                onActionClick = onActionClick
            )
        }
    }
}

@Composable
private fun CardTopHeader(
    category: String,
    categoryBgColor: Color,
    categoryTextColor: Color,
    level: String,
    isCompleted: Boolean,
    durationMins: Int
) {
    val (levelBg, levelText) = when (level) {
        "Cơ bản" -> Color(0xFFFEF3C7) to Color(0xFFB45309)
        "HSK 1" -> Color(0xFFE0F2FE) to Color(0xFF0284C7)
        "HSK 2" -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
        "HSK 3" -> Color(0xFFFFEDD5) to Color(0xFFEA580C)
        "HSK 4" -> Color(0xFFEDE9FE) to Color(0xFF7C3AED)
        "HSK 5" -> Color(0xFFFCE7F3) to Color(0xFFBE185D)
        "HSK 6" -> Color(0xFFFFE4E6) to Color(0xFFE11D48)
        else -> Color(0xFFF1F5F9) to Color(0xFF475569)
    }

    val scoreTag = when (level) {
        "HSK 1", "HSK 2" -> "Thang 200đ • Đỗ 120đ"
        "HSK 3", "HSK 4", "HSK 5", "HSK 6" -> "Thang 300đ • Đỗ 180đ"
        else -> null
    }

    val displayCategory = when (category.uppercase().trim()) {
        "NGỮ PHÁP TRỌNG ĐIỂM", "NGỮ PHÁP" -> "Ngữ pháp trọng điểm"
        "PHÁT ÂM PINYIN", "PINYIN" -> "Phát âm Pinyin"
        "GIAO TIẾP THỰC TẾ", "GIAO TIẾP" -> "Giao tiếp thực tế"
        "CHỮ HÁN & BỘ THỦ", "BỘ THỦ" -> "Chữ Hán & Bộ thủ"
        else -> category
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Hàng 1: Danh sách các Badge (luôn cuộn mượt và không bao giờ bị ngắt dòng)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BadgeTag(
                text = level,
                bgColor = levelBg,
                textColor = levelText
            )

            if (scoreTag != null) {
                BadgeTag(
                    text = scoreTag,
                    bgColor = Color(0xFFEEF2FF),
                    textColor = Color(0xFF4F46E5)
                )
            }

            BadgeTag(
                text = displayCategory,
                bgColor = categoryBgColor,
                textColor = categoryTextColor
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Hàng 2: Thời lượng / Trạng thái
        if (isCompleted) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFEF3C7)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check_circle),
                        contentDescription = null,
                        tint = Color(0xFFB45309),
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Đã hoàn thành • $durationMins phút",
                        color = Color(0xFFB45309),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$durationMins phút",
                    color = Color(0xFF64748B),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
private fun BadgeTag(
    text: String,
    bgColor: Color,
    textColor: Color
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bgColor
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
        )
    }
}

@Composable
private fun CardBottomAction(
    sparks: Int,
    isCompleted: Boolean,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Điểm thưởng Tia Sáng
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bolt),
                contentDescription = null,
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "+$sparks Tia Sáng",
                color = Color(0xFFF59E0B),
                fontSize = 12.5.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // Nút bấm "Ôn lại" hoặc "Bắt đầu học"
        val buttonText = if (isCompleted) "Ôn lại" else "Bắt đầu học"

        Box(
            modifier = Modifier
                .height(34.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFF5C50F6))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = Color.White),
                    onClick = onActionClick
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buttonText,
                color = Color.White,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

