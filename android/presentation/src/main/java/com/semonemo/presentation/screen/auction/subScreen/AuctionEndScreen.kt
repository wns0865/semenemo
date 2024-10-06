package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.screen.auction.AuctionDetailViewModel
import com.semonemo.presentation.theme.Typography

@Preview(showBackground = true)
@Composable
fun AuctionEndScreen(
    modifier: Modifier = Modifier,
    viewModel: AuctionDetailViewModel = hiltViewModel(),
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "경매가 종료되었습니다",
                fontSize = 24.sp,
                style = Typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 추가적인 UI 요소 (예: 로딩 애니메이션 등)
        }
    }
}
