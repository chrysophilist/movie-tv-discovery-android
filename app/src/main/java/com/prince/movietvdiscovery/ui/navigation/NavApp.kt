package com.prince.movietvdiscovery.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.prince.movietvdiscovery.domain.model.AppStartDestination
import com.prince.movietvdiscovery.ui.apikey.ApiKeyRoute
import com.prince.movietvdiscovery.ui.apikey.ApiKeyViewModel
import com.prince.movietvdiscovery.ui.details.DetailsScreen
import com.prince.movietvdiscovery.ui.home.HomeScreen
import com.prince.movietvdiscovery.ui.onboarding.ApiKeyOnboardingScreen
import com.prince.movietvdiscovery.ui.onboarding.OnboardingScreen
import com.prince.movietvdiscovery.ui.onboarding.OnboardingViewModel
import com.prince.movietvdiscovery.ui.settings.SettingsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavApp() {

    val rootViewModel: RootViewModel = koinViewModel()
    val startDestination by rootViewModel.startDestination.collectAsStateWithLifecycle()

    val onboardingViewModel: OnboardingViewModel = koinViewModel()
    val apiKeyViewModel: ApiKeyViewModel = koinViewModel()

    val navController = rememberNavController()

    var scaffoldState by remember { mutableStateOf(AppScaffoldState()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackbar: suspend (String) -> Unit = { message ->
        snackbarHostState.showSnackbar(message)
    }

    if (startDestination == null) return

    Scaffold(
        topBar = {
            scaffoldState.topBar?.invoke()
        },
        bottomBar = {
            scaffoldState.bottomBar?.invoke()
        },
        floatingActionButton = {
            scaffoldState.floatingActionButton?.invoke()
        },
        containerColor = scaffoldState.containerColor ?: MaterialTheme.colorScheme.background,
        floatingActionButtonPosition = scaffoldState.fabPosition ?: FabPosition.End,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = when(startDestination) {
                AppStartDestination.Onboarding -> Routes.OnboardingScreen
                AppStartDestination.Home -> Routes.HomeScreen
                else -> Routes.HomeScreen
            },
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (scaffoldState.applyContentPadding) Modifier.padding(paddingValues)
                    else Modifier
                )
        ) {

            composable<Routes.OnboardingScreen>{
                OnboardingScreen(
                    onSkip = {
                        onboardingViewModel.completeOnboarding()
                        navController.navigate(Routes.HomeScreen) {
                            popUpTo(Routes.OnboardingScreen) { inclusive = true }
                        }
                    },
                    onContinue = {
                        navController.navigate(Routes.ApiKeyOnboardingScreen)
                    }
                )
            }

            composable<Routes.ApiKeyOnboardingScreen>{
                val apiKeyState by apiKeyViewModel.uiState.collectAsStateWithLifecycle()

                ApiKeyOnboardingScreen(
                    state = apiKeyState,
                    onSkip = {
                        onboardingViewModel.completeOnboarding()
                        navController.navigate(Routes.HomeScreen) {
                            popUpTo(Routes.OnboardingScreen) { inclusive = true }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                    onSuccess = {
                        onboardingViewModel.completeOnboarding()
                        navController.navigate(Routes.HomeScreen) {
                            popUpTo(Routes.OnboardingScreen) { inclusive = true }
                        }
                    },
                    onValidate = { key ->
                        apiKeyViewModel.saveApiKey(key)
                        apiKeyViewModel.validateKey()
                    }
                )
            }

            composable<Routes.HomeScreen>{
                HomeScreen(
                    onClick = { titleId ->
                        navController.navigate(Routes.DetailsScreen(titleId))
                    },
                    setScaffoldState = { scaffoldState = it },
                    showSnackbar = showSnackbar,
                    onNavigateToSettings = {
                        navController.navigate(Routes.ApiKeyRoute)
                    },
                    onOpenSettings = {
                        navController.navigate(Routes.SettingsScreen)
                    }
                )
            }

            composable<Routes.DetailsScreen> { backStackEntry ->
                val args = backStackEntry.toRoute<Routes.DetailsScreen>()
                DetailsScreen(
                    titleId = args.titleId,
                    setScaffoldState = { scaffoldState = it },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Routes.SettingsScreen> {
                SettingsScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navigateToApiKey = {
                        navController.navigate(Routes.ApiKeyRoute)
                    },
                    setScaffoldState = { scaffoldState = it }
                )
            }

            composable<Routes.ApiKeyRoute>{
                ApiKeyRoute(
                    setScaffoldState = { scaffoldState = it },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

        }
    }
}


data class AppScaffoldState(
    val topBar: (@Composable () -> Unit)? = null,
    val floatingActionButton: (@Composable () -> Unit)? = null,
    val bottomBar: (@Composable () -> Unit)? = null,
    val containerColor: Color? = null,
    val applyContentPadding: Boolean = true,
    val fabPosition: FabPosition? = null
)
