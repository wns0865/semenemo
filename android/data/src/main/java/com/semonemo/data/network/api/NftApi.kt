package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.domain.model.Nft
import com.semonemo.domain.request.PublishNftRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface NftApi {
    @POST("api/nft")
    suspend fun publishNft(
        @Body request: PublishNftRequest,
    ): BaseResponse<Nft>
}
