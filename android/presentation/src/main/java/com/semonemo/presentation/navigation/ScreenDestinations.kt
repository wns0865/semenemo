package com.semonemo.presentation.navigation

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
    val route: String,
) {
    data object Login : ScreenDestinations(route = "login")

    data object Register : ScreenDestinations(route = "register")

    data object Shop : ScreenDestinations(route = "shop")
}
