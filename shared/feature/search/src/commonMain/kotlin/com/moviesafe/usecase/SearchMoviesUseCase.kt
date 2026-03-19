package com.moviesafe.usecase

import com.moviesafe.model.PageInformation
import com.moviesafe.repository.SearchRepository

open class SearchMoviesUseCase(private val repository: SearchRepository) {
    open suspend operator fun invoke(
        query: String,
        page: Int = 1,
    ): Result<PageInformation> = repository.searchMovies(query, page)
}
