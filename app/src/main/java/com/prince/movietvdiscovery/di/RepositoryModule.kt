package com.prince.movietvdiscovery.di

import com.prince.movietvdiscovery.data.repository.RepositoryImpl
import com.prince.movietvdiscovery.domain.repository.Repository
import org.koin.dsl.module

val repositoryModule = module {

    single<Repository> {
        RepositoryImpl(get())
    }
}