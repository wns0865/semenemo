package com.semonemo.data.network.interceptor

import com.semonemo.domain.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor
    @Inject
    constructor(
        private val tokenRepository: TokenRepository,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
            val accessToken: String =
                runBlocking {
                    val token: String =
                        tokenRepository.getAccessToken()?.let {
                            it
                        } ?: run {
                            ""
                        }
                    token
                }
            val request =
                requestBuilder
                    .header("Authorization", accessToken)
                    .build()
            return chain.proceed(request)
        }
    }
