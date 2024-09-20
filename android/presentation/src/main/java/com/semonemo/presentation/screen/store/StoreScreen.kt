package com.semonemo.presentation.screen.store

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.semonemo.presentation.R
import com.semonemo.presentation.component.SectionHeader
import com.semonemo.presentation.screen.store.subScreen.StoreSubScreen

@Preview(showBackground = true)
@Composable
fun StoreScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        SectionHeader(text = stringResource(id = R.string.section_header_sell_frame))
        StoreSubScreen(
            modifier =
                modifier
                    .weight(1f)
                    .fillMaxWidth(),
            isFrame = true,
        )
        SectionHeader(text = stringResource(id = R.string.section_header_sell_asset))
        StoreSubScreen(
            modifier =
                modifier
                    .weight(1f)
                    .fillMaxWidth(),
            isFrame = false,
        )
    }
}
