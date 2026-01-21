package com.prince.cinemon.domain.model

data class TitleDetailsWithSources(
    val details: TitleDetails,
    val sources: List<StreamingSource>
)