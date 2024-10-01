package com.semonemo.data.repository

import android.util.Log
import com.semonemo.data.network.api.NftApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Nft
import com.semonemo.domain.repository.NftRepository
import com.semonemo.domain.request.PublishNftRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NftRepositoryImpl
    @Inject
    constructor(
        private val api: NftApi,
    ) : NftRepository {
        override suspend fun publishNft(request: PublishNftRequest): Flow<ApiResponse<Nft>> =
            flow {
                val response =
                    emitApiResponse(apiResponse = { api.publishNft(request) }, default = Nft())
                Log.d("jaehan", "publish nft : $response")
                emit(response)
            }
    }
