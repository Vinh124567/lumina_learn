package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.common.LessonCardShimmerItem
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val BrandIndigoLight = Color(0xFF6366F1)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)

/**
 * Component lưu trữ toàn bộ giao diện chế độ Danh sách bài học (Timeline List & Search/Filter).
 * Được đóng gói độc lập để tái sử dụng làm "Thư viện bài học" hoặc "Tra cứu chuyên đề".
 */
@Composable
fun LessonCatalogArchiveView(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    lessons: List<ChineseLessonData>,
    isLoading: Boolean,
    onSelectLesson: (ChineseLessonData) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Thanh tìm kiếm bài học
        LessonSearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange
        )

        // 2. Dải lọc danh mục (Phát âm, Ngữ pháp, Giao tiếp...)
        LessonFilterRow(
            filters = categories,
            selectedFilter = selectedCategory,
            onFilterSelect = onCategorySelect
        )

        // 3. Nội dung danh sách theo trạng thái tải
        when {
            isLoading && lessons.isEmpty() -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(3) {
                        LessonCardShimmerItem()
                    }
                }
            }
            lessons.isEmpty() -> {
                val isFiltering = searchQuery.isNotBlank() || selectedCategory != "Tất cả"
                LessonCatalogEmptyState(
                    title = if (isFiltering) "Không tìm thấy bài học phù hợp" else "Chưa có bài học nào",
                    subtitle = if (isFiltering) "Thử tìm kiếm với từ khóa khác hoặc đổi bộ lọc" else "Vuốt xuống để làm mới danh sách bài học"
                )
            }
            else -> {
                LessonTimelineListView(
                    lessons = lessons,
                    onSelectLesson = onSelectLesson,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun LessonCatalogEmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color(0xFFEEF2FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = null,
                tint = BrandIndigoLight,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = title,
            fontFamily = PlusJakartaSans,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextMain,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = subtitle,
            fontFamily = PlusJakartaSans,
            fontSize = 13.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
