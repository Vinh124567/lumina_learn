package com.example.luminalearn.presentation.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.main.AppDestination
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.flow.collectLatest

/**
 * Màn hình Đăng nhập (LoginScreen) thiết kế theo chuẩn Clean Native quốc tế (Duolingo, Elsa, Airbnb).
 * - Full-bleed phẳng, không đóng khung hộp lơ lửng.
 * - Phân cấp Typography rõ ràng, khoảng trắng (whitespace) tự nhiên.
 * - Solid brand button dứt khoát kết hợp đăng nhập 1 chạm qua Google chuẩn quốc tế.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is LoginUiEffect.NavigateToMain -> {
                    navController.navigate(AppDestination.Main.route) {
                        popUpTo(AppDestination.Login.route) { inclusive = true }
                    }
                }
                is LoginUiEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // ── 1. TOP BAR: NÚT BACK GỌN GÀNG ──
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable {
                    if (!navController.popBackStack()) {
                        // Nếu không còn màn trước đó, vẫn giữ ở màn đăng nhập
                    }
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Quay lại",
                tint = Color(0xFF0F172A),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── 2. TIÊU ĐỀ CHÍNH ──
        Text(
            text = "Đăng nhập",
            fontFamily = PlusJakartaSans,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Chào mừng bạn trở lại với LuminaLearn.",
            fontFamily = PlusJakartaSans,
            fontSize = 14.5.sp,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // ── 3. FORM NHẬP LIỆU ──
        // Nhãn Email
        Text(
            text = "Email",
            fontFamily = PlusJakartaSans,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E293B)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.processIntent(LoginUiIntent.EmailChanged(it)) },
            placeholder = {
                Text(
                    text = "Nhập địa chỉ email",
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.sp,
                    color = Color(0xFF94A3B8)
                )
            },
            isError = state.emailError != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                errorContainerColor = Color(0xFFFEF2F2),
                focusedBorderColor = Color(0xFF4F46E5),
                unfocusedBorderColor = Color(0xFFE2E8F0),
                errorBorderColor = Color(0xFFEF4444),
                focusedTextColor = Color(0xFF0F172A),
                unfocusedTextColor = Color(0xFF0F172A)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (state.emailError != null) {
            Text(
                text = state.emailError ?: "",
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                color = Color(0xFFEF4444),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Nhãn Mật khẩu + Quên mật khẩu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mật khẩu",
                fontFamily = PlusJakartaSans,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
            )

            Text(
                text = "Quên mật khẩu?",
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4F46E5),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        Toast.makeText(
                            context,
                            "Vui lòng kiểm tra email để đặt lại mật khẩu.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.processIntent(LoginUiIntent.PasswordChanged(it)) },
            placeholder = {
                Text(
                    text = "Nhập mật khẩu",
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.sp,
                    color = Color(0xFF94A3B8)
                )
            },
            trailingIcon = {
                val visibilityIcon = if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { passwordVisible = !passwordVisible },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = visibilityIcon),
                        contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = state.passwordError != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    viewModel.processIntent(LoginUiIntent.LoginClicked)
                }
            ),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                errorContainerColor = Color(0xFFFEF2F2),
                focusedBorderColor = Color(0xFF4F46E5),
                unfocusedBorderColor = Color(0xFFE2E8F0),
                errorBorderColor = Color(0xFFEF4444),
                focusedTextColor = Color(0xFF0F172A),
                unfocusedTextColor = Color(0xFF0F172A)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (state.passwordError != null) {
            Text(
                text = state.passwordError ?: "",
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                color = Color(0xFFEF4444),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── 4. NÚT ĐĂNG NHẬP CHÍNH (SOLID INDIGO) ──
        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.processIntent(LoginUiIntent.LoginClicked)
            },
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .bounceClick(scaleDown = 0.98f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F46E5),
                disabledContainerColor = Color(0xFF4F46E5).copy(alpha = 0.6f)
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Đăng nhập",
                    fontFamily = PlusJakartaSans,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── 5. VẠCH NGĂN CÁCH HOẶC ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color(0xFFE2E8F0))
            )
            Text(
                text = "hoặc",
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                color = Color(0xFF94A3B8),
                modifier = Modifier.padding(horizontal = 14.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color(0xFFE2E8F0))
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── 6. NÚT TIẾP TỤC VỚI GOOGLE (CHUẨN QUỐC TẾ) ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(12.dp))
                .bounceClick(scaleDown = 0.98f) {
                    // Đăng nhập nhanh tài khoản demo qua Google 1 chạm
                    viewModel.processIntent(LoginUiIntent.EmailChanged("alex.vance@lumina.io"))
                    viewModel.processIntent(LoginUiIntent.PasswordChanged("12345678"))
                    viewModel.processIntent(LoginUiIntent.LoginClicked)
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Tiếp tục với Google",
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B)
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // ── 7. CHÂN TRANG: ĐĂNG KÝ ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chưa có tài khoản? ",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                color = Color(0xFF64748B)
            )
            Text(
                text = "Đăng ký",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4F46E5),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        Toast.makeText(
                            context,
                            "Bạn có thể dùng ngay nút 'Tiếp tục với Google' để trải nghiệm!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}