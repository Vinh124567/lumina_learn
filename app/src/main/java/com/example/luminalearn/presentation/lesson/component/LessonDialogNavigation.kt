package com.example.luminalearn.presentation.lesson.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.ui.theme.PlusJakartaSans

@Composable
internal fun TopHeaderBar(
    onDismiss: () -> Unit,
    onAskAi: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Drag handle bar
        Box(
            modifier = Modifier
                .size(width = 38.dp, height = 4.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2E8F0))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Nút đóng X ở góc trái với vùng bấm chuẩn 40dp
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Đóng dialog",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Nút Hỏi Gia sư AI ở góc phải
            Surface(
                onClick = onAskAi,
                shape = RoundedCornerShape(50),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
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
                        fontSize = 11.5.sp,
                        color = Color(0xFF4F46E5)
                    )
                }
            }
        }
    }
}

@Composable
internal fun CategoryAndTopicRow(
    category: String,
    subTitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
                fontSize = 11.sp,
                color = Color(0xFF6366F1),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
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
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun LessonProgressBar(
    currentIndex: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    val targetProgress = if (totalCount > 0) currentIndex.toFloat() / totalCount.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "LessonProgressAnimation"
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier
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
internal fun TitleAndHeaderInfo(
    title: String,
    currentIndex: Int,
    totalCount: Int,
    cardType: String
) {
    val formattedTitle = if (title.contains(" & ")) {
        title.replace(" & ", "\n& ")
    } else {
        title
    }

    Text(
        text = formattedTitle,
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        color = Color(0xFF0F172A),
        lineHeight = 25.sp
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

        if (cardType.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFF3E8FF)
            ) {
                Text(
                    text = cardType,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = Color(0xFF6366F1),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
internal fun DialogBottomNavigation(
    isFirst: Boolean,
    isLast: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onComplete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Thẻ trước (vô hiệu hoá nếu đang ở thẻ đầu)
        Surface(
            onClick = onPrev,
            enabled = !isFirst,
            shape = RoundedCornerShape(50),
            color = if (isFirst) Color(0xFFF8FAFC) else Color.White,
            border = BorderStroke(1.dp, if (isFirst) Color(0xFFF1F5F9) else Color(0xFFE2E8F0)),
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
                    color = if (isFirst) Color(0xFF94A3B8) else Color(0xFF64748B)
                )
            }
        }

        // Nút Thẻ tiếp theo (chuyển thành Hoàn thành nếu ở thẻ cuối)
        Surface(
            onClick = {
                if (isLast) {
                    onComplete()
                } else {
                    onNext()
                }
            },
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
                    text = if (isLast) "Hoàn thành bài học ➔" else "Thẻ tiếp theo →",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = Color.White
                )
            }
        }
    }
}
