package com.moviesafe.fake

import com.moviesafe.MSDataStore
import com.moviesafe.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeMovieStore : MSDataStore<List<Movie>> {

    private val moviesFlow = MutableStateFlow<List<Movie>?>(null)

    override val data: Flow<List<Movie>?> = moviesFlow

    override suspend fun updateData(update: (List<Movie>?) -> List<Movie>?): Result<Unit> {
        moviesFlow.value = update(moviesFlow.value)
        return Result.success(Unit)
    }

    fun currentMovies(): List<Movie>? = moviesFlow.value
}
