package com.prince.movietvdiscovery.data.remote.api

import com.prince.movietvdiscovery.BuildConfig
import com.prince.movietvdiscovery.data.local.prefs.ApiKeyDataStore
import com.prince.movietvdiscovery.domain.util.ApiKeyProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreApiKeyProvider (
    private val dataStore: ApiKeyDataStore
): ApiKeyProvider {

    override val apiKeyFlow: Flow<String?> =
        dataStore.apiKey.map { storedKey ->
            when {
                !storedKey.isNullOrEmpty() -> storedKey

                BuildConfig.DEBUG && BuildConfig.WATCHMODE_API_KEY.isNotBlank() -> BuildConfig.WATCHMODE_API_KEY

                else -> null
            }
        }

    override suspend fun getApiKey(): String? =
        runCatching { apiKeyFlow.first() }.getOrNull()


    override suspend fun hasApiKey(): Boolean =
        runCatching { getApiKey() != null }.getOrDefault(false)


    suspend fun saveApiKey(apiKey: String) {
        dataStore.saveApiKey(apiKey)
    }

    suspend fun clearApiKey(){
        dataStore.clearApiKey()
    }
}