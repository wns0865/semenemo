package com.semonemo.presentation.screen.ai_asset

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Gray01
import com.semonemo.presentation.ui.theme.Gray02
import com.semonemo.presentation.ui.theme.GunMetal
import com.semonemo.presentation.ui.theme.Main02
import com.semonemo.presentation.ui.theme.White

@Composable
fun DrawAssetScreen(modifier: Modifier = Modifier) {
    var point by remember { mutableStateOf(Offset.Zero) } // point 위치 추척 state
    val points = remember { mutableListOf<Offset>() } // 새로 그려지는 path 표시하기 위한 points State

    var path by remember { mutableStateOf(Path()) } // 새로 그려지고 있는 중인 획 state
    val paths = remember { mutableStateListOf<Path>() } // 다 그려진 획 리스트

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

    Box(
        modifier =
            Modifier
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
                text = "캔버스 위에 원하는 그림을 그려 보세요",
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
                                onDrag = { _, dragAMount ->
                                    point += dragAMount
                                    points.add(point)

                                    // onDrag 호출될 때마다 현재 그리는 획을 새로 보여줌
                                    path = Path()
                                    points.forEachIndexed { index, point ->
                                        if (index == 0) {
                                            path.moveTo(point.x, point.y)
                                        } else {
                                            path.lineTo(point.x, point.y)
                                        }
                                    }
                                },
                                onDragEnd = {
                                    paths.add(path)
                                    points.clear()
                                },
                            )
                        },
            ) {
                // 이미 완성된 획들
                paths.forEach { path ->
                    drawPath(
                        path = path,
                        color = Color.Black,
                        style = Stroke(width = 2f),
                    )
                }

                // 현재 그려지고 있는 획
                drawPath(
                    path = path,
                    color = Color.Black,
                    style = Stroke(width = 2f),
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
                    onColorSelected = { selectedColor = it },
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
                    onSizeSelected = { selectedSize = it },
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
            LongBlackButton(icon = null, text = "이대로 제작할래요")
        }
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
