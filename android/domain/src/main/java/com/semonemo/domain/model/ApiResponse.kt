package com.semonemo.domain.model

sealed class ApiResponse<out D> {
    data class Success<out D>(
        val data: D,
    ) : ApiResponse<D>()

    data class Error(
        val errorCode: String,
        val errorMessage: String,
    ) : ApiResponse<Nothing>()
}
