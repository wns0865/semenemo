package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.data.network.response.GetAssetsResponse
import com.semonemo.domain.model.Asset
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AssetApi {
    @Multipart
    @POST("api/asset")
    suspend fun registerAsset(
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Asset>

    @GET("api/asset/mine")
    suspend fun getMyAssets(
        @Query("cursorId") cursorId: Long?,
    ): BaseResponse<GetAssetsResponse>
}
