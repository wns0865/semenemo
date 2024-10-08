package com.semonemo.data.repository

import com.semonemo.data.network.api.AuctionApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Auction
import com.semonemo.domain.model.AuctionJoinResponse
import com.semonemo.domain.model.AuctionRegisterRequest
import com.semonemo.domain.model.AuctionRegisterResponse
import com.semonemo.domain.repository.AuctionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuctionRepositoryImpl
    @Inject
    constructor(
        private val api: AuctionApi,
    ) : AuctionRepository {
        override suspend fun registerAuction(auction: AuctionRegisterRequest): Flow<ApiResponse<AuctionRegisterResponse>> =
            flow {
                emit(
                    emitApiResponse(
                        apiResponse = { api.registerAuction(auction) },
                        default = AuctionRegisterResponse(),
                    ),
                )
            }

        override suspend fun getAuction(auctionId: Long): Flow<ApiResponse<Auction>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getAuction(auctionId) },
                        default = Auction(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
            }

        override suspend fun getAllAuction(): Flow<ApiResponse<List<Auction>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getAllAuction() },
                        default = listOf(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
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

        override suspend fun joinAuction(auctionId: Long): Flow<ApiResponse<AuctionJoinResponse>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.joinAuction(auctionId) },
                        default = AuctionJoinResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
            }

        override suspend fun leaveAuction(auctionId: Long): Flow<ApiResponse<Nothing?>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.leaveAuction(auctionId) },
                        default = null,
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
            }
    }
