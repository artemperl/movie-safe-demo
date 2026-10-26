package com.moviesafe.repository

import com.moviesafe.model.MovieDetail

interface DetailRepository {

    suspend fun getDetail(movieId: String): Result<MovieDetail>

    suspend fun clearDetail(): Result<Unit>

}