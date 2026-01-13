package com.prince.movietvdiscovery.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TitleDetailsDto(

    val id: Int,
    val title: String,

    @SerializedName("plot_overview")
    val plotOverview: String?,

    val year: Int?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("poster")
    val posterUrl: String?
)