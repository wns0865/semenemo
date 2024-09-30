package com.semonemo.presentation.screen.frame

import android.graphics.Bitmap

data class FrameUiState(
    val bitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val title: String = "",
    val content: String = "",
)
