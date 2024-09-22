package com.semonemo.data.network.response

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.ErrorResponse
import java.io.IOException

data class BaseResponse<T>(
    val code: String,
    val data: T,
    val message: String,
)

suspend fun <T> emitApiResponse(apiResponse: suspend () -> BaseResponse<T>): ApiResponse<T> =
    runCatching {
        apiResponse()
    }.fold(
        onSuccess = { result ->
            ApiResponse.Success(data = result.data)
        },
        onFailure = { e ->
            when (e) {
                is ApiException -> ApiResponse.Error(e.error)
                is IOException ->
                    ApiResponse.Error(
                        ErrorResponse("Network Error", e.message ?: ""),
                    )

                else -> ApiResponse.Error(ErrorResponse("Unhandled Error", e.message ?: ""))
            }
        },
    )
