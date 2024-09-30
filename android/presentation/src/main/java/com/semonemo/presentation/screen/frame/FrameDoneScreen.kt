package com.semonemo.presentation.screen.frame

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FrameDoneRoute(
    modifier: Modifier = Modifier,
    viewModel: FrameViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    FrameDoneScreen(modifier = modifier, uiState.value.bitmap)
}

@Composable
fun FrameDoneScreen(
    modifier: Modifier = Modifier,
    bitmap: Bitmap? = null,
) {
    Column(
        modifier =
            modifier
                .navigationBarsPadding()
                .statusBarsPadding(),
    ) {
        bitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null)
        }
    }
}
