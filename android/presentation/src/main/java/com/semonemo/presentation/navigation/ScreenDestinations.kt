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

    data object AiAsset : ScreenDestinations(route = "aiAsset")

    data object ImageAsset : ScreenDestinations(route = "imageAsset")

    data object Select : ScreenDestinations(route = "select") {
        override val route: String
            get() = "select/{selectedImg}"
        val arguments =
            listOf(
                navArgument(name = "selectedImg") { type = NavType.StringType },
            )

        fun createRoute(selectedImg: String) = "select/$selectedImg"
    }

    data object AssetDone : ScreenDestinations(route = "assetDone") {
        override val route: String
            get() = "assetDone/{assetUrl}"
        val arguments =
            listOf(
                navArgument(name = "assetUrl") { type = NavType.StringType },
            )

        fun createRoute(assetUrl: String) = "assetDone/$assetUrl"
    }

    data object PictureMain : ScreenDestinations(route = "pictureMain")

    data object DrawAsset : ScreenDestinations(route = "drawAsset")

    data object PromptAsset : ScreenDestinations(route = "promptAsset")

    data object Detail : ScreenDestinations(route = "detail") {
        override val route: String
            get() = "detail/{imgUrl}"
        val arguments =
            listOf(
                navArgument(name = "imgUrl") { type = NavType.StringType },
            )

        fun createRoute(imgUrl: String) = "detail/$imgUrl"
    }

    data object Frame : ScreenDestinations(route = "frame")

    data object AuctionProcess : ScreenDestinations(route = "auctionProcess") {
        override val route: String
            get() = "auctionProcess/{auctionId}"
        val arguments =
            listOf(
                navArgument("auctionId") { type = NavType.StringType },
            )

        fun createRoute(auctionId: String) = "auctionProcess/$auctionId"
    }

    data object StoreFullView: ScreenDestinations(route = "storeFullView") {
        override val route: String
            get() = "storeFullView/{isFrame}"
        val arguments =
            listOf(
                navArgument(name = "isFrame") { type = NavType.BoolType },
            )

        fun createRoute(isFrame: Boolean) = "storeFullView/$isFrame"
    }

    data object FrameDone : ScreenDestinations(route = "frameDone")
}
