package com.semonemo.presentation.screen.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.domain.model.Coin
import com.semonemo.domain.model.CoinHistory
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.screen.nft.NftViewModel
import com.semonemo.presentation.theme.Blue1
import com.semonemo.presentation.theme.Blue2
import com.semonemo.presentation.theme.Blue3
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Main02
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.util.noRippleClickable
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime
import java.util.Locale

data class ProductInfo(
    val message: String,
    val imageRes: Int,
    val color: Color,
)

@Composable
fun WalletRoute(
    modifier: Modifier,
    navigateToCoinDetail: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel(),
    nftViewModel: NftViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    WalletContent(
        modifier = modifier,
        navigateToCoinDetail = navigateToCoinDetail,
        uiState = uiState.value,
        uiEvent = viewModel.uiEvent,
        onShowSnackBar = onShowSnackBar,
    )
}

@Composable
fun WalletContent(
    modifier: Modifier,
    navigateToCoinDetail: () -> Unit,
    uiState: WalletUiState,
    uiEvent: SharedFlow<WalletUiEvent>,
    onShowSnackBar: (String) -> Unit,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is WalletUiEvent.Error -> onShowSnackBar(event.errorMessage)
            }
        }
    }
    WalletScreen(
        modifier = modifier,
        userName = uiState.nickname,
        coinPrice = uiState.coinPrice.toDouble(),
        coinHistory = uiState.coinHistory,
        userCoin = uiState.userCoin,
        userId = uiState.userId,
        navigateToCoinDetail = navigateToCoinDetail,
    )

    if (uiState.isLoading) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {},
        )
        LoadingDialog(
            lottieRes = R.raw.normal_load,
            loadingMessage = stringResource(R.string.hisotry_loading_message),
            subMessage = stringResource(R.string.loading_sub_message),
        )
    }
}

@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    navigateToCoinDetail: () -> Unit = {},
    userName: String = "짜이한",
    userCoin: Coin = Coin(),
    coinPrice: Double = 100000.0,
    changePercent: Double = 8.7,
    changePrice: Double = 8300.0,
    coinHistory: List<CoinHistory> = listOf(),
    userId: Long = 0L,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
            modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(state = scrollState)
                .statusBarsPadding()
                .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        WalletCardBox(
            modifier = modifier,
            userName = userName,
            userCoin = userCoin,
            navigateToCoinDetail = navigateToCoinDetail,
        )
        Spacer(modifier = Modifier.height(10.dp))
        WalletCoinBox(
            modifier = modifier,
            coinPrice = coinPrice,
            changePercent = changePercent,
            changePrice = changePrice,
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = stringResource(R.string.recent_transaction_history))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(coinHistory.size) { index ->
                val item = coinHistory[index]
                val isSell =
                    item.toUser?.let {
                        it.userId == userId
                    } ?: false
                val product = item.tradeType.split(" ").first()
                val originalDateTime = LocalDateTime.parse(item.createdAt)
                val dateOnly = originalDateTime.toLocalDate()
                transactionHistoryBox(
                    modifier = modifier,
                    date = dateOnly.toString(),
                    isSell = isSell,
                    product = product,
                    price = item.amount.toDouble(),
                )
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun WalletCardBox(
    modifier: Modifier = Modifier,
    userName: String,
    userCoin: Coin,
    navigateToCoinDetail: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
    ) {
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .matchParentSize(),
            horizontalArrangement = Arrangement.End,
        ) {
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight(),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(2.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxSize()
                            .padding(start = 30.dp)
                            .background(brush = Main02)
                            .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier =
                            Modifier
                                .size(20.dp)
                                .noRippleClickable { {} },
                        painter = painterResource(id = R.drawable.ic_coin_exchange),
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(id = R.string.exchange),
                        style = Typography.labelMedium.copy(color = White),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(
                        modifier =
                            Modifier
                                .width(50.dp)
                                .height(0.5.dp),
                        color = White,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier =
                            Modifier
                                .size(20.dp)
                                .noRippleClickable { {} },
                        painter = painterResource(id = R.drawable.ic_coin_plus),
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(id = R.string.recharge),
                        style = Typography.labelMedium.copy(color = White),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Card(
            modifier =
                modifier
                    .fillMaxWidth(0.8f)
                    .height(150.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(2.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(color = White)
                        .padding(10.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .noRippleClickable {
                                navigateToCoinDetail()
                            },
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    BoldTextWithKeywords(
                        modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                        fullText = "$userName 님의 지갑",
                        keywords = listOf(userName),
                        brushFlag = listOf(true),
                        boldStyle = Typography.titleSmall.copy(fontSize = 20.sp),
                        normalStyle = Typography.labelLarge.copy(fontSize = 20.sp),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_color_sene_coin),
                            contentDescription = "",
                        )
                        Text(
                            text =
                                String.format(
                                    Locale.KOREAN,
                                    "%,.0f",
                                    userCoin.payableBalance.toDouble(),
                                ),
                            style = Typography.bodyLarge,
                            fontSize = 20.sp,
                        )

                        Text(
                            text = "${stringResource(R.string.coin_unit_name)}(Pay)",
                            style = Typography.labelMedium,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_color_sene_coin),
                            contentDescription = "",
                        )
                        Text(
                            text =
                                String.format(
                                    Locale.KOREAN,
                                    "%,.0f",
                                    userCoin.coinBalance.toDouble(),
                                ),
                            style = Typography.bodyLarge,
                            fontSize = 20.sp,
                        )

                        Text(
                            text = stringResource(R.string.coin_unit_name),
                            style = Typography.labelMedium,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Image(
                        modifier =
                            Modifier
                                .padding(10.dp)
                                .size(105.dp),
                        painter = painterResource(id = R.drawable.img_money),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun WalletCoinBox(
    modifier: Modifier = Modifier,
    coinPrice: Double,
    changePercent: Double,
    changePrice: Double,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 30.dp, vertical = 20.dp),
            horizontalArrangement =
                Arrangement
                    .SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier =
                    Modifier
                        .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.ic_color_sene_coin),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.coin_name),
                    style = Typography.bodyLarge.copy(fontSize = 15.sp),
                )
            }
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.img_graph),
                contentDescription = null,
            )
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.current_price),
                        style = Typography.labelLarge,
                    )
                    Text(text = String.format(Locale.KOREAN, "%,.0f", coinPrice))
                }
                Spacer(modifier = Modifier.width(24.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.fluctuation_rate),
                        style = Typography.labelLarge,
                    )
                    Text(
                        text =
                            if (changePrice > 0) {
                                "+$changePercent%"
                            } else {
                                "-$changePercent%"
                            },
                        color = if (changePercent > 0) Color.Red else Color.Blue,
                        style = Typography.bodyMedium,
                    )
                    Text(
                        text = String.format(Locale.KOREAN, "%,.0f", changePrice),
                        color = if (changePercent > 0) Color.Red else Color.Blue,
                        style = Typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
fun transactionHistoryBox(
    modifier: Modifier = Modifier,
    isSell: Boolean = true,
    date: String = "2024.09.09",
    price: Double = +100000.0,
    product: String = "프레임",
) {
    val productInfo =
        if (product.contains("코인")) {
            if (isSell) {
                ProductInfo(
                    stringResource(R.string.exchange),
                    R.drawable.ic_outline_coin,
                    color = Blue3,
                )
            } else {
                ProductInfo(
                    stringResource(R.string.recharge),
                    R.drawable.ic_outline_coin,
                    color = Blue3,
                )
            }
        } else if (product.contains("NFT")) {
            if (isSell) {
                ProductInfo(
                    stringResource(R.string.sell),
                    R.drawable.ic_fab_frame,
                    color = Blue2,
                )
            } else {
                ProductInfo(
                    stringResource(R.string.buy),
                    R.drawable.ic_fab_frame,
                    color = Blue2,
                )
            }
        } else {
            if (isSell) {
                ProductInfo(
                    stringResource(R.string.sell),
                    R.drawable.ic_fab_asset,
                    color = Blue1,
                )
            } else {
                ProductInfo(
                    stringResource(R.string.buy),
                    R.drawable.ic_fab_asset,
                    color = Blue1,
                )
            }
        }

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 10.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(productInfo.color),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = productInfo.imageRes),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(modifier = Modifier.fillMaxWidth(0.02f))
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(text = date, style = Typography.labelSmall.copy(color = Gray02))
                Text(text = "$product ${productInfo.message}", style = Typography.bodyLarge)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text =
                    String.format(Locale.KOREAN, "%,.0f", price),
                color = if (isSell) Color.Red else Color.Blue,
                style = Typography.bodyLarge,
            )

            Text(text = stringResource(R.string.coin_unit_name), style = Typography.labelLarge)
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WalletScreenPreview() {
    SemonemoTheme {
        Scaffold { innerPadding ->
            WalletScreen(
                modifier = Modifier.padding(innerPadding),
                coinHistory = listOf(CoinHistory()),
            )
        }
    }
}
