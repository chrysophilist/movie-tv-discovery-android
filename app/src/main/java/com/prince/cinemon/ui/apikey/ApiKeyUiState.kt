package com.prince.cinemon.ui.apikey

import com.prince.cinemon.domain.model.ApiKeyStatus

data class ApiKeyUiState(
    val isLoading: Boolean = false,
    val currentKey: String? = null,
    val status: ApiKeyStatus = ApiKeyStatus.Missing
)