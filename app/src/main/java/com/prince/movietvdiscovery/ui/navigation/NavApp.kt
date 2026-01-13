package com.prince.movietvdiscovery.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.prince.movietvdiscovery.ui.details.DetailsScreen
import com.prince.movietvdiscovery.ui.home.HomeScreen

@Composable
fun NavApp() {

    val navController = rememberNavController()

    Scaffold(
        topBar = {},
        bottomBar = {},
        floatingActionButton = {}
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Routes.HomeScreen,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable<Routes.HomeScreen>{
                HomeScreen()
            }

            composable<Routes.DetailsScreen> { backStackEntry ->
                val args = backStackEntry.toRoute<Routes.DetailsScreen>()
                DetailsScreen(
                    titleId = args.titleId
                )
            }

        }
    }
}