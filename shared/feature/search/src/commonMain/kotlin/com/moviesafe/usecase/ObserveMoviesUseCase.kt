package com.moviesafe.usecase

import com.moviesafe.model.Movie
import com.moviesafe.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

open class ObserveMoviesUseCase(private val repository: SearchRepository) {
    open operator fun invoke(): Flow<List<Movie>?> = repository.onMovies()
}
