package com.moviesafe.api

import com.moviesafe.dto.MovieDetailDto

interface TmdbDetailService {

    suspend fun fetchMovie(
        movieId: String,
        language: String = "en-US",
    ): Result<MovieDetailDto>
}