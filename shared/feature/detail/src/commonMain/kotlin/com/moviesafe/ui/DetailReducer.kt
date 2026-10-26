package com.moviesafe.ui

fun DetailState.reduce(mutation: DetailMutation) = when (mutation) {
    DetailMutation.LoadingStarted -> copy(isLoading = true, error = null)
    is DetailMutation.LoadingFailure -> copy(isLoading = false, error = mutation.error)
    is DetailMutation.LoadingSuccess -> copy(isLoading = false, movieDetail = mutation.movieDetail)
}