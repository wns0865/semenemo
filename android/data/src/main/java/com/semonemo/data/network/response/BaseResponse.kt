package com.semonemo.data.network.response

import com.semonemo.domain.model.ApiResponse
import java.io.IOException

data class BaseResponse<T>(
    val code: String,
    val data: T?,
    val message: String,
)

suspend fun <T> emitApiResponse(
    apiResponse: suspend () -> BaseResponse<T>,
    default: T,
): ApiResponse<T> =
    runCatching {
        apiResponse()
    }.fold(
        onSuccess = { result ->
            ApiResponse.Success(data = result.data ?: default)
        },
        onFailure = { e ->
            when (e) {
                is ApiException ->
                    ApiResponse.Error(
                        errorCode = e.error.errorCode,
                        errorMessage = e.error.message,
                    )

                is IOException ->
                    ApiResponse.Error(
                        errorCode = "E001",
                        errorMessage = e.message ?: "",
                    )

                else ->
                    ApiResponse.Error(
                        errorCode = "E002",
                        errorMessage = e.message ?: "",
                    )
            }
        },
    )
