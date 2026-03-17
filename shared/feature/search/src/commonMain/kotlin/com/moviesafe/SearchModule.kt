package com.moviesafe

import com.moviesafe.api.TmdbSearchService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchModule = module {
    single {
        TmdbSearchService(
            httpClient = get(),
            baseUrl = get(named("tmdb_base_url"))
        )
    }
}
