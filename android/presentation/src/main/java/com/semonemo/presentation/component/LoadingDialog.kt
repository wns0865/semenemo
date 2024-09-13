package com.semonemo.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoadingDialog(
    modifier: Modifier = Modifier,
    lottieRes: Int = R.raw.normal_load,
    loadingMessage: String = "로딩중입니다...",
    subMessage: String? = null,
) {
    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottieRes))
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieAnimation(
                composition = lottieComposition,
                restartOnPlay = true,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxHeight(0.25f),
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = loadingMessage,
                style = Typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            subMessage?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = subMessage,
                    style = Typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoadingDialogPreview() {
    SemonemoTheme {
        LoadingDialog(
            lottieRes = R.raw.normal_load,
            loadingMessage = "AI가 열심히 에셋을 제작하고 있어요...",
            subMessage = "조금만 기다려 주세요  (。＾▽＾)",
        )
    }
}
