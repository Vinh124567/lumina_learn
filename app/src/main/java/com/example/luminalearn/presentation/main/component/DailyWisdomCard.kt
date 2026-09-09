package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
fun DailyWisdomCard(
    modifier: Modifier = Modifier,
    quote: String = stringResource(R.string.daily_wisdom_quote),
    author: String = stringResource(R.string.daily_wisdom_author),
    onRefreshClick: () -> Unit = {}
) {
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
            // Header: Label + Refresh icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.daily_wisdom_label),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color(0xFF64748B)
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = stringResource(R.string.cd_refresh),
                    tint = Color(0xFF64748B),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onRefreshClick() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quote text
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

            // Author
            Text(
                text = author,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 13.sp
                ),
                color = Color(0xFF64748B)
            )
        }
    }
}
