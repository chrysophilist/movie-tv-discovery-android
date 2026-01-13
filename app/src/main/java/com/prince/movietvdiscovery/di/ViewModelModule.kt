package com.prince.movietvdiscovery.di

import com.prince.movietvdiscovery.ui.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeViewModel(
            repo = get()
        )
    }
}