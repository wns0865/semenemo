package com.semonemo.data.repository

import com.semonemo.data.network.api.UserApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.User
import com.semonemo.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val api: UserApi,
    ) : UserRepository {
        override suspend fun loadUserInfo(): Flow<ApiResponse<User>> =
            flow {
                emit(emitApiResponse(apiResponse = { api.loadMyInfo() }, default = User()))
            }
    }
