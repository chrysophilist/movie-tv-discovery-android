package com.prince.movietvdiscovery.data.remote.dto.source

data class SourcesResponseItem(
    val android_url: String,
    val ios_url: String,
    val name: String,
    val type: String,
    val web_url: String,
    val region: String,
)