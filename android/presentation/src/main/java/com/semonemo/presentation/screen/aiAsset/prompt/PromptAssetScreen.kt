package com.semonemo.presentation.screen.aiAsset.prompt

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.ScriptTextField
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.aiAsset.draw.AssetButtonList
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

@Composable
fun PromptAssetScreen(
    modifier: Modifier = Modifier,
    navigateToDone: (String) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    var prompt by remember { mutableStateOf("") }

    val titles =
        listOf(
            "실사",
            "카툰",
            "애니메이션",
        )
    var selectedBtn by remember { mutableStateOf("실사") }

    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState),
        color = Color.White,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(modifier = Modifier.fillMaxWidth())
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 22.dp),
            ) {
                BoldTextWithKeywords(
                    modifier = Modifier.padding(top = 15.dp),
                    fullText = stringResource(R.string.prompt_title),
                    keywords = listOf("설명하는 문장", "자세히"),
                    brushFlag = listOf(false, false),
                    boldStyle = Typography.bodyLarge.copy(fontSize = 22.sp),
                    normalStyle = Typography.labelLarge.copy(fontSize = 22.sp),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.prompt_example),
                    style = Typography.labelMedium.copy(fontSize = 14.sp),
                    color = Gray02,
                )
                Spacer(modifier = Modifier.height(40.dp))
                ScriptTextField(
                    placeholder = stringResource(R.string.prompt_placeholder),
                    height = 200,
                    value = prompt,
                    onValueChange = { newValue ->
                        prompt = newValue
                    },
                    focusManager = focusManager,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "화풍",
                        style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    AssetButtonList(
                        titles = titles,
                        selectedBtn = selectedBtn,
                        onBtnSelected = { selectedBtn = it },
                    )
                }
                Spacer(modifier = Modifier.height(80.dp))
                LongBlackButton(
                    modifier = Modifier.fillMaxWidth(),
                    icon = null,
                    text = stringResource(R.string.prompt_done_btn_title),
                    onClick = {
                        // api 통신
                        translateText(text = prompt) { result ->
                            Log.d("nakyung", "translate Result: $result")
                        }
//                        navigateToDone("test")
                    },
                )
            }
        }
    }
}

fun translateText(
    text: String,
    callback: (String?) -> Unit,
) {
    Log.d("nakyung", text)
    val client = OkHttpClient()
    val url = "https://libretranslate.com/translate"
    val requestBody =
        FormBody
            .Builder()
            .add("q", text)
            .add("source", "ko")
            .add("target", "en")
            .add("format", "text")
            .build()

    val request =
        Request
            .Builder()
            .url(url)
            .post(requestBody)
            .build()

    client.newCall(request).enqueue(
        object : Callback {
            override fun onFailure(
                call: Call,
                e: IOException,
            ) {
                callback(null) // 오류 처리
            }

            override fun onResponse(
                call: Call,
                response: Response,
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val json = responseData?.let { JSONObject(it) }
                    callback(json?.getString("translatedText"))
                } else {
                    callback(null) // 오류 처리
                }
            }
        },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PromptAssetScreenPreview() {
    SemonemoTheme {
        PromptAssetScreen()
    }
}
