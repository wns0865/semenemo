package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.spToDp

@Composable
fun BindInfo(
    modifier: Modifier = Modifier,
    topBidders: List<Pair<Int, Int>>,
) {
    Column(
        modifier = modifier,
    ) {
        // 첫 번째 항목을 특별히 처리하여 텍스트를 더 크게 설정
        topBidders.firstOrNull()?.let { (bidder, bidPrice) ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = String.format("%03d번", bidder),
                    fontSize = 32.sp, // 더 큰 텍스트 크기
                    style =
                        Typography.bodyMedium.copy(
                            fontFeatureSettings = "tnum",
                            color = GunMetal,
                        ),
                )
                Text(
                    text = String.format("%,d ${stringResource(id = R.string.coin_price_unit)}", bidPrice),
                    fontSize = 32.sp, // 더 큰 텍스트 크기
                    style =
                        Typography.bodyMedium.copy(
                            fontFeatureSettings = "tnum",
                            color = GunMetal,
                        ),
                )
            }
        }
        Spacer(modifier = modifier.height(8.dp))

        val itemHeight = 24.sp.spToDp() + 8.dp // 텍스트 크기 + 패딩으로 높이 계산

        LazyColumn(
            modifier =
                modifier
                    .fillMaxWidth()
                    .height(itemHeight * 4),
        ) {
            // 나머지 항목들 처리 - LazyColumn으로 구현
            items(topBidders.drop(1)) { (bidder, bidPrice) ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(itemHeight),
                    // 동적으로 계산된 항목 높이 적용
                ) {
                    Text(
                        text = String.format("%03d번", bidder),
                        fontSize = 24.sp, // 기본 텍스트 크기
                        style =
                            Typography.labelMedium.copy(
                                fontFeatureSettings = "tnum",
                                color = Gray02,
                            ),
                    )
                    Text(
                        text = String.format("%,d ${stringResource(id = R.string.coin_price_unit)}", bidPrice),
                        fontSize = 24.sp, // 기본 텍스트 크기
                        style =
                            Typography.labelMedium.copy(
                                fontFeatureSettings = "tnum",
                                color = Gray02,
                            ),
                    )
                }
            }
        }
    }
}
