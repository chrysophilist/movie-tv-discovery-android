package com.prince.movietvdiscovery.domain.model

sealed class AppStartDestination {

    data object Onboarding: AppStartDestination()
    data object Home: AppStartDestination()
}