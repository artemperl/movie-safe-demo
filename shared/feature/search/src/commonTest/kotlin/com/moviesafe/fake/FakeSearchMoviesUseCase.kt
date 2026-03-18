package com.moviesafe.fake

import com.moviesafe.model.PageInformation
import com.moviesafe.usecase.SearchMoviesUseCase

class FakeSearchMoviesUseCase : SearchMoviesUseCase(repository = FakeSearchRepository()) {

    var result: Result<PageInformation> = Result.success(PageInformation(1, 1))
    var callCount = 0
    var lastQuery: String? = null
    var lastPage: Int? = null

    override suspend fun invoke(query: String, page: Int): Result<PageInformation> {
        callCount++
        lastQuery = query
        lastPage = page
        return result
    }
}
