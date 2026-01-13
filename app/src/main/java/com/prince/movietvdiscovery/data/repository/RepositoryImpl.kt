package com.prince.movietvdiscovery.data.repository

import com.prince.movietvdiscovery.data.remote.api.WatchmodeApi
import com.prince.movietvdiscovery.data.remote.mapper.TitleMapper
import com.prince.movietvdiscovery.domain.model.HomeContent
import com.prince.movietvdiscovery.domain.repository.Repository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.Singles

class RepositoryImpl (
    private val api: WatchmodeApi
): Repository {

    override fun fetchHomeContent(): Single<HomeContent> {

        val moviesSingle = api.getMovies()
            .map { response ->
                response.titles.map { dto ->
                    TitleMapper.toMovie(dto)
                }
            }

        val tvShowsSingle = api.getTvShows()
            .map { response ->
                response.titles.map { dto ->
                    TitleMapper.toTvShow(dto)
                }
            }


        return Singles.zip(
            moviesSingle,
            tvShowsSingle
        ){ movies, tvShows ->
            HomeContent(
                movies = movies,
                tvShows = tvShows
            )
        }

    }



}