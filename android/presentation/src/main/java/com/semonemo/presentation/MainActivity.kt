package com.semonemo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.semonemo.presentation.screen.ai_asset.AiAssetScreen
import com.semonemo.presentation.screen.img_asset.ImageAssetScreen
import com.semonemo.presentation.screen.moment.MomentScreen
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 상태바까지 확장
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SemonemoTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MomentScreen(
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ImageAssetScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        style = Typography.labelLarge,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SemonemoTheme {
        Greeting("Android")
    }
}
