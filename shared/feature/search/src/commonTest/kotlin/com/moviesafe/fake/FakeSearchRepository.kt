package com.moviesafe.fake

import com.moviesafe.model.Movie
import com.moviesafe.model.PageInformation
import com.moviesafe.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSearchRepository : SearchRepository {

    private val moviesFlow = MutableStateFlow<List<Movie>?>(null)

    var searchResult: Result<PageInformation> = Result.success(PageInformation(1, 1))
    var searchCallCount = 0
    var lastSearchQuery: String? = null
    var lastSearchPage: Int? = null
    var clearCallCount = 0

    fun setMovies(movies: List<Movie>?) {
        moviesFlow.value = movies
    }

    override fun onMovies(): Flow<List<Movie>?> = moviesFlow

    override suspend fun searchMovies(query: String, page: Int): Result<PageInformation> {
        searchCallCount++
        lastSearchQuery = query
        lastSearchPage = page
        return searchResult
    }

    override suspend fun clearMovies() {
        clearCallCount++
        moviesFlow.value = null
    }
}
