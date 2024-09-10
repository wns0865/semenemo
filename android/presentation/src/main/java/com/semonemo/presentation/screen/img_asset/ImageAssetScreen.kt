package com.semonemo.presentation.screen.img_asset

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Main01
import com.semonemo.presentation.ui.theme.Main02

@Composable
fun ImageAssetScreen(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Log.d("nakyung", "호출호출")
        LoadingDialog(
            lottieRes = R.raw.normal_load,
            loadingMessage = "AI가 열심히 에셋을 제작하고 있어요...",
            subMessage = "조금만 기다려 주세요  (。＾▽＾)",
        )
    } else {
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
                Text(text = "갤러리 속 사진으로 만드는", style = Typography.labelLarge.copy(fontSize = 24.sp))
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = "이미지 에셋",
                    style = Typography.titleLarge.copy(brush = Main02, fontSize = 40.sp),
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.img_framed_picture),
                        contentDescription = null,
                    )
                    Image(
                        painter = painterResource(id = R.drawable.img_clapping_hands),
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.18f))
                Text(
                    text = "스티커로 만들고 싶은 이미지를 고르면",
                    style = Typography.labelLarge.copy(fontSize = 20.sp),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "AI가 배경을 제거해 에셋으로 만들어 줘요!",
                    style = Typography.labelLarge.copy(fontSize = 20.sp),
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.3f))
                Button(
                    onClick = {
                        showDialog = true
                    },
                ) {
                    Text(text = "앨범에서 이미지 선택하기")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
fun ImageAssetPreview() {
    SemonemoTheme {
        ImageAssetScreen()
    }
}
