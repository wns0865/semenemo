package com.semonemo.presentation.screen.signup

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.CustomPasswordTextField
import com.semonemo.presentation.component.CustomTextField
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongUnableButton
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.Validator
import com.semonemo.presentation.util.addFocusCleaner

@Composable
fun SignUpRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToMain: () -> Unit = {},
) {
    SignUpContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToMain = navigateToMain,
    )
}

@Composable
fun SignUpContent(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    navigateToMain: () -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by signUpViewModel.uiState.collectAsStateWithLifecycle()
    SignUpScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToMain = navigateToMain,
        nickname = uiState.nickname,
        password = uiState.password,
        profile = uiState.profileImageUrl,
        updateNickname = { signUpViewModel.updateNickname(it) },
        updatePassword = { signUpViewModel.updatePassword(it) },
        updateProfile = { signUpViewModel.updateProfileImageUrl(it) },
    )
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToMain: () -> Unit = {},
    nickname: String = "",
    password: String = "",
    profile: String = "",
    updateNickname: (String) -> Unit = {},
    updatePassword: (String) -> Unit = {},
    updateProfile: (String) -> Unit = {},
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
            errorMessage = Validator.validationNickname(nickname),
            input = nickname,
            onClearPressed = { updateNickname("") },
            onValueChange = { updateNickname(it) },
            placeholder = stringResource(R.string.input_nickname_message),
        )
        Spacer(modifier = Modifier.weight(0.3f))
        CustomPasswordTextField(
            modifier = Modifier.fillMaxWidth(0.88f),
            focusManager = focusManager,
            errorMessage = Validator.validationPassword(password),
            input = password,
            onClearPressed = { updatePassword("") },
            onValueChange = { updatePassword(it) },
            placeholder = stringResource(R.string.input_password_message),
            isPasswordField = true,
        )
        Spacer(modifier = Modifier.weight(0.3f))
        if (nickname.isNotBlank() &&
            password.isNotBlank() &&
            Validator.validationNickname(nickname).isEmpty() &&
            Validator
                .validationPassword(
                    password,
                ).isEmpty()
        ) {
            LongBlackButton(
                icon = null,
                text = stringResource(R.string.register_message),
            )
        } else {
            LongUnableButton(
                modifier = Modifier.fillMaxWidth(0.88f),
                text = stringResource(id = R.string.register_message),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
@Preview(showSystemUi = true)
fun RegisterScreenPreview() {
    SemonemoTheme {
        SignUpScreen(modifier = Modifier.fillMaxSize())
    }
}
