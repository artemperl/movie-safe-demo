package com.moviesafe.ui

import com.moviesafe.model.MovieDetail

sealed interface DetailMutation {
    data object LoadingStarted : DetailMutation
    data class LoadingSuccess(val movieDetail: MovieDetail) : DetailMutation
    data class LoadingFailure(val error: Throwable) : DetailMutation
}