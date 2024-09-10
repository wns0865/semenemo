package com.semonemo.presentation.screen.ai_asset

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Main01
import com.semonemo.presentation.ui.theme.Main02

@Composable
fun AiAssetScreen(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = Main01),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))
            Text(text = "내 손으로 직접 만드는", style = Typography.labelLarge.copy(fontSize = 24.sp))
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = "AI 에셋",
                style = Typography.titleLarge.copy(brush = Main02, fontSize = 40.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Row {
                Image(
                    painter = painterResource(id = R.drawable.img_m_artist),
                    contentDescription = null,
                )
                Image(
                    painter = painterResource(id = R.drawable.img_fm_artist),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Text(
                text = "만들고 싶은 에셋을 화면에 그려 보세요!",
                style = Typography.labelLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "단순한 손그림을 AI가 에셋으로 변환해 줘요 (´▽｀)",
                style = Typography.labelLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.35f))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "AI 에셋 제작하러 가기")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
