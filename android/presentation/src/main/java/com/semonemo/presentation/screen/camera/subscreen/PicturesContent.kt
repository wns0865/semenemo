package com.semonemo.presentation.screen.camera.subscreen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White

@Composable
fun PicturesContent(
    modifier: Modifier,
    bitmaps: List<Bitmap>,
) {
    if (bitmaps.isEmpty()) {
        Box(
            modifier = modifier.padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("photo Section", style = Typography.titleMedium.copy(color = White))
        }
    } else {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            contentPadding = PaddingValues(16.dp),
            content = {
                items(count = bitmaps.size) { index ->
                    Image(
                        modifier =
                            Modifier
                                .width(100.dp)
                                .clip(RoundedCornerShape((10.dp))),
                        bitmap = bitmaps[index].asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                }
            },
        )
    }
}
