package com.semonemo.presentation.screen.auction

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.AuctionBidMessage
import com.semonemo.presentation.screen.auction.subScreen.AuctionEndScreen
import com.semonemo.presentation.screen.auction.subScreen.AuctionProgressScreen
import com.semonemo.presentation.screen.auction.subScreen.AuctionReadyScreen
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

private const val TAG = "AuctionDetailScreen"

// 경매 상태 관리
enum class AuctionStatus {
    READY,
    PROGRESS,
    END,
    CANCEL,
}

fun parseAuctionStatus(status: String): AuctionStatus = AuctionStatus.valueOf(status)

// 유저 상태 관리
enum class UserStatus {
    NOT_READY, // 준비 전
    READY, // 준비
    IN_PROGRESS, // 플레이 중
    COMPLETED, // 종료
}

@Preview
@Composable
fun AuctionDetailScreen(
    modifier: Modifier = Modifier,
    auctionId: Long = 0L,
    testAuctionId: Long = 12L,
    viewModel: AuctionDetailViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState(pageCount = { AuctionStatus.entries.size })
    val ipfsUrl = BuildConfig.IPFS_READ_URL
    val imgUrl = ipfsUrl + "ipfs/" + viewModel.nftImageUrl.value
    val context = LocalContext.current

    val webSocketManager = viewModel.webSocketManager.value
    // stompSession을 remember로 관리하여 전역적으로 유지
    val stompSession = viewModel.stompSession
    LaunchedEffect(auctionId) {
        // 세션이 초기화되지 않았을 때만 연결
        if (stompSession.value == null) {
            stompSession.value = webSocketManager?.connectToAuction()
        }
        val headersUriMain = "/topic/auction/$auctionId"
        val headersUriStart = "/topic/auction/$auctionId/start"
        val headersUriBid = "/topic/auction/$auctionId/bid"
        val headersUriEnd = "/topic/auction/$auctionId/end"
        val headersUriParticipants = "/topic/auction/$auctionId/participants"
        // 경매 업데이트를 구독
        stompSession.value?.let { session ->
            launch {
                // start 구독 : 경매 시작
                webSocketManager?.subscribeToAuction(
                    session,
                    headersUriStart,
                    onAuctionStart = {
                        viewModel.updateStartMessage(it)
                        viewModel.updateBidPriceUnit()
                    },
                )
            }
            launch {
                // main 구독 : 입찰 수신
                webSocketManager?.subscribeToAuction(
                    session,
                    headersUriMain,
                    onBidUpdate = {
                        viewModel.updateBidLog(it.toAuctionBidLog())
                        viewModel.adjustClear()
                        viewModel.updateBidPriceUnit()
                        viewModel.observeBidMessage(it)
                    },
                )
            }
            launch {
                // bid 구독 : 입찰 발신
                webSocketManager?.subscribeToAuction(
                    session,
                    headersUriBid,
                )
            }
            launch {
                // end 구독 : 경매 종료
                webSocketManager?.subscribeToAuction(
                    session,
                    headersUriEnd,
                    onAuctionEnd = {
                        viewModel.updateEndMessage(it)
                    },
                )
            }
            launch {
                // participants 구독 : 참여자 수
                webSocketManager?.subscribeToAuction(
                    session,
                    headersUriParticipants,
                    onParticipants = {
                        viewModel.updateParticipantsMessage(it)
                    },
                )
            }
        }
    }

    LaunchedEffect(viewModel.auctionStatus.value) {
        when (viewModel.auctionStatus.value) {
            AuctionStatus.PROGRESS -> { // 경매 시작 | 유저 준비
                if (viewModel.userStatus.value == UserStatus.READY) {
                    pagerState.animateScrollToPage(1)
                }
            }
            AuctionStatus.END -> {
                pagerState.animateScrollToPage(2)
            }
            AuctionStatus.CANCEL -> {
                pagerState.animateScrollToPage(2)
            }
            else -> {}
        }
    }

    LaunchedEffect(viewModel.userStatus.value) {
        when (viewModel.userStatus.value) {
            UserStatus.READY -> {
                if (viewModel.auctionStatus.value == AuctionStatus.PROGRESS) {
                    pagerState.animateScrollToPage(1)
                }
            } else -> {}
        }
    }

    // 사운드 재생을 위한 LaunchedEffect 추가
    LaunchedEffect(viewModel.observedBidMessage.value) {
        if (viewModel.observedBidMessage.value) {
            // MediaPlayer 초기화
            val mediaPlayer = MediaPlayer.create(context, R.raw.sound_bid_effect)
            mediaPlayer.start()
            // 사운드 재생 완료 후 리소스 해제
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
            }
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Spacer(modifier = modifier.height(30.dp))
        GlideImage(
            imageModel = imgUrl,
            contentScale = ContentScale.Fit,
            modifier =
                modifier
                    .height(360.dp)
                    .width(240.dp)
                    .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = modifier.height(20.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_auction_human),
                contentDescription = "participant",
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = String.format("%,d", viewModel.participant.value),
                fontSize = 20.sp,
                style =
                    Typography.bodyMedium.copy(
                        fontFeatureSettings = "tnum",
                        color = GunMetal,
                    ),
            )
        }

        // ---------------
//        val adjustEndTime =
//            viewModel.auctionBidLog.value
//                .lastOrNull()
//                ?.endTime
//        CustomAuctionProgressBar(endTime = adjustEndTime ?: viewModel.endTime.value)
//        Spacer(modifier = modifier.height(20.dp))
//        BidInfo(
//            bidLogList = viewModelAuctionBidLog,
//            startPrice = viewModelTopPrice,
//            // 위에서 선언한 auctionBidLog
//        )
//
//        Spacer(modifier = modifier.height(4.dp))
//        AuctionBidPriceInput(
//            modifier = modifier,
//            currentPrice = viewModelTopPrice,
//            adjustedAmount = viewModelMyBidPrice,
//            adjustedPercentage = myPercentage,
//            onClear = { viewModel.adjustClear() },
//        )
//
//        Spacer(modifier = modifier.height(4.dp))
//        Row(
//            modifier = modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//        ) {
//            Text(
//                text = "10% = 100${stringResource(id = R.string.coin_price_unit)}",
//                style =
//                    Typography.labelMedium.copy(
//                        fontFeatureSettings = "tnum",
//                        fontSize = 20.sp,
//                    ),
//            )
//            Text(
//                text = "",
//                style =
//                    Typography.labelMedium.copy(
//                        fontFeatureSettings = "tnum",
//                        fontSize = 20.sp,
//                    ),
//            )
//        }
//        Spacer(modifier = modifier.height(4.dp))
//        Row(
//            modifier = modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
//            BidPriceUnitButton(
//                modifier = modifier.weight(1f),
//                bidPricePercentage = 100,
//                bidPriceUnit = viewModel.bidPriceUnit.longValue * 10,
//                onClick = {
//                    viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue * 10, 100)
//                },
//            )
//            BidPriceUnitButton(
//                modifier = modifier.weight(1f),
//                bidPricePercentage = 50,
//                bidPriceUnit = viewModel.bidPriceUnit.longValue * 5,
//                onClick = {
//                    viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue * 5, 50)
//                },
//            )
//            BidPriceUnitButton(
//                modifier = modifier.weight(1f),
//                bidPricePercentage = 10,
//                bidPriceUnit = viewModel.bidPriceUnit.longValue,
//                onClick = {
//                    viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue, 10)
//                },
//            )
//        }
//        Spacer(modifier = modifier.height(20.dp))
//        CustomNoRippleButton(
//            text = "입찰하기",
//            onClick = {
//                scope.launch {
//                    stompSession.value?.let { session ->
//                        webSocketManager?.sendBid(
//                            session,
//                            testAuctionId,
//                            BidMessage(
//                                myId,
//                                viewModel.anonym.intValue,
//                                viewModelTopPrice + viewModelMyBidPrice,
//                            ),
//                        )
//                        isBidSubmitted.value = true
//                        delay(2000L)
//                        isBidSubmitted.value = false
//                    }
//                }
//            },
//        )

        HorizontalPager(
            modifier = modifier.fillMaxWidth(),
            state = pagerState,
            userScrollEnabled = true,
        ) { page ->
            when (AuctionStatus.entries[page]) {
                AuctionStatus.READY ->
                    AuctionReadyScreen(
                        modifier = modifier,
                        viewModel = viewModel,
                    )

                AuctionStatus.PROGRESS ->
                    AuctionProgressScreen(
                        modifier = modifier,
                        viewModel = viewModel,
                    )

                AuctionStatus.END -> AuctionEndScreen(modifier = modifier, viewModel = viewModel)
                AuctionStatus.CANCEL -> AuctionEndScreen(modifier = modifier, viewModel = viewModel)
            }
        }
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        AnimatedVisibility(
            visible = viewModel.observedBidMessage.value,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AuctionBidMessage(
                modifier = Modifier.padding(top = 250.dp), // 화면 위에서 조금 아래로 내림
                icon = R.drawable.ic_color_sene_coin,
                bidPrice = viewModel.topPrice.longValue + viewModel.myBidPrice.longValue,
                anonym = viewModel.anonym.intValue,
                user = viewModel.observedBidSubmit.value,
            )
        }
    }
}
