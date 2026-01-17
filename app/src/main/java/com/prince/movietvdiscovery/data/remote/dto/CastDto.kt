package com.prince.movietvdiscovery.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CastDto(

    @SerializedName("person_id")
    val personId: Int,

    @SerializedName("full_name")
    val name: String,

    @SerializedName("headshot_url")
    val imageUrl: String?,

    val role: String?,

    val type: String?
)