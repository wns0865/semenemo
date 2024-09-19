package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iamageo.multifablibrary.FabIcon
import com.iamageo.multifablibrary.FabOption
import com.iamageo.multifablibrary.MultiFabItem
import com.iamageo.multifablibrary.MultiFloatingActionButton
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.White

@Preview
@Composable
fun CustomFloatingActionButton() {
    MultiFloatingActionButton(
        fabIcon =
            FabIcon(
                iconRes = R.drawable.ic_fab_shop_add,
                iconResAfterRotate = R.drawable.ic_fab_shop_add,
                iconRotate = 180f,
            ),
        fabOption =
            FabOption(
                iconTint = White,
                showLabels = true,
                backgroundTint = GunMetal,
            ),
        itemsMultiFab =
            listOf(
                MultiFabItem(
                    icon = R.drawable.ic_fab_asset,
                    label = "에셋 판매",
                    labelColor = Color.Unspecified,
                ),
                MultiFabItem(
                    icon = R.drawable.ic_fab_frame,
                    label = "프레임 판매",
                    labelColor = Color.Unspecified,
                ),

            ),
        onFabItemClicked = { println(it) },
        fabTitle = "스토어 판매",
        showFabTitle = false,
    )
}
@Preview(showBackground = true)
@Composable
fun ExpandableFloatingActionButton() {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
            Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CustomFloatingActionButton()
        }
    }
}

@Composable
fun CustomSubButton(
    text: String,
    icon: Int,
) {
    Button(
        onClick = { /* 버튼 동작 */ },
        colors = ButtonDefaults.buttonColors(containerColor = GunMetal),
        shape = CircleShape,
        modifier = Modifier.height(48.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "sell frames",
                tint = Color.Unspecified,
            )
            Text(text = text, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableFloatingActionButtonPreview() {
    ExpandableFloatingActionButton() // 미리보기에서 버튼 확인
}
