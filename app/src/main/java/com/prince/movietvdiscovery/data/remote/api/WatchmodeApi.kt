package com.prince.movietvdiscovery.data.remote.api

import com.prince.movietvdiscovery.BuildConfig
import com.prince.movietvdiscovery.data.remote.dto.ListTitlesResponseDto
import com.prince.movietvdiscovery.data.remote.dto.TitleDetailsDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WatchmodeApi {

    @GET("list-titles/")
    fun getMovies(
        @Query("apiKey") apiKey: String = BuildConfig.WATCHMODE_API_KEY,
        @Query("types") types: String = "movie",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Single<ListTitlesResponseDto>

    @GET("list-titles/")
    fun getTvShows(
        @Query("apiKey") apiKey: String = BuildConfig.WATCHMODE_API_KEY,
        @Query("types") types: String = "tv_series",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Single<ListTitlesResponseDto>


    @GET("title/{title_id}/details/")
    fun getTitleDetails(
        @Query("apiKey") apiKey: String = BuildConfig.WATCHMODE_API_KEY,
        @Query("titleId") titleId: Int
    ): Single<TitleDetailsDto>
}