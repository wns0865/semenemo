package com.semonemo.domain.model

data class AuctionJoinResponse(
    val anonym: Int = 0,
    val participants: Int = 0,
    val bidLogs: List<AuctionBidLog> = listOf(),
)
