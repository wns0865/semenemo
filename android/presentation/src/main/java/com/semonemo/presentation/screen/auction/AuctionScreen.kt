package com.semonemo.presentation.screen.auction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.TextLabel
import com.semonemo.presentation.screen.auction.subScreen.LongAuctionReadScreen
import com.semonemo.presentation.screen.auction.subScreen.ShortAuctionReadScreen

@Composable
fun AuctionScreen() {
    Column {
        Spacer(modifier = Modifier.height(30.dp))
        TextLabel(text = stringResource(R.string.in_progress_short_action))
        ShortAuctionReadScreen()
        Spacer(modifier = Modifier.height(30.dp))
        TextLabel(text = stringResource(R.string.in_progress_long_action))
        LongAuctionReadScreen()
    }
}
