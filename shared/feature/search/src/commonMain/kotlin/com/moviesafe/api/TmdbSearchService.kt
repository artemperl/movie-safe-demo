package com.moviesafe.api

import com.moviesafe.dto.MovieSearchResultDto
import com.moviesafe.dto.PagedResponseDto
import com.moviesafe.dto.TvSeriesSearchResultDto

interface TmdbSearchService {

    suspend fun searchMovies(
        query: String,
        accessToken: String,
        page: Int = 1,
        language: String = "en-US",
    ): Result<PagedResponseDto<MovieSearchResultDto>>

    suspend fun searchTvSeries(
        query: String,
        accessToken: String,
        page: Int = 1,
        language: String = "en-US",
    ): Result<PagedResponseDto<TvSeriesSearchResultDto>>
}