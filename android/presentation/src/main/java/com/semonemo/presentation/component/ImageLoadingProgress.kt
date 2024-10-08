package com.semonemo.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.Gray01

@Composable
@Preview(showBackground = true)
fun ImageLoadingProgress(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier =
            modifier
                .size(40.dp)
                .padding(5.dp),
        color = Gray01,
        strokeWidth = 3.dp,
    )
}
