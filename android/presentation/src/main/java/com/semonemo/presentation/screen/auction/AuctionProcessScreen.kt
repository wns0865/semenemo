package com.semonemo.presentation.screen.auction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.component.BidPriceInputTextField
import com.semonemo.presentation.component.BidPriceUnitButton
import com.semonemo.presentation.screen.auction.subScreen.BindInfo

@Preview
@Composable
fun AuctionProcessScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        BindInfo(topBidders =
            listOf(
                Pair(1, 11111),
                Pair(2, 12000),
                Pair(3, 88888),
                Pair(4, 14000),
                Pair(5, 15000),
            )
        )
        Spacer(modifier = modifier.height(4.dp))
        BidPriceInputTextField(onValueChange = {})
        Spacer(modifier = modifier.height(4.dp))
        Row (
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ){
            BidPriceUnitButton(
                modifier = modifier.weight(1f),
                bidPriceUnit = 10
            )
            BidPriceUnitButton(
                modifier = modifier.weight(1f),
                bidPriceUnit = 5
            )
            BidPriceUnitButton(
                modifier = modifier.weight(1f),
                bidPriceUnit = 1
            )
        }
    }
}
