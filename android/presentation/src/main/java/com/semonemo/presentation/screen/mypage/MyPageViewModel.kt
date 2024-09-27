package com.semonemo.presentation.screen.mypage

import android.util.Log
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
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<MyPageUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            loadUserInfo()
        }

        private fun loadUserInfo() {
            viewModelScope.launch {
                Log.d("jaehan", "정보 조회 요청")
                userRepository.loadUserInfo().collectLatest { response ->
                    Log.d("jaehan", "성공 : $response")
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(MyPageUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            _uiState.value =
                                MyPageUiState.Success(
                                    profileImageUrl = response.data.profileImage,
                                    nickname = response.data.nickname,
                                )
                        }
                    }
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
