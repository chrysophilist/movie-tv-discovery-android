package com.prince.cinemon.di

import com.prince.cinemon.data.repository.RepositoryImpl
import com.prince.cinemon.domain.repository.Repository
import com.prince.cinemon.domain.util.DefaultDispatcherProvider
import com.prince.cinemon.domain.util.DispatcherProvider
import org.koin.dsl.module

val repositoryModule = module {

    single<Repository> {
        RepositoryImpl(
            api = get(),
            dispatcherProvider = get(),
            apiKeyProvider = get()
            )
    }

    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }
}