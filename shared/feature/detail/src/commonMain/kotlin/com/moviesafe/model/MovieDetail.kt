package com.moviesafe.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieDetail(
    val id: String,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val popularity: Double,
    val genreIds: List<Int>,
)
