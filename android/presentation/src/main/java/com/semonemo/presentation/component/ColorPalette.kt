package com.semonemo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.Gray02

/**
 * TODO
 *
 * @param colors : 색상 리스트
 * @param selectedColor : 선택된 색상
 * @param onColorSelected : 색상 선택했을 때
 */
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

/**
 * TODO
 *
 * @param color : 색상
 * @param isSelected : 선택되었을 때
 * @param onClick : 클릭했을 때
 */
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
