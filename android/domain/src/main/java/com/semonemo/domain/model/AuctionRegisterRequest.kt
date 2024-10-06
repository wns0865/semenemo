package com.semonemo.domain.model

data class AuctionRegisterRequest(
    val nftId: Long = 0L,
    val startPrice: Int = 0,
)
