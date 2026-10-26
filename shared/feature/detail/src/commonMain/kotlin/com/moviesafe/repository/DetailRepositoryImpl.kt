package com.moviesafe.repository

import com.moviesafe.MSDataStore
import com.moviesafe.api.TmdbDetailService
import com.moviesafe.mapper.toMovieDetail
import com.moviesafe.model.MovieDetail
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DetailRepositoryImpl(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val detailService: TmdbDetailService,
    private val detailDataStore: MSDataStore<MovieDetail>,
    private val posterBaseUrl: String,
) : DetailRepository {

    override suspend fun getDetail(movieId: String): Result<MovieDetail> =
        withContext(coroutineDispatcher) {
            detailService.fetchMovie(
                movieId = movieId
            ).mapCatching { movieDetailDto ->
                val movie = movieDetailDto.toMovieDetail(posterBaseUrl)
                detailDataStore.updateData {
                    movie
                }.map {
                    movie
                }.getOrThrow()
            }
        }

    override suspend fun clearDetail(): Result<Unit> = withContext(coroutineDispatcher) {
        detailDataStore.updateData { null }.map { }
    }


}