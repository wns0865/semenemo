package com.semonemo.presentation.screen.store

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongUnableButton
import com.semonemo.presentation.component.PriceTextField
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.addFocusCleaner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetSaleScreen() {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var price by remember { mutableStateOf("") } // 판매가
    var showBottomSheet by remember { mutableStateOf(false) } // bottomSheet 보임 여부
    var selectedIndex by remember { mutableIntStateOf(0) } // bottomSheet 선택된 탭 index
    var selectedAsset by remember { mutableIntStateOf(-1) } // 선택된 에셋

    // 더미 데이터
    val tags = listOf("병아리", "삐약이")

    val assets =
        listOf(
            R.drawable.img_example,
            R.drawable.img_example2,
            R.drawable.img_example3,
            R.drawable.img_example,
            R.drawable.img_example2,
            R.drawable.img_example3,
        )

    Surface(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .verticalScroll(state = scrollState),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 15.dp)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .addFocusCleaner(
                        focusManager = focusManager,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "에셋 판매",
                style = Typography.bodyMedium.copy(fontSize = 20.sp),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Surface(
                modifier =
                    Modifier
                        .width(180.dp)
                        .height(240.dp),
                border = BorderStroke(width = 2.dp, color = Gray01),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    showBottomSheet = true
                },
            ) {
                if (selectedAsset != -1) {
                    Image(
                        painter = painterResource(id = selectedAsset),
                        contentDescription = "img_example",
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_frame_plus),
                            contentDescription = "ic_frame_plus",
                            tint = Color.Unspecified,
                        )
                        Spacer(modifier = Modifier.height(9.dp))
                        Text(
                            text = "에셋을 추가해 주세요",
                            style = Typography.bodySmall.copy(fontSize = 15.sp),
                            color = Gray02,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            // 통신 성공인 경우
            AnimatedVisibility(
                visible = selectedAsset != -1,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = stringResource(R.string.register_tag),
                            style = Typography.titleMedium.copy(fontSize = 16.sp),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        content = {
                            items(tags.size) { index ->
                                HashTag(
                                    keyword = tags[index],
                                )
                            }
                        },
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = stringResource(R.string.register_price),
                    style = Typography.titleMedium.copy(fontSize = 16.sp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            PriceTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                price = price,
                onPriceChange = { newPrice ->
                    price = newPrice
                },
            )
            // 프레임 불러오기 Success면 LongBlackButton
            // 다른 상태면 LongUnableButton
            Spacer(modifier = Modifier.height(30.dp))
            if (selectedAsset != -1) {
                LongBlackButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    text = stringResource(R.string.register_btn_title),
                    icon = null,
                    onClick = { },
                )
            } else {
                LongUnableButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    text = stringResource(R.string.register_btn_title),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.45f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LazyVerticalGrid(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 20.dp, vertical = 20.dp),
                        columns = GridCells.Fixed(4),
                        state = rememberLazyGridState(),
                    ) {
                        items(assets.size) { index ->
                            Image(
                                painter = painterResource(id = assets[index]),
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
                                        ).clickable {
                                            selectedAsset = assets[index]
                                            showBottomSheet = false
                                        },
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview
@Composable
fun AssetSaleScreenPreview() {
    SemonemoTheme {
        AssetSaleScreen()
    }
}
