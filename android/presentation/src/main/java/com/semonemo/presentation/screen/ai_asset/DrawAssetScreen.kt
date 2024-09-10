package com.semonemo.presentation.screen.ai_asset

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.component.ColorPalette
import com.semonemo.presentation.component.PenPalette
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Gray02
import com.semonemo.presentation.ui.theme.GunMetal
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
             13.dp
         )
    var selectedSize by remember { mutableStateOf(4.dp) }

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
                    .navigationBarsPadding()
                    .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "캔버스 위에 원하는 그림을 그려 보세요",
                style = Typography.labelMedium,
                color = Gray02,
            )
            Spacer(modifier = Modifier.height(15.dp))
            Canvas(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .background(color = White)
                        .border(width = 2.dp, color = Gray02, shape = RoundedCornerShape(15.dp))
                        .aspectRatio(1.0f)
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
                modifier = Modifier.fillMaxWidth(),
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
                        .height(35.dp),
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
                modifier = Modifier.fillMaxWidth(),
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
                    .height(35.dp),
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
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "화풍",
                    style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DrawAssetPreview() {
    SemonemoTheme {
        DrawAssetScreen()
    }
}
