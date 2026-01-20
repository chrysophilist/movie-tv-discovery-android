package com.prince.movietvdiscovery.di

import com.prince.movietvdiscovery.BuildConfig
import com.prince.movietvdiscovery.data.remote.api.ApiKeyInterceptor
import com.prince.movietvdiscovery.data.remote.api.WatchmodeApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.watchmode.com/v1/"

val networkModule = module {


    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }
    }

    single {
        ApiKeyInterceptor(
            apiKeyProvider = get()
        )
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<ApiKeyInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<WatchmodeApi> {
        get<Retrofit>().create(WatchmodeApi::class.java)
    }
}