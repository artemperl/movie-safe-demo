package com.moviesafe.api

import com.moviesafe.dto.MovieDetailDto
import com.moviesafe.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TmdbDetailServiceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : TmdbDetailService {

    override suspend fun fetchMovie(
        movieId: String,
        language: String
    ): Result<MovieDetailDto> =
        httpClient.safeRequest {
            get("$baseUrl/movie/$movieId") {
                parameter("language", language)
            }
        }

}