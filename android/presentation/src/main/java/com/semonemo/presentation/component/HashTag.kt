package com.semonemo.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.WhiteGray
import com.semonemo.presentation.util.noRippleClickable

/**
 * TODO
 * @param keyword -> tag 안에 들어갈 내용
 * @param onTagClicked -> tag 클릭했을 때 람다식
 * @param isEdit -> 편집 모드 인지, 아닌지
 * @param onCloseClicked -> x 버튼 누를 경우
 */

@Composable
fun HashTag(
    modifier: Modifier = Modifier,
    keyword: String,
    onTagClicked: () -> Unit = {},
    isEdit: Boolean = false,
    onCloseClicked: () -> Unit = {},
) {
    AssistChip(
        modifier = modifier.wrapContentHeight(),
        border = null,
        onClick = onTagClicked,
        shape = RoundedCornerShape(8.dp),
        label = { Text(text = "# $keyword") },
        trailingIcon = {
            if (isEdit) {
                Icon(
                    modifier = Modifier.size(15.dp).noRippleClickable { onCloseClicked() },
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            }
        },
        colors =
            AssistChipDefaults.assistChipColors(
                containerColor = WhiteGray,
                labelColor = GunMetal,
                leadingIconContentColor = Color.Transparent,
                trailingIconContentColor = Gray02,
                disabledContainerColor = Color.Transparent,
                disabledLabelColor = Color.Transparent,
                disabledLeadingIconContentColor = Color.Transparent,
            ),
    )
}

@Composable
@Preview()
fun HashTagEditPreview() {
    SemonemoTheme {
        HashTag(keyword = "이나경", isEdit = true)
    }
}

@Composable
@Preview()
fun HashTagPreview() {
    SemonemoTheme {
        HashTag(keyword = "짜이한")
    }
}
