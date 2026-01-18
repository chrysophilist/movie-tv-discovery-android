package com.prince.movietvdiscovery

import android.app.Application
import com.prince.movietvdiscovery.di.dataStoreModule
import com.prince.movietvdiscovery.di.networkModule
import com.prince.movietvdiscovery.di.repositoryModule
import com.prince.movietvdiscovery.di.viewModelModule
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.io.IOException

class MovieTvDiscoveryApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MovieTvDiscoveryApp)
            modules(
                networkModule,
                repositoryModule,
                viewModelModule,
                dataStoreModule
            )
        }
    }
}