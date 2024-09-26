package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.SemonemoTheme

enum class TopAppBarNavigationType { Back, None }

/**
 * TODO
 *
 * @param modifier
 * @param title : 타이틀 Composable 함수
 * @param navigationType : 내비게이션 타입 ex)
 * @param actionButtons : 끝 버튼 ex) 구독 버튼, 잠기는 버튼
 * @param onNavigationClick : icon 클릭 동작 ex) 뒤로 가기
 */
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationType: TopAppBarNavigationType = TopAppBarNavigationType.Back,
    actionButtons: @Composable () -> Unit = {},
    onNavigationClick: () -> Unit = {},
) {
    val icon: @Composable (Modifier, imageVector: ImageVector) -> Unit =
        { modifier, imageVector ->
            IconButton(
                onClick = onNavigationClick,
                modifier = modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "",
                )
            }
        }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {}
                .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        if (navigationType == TopAppBarNavigationType.Back) {
            icon(
                Modifier.align(Alignment.CenterStart),
                Icons.AutoMirrored.Filled.ArrowBack,
            )
        }
        Row(Modifier.align(Alignment.Center)) {
            title()
        }
        Row(Modifier.align(Alignment.CenterEnd).padding(end = 10.dp)) {
            actionButtons()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TopAppBarPreview() {
    val (select, setSelect) =
        remember {
            androidx.compose.runtime.mutableStateOf(false)
        }
    SemonemoTheme {
        TopAppBar(
            title = {
                NameWithBadge(
                    name = "나갱갱",
                    size = 18,
                )
            },
            actionButtons = {
                SubScribeButton(
                    isSubscribed = select,
                    onToggleSubscription = { setSelect(select.not()) },
                )
            },
        )
    }
}
