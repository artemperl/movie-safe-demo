package com.moviesafe.fake

import com.moviesafe.model.Movie
import com.moviesafe.usecase.ObserveMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeObserveMoviesUseCase : ObserveMoviesUseCase(repository = FakeSearchRepository()) {

    private val moviesFlow = MutableStateFlow<List<Movie>?>(null)

    fun setMovies(movies: List<Movie>?) {
        moviesFlow.value = movies
    }

    override fun invoke(): Flow<List<Movie>?> = moviesFlow
}
