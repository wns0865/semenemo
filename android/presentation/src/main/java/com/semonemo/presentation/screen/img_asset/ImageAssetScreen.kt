package com.semonemo.presentation.screen.img_asset

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Main01
import com.semonemo.presentation.ui.theme.Main02

@Composable
fun ImageAssetScreen(modifier: Modifier = Modifier) {
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
            Text(
                text = stringResource(R.string.img_asset_title1),
                style = Typography.labelLarge.copy(fontSize = 24.sp),
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = stringResource(R.string.img_asset_title2),
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
                text = stringResource(R.string.img_asset_script1),
                style = Typography.labelLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.img_asset_script2),
                style = Typography.labelLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.3f))
            LongWhiteButton(
                icon = null,
                text = stringResource(R.string.img_asset_btn_title),
            )
            Spacer(modifier = Modifier.weight(1f))
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
