package com.moviesafe

import com.moviesafe.api.TmdbSearchService
import com.moviesafe.model.Movie
import com.moviesafe.model.TvSeries
import com.moviesafe.repository.SearchRepository
import com.moviesafe.repository.SearchRepositoryImpl
import kotlinx.serialization.builtins.ListSerializer
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchModule = module {
    single {
        TmdbSearchService(
            httpClient = get(),
            baseUrl = get(named("tmdb_base_url"))
        )
    }

    single(named("movies_store")) {
        PersistenceDataStore(
            serializer = ListSerializer(Movie.serializer()),
            filename = "movies_search",
        )
    }

    single(named("tv_series_store")) {
        PersistenceDataStore(
            serializer = ListSerializer(TvSeries.serializer()),
            filename = "tv_series_search",
        )
    }

    single<SearchRepository> {
        SearchRepositoryImpl(
            searchService = get(),
            moviesDataStore = get(named("movies_store")),
            tvSeriesDataStore = get(named("tv_series_store")),
        )
    }
}
