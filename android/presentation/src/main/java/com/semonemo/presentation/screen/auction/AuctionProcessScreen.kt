package com.semonemo.presentation.screen.auction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.component.BidPriceInputTextField
import com.semonemo.presentation.component.BidPriceUnitButton
import com.semonemo.presentation.component.CustomAuctionProgressBar
import com.semonemo.presentation.component.CustomNoRippleButton
import com.semonemo.presentation.screen.auction.subScreen.BindInfo
import com.semonemo.presentation.theme.Typography
import com.skydoves.landscapist.glide.GlideImage

@Preview
@Composable
fun AuctionProcessScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Spacer(modifier = modifier.height(30.dp))
        GlideImage(
            imageModel = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQL4Xl-3MO5y2aaLELSf9nlsbS4DPkA2hokFtTkU2S4Zrj5_3JzNBWudLaf5Uz4NeXmzF0&usqp=CAU",
            contentScale = ContentScale.Crop,
            modifier =
                modifier
                    .height(360.dp)
                    .width(240.dp)
                    .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = modifier.height(20.dp))
        CustomAuctionProgressBar()
        Spacer(modifier = modifier.height(20.dp))
        BindInfo(
            topBidders =
                listOf(
                    Pair(1, 15000),
                    Pair(2, 12000),
                    Pair(3, 8000),
                    Pair(4, 5000),
                    Pair(5, 4000),
                    Pair(6, 2000),
                    Pair(7, 1000),
                    Pair(8, 500),
                    Pair(9, 200),
                ),
        )

        Spacer(modifier = modifier.height(4.dp))
        BidPriceInputTextField(onValueChange = {})
        Spacer(modifier = modifier.height(4.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "10% = 100AOR",
                style =
                    Typography.labelMedium.copy(
                        fontFeatureSettings = "tnum",
                        fontSize = 20.sp,
                    ),
            )
            Text(
                text = "= 52,000원",
                style =
                    Typography.labelMedium.copy(
                        fontFeatureSettings = "tnum",
                        fontSize = 20.sp,
                    ),
            )
        }
        Spacer(modifier = modifier.height(4.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BidPriceUnitButton(
                modifier = modifier.weight(1f),
                bidPriceUnit = 10,
            )
            BidPriceUnitButton(
                modifier = modifier.weight(1f),
                bidPriceUnit = 5,
            )
            BidPriceUnitButton(
                modifier = modifier.weight(1f),
                bidPriceUnit = 1,
            )
        }
        Spacer(modifier = modifier.height(20.dp))
        CustomNoRippleButton(
            text = "입찰하기",
            onClick = {},
        )
    }
}
