package com.example.luminalearn.presentation.spark_ai_lab.component

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R

import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelCornerStyle
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

data class ChatMessage(
    val id: String,
    val sender: String,
    val isAi: Boolean,
    val text: String,
    val pinyin: String? = null,
    val translation: String? = null,
    val feedback: String? = null
)

@Composable
fun RoleplayPracticeDialog(
    scenario: RoleplayScenarioData,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = "1",
                sender = scenario.aiRole,
                isAi = true,
                text = "您好！欢迎光临，请问今天想喝点什么？",
                pinyin = "Nín hǎo! Huānyíng guānglín, qǐngwèn jīntiān xiǎng hē diǎn shénme?",
                translation = "Xin chào! Hoan nghênh quý khách, xin hỏi hôm nay bạn muốn dùng món gì?"
            )
        )
    }

    var inputText by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        PixelCardBox(
            modifier = modifier
                .fillMaxWidth(0.95f)
                .height(640.dp),
            backgroundColor = Color(0xFFF8FAFC),
            headerTitle = "${scenario.iconEmoji} ${scenario.title.uppercase()}",
            headerBadge = "LIVE DIALOGUE",
            headerColor = PixelColors.PixelBlue,
            cornerStyle = PixelCornerStyle.Rivets
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                // Header phụ: Hiển thị phân vai & Nút Thoát
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🤖 ${scenario.aiRole} × 🧑 ${scenario.userRole}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark
                    )

                    // Phím thoát pixel
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(PixelStepCornerShape(1.dp))
                            .background(Color(0xFFEF4444))
                            .border(BorderStroke(1.dp, PixelColors.BorderDark), PixelStepCornerShape(1.dp))
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✕",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Danh sách tin nhắn thoại
                MessageList(messages = messages, modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.height(10.dp))

                // Thanh nhập câu thoại & nút Gửi Arcade
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val inputShape = PixelStepCornerShape(2.dp)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(inputShape)
                            .background(Color.White)
                            .border(BorderStroke(1.2.dp, PixelColors.BorderDark), inputShape)
                            .padding(horizontal = 10.dp, vertical = 9.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (inputText.isEmpty()) {
                            Text(
                                text = "Nhập câu thoại tiếng Trung...",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }

                        BasicTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            textStyle = TextStyle(
                                fontSize = 13.sp,
                                color = PixelColors.BorderDark,
                                fontWeight = FontWeight.Bold
                            ),
                            cursorBrush = SolidColor(PixelColors.PixelBlue),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    PixelArcadeButton(
                        text = "GỬI ▶",
                        color = PixelColors.PixelOrange,
                        onClick = {
                            if (inputText.isNotBlank()) {
                                val userMsg = inputText.trim()
                                messages.add(
                                    ChatMessage(
                                        id = System.currentTimeMillis().toString(),
                                        sender = scenario.userRole,
                                        isAi = false,
                                        text = userMsg
                                    )
                                )
                                inputText = ""
                                messages.add(
                                    ChatMessage(
                                        id = (System.currentTimeMillis() + 1).toString(),
                                        sender = scenario.aiRole,
                                        isAi = true,
                                        text = "好的，没问题！糖度选半糖，去冰对吗？",
                                        pinyin = "Hǎo de, méi wèntí! Tángdù xuǎn bàn táng, qù bīng duì ma?",
                                        translation = "Dạ vâng được ạ! Mức đường nửa ngọt, không đá đúng không ạ?",
                                        feedback = "✨ Lumina Native Polish: Câu nói tự nhiên, ngữ điệu chuẩn bản xứ!"
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageList(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(messages, key = { it.id }) { msg ->
            ChatMessageBubble(message = msg)
        }
    }
}

@Composable
private fun ChatMessageBubble(message: ChatMessage) {
    val bubbleShape = PixelStepCornerShape(2.dp)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isAi) Alignment.Start else Alignment.End
    ) {
        Text(
            text = message.sender,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )

        Box(
            modifier = Modifier
                .clip(bubbleShape)
                .background(if (message.isAi) Color.White else PixelColors.PixelBlue)
                .border(
                    BorderStroke(1.2.dp, PixelColors.BorderDark),
                    bubbleShape
                )
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (message.isAi) PixelColors.BorderDark else Color.White
                )
                if (message.pinyin != null) {
                    Text(
                        text = message.pinyin,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2563EB),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                if (message.translation != null) {
                    Text(
                        text = message.translation,
                        fontSize = 11.sp,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }
        }

        if (message.feedback != null) {
            val fbShape = PixelStepCornerShape(1.5.dp)
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(fbShape)
                    .background(Color(0xFFEDE9FE))
                    .border(BorderStroke(1.dp, Color(0xFFC4B5FD)), fbShape)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = message.feedback,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF5B21B6)
                )
            }
        }
    }
}
