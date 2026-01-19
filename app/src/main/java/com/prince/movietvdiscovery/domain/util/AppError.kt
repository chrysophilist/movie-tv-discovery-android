package com.prince.movietvdiscovery.domain.util

sealed class AppError(
    open val message: String
) {
    data class BadRequest(
        override val message: String = "Something went wrong"
    ) : AppError(message)

    data class Unauthorized(
        override val message: String = "Invalid API key"
    ) : AppError(message)

    data class NotFound(
        override val message: String = "Content not found"
    ) : AppError(message)

    data class Network(
        override val message: String = "No internet connection"
    ) : AppError(message)

    data class Timeout(
        override val message: String = "Request timed out"
    ) : AppError(message)

    data class Unknown(
        override val message: String = "Unexpected error occurred"
    ) : AppError(message)

    data class MissingApiKey(
        override val message: String = "API key not added"
    ) : AppError(message)

    data class QuotaExceeded(
        override val message: String = "Monthly API quota exhausted"
    ): AppError(message)
}
