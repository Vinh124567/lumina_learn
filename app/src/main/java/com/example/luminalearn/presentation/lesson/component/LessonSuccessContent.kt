package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.common.CelebrationEffect
import com.example.luminalearn.ui.theme.PlusJakartaSans

/**
 * Màn hình Hoàn thành bài học xuất sắc (Success Screen)
 */
@Composable
internal fun LessonSuccessContent(
    toneCardData: ToneCardData,
    sparksReward: Int,
    action: LessonAction
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Header chuẩn theo mockup Figma
            SuccessTopHeader(
                category = toneCardData.category,
                subTitle = toneCardData.subTitle,
                title = toneCardData.title,
                onDismiss = action.onDismiss,
                onAskAi = action.onAskAi
            )

            Spacer(modifier = Modifier.weight(0.4f))

            // 2. Huy hiệu vàng lấp lánh kèm Confetti trang trí
            SuccessCelebrationBadge()

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Tiêu đề chúc mừng
            Text(
                text = "Hoàn thành bài học xuất sắc!",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.5.sp,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Mô tả chi tiết kèm tên bài học và số Tia Sáng nhận được
            val congratulatoryText = buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFF475569), fontWeight = FontWeight.Normal, fontSize = 13.5.sp)) {
                    append("Tuyệt vời! Bạn đã hoàn thành bài học ")
                }
                withStyle(SpanStyle(color = Color(0xFF5538EE), fontWeight = FontWeight.Bold, fontSize = 13.5.sp)) {
                    append(toneCardData.title)
                }
                withStyle(SpanStyle(color = Color(0xFF475569), fontWeight = FontWeight.Normal, fontSize = 13.5.sp)) {
                    append(" và nhận được\n")
                }
                withStyle(SpanStyle(color = Color(0xFFD97706), fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)) {
                    append("+$sparksReward Tia Sáng!")
                }
            }

            Text(
                text = congratulatoryText,
                fontFamily = PlusJakartaSans,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(0.6f))

            // 5. Nút Hoàn tất & Tiếp tục màu vàng hổ phách nổi bật
            Surface(
                onClick = action.onComplete,
                shape = RoundedCornerShape(50),
                color = Color(0xFFEBB15B),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Hoàn tất & Tiếp tục",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E293B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Hiệu ứng pháo hoa giấy nổ rực rỡ khi hoàn thành bài học
        CelebrationEffect(
            triggerKey = 1,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
internal fun SuccessTopHeader(
    category: String,
    subTitle: String,
    title: String,
    onDismiss: () -> Unit,
    onAskAi: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Drag handle bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 38.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0))
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Hàng chip thể loại, chữ Hán phụ và các nút hành động bên phải
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                if (category.isNotBlank()) {
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
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            maxLines = 1
                        )
                    }
                }
                if (subTitle.isNotBlank()) {
                    Text(
                        text = subTitle,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.5.sp,
                        color = Color(0xFF6366F1),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    onClick = onAskAi,
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
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

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onDismiss),
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
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tiêu đề bài học
        Text(
            text = title,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.5.sp,
            color = Color(0xFF0F172A),
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Thanh tiến độ bài học đạt mốc 100% rực rỡ
        LinearProgressIndicator(
            progress = { 1f },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(50)),
            color = Color(0xFF5538EE),
            trackColor = Color(0xFF5538EE),
            strokeCap = StrokeCap.Round,
            drawStopIndicator = {}
        )
    }
}

@Composable
internal fun SuccessCelebrationBadge() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
    ) {
        // Confetti trang trí xung quanh huy hiệu theo thiết kế
        DecorativeConfetti(
            color = Color(0xFFDDD6FE),
            width = 8.dp,
            height = 13.dp,
            rotation = -25f,
            modifier = Modifier.align(Alignment.TopStart).padding(start = 45.dp, top = 20.dp)
        )
        DecorativeConfetti(
            color = Color(0xFFFDE68A),
            width = 9.dp,
            height = 9.dp,
            rotation = 45f,
            modifier = Modifier.align(Alignment.TopEnd).padding(end = 65.dp, top = 22.dp)
        )
        DecorativeConfetti(
            color = Color(0xFFFDE047),
            width = 7.dp,
            height = 7.dp,
            isCircle = true,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 80.dp, bottom = 25.dp)
        )
        DecorativeConfetti(
            color = Color(0xFFFBCFE8),
            width = 8.dp,
            height = 8.dp,
            isCircle = true,
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 95.dp, bottom = 25.dp)
        )
        DecorativeConfetti(
            color = Color(0xFFBAE6FD),
            width = 7.dp,
            height = 12.dp,
            rotation = 35f,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 55.dp, top = 40.dp)
        )
        DecorativeConfetti(
            color = Color(0xFFC7D2FE),
            width = 7.dp,
            height = 12.dp,
            rotation = -15f,
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 55.dp, top = 35.dp)
        )
        DecorativeConfetti(
            color = Color(0xFFFDE68A),
            width = 9.dp,
            height = 9.dp,
            rotation = 45f,
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 70.dp, bottom = 12.dp)
        )
        DecorativeConfetti(
            color = Color(0xFFFED7AA),
            width = 8.dp,
            height = 8.dp,
            rotation = 30f,
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 70.dp, bottom = 18.dp)
        )

        // Vòng tròn trung tâm toả sáng màu vàng kem
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(124.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEF9C3).copy(alpha = 0.55f))
            )
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFDE68A))
            )
            // Ngôi sao lấp lánh đôi ở trung tâm
            Box(
                modifier = Modifier.size(46.dp),
                contentAlignment = Alignment.Center
            ) {
                SparkleStar(
                    modifier = Modifier.size(30.dp),
                    color = Color(0xFF0F172A)
                )
                SparkleStar(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.TopEnd),
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}

@Composable
internal fun DecorativeConfetti(
    color: Color,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    rotation: Float = 0f,
    isCircle: Boolean = false
) {
    Box(
        modifier = modifier
            .rotate(rotation)
            .size(width = width, height = height)
            .clip(if (isCircle) CircleShape else RoundedCornerShape(2.dp))
            .background(color)
    )
}

@Composable
internal fun SparkleStar(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF0F172A)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.5f, 0f)
            quadraticTo(w * 0.5f, h * 0.5f, w, h * 0.5f)
            quadraticTo(w * 0.5f, h * 0.5f, w * 0.5f, h)
            quadraticTo(w * 0.5f, h * 0.5f, 0f, h * 0.5f)
            quadraticTo(w * 0.5f, h * 0.5f, w * 0.5f, 0f)
            close()
        }
        drawPath(path = path, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun LessonSuccessDialogPreview() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        LessonSuccessContent(
            toneCardData = ToneCardData(
                category = "PHÁT ÂM PINYIN",
                subTitle = "四声与变调 (Sì shēng yǔ biàntiào)",
                title = "4 Thanh điệu Pinyin & Quy tắc biến âm",
                currentIndex = 4,
                totalCount = 4
            ),
            sparksReward = 30,
            action = LessonAction()
        )
    }
}
