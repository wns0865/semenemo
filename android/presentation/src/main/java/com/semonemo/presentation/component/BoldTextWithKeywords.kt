package com.semonemo.presentation.component

import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Main02

/*
@Param  fullText : 전체 텍스트
        keywords : 강조할 키워드
        brushFlag : brush 여부 , true -> brush on | false -> brush off
        style : TextStyle
 */
@Composable
fun BoldTextWithKeywords(
    fullText: String,
    keywords: List<String>,
    brushFlag: List<Boolean>,
    style: TextStyle,
) {
    val annotatedText =
        buildAnnotatedString {
            var currentIndex = 0
            keywords.forEachIndexed { index, keyword ->
                val keywordIndex = fullText.indexOf(keyword, currentIndex)
                if (keywordIndex >= 0) {
                    append(fullText.substring(currentIndex, keywordIndex))
                    withStyle(
                        style =
                            SpanStyle(
                                fontWeight = style.fontWeight ?: FontWeight.SemiBold,
                                brush = if (brushFlag[index]) Main02 else null,
                            ),
                    ) {
                        append(keyword)
                    }
                    currentIndex = keywordIndex + keyword.length
                }
            }
            if (currentIndex < fullText.length) {
                append(fullText.substring(currentIndex))
            }
        }
    Text(text = annotatedText, style = style)
}

@Preview(showBackground = true)
@Composable
fun PreviewBoldTextWithKeywords() {
    val text = "오늘의 추억을 프레임 속에 담다"
    val boldKeywords = listOf("추억", "프레임", "담다")
    val brushFlag = listOf(true, true, true)
    BoldTextWithKeywords(
        fullText = text,
        keywords = boldKeywords,
        brushFlag,
        style = Typography.bodyLarge,
    )
}
