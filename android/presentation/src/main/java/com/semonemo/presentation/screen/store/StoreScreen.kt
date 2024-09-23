package com.semonemo.presentation.screen.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomStoreFAB
import com.semonemo.presentation.component.SectionFullViewButton
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
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SectionHeader(text = stringResource(id = R.string.section_header_sell_frame))
            SectionFullViewButton(onClick = {})
        }

        StoreSubScreen(
            modifier =
                modifier
                    .weight(1f)
                    .fillMaxWidth(),
            isFrame = true,
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SectionHeader(text = stringResource(id = R.string.section_header_sell_asset))
            SectionFullViewButton(onClick = {})
        }

        StoreSubScreen(
            modifier =
                modifier
                    .weight(1f)
                    .fillMaxWidth(),
            isFrame = false,
        )
    }
    CustomStoreFAB()
}
