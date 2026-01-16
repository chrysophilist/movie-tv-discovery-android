package com.prince.movietvdiscovery.domain.model

data class TitleDetailsWithSources(
    val details: TitleDetails,
    val sources: List<StreamingSource>
)