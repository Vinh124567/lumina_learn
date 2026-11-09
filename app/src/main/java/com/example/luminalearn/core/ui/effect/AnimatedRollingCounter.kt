package com.example.luminalearn.core.ui.effect

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Hiệu ứng số cuộn tròn (Rolling Number Odometer) khi điểm hoặc streak thay đổi.
 */
@Composable
fun AnimatedRollingCounter(
    count: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Unspecified
    )
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        count.toString().forEach { char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { it } togetherWith slideOutVertically { -it }
                    } else {
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                    }
                },
                label = "digit_roll"
            ) { targetChar ->
                Text(
                    text = targetChar.toString(),
                    style = textStyle
                )
            }
        }
    }
}
