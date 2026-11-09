package com.example.luminalearn.presentation.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.core.ui.effect.breathingGlow
import com.example.luminalearn.core.ui.effect.staggeredEntrance
import com.example.luminalearn.presentation.main.AppDestination
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.flow.collectLatest

private val BrandIndigo = Color(0xFF4F46E5)
private val BrandIndigoLight = Color(0xFF6366F1)
private val BackgroundClean = Color(0xFFF8FAFC)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)
private val ErrorRed = Color(0xFFEF4444)
private val DemoCardBg = Color(0xFFEEF2FF)
private val DemoCardBorder = Color(0xFFC7D2FE)

private const val DEMO_EMAIL = "alex.vance@lumina.io"
private const val DEMO_PASSWORD = "password123"

/**
 * Màn hình Đăng nhập (LoginScreen) tái thiết kế theo phong cách Modern iOS Luxury:
 * - Vầng hào quang ánh sáng mềm mại ở đỉnh (Soft Ambient Aurora)
 * - Biểu tượng thương hiệu LuminaLearn với nhịp thở Vector Motion
 * - Form thông tin Bento Card viền sứ thanh lịch với độ nảy xúc giác
 * - Chuyển động lướt xuất hiện xếp tầng mềm mại (Staggered Entrance)
 * - Nút đăng nhập 1-chạm tài khoản mẫu & đăng nhập qua Google
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFDF8),
                        Color(0xFFFBFDFF),
                        BackgroundClean
                    )
                )
            )
    ) {
        // Quầng sáng cực quang pastel chuyển động mềm mại phía sau (iOS Aurora Mesh)
        AuroraMeshGlow(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // ── 1. TIÊU ĐỀ & BIỂU TƯỢNG THƯƠNG HIỆU ──
            LoginHeader(modifier = Modifier.staggeredEntrance(index = 0))

            Spacer(modifier = Modifier.height(24.dp))

            // ── 2. BENTO CARD FORM ĐĂNG NHẬP ──
            LoginFormCard(
                state = state,
                passwordVisible = passwordVisible,
                onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                onEmailChanged = { viewModel.processIntent(LoginUiIntent.EmailChanged(it)) },
                onPasswordChanged = { viewModel.processIntent(LoginUiIntent.PasswordChanged(it)) },
                onForgotPasswordClick = {
                    Toast.makeText(
                        context,
                        "Vui lòng kiểm tra email để đặt lại mật khẩu.",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onSubmit = {
                    focusManager.clearFocus()
                    viewModel.processIntent(LoginUiIntent.LoginClicked)
                },
                modifier = Modifier.staggeredEntrance(index = 1)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ── 3. NÚT ĐĂNG NHẬP & TRẢI NGHIỆM NHANH 1-CHẠM ──
            LoginActionButtons(
                isLoading = state.isLoading,
                onLoginClick = {
                    focusManager.clearFocus()
                    viewModel.processIntent(LoginUiIntent.LoginClicked)
                },
                onQuickDemoClick = {
                    focusManager.clearFocus()
                    viewModel.processIntent(LoginUiIntent.EmailChanged(DEMO_EMAIL))
                    viewModel.processIntent(LoginUiIntent.PasswordChanged(DEMO_PASSWORD))
                    viewModel.processIntent(LoginUiIntent.LoginClicked)
                },
                modifier = Modifier.staggeredEntrance(index = 2)
            )

            Spacer(modifier = Modifier.height(22.dp))

            // ── 4. ĐĂNG NHẬP QUA GOOGLE ──
            SocialLoginSection(
                onGoogleClick = {
                    focusManager.clearFocus()
                    viewModel.processIntent(LoginUiIntent.EmailChanged(DEMO_EMAIL))
                    viewModel.processIntent(LoginUiIntent.PasswordChanged(DEMO_PASSWORD))
                    viewModel.processIntent(LoginUiIntent.LoginClicked)
                },
                modifier = Modifier.staggeredEntrance(index = 3)
            )

            Spacer(modifier = Modifier.height(26.dp))

            // ── 5. CHÂN TRANG ĐĂNG KÝ & ĐIỀU KHOẢN ──
            LoginFooter(
                onRegisterClick = {
                    Toast.makeText(
                        context,
                        "Bạn có thể dùng nút 'Trải nghiệm nhanh' để đăng nhập ngay!",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier.staggeredEntrance(index = 4)
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Phần Header thương hiệu: Logo nảy sinh động, Badge đẳng cấp, Tiêu đề chính.
 */
@Composable
private fun LoginHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Biểu tượng Logo tròn với viền sứ siêu mảnh và Vector Motion Breathing
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(BorderStroke(0.5.dp, Color(0xFFE0E7FF).copy(alpha = 0.6f)), CircleShape)
                    .breathingGlow(minScale = 0.96f, maxScale = 1.05f, durationMillis = 2200),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher),
                    contentDescription = "LuminaLearn Logo",
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                )
            }

            // Chip huy hiệu hệ sinh thái phẳng không viền
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFEEF2FF))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "✨ HSK SMART AI",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandIndigo
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Đăng nhập",
            fontFamily = PlusJakartaSans,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextMain
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Khơi mở tiềm năng ngôn ngữ cùng trợ lý học tập AI thông minh.",
            fontFamily = PlusJakartaSans,
            fontSize = 14.sp,
            lineHeight = 19.sp,
            color = TextMuted
        )
    }
}

/**
 * Thẻ Bento chứa form nhập liệu: Email và Mật khẩu.
 */
@Composable
private fun LoginFormCard(
    state: LoginUiState,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // ── Field: Email ──
            Text(
                text = "Email",
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMain
            )

            Spacer(modifier = Modifier.height(7.dp))

            TextField(
                value = state.email,
                onValueChange = onEmailChanged,
                placeholder = {
                    Text(
                        text = "name@lumina.io",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.5.sp,
                        color = Color(0xFF94A3B8)
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_mail),
                        contentDescription = "Email",
                        tint = if (state.emailError != null) ErrorRed
                        else if (state.email.isNotBlank()) BrandIndigo
                        else Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )
                },
                isError = state.emailError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F5F9),
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    disabledContainerColor = Color(0xFFF8FAFC),
                    errorContainerColor = Color(0xFFFEF2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain
                ),
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(
                visible = state.emailError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = state.emailError ?: "",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = ErrorRed,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Field: Password ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mật khẩu",
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMain
                )

                Text(
                    text = "Quên mật khẩu?",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandIndigo,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(onClick = onForgotPasswordClick)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            TextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                placeholder = {
                    Text(
                        text = "••••••••",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.5.sp,
                        color = Color(0xFF94A3B8)
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = "Mật khẩu",
                        tint = if (state.passwordError != null) ErrorRed
                        else if (state.password.isNotBlank()) BrandIndigo
                        else Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    val visibilityIcon = if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onPasswordVisibilityToggle),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = visibilityIcon),
                            contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(19.dp)
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
                keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F5F9),
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    disabledContainerColor = Color(0xFFF8FAFC),
                    errorContainerColor = Color(0xFFFEF2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain
                ),
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(
                visible = state.passwordError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = state.passwordError ?: "",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = ErrorRed,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }
        }
    }
}

/**
 * Cụm nút bấm hành động: Nút đăng nhập chính và Nút thử nghiệm nhanh 1-chạm.
 */
@Composable
private fun LoginActionButtons(
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onQuickDemoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Nút chính: Solid Indigo với hiệu ứng nảy Tactile Spring
        Button(
            onClick = onLoginClick,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .bounceClick(scaleDown = 0.97f),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandIndigo,
                disabledContainerColor = BrandIndigo.copy(alpha = 0.55f)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Đăng nhập ngay",
                        fontFamily = PlusJakartaSans,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "→",
                        fontFamily = PlusJakartaSans,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Nút 1-chạm: Đăng nhập tài khoản mẫu demo phẳng không viền
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(DemoCardBg)
                .bounceClick(scaleDown = 0.98f, onClick = onQuickDemoClick),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = null,
                    tint = BrandIndigo,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Trải nghiệm nhanh (Tài khoản mẫu)",
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandIndigo
                )
            }
        }
    }
}

/**
 * Mục Đăng nhập mạng xã hội (Google).
 */
@Composable
private fun SocialLoginSection(
    onGoogleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(0.5.dp)
                    .background(BorderSubtle.copy(alpha = 0.6f))
            )
            Text(
                text = "hoặc tiếp tục với",
                fontFamily = PlusJakartaSans,
                fontSize = 12.5.sp,
                color = TextMuted,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(0.5.dp)
                    .background(BorderSubtle.copy(alpha = 0.6f))
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .border(BorderStroke(0.5.dp, BorderSubtle.copy(alpha = 0.6f)), RoundedCornerShape(14.dp))
                .bounceClick(scaleDown = 0.98f, onClick = onGoogleClick),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(9.dp))
                Text(
                    text = "Tiếp tục với Google",
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMain
                )
            }
        }
    }
}

/**
 * Chân trang đăng ký và tuyên bố bảo mật.
 */
@Composable
private fun LoginFooter(
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chưa có tài khoản? ",
                fontFamily = PlusJakartaSans,
                fontSize = 13.5.sp,
                color = TextMuted
            )
            Text(
                text = "Đăng ký ngay",
                fontFamily = PlusJakartaSans,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = BrandIndigo,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onRegisterClick)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Bằng cách tiếp tục, bạn đồng ý với Điều khoản dịch vụ và Chính sách bảo mật của LuminaLearn.",
            fontFamily = PlusJakartaSans,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            color = Color(0xFF94A3B8),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

/**
 * Quầng sáng cực quang đa sắc chuyển động mềm mại (Aurora Mesh Glow) chuẩn iOS 18:
 * - 3 khối màu pastel (Indigo dịu, Champagne ấm, Rose/Violet blush) hòa sắc mượt mà vào nền trắng ngọc trai.
 * - 100% Radial Gradients siêu nhòe, không góc cạnh, không tia sắc, không gây chói hay rối mắt.
 * - Nhịp thở trôi dạt êm ái (Fluid drifting & gentle breathing) tạo chiều sâu không gian cao cấp và sống động.
 */
@Composable
private fun AuroraMeshGlow(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora_transition")

    // Dịch chuyển chậm của khối cực quang 1 (Indigo / Sky Blue)
    val drift1X by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 22f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aurora_drift1_x"
    )
    val drift1Y by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(
            animation = tween(8500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aurora_drift1_y"
    )

    // Dịch chuyển chậm của khối cực quang 2 (Champagne Gold ấm)
    val drift2X by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = -18f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aurora_drift2_x"
    )
    val drift2Y by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = -14f,
        animationSpec = infiniteRepeatable(
            animation = tween(6500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aurora_drift2_y"
    )

    // Nhịp thở co giãn nhẹ nhàng
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(5500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aurora_breathing"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(480.dp)
            .graphicsLayer { alpha = 0.90f }
    ) {
        val w = size.width

        // 1. Khối cực quang Indigo / Sky Blue pastel góc trên bên phải
        val center1 = Offset(w * 0.88f + drift1X, -10f + drift1Y)
        val radius1 = w * 0.72f * breathingScale
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE0E7FF).copy(alpha = 0.75f),
                    Color(0xFFBAE6FD).copy(alpha = 0.40f),
                    Color(0xFFC7D2FE).copy(alpha = 0.15f),
                    Color.Transparent
                ),
                center = center1,
                radius = radius1
            ),
            radius = radius1,
            center = center1
        )

        // 2. Khối cực quang Champagne / Warm Gold ở phía trên giữa (tạo điểm nhấn ấm áp)
        val center2 = Offset(w * 0.46f + drift2X, 35f + drift2Y)
        val radius2 = w * 0.62f / breathingScale
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFEF3C7).copy(alpha = 0.60f),
                    Color(0xFFFDE68A).copy(alpha = 0.28f),
                    Color(0xFFFFFBEB).copy(alpha = 0.10f),
                    Color.Transparent
                ),
                center = center2,
                radius = radius2
            ),
            radius = radius2,
            center = center2
        )

        // 3. Khối cực quang Soft Rose / Violet blush phớt nhẹ ở góc trên bên trái
        val center3 = Offset(-15f + (drift1X * 0.4f), 15f + (drift2Y * 0.4f))
        val radius3 = w * 0.54f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFCE7F3).copy(alpha = 0.45f),
                    Color(0xFFEDE9FE).copy(alpha = 0.20f),
                    Color.Transparent
                ),
                center = center3,
                radius = radius3
            ),
            radius = radius3,
            center = center3
        )
    }
}