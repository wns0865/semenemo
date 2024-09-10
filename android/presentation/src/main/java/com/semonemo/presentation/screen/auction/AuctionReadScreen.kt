package com.semonemo.presentation.screen.auction

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.component.LiveAuctionCard
import com.semonemo.presentation.theme.SemonemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true, name = "AuctionReadScreen")
@Composable
fun AuctionReadScreen() {
    SemonemoTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            // 상단 제목
            Text(
                text = "진행 중인 단기 경매",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp),
            )

            // LazyRow의 스크롤 상태를 저장
            val listState = rememberLazyListState()

            // CoroutineScope를 기억
            val coroutineScope = rememberCoroutineScope()

            // 사용자가 마지막으로 스크롤한 시간을 추적
            var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }

            // 경매 데이터
            val auctionDataList = getSampleAuctionData()

            // 사용자가 1초 동안 상호작용하지 않았을 때 자동으로 스크롤 시작
            LaunchedEffect(lastInteractionTime) {
                while (true) {
                    if (System.currentTimeMillis() - lastInteractionTime > 1000) {
                        coroutineScope.launch {
                            val currentIndex = listState.firstVisibleItemIndex

                            // 리스트의 끝에 도달하면 처음으로 돌아가기
                            if (currentIndex == auctionDataList.size - 1) {
                                listState.scrollToItem(0) // 애니메이션 없이 즉시 첫 번째로 스크롤
                            } else {
                                listState.animateScrollToItem(currentIndex + 1) // 부드럽게 다음 아이템으로 스크롤
                            }
                        }
                    }
                    delay(2000L) // 2초마다 스크롤
                }
            }

            // 수평 스크롤 가능한 경매 리스트
            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier =
                    Modifier
                        .pointerInput(Unit) {
                            // 사용자가 스크롤할 때마다 상호작용 시간 업데이트
                            detectTapGestures {
                                lastInteractionTime = System.currentTimeMillis()
                            }
                        },
            ) {
                items(auctionDataList) { auctionData ->
                    LiveAuctionCard(
                        viewerCount = auctionData.viewerCount,
                        likeCount = auctionData.likeCount,
                        price = auctionData.price,
                        imageUrl = auctionData.imageUrl,
                        modifier =
                            Modifier
                                .width(160.dp) // 카드의 너비 설정
                                .height(300.dp),
                    )
                }
            }
        }
    }
}

// 샘플 데이터 생성을 위한 데이터 클래스와 함수는 그대로 유지
data class AuctionData(
    val viewerCount: Int,
    val likeCount: Int,
    val price: Int,
    val imageUrl: String,
)

fun getSampleAuctionData(): List<AuctionData> =
    listOf(
        AuctionData(
            123,
            20,
            300,
            "https://static.wikia.nocookie.net/shinchan/images/b/b2/%EC%8B%A0%EC%A7%B1%EA%B5%AC2.JPG/revision/latest?cb=20131026025408&path-prefix=ko",
        ),
        AuctionData(
            456,
            30,
            500,
            "https://i.namu.wiki/i/imS20jFHx8ISktmwqwfs90JMvIDykHuaiGZKZfWJUTBeYs6ovoqIBotk7trFa8nWj7dpWp_9Mzz2rv1TMHLskg.webp",
        ),
        AuctionData(
            789,
            40,
            700,
            "https://kr-cdn.spooncast.net/profiles/v/9MO6KvsperDEG/d5b9099a-859a-4cc8-8b81-fa23bd3e375a.jpg",
        ),
        AuctionData(
            789,
            42,
            500,
            "https://i.namu.wiki/i/2NE9ni_Jk32mN-zEQrswpjEA_iZ1lK_gbDo2tG44wlLxmN-0M4wp8ALSIX-Qxy1yK1fpBqEO1jDXxWyViV_pBA.webp",
        ),
        AuctionData(
            111,
            22,
            33,
            "https://i.namu.wiki/i/zfd-NOPP39XJ49BUBLXu8d3SAPsYnpvqYviuQHzSe8FqI6DhYAaHp5Nx30dWi_Q5XGUcbczMfuSp1lOMAN3NvA.webp",
        ),
        // 더 많은 샘플 데이터 추가...
    )
