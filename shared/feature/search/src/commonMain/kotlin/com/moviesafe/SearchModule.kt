package com.moviesafe

import com.moviesafe.api.TmdbSearchService
import com.moviesafe.api.TmdbSearchServiceImpl
import com.moviesafe.model.Movie
import com.moviesafe.repository.SearchRepository
import com.moviesafe.repository.SearchRepositoryImpl
import com.moviesafe.ui.SearchViewModel
import com.moviesafe.usecase.ClearMoviesUseCase
import com.moviesafe.usecase.ObserveMoviesUseCase
import com.moviesafe.usecase.SearchMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.builtins.ListSerializer
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchModule = module {
    includes(networkModule)

    single<TmdbSearchService> {
        TmdbSearchServiceImpl(
            httpClient = get(),
            baseUrl = get(named("tmdb_base_url")),
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
            posterBaseUrl = get(named("tmdb_poster_base_url")),
        )
    }

    factory { ObserveMoviesUseCase(get()) }

    factory { SearchMoviesUseCase(get()) }

    factory { ClearMoviesUseCase(get()) }

    viewModel {
        SearchViewModel(
            searchMoviesUseCase = get(),
            observeMoviesUseCase = get(),
            clearMoviesUseCase = get(),
            accessToken = get(named("tmdb_access_token")),
        )
    }
}
