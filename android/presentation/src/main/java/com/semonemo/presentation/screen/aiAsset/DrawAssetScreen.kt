package com.semonemo.presentation.screen.aiAsset

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.ColorCircle
import com.semonemo.presentation.component.ColorPalette
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.PenCircle
import com.semonemo.presentation.component.PenPalette
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Main02
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White

// 펜 스타일 데이터 클래스
data class PathStyle(
    var color: Color = Color.Black,
    var width: Float = 4.dp.value,
)

// PathStyle 넘겨 주면 매핑해 주는 함수 선언
internal fun DrawScope.drawPath(
    path: Path,
    style: PathStyle,
) {
    drawPath(
        path = path,
        color = style.color,
        style = Stroke(width = style.width),
    )
}

@Composable
fun DrawAssetScreen(
    modifier: Modifier = Modifier,
    navigateToDone: (String) -> Unit = {},
) {
    // 색상 팔레트
    val colors =
        listOf(
            Color.Black,
            Color.White,
            Color.Red,
            Color.Yellow,
            Color.Blue,
            Color.Magenta,
        )
    var selectedColor by remember { mutableStateOf(Color.Black) }

    // 펜 굵기 팔레트
    val sizes =
        listOf(
            4.dp,
            7.dp,
            10.dp,
            13.dp,
        )
    var selectedSize by remember { mutableStateOf(4.dp) }

    val titles =
        listOf(
            "픽셀",
            "실사",
            "애니메이션",
        )
    var selectedBtn by remember { mutableStateOf("픽셀") }

    var point by remember { mutableStateOf(Offset.Zero) } // point 위치 추척 state
    val points = remember { mutableListOf<Offset>() } // 새로 그려지는 path 표시하기 위한 points State

    var path by remember { mutableStateOf(Path()) }
    val paths = remember { mutableStateListOf<Pair<Path, PathStyle>>() } // 다 그려진 획 리스트
    val removedPaths = remember { mutableStateListOf<Pair<Path, PathStyle>>() } // undo, redo 위한 리스트
    val pathStyle by remember { mutableStateOf(PathStyle()) }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = stringResource(R.string.canvas_script),
                style = Typography.labelMedium,
                color = Gray02,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Canvas(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.48f)
                        .background(color = White)
                        .padding(horizontal = 17.dp)
                        .border(width = 1.dp, color = Gray02, shape = RoundedCornerShape(15.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    point = offset
                                    points.add(point)
                                },
                                onDrag = { _, dragAmount ->
                                    val newPoint = point + dragAmount // 새로운 포인트 위치 계산
                                    val canvasBounds = size // 범위 내인지 확인

                                    if (newPoint.x in 0f..canvasBounds.width.toFloat() && newPoint.y in 0f..canvasBounds.height.toFloat()) {
                                        point = newPoint
                                        points.add(point)

                                        path = Path()

                                        points.forEachIndexed { index, point ->
                                            if (index == 0) {
                                                path.moveTo(point.x, point.y)
                                            } else {
                                                path.lineTo(point.x, point.y)
                                            }
                                        }
                                    }
                                },
                                onDragEnd = {
                                    paths.add(Pair(path, pathStyle.copy()))
                                    points.clear()
                                    path = Path()
                                },
                            )
                        },
            ) {
                // 이미 완성된 획들
                paths.forEach { pair ->
                    drawPath(
                        path = pair.first,
                        style = pair.second,
                    )
                }

                // 현재 그려지고 있는 획
                drawPath(
                    path = path,
                    style = pathStyle,
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                DrawNavigateBar(
                    onUndoClicked = {
                        if (paths.isNotEmpty()) {
                            val lastPath = paths.removeLast()
                            removedPaths.add(lastPath)
                        }
                    },
                    onRedoClicked = {
                        if (removedPaths.isNotEmpty()) {
                            val lastRemovedPath = removedPaths.removeLast()
                            paths.add(lastRemovedPath)
                        }
                    },
                    onClearClicked = {
                        paths.clear()
                        removedPaths.clear()
                    },
                )
            }
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "색상",
                    style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                ColorPalette(
                    colors = colors,
                    selectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                        pathStyle.color = it
                    },
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "펜 굵기",
                    style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                PenPalette(
                    sizes = sizes,
                    selectedSize = selectedSize,
                    onSizeSelected = {
                        selectedSize = it
                        pathStyle.width = it.value
                    },
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "화풍",
                    style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                AssetButtonList(
                    titles = titles,
                    selectedBtn = selectedBtn,
                    onBtnSelected = { selectedBtn = it },
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.3f))
            LongBlackButton(
                icon = null,
                text = stringResource(R.string.draw_done),
                onClick = {
                    // ai랑 통신해서 결과 얻어오고, 완료 화면으로 이동
                    navigateToDone("test")
                },
            )
        }
    }
}

// 뒤로가기, 앞으로가기, 초기화하기
@Composable
fun DrawNavigateBar(
    onUndoClicked: () -> Unit,
    onRedoClicked: () -> Unit,
    onClearClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.img_undo),
            contentDescription = null,
            modifier =
                Modifier
                    .size(30.dp)
                    .clickable { onUndoClicked() },
            tint = Color.Unspecified,
        )
        Icon(
            painter = painterResource(id = R.drawable.img_redo),
            contentDescription = null,
            modifier =
                Modifier
                    .size(30.dp)
                    .clickable { onRedoClicked() },
            tint = Color.Unspecified,
        )
        Icon(
            painter = painterResource(id = R.drawable.img_reset),
            contentDescription = null,
            modifier =
                Modifier
                    .size(30.dp)
                    .clickable { onClearClicked() },
            tint = Color.Unspecified,
        )
    }
}

// 색상 팔레트
@Composable
fun ColorPalette(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        colors.forEach { color ->
            ColorCircle(
                color = color,
                isSelected = color == selectedColor,
                onClick = { onColorSelected(color) },
            )
        }
    }
}

@Composable
fun ColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) Color.Red else Color.Transparent

    Box(
        modifier =
            Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = 1.dp,
                    color =
                        if (color == Color.White && !isSelected) {
                            Gray02
                        } else {
                            borderColor
                        },
                    shape = CircleShape,
                ).clickable { onClick() },
    )
}

// 펜 굵기 팔레트
@Composable
fun PenPalette(
    sizes: List<Dp>,
    selectedSize: Dp,
    onSizeSelected: (Dp) -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            sizes.forEach { size ->
                PenCircle(
                    onClick = { onSizeSelected(size) },
                    isSelected = size == selectedSize,
                    size = size,
                )
            }
        }
    }
}

@Composable
fun PenCircle(
    onClick: () -> Unit,
    isSelected: Boolean,
    size: Dp,
) {
    val borderColor = if (isSelected) Color.Red else Gray02

    Box(
        modifier =
            Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(White)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = CircleShape,
                ).clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(GunMetal),
        )
    }
}

// 화풍 버튼 리스트
@Composable
fun AssetButtonList(
    titles: List<String>,
    selectedBtn: String,
    onBtnSelected: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        titles.forEach { title ->
            AssetButton(
                onClick = { onBtnSelected(title) },
                isSelected = title == selectedBtn,
                title = title,
            )
        }
    }
}

@Composable
fun AssetButton(
    onClick: () -> Unit,
    isSelected: Boolean,
    title: String,
) {
    if (isSelected) {
        Box(
            modifier =
                Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(brush = Main02)
                    .clickable { onClick() },
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                text = title,
                color = White,
                style = Typography.bodySmall.copy(fontSize = 14.sp),
            )
        }
    } else {
        Box(
            modifier =
                Modifier
                    .border(width = 1.dp, color = Gray01, shape = RoundedCornerShape(20.dp))
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = White)
                    .clickable { onClick() },
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                text = title,
                style = Typography.labelMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawAssetPreview() {
    SemonemoTheme {
        DrawAssetScreen()
    }
}
