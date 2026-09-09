package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
    val categoryTextColor: Color = Color(0xFF7E22CE)
)

@Composable
fun LessonCardItem(
    lesson: ChineseLessonData,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
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

            // 2. Dòng chữ Hán kèm phiên âm Pinyin
            Text(
                text = lesson.pinyinHanziTitle,
                color = Color(0xFF4F46E5),
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 3. Tiêu đề chính của bài học
            Text(
                text = lesson.title,
                color = Color(0xFF0F172A),
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 4. Mô tả tóm tắt
            Text(
                text = lesson.description,
                color = Color(0xFF64748B),
                fontSize = 13.sp,
                lineHeight = 19.sp
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Nhãn danh mục + Cấp độ
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BadgeTag(
                text = category,
                bgColor = categoryBgColor,
                textColor = categoryTextColor
            )

            BadgeTag(
                text = level,
                bgColor = Color(0xFFFEF3C7),
                textColor = Color(0xFFB45309)
            )
        }

        // Trạng thái đã hoàn thành + Thời lượng
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (isCompleted) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = "✔ ${stringResource(R.string.status_completed)}",
                        color = Color(0xFFB45309),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = stringResource(R.string.duration_mins_format, durationMins),
                color = Color(0xFF64748B),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
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
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
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
                text = stringResource(R.string.sparks_earned_format, sparks),
                color = Color(0xFF475569),
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Nút bấm "Ôn lại →" hoặc "Bắt đầu →"
        val buttonText = if (isCompleted) {
            stringResource(R.string.btn_review)
        } else {
            stringResource(R.string.btn_start_now)
        }

        Box(
            modifier = Modifier
                .height(34.dp)
                .clip(CircleShape)
                .background(Color(0xFF5C50F6))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = Color.White),
                    onClick = onActionClick
                )
                .padding(horizontal = 14.dp),
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
