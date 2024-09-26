package com.semonemo.presentation.screen.imgAsset

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ImageSelectScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToDone: (String) -> Unit = {},
    imageUri: Uri? = null,
) {
    var selectedImg by remember { mutableStateOf<Uri?>(null) }

    val photoFromAlbumLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    selectedImg = it
                }
            },
        )

    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = White)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 100.dp)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            GlideImage(
                imageModel = selectedImg ?: imageUri,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f),
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.img_select_title),
                style = Typography.labelLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.weight(1f))
            LongBlackButton(
                modifier = Modifier.fillMaxWidth(),
                icon = null,
                text = stringResource(R.string.img_select_confirm),
                onClick = {
                    // AI 배경 제거 작업 후 완료 화면으로 이동
                    // S3 url 전달
                    navigateToDone("test")
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
            LongWhiteButton(
                modifier = Modifier.fillMaxWidth(),
                icon = null,
                text = stringResource(R.string.img_select_again),
                onClick = {
                    // 다시 갤러리에서 사진 선택
                    photoFromAlbumLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                },
            )
        }
    }
}
