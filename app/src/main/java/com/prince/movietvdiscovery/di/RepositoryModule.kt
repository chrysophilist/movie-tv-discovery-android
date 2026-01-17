package com.prince.movietvdiscovery.di

import com.prince.movietvdiscovery.data.repository.RepositoryImpl
import com.prince.movietvdiscovery.domain.repository.Repository
import com.prince.movietvdiscovery.domain.util.DefaultDispatcherProvider
import com.prince.movietvdiscovery.domain.util.DispatcherProvider
import org.koin.dsl.module

val repositoryModule = module {

    single<Repository> {
        RepositoryImpl(get())
    }

    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }
}