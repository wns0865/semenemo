package com.semonemo.presentation.screen.frame

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BrushPalette
import com.semonemo.presentation.component.ColorPalette
import com.semonemo.presentation.component.CustomTab
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.screen.aiAsset.AssetButtonList
import com.semonemo.presentation.theme.BlackGradient
import com.semonemo.presentation.theme.BlueGradient
import com.semonemo.presentation.theme.FrameBlue
import com.semonemo.presentation.theme.FrameOrange
import com.semonemo.presentation.theme.FramePink
import com.semonemo.presentation.theme.FramePurple
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.GreenGradient
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.PinkGradient
import com.semonemo.presentation.theme.PurpleGradient
import com.semonemo.presentation.theme.Rainbow
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.theme.WhiteGray
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch

enum class FrameType {
    OneByOne,
    TwoByTwo,
    OneByFour,
}

data class OverlayAsset(
    val resourceId: Int,
    var scale: Float = 1f,
    var offsetX: Float = 0f,
    var offsetY: Float = 0f,
    val rotation: Float = 0f,
)

@Composable
fun FrameRoute(
    modifier: Modifier = Modifier,
    navigateToFrameDone: () -> Unit = {},
    viewModel: FrameViewModel = hiltViewModel(),
    onErrorSnackBar: (String) -> Unit = {},
) {
    FrameScreen(
        modifier = modifier,
        navigateToFrameDone = navigateToFrameDone,
        updateFrame = viewModel::updateFrame,
        onErrorSnackBar = onErrorSnackBar,
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
fun FrameScreen(
    modifier: Modifier = Modifier,
    navigateToFrameDone: () -> Unit = {},
    updateFrame: (Bitmap) -> Unit = {},
    onErrorSnackBar: (String) -> Unit = {},
) {
    val captureController = rememberCaptureController()
    val scope = rememberCoroutineScope()
    val tabs = listOf("사이즈", "배경색", "에셋")
    val sizes = listOf("1x1", "1x4", "2x2")
    val colors =
        listOf(
            Color.Black,
            FramePink,
            FrameOrange,
            FrameBlue,
            FramePurple,
            Gray01,
        )
    val brushes =
        listOf(
            BlackGradient,
            PinkGradient,
            GreenGradient,
            PurpleGradient,
            BlueGradient,
            Rainbow,
        )
    val assets =
        listOf(
            R.drawable.asset_example,
            R.drawable.asset_example2,
            R.drawable.asset_example3,
            R.drawable.asset_example,
            R.drawable.asset_example2,
            R.drawable.asset_example3,
        )

    var frameType by remember { mutableStateOf(FrameType.OneByOne) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var selectedBtn by remember { mutableStateOf("1x1") }
    var selectedColor by remember { mutableStateOf<Color?>(Color.Black) }
    var selectedBrush by remember { mutableStateOf<Brush?>(null) }
    val overlayAssets = remember { mutableStateListOf<OverlayAsset>() }

    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = Color.White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            Text(
                text = "프레임은 아래와 같이 제작돼요!",
                style = Typography.labelMedium.copy(fontSize = 16.sp),
                color = GunMetal,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f)
                            .capturable(captureController),
                    contentAlignment = Alignment.Center,
                ) {
                    FramePreview(
                        frameType = frameType,
                        overlayAssets = overlayAssets,
                        backgroundColor = selectedColor,
                        backgroundBrush = selectedBrush,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            CustomTab(
                modifier = Modifier.fillMaxWidth(),
                tabList = tabs,
                selectedIndex = selectedIndex,
                onTabSelected = {
                    selectedIndex = it
                },
            )
            when (selectedIndex) {
                0 -> {
                    Box(
                        modifier =
                            Modifier
                                .wrapContentSize()
                                .padding(top = 25.dp),
                    ) {
                        AssetButtonList(
                            titles = sizes,
                            selectedBtn = selectedBtn,
                            onBtnSelected = {
                                selectedBtn = it
                                when (it) {
                                    "1x1" -> frameType = FrameType.OneByOne
                                    "1x4" -> frameType = FrameType.OneByFour
                                    "2x2" -> frameType = FrameType.TwoByTwo
                                }
                            },
                        )
                    }
                }

                1 -> {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Spacer(modifier = Modifier.fillMaxHeight(0.06f))
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = "단색",
                            style = Typography.bodySmall.copy(fontSize = 15.sp),
                        )
                        Spacer(modifier = Modifier.fillMaxHeight(0.03f))
                        ColorPalette(
                            colors = colors,
                            circleSize = 45,
                            selectedColor = selectedColor,
                            onColorSelected = {
                                selectedColor = it
                                selectedBrush = null
                            },
                        )
                        Spacer(modifier = Modifier.fillMaxHeight(0.13f))
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = "그라데이션",
                            style = Typography.bodySmall.copy(fontSize = 15.sp),
                        )
                        Spacer(modifier = Modifier.fillMaxHeight(0.03f))
                        BrushPalette(
                            brushes = brushes,
                            circleSize = 45,
                            selectedBrush = selectedBrush,
                            onBrushSelected = {
                                selectedBrush = it
                                selectedColor = null
                            },
                        )
                    }
                }

                2 -> {
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
                                        .background(color = WhiteGray)
                                        .clickable {
                                            overlayAssets.add(OverlayAsset(resourceId = assets[index]))
                                        },
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            LongBlackButton(
                icon = null,
                text = "완성했어요!",
                onClick = {
                    scope.launch {
                        val bitmapAsync = captureController.captureAsync()
                        try {
                            val bitmap = bitmapAsync.await().asAndroidBitmap()
                            updateFrame(bitmap)
                        } catch (error: Throwable) {
                            onErrorSnackBar(error.message?:"")
                        }
                    }
                    navigateToFrameDone()
                },
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun FramePreview(
    modifier: Modifier = Modifier,
    frameType: FrameType,
    overlayAssets: List<OverlayAsset>,
    backgroundColor: Color? = null,
    backgroundBrush: Brush? = null,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var parentSize by remember { mutableStateOf(IntSize.Zero) } // 부모 박스 크기
    var contentSize by remember { mutableStateOf(IntSize.Zero) } // 프레임 크기

    val transformableState =
        rememberTransformableState { zoomChange, offsetChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 3f)
            val newOffsetX = offsetX + offsetChange.x
            val newOffsetY = offsetY + offsetChange.y

            // 부모 박스와 프레임의 크기를 고려하여 이동 제한
            val maxOffsetX =
                if (contentSize.width * scale > parentSize.width) {
                    (contentSize.width * scale - parentSize.width) / 2
                } else {
                    0f // 부모 박스가 더 크면 이동할 필요가 없으므로 0으로 제한
                }

            val maxOffsetY =
                if (contentSize.height * scale > parentSize.height) {
                    (contentSize.height * scale - parentSize.height) / 2
                } else {
                    0f // 부모 박스가 더 크면 이동할 필요가 없으므로 0으로 제한
                }

            offsetX = newOffsetX.coerceIn(-maxOffsetX, maxOffsetX)
            offsetY = newOffsetY.coerceIn(-maxOffsetY, maxOffsetY)
        }

    val backgroundModifier =
        when {
            backgroundBrush != null -> Modifier.background(brush = backgroundBrush)
            backgroundColor != null -> Modifier.background(color = backgroundColor)
            else -> Modifier
        }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clipToBounds()
                .onSizeChanged { parentSize = it } // 부모 박스의 크기를 저장
                .transformable(
                    state = transformableState,
                ).graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY,
                ),
        contentAlignment = Alignment.Center,
    ) {
        when (frameType) {
            FrameType.OneByOne -> {
                // 1x1 그리드 레이아웃
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight()
                            .then(backgroundModifier)
                            .onSizeChanged { contentSize = it },
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(start = 5.dp, top = 5.dp, end = 5.dp, bottom = 50.dp)
                                .background(color = White),
                    )
                }
            }

            FrameType.TwoByTwo -> {
                // 2x2 그리드 레이아웃
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight()
                            .then(backgroundModifier)
                            .onSizeChanged { contentSize = it },
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Row(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(horizontal = 5.dp)
                                    .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .weight(1f)
                                        .background(color = White),
                            )
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .weight(1f)
                                        .background(color = White),
                            )
                        }
                        Row(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(horizontal = 5.dp)
                                    .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .weight(1f)
                                        .background(color = White),
                            )
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .weight(1f)
                                        .background(color = White),
                            )
                        }
                        Spacer(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                        )
                    }
                }
            }

            FrameType.OneByFour -> {
                // 1x4 그리드 레이아웃
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.25f)
                            .fillMaxHeight()
                            .then(backgroundModifier)
                            .onSizeChanged { contentSize = it },
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                    ) {
                        repeat(4) {
                            Box(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .background(color = White),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
        overlayAssets.forEach { asset ->
            var imageScale by remember { mutableFloatStateOf(0.5f) }
            var imageOffsetX by remember { mutableFloatStateOf(0f) }
            var imageOffsetY by remember { mutableFloatStateOf(0f) }
            var imageRotation by remember { mutableFloatStateOf(0f) }
            var assetSize by remember { mutableStateOf(IntSize.Zero) }

            val imageTransformableState =
                rememberTransformableState { zoomChange, offsetChange, rotationChange ->
                    imageScale = (imageScale * zoomChange).coerceIn(0.25f, 1.5f)
                    imageRotation += rotationChange

                    // 이미지 이동 제한 로직
                    val newOffsetX = imageOffsetX + offsetChange.x * imageScale
                    val newOffsetY = imageOffsetY + offsetChange.y * imageScale

                    val maxOffsetX = (contentSize.width + 250 - assetSize.width * imageScale) / 2
                    val maxOffsetY = (parentSize.height + 200 - assetSize.height * imageScale) / 2

                    // 최소값과 최대값을 안전하게 계산
                    val minX = -maxOffsetX.coerceAtLeast(0f)
                    val maxX = maxOffsetX.coerceAtLeast(0f)
                    val minY = -maxOffsetY.coerceAtLeast(0f)
                    val maxY = maxOffsetY.coerceAtLeast(0f)

                    imageOffsetX = newOffsetX.coerceIn(minX, maxX)
                    imageOffsetY = newOffsetY.coerceIn(minY, maxY)
                }

            val assetModifier =
                Modifier
                    .graphicsLayer(
                        scaleX = imageScale,
                        scaleY = imageScale,
                        translationX = imageOffsetX,
                        translationY = imageOffsetY,
                        rotationZ = imageRotation,
                    ).onSizeChanged { assetSize = it }
                    .transformable(
                        state = imageTransformableState,
                    )
            Image(
                modifier = assetModifier.wrapContentSize(),
                painter = painterResource(id = asset.resourceId),
                contentDescription = null,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FrameScreenPreview() {
    SemonemoTheme {
        FrameScreen()
    }
}
