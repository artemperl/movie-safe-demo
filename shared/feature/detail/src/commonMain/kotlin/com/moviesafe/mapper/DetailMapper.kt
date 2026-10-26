package com.moviesafe.mapper

import com.moviesafe.dto.MovieDetailDto
import com.moviesafe.model.MovieDetail

fun MovieDetailDto.toMovieDetail(
    posterBaseUrl: String
): MovieDetail = MovieDetail(
    id = id,
    title = title,
    overview = overview,
    posterUrl = posterBaseUrl + posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    popularity = popularity,
    genreIds = genreIds
)