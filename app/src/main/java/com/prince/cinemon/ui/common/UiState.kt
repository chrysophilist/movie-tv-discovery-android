package com.prince.cinemon.ui.common

import com.prince.cinemon.domain.util.AppError

sealed class UiState<out T> {

    data object Loading: UiState<Nothing>()

    data class Success<T>(val data: T): UiState<T>()

    data class Error(val message: AppError): UiState<Nothing>()
}