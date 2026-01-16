package com.ppdm.app.data.remote

import com.ppdm.app.data.local.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenManager.token.first() }
        val request = chain.request().newBuilder()
        if (!token.isNullOrEmpty()) {
            request.addHeader("app-auth", token) // Custom header per your request
        }
        return chain.proceed(request.build())
    }
}