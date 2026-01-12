package com.prince.movietvdiscovery.di

import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

private const val BASE_URL = "https://api.watchmode.com/v1/"

val networkModule = module {


    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildC)
        }
    }
}