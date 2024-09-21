package com.semonemo.presentation.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : ViewModel() {
        fun existUser(address: String) {
            viewModelScope.launch {
                authRepository.exists(address).collectLatest { response ->
                    // TODO 추후 Ui 반영 처리
                    Log.d("jaehan", "response : $response")
                }
            }
        }
    }
