package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items // 이 import를 추가하세요
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.component.AuctionItemCard

@Composable
fun AuctionItemGrid() {
    val auctionDataList = getSampleAuctionItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(auctionDataList) { auctionItem ->
            // 여기를 수정했습니다
            AuctionItemCard(
                title = auctionItem.title,
                author = auctionItem.author,
                imageUrl = auctionItem.imageUrl,
                remainingTime = auctionItem.remainingTime,
                price = auctionItem.price,
                isLiked = auctionItem.isLiked,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.75f),
            )
        }
    }
}

data class AuctionItem(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String,
    val remainingTime: String,
    val price: Int,
    val isLiked: Boolean,
)

fun getSampleAuctionItems(): List<AuctionItem> =
    listOf(
        AuctionItem(
            id = 1,
            title = "인사이드 아웃",
            author = "짜이한",
            imageUrl = "https://example.com/inside_out.jpg",
            remainingTime = "06:22:41:03",
            price = 20,
            isLiked = false,
        ),
        AuctionItem(
            id = 2,
            title = "루피와 함께",
            author = "짜이한",
            imageUrl = "https://example.com/loopy.jpg",
            remainingTime = "00:00:26:59",
            price = 70,
            isLiked = true,
        ),
        // 추가 아이템들...
        AuctionItem(
            id = 3,
            title = "뽀로로 어드벤처",
            author = "짜이한",
            imageUrl = "https://example.com/pororo.jpg",
            remainingTime = "02:14:35:22",
            price = 50,
            isLiked = false,
        ),
        AuctionItem(
            id = 4,
            title = "토이 스토리",
            author = "짜이한",
            imageUrl = "https://example.com/toy_story.jpg",
            remainingTime = "01:08:15:40",
            price = 80,
            isLiked = true,
        ),
    )
