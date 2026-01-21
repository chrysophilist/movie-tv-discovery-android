package com.prince.cinemon.domain.model

sealed class AppStartDestination {

    data object Onboarding: AppStartDestination()
    data object Home: AppStartDestination()
}