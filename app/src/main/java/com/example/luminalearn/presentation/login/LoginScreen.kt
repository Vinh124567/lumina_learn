package com.example.luminalearn.presentation.login

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luminalearn.R
import com.example.luminalearn.presentation.common.AppTextField
import com.example.luminalearn.presentation.main.AppDestination
import com.example.luminalearn.ui.theme.TextSecondary
import kotlinx.coroutines.flow.collectLatest

import com.example.luminalearn.presentation.common.AppScaffold

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

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

    AppScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BouncingImage()
            Text(
                text = "Lumina Learn",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary

            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Học tập vi mô 5 phút mỗi ngày & khơi \n nguồn cảm hứng sáng tạo",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(modifier = Modifier.height(30.dp))
            AppTextField(
                value = state.email,
                onValueChange = { viewModel.processIntent(LoginUiIntent.EmailChanged(it)) },
                label = "Email",
                isError = state.emailError != null,
                errorMessage = state.emailError,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppTextField(
                value = state.password,
                onValueChange = { viewModel.processIntent(LoginUiIntent.PasswordChanged(it)) },
                label = "Mật khẩu",
                isError = state.passwordError != null,
                errorMessage = state.passwordError,
                isPassword = true,
                keyboardType = KeyboardType.Password,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.processIntent(LoginUiIntent.LoginClicked) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(45.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Đăng nhập")
                }
            }
        }
    }
}


@Composable
fun BouncingImage() {
    val infiniteTransition = rememberInfiniteTransition(label = "bounce")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -30f, // nảy lên 30dp
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse // đi lên rồi tự quay xuống
        ),
        label = "offsetY"
    )

    Image(
        painter = painterResource(id = R.drawable.banner_login),
        contentDescription = "Ảnh nảy",
        modifier = Modifier
            .size(250.dp)
            .offset(y = offsetY.dp)
    )
}