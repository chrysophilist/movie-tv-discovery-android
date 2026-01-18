package com.prince.movietvdiscovery.di

import com.prince.movietvdiscovery.data.remote.api.DefaultApiKeyProvider
import com.prince.movietvdiscovery.data.repository.RepositoryImpl
import com.prince.movietvdiscovery.domain.repository.Repository
import com.prince.movietvdiscovery.domain.util.ApiKeyProvider
import com.prince.movietvdiscovery.domain.util.DefaultDispatcherProvider
import com.prince.movietvdiscovery.domain.util.DispatcherProvider
import org.koin.dsl.module

val repositoryModule = module {

    single<ApiKeyProvider> {
        DefaultApiKeyProvider()
    }

    single<Repository> {
        RepositoryImpl(
            api = get(),
            dispatcherProvider = get()
            )
    }

    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }
}