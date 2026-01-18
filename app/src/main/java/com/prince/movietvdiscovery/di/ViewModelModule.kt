package com.prince.movietvdiscovery.di

import com.prince.movietvdiscovery.ui.apikey.ApiKeyViewModel
import com.prince.movietvdiscovery.ui.details.DetailsViewModel
import com.prince.movietvdiscovery.ui.home.HomeViewModel
import com.prince.movietvdiscovery.ui.navigation.RootViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeViewModel(
            repo = get()
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
}