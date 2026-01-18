package com.prince.movietvdiscovery.data.remote.api

import com.prince.movietvdiscovery.BuildConfig
import com.prince.movietvdiscovery.domain.util.ApiKeyProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultApiKeyProvider : ApiKeyProvider {

    private val apiKeyState = MutableStateFlow(
        BuildConfig.WATCHMODE_API_KEY.takeIf { it.isNotBlank() }
    )

    override fun getApiKey(): String? = apiKeyState.value

    override fun hasApiKey(): Boolean = !apiKeyState.value.isNullOrEmpty()

    override val apiKeyFlow: Flow<String?> = apiKeyState
}