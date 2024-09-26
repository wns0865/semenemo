package com.semonemo.data.repository

import com.google.gson.Gson
import com.semonemo.data.datasource.AuthDataSource
import com.semonemo.data.datasource.TokenDataSource
import com.semonemo.data.network.api.AuthApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.JwtToken
import com.semonemo.domain.repository.AuthRepository
import com.semonemo.domain.request.LoginRequest
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
        private val tokenDataSource: TokenDataSource,
        private val authDataSource: AuthDataSource,
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
                if (response is ApiResponse.Success) {
                    authDataSource.savePassword(data.password)
                    authDataSource.saveWalletAddress(data.address)
                }
                emit(response)
            }

        override suspend fun login(request: LoginRequest): Flow<ApiResponse<JwtToken>> =
            flow {
                val response =
                    emitApiResponse(apiResponse = { api.login(request) }, default = JwtToken())
                if (response is ApiResponse.Success) {
                    tokenDataSource.saveJwtToken(
                        jwtToken = response.data,
                    )
                    authDataSource.savePassword(request.password)
                    authDataSource.saveWalletAddress(request.address)
                }
                emit(response)
            }

        override suspend fun login(): Flow<ApiResponse<Boolean>> =
            flow {
                val address = authDataSource.getWalletAddress()
                val password = authDataSource.getPassword()
                if (password != null && address != null) {
                    emit(ApiResponse.Success(data = true))
                } else {
                    emit(ApiResponse.Success(data = false))
                }
            }
    }
