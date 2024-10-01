package com.semonemo.presentation.screen.search

import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.Asset
import com.semonemo.domain.model.Frame
import com.semonemo.domain.model.User
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor() : BaseViewModel() {
        private val _searchState = MutableStateFlow<SearchState>(SearchState.Loading)
        val searchState = _searchState.asStateFlow()

        init {
            loadHotSearch()
        }

        // 사람들이 많이 찾는 거 순위 불러오기
        private fun loadHotSearch() {
            viewModelScope.launch {
                _searchState.value =
                    SearchState.Init(
                        hotList =
                            listOf(
                                "아이유",
                                "도은호",
                                "플레이브",
                            ),
                    )
            }
        }

        // 유저 검색
        fun userSearch(nickname: String) {
            viewModelScope.launch {
                _searchState.value =
                    SearchState.Success(
                        userList =
                            listOf(
                                User(
                                    userId = 1,
                                    address = "",
                                    nickname = "나갱갱",
                                    profileImage = "",
                                ),
                                User(
                                    userId = 2,
                                    address = "",
                                    nickname = "또으노",
                                    profileImage = "",
                                ),
                                User(
                                    userId = 3,
                                    address = "",
                                    nickname = "짜이한",
                                    profileImage = "",
                                ),
                            ),
                    )
            }
        }

        // 프레임 (NFT) 검색
        fun frameSearch(keyword: String) {
            viewModelScope.launch {
                _searchState.value =
                    SearchState.Success(
                        frameList =
                            listOf(
                                Frame(
                                    frameId = 1,
                                    imgUrl = "",
                                ),
                                Frame(
                                    frameId = 2,
                                    imgUrl = "",
                                ),
                                Frame(
                                    frameId = 3,
                                    imgUrl = "",
                                ),
                                Frame(
                                    frameId = 4,
                                    imgUrl = "",
                                ),
                            ),
                    )
            }
        }

        // 에셋 검색
        fun assetSearch(keyword: String) {
            viewModelScope.launch {
                _searchState.value =
                    SearchState.Success(
                        assetList =
                            listOf(
                                Asset(
                                    assetId = 1,
                                    imageUrl = "",
                                ),
                                Asset(
                                    assetId = 2,
                                    imageUrl = "",
                                ),
                                Asset(
                                    assetId = 1,
                                    imageUrl = "",
                                ),
                                Asset(
                                    assetId = 2,
                                    imageUrl = "",
                                ),
                            ),
                    )
            }
        }
    }
