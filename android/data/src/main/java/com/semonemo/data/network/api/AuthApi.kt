package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthApi {
    @GET("api/auth/exist")
    suspend fun exists(
        @Query("address") address: String,
    ): BaseResponse<Boolean>
}
