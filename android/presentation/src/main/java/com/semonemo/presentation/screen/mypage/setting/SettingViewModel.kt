package com.semonemo.presentation.screen.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.UserRepository
import com.semonemo.domain.request.EditUserRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val authDataSource: AuthDataSource,
    ) : ViewModel() {
        private val _uiEvent = MutableSharedFlow<SettingUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        fun editNickname(nickname: String) {
            viewModelScope.launch {
                userRepository
                    .edit(request = EditUserRequest(nickname))
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(SettingUiEvent.Error(response.errorMessage))
                            }

                            is ApiResponse.Success -> {
                                _uiEvent.emit(SettingUiEvent.EditSuccess("닉네임 변경 성공"))
                            }
                        }
                    }
            }
        }

        fun logout() {
            viewModelScope.launch {
                authDataSource.deleteAuthData()
                _uiEvent.emit(SettingUiEvent.Logout)
            }
        }

        fun deleteUser() {
            viewModelScope.launch {
                userRepository.delete().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> _uiEvent.emit(SettingUiEvent.Error(response.errorMessage))
                        is ApiResponse.Success -> _uiEvent.emit(SettingUiEvent.Logout)
                    }
                }
            }
        }
    }
