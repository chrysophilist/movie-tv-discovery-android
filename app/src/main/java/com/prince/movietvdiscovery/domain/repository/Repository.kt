package com.prince.movietvdiscovery.domain.repository

import com.prince.movietvdiscovery.domain.model.HomeContent
import com.prince.movietvdiscovery.domain.model.TitleDetails
import com.prince.movietvdiscovery.domain.model.TitleDetailsWithSources
import com.prince.movietvdiscovery.domain.util.Result
import io.reactivex.rxjava3.core.Single

interface Repository {

    suspend fun fetchHomeContent(): Result<HomeContent>

    suspend fun fetchTitleDetails(titleId: Int): Result<TitleDetails>

    suspend fun fetchTitleDetailsWithSources(titleId: Int): Result<TitleDetailsWithSources>
}