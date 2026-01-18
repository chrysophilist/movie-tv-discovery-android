package com.prince.movietvdiscovery.ui.apikey

import com.prince.movietvdiscovery.domain.model.ApiKeyStatus

data class ApiKeyUiState(
    val isLoading: Boolean = false,
    val currentKey: String? = null,
    val status: ApiKeyStatus = ApiKeyStatus.Missing
)