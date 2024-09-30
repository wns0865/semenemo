package com.semonemo.presentation.screen.frame

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.semonemo.presentation.navigation.ScreenDestinations

fun NavGraphBuilder.FrameNavGraph(
    modifier: Modifier,
    navController: NavController,
    graphRoute: String,
    onErrorSnackBar: (String) -> Unit,
) {
    navigation(startDestination = ScreenDestinations.Frame.route, route = graphRoute) {
        composable(
            route = ScreenDestinations.Frame.route,
        ) {
            val viewModel =
                hiltViewModel<FrameViewModel>(
                    navController.getBackStackEntry(graphRoute),
                )
            FrameRoute(
                modifier = modifier,
                viewModel = viewModel,
                navigateToFrameDone = { navController.navigate(ScreenDestinations.FrameDone.route) },
                onErrorSnackBar = onErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.FrameDone.route,
        ) {
            val viewModel =
                hiltViewModel<FrameViewModel>(
                    navController.getBackStackEntry(graphRoute),
                )
            FrameDoneRoute(
                modifier = modifier,
                viewModel = viewModel,
                navigateToMoment = { navController.navigate(ScreenDestinations.Moment.route) },
            )
        }
    }
}
