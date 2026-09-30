package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.core.ui.effect.bounceClick

private val PrimaryIndigo = Color(0xFF5C50F6)
private val TitleColor = Color(0xFF0F172A)
private val SubtitleColor = Color(0xFF64748B)

/**
 * Tiêu đề phân khu rõ ràng (Section Header) chuẩn UX/UI cho màn hình chính.
 */
@Composable
fun MainSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.5.sp
                ),
                color = TitleColor,
                modifier = Modifier.weight(1f, fill = false),
                maxLines = 1
            )

            if (actionText != null && onActionClick != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp
                    ),
                    color = PrimaryIndigo,
                    modifier = Modifier.bounceClick(scaleDown = 0.94f) { onActionClick() }
                )
            }
        }

        if (subtitle != null) {
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                ),
                color = SubtitleColor
            )
        }
    }
}
