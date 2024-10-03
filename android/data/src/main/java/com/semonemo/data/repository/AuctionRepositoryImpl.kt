package com.semonemo.data.repository

import com.semonemo.data.network.api.AuctionApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Auction
import com.semonemo.domain.model.AuctionBidLog
import com.semonemo.domain.repository.AuctionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuctionRepositoryImpl
    @Inject
    constructor(
        private val api: AuctionApi,
    ) : AuctionRepository {
        override suspend fun registerAuction(auction: Auction): Flow<ApiResponse<Auction>> =
            flow {
                emit(
                    emitApiResponse(
                        apiResponse = { api.registerAuction(auction) },
                        default = Auction(),
                    ),
                )
            }

        override suspend fun startAuction(auctionId: Long): Flow<ApiResponse<Nothing?>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.startAuction(auctionId) },
                        default = null,
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
            }

        override suspend fun joinAuction(auctionId: Long): Flow<ApiResponse<List<AuctionBidLog>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.joinAuction(auctionId) },
                        default = listOf(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
            }
    }
