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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BidPriceInputTextField
import com.semonemo.presentation.component.BidPriceUnitButton
import com.semonemo.presentation.component.CustomAuctionProgressBar
import com.semonemo.presentation.component.CustomNoRippleButton
import com.semonemo.presentation.screen.auction.subScreen.BindInfo
import com.semonemo.presentation.theme.Typography
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompSession
import kotlin.math.pow

@Preview
@Composable
fun AuctionProcessScreen(
    modifier: Modifier = Modifier,
    auctionId: String = "",
    viewModel: AuctionProcessViewModel = hiltViewModel(),
) {
    val auctionBidLog = viewModel.auctionBidLog.value
    val scope = rememberCoroutineScope()
    val webSocketManager = remember { WebSocketManager() }
    var currentPrice by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(0f) }
    var price by remember { mutableIntStateOf(1000) }

//    val (price, setPrice) = remember { mutableIntStateOf(0) }

    // 최상위 입찰가를 계산하는 함수
    fun getTopBidAmount(): Int = auctionBidLog.maxByOrNull { it.bidAmount }?.bidAmount ?: 0

    // 버튼 클릭 시 증가할 금액을 계산하는 함수
    fun calculateIncrementAmount(percentage: Int): Int {
        val topBid = getTopBidAmount()
        val digits = topBid.toString().length - 1
        val baseIncrement = 10.0.pow(digits.toDouble()).toInt()
        return when (percentage) {
            100 -> baseIncrement
            50 -> baseIncrement / 2
            10 -> baseIncrement / 10
            else -> 0
        }
    }

    /**
     * 입력된 가격의 유효성을 검사하고 포맷팅하는 함수
     * @param input
     * @return
     */
    fun validateAndFormatPrice(input: String): String {
        val numericInput = input.filter { it.isDigit() }
        val numericValue = numericInput.toIntOrNull() ?: 0
        val topBid = getTopBidAmount()
        return when {
            numericValue <= topBid -> (topBid + 1).toString()
            else -> numericValue.toString()
        }
    }

    // stompSession을 remember로 관리하여 전역적으로 유지
    val stompSession =
        remember {
            mutableStateOf<StompSession?>(null)
        }
    LaunchedEffect(auctionId) {
        // 세션이 초기화되지 않았을 때만 연결
        if (stompSession.value == null) {
            stompSession.value = webSocketManager.connectToAuction(auctionId)
        }
        val headersUriStart = "/topic/auction/1"
        val headersUriBid = "/topic/auction/1/bid"
        val headersUriEnd = "/topic/auction/1/end"
        // 경매 업데이트를 구독
        stompSession.value?.let { session ->
            launch {
                webSocketManager.subscribeToAuction(session, headersUriStart) { update ->
                    //
                    // viewModel -> auctionBidLog -> update->
                }
            }
            launch {
                webSocketManager.subscribeToAuction(session, headersUriBid) { update ->
                    // message -> 입찰 일 경우
                }
            }
            launch {
                webSocketManager.subscribeToAuction(session, headersUriEnd) { update ->
                    // message -> 종료 일 경우
                }
            }
        }
    }

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
            // 위에서 선언한 auctionBidLog
        )

        Spacer(modifier = modifier.height(4.dp))
//        BidPriceInputTextField(bidPrice = price.toString(), onValueChange = {
//            setPrice(it.toInt())
//        })
        BidPriceInputTextField(
            bidPrice = price,
            onValueChange = { input ->
                price = validateAndFormatPrice(input).toInt()
            },
        )

        Spacer(modifier = modifier.height(4.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "10% = 100${stringResource(id = R.string.coin_price_unit)}",
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
                onClick = {
                    val increment = calculateIncrementAmount(100)
                    price = (getTopBidAmount() + increment).coerceAtLeast(price + increment)
                },
            )
            BidPriceUnitButton(
                modifier = modifier.weight(1f),
                bidPriceUnit = 5,
                onClick = {
                    val increment = calculateIncrementAmount(50)
                    price = (getTopBidAmount() + increment).coerceAtLeast(price + increment)
                },
            )
            BidPriceUnitButton(
                modifier = modifier.weight(1f),
                bidPriceUnit = 1,
                onClick = {
                    val increment = calculateIncrementAmount(10)
                    price = (getTopBidAmount() + increment).coerceAtLeast(price + increment)
                },
            )
        }
        Spacer(modifier = modifier.height(20.dp))
        CustomNoRippleButton(
            text = "입찰하기",
            onClick = {
                scope.launch {
                    stompSession.value?.let { session ->
                        webSocketManager.sendBid(session, BidMessage(1, 1, price, 1))
                    }
                }
            },
        )
    }
}
