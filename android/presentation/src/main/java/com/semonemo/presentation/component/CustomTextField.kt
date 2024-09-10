package com.semonemo.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Gray02
import com.semonemo.presentation.ui.theme.WhiteGray

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    nickName: String = "",
    focusManager: FocusManager,
    errorMessage: String = "",
    onClearPressed: () -> Unit = {},
) {
    OutlinedTextField(
        value = nickName,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            if (nickName.isEmpty()) {
                Text(
                    text = placeholder,
                    style = Typography.labelSmall,
                )
            }
        },
        modifier =
            modifier
                .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        keyboardActions =
            KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
        textStyle = Typography.labelSmall,
        trailingIcon = {
            IconButton(
                modifier = Modifier.testTag("clear_button"),
                onClick = onClearPressed,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_cancel),
                    contentDescription = "clear_button",
                    tint = Color.Unspecified,
                )
            }
        },
        isError = errorMessage != "",
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
fun CustomTextFieldPreview() {
    SemonemoTheme {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            focusManager = LocalFocusManager.current,
            onValueChange = {},
        )
    }
}
