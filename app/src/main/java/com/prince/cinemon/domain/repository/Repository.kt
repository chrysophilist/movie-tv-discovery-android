package com.prince.cinemon.domain.repository

import com.prince.cinemon.domain.model.ApiKeyStatus
import com.prince.cinemon.domain.model.HomeContent
import com.prince.cinemon.domain.model.TitleDetails
import com.prince.cinemon.domain.model.TitleDetailsWithSources
import com.prince.cinemon.domain.util.Result

interface Repository {

    suspend fun fetchHomeContent(): Result<HomeContent>

    suspend fun fetchTitleDetails(titleId: Int): Result<TitleDetails>

    suspend fun fetchTitleDetailsWithSources(titleId: Int): Result<TitleDetailsWithSources>

    suspend fun validateApiKey(): ApiKeyStatus
}