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

    private val moshi =
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    // AuctionUpdate 어댑터 생성
    private val auctionUpdateAdapter = moshi.adapter(AuctionUpdate::class.java).lenient()

    suspend fun connectToAuction(auctionId: String): StompSession {
//        val url = "ws://192.168.100.69:8090/ws/auction"
        val url = "http://192.168.100.203:8081/ws-stomp"
        return stompClient.connect(url)
    }

    suspend fun sendBid(
        stompSession: StompSession,
        bidRequest: BidRequest,
    ) {
        Log.d(TAG, "sendBid: 보내기!!")
        // BidRequest를 JSON 문자열로 변환
        val bidRequestAdapter = moshi.adapter(BidRequest::class.java).lenient()
        val bidJson = bidRequestAdapter.toJson(bidRequest)
        val headers = StompSendHeaders("/app/auction/1/bid")

        // FrameBody로 감싸서 전송
        val frameBody = FrameBody.Text(bidJson)

        // 변환된 JSON 문자열을 Stomp 메시지로 전송

        stompSession.send(headers, frameBody)
    }

    suspend fun subscribeToAuction(
        stompSession: StompSession,
        headersUri: String,
        onAuctionUpdate: (AuctionUpdate) -> Unit,
    ) {
        // StompSubscribeHeaders를 사용해 헤더 생성
        val headers = StompSubscribeHeaders(headersUri)
        Log.d(TAG, "구독 좋아요 $headersUri")

        // 구독하고 메시지를 처리
        stompSession.subscribe(headers).collect { message ->
            val body = message.body
            if (body is FrameBody.Text) {
                val textBody = body.text
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
    val auctionId: Int,
    val userId: Int,
    val bidAmount: Int,
)

@JsonClass(generateAdapter = true)
data class AuctionUpdate(
    val auctionId: Int,
    val userId: Int,
    val bidAmount: Int,
)
