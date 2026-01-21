package com.prince.movietvdiscovery

import android.app.Application
import com.prince.movietvdiscovery.di.dataStoreModule
import com.prince.movietvdiscovery.di.networkModule
import com.prince.movietvdiscovery.di.repositoryModule
import com.prince.movietvdiscovery.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieTvDiscoveryApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MovieTvDiscoveryApp)
            modules(
                dataStoreModule,
                networkModule,
                repositoryModule,
                viewModelModule,
            )
        }
    }
}