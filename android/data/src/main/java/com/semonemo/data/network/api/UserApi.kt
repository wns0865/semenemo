package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.domain.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {
    @GET("api/user/me")
    suspend fun loadMyInfo(): BaseResponse<User>

    @PUT("api/user")
    @Multipart
    suspend fun edit(
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Unit>

    @DELETE("api/user")
    suspend fun delete(): BaseResponse<Unit>

    @GET("api/user/{userId}/following")
    suspend fun loadFollowing(
        @Path("userId") userId: Long,
    ): BaseResponse<List<User>>

    @GET("api/user/{userId}/followers")
    suspend fun loadFollowers(
        @Path("userId") userId: Long,
    ): BaseResponse<List<User>>
}
