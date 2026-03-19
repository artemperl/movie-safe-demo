package com.moviesafe.repository

import com.moviesafe.MSDataStore
import com.moviesafe.api.TmdbSearchService
import com.moviesafe.mapper.toDomain
import com.moviesafe.model.Movie
import com.moviesafe.model.PageInformation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class SearchRepositoryImpl(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val searchService: TmdbSearchService,
    private val moviesDataStore: MSDataStore<List<Movie>>,
    private val posterBaseUrl: String,
) : SearchRepository {

    override fun onMovies(): Flow<List<Movie>?> {
        return moviesDataStore.data.flowOn(coroutineDispatcher)
    }

    override suspend fun searchMovies(
        query: String,
        page: Int,
    ): Result<PageInformation> {
        val pagedResponse = searchService.searchMovies(query, page)
            .getOrElse { error ->
                return Result.failure(error)
            }

        val result = pagedResponse.results.map { it.toDomain(posterBaseUrl) }

        result.let { movies ->
            moviesDataStore.updateData { currentMovies ->
                currentMovies.orEmpty() + movies
            }.onFailure { error ->
                return Result.failure(error)
            }
        }

        return Result.success(
            PageInformation(
                currentPage = pagedResponse.page,
                totalPages = pagedResponse.totalPages
            )
        )
    }

    override suspend fun clearMovies(): Result<Unit> {
        return moviesDataStore.updateData { null }
    }

}