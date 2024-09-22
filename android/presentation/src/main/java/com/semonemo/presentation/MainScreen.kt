package com.semonemo.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.semonemo.presentation.navigation.ScreenDestinations
import com.semonemo.presentation.screen.ai_asset.DrawAssetScreen
import com.semonemo.presentation.screen.auction.AuctionScreen
import com.semonemo.presentation.screen.login.LoginRoute
import com.semonemo.presentation.screen.login.RegisterRoute
import com.semonemo.presentation.screen.moment.MomentScreen
import com.semonemo.presentation.screen.mypage.MyPageScreen
import com.semonemo.presentation.screen.wallet.WalletScreen

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val snackBarHostState =
        remember {
            SnackbarHostState()
        }

    Scaffold(
        modifier = Modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { innerPadding ->
        MainNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = ScreenDestinations.Login.route,
        )
    }
}

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(
            route = ScreenDestinations.Login.route,
        ) {
            LoginRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToSignUp = {
                    navController.navigate(ScreenDestinations.Register.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.Register.route,
        ) {
            RegisterRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
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
