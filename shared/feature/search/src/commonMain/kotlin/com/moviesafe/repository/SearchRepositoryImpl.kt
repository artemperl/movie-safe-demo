package com.moviesafe.repository

import com.moviesafe.PersistenceDataStore
import com.moviesafe.api.TmdbSearchService
import com.moviesafe.dto.MovieSearchResultDto
import com.moviesafe.dto.TvSeriesSearchResultDto
import com.moviesafe.mapper.toDomain
import com.moviesafe.model.Movie
import com.moviesafe.model.PageInformation
import com.moviesafe.model.TvSeries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class SearchRepositoryImpl(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val searchService: TmdbSearchService,
    private val moviesDataStore: PersistenceDataStore<List<Movie>>,
) : SearchRepository {

    override fun onMovies(): Flow<List<Movie>?> {
        return moviesDataStore.data.flowOn(coroutineDispatcher)
    }

    override suspend fun searchMovies(
        query: String,
        accessToken: String,
        page: Int,
    ): Result<PageInformation> {
        val pagedResponse = searchService.searchMovies(query, accessToken, page)
            .getOrElse { error ->
                return Result.failure(error)
            }

        val result = pagedResponse.results.map { it.toDomain() }

        result.let { movies ->
            moviesDataStore.updateData { currentMovies ->
                currentMovies.orEmpty() + movies
            }
        }

        return Result.success(
            PageInformation(
                currentPage = pagedResponse.page,
                totalPages = pagedResponse.totalPages
            )
        )
    }

}