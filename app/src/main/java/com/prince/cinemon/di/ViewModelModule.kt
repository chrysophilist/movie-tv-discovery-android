package com.prince.cinemon.di

import com.prince.cinemon.ui.apikey.ApiKeyViewModel
import com.prince.cinemon.ui.details.DetailsViewModel
import com.prince.cinemon.ui.home.HomeViewModel
import com.prince.cinemon.ui.navigation.RootViewModel
import com.prince.cinemon.ui.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeViewModel(
            repo = get(),
            apiKeyProvider = get()
        )
    }

    viewModel {
        DetailsViewModel(
            repo = get()
        )
    }

    viewModel {
        ApiKeyViewModel(
            repo = get(),
            apiKeyProvider = get()
        )
    }

    viewModel {
        RootViewModel(
            onboardingDataStore = get()
        )
    }

    viewModel {
        OnboardingViewModel(
            onboardingDataStore = get()
        )
    }
}