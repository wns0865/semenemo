package com.semonemo.presentation.screen.wallet.subscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.domain.model.Coin
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.theme.Main02
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.util.noRippleClickable
import java.util.Locale

@Composable
fun WalletCardBox(
    modifier: Modifier = Modifier,
    userName: String,
    userCoin: Coin,
    onShowSnackBar: (String) -> Unit,
    sendExchangePayableTransaction: (String) -> Unit,
    sendExchangeCoinTransaction: (String) -> Unit,
) {
    val (showExchangeCoinPayable, setShowExchangeCoinPayable) =
        remember {
            mutableStateOf(false)
        }
    val (showExchangePayableCoin, setShowExchangePayableCoin) =
        remember {
            mutableStateOf(false)
        }

    if (showExchangeCoinPayable) {
        WalletDialog(
            title = "보유 코인을 환전하시겠습니까?",
            onDismissMessage = "취소",
            onConfirmMessage = "변경",
            onConfirm = {
                if (userCoin.coinBalance < it) {
                    onShowSnackBar("잔여코인이 부족합니다")
                } else {
                    sendExchangePayableTransaction(it.toString())
                }
                setShowExchangeCoinPayable(false)
            },
            onDismiss = {
                setShowExchangeCoinPayable(false)
            },
        )
    }

    if (showExchangePayableCoin) {
        WalletDialog(
            title = "지불 가능한 코인을 환전하시겠습니까?",
            onDismissMessage = "취소",
            onConfirmMessage = "변경",
            onConfirm = {
                if (userCoin.payableBalance < it) {
                    onShowSnackBar("잔여코인이 부족합니다")
                } else {
                    sendExchangeCoinTransaction(it.toString())
                }
                setShowExchangePayableCoin(false)
            },
            onDismiss = {
                setShowExchangePayableCoin(false)
            },
        )
    }

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
                    Column(
                        modifier =
                            Modifier
                                .wrapContentSize()
                                .noRippleClickable { setShowExchangeCoinPayable(true) },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            modifier =
                                Modifier
                                    .size(20.dp),
                            painter = painterResource(id = R.drawable.ic_coin_exchange),
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = stringResource(id = R.string.exchange),
                            style = Typography.labelMedium.copy(color = White),
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(
                        modifier =
                            Modifier
                                .width(50.dp)
                                .height(0.5.dp),
                        color = White,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.wrapContentSize().noRippleClickable { },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            modifier =
                                Modifier
                                    .size(20.dp)
                                    .noRippleClickable { setShowExchangePayableCoin(true) },
                            painter = painterResource(id = R.drawable.ic_coin_plus),
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = stringResource(id = R.string.recharge),
                            style = Typography.labelMedium.copy(color = White),
                        )
                    }

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
                            .wrapContentHeight(),
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
