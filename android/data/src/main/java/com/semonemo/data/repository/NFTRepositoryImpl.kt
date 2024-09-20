package com.semonemo.data.repository

import com.semonemo.data.network.api.NFTApi
import com.semonemo.domain.model.Transaction
import com.semonemo.domain.repository.NFTRepository
import com.semonemo.domain.request.TransferRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NFTRepositoryImpl
    @Inject
    constructor(
        private val api: NFTApi,
    ) : NFTRepository {
        override suspend fun transfer(request: TransferRequest): Flow<Transaction?> =
            flow {
                runCatching {
                    api.transfer(request)
                }.onSuccess {
                    emit(it)
                }.onFailure {
                    emit(null)
                }
            }
    }
