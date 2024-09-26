package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun loadUserInfo(): Flow<ApiResponse<User>>
}
