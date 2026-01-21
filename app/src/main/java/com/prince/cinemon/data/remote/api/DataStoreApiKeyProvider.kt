package com.prince.cinemon.data.remote.api

import com.prince.cinemon.BuildConfig
import com.prince.cinemon.data.local.prefs.ApiKeyDataStore
import com.prince.cinemon.domain.util.ApiKeyProvider
import kotlinx.coroutines.flow.Flow
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


    override suspend fun saveApiKey(apiKey: String) {
        dataStore.saveApiKey(apiKey)
    }

    override suspend fun clearApiKey(){
        dataStore.clearApiKey()
    }
}