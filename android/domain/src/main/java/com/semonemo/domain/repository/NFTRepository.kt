package com.semonemo.domain.repository

import com.semonemo.domain.model.Transaction
import com.semonemo.domain.request.TransferRequest
import kotlinx.coroutines.flow.Flow

interface NFTRepository {
    suspend fun transfer(request: TransferRequest): Flow<Transaction?>
}
