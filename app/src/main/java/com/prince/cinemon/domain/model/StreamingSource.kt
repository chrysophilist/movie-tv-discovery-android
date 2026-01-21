package com.prince.cinemon.domain.model

data class StreamingSource(
    val name: String,
    val webUrl: String?,
    val androidUrl: String?,
    val iosUrl: String?,
    val type: StreamingType,
    val region: String,
)

enum class StreamingType {
    SUBSCRIPTION,
    RENT,
    BUY,
    FREE,
    TV,
    UNKNOWN
}
