package com.prince.movietvdiscovery.domain.model

data class TitleDetails(
    val id: Int,
    val title: String,
    val description: String?,
    val year: Int?,
    val releaseDate: String,
    val posterUrl: String?
)
