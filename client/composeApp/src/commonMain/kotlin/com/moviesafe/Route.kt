package com.moviesafe

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Search : Route

    @Serializable
    data class Details(val movieId: String): Route

    @Serializable
    data class MovieDetail(val movieId: String) : Route
}
