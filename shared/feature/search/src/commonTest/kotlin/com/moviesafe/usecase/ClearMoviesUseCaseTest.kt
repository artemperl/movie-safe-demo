package com.moviesafe.usecase

import com.moviesafe.aMovie
import com.moviesafe.fake.FakeSearchRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ClearMoviesUseCaseTest {

    private val repository = FakeSearchRepository()
    private val useCase = ClearMoviesUseCase(repository)

    @Test
    fun `clearMovies delegates to repository`() = runTest {
        useCase()
        assertEquals(1, repository.clearCallCount)
    }

    @Test
    fun `clears movies from repository`() = runTest {
        repository.setMovies(listOf(aMovie()))
        useCase()
        assertEquals(null, repository.onMovies().first())
    }
}
