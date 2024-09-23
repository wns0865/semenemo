package com.semonemo.presentation.screen.register

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.semonemo.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val walletAddress = savedStateHandle.get<String>("walletAddress") ?: ""

        init {
            Log.d("jaehan", "walletAddress $walletAddress")
        }
    }
