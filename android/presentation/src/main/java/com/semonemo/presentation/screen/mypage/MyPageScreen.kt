package com.semonemo.presentation.screen.mypage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomDropdownMenu
import com.semonemo.presentation.component.CustomDropdownMenuStyles
import com.semonemo.presentation.component.CustomTab
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.NameWithBadge
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.component.TopAppBarNavigationType
import com.semonemo.presentation.theme.Blue3
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable
import com.semonemo.presentation.util.toAbsolutePath
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@Composable
fun MyPageRoute(
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
    viewModel: MyPageViewModel = hiltViewModel(),
    onErrorSnackBar: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    HandleMyPageEvent(
        uiEvent = viewModel.uiEvent,
        onErrorSnackBar = onErrorSnackBar,
    )
    HandleMyPageUi(
        modifier = modifier,
        uiState = uiState,
        navigateToDetail = navigateToDetail,
        updateProfileImage = { imageUri ->
            val image = File(imageUri.toAbsolutePath(context))
            viewModel.updateProfileImage(image, imageUri.toString())
        },
    )
}

@Composable
fun HandleMyPageEvent(
    uiEvent: SharedFlow<MyPageUiEvent>,
    onErrorSnackBar: (String) -> Unit,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is MyPageUiEvent.Error -> onErrorSnackBar(event.errorMessage)
                MyPageUiEvent.Subscribe -> {}
            }
        }
    }
}

@Composable
fun HandleMyPageUi(
    modifier: Modifier = Modifier,
    uiState: MyPageUiState,
    navigateToDetail: (String) -> Unit,
    updateProfileImage: (Uri) -> Unit,
) {
    when (uiState) {
        MyPageUiState.Loading -> LoadingDialog()
        is MyPageUiState.Success ->
            MyPageScreen(
                modifier = modifier,
                navigateToDetail = navigateToDetail,
                nickname = uiState.nickname,
                profileImageUrl = uiState.profileImageUrl,
                amount = uiState.amount,
                volume = uiState.volume,
                follower = uiState.follower.size,
                following = uiState.following.size,
                updateProfileImage = updateProfileImage,
            )
    }
}

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit = {},
    nickname: String = "짜이한",
    profileImageUrl: String = "",
    amount: Int = 0,
    volume: Int = 0,
    follower: Int = 0,
    following: Int = 0,
    updateProfileImage: (Uri) -> Unit = {},
) {
    val tabs = listOf("내 프레임", "에셋", "찜")
    val selectedIndex = remember { mutableIntStateOf(0) }
    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    updateProfileImage(it)
                }
            },
        )
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

    Surface(
        modifier =
            modifier
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
            TopAppBar(
                modifier = Modifier,
                title = {
                    NameWithBadge(
                        name = nickname,
                        size = 18,
                    )
                },
                navigationType = TopAppBarNavigationType.None,
                actionButtons = {
                    Icon(
                        modifier = Modifier.size(30.dp).noRippleClickable {  },
                        painter = painterResource(id = R.drawable.ic_setting),
                        contentDescription = "",
                    )
                },
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.04f))
            Box {
                GlideImage(
                    imageModel = profileImageUrl.toUri(),
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .size(120.dp)
                            .clip(shape = CircleShape),
                )
                Image(
                    modifier =
                        Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 5.dp, bottom = 10.dp)
                            .background(Blue3, shape = CircleShape)
                            .size(30.dp)
                            .noRippleClickable {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly,
                                    ),
                                )
                            },
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
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
                        text = "$amount",
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
                        text = "$volume",
                        style = Typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "거래량",
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
                        text = "$follower",
                        style = Typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "팔로워",
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
                        text = "$following",
                        style = Typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "팔로잉",
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
                when (targetIndex) {
                    0 -> {
                        // 기본 보유중 프레임 불러오기
                        images.clear()
                        images.addAll(frames)

                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            CustomDropdownMenu(
                                menuItems =
                                    listOf(
                                        "보유중" to {
                                            // 통신 (보유 중인 프레임 불러 오기)
                                            images.clear()
                                            images.addAll(frames)
                                        },
                                        "판매중" to {
                                            // 통신 (판매 중인 프레임 불러 오기)
                                            images.clear()
                                            images.addAll(frames2)
                                        },
                                        "경매중" to {
                                            // 통신 (경매 중인 프레임 불러 오기)
                                            images.clear()
                                            images.addAll(frames)
                                        },
                                    ),
                                styles =
                                    CustomDropdownMenuStyles(),
                            )
                        }
                    }

                    1 -> {
                        // 에셋 불러오기
                        images.clear()
                        images.addAll(frames)

                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            CustomDropdownMenu(
                                menuItems =
                                    listOf(
                                        "보유중" to {
                                            // 통신 (보유 중인 에셋 불러 오기)
                                            images.clear()
                                            images.addAll(frames)
                                        },
                                        "판매중" to {
                                            // 통신 (판매 중인 에셋 불러 오기)
                                            images.clear()
                                            images.addAll(frames2)
                                        },
                                    ),
                                styles =
                                    CustomDropdownMenuStyles(),
                            )
                        }
                    }

                    2 -> {
                        // 에셋 불러오기
                        images.clear()
                        images.addAll(frames)

                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            CustomDropdownMenu(
                                menuItems =
                                    listOf(
                                        "판매중" to {
                                            // 통신 (판매 중인 찜한 프레임 불러 오기)
                                            images.clear()
                                            images.addAll(frames)
                                        },
                                        "경매중" to {
                                            // 통신 (경매 중인 찜한 프레임 불러 오기)
                                            images.clear()
                                            images.addAll(frames2)
                                        },
                                        "에셋" to {
                                            // 통신 (찜한 에셋 불러 오기)
                                            images.clear()
                                            images.addAll(assets)
                                        },
                                    ),
                                styles =
                                    CustomDropdownMenuStyles(),
                            )
                        }
                    }
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
                                ).noRippleClickable {
                                    navigateToDetail(images[index].toString())
                                },
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MyPageScreenPreview() {
    SemonemoTheme {
        MyPageScreen()
    }
}
