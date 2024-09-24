package com.semonemo.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.ProgressGreen
import com.semonemo.presentation.theme.ProgressRed
import com.semonemo.presentation.theme.ProgressYellow
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun CustomAuctionProgressBar(
    modifier: Modifier = Modifier,
    initialTime: Float = 15f, // 초기 시간 설정
    currentTimeMillis: () -> Long = { System.currentTimeMillis() }, // 시스템 시간을 가져오는 함수
) {
    val startTime = remember { currentTimeMillis() } // 시작 시간
    val endTime = remember { startTime + (initialTime * 1000).toLong() } // 종료 시간
    val timeLeftFlow = remember { MutableStateFlow(initialTime) }
    var timeLeft by remember { mutableFloatStateOf(initialTime) }

    val progress = timeLeft / initialTime // 진행률

    val progressColor =
        when {
            progress > 0.5f -> lerp(ProgressGreen, ProgressYellow, (1 - progress) * 2)
            else -> lerp(ProgressYellow, ProgressRed, (0.5f - progress) * 2)
        }

    LaunchedEffect(Unit) {
        launch {
            timeLeftFlow.collectLatest { newTimeLeft ->
                timeLeft = newTimeLeft
            }
        }
    }

    // 시간 업데이트 로직, 시스템 시간을 기반으로 실시간 업데이트
    LaunchedEffect(Unit) {
        launch {
            while (timeLeft > 0) {
                val currentTime = currentTimeMillis()
                val updatedTime = max(0f, (endTime - currentTime) / 1000f)
                timeLeftFlow.emit(updatedTime)
                delay(100L)
            }
        }
    }

    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier =
            modifier
                .fillMaxWidth()
                .height(30.dp),
    ) {
        // 커스텀 프로그레스바
        Canvas(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(30.dp),
        ) {
            drawRoundRect(
                color = WhiteGray,
                size = size,
                cornerRadius = CornerRadius(8.dp.toPx()),
            )
            drawRoundRect(
                color = progressColor, // 남은 진행 바 색상
                size = size.copy(width = size.width * progress),
                cornerRadius = CornerRadius(8.dp.toPx()),
            )
        }

        // 남은 시간 텍스트
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = String.format("%.1fs", timeLeft),
            style =
                Typography.bodyMedium.copy(
                    fontFeatureSettings = "tnum",
                ),
            color = GunMetal,
        )
    }
}

@Composable
@Preview
fun CustomAuctionProgressBarPreview() {
    CustomAuctionProgressBar(modifier = Modifier.padding(16.dp))
}
