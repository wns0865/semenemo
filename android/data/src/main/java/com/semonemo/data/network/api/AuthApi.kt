package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AuthApi {
    @GET("api/auth/exist")
    suspend fun exists(
        @Query("address") address: String,
    ): BaseResponse<Boolean>

    @GET("api/auth/validate")
    suspend fun validateNickname(
        @Query("nickname") nickname: String,
    ): BaseResponse<Boolean>

    @POST("api/auth/register")
    @Multipart
    suspend fun signUp(
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Unit>
}
