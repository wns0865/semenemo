package com.semonemo.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray

@Composable
fun BidPriceInputTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    bidPrice: Int = 0,
    onClearPressed: () -> Unit = {},
) {
    val (price, setPrice) =
        remember {
            mutableStateOf("")
        }
    OutlinedTextField(
        modifier =
            modifier
                .fillMaxWidth(),
        value = price,
        onValueChange = {
            setPrice(it)
            // onValueChange(it)
        },
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        textStyle = Typography.bodyMedium
            .copy(
                textAlign = TextAlign.End,
                color = GunMetal,
                ),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search_cancel),
                contentDescription = "clear_button",
                tint = Color.Unspecified,
            )
        },
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = WhiteGray,
                unfocusedContainerColor = WhiteGray,
                errorContainerColor = Color.Red,
                focusedTextColor = Gray02,
                unfocusedTextColor = Gray02,
                unfocusedPlaceholderColor = Gray02,
                focusedPlaceholderColor = Gray02,
            ),
    )
}

@Composable
@Preview()
fun BidPriceInputTextFieldPreview() {
    SemonemoTheme {
        BidPriceInputTextField(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {},
        )
    }
}
