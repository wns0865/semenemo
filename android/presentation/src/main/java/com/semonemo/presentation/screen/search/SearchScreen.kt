package com.semonemo.presentation.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BackButton
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.SearchTextField
import com.semonemo.presentation.component.SectionHeader
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable

@Composable
fun SearchRoute() {
}

val testData = listOf("이나경", "이지언", "이준형", "최현성", "전형선", "이재한")

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onClickedKeyword: () -> Unit = {},
    onDeleteKeyword: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val (isEdit, setEdit) =
        remember {
            mutableStateOf(false)
        }
    Column(
        modifier =
            modifier
                .padding(horizontal = 10.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
    ) {
        Row(modifier = Modifier) {
            BackButton(popUpBackStack = popUpBackStack)
            SearchTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 5.dp),
                focusManager = focusManager,
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        RecentSearchSection(setEdit, isEdit)
        Spacer(modifier = Modifier.weight(0.02f))
        RecentSearchKeywords(isEdit, onClickedKeyword, onDeleteKeyword)
        Spacer(modifier = Modifier.weight(0.1f))
        HotKeywordSection(modifier = Modifier)
        Spacer(modifier = Modifier.weight(0.1f))
        HotKeywords(keywords = testData, onClick = onClickedKeyword)
        Spacer(modifier = Modifier.weight(0.4f))
    }
}

@Composable
private fun RecentSearchKeywords(
    isEdit: Boolean,
    onClickedKeyword: () -> Unit,
    onDeleteKeyword: () -> Unit,
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        modifier = Modifier.height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            items(testData) { item ->
                HashTag(
                    modifier = Modifier,
                    keyword = item,
                    isEdit = isEdit,
                    onTagClicked = onClickedKeyword,
                    onCloseClicked = onDeleteKeyword,
                )
            }
        },
    )
}

@Composable
private fun RecentSearchSection(
    setEdit: (Boolean) -> Unit,
    isEdit: Boolean,
) {
    val mode =
        if (isEdit) {
            stringResource(id = R.string.edit_true_text)
        } else {
            stringResource(id = R.string.edit_false_text)
        }
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.img_clock),
            contentDescription = null,
        )
        SectionHeader(modifier = Modifier, text = stringResource(R.string.recent_search_text))
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier =
                Modifier.noRippleClickable {
                    setEdit(isEdit.not())
                },
            text = mode,
            style = Typography.labelLarge.copy(color = Gray01),
        )
        Spacer(modifier = Modifier.weight(0.1f))
    }
}

@Composable
fun HotKeywordSection(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.img_fire),
            contentDescription = null,
        )
        SectionHeader(modifier = Modifier, text = stringResource(R.string.hot_section_text))
    }
}

@Composable
fun HotKeywords(
    keywords: List<String> = listOf(),
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        repeat(keywords.size) { index ->
            val item = testData[index]
            HotKeyWord(
                modifier =
                    Modifier.noRippleClickable {
                        onClick()
                    },
                number = index + 1,
                keyword = item,
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun SearchScreenPreview() {
    SemonemoTheme {
        SearchScreen(modifier = Modifier.fillMaxSize())
    }
}
