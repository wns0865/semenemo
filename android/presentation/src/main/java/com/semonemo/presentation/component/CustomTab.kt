package com.semonemo.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Gray02
import com.semonemo.presentation.ui.theme.GunMetal
import kotlinx.collections.immutable.toPersistentList

/**
 * CustomTab
 * tabList에 메뉴 이름들을 List 형태로 넣어 주면 됨.
 * 사용 예시는 프리뷰에
 */

@Composable
fun CustomTab(
    modifier: Modifier = Modifier,
    tabList: List<String> = listOf(),
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val tabItems = tabList.toPersistentList()

    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.Unspecified,
        contentColor = GunMetal,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                height = 1.dp,
                color = GunMetal,
            )
        },
    ) {
        tabItems.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                selectedContentColor = GunMetal,
                unselectedContentColor = Gray02,
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = title,
                    style =
                        if (selectedIndex == index) {
                            Typography.bodySmall.copy(fontSize = 13.sp)
                        } else {
                            Typography.labelMedium.copy(
                                fontSize = 13.sp,
                            )
                        },
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomTabPreview() {
    CustomTab(
        tabList = listOf("내 프레임", "에셋"),
    )
}
