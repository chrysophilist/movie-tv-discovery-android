package com.prince.movietvdiscovery.domain.repository

import com.prince.movietvdiscovery.domain.model.HomeContent
import com.prince.movietvdiscovery.domain.model.TitleDetails
import com.prince.movietvdiscovery.domain.util.Result
import io.reactivex.rxjava3.core.Single

interface Repository {

    fun fetchHomeContent(): Single<Result<HomeContent>>

    fun fetchTitleDetails(titleId: Int): Single<Result<TitleDetails>>
}