package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray

@Composable
fun ScriptTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    height: Int,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = height.dp),
            value = value,
            onValueChange = {
                if (it.isNotEmpty()) {
                    onValueChange(it)
                }
            },
            textStyle = Typography.labelSmall.copy(fontSize = 13.sp),
            placeholder = {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = placeholder,
                    style = Typography.labelSmall.copy(fontSize = 13.sp),
                    color = Gray02,
                )
            },
            shape = RoundedCornerShape(10.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = GunMetal,
                    unfocusedTextColor = GunMetal,
                    focusedContainerColor = WhiteGray,
                    unfocusedContainerColor = WhiteGray,
                ),
        )
    }
}
