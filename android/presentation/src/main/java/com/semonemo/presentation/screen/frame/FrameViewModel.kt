package com.semonemo.presentation.screen.frame

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FrameViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(FrameUiState())
        val uiState = _uiState.asStateFlow()
        var frame = mutableStateOf<Bitmap?>(null)
            private set

        fun updateFrame(bitmap: Bitmap) {
            _uiState.update { it.copy(bitmap = bitmap) }
        }
    }
