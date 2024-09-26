package com.semonemo.presentation.screen.store

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.semonemo.presentation.screen.store.subScreen.StoreSubFullViewScreen

@Composable
fun StoreFullViewScreen(
    modifier: Modifier = Modifier,
    isFrame: Boolean,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        StoreSubFullViewScreen(
            modifier =
                modifier
                    .weight(1f)
                    .fillMaxWidth(),
            isFrame = isFrame,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StoreFullViewScreenPreview() {
    StoreFullViewScreen(isFrame = true)
}
