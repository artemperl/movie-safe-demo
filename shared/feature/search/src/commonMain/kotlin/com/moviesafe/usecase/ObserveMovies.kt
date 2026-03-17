package com.moviesafe.usecase

import com.moviesafe.model.Movie
import com.moviesafe.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class ObserveMovies(private val repository: SearchRepository) {
    operator fun invoke(): Flow<List<Movie>?> = repository.onMovies()
}
