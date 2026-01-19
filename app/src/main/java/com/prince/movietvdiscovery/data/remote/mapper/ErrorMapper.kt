package com.prince.movietvdiscovery.data.remote.mapper

import com.prince.movietvdiscovery.domain.util.AppError
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toAppError(): AppError {
    return when (this) {
        is HttpException -> {
            when (code()) {
                400 -> AppError.BadRequest("Missing or invalid parameters")
                401 -> AppError.Unauthorized("Invalid API key")
                404 -> AppError.NotFound("Content not found")
                429 -> AppError.QuotaExceeded("Monthly API quota exhausted")
                else -> AppError.Unknown("Server error")
            }
        }

        is SocketTimeoutException ->
            AppError.Timeout("Request timeout. Please retry.")

        is UnknownHostException ->
            AppError.Network("Check your internet connection")

        else ->
            AppError.Unknown("Something went wrong")
    }
}
