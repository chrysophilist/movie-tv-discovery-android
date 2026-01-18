package com.prince.movietvdiscovery.domain.util

import kotlinx.coroutines.flow.Flow

interface ApiKeyProvider {

    fun getApiKey(): String?

    fun hasApiKey(): Boolean

    val apiKeyFlow: Flow<String?>
}