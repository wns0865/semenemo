package com.semonemo.presentation.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.domain.model.Asset
import com.semonemo.domain.model.Frame
import com.semonemo.domain.model.User
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BackButton
import com.semonemo.presentation.component.CustomTab
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.SearchTextField
import com.semonemo.presentation.component.SectionHeader
import com.semonemo.presentation.screen.wallet.testData
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable

@Composable
fun SearchRoute(
    modifier: Modifier = Modifier,
    navigateToProfile: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    SearchScreen(
        modifier = modifier,
        navigateToProfile = navigateToProfile,
        searchState = searchState,
        viewModel = viewModel,
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchState: SearchState,
    viewModel: SearchViewModel,
    popUpBackStack: () -> Unit = {},
    onSearchAction: (String) -> Unit = {},
    navigateToProfile: (Long) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val testData = remember { mutableStateListOf("이나경", "이지언", "이준형", "최현성", "전형선", "이재한") }

    Column(
        modifier =
            modifier
                .navigationBarsPadding()
                .statusBarsPadding()
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
                onSearchAction = { keyword ->
                    viewModel.userSearch(keyword)
                },
            )
        }
        Spacer(modifier = Modifier.weight(0.05f))
        when (searchState) {
            is SearchState.Loading -> {
                LoadingDialog()
            }

            is SearchState.Init -> {
                SearchInitScreen(
                    modifier = modifier,
                    recentSearchList = testData,
                    hotSearchList = searchState.hotList,
                    onClickedKeyword = { keyword ->
                        // 키워드 검색
                        // searchTextField에 해당 키워드 뜨도록
                        viewModel.userSearch(keyword)
                    },
                    onDeleteKeyword = {
                        testData.remove(it)
                    },
                )
            }

            is SearchState.Success -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    SearchTabScreen(viewModel = viewModel)
                    Spacer(modifier = Modifier.height(10.dp))
                    SearchSuccessScreen(
                        modifier = modifier,
                        userList = searchState.userList,
                        frameList = searchState.frameList,
                        assetList = searchState.assetList,
                        navigateToProfile = navigateToProfile,
                    )
                }
            }

            is SearchState.Error -> {
            }
        }
    }
}

@Composable
fun SearchInitScreen(
    modifier: Modifier = Modifier,
    recentSearchList: List<String> = emptyList(),
    hotSearchList: List<String> = emptyList(),
    onClickedKeyword: (String) -> Unit,
    onDeleteKeyword: (String) -> Unit,
) {
    val (isEdit, setEdit) =
        remember {
            mutableStateOf(false)
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(0.06f))
        RecentSearchSection(setEdit, isEdit)
        RecentSearchKeywords(isEdit, recentSearchList, onClickedKeyword, onDeleteKeyword)
        Spacer(modifier = Modifier.weight(0.08f))
        HotKeywordSection(modifier = Modifier)
        Spacer(modifier = Modifier.weight(0.06f))
        HotKeywords(keywords = hotSearchList, onClick = onClickedKeyword)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SearchTabScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val tabList = listOf("사용자", "프레임", "에셋")
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        CustomTab(
            modifier = Modifier.fillMaxWidth(),
            tabList = tabList,
            selectedIndex = selectedIndex,
            onTabSelected = { index ->
                selectedIndex = index
            },
        )
        when (selectedIndex) {
            0 -> {
                viewModel.userSearch("닉네임")
            }

            1 -> {
                viewModel.frameSearch("아이유")
            }

            2 -> {
                viewModel.assetSearch("고양이")
            }
        }
    }
}

@Composable
fun SearchSuccessScreen(
    modifier: Modifier = Modifier,
    userList: List<User> = emptyList(),
    frameList: List<Frame> = emptyList(),
    assetList: List<Asset> = emptyList(),
    navigateToProfile: (Long) -> Unit = {},
) {
    if (userList.isNotEmpty()) {
        LazyColumn(
            modifier =
                modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(userList.size) { index ->
                val user = userList[index]
                UserListItem(
                    userId = user.userId,
                    profileImgUrl = user.profileImage,
                    nickname = user.nickname,
                    navigateToProfile = navigateToProfile,
                )
            }
        }
    } else if (frameList.isNotEmpty()) {
        LazyVerticalGrid(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp),
            columns = GridCells.Fixed(2),
            state = rememberLazyGridState(),
        ) {
            items(frameList.size) { index ->
                val frame = frameList[index]
                // 실제 api 연결 후 코드는 아래와 같이
//                GlideImage(
//                    modifier =
//                        Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                            .clip(shape = RoundedCornerShape(10.dp))
//                            .border(
//                                width = 1.dp,
//                                shape = RoundedCornerShape(10.dp),
//                                color = Gray03,
//                            ),
//                    imageModel = frame.imgUrl.toUri(),
//                    contentScale = ContentScale.Crop,
//                )

                // 임시
                Image(
                    painter = painterResource(id = R.drawable.img_example3),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(10.dp),
                                color = Gray03,
                            ),
                )
            }
        }
    } else if (assetList.isNotEmpty()) {
        LazyVerticalGrid(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp),
            columns = GridCells.Fixed(3),
            state = rememberLazyGridState(),
        ) {
            items(assetList.size) { index ->
                val asset = assetList[index]
                // 실제 api 연결 후 코드는 아래와 같이
//                GlideImage(
//                    modifier =
//                        Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                            .clip(shape = RoundedCornerShape(10.dp))
//                            .border(
//                                width = 1.dp,
//                                shape = RoundedCornerShape(10.dp),
//                                color = Gray03,
//                            ),
//                    imageModel = asset.imgUrl.toUri(),
//                    contentScale = ContentScale.Crop,
//                )

                // 임시
                Image(
                    painter = painterResource(id = R.drawable.asset_example),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(8.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(10.dp),
                                color = Gray03,
                            ),
                )
            }
        }
    }
}

@Composable
fun UserListItem(
    modifier: Modifier = Modifier,
    userId: Long = 0L,
    profileImgUrl: String = "",
    nickname: String = "",
    navigateToProfile: (Long) -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .noRippleClickable {
                    navigateToProfile(userId)
                },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 실제 api 연동 후엔 이 코드로
//        Box(
//            modifier =
//            Modifier
//                .size(50.dp)
//                .clip(CircleShape)
//        ) {
//            GlideImage(
//                imageModel = profileImgUrl.toUri(),
//                contentScale = ContentScale.Crop
//            )
//        }
        Box(
            modifier =
                Modifier
                    .size(50.dp)
                    .clip(CircleShape),
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_example3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = nickname,
            style = Typography.bodySmall.copy(fontSize = 14.sp),
        )
    }
}

@Composable
private fun RecentSearchKeywords(
    isEdit: Boolean,
    recentSearchList: List<String>,
    onClickedKeyword: (String) -> Unit,
    onDeleteKeyword: (String) -> Unit,
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        modifier = Modifier.height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            items(recentSearchList) { item ->
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
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.img_clock),
            contentDescription = null,
        )
        SectionHeader(
            modifier = Modifier,
            text = stringResource(R.string.recent_search_text),
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier =
                Modifier.noRippleClickable {
                    setEdit(isEdit.not())
                },
            text = mode,
            style = Typography.labelLarge.copy(color = Gray01),
        )
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
    onClick: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        repeat(keywords.size) { index ->
            val item = keywords[index]
            HotKeyWord(
                modifier =
                    Modifier.noRippleClickable {
                        onClick(item)
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
    }
}
