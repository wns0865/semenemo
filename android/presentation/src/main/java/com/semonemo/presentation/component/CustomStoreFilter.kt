package com.semonemo.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.theme.WhiteGray

@Composable
fun FilterButton(
    text: String,
    state: Boolean?, // 필터 상태: null, true, false
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 필터 상태에 따른 색상 설정
    val backgroundColor =
        when (state) {
            null -> WhiteGray
            else -> GunMetal
        }

    val contentColor =
        when (state) {
            null -> GunMetal
            else -> White
        }

    // 뒤집히는 애니메이션을 위한 X 축 스케일 값
    val flipScaleY by animateFloatAsState(
        targetValue = if (state == false) -1f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "",
    )


    Box(
        modifier =
            modifier
                .background(backgroundColor, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable { onClick() }
                .animateContentSize(),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, color = contentColor)
            // 아이콘의 가시성을 애니메이션으로 처리
            AnimatedVisibility(
                visible = state != null,
                enter = expandHorizontally(),
                exit = shrinkHorizontally(),
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_up),
                    contentDescription = null,
                    tint = contentColor,
                    modifier =
                        Modifier
                            .size(20.dp)
                            .scale(1f, flipScaleY),
                )
            }
        }
    }
}

@Composable
fun FilterBar(modifier: Modifier = Modifier) {
    // 상태를 관리할 변수
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    var filterStates by remember {
        mutableStateOf(
            mapOf(
                "날짜순" to null,
                "가격순" to null,
                "좋아요" to null,
                "판매량" to null,
                "조회수" to null,
            ) as Map<String, Boolean?>,
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        filterStates.forEach { (filterName, filterState) ->
            FilterButton(
                text = filterName,
                state = filterState,
                onClick = {
                    // 클릭 시 해당 필터의 상태 변경 로직
                    val newState =
                        when (filterState) {
                            true -> false
                            false -> null
                            else -> true
                        }

                    // 상태 업데이트
                    filterStates =
                        filterStates.mapValues {
                            if (it.key == filterName) newState else null
                        }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterBarPreview() {
    FilterBar(modifier = Modifier.padding(10.dp))
}

enum class StoreFilter(name: String, status: Boolean?){
    DATE("날짜순", null),
    PRICE("가격순", null),
    LIKE("좋아요", null),
    SALE("판매량", null),
    VIEW("조회수", null),
}
