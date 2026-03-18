package com.moviesafe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.moviesafe.model.Movie
import org.koin.compose.viewmodel.koinViewModel

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342"
private const val GRID_COLUMNS = 3

@Composable
fun SearchScreen(
    onMovieSelected: (movieId: String) -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    HandleEvents(
        viewModel = viewModel,
        onMovieSelected = onMovieSelected
    )

    SearchScreenContent(
        state = viewModel.state,
        onNewSearchQuery = { viewModel.onIntent(SearchIntent.UpdateQuery(it)) },
        onLoadNextPage = { viewModel.onIntent(SearchIntent.LoadNextPage) },
        onMovieClick = { viewModel.onIntent(SearchIntent.SelectMovie(it)) },
    )
}

@Composable
private fun HandleEvents(
    viewModel: SearchViewModel,
    onMovieSelected: (movieId: String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchViewModel.Event.MovieSelected -> onMovieSelected(event.movieId)
            }
        }
    }
}

@Composable
private fun SearchScreenContent(
    state: SearchState,
    onNewSearchQuery: (String) -> Unit,
    onLoadNextPage: () -> Unit,
    onMovieClick: (movieId: String) -> Unit,
) {
    val gridState = rememberLazyGridState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false
            lastVisible.index >= gridState.layoutInfo.totalItemsCount - GRID_COLUMNS * 2
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadNextPage()
    }

    Surface {

        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onNewSearchQuery,
                // TODO string resource
                placeholder = { Text("Search movies...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
            )

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                when {
                    state.isSearchLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(GRID_COLUMNS),
                            state = gridState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            items(state.movies, key = { it.id }) { movie ->
                                MovieGridItem(movie = movie, onClick = { onMovieClick(movie.id) })
                            }

                            if (state.isPagingLoading) {
                                item(span = { GridItemSpan(GRID_COLUMNS) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                    }
                }

                state.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieGridItem(movie: Movie, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f / 3f),
    ) {
        Box {
            AsyncImage(
                model = movie.posterPath?.let { "$TMDB_IMAGE_BASE_URL$it" },
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            // gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.7f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.8f),
                        )
                    )
            )

            Text(
                text = movie.title,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp),
            )
        }
    }
}

private val previewMovies = listOf(
    "The Dark Knight", "Inception", "Interstellar",
    "Avengers: Endgame", "Spider-Man", "Iron Man",
    "The Matrix", "Pulp Fiction", "Goodfellas",
).mapIndexed { index, title ->
    Movie(
        id = index.toString(),
        title = title,
        overview = "A compelling story about heroes and villains.",
        posterPath = null,
        backdropPath = null,
        releaseDate = "2024",
        voteAverage = 8.0,
        popularity = 200.0,
        genreIds = emptyList(),
    )
}

@Preview
@Composable
private fun SearchScreenContentPreview() {
    MaterialTheme {
        SearchScreenContent(
            state = SearchState(query = "Marvel", movies = previewMovies),
            onNewSearchQuery = {},
            onLoadNextPage = {},
            onMovieClick = {},
        )
    }
}

@Preview
@Composable
private fun SearchScreenSearchLoadingPreview() {
    MaterialTheme {
        SearchScreenContent(
            state = SearchState(query = "Marvel", isSearchLoading = true),
            onNewSearchQuery = {},
            onLoadNextPage = {},
            onMovieClick = {},
        )
    }
}

@Preview
@Composable
private fun SearchScreenPagingLoadingPreview() {
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

