package com.moviesafe.ui

import com.moviesafe.model.MovieDetail

data class DetailState(
    val isLoading: Boolean = false,
    val movieDetail: MovieDetail? = null,
    val error: Throwable? = null
)