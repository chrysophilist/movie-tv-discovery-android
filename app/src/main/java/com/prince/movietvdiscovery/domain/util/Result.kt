package com.prince.movietvdiscovery.domain.util

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val error: AppError) : Result<Nothing>()
}
