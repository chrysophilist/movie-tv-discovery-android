package com.prince.cinemon.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    object OnboardingScreen: Routes()

    @Serializable
    object ApiKeyOnboardingScreen: Routes()

    @Serializable
    object HomeScreen: Routes()

    @Serializable
    data class DetailsScreen(val titleId: Int): Routes()

    @Serializable
    object SettingsScreen: Routes()

    @Serializable
    object ApiKeyRoute: Routes()
}