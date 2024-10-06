package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.data.network.response.GetBalanceResponse
import com.semonemo.data.network.response.GetCoinHistoryResponse
import retrofit2.http.GET

interface CoinApi {
    @GET("api/coin")
    suspend fun getBalance(): BaseResponse<GetBalanceResponse>

    @GET("api/coin/history")
    suspend fun getCoinHistory(): BaseResponse<GetCoinHistoryResponse>
}
