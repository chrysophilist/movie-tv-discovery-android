package com.prince.cinemon.data.repository

import com.prince.cinemon.data.remote.api.WatchmodeApi
import com.prince.cinemon.data.remote.mapper.SourceMapper
import com.prince.cinemon.data.remote.mapper.TitleMapper
import com.prince.cinemon.data.remote.mapper.toAppError
import com.prince.cinemon.domain.model.ApiKeyStatus
import com.prince.cinemon.domain.model.HomeContent
import com.prince.cinemon.domain.model.TitleDetails
import com.prince.cinemon.domain.model.TitleDetailsWithSources
import com.prince.cinemon.domain.repository.Repository
import com.prince.cinemon.domain.util.ApiKeyProvider
import com.prince.cinemon.domain.util.AppError
import com.prince.cinemon.domain.util.DispatcherProvider
import com.prince.cinemon.domain.util.Result
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

            // For HomeScreen
            val apiKey = apiKeyProvider.getApiKey()
            if (apiKey.isNullOrBlank()){
                return@withContext Result.Error(AppError.MissingApiKey())
            }

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


    // For Setting's ApiKeyScreen and ApiKeyOnboardingScreen
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

                if (e is HttpException)  {
                    when(e.code()) {
                        401 -> ApiKeyStatus.Invalid
                        429 -> ApiKeyStatus.QuotaExceeded(
                            quota = 1000,
                            quotaUsed = 1000,
                            resetInDays = 30
                        )
                        else -> ApiKeyStatus.Error(
                            message = "Unable to validate API key"
                        )
                    }
                } else {
                    ApiKeyStatus.Error(
                        message = e.localizedMessage ?: "Unable to validate API key"
                    )
                }
            }
        }
}