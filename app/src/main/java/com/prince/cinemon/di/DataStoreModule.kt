package com.prince.cinemon.di

import com.prince.cinemon.data.local.prefs.ApiKeyDataStore
import com.prince.cinemon.data.local.prefs.OnboardingDataStore
import com.prince.cinemon.data.remote.api.DataStoreApiKeyProvider
import com.prince.cinemon.domain.util.ApiKeyProvider
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

    single {
        OnboardingDataStore(androidContext())
    }
}