package com.semonemo.presentation.screen.mypage

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface MyPageUiState {
    @Immutable
    data object Loading : MyPageUiState

    @Immutable
    data class Success(
        val userId: Long = 0,
        val nickname: String = "",
        val profileImageUrl: String = "",
        val following: Int = 0,
        val follower: Int = 0,
        val volume: Int = 0,
        val amount: Int = 0,
    ) : MyPageUiState
}
