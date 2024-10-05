package com.semonemo.presentation.screen.auction

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.AuctionBidLog
import com.semonemo.domain.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.pow

@HiltViewModel
class AuctionProcessViewModel
    @Inject
    constructor(
        private val auctionRepository: AuctionRepository,
        private val saveStateHandle: SavedStateHandle,
    ) : ViewModel() {
        var currentParticipant = mutableIntStateOf(0)
            private set
        var auctionBidLog = mutableStateOf<List<AuctionBidLog>>(listOf())
            private set
        var bidPriceUnit = mutableLongStateOf(1L)
            private set
        var topPrice = mutableLongStateOf(0L)
            private set
        var myPercentage = mutableIntStateOf(0)
            private set
        var myBidPrice = mutableLongStateOf(0L)
            private set
        var endTime = mutableStateOf(LocalDateTime.now().plusSeconds(15))
            private set
        var auctionId = saveStateHandle["auctionId"] ?: -1L
            private set
        private val _uiEvent = MutableSharedFlow<AuctionUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            joinAuction()
        }

        private fun joinAuction() {
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
                            auctionBidLog.value = response.data
                        }
                    }
                }
            }
        }

        fun updateStartMessage(startMessage: StartMessage) {
            topPrice.longValue = startMessage.currentBid
        }

        fun updateParticipants(participants: Int) {
            currentParticipant.intValue = participants
        }

        fun updateBidPriceUnit() {
            val unit = log10(topPrice.longValue.toDouble()).toInt() - 1
            bidPriceUnit.longValue = 10.0.pow(unit).toLong()
        }

        fun calculateBidPriceUnit() {
        }

        fun updateBidLog(bidLog: AuctionBidLog) {
            auctionBidLog.value += bidLog
            topPrice.longValue = bidLog.bidAmount
            endTime.value = bidLog.endTime
        }

        fun adjustClear() {
            myBidPrice.longValue = 0
            myPercentage.intValue = 0
        }

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
