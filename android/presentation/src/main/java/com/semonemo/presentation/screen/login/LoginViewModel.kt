package com.semonemo.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Init)
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

    /*
     * TODO
     * 유저 정보 로딩(지갑주소,비밀번호)
     */
        private fun loadUserInfo() {
        }

        fun existUser(address: String) {
            viewModelScope.launch {
                authRepository.exists(address).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            if (response.data) {
                                _uiState.value =
                                    LoginUiState.Loading(
                                        isWalletLoading = true,
                                        walletAddress = address,
                                    )
                            } else {
                                _uiEvent.emit(LoginUiEvent.RequiredRegister(address))
                            }
                        }

                        is ApiResponse.Error -> {
                            _uiState.value =
                                LoginUiState.Error(
                                    errorCode = response.errorCode,
                                    errorMessage = response.errorMessage,
                                )
                        }
                    }
                }
            }
        }
    }
