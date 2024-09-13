package com.semonemo.presentation.screen.login

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Main01
import com.semonemo.presentation.ui.theme.Main02

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToSignUp: () -> Unit = {},
    viewModel: EthereumViewModel = hiltViewModel(),
) {
    LoginContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        navigateToSignUp = navigateToSignUp,
        viewModel = viewModel,
    )
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit,
    navigateToSignUp: () -> Unit,
    viewModel: EthereumViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val packageName = BuildConfig.METAMASK_PACKAGE_NAME
    val isInstalled: Boolean =
        try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    if (isInstalled.not()) {
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BuildConfig.METAMASK_PLAY_STORE_PATH),
            )
        context.startActivity(intent)
    }

    LoginScreen(
        modifier = modifier,
        popUpBackStack,
        navigateToSignUp,
        onClicked = {
            viewModel.connect { result ->
                if (result.contains("Error")) {
                } else {
                    viewModel.switchChain(
                        BuildConfig.CHAIN_ID,
                        BuildConfig.CHAIN_NAME,
                        BuildConfig.RPC_URLS,
                        onSuccess = { message ->
                            Log.d("jaehan", "addSuccess : $message")
                        },
                        onError = { message, action ->
                            action?.let {
                                Log.d("jaehan", "error success?")
                                action()
                            } ?: run {
                                Log.d("jaehan", "그냥 에러, $message")
                            }
                        },
                    )
                }
            }
        },
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToSignUp: () -> Unit = {},
    onClicked: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
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
        Spacer(modifier = Modifier.weight(0.3f))
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
