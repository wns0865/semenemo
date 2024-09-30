package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.User
import com.semonemo.domain.request.EditUserRequest
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    suspend fun edit(
        profileImage: File? = null,
        request: EditUserRequest,
    ): Flow<ApiResponse<Unit>>

    suspend fun loadUserInfo(): Flow<ApiResponse<User>>

    suspend fun delete(): Flow<ApiResponse<Unit>>
}
