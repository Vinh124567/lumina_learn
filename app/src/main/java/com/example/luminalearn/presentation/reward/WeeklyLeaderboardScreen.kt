package com.example.luminalearn.presentation.reward

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.data.model.LeaderboardUserDto
import com.example.luminalearn.presentation.lesson.LessonViewModel
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val GoldAmber = Color(0xFFF59E0B)
private val BrandIndigo = Color(0xFF4F46E5)
private val BrandIndigoLight = Color(0xFF6366F1)
private val BackgroundColor = Color(0xFFF6F8FB)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderHairline = Color(0xFFE2E8F0)

/**
 * Màn hình Bảng Xếp Hạng Tuần & Giải Đấu (Weekly League Leaderboard).
 * Thiết kế chuẩn Luxury Aesthetics: Hairline 0.5dp, Soft Elevation, Pull-to-Refresh & Empty State.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyLeaderboardScreen(
    onNavigateToLesson: () -> Unit,
    modifier: Modifier = Modifier,
    lessonViewModel: LessonViewModel = viewModel()
) {
    val uiState by lessonViewModel.uiState.collectAsState()
    val leaderboard = uiState.leaderboard
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lessonViewModel.loadLeaderboard()
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            isRefreshing = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding()
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                lessonViewModel.loadLeaderboard()
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Banner Giải Đấu Sang Trọng
                LeaderboardLeagueBanner(
                    leagueTier = leaderboard?.leagueTier ?: "HẠNG VÀNG (GOLD)",
                    daysRemaining = leaderboard?.daysRemaining ?: 3
                )

                val users = leaderboard?.users ?: emptyList()

                if (users.isEmpty() && !uiState.isLoading) {
                    // Trạng thái chưa có người xếp hạng (Empty State)
                    LeaderboardEmptyState(onStartLearning = onNavigateToLesson)
                } else {
                    val top3 = users.take(3)
                    val rest = users.drop(3)

                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Podium Top 3 người dẫn đầu
                        if (top3.isNotEmpty()) {
                            item {
                                PodiumSection(top3 = top3)
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }

                        // Vạch kẻ ranh giới thăng hạng (Promotion Line)
                        item {
                            PromotionThresholdIndicator()
                        }

                        // Danh sách người dùng từ hạng 4 trở đi
                        items(rest, key = { it.userId.ifBlank { "${it.rank}_${it.fullName}" } }) { user ->
                            LeaderboardUserRow(user = user)
                        }

                        // Nút CTA nhanh ở cuối danh sách: Học ngay để leo hạng
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = BrandIndigo,
                                shadowElevation = 2.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .bounceClick { onNavigateToLesson() }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "HỌC BÀI NGAY ĐỂ LEO HẠNG 🚀",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 13.5.sp,
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

/**
 * Banner giải đấu phong cách Gradient mềm với Hairline Divider
 */
@Composable
private fun LeaderboardLeagueBanner(
    leagueTier: String,
    daysRemaining: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFBEB).copy(alpha = 0.5f),
                            Color.White
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "🏆", fontSize = 18.sp)
                Text(
                    text = "GIẢI ĐẤU $leagueTier",
                    fontFamily = PlusJakartaSans,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldAmber,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Kết thúc sau $daysRemaining ngày nữa • Top 3 xuất sắc nhất được thăng hạng!",
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted
            )
        }
    }
}

/**
 * Bục Podium Top 3 người xuất sắc nhất
 */
@Composable
private fun PodiumSection(top3: List<LeaderboardUserDto>) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(0.5.dp, BorderHairline.copy(alpha = 0.6f)),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            // Hạng 2 (Bạc)
            if (top3.size >= 2) {
                PodiumColumn(user = top3[1], rank = 2, height = 90, color = Color(0xFF94A3B8), medal = "🥈")
            }
            // Hạng 1 (Vàng / Vương miện)
            if (top3.isNotEmpty()) {
                PodiumColumn(user = top3[0], rank = 1, height = 122, color = GoldAmber, medal = "👑")
            }
            // Hạng 3 (Đồng)
            if (top3.size >= 3) {
                PodiumColumn(user = top3[2], rank = 3, height = 75, color = Color(0xFFD97706), medal = "🥉")
            }
        }
    }
}

@Composable
private fun PodiumColumn(user: LeaderboardUserDto, rank: Int, height: Int, color: Color, medal: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(96.dp)
    ) {
        Text(text = medal, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f))
                .border(1.5.dp, color.copy(alpha = 0.7f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.fullName.take(1).uppercase(),
                fontFamily = PlusJakartaSans,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (user.isCurrentUser) "Bạn" else user.fullName,
            fontFamily = PlusJakartaSans,
            fontSize = 12.sp,
            fontWeight = if (user.isCurrentUser) FontWeight.Black else FontWeight.Bold,
            color = if (user.isCurrentUser) BrandIndigo else TextMain,
            maxLines = 1
        )
        Text(
            text = "${user.weeklySparks} ✨",
            fontFamily = PlusJakartaSans,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAmber
        )
        Spacer(modifier = Modifier.height(6.dp))
        // Khối bục với góc bo mềm phía trên
        Box(
            modifier = Modifier
                .width(76.dp)
                .height(height.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                fontFamily = PlusJakartaSans,
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}

/**
 * Chỉ báo ranh giới khu vực thăng hạng
 */
@Composable
private fun PromotionThresholdIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(0.5.dp)
                .background(Color(0xFF10B981).copy(alpha = 0.6f))
        )
        Text(
            text = " ▲ KHU VỰC THĂNG HẠNG ▲ ",
            fontFamily = PlusJakartaSans,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF10B981),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(0.5.dp)
                .background(Color(0xFF10B981).copy(alpha = 0.6f))
        )
    }
}

/**
 * Hàng thông tin học viên từ hạng 4 trở đi
 */
@Composable
private fun LeaderboardUserRow(user: LeaderboardUserDto) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (user.isCurrentUser) Color(0xFFEEF2FF) else Color.White,
        border = BorderStroke(
            width = 0.5.dp,
            color = if (user.isCurrentUser) BrandIndigoLight.copy(alpha = 0.4f) else BorderHairline.copy(alpha = 0.5f)
        ),
        shadowElevation = 0.5.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${user.rank}",
                fontFamily = PlusJakartaSans,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = if (user.isCurrentUser) BrandIndigo else TextMuted,
                modifier = Modifier.width(36.dp)
            )

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (user.isCurrentUser) BrandIndigoLight else Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.fullName.take(1).uppercase(),
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (user.isCurrentUser) Color.White else TextMain
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (user.isCurrentUser) "${user.fullName} (Bạn)" else user.fullName,
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.5.sp,
                    fontWeight = if (user.isCurrentUser) FontWeight.ExtraBold else FontWeight.SemiBold,
                    color = if (user.isCurrentUser) BrandIndigo else TextMain
                )
            }

            Text(
                text = "${user.weeklySparks} ✨",
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAmber
            )
        }
    }
}

/**
 * Giao diện trống (Empty State) khi chưa có ai ghi danh bảng xếp hạng tuần
 */
@Composable
private fun LeaderboardEmptyState(onStartLearning: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🏆", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Tuần Mới Đã Bắt Đầu!",
            fontFamily = PlusJakartaSans,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextMain
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Hãy hoàn thành các bài học đầu tiên để tích lũy Sparks và ghi tên mình lên đỉnh bảng xếp hạng!",
            fontFamily = PlusJakartaSans,
            fontSize = 13.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BrandIndigo,
            shadowElevation = 2.dp,
            modifier = Modifier
                .bounceClick { onStartLearning() }
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Bắt đầu học ngay 🚀",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }
    }
}
