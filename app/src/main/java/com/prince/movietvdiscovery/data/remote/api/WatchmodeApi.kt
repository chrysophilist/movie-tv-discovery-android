package com.prince.movietvdiscovery.data.remote.api

import com.prince.movietvdiscovery.data.remote.dto.ApiStatusDto
import com.prince.movietvdiscovery.data.remote.dto.ListTitlesResponseDto
import com.prince.movietvdiscovery.data.remote.dto.TitleDetailsDto
import com.prince.movietvdiscovery.data.remote.dto.source.SourcesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WatchmodeApi {

    @GET("list-titles/")
    suspend fun getMovies(
        @Query("types") types: String = "movie",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): ListTitlesResponseDto

    @GET("list-titles/")
    suspend fun getTvShows(
        @Query("types") types: String = "tv_series",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): ListTitlesResponseDto


    @GET("title/{title_id}/details/")
    suspend fun getTitleDetails(
        @Path("title_id") titleId: Int,
        @Query("append_to_response") append: String = "cast-crew"
    ): TitleDetailsDto


    @GET("title/{title_id}/sources/")
    suspend fun getTitleSources(
        @Path("title_id") titleId: Int,
    ): SourcesResponse


    @GET("status/")
    suspend fun getApiStatus(): ApiStatusDto
}