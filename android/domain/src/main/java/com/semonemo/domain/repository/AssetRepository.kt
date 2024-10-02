package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Asset
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AssetRepository {
    suspend fun registerAsset(image: File): Flow<ApiResponse<Asset>>

    suspend fun getMyAssets(cursorId: Long?): Flow<ApiResponse<List<Asset>>>
}
