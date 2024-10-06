package com.semonemo.presentation.screen.picture.select

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.presentation.R
import com.semonemo.presentation.component.ColorPalette
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.frame.FrameType
import com.semonemo.presentation.screen.picture.PictureUiEvent
import com.semonemo.presentation.screen.picture.PictureUiState
import com.semonemo.presentation.screen.picture.camera.CameraViewModel
import com.semonemo.presentation.screen.picture.select.subscreen.CreatePictureButton
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.frameBackGroundColor
import com.semonemo.presentation.util.getTodayDate
import com.semonemo.presentation.util.noRippleClickable
import com.semonemo.presentation.util.saveBitmapToGallery
import com.semonemo.presentation.util.urlToIpfs
import com.skydoves.landscapist.glide.GlideImage
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun PictureSelectRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit = {},
    actionWithSnackBar: (Uri) -> Unit = {},
    type: Int,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    PictureSelectContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        uiState = uiState.value,
        uiEvent = viewModel.uiEvent,
        onShowSnackBar = onShowSnackBar,
        loadMyFrame = viewModel::loadAvailableFrames,
        type = type,
        actionWithSnackBar = actionWithSnackBar,
    )

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetUiState()
        }
    }
}

@Composable
fun PictureSelectContent(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    uiState: PictureUiState,
    uiEvent: SharedFlow<PictureUiEvent>,
    onShowSnackBar: (String) -> Unit,
    loadMyFrame: (Int) -> Unit,
    type: Int,
    actionWithSnackBar: (Uri) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        loadMyFrame(type)
    }
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is PictureUiEvent.Error -> onShowSnackBar(event.errorMessage)
                PictureUiEvent.NavigateToSelect -> {}
            }
        }
    }
    PictureSelectScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        pictures = uiState.bitmaps,
        frames = uiState.frames,
        type = FrameType.fromIdx(type),
        onShowSnackBar = onShowSnackBar,
        actionWithSnackBar = actionWithSnackBar,
    )
    if (uiState.isLoading) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {},
        )
        LoadingDialog(
            lottieRes = R.raw.normal_load,
            loadingMessage = stringResource(R.string.frame_loading_title),
            subMessage = stringResource(R.string.loading_sub_message),
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
fun PictureSelectScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    pictures: List<Bitmap> = listOf(),
    frames: List<MyFrame> = listOf(),
    type: FrameType = FrameType.OneByOne,
    onShowSnackBar: (String) -> Unit = {},
    actionWithSnackBar: (Uri) -> Unit = {},
) {
    val captureController = rememberCaptureController()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var check =
        remember {
            mutableStateOf(false)
        }
    var selectedFrameIndex =
        remember {
            mutableIntStateOf(-1)
        }
    val selectedPictures = remember { mutableStateOf(mutableListOf<Bitmap>()) }
    var selectedColor by remember { mutableStateOf(Color.Black) }
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
            Spacer(modifier = Modifier.height(20.dp))
            TopAppBar(modifier = Modifier, onNavigationClick = popUpBackStack, actionButtons = {
                val isEnabled = selectedPictures.value.size == type.amount
                CreatePictureButton(
                    modifier = Modifier,
                    isEnabled = isEnabled,
                    onClick = {
                        if(isEnabled) {
                            scope.launch {
                                val bitmapAsync = captureController.captureAsync()
                                try {
                                    val frame = frames[selectedFrameIndex.value]
                                    val bitmap = bitmapAsync.await().asAndroidBitmap()
                                    saveBitmapToGallery(
                                        context = context,
                                        bitmap = bitmap,
                                        nickname = frame.owner.nickname,
                                        frameTitle = frame.nftInfo.data.title,
                                        onSuccess = {
                                            actionWithSnackBar(it)
                                            popUpBackStack()
                                        },
                                    )
                                } catch (error: Throwable) {
                                    onShowSnackBar(error.message ?: "")
                                }
                            }
                        } else {
                            onShowSnackBar("사진을 선택해주세요!")
                        }
                    },
                )
            })
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.4f)
                        .capturable(captureController),
                contentAlignment = Alignment.Center,
            ) {
                if (selectedFrameIndex.value == -1) {
                    Text(
                        text = "프레임을 선택해주세요! 📸",
                        style = Typography.bodyMedium,
                        color = Gray02,
                    )
                    selectedPictures.value.clear()
                } else {
                    GlideImage(
                        imageModel =
                            frames[selectedFrameIndex.value]
                                .nftInfo.data.image
                                .urlToIpfs(),
                        contentScale = ContentScale.Fit,
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .noRippleClickable {
                                },
                    )
                    if (check.value) {
                        val (textStyle, alignment) =
                            if (type == FrameType.OneByFour) {
                                Pair(
                                    Typography.bodySmall.copy(color = selectedColor),
                                    Alignment.BottomCenter,
                                )
                            } else {
                                Pair(
                                    Typography.bodyMedium.copy(color = selectedColor),
                                    Alignment.BottomEnd,
                                )
                            }

                        Text(
                            modifier =
                                Modifier
                                    .wrapContentHeight()
                                    .align(alignment)
                                    .padding(7.dp),
                            text = getTodayDate(),
                            style = textStyle,
                            textAlign = TextAlign.End,
                        )
                    }
                    when (type) {
                        FrameType.OneByOne -> {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(top = 5.dp, bottom = 50.dp),
                            ) {
                                selectedPictures.value.forEach { bitmap ->
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier =
                                            Modifier
                                                .fillMaxSize()
                                                .align(Alignment.Center),
                                    )
                                }
                            }
                        }

                        FrameType.TwoByTwo -> {
                            Box(
                                modifier =
                                    Modifier
                                        .wrapContentSize(),
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxSize(),
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        RenderImageRow(
                                            modifier = Modifier.weight(1f),
                                            startIndex = 0,
                                            selectedPictures = selectedPictures.value,
                                        )

                                        RenderImageRow(
                                            modifier = Modifier.weight(1f),
                                            startIndex = 2,
                                            selectedPictures = selectedPictures.value,
                                        )
                                        Spacer(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(50.dp),
                                        )
                                    }
                                }
                            }
                        }

                        FrameType.OneByFour -> {
                            Box(
                                modifier =
                                    Modifier
                                        .wrapContentSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxSize(),
                                ) {
                                    Column(
                                        modifier =
                                            Modifier
                                                .fillMaxSize(),
                                    ) {
                                        for (i in 0 until FrameType.OneByFour.amount) {
                                            RenderImage(
                                                modifier = Modifier.fillMaxWidth().weight(1f),
                                                index = i,
                                                selectedPictures = selectedPictures.value,
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(40.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                modifier =
                    modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                content = {
                    items(count = pictures.size) { index ->
                        val isSelected = selectedPictures.value.contains(pictures[index])
                        Image(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape((10.dp)))
                                    .width(70.dp)
                                    .height(70.dp)
                                    .graphicsLayer(alpha = if (isSelected) 0.5f else 1f)
                                    .noRippleClickable {
                                        selectedPictures.value =
                                            selectedPictures.value
                                                .toMutableList()
                                                .apply {
                                                    if (isSelected) {
                                                        remove(pictures[index])
                                                    } else {
                                                        add(pictures[index])
                                                    }
                                                }
                                    },
                            bitmap = pictures[index].asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = GunMetal,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = check.value,
                    onCheckedChange = { check.value = !check.value },
                )
                Text(text = stringResource(R.string.picture_date), style = Typography.bodySmall)
                Spacer(modifier = Modifier.width(20.dp))
                ColorPalette(
                    colors = frameBackGroundColor,
                    circleSize = 25,
                    selectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                    },
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp)
                        .align(Alignment.Start),
                text = stringResource(R.string.my_frame),
                style = Typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                modifier =
                    modifier
                        .align(Alignment.Start)
                        .fillMaxHeight(1f)
                        .padding(start = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                content = {
                    items(frames.size) { index ->
                        val frame = frames[index]
                        val imgUrl =
                            frame.nftInfo.data.image
                                .urlToIpfs()
                        GlideImage(
                            imageModel = imgUrl.toUri(),
                            contentScale = ContentScale.Fit,
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .height(200.dp)
                                    .aspectRatio(1f)
                                    .noRippleClickable {
                                        if (selectedFrameIndex.value == index) {
                                            selectedFrameIndex.value = -1
                                        } else {
                                            selectedFrameIndex.value = index
                                        }
                                    },
                        )
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PictureSelectScreenPreview() {
    SemonemoTheme {
        PictureSelectScreen()
    }
}
