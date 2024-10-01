package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.domain.model.Asset
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AssetApi {
    @Multipart
    @POST("api/asset")
    suspend fun registerAsset(
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Asset>
}
