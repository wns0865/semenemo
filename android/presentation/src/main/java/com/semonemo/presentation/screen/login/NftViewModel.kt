package com.semonemo.presentation.screen.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.Transaction
import com.semonemo.domain.repository.NFTRepository
import com.semonemo.domain.request.TransferRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import io.metamask.androidsdk.Dapp
import io.metamask.androidsdk.ErrorType
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.EthereumMethod
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.RequestError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NftViewModel
    @Inject
    constructor(
        private val ethereum: Ethereum,
        private val dApp: Dapp,
        private val nftRepository: NFTRepository,
    ) : ViewModel() {
        private val walletAddress = mutableStateOf("")
        private val _transaction = MutableStateFlow<Transaction>(Transaction("", "", "", "", "", ""))
        val ethereumState =
            MediatorLiveData<EthereumState>().apply {
                addSource(ethereum.ethereumState) { newEthereumState ->
                    value = newEthereumState
                }
            }

        fun connect(callback: ((String) -> Unit)) {
            ethereum.connect(dApp) { result ->
                if (result is RequestError) {
                    callback(result.message)
                } else {
                    walletAddress.value = ethereum.selectedAddress
                    callback(ethereum.selectedAddress)
                }
            }
        }

        private fun addEthereumChain(
            chainId: String,
            chainName: String,
            rpcUrls: String,
            onError: (message: String) -> Unit,
        ) {
            val addChainParams: Map<String, Any> =
                mapOf(
                    "chainId" to chainId,
                    "chainName" to chainName,
                    "rpcUrls" to listOf(rpcUrls),
                )
            val addChainRequest =
                EthereumRequest(
                    method = EthereumMethod.ADD_ETHEREUM_CHAIN.value,
                    params = listOf(addChainParams),
                )

            ethereum.sendRequest(addChainRequest) { result ->
                if (result is RequestError) {
                    onError("Add chain error: ${result.message}")
                }
            }
        }

        fun switchChain(
            chainId: String,
            chainName: String,
            rpcUrls: String,
            onError: (message: String, action: (() -> Unit)?) -> Unit,
            onSuccess: (String) -> Unit,
        ) {
            val hexChainId = "0x" + chainId.toLong().toString(16)
            val switchChainParams: Map<String, String> = mapOf("chainId" to hexChainId)
            val switchChainRequest =
                EthereumRequest(
                    method = EthereumMethod.SWITCH_ETHEREUM_CHAIN.value,
                    params = listOf(switchChainParams),
                )

            ethereum.sendRequest(switchChainRequest) { result ->
                if (result is RequestError) {
                    if (result.code == ErrorType.UNRECOGNIZED_CHAIN_ID.code || result.code == ErrorType.SERVER_ERROR.code) {
                        val message = "${chainId}에 해당되는 체인이 없어서 추가해야함."
                        val action: () -> Unit = {
                            addEthereumChain(
                                hexChainId,
                                chainName,
                                rpcUrls,
                                onError = { error -> onError(error, null) },
                            )
                        }
                        onError(message, action)
                    } else {
                        onError("Switch 성공 에러", null)
                    }
                } else {
                    onSuccess(result.toString())
                }
            }
        }

        fun transfer(
            toAddress: String,
            amount: String,
        ) {
            viewModelScope.launch {
                nftRepository
                    .transfer(
                        TransferRequest(
                            fromAddress = walletAddress.value,
                            toAddress = toAddress,
                            amount = amount,
                        ),
                    ).collectLatest {
                        it?.let { transaction ->
                            _transaction.value = transaction
                        }
                    }
            }
        }

        fun sendTransaction() {
            val params: MutableMap<String, Any> =
                mutableMapOf(
                    "from" to _transaction.value.from,
                    "to" to _transaction.value.to,
                    "data" to _transaction.value.data,
                )

            val transactionRequest =
                EthereumRequest(
                    method = EthereumMethod.ETH_SEND_TRANSACTION.value,
                    params = listOf(params),
                )
            ethereum.run { sendRequest(transactionRequest) }
        }
    }
