package com.semonemo.presentation.screen.mypage

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.UserRepository
import com.semonemo.domain.request.EditUserRequest
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<MyPageUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()
        private val userId = savedStateHandle.get<Long>("userId")

        init {
            loadUserInfo()
        }

        private fun loadUserInfo() {
            viewModelScope.launch {
                combine(
                    userRepository.loadUserInfo(),
                    userRepository.loadFollowing(userId),
                ) { userInfo, followingInfo ->
                    Pair(userInfo, followingInfo)
                }.map { (userInfo, followingInfo) ->
                    val currentState = MyPageUiState.Success()
                    val newUiState =
                        when (userInfo) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(MyPageUiEvent.Error(userInfo.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    profileImageUrl = userInfo.data.profileImage,
                                    nickname = userInfo.data.nickname,
                                )
                            }
                        }
                    when (followingInfo) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(MyPageUiEvent.Error(followingInfo.errorMessage))
                            newUiState
                        }

                        is ApiResponse.Success -> {
                            newUiState.copy(
                                following = followingInfo.data,
                            )
                        }
                    }
                    newUiState
                }.collectLatest { updatedUiState ->
                    _uiState.value = updatedUiState
                }
            }
        }

        fun updateProfileImage(
            image: File,
            imageUri: String,
        ) {
            val state = uiState.value
            if (state !is MyPageUiState.Success) {
                return
            }
            Log.d("jaehan", "프로필 이미지 수정 요청 ${image.path} $imageUri")
            viewModelScope.launch {
                userRepository
                    .edit(profileImage = image, request = EditUserRequest(state.nickname))
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(MyPageUiEvent.Error(response.errorMessage))
                            }

                            is ApiResponse.Success -> {
                                _uiState.value =
                                    state.copy(
                                        profileImageUrl = imageUri,
                                    )
                            }
                        }
                    }
            }
        }
    }
