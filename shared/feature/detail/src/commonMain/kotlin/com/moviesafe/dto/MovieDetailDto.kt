package com.moviesafe.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailDto (
    val id: String,
    val title: String,
    @SerialName("original_title") val originalTitle: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("release_date") val releaseDate: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    val popularity: Double,
    @SerialName("genre_ids") val genreIds: List<Int>,
    @SerialName("original_language") val originalLanguage: String,
    val adult: Boolean,
    val video: Boolean,
)
