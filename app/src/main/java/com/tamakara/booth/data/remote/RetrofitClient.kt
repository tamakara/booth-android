// kotlin
package com.tamakara.booth.data.remote

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val gson = GsonBuilder().setLenient().create()

    private var apiServiceInternal: ApiService? = null

    fun init(context: Context) {
        if (apiServiceInternal != null) return

        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(prefs))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiServiceInternal = retrofit.create(ApiService::class.java)
    }

    fun apiService(): ApiService {
        return apiServiceInternal
            ?: throw IllegalStateException("RetrofitClient 未初始化，调用 RetrofitClient.init(context) 在 Application.onCreate 或仓库 init 中")
    }
}
