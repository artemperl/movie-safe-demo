package com.moviesafe.mapper

import com.moviesafe.dto.MovieSearchResultDto
import com.moviesafe.dto.TvSeriesSearchResultDto
import com.moviesafe.model.Movie
import com.moviesafe.model.TvSeries

internal fun MovieSearchResultDto.toDomain(posterBaseUrl: String) = Movie(
    id = id,
    title = title,
    overview = overview,
    posterUrl = posterBaseUrl + posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    popularity = popularity,
    genreIds = genreIds,
)

internal fun TvSeriesSearchResultDto.toDomain(posterBaseUrl: String) = TvSeries(
    id = id,
    name = name,
    overview = overview,
    posterUrl = posterBaseUrl + posterPath,
    backdropPath = backdropPath,
    firstAirDate = firstAirDate,
    voteAverage = voteAverage,
    popularity = popularity,
    genreIds = genreIds,
    originCountry = originCountry,
)
