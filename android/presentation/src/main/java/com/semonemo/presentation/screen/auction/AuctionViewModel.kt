package com.semonemo.presentation.screen.auction

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Auction
import com.semonemo.domain.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AuctionViewModel"

@HiltViewModel
class AuctionViewModel
    @Inject
    constructor(
        private val auctionRepository: AuctionRepository,
    ) : ViewModel() {
        private val _uiEvent = MutableSharedFlow<AuctionUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        var shortAuctionList = mutableStateOf<List<Auction>>(listOf())
            private set

        init {
            loadShortAuction()
        }

        fun loadShortAuction() {
            viewModelScope.launch {
                auctionRepository.getAllAuction().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            Log.d(TAG, "loadShortAuction: ${response.data}")
                            shortAuctionList.value = response.data
                        }
                    }
                }
            }
        }
    }
