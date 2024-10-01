package com.semonemo.data.repository

import com.semonemo.data.network.api.AssetApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.data.util.toMultiPart
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Asset
import com.semonemo.domain.repository.AssetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class AssetRepositoryImpl
    @Inject
    constructor(
        private val api: AssetApi,
    ) : AssetRepository {
        override suspend fun registerAsset(image: File): Flow<ApiResponse<Asset>> =
            flow {
                val requestFile = image.toMultiPart()
                emit(
                    emitApiResponse(
                        apiResponse = { api.registerAsset(requestFile) },
                        default = Asset(),
                    ),
                )
            }
    }
