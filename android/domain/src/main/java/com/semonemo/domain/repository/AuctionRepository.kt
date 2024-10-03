package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Auction
import com.semonemo.domain.model.AuctionBidLog
import kotlinx.coroutines.flow.Flow

interface AuctionRepository {
    suspend fun registerAuction(auction: Auction): Flow<ApiResponse<Auction>>

    suspend fun startAuction(auctionId: Long): Flow<ApiResponse<Nothing?>>

    suspend fun joinAuction(auctionId: Long): Flow<ApiResponse<List<AuctionBidLog>>>
}
