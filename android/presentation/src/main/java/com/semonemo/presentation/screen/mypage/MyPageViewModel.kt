package com.semonemo.presentation.screen.mypage

import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.UserRepository
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
                userRepository.loadUserInfo().collectLatest { response ->
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
    }
