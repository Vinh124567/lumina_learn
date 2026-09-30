package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R

data class RoleplayScenarioData(
    val id: String,
    val iconEmoji: String,
    val title: String,
    val pinyinTitle: String,
    val levelCategory: String, // "hsk12", "hsk23", "hsk34"
    val levelLabel: String,
    val levelColor: Color,
    val description: String,
    val aiRole: String,
    val userRole: String,
    val bonusSparks: Int = 30
)

val DEFAULT_SCENARIOS = listOf(
    RoleplayScenarioData(
        id = "heytea",
        iconEmoji = "🧋",
        title = "Gọi trà sữa HeyTea",
        pinyinTitle = "点喜茶 (Diǎn Xǐchá)",
        levelCategory = "hsk12",
        levelLabel = "HSK 1–2 Cơ bản",
        levelColor = Color(0xFF22C55E),
        description = "Luyện chọn mức đường (半糖), đá (少冰) và gọi thêm topping trân châu bản xứ.",
        aiRole = "Nhân viên HeyTea",
        userRole = "Khách hàng"
    ),
    RoleplayScenarioData(
        id = "hotpot",
        iconEmoji = "🍲",
        title = "Gọi món lẩu Tứ Xuyên",
        pinyinTitle = "吃四川火锅 (Chī Sìchuān Huǒguō)",
        levelCategory = "hsk23",
        levelLabel = "HSK 2–3 Giao tiếp",
        levelColor = Color(0xFF3B82F6),
        description = "Chọn vị nước lẩu uyên ương (鸳鸯锅), gọi món nhúng thịt bò và pha sốt mè chuẩn vị.",
        aiRole = "Phục vụ bàn",
        userRole = "Thực khách"
    ),
    RoleplayScenarioData(
        id = "taxi",
        iconEmoji = "🚕",
        title = "Bắt taxi tại Bắc Kinh",
        pinyinTitle = "在打车 (Zài Dǎchē)",
        levelCategory = "hsk23",
        levelLabel = "HSK 2–3 Giao tiếp",
        levelColor = Color(0xFF3B82F6),
        description = "Chỉ dẫn tài xế tới điểm hẹn, hỏi giá cước và xin xuất hóa đơn (发票).",
        aiRole = "Tài xế bản xứ",
        userRole = "Hành khách"
    ),
    RoleplayScenarioData(
        id = "shopping",
        iconEmoji = "🛍️",
        title = "Trả giá mua sắm chợ đêm",
        pinyinTitle = "夜市砍价 (Yèshì Kǎnjià)",
        levelCategory = "hsk34",
        levelLabel = "HSK 3–4 Nâng cao",
        levelColor = Color(0xFFF97316),
        description = "Mặc cả khéo léo khi mua đồ lưu niệm, áo quần và thanh toán qua WeChat Pay.",
        aiRole = "Chủ sạp hàng",
        userRole = "Người mua"
    )
)

private val CardBorderColor = Color(0xFFE2E8F0)
private val PrimaryIndigo = Color(0xFF4F46E5)

@Composable
fun RoleplayScenarioCard(
    scenario: RoleplayScenarioData,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ScenarioHeader(scenario = scenario)
            Spacer(modifier = Modifier.height(10.dp))
            ScenarioContent(scenario = scenario)
            Spacer(modifier = Modifier.height(14.dp))
            ScenarioFooter(scenario = scenario, onStartClick = onStartClick)
        }
    }
}

@Composable
private fun ScenarioHeader(scenario: RoleplayScenarioData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = scenario.iconEmoji, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = scenario.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = scenario.pinyinTitle,
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(scenario.levelColor.copy(alpha = 0.12f))
                .padding(horizontal = 9.dp, vertical = 4.dp)
        ) {
            Text(
                text = scenario.levelLabel,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = scenario.levelColor
            )
        }
    }
}

@Composable
private fun ScenarioContent(scenario: RoleplayScenarioData) {
    Text(
        text = scenario.description,
        fontSize = 13.sp,
        color = Color(0xFF475569),
        lineHeight = 18.sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RoleInfoTag(label = "AI", value = scenario.aiRole)
        RoleInfoTag(label = "Bạn", value = scenario.userRole)
    }
}

@Composable
private fun RoleInfoTag(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label: ",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF94A3B8)
        )
        Text(
            text = value,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF334155)
        )
    }
}

@Composable
private fun ScenarioFooter(
    scenario: RoleplayScenarioData,
    onStartClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bolt),
                contentDescription = null,
                tint = Color(0xFFD97706),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "+${scenario.bonusSparks} Tia Sáng",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD97706)
            )
        }

        Surface(
            shape = CircleShape,
            color = PrimaryIndigo,
            modifier = Modifier.clickable(onClick = onStartClick)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "Bắt đầu nhập vai",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "→",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
