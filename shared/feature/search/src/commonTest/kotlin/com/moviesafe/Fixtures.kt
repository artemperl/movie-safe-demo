package com.moviesafe

import com.moviesafe.dto.MovieSearchResultDto
import com.moviesafe.dto.PagedResponseDto
import com.moviesafe.model.Movie

internal fun aMovie(
    id: String = "1",
    title: String = "Test Movie",
) = Movie(
    id = id,
    title = title,
    overview = "Test overview",
    posterUrl = "https://poster.test/movie$id.jpg",
    backdropPath = null,
    releaseDate = "2024-01-01",
    voteAverage = 7.5,
    popularity = 100.0,
    genreIds = emptyList(),
)

internal fun aMovieDto(
    id: String = "1",
    title: String = "Test Movie",
    posterPath: String = "/poster$id.jpg",
) = MovieSearchResultDto(
    id = id,
    title = title,
    originalTitle = title,
    overview = "Test overview",
    posterPath = posterPath,
    backdropPath = null,
    releaseDate = "2024-01-01",
    voteAverage = 7.5,
    voteCount = 100,
    popularity = 100.0,
    genreIds = emptyList(),
    originalLanguage = "en",
    adult = false,
    video = false,
)

internal fun aPagedResponse(
    page: Int = 1,
    totalPages: Int = 1,
    results: List<MovieSearchResultDto> = emptyList(),
) = PagedResponseDto(
    page = page,
    results = results,
    totalPages = totalPages,
    totalResults = results.size,
)
