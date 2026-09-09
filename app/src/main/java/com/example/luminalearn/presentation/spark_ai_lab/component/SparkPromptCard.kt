package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R

/**
 * Thẻ nhập Prompt và nút bấm Generate của Spark AI Lab.
 * Nút Generate được nhúng vào góc dưới bên phải bên trong ô nhập (chuẩn phong cách ChatGPT / Gemini).
 */
@Composable
fun SparkPromptCard(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color(0x1A1E293B),
                ambientColor = Color(0x0A000000)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            PromptInputBox(
                prompt = prompt,
                onPromptChange = onPromptChange,
                onGenerateClick = onGenerateClick
            )
        }
    }
}

@Composable
private fun PromptInputBox(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEnabled = prompt.isNotBlank()
    val borderColor = if (isEnabled) Color(0xFFC7D2FE) else Color(0xFFE2E8F0)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFAFAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 10.dp, top = 14.dp, bottom = 10.dp)
        ) {
            // Vùng nhập văn bản (100% chiều ngang rộng rãi)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 44.dp, max = 110.dp)
            ) {
                if (prompt.isEmpty()) {
                    Text(
                        text = stringResource(R.string.spark_ai_prompt_placeholder),
                        color = Color(0xFF94A3B8),
                        fontSize = 13.5.sp,
                        lineHeight = 20.sp
                    )
                }

                BasicTextField(
                    value = prompt,
                    onValueChange = onPromptChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5,
                    cursorBrush = SolidColor(Color(0xFF4F46E5)),
                    textStyle = TextStyle(
                        color = Color(0xFF0F172A),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hàng góc dưới: Nút Generate nhúng ở góc phải
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GenerateInnerButton(
                    isEnabled = isEnabled,
                    onClick = onGenerateClick
                )
            }
        }
    }
}

@Composable
private fun GenerateInnerButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))
    )
    val disabledColor = Color(0xFFE2E8F0)
    val iconTint = if (isEnabled) Color.White else Color(0xFF94A3B8)

    Box(
        modifier = modifier
            .size(36.dp)
            .shadow(
                elevation = if (isEnabled) 6.dp else 0.dp,
                shape = CircleShape,
                spotColor = Color(0x664F46E5),
                ambientColor = Color(0x334F46E5)
            )
            .clip(CircleShape)
            .then(
                if (isEnabled) Modifier.background(activeBrush)
                else Modifier.background(disabledColor)
            )
            .clickable(
                enabled = isEnabled,
                indication = ripple(color = Color.White),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sparkle),
            contentDescription = stringResource(R.string.btn_generate),
            tint = iconTint,
            modifier = Modifier.size(17.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SparkPromptCardPreview() {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
    ) {
        SparkPromptCard(
            prompt = text,
            onPromptChange = { text = it },
            onGenerateClick = {}
        )
    }
}
