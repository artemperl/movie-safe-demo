package com.moviesafe.model

import kotlinx.serialization.Serializable

@Serializable
data class TvSeries(
    val id: Int,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val firstAirDate: String?,
    val voteAverage: Double,
    val popularity: Double,
    val genreIds: List<Int>,
    val originCountry: List<String>,
)
