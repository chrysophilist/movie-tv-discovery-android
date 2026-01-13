package com.prince.movietvdiscovery.domain.repository

import com.prince.movietvdiscovery.domain.model.HomeContent
import com.prince.movietvdiscovery.domain.model.TitleDetails
import io.reactivex.rxjava3.core.Single

interface Repository {

    fun fetchHomeContent(): Single<HomeContent>

    fun fetchTitleDetails(titleId: Int): Single<TitleDetails>
}