package com.example.luminalearn.data.remote

import com.example.luminalearn.data.local.TokenManager
import com.example.luminalearn.data.remote.api.AuthApiService
import com.example.luminalearn.data.remote.api.LessonApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    /**
     * Cấu hình Base URL:
     * - Máy thật cắm cáp USB (đã chạy adb reverse tcp:8080 tcp:8080): "http://localhost:8080/api/"
     * - Máy ảo Android Emulator: "http://10.0.2.2:8080/api/"
     * - Máy thật kết nối cùng mạng Wi-Fi: "http://10.169.197.243:8080/api/"
     */
    const val BASE_URL = "http://localhost:8080/api/"

    private var tokenManager: TokenManager? = null

    fun init(tokenManager: TokenManager) {
        this.tokenManager = tokenManager
    }

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = tokenManager?.getAccessToken()

        val newRequest = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        chain.proceed(newRequest)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val lessonApiService: LessonApiService by lazy {
        retrofit.create(LessonApiService::class.java)
    }
}
