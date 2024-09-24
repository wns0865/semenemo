package com.semonemo.data.network.interceptor

import com.google.gson.Gson
import com.semonemo.data.network.response.ApiException
import com.semonemo.domain.model.ErrorResponse
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorHandlingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            if (response.isSuccessful) return response
            val errorBody = response.body?.string() ?: return response
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(error = errorResponse)
        } catch (e: Throwable) {
            when (e) {
                is ApiException -> throw e
                is IOException -> throw IOException(e)

                else -> throw e
            }
        }
    }
}
