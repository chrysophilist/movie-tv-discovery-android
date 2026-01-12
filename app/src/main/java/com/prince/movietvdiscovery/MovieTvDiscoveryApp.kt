package com.prince.movietvdiscovery

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieTvDiscoveryApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MovieTvDiscoveryApp)
            modules(
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}