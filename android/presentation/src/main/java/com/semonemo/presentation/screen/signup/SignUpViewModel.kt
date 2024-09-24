package com.semonemo.presentation.screen.signup

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.semonemo.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val walletAddress = savedStateHandle.get<String>("walletAddress") ?: ""
        private val _uiState = MutableStateFlow(SignUpUiState())
        val uiState = _uiState.asStateFlow()

        init {
            Log.d("jaehan", "walletAddress $walletAddress")
        }

        fun updateNickname(nickname: String) {
            _uiState.update {
                it.copy(
                    nickname = nickname,
                )
            }
        }

        fun updatePassword(password: String) {
            _uiState.update {
                it.copy(
                    password = password,
                )
            }
        }

        fun updateProfileImageUrl(profileImageUrl: String) {
            _uiState.update {
                it.copy(
                    profileImageUrl = profileImageUrl,
                )
            }
        }
    }
