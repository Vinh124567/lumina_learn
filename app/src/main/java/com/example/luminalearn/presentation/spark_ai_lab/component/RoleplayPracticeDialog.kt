package com.example.luminalearn.presentation.spark_ai_lab.component

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

data class ChatMessage(
    val id: String,
    val sender: String,
    val isAi: Boolean,
    val text: String,
    val pinyin: String? = null,
    val translation: String? = null,
    val feedback: String? = null
)

private val PrimaryIndigo = Color(0xFF4F46E5)

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
        Surface(
            modifier = modifier
                .fillMaxWidth(0.95f)
                .height(640.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF8FAFC)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DialogHeader(scenario = scenario, onClose = onDismiss)
                Spacer(modifier = Modifier.height(12.dp))
                MessageList(messages = messages, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(10.dp))
                InputControls(
                    inputText = inputText,
                    onInputChange = { inputText = it },
                    onSend = {
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
                                    feedback = "✨ Lumina Native Polish: Câu nói tự nhiên, phát âm chuẩn ngữ điệu bản xứ!"
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun DialogHeader(
    scenario: RoleplayScenarioData,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = scenario.iconEmoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = scenario.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "${scenario.aiRole} (AI) × ${scenario.userRole} (Bạn)",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2E8F0))
                .clickable(onClick = onClose),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Đóng",
                tint = Color(0xFF475569),
                modifier = Modifier.size(16.dp)
            )
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
    val bubbleColor = if (message.isAi) Color.White else PrimaryIndigo
    val textColor = if (message.isAi) Color(0xFF1E293B) else Color.White

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isAi) Alignment.Start else Alignment.End
    ) {
        Text(
            text = message.sender,
            fontSize = 11.sp,
            color = Color(0xFF94A3B8),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = bubbleColor,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
                if (message.pinyin != null) {
                    Text(
                        text = message.pinyin,
                        fontSize = 12.sp,
                        color = Color(0xFF6366F1),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                if (message.translation != null) {
                    Text(
                        text = message.translation,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        if (message.feedback != null) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFEEF2FF),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = message.feedback,
                    fontSize = 11.5.sp,
                    color = Color(0xFF4338CA),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun InputControls(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = onInputChange,
            placeholder = { Text("Nhập câu thoại tiếng Trung...", fontSize = 13.sp) },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.weight(1f),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onSend,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
            modifier = Modifier.size(48.dp)
        ) {
            Text("→", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
