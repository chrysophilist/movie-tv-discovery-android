package com.prince.cinemon.data.remote.dto

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
    val posterUrl: String?,

    val backdrop: String?,

    @SerializedName("runtime_minutes")
    val runtimeMinutes: Int?,

    @SerializedName("genre_names")
    val genreNames: List<String>?,

    @SerializedName("cast")
    val cast: List<CastDto>?
)