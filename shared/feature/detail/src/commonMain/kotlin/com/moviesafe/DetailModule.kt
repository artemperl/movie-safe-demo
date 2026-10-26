package com.moviesafe

import com.moviesafe.api.TmdbDetailService
import com.moviesafe.api.TmdbDetailServiceImpl
import com.moviesafe.model.MovieDetail
import com.moviesafe.repository.DetailRepository
import com.moviesafe.repository.DetailRepositoryImpl
import com.moviesafe.ui.DetailViewModel
import com.moviesafe.usecase.GetMovieDetailUseCase
import io.ktor.http.parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.builtins.ListSerializer
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val detailModule = module {
    includes(networkModule)

    single<TmdbDetailService> {
        TmdbDetailServiceImpl(
            httpClient = get(),
            baseUrl = get(named("tmdb_base_url")),
        )
    }

    single<MSDataStore<MovieDetail>>(named("detail_store")) {
        PersistenceDataStore(
            serializer = MovieDetail.serializer(),
            filename = "movies_detail",
        )
    }

    single<DetailRepository> {
        DetailRepositoryImpl(
            coroutineDispatcher = Dispatchers.IO,
            detailService = get(),
            detailDataStore = get(named("detail_store")),
            posterBaseUrl = get(named("tmdb_poster_base_url")),
        )
    }

    factory {
        GetMovieDetailUseCase(
            detailRepository = get()
        )
    }

    viewModel { params ->
        DetailViewModel(
            movieId = params.get(),
            getMovieDetailUseCase = get()
        )
    }
}
