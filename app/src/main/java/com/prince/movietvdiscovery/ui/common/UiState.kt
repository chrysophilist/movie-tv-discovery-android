package com.prince.movietvdiscovery.ui.common

import com.prince.movietvdiscovery.domain.util.AppError

sealed class UiState<out T> {

    data object Loading: UiState<Nothing>()

    data class Success<T>(val data: T): UiState<T>()

    data class Error(val message: AppError): UiState<Nothing>()
}