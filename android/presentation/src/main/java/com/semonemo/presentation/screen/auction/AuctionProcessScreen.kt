package com.semonemo.presentation.screen.auction

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.R
import com.semonemo.presentation.component.AuctionBidPriceInput
import com.semonemo.presentation.component.BidPriceUnitButton
import com.semonemo.presentation.component.CustomAuctionProgressBar
import com.semonemo.presentation.component.CustomNoRippleButton
import com.semonemo.presentation.screen.auction.subScreen.BidInfo
import com.semonemo.presentation.theme.Typography
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompSession
import kotlin.math.pow

private const val TAG = "AuctionProcessScreen"

@Preview
@Composable
fun AuctionProcessScreen(
    modifier: Modifier = Modifier,
    auctionId: Long = 3L,
    testAuctionId: Long = 14L,
    viewModel: AuctionProcessViewModel = hiltViewModel(),
) {
    val viewModelAuctionBidLog = viewModel.auctionBidLog.value
    val viewModelTopPrice = viewModel.topPrice.longValue
    val viewModelMyBidPrice = viewModel.myBidPrice.longValue
    val myPercentage = viewModel.myPercentage.intValue
    val scope = rememberCoroutineScope()
    val webSocketManager = remember { WebSocketManager() }

//    val (price, setPrice) = remember { mutableIntStateOf(0) }

    // 최상위 입찰가를 계산하는 함수

    // 버튼 클릭 시 증가할 금액을 계산하는 함수
    fun calculateIncrementAmount(percentage: Int): Long {
        val baseIncrement = 10.0.pow(viewModelTopPrice.toDouble()).toLong()
        return when (percentage) {
            100 -> baseIncrement
            50 -> baseIncrement / 2
            10 -> baseIncrement / 10
            else -> 0
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
        val headersUriMain = "/topic/auction/$testAuctionId"
        val headersUriStart = "/topic/auction/$testAuctionId/start"
        val headersUriBid = "/topic/auction/$testAuctionId/bid"
        val headersUriEnd = "/topic/auction/$testAuctionId/end"
        val headersUriParticipants = "/topic/auction/$testAuctionId/participants"
        // 경매 업데이트를 구독
        stompSession.value?.let { session ->
            launch {
                webSocketManager.subscribeToAuction(
                    session,
                    headersUriStart,
                    onAuctionStart = {
                        viewModel.updateStartMessage(it)
                        viewModel.updateBidPriceUnit()
                    },
                )
            }
            launch {
                webSocketManager.subscribeToAuction(
                    session,
                    headersUriMain,
                    onBidUpdate = {
                        // VIEWMDOEL.QWEQW(IT)
                        viewModel.updateBidLog(it.toAuctionBidLog())
                        viewModel.adjustClear()
                        viewModel.updateBidPriceUnit()
                    },
                )
            }
            launch {
                webSocketManager.subscribeToAuction(
                    session,
                    headersUriBid,
                )
            }
            launch {
                webSocketManager.subscribeToAuction(
                    session,
                    headersUriEnd,
                    onAuctionEnd = {
                    },
                )
            }
            launch {
                webSocketManager.subscribeToAuction(
                    session,
                    headersUriParticipants,
                    onAuctionEnd = {
                    },
                    onBidUpdate = {
                    },
                )
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
        val adjustEndTime =
            viewModel.auctionBidLog.value
                .lastOrNull()
                ?.endTime
        CustomAuctionProgressBar(endTime = adjustEndTime ?: viewModel.endTime.value)
        Spacer(modifier = modifier.height(20.dp))
        BidInfo(
            bidLogList = viewModelAuctionBidLog,
            startPrice = viewModelTopPrice,
            // 위에서 선언한 auctionBidLog
        )

        Spacer(modifier = modifier.height(4.dp))
//        BidPriceInputTextField(bidPrice = price.toString(), onValueChange = {
//            setPrice(it.toInt())
//        })
//        BidPriceInputTextField(
//            bidPrice = price,
//            onValueChange = { input ->
//                price = input
//            },
//        )
        AuctionBidPriceInput(
            modifier = modifier,
            currentPrice = viewModelTopPrice,
            adjustedAmount = viewModelMyBidPrice,
            adjustedPercentage = myPercentage,
            onClear = { viewModel.adjustClear() },
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
                bidPricePercentage = 100,
                bidPriceUnit = viewModel.bidPriceUnit.longValue * 10,
                onClick = {
                    val increment = calculateIncrementAmount(100)
                    viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue * 10, 100)
                },
            )
            BidPriceUnitButton(
                modifier = modifier.weight(1f),
                bidPricePercentage = 50,
                bidPriceUnit = viewModel.bidPriceUnit.longValue * 5,
                onClick = {
                    val increment = calculateIncrementAmount(50)
                    viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue * 5, 50)
                },
            )
            BidPriceUnitButton(
                modifier = modifier.weight(1f),
                bidPricePercentage = 10,
                bidPriceUnit = viewModel.bidPriceUnit.longValue,
                onClick = {
                    val increment = calculateIncrementAmount(10)
                    viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue, 10)
                },
            )
        }
        Spacer(modifier = modifier.height(20.dp))
        CustomNoRippleButton(
            text = "입찰하기",
            onClick = {
                scope.launch {
                    stompSession.value?.let { session ->
                        Log.d(TAG, "입찰가: ${viewModelTopPrice + viewModelMyBidPrice}")
                        webSocketManager.sendBid(session, testAuctionId, BidMessage(1, viewModelTopPrice + viewModelMyBidPrice))
                    }
                }
            },
        )
    }
}
