package com.semonemo.presentation.screen.mypage.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.semonemo.domain.model.Asset
import com.semonemo.presentation.component.ImageLoadingProgress
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.White
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MyPageOwnedAssets(
    modifier: Modifier,
    assetList: List<Asset>,
) {
    LazyVerticalGrid(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp),
        columns = GridCells.Fixed(3),
        state = rememberLazyGridState(),
    ) {
        items(assetList.size) { index ->
            val asset = assetList[index]

            GlideImage(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(color = White.copy(alpha = 0.6f))
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(10.dp),
                            color = Gray03,
                        ),
                imageModel = asset.imageUrl,
                contentScale = ContentScale.Inside,
                loading = {
                    ImageLoadingProgress(
                        modifier = Modifier.fillMaxSize(),
                    )
                },
            )
        }
    }
}
