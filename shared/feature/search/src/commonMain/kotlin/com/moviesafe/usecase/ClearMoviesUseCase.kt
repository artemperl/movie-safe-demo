package com.moviesafe.usecase

import com.moviesafe.repository.SearchRepository

class ClearMoviesUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke() = repository.clearMovies()
}
