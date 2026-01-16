package com.prince.movietvdiscovery.data.remote.mapper

import com.prince.movietvdiscovery.data.remote.dto.TitleDto
import com.prince.movietvdiscovery.data.remote.dto.source.SourcesResponseItem
import com.prince.movietvdiscovery.domain.model.Movie
import com.prince.movietvdiscovery.domain.model.StreamingSource
import com.prince.movietvdiscovery.domain.model.StreamingType
import java.util.Locale

fun toStreamingSource(dto: SourcesResponseItem): StreamingSource =
    StreamingSource(
        name = dto.name,
        region = dto.region,
        webUrl = dto.web_url,
        androidUrl = dto.android_url,
        iosUrl = dto.ios_url,
        type = when(dto.type.lowercase(Locale.US)){
            "sub" -> StreamingType.SUBSCRIPTION
            "rent" -> StreamingType.RENT
            "buy" -> StreamingType.BUY
            "free" -> StreamingType.FREE
            "tve" -> StreamingType.TV
            else -> StreamingType.UNKNOWN
        }
    )

fun toMovie(dto: TitleDto): Movie =
    Movie(
        id = dto.id,
        title = dto.title,
        year = dto.year,
        type = dto.type,
    )