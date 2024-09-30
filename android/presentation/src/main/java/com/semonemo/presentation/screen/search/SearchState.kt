package com.semonemo.presentation.screen.search

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.semonemo.domain.model.Asset
import com.semonemo.domain.model.Frame
import com.semonemo.domain.model.User

@Stable
sealed interface SearchState {
    @Immutable
    data object Loading : SearchState

    @Immutable
    data class Init(
        val hotList: List<String> = emptyList(),
    ) : SearchState

    @Immutable
    data class Success(
        val userList: List<User> = emptyList(),
        val frameList: List<Frame> = emptyList(),
        val assetList: List<Asset> = emptyList(),
    ) : SearchState

    @Immutable
    data class Error(
        val message: String = "",
    ) : SearchState
}
