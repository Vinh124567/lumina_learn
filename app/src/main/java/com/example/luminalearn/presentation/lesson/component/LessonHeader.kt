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

@Composable
fun LessonHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 1. Badge "THƯ VIỆN HÁN NGỮ VI MÔ"
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFEEF2FF)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_nav_explore),
                    contentDescription = null,
                    tint = Color(0xFF6366F1),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(R.string.library_badge),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF6366F1),
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Main Title
        Text(
            text = stringResource(R.string.library_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A),
                letterSpacing = (-0.5).sp
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        // 3. Subtitle Description
        Text(
            text = stringResource(R.string.library_desc),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.5.sp,
                color = Color(0xFF64748B),
                lineHeight = 20.sp
            )
        )
    }
}
