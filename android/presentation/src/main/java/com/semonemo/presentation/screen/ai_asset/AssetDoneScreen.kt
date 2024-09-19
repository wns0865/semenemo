package com.semonemo.presentation.screen.ai_asset

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.Gray02

@Composable
fun AssetDoneScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BoldTextWithKeywords(
                    fullText = stringResource(R.string.asset_done_title),
                    keywords = arrayListOf("완료"),
                    brushFlag = arrayListOf(true),
                    boldStyle = Typography.titleMedium,
                    normalStyle = Typography.labelLarge.copy(fontSize = 24.sp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.img_firecracker),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
            BoldTextWithKeywords(
                fullText = stringResource(R.string.asset_done_title2),
                keywords = arrayListOf("보관함", "다시 제작"),
                brushFlag = arrayListOf(true, true),
                boldStyle = Typography.titleMedium,
                normalStyle = Typography.labelLarge.copy(fontSize = 24.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Text(
                text = stringResource(R.string.asset_done_script),
                color = Gray02,
                style = Typography.labelLarge.copy(fontSize = 18.sp),
            )
            Spacer(modifier = Modifier.height(13.dp))
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight(0.3f),
                painter = painterResource(id = R.drawable.img_robot),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.43f))
            LongBlackButton(icon = null, text = stringResource(R.string.save_asset))
            Spacer(modifier = Modifier.height(13.dp))
            LongWhiteButton(icon = null, text = stringResource(R.string.remake_asset))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AssetDoneScreenPreview() {
    AssetDoneScreen()
}
