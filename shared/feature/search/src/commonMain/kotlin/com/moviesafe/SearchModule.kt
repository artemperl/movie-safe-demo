package com.moviesafe

import com.moviesafe.api.TmdbSearchService
import com.moviesafe.model.Movie
import com.moviesafe.repository.SearchRepository
import com.moviesafe.repository.SearchRepositoryImpl
import com.moviesafe.usecase.ObserveMovies
import com.moviesafe.usecase.SearchMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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

    single<SearchRepository> {
        SearchRepositoryImpl(
            coroutineDispatcher = Dispatchers.IO,
            searchService = get(),
            moviesDataStore = get(named("movies_store")),
        )
    }

    factory { ObserveMovies(get()) }
    factory { SearchMovies(get()) }
}
