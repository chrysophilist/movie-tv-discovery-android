package com.prince.movietvdiscovery.di

import com.prince.movietvdiscovery.data.local.prefs.ApiKeyDataStore
import com.prince.movietvdiscovery.data.remote.api.DataStoreApiKeyProvider
import com.prince.movietvdiscovery.domain.util.ApiKeyProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {

    single {
        ApiKeyDataStore(
            context = androidContext()
        )
    }

    single<ApiKeyProvider> {
        DataStoreApiKeyProvider(get())
    }
}