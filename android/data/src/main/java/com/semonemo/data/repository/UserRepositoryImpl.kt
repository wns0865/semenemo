package com.semonemo.data.repository

import com.semonemo.data.datasource.AuthDataSource
import com.semonemo.data.network.api.UserApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.data.util.toMultiPart
import com.semonemo.data.util.toRequestBody
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.User
import com.semonemo.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val api: UserApi,
        private val authDataSource: AuthDataSource,
    ) : UserRepository {
        override suspend fun edit(
            profileImage: File?,
            nickName: String,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val image = profileImage.toMultiPart()
                val requestBody = nickName.toRequestBody()
                val response =
                    emitApiResponse(apiResponse = {
                        api.edit(
                            image = image,
                            data = requestBody,
                        )
                    }, default = Unit)
                if (response is ApiResponse.Success) {
                    authDataSource.saveNickname(nickName)
                    profileImage?.let{
                        authDataSource.saveProfileImage(it.path)
                    }
                }
                emit(response)
            }

        override suspend fun loadUserInfo(): Flow<ApiResponse<User>> =
            flow {
                emit(emitApiResponse(apiResponse = { api.loadMyInfo() }, default = User()))
            }
    }
