package com.semonemo.presentation.screen.auction

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.AuctionBidLog
import com.semonemo.domain.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompSession
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.pow

private const val TAG = "AuctionDetailViewModel"

@HiltViewModel
class AuctionDetailViewModel
    @Inject
    constructor(
        private val auctionRepository: AuctionRepository,
        private val saveStateHandle: SavedStateHandle,
        private val authDataSource: AuthDataSource,
    ) : ViewModel() {
        // 경매 상태 관리
        var auctionStatus = mutableStateOf(AuctionStatus.READY)
            private set
        var userStatus = mutableStateOf(UserStatus.NOT_READY)
            private set
        var webSocketManager = mutableStateOf<WebSocketManager?>(null)
            private set
        var stompSession = mutableStateOf<StompSession?>(null)
            private set
        var auctionId = saveStateHandle["auctionId"] ?: -1L // 경매 번호
            private set
        var userId: Long = 0L // 유저 ID
        var nftImageUrl = mutableStateOf("")
        var anonym = mutableIntStateOf(1) // 익명 번호
            private set
        var participant = mutableIntStateOf(0) // 참여자 수
            private set
        var auctionBidLog = mutableStateOf<List<AuctionBidLog>>(listOf()) // 경매 로그
            private set
        var bidPriceUnit = mutableLongStateOf(1L) // 경매 단가 (10%)
            private set
        var topPrice = mutableLongStateOf(0L) // 상위 입찰가
            private set
        var myPercentage = mutableIntStateOf(0) // 입력 퍼센트
            private set
        var myBidPrice = mutableLongStateOf(0L) // 입력 입찰 금액
            private set
        var endTime = mutableStateOf(LocalDateTime.now().plusSeconds(15)) // 종료 시간
            private set

        var result = mutableStateOf<EndMessage?>(null) // 경매 결과 (종료 시)
            private set
        private val _uiEvent = MutableSharedFlow<AuctionUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            viewModelScope.launch {
                userId = authDataSource.getUserId()?.toLong() ?: 0L
                Log.d(TAG, "userId: $userId")
            }
            initWebSocketManager()
            initStompSession()
            loadAuctionDetail()
//            joinAuction()
        }

        private fun loadAuctionDetail() {
            if (auctionId == -1L) {
                viewModelScope.launch {
                    _uiEvent.emit(AuctionUiEvent.Error("경매가 종료되었습니다."))
                }
                return
            }
            viewModelScope.launch {
                auctionRepository.getAuction(auctionId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            nftImageUrl.value = response.data.nftImageUrl
                            participant.intValue = response.data.participants
                        }
                    }
                }
            }
        }

        /**
         * 경매를 시작
         */
        fun startAuction() {
            if (auctionId == -1L) {
                viewModelScope.launch {
                    _uiEvent.emit(AuctionUiEvent.Error("경매가 종료되었습니다."))
                }
                return
            }
            viewModelScope.launch {
                auctionRepository.startAuction(auctionId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            auctionStatus.value = AuctionStatus.PROGRESS
                        }
                    }
                }
            }
        }

        /**
         * 해당 경매에 참여
         */
        fun joinAuction() {
            if (auctionId == -1L) {
                viewModelScope.launch {
                    _uiEvent.emit(AuctionUiEvent.Error("경매가 종료되었습니다."))
                }
                return
            }
            viewModelScope.launch {
                auctionRepository.joinAuction(auctionId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            Log.d(TAG, "joinAuction: ${response.data}")
                            auctionBidLog.value = response.data.bidLogs
                            userStatus.value = UserStatus.READY
                        }
                    }
                }
            }
        }

        fun initWebSocketManager() {
            webSocketManager.value = WebSocketManager()
        }

        fun initStompSession() {
            viewModelScope.launch {
                if (stompSession.value != null) {
                    stompSession.value = webSocketManager.value?.connectToAuction()
                }
            }
        }

        fun updateStartMessage(startMessage: StartMessage) {
            topPrice.longValue = startMessage.currentBid
        }

        fun updateParticipants(participants: Int) {
            participant.intValue = participants
        }

        /** log10을 통해 10%의 입찰 단가를 계산 */
        fun updateBidPriceUnit() {
            val unit = log10(topPrice.longValue.toDouble()).toInt() - 1
            bidPriceUnit.longValue = 10.0.pow(unit).toLong()
        }

        fun updateEndMessage(endMessage: EndMessage) {
            result.value = endMessage
        }

        /**
         * 입찰 로그 리스트를 갱신
         * @param bidLog 입찰 로그
         */
        fun updateBidLog(bidLog: AuctionBidLog) {
            auctionBidLog.value += bidLog
            topPrice.longValue = bidLog.bidAmount
            endTime.value = bidLog.endTime
        }

        /** 사용자가 입력하는 입찰가를 초기화 */
        fun adjustClear() {
            myBidPrice.longValue = 0
            myPercentage.intValue = 0
        }

        /**
         * 사용자가 입력하는 입찰가 조정
         * @param price 상위가에 추가되는 금액
         * @param percentage 입력하는 퍼센트 합
         */
        fun adjustBidPrice(
            price: Long,
            percentage: Int,
        ) {
            myBidPrice.longValue += price
            myPercentage.intValue += percentage
        }

        fun exitBidLog(bidLog: AuctionBidLog) {
            auctionBidLog.value -= bidLog
        }
    }
