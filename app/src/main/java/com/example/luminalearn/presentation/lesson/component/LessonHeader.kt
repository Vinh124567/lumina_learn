package com.example.luminalearn.presentation.lesson.component

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.ui.theme.PlusJakartaSans

@Composable
fun LessonHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 1. Tag pill phía trên
        Surface(
            shape = RoundedCornerShape(50.dp),
            color = Color(0xFFEDE9FE)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_nav_explore),
                    contentDescription = null,
                    tint = Color(0xFF5C50F6),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LỘ TRÌNH BÀI HỌC HSK",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5C50F6),
                    letterSpacing = 0.4.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Main Title
        Text(
            text = stringResource(R.string.library_title),
            fontFamily = PlusJakartaSans,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0F172A),
            letterSpacing = (-0.2).sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Khóa học tương tác & luyện phản xạ theo cấp độ",
            fontFamily = PlusJakartaSans,
            fontSize = 12.5.sp,
            color = Color(0xFF64748B)
        )
    }
}
