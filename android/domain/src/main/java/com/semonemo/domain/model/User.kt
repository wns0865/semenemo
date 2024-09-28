package com.semonemo.domain.model

data class User(
    val userId: Long = 0L,
    val address: String = "",
    val nickname: String = "",
    val profileImage: String = "",
)
