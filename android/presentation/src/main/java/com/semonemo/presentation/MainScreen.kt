package com.semonemo.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.semonemo.presentation.navigation.BottomNavItem
import com.semonemo.presentation.navigation.CustomFAB
import com.semonemo.presentation.navigation.ScreenDestinations
import com.semonemo.presentation.screen.ai_asset.DrawAssetScreen
import com.semonemo.presentation.screen.auction.AuctionScreen
import com.semonemo.presentation.screen.login.LoginRoute
import com.semonemo.presentation.screen.moment.MomentScreen
import com.semonemo.presentation.screen.mypage.MyPageScreen
import com.semonemo.presentation.screen.signup.SignUpRoute
import com.semonemo.presentation.screen.wallet.WalletScreen
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val (visible, setVisible) =
        remember {
            mutableStateOf(false)
        }
    val items =
        listOf(
            BottomNavItem.Shop,
            BottomNavItem.Auction,
            BottomNavItem.Wallet,
            BottomNavItem.MyPage,
        )
    when (currentRoute) {
        "mypage", "shop", "moment", "wallet", "auction" -> setVisible(true)

        else -> setVisible(false)
    }

    val snackBarHostState =
        remember {
            SnackbarHostState()
        }

    val onShowErrorSnackBar: (errorMessage: String) -> Unit = { errorMessage ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar(errorMessage)
        }
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding().statusBarsPadding(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            if (visible) {
                BottomAppBar(
                    modifier =
                        modifier
                            .height(65.dp)
                            .shadow(
                                elevation = 20.dp,
                                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                            ), // 그림자 적용
                    containerColor = Color.White,
                ) {
                    items.forEachIndexed { index, item ->
                        val isSelected = currentRoute == item.route
                        val icon = if (isSelected) item.iconSelected else item.icon

                        if (index == 2) {
                            Spacer(modifier = Modifier.weight(1f, true))
                        }

                        NavigationBarItem(
                            colors =
                                NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent,
                                    selectedIconColor = GunMetal,
                                    unselectedIconColor = Gray01,
                                    selectedTextColor = GunMetal,
                                    unselectedTextColor = Gray01,
                                ),
                            selected = currentRoute == item.route,
                            label = {
                                Text(
                                    text = stringResource(id = item.title),
                                    style = if (isSelected) Typography.bodySmall.copy(fontSize = 12.sp) else Typography.labelSmall,
                                )
                            },
                            onClick = {
                                navController.navigate(item.route) {
                                    navController.graph.startDestinationRoute?.let {
                                        popUpTo(it) { saveState = true }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = stringResource(id = item.title),
                                    tint = Color.Unspecified,
                                )
                            },
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (visible) {
                val isSelected = currentRoute == ScreenDestinations.Moment.route

                CustomFAB(
                    onClick = {
                        navController.navigate(ScreenDestinations.Moment.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon =
                        if (isSelected) {
                            R.drawable.ic_bot_nav_fill_frame
                        } else {
                            R.drawable.ic_bot_nav_outline_frame
                        },
                    iconColor = Color.White,
                    labelColor =
                        if (isSelected) GunMetal else Gray01,
                    labelStyle =
                        if (isSelected) Typography.bodySmall.copy(fontSize = 12.sp) else Typography.labelSmall,
                )
            }
        },
    ) { innerPadding ->
        MainNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = ScreenDestinations.Login.route,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    onShowErrorSnackBar: (String) -> Unit,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(
            route = ScreenDestinations.Login.route,
        ) {
            LoginRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToRegister = { walletAddress ->
                    navController.navigate(ScreenDestinations.Register.createRoute(walletAddress))
                },
                onShowErrorSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Register.route,
            arguments = ScreenDestinations.Register.arguments,
        ) {
            SignUpRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowErrorSnackBar = onShowErrorSnackBar,
                navigateToMain = {
                    navController.navigate(ScreenDestinations.Moment.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.Shop.route,
        ) {
            // 상점 스크린
            // 일단 임시로 아무 스크린이나
            DrawAssetScreen()
        }

        composable(
            route = ScreenDestinations.Auction.route,
        ) {
            AuctionScreen()
        }

        composable(
            route = ScreenDestinations.Moment.route,
        ) {
            MomentScreen()
        }

        composable(
            route = ScreenDestinations.Wallet.route,
        ) {
            WalletScreen()
        }

        composable(
            route = ScreenDestinations.MyPage.route,
        ) {
            MyPageScreen()
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry =
        remember(this) {
            navController.getBackStackEntry(navGraphRoute)
        }
    return hiltViewModel(parentEntry)
}
