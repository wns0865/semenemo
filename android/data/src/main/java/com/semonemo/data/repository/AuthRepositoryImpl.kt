package com.semonemo.data.repository

import com.semonemo.data.network.api.AuthApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val api: AuthApi,
    ) : AuthRepository {
        override suspend fun exists(address: String): Flow<ApiResponse<Boolean>> =
            flow {
                val response =
                    emitApiResponse {
                        api.exists(address)
                    }
                emit(response)
            }
    }
