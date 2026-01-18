package com.prince.movietvdiscovery.domain.model

sealed class ApiKeyStatus {

    data class Valid(
        val quota: Int,
        val quotaUsed: Int,
        val quotaRemaining: Int
    ): ApiKeyStatus()

    data object Invalid: ApiKeyStatus()

    data object Missing: ApiKeyStatus()

    data class Error(
        val message: String
    ): ApiKeyStatus()
}