package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.data.network.response.GetAssetsResponse
import com.semonemo.data.network.response.GetCreatorAssetsResponse
import com.semonemo.data.network.response.LikeResponse
import com.semonemo.domain.model.AllSellAssets
import com.semonemo.domain.model.Asset
import com.semonemo.domain.model.CreateAiAsset
import com.semonemo.domain.model.CreateAsset
import com.semonemo.domain.model.SellAsset
import com.semonemo.domain.model.SellAssetDetail
import com.semonemo.domain.request.PurchaseAssetRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AssetApi {
    @Multipart
    @POST("api/asset")
    suspend fun registerAsset(
        @Part image: MultipartBody.Part?,
    ): BaseResponse<CreateAiAsset>

    @GET("api/asset/mine")
    suspend fun getMyAssets(
        @Query("cursorId") cursorId: Long?,
    ): BaseResponse<GetAssetsResponse>

    // 판매 에셋 등록
    @POST("api/asset/sell")
    suspend fun sellAsset(
        @Body asset: SellAsset,
    ): BaseResponse<Unit>

    // 에셋 한 개 상세 조회
    @GET("api/asset/{assetId}/detail")
    suspend fun getAssetDetail(
        @Path("assetId") assetId: Long,
    ): BaseResponse<Asset>

    // 판매 에셋 상세 조회
    @GET("api/asset/sell/{assetSellId}/detail")
    suspend fun getSellAssetDetail(
        @Path("assetSellId") assetSellId: Long,
    ): BaseResponse<SellAssetDetail>

    // 판매 중 에셋 전체 조회
    @GET("api/asset/sort")
    suspend fun getAllSellAssets(
        @Query("orderBy") option: String,
    ): BaseResponse<AllSellAssets>

    // 유저 제작 에셋 조회
    @GET("api/asset/user")
    suspend fun getCreateAssets(
        @Query("userId") userId: Long,
    ): BaseResponse<CreateAsset>

    // 에셋 좋아요
    @POST("api/asset/{assetSellId}/like")
    suspend fun likeAsset(
        @Path("assetSellId") assetSellId: Long,
    ): BaseResponse<LikeResponse>

    // 에셋 좋아요 삭제
    @DELETE("api/asset/{assetSellId}/like")
    suspend fun unlikeAsset(
        @Path("assetSellId") assetSellId: Long,
    ): BaseResponse<LikeResponse>

    // 에셋 자세히 보기
    @GET("api/asset/sell/{assetSellId}/detail")
    suspend fun getSaleAssetDetail(
        @Path("assetSellId") assetSellId: Long,
    ): BaseResponse<SellAssetDetail>

    // 좋아요한 에셋 목록
    @GET("api/asset/like")
    suspend fun getLikeAssets(): BaseResponse<AllSellAssets>

    // 에셋 구매
    @POST("api/asset/purchase")
    suspend fun purchaseAsset(
        @Body purchaseAssetRequest: PurchaseAssetRequest,
    ): BaseResponse<Unit>

    // 유저 제작한 에셋
    @GET("api/asset/creator")
    suspend fun getCreatorAsset(
        @Query("userId") userId: Long,
    ): BaseResponse<GetCreatorAssetsResponse>

    // 판매 가능한 에셋 조회
    @GET("api/asset/unsell")
    suspend fun getUnSaleAssets(): BaseResponse<CreateAsset>
}
