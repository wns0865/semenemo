package com.semonemo.presentation.screen.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.CustomTextField
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.addFocusCleaner

@Composable
fun RegisterRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToMain: () -> Unit = {},
) {
    RegisterContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToMain = navigateToMain,
    )
}

@Composable
fun RegisterContent(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    navigateToMain: () -> Unit,
    registerViewModel: RegisterViewModel = hiltViewModel(),
) {
    RegisterScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToMain = navigateToMain,
    )
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToMain: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier =
            modifier
                .background(brush = Main01)
                .padding(top = 65.dp)
                .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BoldTextWithKeywords(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Start),
            fullText = stringResource(R.string.register_title),
            keywords =
                listOf(
                    stringResource(R.string.brush_need),
                    stringResource(R.string.brush_persenal_info),
                ),
            brushFlag = listOf(false, false),
            boldStyle = Typography.titleSmall.copy(fontSize = 25.sp),
            normalStyle = Typography.labelLarge.copy(fontSize = 25.sp),
        )

        Spacer(modifier = Modifier.weight(0.1f))
        IconButton(
            modifier = Modifier.size(200.dp),
            onClick = { /*TODO*/ },
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_user_profile_add),
                contentDescription = "",
            )
        }
        Spacer(modifier = Modifier.weight(0.3f))
        CustomTextField(
            modifier = Modifier.fillMaxWidth(0.88f),
            focusManager = focusManager,
            errorMessage = "",
            nickName = "",
            onClearPressed = {},
            onValueChange = {},
            placeholder = stringResource(R.string.input_nickname_message),
        )
        Spacer(modifier = Modifier.weight(0.1f))
        CustomTextField(
            modifier = Modifier.fillMaxWidth(0.88f),
            focusManager = focusManager,
            errorMessage = "",
            nickName = "",
            onClearPressed = {},
            onValueChange = {},
            placeholder = stringResource(R.string.input_password_message),
        )
        Spacer(modifier = Modifier.weight(0.3f))
        LongBlackButton(
            icon = null,
            text = stringResource(R.string.register_message),
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
@Preview(showSystemUi = true)
fun RegisterScreenPreview() {
    SemonemoTheme {
        RegisterScreen(modifier = Modifier.fillMaxSize())
    }
}
