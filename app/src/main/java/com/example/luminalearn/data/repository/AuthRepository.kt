package com.example.luminalearn.data.repository

import com.example.luminalearn.data.local.TokenManager
import com.example.luminalearn.data.model.AuthResponseDto
import com.example.luminalearn.data.model.LoginRequestDto
import com.example.luminalearn.data.model.RefreshTokenRequestDto
import com.example.luminalearn.data.remote.RetrofitClient
import com.example.luminalearn.data.remote.api.AuthApiService

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResponseDto>
    suspend fun refreshToken(): Result<AuthResponseDto>
    fun saveTokens(accessToken: String, refreshToken: String)
    fun saveUserInfo(email: String, fullName: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun clearTokens()
    fun isLoggedIn(): Boolean
}

class AuthRepositoryImpl(
    private val authApiService: AuthApiService = RetrofitClient.authApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResponseDto> {
        return try {
            val response = authApiService.login(LoginRequestDto(email, password))
            if (response.isSuccessful && response.body()?.data != null) {
                val authData = response.body()!!.data!!
                saveTokens(authData.accessToken, authData.refreshToken)
                saveUserInfo(authData.user.email, authData.user.fullName)
                Result.success(authData)
            } else {
                val errorMsg = response.body()?.message ?: response.errorBody()?.string() ?: "Đăng nhập thất bại"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(): Result<AuthResponseDto> {
        return try {
            val currentRefreshToken = getRefreshToken()
                ?: return Result.failure(IllegalStateException("Không tìm thấy Refresh Token"))

            val response = authApiService.refreshToken(RefreshTokenRequestDto(currentRefreshToken))
            if (response.isSuccessful && response.body()?.data != null) {
                val authData = response.body()!!.data!!
                saveTokens(authData.accessToken, authData.refreshToken)
                Result.success(authData)
            } else {
                val errorMsg = response.body()?.message ?: "Làm mới token thất bại"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        tokenManager.saveTokens(accessToken, refreshToken)
    }

    override fun saveUserInfo(email: String, fullName: String) {
        tokenManager.saveUserInfo(email, fullName)
    }

    override fun getAccessToken(): String? = tokenManager.getAccessToken()

    override fun getRefreshToken(): String? = tokenManager.getRefreshToken()

    override fun clearTokens() {
        tokenManager.clearTokens()
    }

    override fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}
