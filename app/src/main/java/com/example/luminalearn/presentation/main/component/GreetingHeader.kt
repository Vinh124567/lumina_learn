package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R

import androidx.compose.ui.res.stringResource

@Composable
fun GreetingHeader(
    modifier: Modifier = Modifier,
    userName: String = "Alex Vance!",
    onAskAiClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Tag 'WELCOME BACK'
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFEDE9FE))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = stringResource(R.string.cd_welcome),
                    tint = Color(0xFF5C50F6),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = stringResource(R.string.welcome_back),
                    color = Color(0xFF5C50F6),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Heading: "Brighten your mind today,\nAlex Vance!"
        val titlePart1 = stringResource(R.string.greeting_title_part1)
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF0F172A),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp
                    )
                ) {
                    append(titlePart1)
                }
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF5C50F6),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp
                    )
                ) {
                    append(userName)
                }
            },
            lineHeight = 38.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Subtitle
        Text(
            text = stringResource(R.string.daily_goal_done_subtitle),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Button "Ask Spark AI Lab"
        Button(
            onClick = onAskAiClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5C50F6)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_spark_ai),
                    contentDescription = stringResource(R.string.cd_spark_ai),
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(R.string.btn_ask_spark_ai),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}