package com.semonemo.presentation.screen.moment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.MomentBox
import com.semonemo.presentation.component.MomentLongBox
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.Main01

@Composable
fun MomentScreen(modifier: Modifier = Modifier) {
    val userName = "나갱갱"

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = Main01),
    ) {
        Column(
            modifier =
                Modifier
                    .wrapContentHeight()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 17.dp, vertical = 30.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .wrapContentSize()
                        .padding(start = 7.dp),
            ) {
                BoldTextWithKeywords(
                    fullText = "$userName 님,\n추억을 나만의 프레임에 담아 보세요",
                    keywords = arrayListOf(userName, "나만의 프레임"),
                    brushFlag = arrayListOf(true, true),
                    boldStyle = Typography.titleSmall.copy(fontSize = 24.sp),
                    normalStyle = Typography.labelLarge.copy(fontSize = 24.sp),
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                MomentBox(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.ai_asset_label),
                    subTitle = stringResource(R.string.make_title),
                    icon = R.drawable.img_robot,
                )
                Spacer(modifier = Modifier.width(10.dp))
                MomentBox(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.img_asset_label),
                    subTitle = stringResource(R.string.make_title),
                    icon = R.drawable.img_disk,
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            MomentLongBox(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.my_frame_label),
                subTitle = stringResource(R.string.frame_label_2),
                icon = R.drawable.img_sparkles,
            )
            Spacer(modifier = Modifier.height(18.dp))
            MomentLongBox(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.picture_label),
                subTitle = stringResource(R.string.picture_label2),
                icon = R.drawable.img_camera,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MomentScreenPreview() {
    SemonemoTheme {
        MomentScreen()
    }
}
