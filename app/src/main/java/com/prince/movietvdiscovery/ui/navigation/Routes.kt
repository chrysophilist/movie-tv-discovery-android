package com.prince.movietvdiscovery.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    object OnboardingScreen: Routes()

    @Serializable
    object HomeScreen: Routes()

    @Serializable
    data class DetailsScreen(val titleId: Int): Routes()
}