package com.semonemo.presentation.screen.search

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.semonemo.domain.model.AssetDetail
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.Profile

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
        val userList: List<Profile> = emptyList(),
        val frameList: List<FrameDetail> = emptyList(),
        val assetList: List<AssetDetail> = emptyList(),
    ) : SearchState

    @Immutable
    data class Error(
        val message: String = "",
    ) : SearchState
}
