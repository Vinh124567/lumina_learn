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

import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            BouncingImage()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(28.dp))

            AppTextField(
                value = state.email,
                onValueChange = { viewModel.processIntent(LoginUiIntent.EmailChanged(it)) },
                label = stringResource(R.string.label_email),
                isError = state.emailError != null,
                errorMessage = state.emailError,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            AppTextField(
                value = state.password,
                onValueChange = { viewModel.processIntent(LoginUiIntent.PasswordChanged(it)) },
                label = stringResource(R.string.label_password),
                isError = state.passwordError != null,
                errorMessage = state.passwordError,
                isPassword = true,
                keyboardType = KeyboardType.Password,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { viewModel.processIntent(LoginUiIntent.LoginClicked) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5C50F6),
                    disabledContainerColor = Color(0xFF5C50F6).copy(alpha = 0.6f)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.btn_login),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
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
        contentDescription = stringResource(R.string.cd_bouncing_image),
        modifier = Modifier
            .size(250.dp)
            .offset(y = offsetY.dp)
    )
}