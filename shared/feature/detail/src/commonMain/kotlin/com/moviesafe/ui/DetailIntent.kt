package com.moviesafe.ui

sealed interface DetailIntent {
    data class LikeMovie(val movieId: String): DetailIntent
}