package com.example.luminalearn.presentation.vocabulary.component

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.R
import com.example.luminalearn.data.model.CreateVocabularyRequest
import com.example.luminalearn.data.model.VocabularyDto
import com.example.luminalearn.presentation.vocabulary.model.VocabColors
import com.example.luminalearn.ui.theme.PlusJakartaSans

@Composable
fun AddVocabBottomSheet(
    isOpen: Boolean,
    isSubmitting: Boolean,
    isAiLookingUp: Boolean,
    onDismiss: () -> Unit,
    onLookupAi: (String, (VocabularyDto?) -> Unit) -> Unit,
    onSubmit: (CreateVocabularyRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    val context = LocalContext.current

    // State các trường nhập
    var aiQuery by remember { mutableStateOf("") }
    var hanzi by remember { mutableStateOf("") }
    var pinyin by remember { mutableStateOf("") }
    var hanViet by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var hskLevel by remember { mutableStateOf("HSK 2 (Sơ cấp – 300 từ)") }
    var userTopic by remember { mutableStateOf("Sổ tay cá nhân") }
    var partOfSpeech by remember { mutableStateOf("Danh từ") }
    var radical by remember { mutableStateOf("") }
    var strokes by remember { mutableStateOf("") }
    var exampleHanzi by remember { mutableStateOf("") }
    var examplePinyin by remember { mutableStateOf("") }
    var exampleMeaning by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    // Dropdown expanded states
    var hskExpanded by remember { mutableStateOf(false) }
    var posExpanded by remember { mutableStateOf(false) }

    val hskOptions = listOf(
        "HSK 1 (Cơ bản – 150 từ)",
        "HSK 2 (Sơ cấp – 300 từ)",
        "HSK 3 (Sơ - Trung cấp – 600 từ)",
        "HSK 4 (Trung cấp – 1,200 từ)",
        "HSK 5 (Cao cấp – 2,500 từ)",
        "HSK 6 (Thành thạo – 5,000 từ)",
        "Sổ tay từ vựng cá nhân"
    )

    val posOptions = listOf(
        "Danh từ", "Động từ", "Tính từ", "Phó từ",
        "Liên từ", "Lượng từ", "Giới từ", "Thành ngữ"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ── 1. HEADER (Tags, Nút đóng X, Title, Subtitle) ──
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    // Hàng 1: Tags/Badges (Trái) & Nút đóng X (Phải)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Badge chuyên mục: SỔ TAY TỪ VỰNG
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFEEF2FF))
                                    .border(0.8.dp, Color(0xFFC7D2FE), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "SỔ TAY TỪ VỰNG",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VocabColors.BrandPrimary
                                )
                            }

                            // Badge Cá nhân hóa
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(VocabColors.BrandLight)
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "CÁ NHÂN HÓA",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VocabColors.BrandPrimary
                                )
                            }
                        }

                        // Nút đóng tròn chuẩn app
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9))
                                .clickable(onClick = onDismiss),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "Đóng",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    // Hàng 2: Tiêu đề lớn
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Thêm Từ Vựng Mới",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    // Hàng 3: Subtitle mô tả
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Lưu từ vựng mới vào sổ tay cá nhân, hỗ trợ tự động điền nhanh với Lumina AI.",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 16.sp
                    )
                }

                HorizontalDivider(color = Color(0xFFF1F5F9))

                // ── 2. SCROLLABLE FORM CONTENT ──
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ── BOX AI TRA CỨU SIÊU TỐC (Gọn gàng, tinh tế) ──
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
                        border = BorderStroke(1.dp, Color(0xFFE9D5FF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_sparkle),
                                    contentDescription = null,
                                    tint = Color(0xFF7C3AED),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tra cứu & tự động điền với Lumina AI",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Nhập chữ Hán hoặc tiếng Việt để AI tự điền toàn bộ thông tin:",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = aiQuery,
                                    onValueChange = { aiQuery = it },
                                    placeholder = { Text("VD: 咖啡 hoặc cà phê...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedBorderColor = Color(0xFF7C3AED),
                                        unfocusedBorderColor = Color(0xFFE2E8F0)
                                    )
                                )

                                Surface(
                                    modifier = Modifier
                                        .height(48.dp)
                                        .clickable(enabled = !isAiLookingUp) {
                                            if (aiQuery.isBlank()) {
                                                Toast.makeText(context, "Vui lòng nhập từ để AI tra cứu", Toast.LENGTH_SHORT).show()
                                                return@clickable
                                            }
                                            onLookupAi(aiQuery) { result ->
                                                if (result != null) {
                                                    if (result.hanzi.isNotBlank()) hanzi = result.hanzi
                                                    if (result.pinyin.isNotBlank()) pinyin = result.pinyin
                                                    if (result.hanViet.isNotBlank()) hanViet = result.hanViet
                                                    if (result.meaning.isNotBlank()) meaning = result.meaning
                                                    if (result.partOfSpeech.isNotBlank()) partOfSpeech = result.partOfSpeech
                                                    if (result.topic.isNotBlank()) userTopic = result.topic
                                                    if (result.radical.isNotBlank()) radical = result.radical
                                                    if (result.strokes.isNotBlank()) strokes = result.strokes
                                                    if (result.exampleHanzi.isNotBlank()) exampleHanzi = result.exampleHanzi
                                                    if (result.examplePinyin.isNotBlank()) examplePinyin = result.examplePinyin
                                                    if (result.exampleMeaning.isNotBlank()) exampleMeaning = result.exampleMeaning
                                                    Toast.makeText(context, "Đã tự động điền từ '${result.hanzi}'!", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    Toast.makeText(context, "Không tìm thấy từ tương ứng, bạn có thể tự nhập tay!", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF7C3AED)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 14.dp)
                                            .fillMaxHeight(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isAiLookingUp) {
                                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                        } else {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_sparkle),
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "AI Điền",
                                                    fontFamily = PlusJakartaSans,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ── HÀNG 1: Chữ Hán (Hanzi) & Phiên âm Pinyin (Cân đối hoàn hảo) ──
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            FormLabel(title = "Chữ Hán (Hanzi)", isRequired = true)
                            Spacer(modifier = Modifier.height(4.dp))
                            FormTextField(
                                value = hanzi,
                                onValueChange = { hanzi = it },
                                placeholder = "VD: 咖啡"
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            FormLabel(title = "Phiên âm Pinyin", isRequired = true)
                            Spacer(modifier = Modifier.height(4.dp))
                            FormTextField(
                                value = pinyin,
                                onValueChange = { pinyin = it },
                                placeholder = "VD: kāfēi"
                            )
                        }
                    }

                    // ── HÀNG 2: Âm Hán Việt ──
                    Column(modifier = Modifier.fillMaxWidth()) {
                        FormLabel(title = "Âm Hán Việt", subtitle = "(giúp nhớ từ gốc)")
                        Spacer(modifier = Modifier.height(4.dp))
                        FormTextField(
                            value = hanViet,
                            onValueChange = { hanViet = it },
                            placeholder = "VD: Cà Phê, Thụy Giác, Học Tập"
                        )
                    }

                    // ── HÀNG 3: Nghĩa tiếng Việt ──
                    Column(modifier = Modifier.fillMaxWidth()) {
                        FormLabel(title = "Nghĩa tiếng Việt", isRequired = true)
                        Spacer(modifier = Modifier.height(4.dp))
                        FormTextField(
                            value = meaning,
                            onValueChange = { meaning = it },
                            placeholder = "VD: Cà phê, Ngủ, Học hành"
                        )
                    }

                    // ── HÀNG 4: Cấp độ mục tiêu (HSK) ──
                    Column(modifier = Modifier.fillMaxWidth()) {
                        FormLabel(title = "Cấp độ mục tiêu (HSK)")
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            FormDropdownSelector(
                                selectedText = hskLevel,
                                onClick = { hskExpanded = true }
                            )
                            DropdownMenu(
                                expanded = hskExpanded,
                                onDismissRequest = { hskExpanded = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                hskOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt, fontSize = 13.sp) },
                                        onClick = {
                                            hskLevel = opt
                                            hskExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // ── HÀNG 5: 3 cột (Từ loại, Bộ thủ, Số nét bút) ──
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1.1f)) {
                            FormLabel(title = "Từ loại")
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.fillMaxWidth()) {
                                FormDropdownSelector(
                                    selectedText = partOfSpeech,
                                    onClick = { posExpanded = true }
                                )
                                DropdownMenu(
                                    expanded = posExpanded,
                                    onDismissRequest = { posExpanded = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    posOptions.forEach { opt ->
                                        DropdownMenuItem(
                                            text = { Text(opt, fontSize = 13.sp) },
                                            onClick = {
                                                partOfSpeech = opt
                                                posExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            FormLabel(title = "Bộ thủ")
                            Spacer(modifier = Modifier.height(4.dp))
                            FormTextField(
                                value = radical,
                                onValueChange = { radical = it },
                                placeholder = "VD: 口 (Khẩu)"
                            )
                        }
                        Column(modifier = Modifier.weight(0.9f)) {
                            FormLabel(title = "Số nét bút")
                            Spacer(modifier = Modifier.height(4.dp))
                            FormTextField(
                                value = strokes,
                                onValueChange = { strokes = it },
                                placeholder = "VD: 8, 12"
                            )
                        }
                    }

                    // ── HÀNG 6: Box Câu ví dụ thực tế ngữ cảnh ──
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_nav_vocabulary),
                                    contentDescription = null,
                                    tint = VocabColors.BrandPrimary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Câu ví dụ thực tế ngữ cảnh",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            FormTextField(
                                value = exampleHanzi,
                                onValueChange = { exampleHanzi = it },
                                placeholder = "Câu ví dụ bằng chữ Hán (VD: 我想喝一杯热咖啡。)"
                            )

                            FormTextField(
                                value = examplePinyin,
                                onValueChange = { examplePinyin = it },
                                placeholder = "Pinyin của câu ví dụ (VD: Wǒ xiǎng hē yībēi rè kāfēi.)"
                            )

                            FormTextField(
                                value = exampleMeaning,
                                onValueChange = { exampleMeaning = it },
                                placeholder = "Dịch nghĩa tiếng Việt (VD: Tôi muốn uống một cốc cà phê nóng.)"
                            )
                        }
                    }

                    // ── HÀNG 7: Mẹo ghi nhớ hoặc lưu ý phòng thi HSK ──
                    Column(modifier = Modifier.fillMaxWidth()) {
                        FormLabel(title = "💡 Mẹo ghi nhớ hoặc lưu ý phòng thi HSK", subtitle = "(tùy chọn)")
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            placeholder = { Text("VD: Cả hai chữ đều có bộ Khẩu (口) vì liên quan đến việc ăn uống...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                            minLines = 2,
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedBorderColor = VocabColors.BrandPrimary,
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ── 3. BOTTOM FOOTER BAR (Đồng bộ 100% với DialogBottomBar của app) ──
                HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Nút Hủy bỏ (Nền xám nhạt #F8FAFC, viền xám #E2E8F0, chữ xám đậm #64748B)
                    Surface(
                        modifier = Modifier
                            .height(44.dp)
                            .clickable(onClick = onDismiss),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Hủy bỏ",
                                fontFamily = PlusJakartaSans,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    // Nút Lưu từ vựng mới (Màu tím thương hiệu #5538EE nổi bật, chữ trắng)
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clickable(enabled = !isSubmitting) {
                                if (hanzi.isBlank()) {
                                    Toast.makeText(context, "Vui lòng nhập Chữ Hán (Hanzi)", Toast.LENGTH_SHORT).show()
                                    return@clickable
                                }
                                if (pinyin.isBlank()) {
                                    Toast.makeText(context, "Vui lòng nhập Phiên âm Pinyin", Toast.LENGTH_SHORT).show()
                                    return@clickable
                                }
                                if (meaning.isBlank()) {
                                    Toast.makeText(context, "Vui lòng nhập Nghĩa tiếng Việt", Toast.LENGTH_SHORT).show()
                                    return@clickable
                                }

                                val cleanHskLevel = when {
                                    hskLevel.startsWith("HSK 1") -> "HSK 1"
                                    hskLevel.startsWith("HSK 2") -> "HSK 2"
                                    hskLevel.startsWith("HSK 3") -> "HSK 3"
                                    hskLevel.startsWith("HSK 4") -> "HSK 4"
                                    hskLevel.startsWith("HSK 5") -> "HSK 5"
                                    hskLevel.startsWith("HSK 6") -> "HSK 6"
                                    else -> "HSK 1"
                                }

                                val request = CreateVocabularyRequest(
                                    hanzi = hanzi.trim(),
                                    pinyin = pinyin.trim(),
                                    hanViet = hanViet.trim(),
                                    meaning = meaning.trim(),
                                    hskLevel = cleanHskLevel,
                                    topic = userTopic.ifBlank { "Sổ tay cá nhân" },
                                    partOfSpeech = partOfSpeech.trim(),
                                    radical = radical.trim(),
                                    strokes = strokes.trim(),
                                    exampleHanzi = exampleHanzi.trim(),
                                    examplePinyin = examplePinyin.trim(),
                                    exampleMeaning = exampleMeaning.trim(),
                                    note = note.trim()
                                )
                                onSubmit(request)
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = VocabColors.BrandPrimary
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Lưu từ vựng mới",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormLabel(
    title: String,
    isRequired: Boolean = false,
    subtitle: String? = null
) {
    Text(
        text = buildAnnotatedString {
            append(title)
            if (isRequired) {
                withStyle(SpanStyle(color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)) {
                    append(" *")
                }
            }
            if (subtitle != null) {
                withStyle(SpanStyle(color = Color(0xFF94A3B8), fontWeight = FontWeight.Normal, fontSize = 11.sp)) {
                    append(" $subtitle")
                }
            }
        },
        fontFamily = PlusJakartaSans,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF334155),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, fontSize = 12.sp, color = Color(0xFF94A3B8)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF8FAFC),
            unfocusedContainerColor = Color(0xFFF8FAFC),
            focusedBorderColor = VocabColors.BrandPrimary,
            unfocusedBorderColor = Color(0xFFE2E8F0)
        )
    )
}

@Composable
private fun FormDropdownSelector(
    selectedText: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth().height(48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedText,
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                color = Color(0xFF1E293B)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
