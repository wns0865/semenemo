package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.domain.model.Auction
import com.semonemo.domain.model.AuctionBidLog
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuctionApi {
    @POST("api/auction")
    suspend fun registerAuction(
        @Body auction: Auction,
    ): BaseResponse<Auction>

    @GET("api/auction/{auctionId}")
    suspend fun startAuction(
        @Path("auctionId") auctionId: Long,
    ): BaseResponse<Nothing?>

    @GET("api/auction/{auctionId}/join")
    suspend fun joinAuction(
        @Path("auctionId") auctionId: Long,
    ): BaseResponse<List<AuctionBidLog>>
}
