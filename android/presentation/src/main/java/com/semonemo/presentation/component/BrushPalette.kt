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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * TODO
 *
 * @param brushes : 브러쉬들 (그라데이션)
 * @param circleSize : 원형 크기
 * @param selectedBrush : 선택된 브러쉬
 * @param onBrushSelected : 브러쉬 선택했을 때
 */
@Composable
fun BrushPalette(
    brushes: List<Brush>,
    circleSize: Int = 30,
    selectedBrush: Brush?,
    onBrushSelected: (Brush) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        brushes.forEach { brush ->
            BrushCircle(
                brush = brush,
                circleSize = circleSize,
                isSelected = brush == selectedBrush,
                onClick = { onBrushSelected(brush) },
            )
        }
    }
}

/**
 * TODO
 *
 * @param brush : 색상
 * @param circleSize : 원형 크기
 * @param isSelected : 선택되었을 때
 * @param onClick : 클릭했을 때
 */
@Composable
fun BrushCircle(
    brush: Brush,
    circleSize: Int = 30,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) Color.Red else Color.Transparent

    Box(
        modifier =
            Modifier
                .size(circleSize.dp)
                .clip(CircleShape)
                .background(brush)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = CircleShape,
                ).clickable { onClick() },
    )
}
