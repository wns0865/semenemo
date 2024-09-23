package com.semonemo.presentation.navigation

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.semonemo.presentation.MainNavHost
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography

@Composable
fun MyBottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items =
        listOf(
            BottomNavItem.Shop,
            BottomNavItem.Auction,
            BottomNavItem.Wallet,
            BottomNavItem.MyPage,
        )

    Scaffold(
        bottomBar = {
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
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
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
        },
    ) { innerPadding ->
        MainNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = ScreenDestinations.Login.route,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyBottomNavigationPreview() {
    SemonemoTheme {
        val navController = rememberNavController()
        MyBottomNavigation(navController = navController)
    }
}
