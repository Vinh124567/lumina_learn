package com.example.luminalearn.presentation.lesson

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.lesson.component.ChineseLessonData
import com.example.luminalearn.presentation.lesson.component.LessonCardItem
import com.example.luminalearn.presentation.lesson.component.LessonFilterRow
import com.example.luminalearn.presentation.lesson.component.LessonHeader
import com.example.luminalearn.presentation.lesson.component.LessonSearchBar

@Composable
fun LessonScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Tất cả") }
    val scrollState = rememberScrollState()

    val filterOptions = listOf(
        "Tất cả",
        stringResource(R.string.filter_pinyin),
        stringResource(R.string.filter_conversation),
        stringResource(R.string.filter_radicals),
        stringResource(R.string.filter_grammar)
    )

    val allLessons = remember {
        listOf(
            ChineseLessonData(
                id = "1",
                category = "Phát âm Pinyin",
                categoryBgColor = Color(0xFFF3E8FF),
                categoryTextColor = Color(0xFF7E22CE),
                level = "Cơ bản",
                pinyinHanziTitle = "四声与变调 (Sì shēng yǔ biàntiào)",
                title = "4 Thanh điệu Pinyin & Quy tắc biến âm",
                description = "Nắm chắc 4 thanh điệu tiếng Trung và quy tắc biến điệu hai thanh 3 kinh điển giúp nói tự nhiên như người bản xứ.",
                durationMins = 4,
                sparks = 30,
                isCompleted = true
            ),
            ChineseLessonData(
                id = "2",
                category = "Giao tiếp thực tế",
                categoryBgColor = Color(0xFFEEF2FF),
                categoryTextColor = Color(0xFF4F46E5),
                level = "HSK 1",
                pinyinHanziTitle = "日常问候 (Rìcháng wènhòu)",
                title = "Câu chào hỏi & Xã giao thường nhật",
                description = "Học 5 mẫu câu chào hỏi, cảm ơn và xin lỗi thông dụng nhất kèm cử chỉ văn hóa chuẩn xác.",
                durationMins = 3,
                sparks = 25,
                isCompleted = true
            ),
            ChineseLessonData(
                id = "3",
                category = "Chữ Hán & Bộ thủ",
                categoryBgColor = Color(0xFFFCE7F3),
                categoryTextColor = Color(0xFFBE185D),
                level = "Cơ bản",
                pinyinHanziTitle = "汉字偏旁部首 (Hànzì piānpáng)",
                title = "Bí kíp Chiết tự: Bộ Nhân, Bộ Nữ, Bộ Mộc",
                description = "Mở khóa hàng trăm chữ Hán chỉ bằng việc hiểu sâu 3 bộ thủ tượng hình quen thuộc nhất.",
                durationMins = 5,
                sparks = 35,
                isCompleted = false
            ),
            ChineseLessonData(
                id = "4",
                category = "Giao tiếp thực tế",
                categoryBgColor = Color(0xFFEEF2FF),
                categoryTextColor = Color(0xFF4F46E5),
                level = "HSK 1",
                pinyinHanziTitle = "数字与购物 (Shùzì yǔ gòuwù)",
                title = "Số đếm 1–10 & Mẹo hỏi giá khi du lịch",
                description = "Học cách đọc số từ 1 đến 10, cấu trúc hỏi giá \"Cái này bao nhiêu tiền\" và cách dùng cử chỉ tay số đếm.",
                durationMins = 4,
                sparks = 30,
                isCompleted = false
            )
        )
    }

    // Lọc bài học theo tìm kiếm và danh mục
    val filteredLessons = allLessons.filter { lesson ->
        val matchesCategory = selectedFilter == "Tất cả" || selectedFilter == "All" ||
                lesson.category.contains(selectedFilter, ignoreCase = true) ||
                selectedFilter.contains(lesson.category, ignoreCase = true)

        val matchesQuery = searchQuery.isBlank() ||
                lesson.title.contains(searchQuery, ignoreCase = true) ||
                lesson.pinyinHanziTitle.contains(searchQuery, ignoreCase = true) ||
                lesson.description.contains(searchQuery, ignoreCase = true)

        matchesCategory && matchesQuery
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // 1. Header: Thư viện Hán ngữ vi mô
            LessonHeader()

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Thanh tìm kiếm bài học
            LessonSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Hàng Filter chip
            LessonFilterRow(
                filters = filterOptions,
                selectedFilter = selectedFilter,
                onFilterSelect = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 4. Danh sách các thẻ bài học
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                filteredLessons.forEach { lesson ->
                    LessonCardItem(
                        lesson = lesson,
                        onActionClick = {
                            Toast.makeText(
                                context,
                                "Bắt đầu: ${lesson.title}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }

            // Khoảng đệm đáy 96dp để không bị Bottom Bar che khuất
            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}
