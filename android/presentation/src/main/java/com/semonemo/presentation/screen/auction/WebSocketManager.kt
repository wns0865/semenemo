package com.semonemo.presentation.screen.auction

import android.util.Log
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient

private const val TAG = "WebSocketManager"

class WebSocketManager {
    private val webSocketClient = OkHttpWebSocketClient()
    private val stompClient = StompClient(webSocketClient)

    // Moshi 인스턴스를 생성
    val moshi =
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory()) // Kotlin 지원을 추가
            .build()

    // AuctionUpdate 어댑터 생성
    val auctionUpdateAdapter = moshi.adapter(AuctionUpdate::class.java).lenient()

    suspend fun connectToAuction(auctionId: String): StompSession {
//        val url = "ws://localhost:8090/ws/$auctionId"
        val url = "ws://192.168.100.69:8090/ws/auction"
        return stompClient.connect(url)
    }

    suspend fun sendBid(
        stompSession: StompSession,
        bid: Int,
    ) {
        Log.d(TAG, "sendBid: 보내기!!")
        // BidRequest를 JSON 문자열로 변환
        val bidRequestAdapter = moshi.adapter(BidRequest::class.java).lenient()
        val bidJson = bidRequestAdapter.toJson(BidRequest(bid))
        val headers = StompSendHeaders("/app/bid")

        // FrameBody로 감싸서 전송
        val frameBody = FrameBody.Text(bidJson)

        // 변환된 JSON 문자열을 Stomp 메시지로 전송

        stompSession.send(headers, frameBody)
    }

    suspend fun subscribeToAuction(
        stompSession: StompSession,
        onAuctionUpdate: (AuctionUpdate) -> Unit,
    ) {
        // StompSubscribeHeaders를 사용해 헤더 생성
        val headers = StompSubscribeHeaders("/topic/auctionUpdates")
        Log.d(TAG, "구독 좋아요")
        // 구독하고 메시지를 처리
        stompSession.subscribe(headers).collect { message ->
            val body = message.body
            if (body is FrameBody.Text) {
                val textBody = body.text // UTF-8로 디코딩된 텍스트 사용
                Log.d(TAG, "subscribeToAuction: $textBody")

                try {
                    val auctionUpdate = auctionUpdateAdapter.fromJson(textBody)
                    if (auctionUpdate != null) {
                        onAuctionUpdate(auctionUpdate)
                    } else {
                        Log.e(TAG, "파싱 실패: JSON 데이터를 변환할 수 없습니다.")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "JSON 파싱 중 오류 발생", e)
                }
            } else if (body is FrameBody.Binary) {
                Log.e(TAG, "Binary frame received, expected text")
            }
        }
    }
}

@JsonClass(generateAdapter = true)
data class BidRequest(
    val bid: Int?,
)

@JsonClass(generateAdapter = true)
data class AuctionUpdate(
    val currentPrice: Int,
    val timeLeft: Float,
)
