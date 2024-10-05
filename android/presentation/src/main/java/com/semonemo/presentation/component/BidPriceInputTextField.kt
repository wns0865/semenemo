package com.semonemo.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.semonemo.presentation.theme.SemonemoTheme

@Composable
fun BidPriceInputTextField(
    modifier: Modifier = Modifier,
    bidPrice: Int = 0,
    onValueChange: (Int) -> Unit,
    onClearPressed: () -> Unit = {},
) {
    val (price, setPrice) =
        remember {
            mutableIntStateOf(0)
        }
//    OutlinedTextField(
//        modifier =
//            modifier
//                .fillMaxWidth(),
//        value = price,
//        onValueChange = {
//            setPrice(it)
//            // onValueChange(it)
//        },
//        shape = RoundedCornerShape(10.dp),
//        singleLine = true,
//        textStyle = Typography.bodyMedium
//            .copy(
//                textAlign = TextAlign.End,
//                color = GunMetal,
//                ),
//        leadingIcon = {
//            IconButton(onClick = onClearPressed) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_search_cancel),
//                    contentDescription = "clear_button",
//                    tint = Color.Unspecified,
//                )
//            }
//        },
//        colors =
//            OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Color.Transparent,
//                unfocusedBorderColor = Color.Transparent,
//                focusedContainerColor = WhiteGray,
//                unfocusedContainerColor = WhiteGray,
//                errorContainerColor = Color.Red,
//                focusedTextColor = Gray02,
//                unfocusedTextColor = Gray02,
//                unfocusedPlaceholderColor = Gray02,
//                focusedPlaceholderColor = Gray02,
//            ),
//    )
}

@Composable
@Preview()
fun BidPriceInputTextFieldPreview() {
    SemonemoTheme {
        BidPriceInputTextField(
            modifier = Modifier.fillMaxWidth(),
            bidPrice = 5000,
            onValueChange = {},
            onClearPressed = {},
        )
    }
}
