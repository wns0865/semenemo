package com.semonemo.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography

@Composable
fun TextLabel(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier.padding(15.dp),
        text = text,
        style = Typography.bodyMedium.copy(color = GunMetal, fontSize = 24.sp),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTextLabel() {
    TextLabel(text = "텍스트 라벨")
}
