package com.prince.movietvdiscovery.data.repository

import com.prince.movietvdiscovery.data.remote.api.WatchmodeApi
import com.prince.movietvdiscovery.data.remote.mapper.SourceMapper
import com.prince.movietvdiscovery.data.remote.mapper.TitleMapper
import com.prince.movietvdiscovery.data.remote.mapper.toAppError
import com.prince.movietvdiscovery.domain.model.ApiKeyStatus
import com.prince.movietvdiscovery.domain.model.HomeContent
import com.prince.movietvdiscovery.domain.model.TitleDetails
import com.prince.movietvdiscovery.domain.model.TitleDetailsWithSources
import com.prince.movietvdiscovery.domain.repository.Repository
import com.prince.movietvdiscovery.domain.util.ApiKeyProvider
import com.prince.movietvdiscovery.domain.util.DispatcherProvider
import com.prince.movietvdiscovery.domain.util.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RepositoryImpl (
    private val api: WatchmodeApi,
    private val dispatcherProvider: DispatcherProvider,
    private val apiKeyProvider: ApiKeyProvider
): Repository {

    override suspend fun fetchHomeContent(): Result<HomeContent> =
        withContext(dispatcherProvider.io) {
            try {
                coroutineScope {
                    val movieDeferred = async {
                        api.getMovies().titles.map { dto ->
                            TitleMapper.toMovie(dto)
                        }
                    }

                    val tvShowDeferred = async {
                        api.getTvShows().titles.map { dto ->
                            TitleMapper.toTvShow(dto)
                        }
                    }

                    val homeContent = HomeContent(
                        movies = movieDeferred.await(),
                        tvShows = tvShowDeferred.await()
                    )

                    Result.Success(homeContent)
                }
            } catch (e: Exception) {
                Result.Error(e.toAppError())
            }
        }


    override suspend fun fetchTitleDetails(titleId: Int): Result<TitleDetails> =
        withContext(dispatcherProvider.io) {
            try {
                val details = api.getTitleDetails(titleId)
                Result.Success(TitleMapper.toTitleDetails(details))
            } catch (e: Exception){
                Result.Error(e.toAppError())
            }
        }


    override suspend fun fetchTitleDetailsWithSources(titleId: Int): Result<TitleDetailsWithSources> =
        withContext(dispatcherProvider.io) {
            try {
                coroutineScope {
                    val detailsDeferred = async {
                        TitleMapper.toTitleDetails(api.getTitleDetails(titleId))
                    }

                    val sourcesDeferred = async {
                        api.getTitleSources(titleId).map { sourcesResponseItem ->
                            SourceMapper.toStreamingSource(sourcesResponseItem)
                        }
                    }

                    Result.Success(
                        TitleDetailsWithSources(
                            details = detailsDeferred.await(),
                            sources = sourcesDeferred.await()
                        )
                    )
                }
            } catch (e: Exception){
                Result.Error(e.toAppError())
            }
        }


    override suspend fun validateApiKey(): ApiKeyStatus =
        withContext(dispatcherProvider.io) {

            val apiKey = apiKeyProvider.getApiKey()

            if (apiKey.isNullOrBlank()) {
                return@withContext ApiKeyStatus.Missing
            }

            try {
                val status = api.getApiStatus()

                ApiKeyStatus.Valid(
                    quota = status.quota,
                    quotaUsed = status.quotaUsed,
                    quotaRemaining = status.quota - status.quotaUsed
                )
            } catch (e: Exception) {

                if (e is HttpException && e.code() == 401) {
                    ApiKeyStatus.Invalid
                } else {
                    ApiKeyStatus.Error(
                        message = e.localizedMessage ?: "Unable to validate API key"
                    )
                }
            }
        }
}