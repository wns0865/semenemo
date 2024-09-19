package com.semonemo.data.network.api

import com.semonemo.domain.model.Transaction
import com.semonemo.domain.request.TransferRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface NFTApi {
    @POST("bcapi/coin/transfer")
    suspend fun transfer(
        @Body request: TransferRequest,
    ): Transaction
}
