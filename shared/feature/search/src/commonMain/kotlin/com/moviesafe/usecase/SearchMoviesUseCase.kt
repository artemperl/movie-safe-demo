package com.moviesafe.usecase

import com.moviesafe.model.PageInformation
import com.moviesafe.repository.SearchRepository

class SearchMoviesUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(
        query: String,
        accessToken: String,
        page: Int = 1,
    ): Result<PageInformation> = repository.searchMovies(query, accessToken, page)
}
