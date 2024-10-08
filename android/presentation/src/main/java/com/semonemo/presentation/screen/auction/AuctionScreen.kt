package com.semonemo.presentation.screen.auction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomAuctionFAB
import com.semonemo.presentation.component.SectionHeader
import com.semonemo.presentation.screen.auction.subScreen.LongAuctionReadScreen
import com.semonemo.presentation.screen.auction.subScreen.ShortAuctionReadScreen
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable

@Composable
fun AuctionScreen(
    modifier: Modifier = Modifier,
    viewModel: AuctionViewModel = hiltViewModel(),
    navigateToAuctionDetail: (Long) -> Unit = {},
    navigateToAuctionRegister: () -> Unit = {},
) {
    Surface(
        modifier =
            modifier
                .fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                SectionHeader(text = stringResource(R.string.in_progress_short_action))
                Box(
                    modifier =
                        Modifier
                            .padding(5.dp)
                            .noRippleClickable(
                                onClick = { viewModel.loadShortAuction() },
                            ),
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.ic_reload),
                            contentDescription = "reload",
                            tint = Color.Unspecified,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            modifier = Modifier,
                            text = stringResource(R.string.reload_label),
                            color = GunMetal,
                            style = Typography.labelMedium,
                        )
                    }
                }
            }

            ShortAuctionReadScreen(
                viewModel = viewModel,
                navigateToAuctionDetail = navigateToAuctionDetail,
            )
            Spacer(modifier = Modifier.height(30.dp))
            SectionHeader(text = stringResource(R.string.in_progress_long_action))
            LongAuctionReadScreen()
        }
        CustomAuctionFAB(
            modifier = modifier,
            navigateToAuctionRegister = navigateToAuctionRegister,
        )
    }
}
