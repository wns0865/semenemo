package com.semonemo.presentation.screen.mypage

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomDropdownMenu
import com.semonemo.presentation.component.CustomDropdownMenuStyles
import com.semonemo.presentation.component.CustomTab
import com.semonemo.presentation.component.NameWithBadge
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Gray03
import com.semonemo.presentation.ui.theme.Main01

@Composable
fun MyPageScreen() {
    val tabs = listOf("내 프레임", "에셋")
    val selectedIndex = remember { mutableIntStateOf(0) }

    val images = remember { mutableStateListOf<Int>() }

    // 더미 이미지 데이터들
    val frames =
        listOf(
            R.drawable.img_example,
            R.drawable.img_example2,
            R.drawable.img_example3,
            R.drawable.img_example,
            R.drawable.img_example2,
            R.drawable.img_example3,
        )
    val frames2 =
        listOf(
            R.drawable.img_example,
            R.drawable.img_example2,
            R.drawable.img_example3,
        )

    val assets =
        listOf(
            R.drawable.img_example3,
            R.drawable.img_example2,
            R.drawable.img_example,
            R.drawable.img_example3,
            R.drawable.img_example2,
            R.drawable.img_example,
            R.drawable.img_example3,
            R.drawable.img_example2,
            R.drawable.img_example,
        )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(brush = Main01),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            NameWithBadge(
                name = "나갱갱",
                size = 18,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.04f))
            Box(
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(shape = CircleShape),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_example),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Spacer(modifier = Modifier.weight(0.4f))
                Column(
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "23",
                        style = Typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "보유중",
                        style = Typography.labelSmall.copy(fontSize = 13.sp),
                    )
                }
                Column(
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "30",
                        style = Typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "구독자",
                        style = Typography.labelSmall.copy(fontSize = 13.sp),
                    )
                }
                Column(
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "50",
                        style = Typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "거래량",
                        style = Typography.labelSmall.copy(fontSize = 13.sp),
                    )
                }
                Spacer(modifier = Modifier.weight(0.4f))
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            CustomTab(
                tabList = tabs,
                selectedIndex = selectedIndex.intValue,
                onTabSelected = { tab -> selectedIndex.intValue = tab },
            )
            Spacer(modifier = Modifier.height(10.dp))
            AnimatedContent(
                targetState = selectedIndex.intValue,
                transitionSpec = {
                    (
                        fadeIn(animationSpec = tween(durationMillis = 300)) +
                            slideInVertically(
                                animationSpec = tween(durationMillis = 300),
                            )
                    ).togetherWith(
                        fadeOut(animationSpec = tween(durationMillis = 300)) +
                            slideOutVertically(
                                animationSpec = tween(durationMillis = 300),
                            ),
                    )
                },
                label = "",
            ) { targetIndex ->
                if (targetIndex == 0) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        // 기본 보유중 프레임 불러오기
                        images.clear()
                        images.addAll(frames)

                        CustomDropdownMenu(
                            menuItems =
                                listOf(
                                    "보유중" to {
                                        // 통신 (보유중인 프레임 불러 오기)
                                        images.clear()
                                        images.addAll(frames)
                                    },
                                    "판매중" to {
                                        // 통신 (판매중인 프레임 불러 오기)
                                        images.clear()
                                        images.addAll(frames2)
                                    },
                                ),
                            styles =
                                CustomDropdownMenuStyles(),
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(8.dp))

                    // 에셋 불러오기
                    images.clear()
                    images.addAll(assets)
                }
            }
            LazyVerticalGrid(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 10.dp),
                columns = GridCells.Fixed(3),
                state = rememberLazyGridState(),
            ) {
                items(images.size) { index ->
                    Image(
                        painter = painterResource(id = images[index]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .padding(8.dp)
                                .clip(shape = RoundedCornerShape(10.dp))
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    color = Gray03,
                                ),
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MyPageScreenPreview() {
    SemonemoTheme {
        MyPageScreen()
    }
}
