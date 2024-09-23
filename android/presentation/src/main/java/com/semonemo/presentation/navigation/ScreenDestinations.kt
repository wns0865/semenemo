package com.semonemo.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/*
 추가 하는 방법
    data object sample : ScreenDestination(route = "화면이름"){
        // 넘기는 인자 있어야 하는 경우
        val arguments = listOf(
            navArgument(name = "id") { type = NavType.StringType },
            navArgument(name = "number") { type = NavType.IntType },
        )
        fun createRoute(
            id : String,
            number : Int
        ) = this.route/id/$number
    }
 */

sealed class ScreenDestinations(
    open val route: String,
) {
    data object Login : ScreenDestinations(route = "login")

    data object Register : ScreenDestinations(route = "register") {
        override val route: String
            get() = "register/{walletAddress}"
        val arguments =
            listOf(
                navArgument(name = "walletAddress") { type = NavType.StringType },
            )

        fun createRoute(walletAddress: String) = "register/$walletAddress"
    }

    data object Shop : ScreenDestinations(route = "shop")

    data object Auction : ScreenDestinations(route = "auction")

    data object Moment : ScreenDestinations(route = "moment")

    data object Wallet : ScreenDestinations(route = "wallet")

    data object MyPage : ScreenDestinations(route = "mypage")
}
