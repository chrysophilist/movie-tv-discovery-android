package com.prince.movietvdiscovery.data.repository

import com.prince.movietvdiscovery.data.remote.api.WatchmodeApi
import com.prince.movietvdiscovery.data.remote.dto.TitleDetailsDto
import com.prince.movietvdiscovery.data.remote.mapper.SourceMapper
import com.prince.movietvdiscovery.data.remote.mapper.TitleMapper
import com.prince.movietvdiscovery.data.remote.mapper.TitleMapper.toTitleDetails
import com.prince.movietvdiscovery.data.remote.mapper.toAppError
import com.prince.movietvdiscovery.domain.model.HomeContent
import com.prince.movietvdiscovery.domain.model.TitleDetails
import com.prince.movietvdiscovery.domain.model.TitleDetailsWithSources
import com.prince.movietvdiscovery.domain.repository.Repository
import com.prince.movietvdiscovery.domain.util.Result
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.Singles

class RepositoryImpl (
    private val api: WatchmodeApi
): Repository {

    override fun fetchHomeContent(): Single<Result<HomeContent>> {

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
            .map<Result<HomeContent>> { homeContent ->
                Result.Success(homeContent)
            }
            .onErrorReturn { throwable ->
                Result.Error(throwable.toAppError())
            }

    }


    override fun fetchTitleDetailsWithSources(titleId: Int): Single<Result<TitleDetailsWithSources>> {

        val detailsSingle = api.getTitleDetails(titleId = titleId)
            .map { dto ->
                toTitleDetails(dto)
            }

        val sourcesSingle = api.getTitleSources(titleId = titleId)
            .map { response ->
                response.map { dto ->
                    SourceMapper.toStreamingSource(dto)
                }
            }


        return Singles.zip(
            detailsSingle,
            sourcesSingle
        ){ details, sources ->
            TitleDetailsWithSources(
                details = details,
                sources = sources
            )
        }
            .map<Result<TitleDetailsWithSources>> { it ->
                Result.Success(it)
            }
            .onErrorReturn { throwable ->
                Result.Error(throwable.toAppError())
            }
    }


}