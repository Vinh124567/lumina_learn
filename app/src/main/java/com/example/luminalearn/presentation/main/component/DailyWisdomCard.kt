package com.example.luminalearn.presentation.main.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.data.model.DailyWisdomDto
import com.example.luminalearn.ui.theme.PlusJakartaSans

@Composable
fun DailyWisdomCard(
    modifier: Modifier = Modifier,
    wisdom: DailyWisdomDto? = null,
    quote: String = stringResource(R.string.daily_wisdom_quote),
    author: String = stringResource(R.string.daily_wisdom_author),
    onRefreshClick: () -> Unit = {}
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 400),
        label = "WisdomRefreshRotation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            WisdomHeader(
                animatedRotation = animatedRotation,
                onRefreshClick = {
                    rotationAngle += 360f
                    onRefreshClick()
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            WisdomBody(
                wisdom = wisdom,
                quote = quote,
                author = author
            )
        }
    }
}

@Composable
private fun WisdomBody(
    wisdom: DailyWisdomDto?,
    quote: String,
    author: String
) {
    if (wisdom != null && wisdom.chinese.isNotBlank()) {
        WisdomDetailContent(wisdom)
    } else {
        WisdomFallbackContent(quote, author)
    }
}

@Composable
private fun WisdomHeader(
    animatedRotation: Float,
    onRefreshClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6366F1))
            )
            Text(
                text = stringResource(R.string.daily_wisdom_label),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = Color(0xFF64748B)
            )
        }

        Surface(
            shape = CircleShape,
            color = Color(0xFFF1F5F9),
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .clickable(onClick = onRefreshClick)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = stringResource(R.string.cd_refresh),
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier
                        .size(15.dp)
                        .rotate(animatedRotation)
                )
            }
        }
    }
}

@Composable
private fun WisdomDetailContent(wisdom: DailyWisdomDto) {
    Text(
        text = wisdom.chinese,
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        color = Color(0xFF0F172A)
    )

    if (wisdom.pinyin.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = wisdom.pinyin,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.5.sp,
            color = Color(0xFF4F46E5)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (wisdom.vietnamese.isNotBlank()) {
        Text(
            text = "“${wisdom.vietnamese}”",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 13.5.sp,
            lineHeight = 20.sp,
            color = Color(0xFF334155)
        )
    }

    if (wisdom.meaning.isNotBlank()) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = wisdom.meaning,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            color = Color(0xFF64748B)
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = formatAuthor(wisdom.author),
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 12.5.sp,
        color = Color(0xFF6366F1)
    )
}

private fun formatAuthor(author: String): String =
    if (author.startsWith("—")) author else "— $author"

@Composable
private fun WisdomFallbackContent(quote: String, author: String) {
    Text(
        text = quote,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 22.sp
        ),
        color = Color(0xFF0F172A)
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = author,
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 13.sp
        ),
        color = Color(0xFF64748B)
    )
}
