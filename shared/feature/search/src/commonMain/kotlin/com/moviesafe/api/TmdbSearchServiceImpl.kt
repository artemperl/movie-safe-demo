package com.moviesafe.api

import com.moviesafe.dto.MovieSearchResultDto
import com.moviesafe.dto.PagedResponseDto
import com.moviesafe.dto.TvSeriesSearchResultDto
import com.moviesafe.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class TmdbSearchServiceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : TmdbSearchService {

    override suspend fun searchMovies(
        query: String,
        accessToken: String,
        page: Int,
        language: String,
    ): Result<PagedResponseDto<MovieSearchResultDto>> =
        httpClient.safeRequest {
            get("$baseUrl/search/movie") {
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                parameter("query", query)
                parameter("page", page)
                parameter("language", language)
                parameter("include_adult", false)
            }
        }

    override suspend fun searchTvSeries(
        query: String,
        accessToken: String,
        page: Int,
        language: String,
    ): Result<PagedResponseDto<TvSeriesSearchResultDto>> =
        httpClient.safeRequest {
            get("$baseUrl/search/tv") {
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                parameter("query", query)
                parameter("page", page)
                parameter("language", language)
                parameter("include_adult", false)
            }.body()
        }
}
