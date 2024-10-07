package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Auction
import com.semonemo.domain.model.AuctionJoinResponse
import com.semonemo.domain.model.AuctionRegisterRequest
import com.semonemo.domain.model.AuctionRegisterResponse
import kotlinx.coroutines.flow.Flow

interface AuctionRepository {
    suspend fun registerAuction(auction: AuctionRegisterRequest): Flow<ApiResponse<AuctionRegisterResponse>>

    suspend fun getAuction(auctionId: Long): Flow<ApiResponse<Auction>>

    suspend fun getAllAuction(): Flow<ApiResponse<List<Auction>>>

    suspend fun startAuction(auctionId: Long): Flow<ApiResponse<Nothing?>>

    suspend fun joinAuction(auctionId: Long): Flow<ApiResponse<AuctionJoinResponse>>

    suspend fun leaveAuction(auctionId: Long): Flow<ApiResponse<Nothing?>>
}
