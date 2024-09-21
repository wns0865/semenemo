package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun exists(address: String): Flow<ApiResponse<Boolean>>
}
