package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.domain.model.User
import retrofit2.http.GET

interface UserApi {
    @GET("api/user/me")
    suspend fun loadMyInfo(): BaseResponse<User>
}
