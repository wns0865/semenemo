package com.semonemo.presentation.screen.auction

import android.util.Log
import com.semonemo.domain.model.AuctionBidLog
import com.semonemo.presentation.BuildConfig
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "WebSocketManager"

class WebSocketManager {
    private val webSocketClient = OkHttpWebSocketClient()
    private val stompClient = StompClient(webSocketClient)

    private val moshi =
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .add(LocalDateTimeAdapter())
            .build()

    // AuctionUpdate 어댑터 생성
    private val auctionUpdateAdapter = moshi.adapter(BidMessage::class.java).lenient()
//    private val entranceAdapter = moshi.adapter()

    suspend fun connectToAuction(auctionId: Long): StompSession {
//        val url = "ws://192.168.100.69:8090/ws/auction"
//        val url = "http://192.168.100.203:8081/ws-stomp"
        val url = "${BuildConfig.SEVER_URL}ws-stomp"
        return stompClient.connect(url)
    }

    suspend fun sendBid(
        stompSession: StompSession,
        auctionId: Long,
        bidRequest: BidMessage,
    ) {
        Log.d(TAG, "sendBid: 보내기!!")
        Log.d(TAG, "auction: $auctionId")
        // BidRequest를 JSON 문자열로 변환
        val bidRequestAdapter = moshi.adapter(BidMessage::class.java).lenient()
        val bidJson = bidRequestAdapter.toJson(bidRequest)
        val headers = StompSendHeaders("/app/auction/$auctionId/bid")

        // FrameBody로 감싸서 전송
        val frameBody = FrameBody.Text(bidJson)

        // 변환된 JSON 문자열을 Stomp 메시지로 전송

        stompSession.send(headers, frameBody)
    }

    suspend fun subscribeToAuction(
        stompSession: StompSession,
        headersUri: String,
        onBidUpdate: (BidMessage) -> Unit,
        onAuctionEnd: () -> Unit,
    ) {
        // StompSubscribeHeaders를 사용해 헤더 생성
        val headers = StompSubscribeHeaders(headersUri)
        Log.d(TAG, "구독 좋아요 $headersUri")

        // 구독하고 메시지를 처리
        stompSession.subscribe(headers).collect { message ->
            val msgHeaders = message.headers
            val msgBody = message.body

            if (msgBody is FrameBody.Text) {
                val testHeaders = msgHeaders.destination
                val textBody = msgBody.text
                Log.d(TAG, "msgHeaders: $testHeaders")
                Log.d(TAG, "msgBody: $textBody")
                runCatching {
                    auctionUpdateAdapter.fromJson(textBody)
                }.onSuccess { auctionUpdate ->
                    if (auctionUpdate != null) {
                        when (msgHeaders.containsKey("end")) {
                            true -> {
                                onAuctionEnd()
                            }
                            else -> {
                                onBidUpdate(auctionUpdate)
                            }
                        }
                    } else {
                        Log.e("AuctionProcess", "파싱 실패: JSON 데이터를 변환할 수 없습니다.")
                    }
                }.onFailure { exception ->
                    Log.e("AuctionProcess", "JSON 파싱 중 오류 발생", exception)
                }
            } else if (msgBody is FrameBody.Binary) {
                Log.e(TAG, "Binary frame received, expected text")
            }
        }
    }

    suspend fun exitAuction(
        stompSession: StompSession,
        auctionId: Long,
        userId: Int,
    ) {
        val headers = StompSendHeaders("/app/auction/$auctionId/exit")
    }
}

enum class MessageType {
    BID,
    ENTRANCE,
    AUCTION_STATUS,
}

fun BidMessage.toAuctionBidLog(): AuctionBidLog =
    AuctionBidLog(
        userId = this.userId,
        bidAmount = this.bidAmount,
        bidTime = this.bidTime,
        endTime = this.endTime,
    )

@JsonClass(generateAdapter = true)
data class BidMessage(
    val userId: Long,
    val bidAmount: Long,
    val bidTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = LocalDateTime.now(),
)

@JsonClass(generateAdapter = true)
data class BidMessageResponse(
    val id: Long,
    val startPrice: Long,
    val currentBid: Long,
    val currentBidder: Long,
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = LocalDateTime.now(),
)

@JsonClass(generateAdapter = true)
data class EntranceMessage(
    val auctionId: Long,
    val userId: Int,
    val status: Int,
)

@JsonClass(generateAdapter = true)
data class AuctionStatusMessage(
    val auctionId: Long,
    val status: Int,
)

class LocalDateTimeAdapter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @ToJson
    fun toJson(value: LocalDateTime): String = value.format(formatter)

    @FromJson
    fun fromJson(value: String): LocalDateTime = LocalDateTime.parse(value, formatter)
}
