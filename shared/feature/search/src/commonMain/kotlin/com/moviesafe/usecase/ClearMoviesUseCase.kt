package com.moviesafe.usecase

import com.moviesafe.repository.SearchRepository

open class ClearMoviesUseCase(private val repository: SearchRepository) {
    open suspend operator fun invoke() = repository.clearMovies()
}
