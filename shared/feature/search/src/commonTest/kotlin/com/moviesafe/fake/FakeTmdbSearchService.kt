package com.moviesafe.fake

import com.moviesafe.aPagedResponse
import com.moviesafe.api.TmdbSearchService
import com.moviesafe.dto.MovieSearchResultDto
import com.moviesafe.dto.PagedResponseDto
import com.moviesafe.dto.TvSeriesSearchResultDto

class FakeTmdbSearchService : TmdbSearchService {

    var searchMoviesResult: Result<PagedResponseDto<MovieSearchResultDto>> =
        Result.success(aPagedResponse())
    var searchCallCount = 0
    var lastQuery: String? = null
    var lastPage: Int? = null

    override suspend fun searchMovies(
        query: String,
        page: Int,
        language: String,
    ): Result<PagedResponseDto<MovieSearchResultDto>> {
        searchCallCount++
        lastQuery = query
        lastPage = page
        return searchMoviesResult
    }

    override suspend fun searchTvSeries(
        query: String,
        page: Int,
        language: String,
    ): Result<PagedResponseDto<TvSeriesSearchResultDto>> =
        Result.success(PagedResponseDto(page = 1, results = emptyList(), totalPages = 1, totalResults = 0))
}
