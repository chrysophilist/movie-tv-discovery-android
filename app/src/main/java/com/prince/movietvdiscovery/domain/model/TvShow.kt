package com.prince.movietvdiscovery.domain.model

data class TvShow(
    val id: Int,
    val title: String,
    val year: Int?,
    val type: String?
)