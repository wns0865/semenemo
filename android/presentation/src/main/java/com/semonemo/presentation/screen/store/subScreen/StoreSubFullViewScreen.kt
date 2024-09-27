package com.semonemo.presentation.screen.store.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.component.CustomStoreFilter
import com.semonemo.presentation.component.StoreFilter
import com.semonemo.presentation.component.StoreItemCard
import com.semonemo.presentation.component.frameFilters

@Preview(showBackground = true)
@Composable
fun StoreSubFullViewScreen(
    modifier: Modifier = Modifier,
    isFrame: Boolean = true,
) {
    var filterStates by remember {
        mutableStateOf(StoreFilter.entries.associateWith { null as Boolean? })
    }
    val frameDataList = getSampleFrameData(5)
    Column(
        modifier = modifier
    ) {

        CustomStoreFilter(
            modifier = modifier,
            filters = frameFilters,
            filterStates = filterStates,
            onFilterChange = { selectedFilter, newState ->
                // 선택된 필터만 활성화하고 나머지는 null로
                filterStates = filterStates.mapValues { (filter, _) ->
                    if (filter == selectedFilter) newState else null
                }
            },
        )
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(if (isFrame) 3 else 4),
            contentPadding = PaddingValues(8.dp, 0.dp, 8.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(frameDataList) { storeItem ->
                StoreItemCard(
                    modifier =
                    modifier
                        .fillMaxWidth()
                        .aspectRatio(if (isFrame) 0.6f else 0.8f),
                    title = storeItem.title,
                    author = storeItem.author,
                    imgUrl = storeItem.imgUrl,
                    price = storeItem.price,
                    isLiked = storeItem.isLiked,
                )
            }
        }
    }

}
