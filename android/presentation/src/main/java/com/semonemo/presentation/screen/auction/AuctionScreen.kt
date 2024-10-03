package com.semonemo.presentation.screen.auction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomAuctionFAB
import com.semonemo.presentation.component.SectionHeader
import com.semonemo.presentation.screen.auction.subScreen.LongAuctionReadScreen
import com.semonemo.presentation.screen.auction.subScreen.ShortAuctionReadScreen

@Preview(showBackground = true)
@Composable
fun AuctionScreen(
    modifier: Modifier = Modifier,
    navigateToAuctionProcess: (String) -> Unit = {},
) {
    val verticalScrollState = rememberScrollState()
    Surface(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
//        Column(
//            modifier =
//                Modifier
//                    .fillMaxSize()
//                    .statusBarsPadding()
//                    .navigationBarsPadding()
//                    .verticalScroll(state = verticalScrollState),
//        ) {
//            Spacer(modifier = Modifier.height(30.dp))
//            SectionHeader(text = stringResource(R.string.in_progress_short_action))
//            ShortAuctionReadScreen(navigateToAuctionProcess = navigateToAuctionProcess)
//            Spacer(modifier = Modifier.height(30.dp))
//            SectionHeader(text = stringResource(R.string.in_progress_long_action))
//            LongAuctionReadScreen()
//        }
        CustomAuctionFAB(modifier = modifier)
    }
}
