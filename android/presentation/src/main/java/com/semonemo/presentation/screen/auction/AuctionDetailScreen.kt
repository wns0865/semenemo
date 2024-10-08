package com.semonemo.presentation.screen.auction

import android.media.MediaPlayer
import android.util.Log
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.AuctionBidMessage
import com.semonemo.presentation.component.AuctionTopAppBarTitle
import com.semonemo.presentation.component.CustomDialog
import com.semonemo.presentation.component.CustomTextButton
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.component.TopAppBarNavigationType
import com.semonemo.presentation.screen.auction.subScreen.AuctionEndScreen
import com.semonemo.presentation.screen.auction.subScreen.AuctionProgressScreen
import com.semonemo.presentation.screen.auction.subScreen.AuctionReadyScreen
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

private const val TAG = "AuctionDetailScreen"

// 경매 상태 관리
enum class AuctionStatus(
    val page: Int,
) {
    READY(0),
    PROGRESS(1),
    END(2),
    CANCEL(3),
}

fun parseAuctionStatus(status: String): AuctionStatus = AuctionStatus.valueOf(status)

// 유저 상태 관리
enum class UserStatus {
    NOT_READY, // 준비 전
    READY, // 준비
    IN_PROGRESS, // 플레이 중
    COMPLETED, // 종료
}

// 참가 모드
enum class ParticipationStatus {
    PARTICIPANTS, // 참가자
    OBSERVER, // 관찰자
}

@Preview
@Composable
fun AuctionDetailScreen(
    modifier: Modifier = Modifier,
    auctionId: Long = 0L,
    registerId: Long = 0L,
    popUpBackStack: () -> Unit = {},
    viewModel: AuctionDetailViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState(pageCount = { AuctionStatus.entries.size })
    val ipfsUrl = BuildConfig.IPFS_READ_URL
    val imgUrl = ipfsUrl + "ipfs/" + viewModel.nftImageUrl.value
    val context = LocalContext.current

    val webSocketManager = viewModel.webSocketManager.value
    // stompSession을 remember로 관리하여 전역적으로 유지
    val stompSession = viewModel.stompSession

    var showDialog by remember { mutableStateOf(false) }

    @Composable
    fun showCustomDialog(
        onConfirmClicked: () -> Unit,
        titleKeywords: List<String> = emptyList(),
        contentKeywords: List<String> = emptyList(),
    ) {
        CustomDialog(
            title = stringResource(R.string.auction_bid_give_up_title),
            content = stringResource(R.string.auction_bid_give_up_content),
            onConfirmMessage = stringResource(R.string.auction_bid_give_up_confirm),
            onDismissMessage = stringResource(R.string.auction_bid_give_up_dismiss),
            titleKeywords = titleKeywords,
            contentKeywords = contentKeywords,
            titleBrushFlag = titleKeywords.map { false },
            contentBrushFlag = contentKeywords.map { false },
            onConfirm = {
                showDialog = false // 다이얼로그 닫기
                onConfirmClicked()
            },
            onDismiss = {
                showDialog = false
            },
        )
    }

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
                    pagerState.scrollToPage(AuctionStatus.PROGRESS.page)
                }
            }

            AuctionStatus.END -> {
                pagerState.scrollToPage(AuctionStatus.END.page)
            }

            AuctionStatus.CANCEL -> {
                pagerState.scrollToPage(AuctionStatus.CANCEL.page)
            }

            else -> {}
        }
    }

    LaunchedEffect(viewModel.userStatus.value) {
        when (viewModel.userStatus.value) {
            UserStatus.READY -> {
                if (viewModel.auctionStatus.value == AuctionStatus.PROGRESS) {
                    pagerState.scrollToPage(AuctionStatus.PROGRESS.page)
                }
            }

            else -> {}
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
        modifier =
            modifier
                .padding(16.dp)
                .statusBarsPadding(),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        TopAppBar(
            modifier = Modifier,
            title = { AuctionTopAppBarTitle(modifier = Modifier, viewModel = viewModel) },
            navigationType = TopAppBarNavigationType.None,
            actionButtons = {
                CustomTextButton(
                    text = stringResource(id = R.string.auction_bid_give_up_button),
                    isPossible = true,
                    onClick = { showDialog = true },
                )
            },
        )
        Spacer(modifier = modifier.height(30.dp))
        GlideImage(
            imageModel = imgUrl,
            contentScale = ContentScale.Fit,
            modifier =
                modifier
                    .height(300.dp)
                    .width(200.dp)
                    .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = modifier.height(20.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
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

    // Composable이 사라질 때 leaveAuction() 호출
    DisposableEffect(Unit) {
        onDispose {
            Log.d(TAG, "Composable is being disposed. Calling leaveAuction()")
            viewModel.leaveAuction()
        }
    }

    if (showDialog) {
        showCustomDialog(
            onConfirmClicked = {
                popUpBackStack()
            },
        )
    }
}
