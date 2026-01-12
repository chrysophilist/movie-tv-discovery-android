package com.prince.movietvdiscovery.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val year: Int?,
    val type: String
)