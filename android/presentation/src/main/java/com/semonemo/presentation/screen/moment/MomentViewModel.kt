package com.semonemo.presentation.screen.moment

import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.UserRepository
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MomentViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(MomentUiState())
        val uiState = _uiState.asStateFlow()

        init {
            loadUserInfo()
        }

        private fun loadUserInfo() {
            viewModelScope.launch {
                userRepository.loadUserInfo().collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    userId = response.data.userId,
                                    nickname = response.data.nickname,
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
