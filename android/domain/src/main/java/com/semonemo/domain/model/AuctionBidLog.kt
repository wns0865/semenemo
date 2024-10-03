package com.semonemo.domain.model

import java.time.LocalTime

data class AuctionBidLog(
    val userId: Long = 0L,
    val bidAmount: Int = 0,
    val bidTime: LocalTime = LocalTime.now(),
)
