package com.moviesafe.screenshot

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.moviesafe.model.Movie
import com.moviesafe.ui.SearchScreenContent
import com.moviesafe.ui.SearchState

private val previewMovies = listOf(
    "The Dark Knight", "Inception", "Interstellar",
    "Avengers: Endgame", "Spider-Man", "Iron Man",
    "The Matrix", "Pulp Fiction", "Goodfellas",
).mapIndexed { index, title ->
    Movie(
        id = index.toString(),
        title = title,
        overview = "A compelling story about heroes and villains.",
        posterUrl = null,
        backdropPath = null,
        releaseDate = "2024",
        voteAverage = 8.0,
        popularity = 200.0,
        genreIds = emptyList(),
    )
}

@PreviewTest
@Preview
@Composable
fun SearchScreenContentPreview() {
    MaterialTheme {
        SearchScreenContent(
            state = SearchState(query = "Marvel", movies = previewMovies),
            onNewSearchQuery = {},
            onLoadNextPage = {},
            onMovieClick = {},
        )
    }
}

@PreviewTest
@Preview
@Composable
fun SearchScreenSearchLoadingPreview() {
    MaterialTheme {
        SearchScreenContent(
            state = SearchState(query = "Marvel", isSearchLoading = true),
            onNewSearchQuery = {},
            onLoadNextPage = {},
            onMovieClick = {},
        )
    }
}

@PreviewTest
@Preview
@Composable
fun SearchScreenPagingLoadingPreview() {
    MaterialTheme {
        SearchScreenContent(
            state = SearchState(
                query = "Marvel",
                movies = previewMovies,
                isPagingLoading = true,
                currentPage = 2,
                totalPages = 5,
            ),
            onNewSearchQuery = {},
            onLoadNextPage = {},
            onMovieClick = {},
        )
    }
}

@PreviewTest
@Preview
@Composable
fun SearchScreenErrorPreview() {
    MaterialTheme {
        SearchScreenContent(
            state = SearchState(
                query = "Marvel",
                error = RuntimeException("Network error"),
            ),
            onNewSearchQuery = {},
            onLoadNextPage = {},
            onMovieClick = {},
        )
    }
}
