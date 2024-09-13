package com.semonemo.presentation.screen.login

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.metamask.androidsdk.Dapp
import io.metamask.androidsdk.ErrorType
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.EthereumMethod
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.Network
import io.metamask.androidsdk.RequestError
import javax.inject.Inject

@HiltViewModel
class EthereumViewModel
    @Inject
    constructor(
        private val ethereum: Ethereum,
        private val dApp: Dapp,
    ) : ViewModel() {
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
                    callback(ethereum.selectedAddress)
                }
            }
        }

        private fun addEthereumChain(
            chainId: String,
            chainName: String,
            rpcUrls: String,
            onSuccess: (message: String) -> Unit,
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
                } else {
                    if (chainId == ethereum.chainId) {
                        onSuccess("Successfully switched to ${Network.chainNameFor(chainId)} ($chainId)")
                    } else {
                        onSuccess("Successfully added ${Network.chainNameFor(chainId)} ($chainId)")
                    }
                }
            }
        }

        fun switchChain(
            chainId: String,
            chainName: String,
            rpcUrls: String,
            onSuccess: (message: String) -> Unit,
            onError: (message: String, action: (() -> Unit)?) -> Unit,
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
                                onSuccess = { result -> onSuccess(result) },
                                onError = { error -> onError(error, null) },
                            )
                        }
                        onError(message, action)
                    } else {
                        onError("Switch 성공 에러", null)
                    }
                } else {
                    onSuccess("Switch 성공")
                }
            }
        }
    }
