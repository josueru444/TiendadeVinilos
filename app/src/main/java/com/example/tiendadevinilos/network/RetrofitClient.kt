package com.example.tiendadevinilos.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //private const val BASE_URL = "http://192.168.0.14:8000/"
    //private const val BASE_URL = "http://192.168.46.246:8000/"
    private const val BASE_URL = "https://b95a-2806-265-1400-586-00-2.ngrok-free.app"
    private const val AUTH_TOKEN = "TPW9cWC0J6eAuXsgX90nPXjLEZbrSTE5XJcqBFsM"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestWithToken = original.newBuilder()
                .header("X-CSRF-TOKEN", AUTH_TOKEN)
                .header("Accept", "application/json")
                .header("X-Requested-With", "XMLHttpRequest")
                .method(original.method, original.body)
                .build()
            chain.proceed(requestWithToken)
        }
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
