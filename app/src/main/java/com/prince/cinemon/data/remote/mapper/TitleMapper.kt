package com.prince.cinemon.data.remote.mapper

import com.prince.cinemon.data.remote.dto.TitleDetailsDto
import com.prince.cinemon.data.remote.dto.TitleDto
import com.prince.cinemon.domain.model.Credit
import com.prince.cinemon.domain.model.CreditType
import com.prince.cinemon.domain.model.Movie
import com.prince.cinemon.domain.model.TitleDetails
import com.prince.cinemon.domain.model.TvShow

object TitleMapper {

    fun toMovie(dto: TitleDto): Movie =
        Movie(
            id = dto.id,
            title = dto.title,
            year = dto.year,
            type = dto.type,
        )

    fun toTvShow(dto: TitleDto): TvShow =
        TvShow(
            id = dto.id,
            title = dto.title,
            year = dto.year,
            type = dto.type,
        )

    fun toTitleDetails(dto: TitleDetailsDto): TitleDetails =
        TitleDetails(
            id = dto.id,
            title = dto.title,
            description = dto.plotOverview,
            year = dto.year,
            releaseDate = dto.releaseDate,
            posterUrl = dto.posterUrl,
            backdrop = dto.backdrop,
            runtimeMinutes = dto.runtimeMinutes,
            genres = dto.genreNames ?: emptyList(),
            cast = dto.cast?.map { castDto ->
                Credit(
                    id = castDto.personId,
                    name = castDto.name,
                    role = castDto.role,
                    imageUrl = castDto.imageUrl,
                    type = mapCreditType(castDto.type)
                )
            } ?: emptyList()
        )
}

private fun mapCreditType(type: String?): CreditType =
    when (type?.lowercase()) {
        "cast" -> CreditType.CAST
        "crew" -> CreditType.CREW
        else -> CreditType.CREW
    }
