package com.prince.cinemon

import android.app.Application
import com.prince.cinemon.di.dataStoreModule
import com.prince.cinemon.di.networkModule
import com.prince.cinemon.di.repositoryModule
import com.prince.cinemon.di.viewModelModule
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