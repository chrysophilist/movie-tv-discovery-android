package com.prince.movietvdiscovery.domain.model

data class Credit(
    val id: Int,
    val name: String,
    val role: String?,
    val imageUrl: String?,
    val type: CreditType
)

enum class CreditType {
    CAST,
    CREW
}
