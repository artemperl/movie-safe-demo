package com.moviesafe.repository

import com.moviesafe.model.Movie
import com.moviesafe.model.PageInformation
import com.moviesafe.model.TvSeries
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun onMovies(): Flow<List<Movie>?>

    suspend fun searchMovies(
        query: String,
        page: Int = 1,
    ): Result<PageInformation>

    suspend fun clearMovies(): Result<Unit>

}
