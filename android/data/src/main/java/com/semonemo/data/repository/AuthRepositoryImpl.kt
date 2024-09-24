package com.semonemo.data.repository

import com.google.gson.Gson
import com.semonemo.data.network.api.AuthApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AuthRepository
import com.semonemo.domain.request.SignUpRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val api: AuthApi,
    ) : AuthRepository {
        override suspend fun exists(address: String): Flow<ApiResponse<Boolean>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.exists(address) },
                        default = false,
                    )
                emit(response)
            }

        override suspend fun validateNickname(nickname: String): Flow<ApiResponse<Boolean>> =
            flow {
                val response =
                    emitApiResponse(apiResponse = {
                        api.validateNickname(nickname)
                    }, default = false)
                emit(response)
            }

        override suspend fun signUp(
            data: SignUpRequest,
            profileImage: File,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val requestFile = profileImage.asRequestBody("image/*".toMediaTypeOrNull())
                val image = MultipartBody.Part.createFormData("image", profileImage.name, requestFile)
                val requestBody =
                    Gson().toJson(data).toRequestBody("application/json".toMediaTypeOrNull())
                val response =
                    emitApiResponse(apiResponse = {
                        api.signUp(
                            image = image,
                            data = requestBody,
                        )
                    }, default = Unit)
                emit(response)
            }
    }
