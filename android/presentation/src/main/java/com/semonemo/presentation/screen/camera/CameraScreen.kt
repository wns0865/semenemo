package com.semonemo.presentation.screen.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.picture.subscreen.CircularCountdownTimer
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White

@Composable
fun CameraRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    CameraContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        onShowSnackBar = onShowSnackBar,
    )
}

@Composable
fun CameraContent(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    CameraScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        onShowSnackBar = onShowSnackBar,
    )
}

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
) {
    var selectedIndex by remember { mutableStateOf(0) }
    val options = listOf(3, 10)
    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = GunMetal),
    ) {
        Column(
            modifier =
                Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .background(GunMetal)
                    .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(modifier = Modifier, onNavigationClick = popUpBackStack, iconColor = White)
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.img_fire),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(10.dp))
            SingleChoiceSegmentedButtonRow {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape =
                            SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size,
                            ),
                        onClick = {
                            selectedIndex = index
                        },
                        selected = index == selectedIndex,
                        colors =
                            SegmentedButtonDefaults.colors(
                                inactiveBorderColor = Gray02,
                                inactiveContainerColor = Gray02,
                                activeContainerColor = White,
                                activeBorderColor = White,
                            ),
                        icon = {
                            if (index == selectedIndex) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_auction_clock),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp), // 아이콘 크기 조정
                                )
                            }
                        },
                    ) {
                        Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 텍스트 사이에 간격 추가
                        Text("${label}s", style = Typography.bodyLarge)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.2f))
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.mypage_cancel_tag),
                    style = Typography.bodyMedium.copy(color = White),
                )
                Spacer(modifier = Modifier.weight(1f))
                CircularCountdownTimer(
                    totalTime = options[selectedIndex] * 1000L,
                    countdownColor = White,
                    backgroundColor = Gray01,
                    modifier = Modifier,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .background(shape = CircleShape, color = Gray03)
                            .padding(7.dp),
                    painter = painterResource(id = R.drawable.ic_coin_exchange),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(0.3f))
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CameraScreenPreview() {
    SemonemoTheme {
        CameraScreen(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(GunMetal),
        )
    }
}
