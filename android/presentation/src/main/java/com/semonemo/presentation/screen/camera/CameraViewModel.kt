package com.semonemo.presentation.screen.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel
    @Inject
    constructor() : ViewModel() {
        private val _bitmaps = MutableStateFlow<List<Bitmap>>(listOf())
        val bitmaps = _bitmaps.asStateFlow()

        fun takePhoto(bitmap: Bitmap) {
            _bitmaps.value += bitmap
        }
    }
