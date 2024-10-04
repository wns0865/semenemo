package com.semonemo.data.network.response

data class GetBalanceResponse(
    val userId: Long,
    val coinBalance: Long,
    val payableBalance: Long,
)
