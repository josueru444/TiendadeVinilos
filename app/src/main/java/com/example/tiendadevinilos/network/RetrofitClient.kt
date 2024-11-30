package com.example.tiendadevinilos.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //private const val BASE_URL = "http://192.168.0.14:8000/"
    //private const val BASE_URL = "http://192.168.46.246:8000/"
    private const val BASE_URL = "https://8000-idx-server-tienda-vinilos-1732669974277.cluster-wfwbjypkvnfkaqiqzlu3ikwjhe.cloudworkstations.dev/"
    private const val AUTH_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2Nsb3VkLmdvb2dsZS5jb20vd29ya3N0YXRpb25zIiwiYXVkIjoiaWR4LXNlcnZlci10aWVuZGEtdmluaWxvcy0xNzMyNjY5OTc0Mjc3LmNsdXN0ZXItd2Z3Ymp5cGt2bmZrYXFpcXpsdTNpa3dqaGUuY2xvdWR3b3Jrc3RhdGlvbnMuZGV2IiwiaWF0IjoxNzMyNzYwMjEwLCJleHAiOjE3MzI4NDY2MTB9.I3fyKeBfiFIehk-rSc89e28FcRPUtTGB2-FYMzTJ5OR6ZUSSRPtkK68Dt_aZdq7SXulPk7-cSix41mQ5_zWdDmEa7Vb4m0l2MbuKBLwzp0VahUjxDkXEy3UdElYAHQ0wpw4Q-csuL9vvvDtMk6lOOD3BpZCnKRozDHV_X-7xQUUuSjbbMMrsJKHrXZssgYml2Ftbq_SBCyPf4-Fe3mFiijORlsZUQOsCR77smG-UeHA0katwTjsoY3OSj07ai8aMU5j-TFcCtR5n34Km1v8yIcO7rzk88mRYxqqwZrrkaCo_OuphEhIJwrZ8_h3Epwc2ok4UcmCe-0Xrg84KCfW2iA"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestWithToken = original.newBuilder()
                .header("Authorization", "Bearer $AUTH_TOKEN")
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
