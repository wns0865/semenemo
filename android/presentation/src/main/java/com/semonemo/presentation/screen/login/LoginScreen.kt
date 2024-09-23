package com.semonemo.presentation.screen.login

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.CustomDialog
import com.semonemo.presentation.component.CustomTextField
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.Main02
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToRegister: (String) -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    LoginContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToRegister = navigateToRegister,
        loginViewModel = loginViewModel,
    )
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
    navigateToRegister: (String) -> Unit,
    nftViewModel: NftViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val isInstalled = checkIfMetaMaskInstalled(context)
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (isConnect, setIsConnect) = remember { mutableStateOf(false) }
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    if (showDialog) {
        CustomDialog(
            title = stringResource(R.string.login_dialog_title),
            content =
                stringResource(R.string.login_dialog_content),
            onConfirmMessage = stringResource(R.string.login_dialog_confirm_message),
            onDismissMessage = stringResource(R.string.login_dialog_dismiss_message),
            titleKeywords = listOf(stringResource(R.string.login_title_keywords)),
            contentKeywords = listOf(stringResource(R.string.login_content_keywords)),
            titleBrushFlag = listOf(false),
            contentBrushFlag = listOf(false),
            onDismiss = { setShowDialog(false) },
            onConfirm = {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(BuildConfig.METAMASK_PLAY_STORE_PATH),
                    )
                context.startActivity(intent)
            },
        )
    }

    HandleLoginUiState(
        uiState = uiState,
        onErrorSnackBar = { message ->
        },
        navigateToMain = {},
        uiEvent = loginViewModel.uiEvent,
        navigateToRegister = navigateToRegister,
        onConnectSuccess = {
            setIsConnect(it)
        },
    )

    LoginScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToRegister = navigateToRegister,
        onClicked = {
            if (isInstalled.not()) { // 설치 안된 경우
                setShowDialog(true) //
            } else {
                nftViewModel.connect { result ->
                    if (result.contains("Error")) {
                        // 에러 처리
                    } else {
                        nftViewModel.switchChain(
                            BuildConfig.CHAIN_ID,
                            BuildConfig.CHAIN_NAME,
                            BuildConfig.RPC_URLS,
                            onError = { message, action ->
                                action?.let {
                                    action()
                                } ?: run {
                                    // 에러 처리
                                }
                            },
                            onSuccess = { address -> loginViewModel.existUser(result) },
                        )
                    }
                }
            }
        },
        isConnect = isConnect,
//        transfer = { nftViewModel.transfer(BuildConfig.CONTRACT_ADDRESS, "1") },
//        onSigned = nftViewModel::sendTransaction,
    )
}

@Composable
fun HandleLoginUiState(
    uiState: LoginUiState,
    uiEvent: SharedFlow<LoginUiEvent>,
    onErrorSnackBar: (String) -> Unit,
    navigateToMain: () -> Unit,
    navigateToRegister: (String) -> Unit,
    onConnectSuccess: (Boolean) -> Unit,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                LoginUiEvent.AutoLogin -> navigateToMain()
                is LoginUiEvent.RequiredRegister -> {
                    navigateToRegister(event.walletAddress)
                }
            }
        }
    }
    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Error -> onErrorSnackBar(uiState.errorMessage) // 에러
            is LoginUiState.Success -> navigateToMain() // 로그인 성공 ->
            LoginUiState.Init -> {}
            is LoginUiState.Loading -> onConnectSuccess(uiState.isWalletLoading)
        }
    }
}

fun checkIfMetaMaskInstalled(context: Context): Boolean =
    try {
        context.packageManager.getPackageInfo("io.metamask", 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    focusManager: FocusManager = LocalFocusManager.current,
    popUpBackStack: () -> Unit = {},
    navigateToRegister: (String) -> Unit = {},
    onClicked: () -> Unit = {},
    isConnect: Boolean = false,
) {
    val offsetY by remember { mutableIntStateOf(0) }

    LaunchedEffect(isConnect) {
        if (isConnect) {
            delay(700)
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = Main01),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        BoldTextWithKeywords(
            fullText = stringResource(R.string.login_title),
            keywords =
                listOf(
                    stringResource(R.string.brush_moment),
                    stringResource(R.string.brush_frame),
                ),
            brushFlag = listOf(true, true),
            boldStyle = Typography.titleSmall.copy(fontSize = 16.sp),
            normalStyle = Typography.labelLarge,
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = stringResource(id = R.string.app_name),
            style = Typography.titleLarge.copy(brush = Main02, fontSize = 40.sp),
        )
        Spacer(modifier = Modifier.weight(0.25f))
        Image(
            painter = painterResource(id = R.drawable.img_start_background),
            contentDescription = "",
        )

        LongWhiteButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            icon = R.drawable.img_metamask,
            text = stringResource(R.string.login_message),
            onClick = onClicked,
        )
        Spacer(modifier = Modifier.weight(0.05f))

        AnimatedVisibility(
            visible = isConnect,
            enter =
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300),
                ),
        ) {
            CustomTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .offset { IntOffset(0, offsetY) },
                focusManager = focusManager,
                errorMessage = "",
                nickName = "",
                onClearPressed = {},
                onValueChange = {},
                placeholder = stringResource(R.string.input_password_message),
                containColor = White,
                borderColor = Gray03,
                roundDp = 14,
            )
        }

        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun LoginScreenPreview() {
    SemonemoTheme {
        LoginScreen()
    }
}
