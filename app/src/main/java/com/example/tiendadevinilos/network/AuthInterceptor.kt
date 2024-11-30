package com.example.tiendadevinilos.network

import okhttp3.Interceptor
import okhttp3.Response

private class AuthInterceptor(private val csrfToken: String, private val authToken: String) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestWithTokens = original.newBuilder()
            .header("X-CSRF-TOKEN", csrfToken)
            .header("Authorization", "TPW9cWC0J6eAuXsgX90nPXjLEZbrSTE5XJcqBFsM")
            .method(original.method, original.body)
            .build()

        return chain.proceed(requestWithTokens)
    }
}
