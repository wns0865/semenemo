package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.theme.Typography

@Composable
fun BindInfo(
    modifier: Modifier = Modifier,
    topBidders: List<Pair<Int, Int>>
) {
    Column(
        modifier = modifier,
    ) {
        topBidders.forEach { (bidder, bidPrice) ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = String.format("%03d번", bidder),
                    fontSize = 20.sp,
                    style = Typography.bodyMedium.copy(
                        fontFeatureSettings = "tnum"
                    ),
                )
                Text(
                    text = String.format("%,d AHO", bidPrice),
                    fontSize = 20.sp,
                    style = Typography.bodyMedium.copy(
                        fontFeatureSettings = "tnum"
                    ),
                )
            }
        }
        Spacer(modifier = modifier.height(8.dp))

    }
}
