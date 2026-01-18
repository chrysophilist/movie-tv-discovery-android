package com.prince.movietvdiscovery.domain.util

import kotlinx.coroutines.flow.Flow

interface ApiKeyProvider {

    suspend fun getApiKey(): String?

    suspend fun hasApiKey(): Boolean

    val apiKeyFlow: Flow<String?>
}