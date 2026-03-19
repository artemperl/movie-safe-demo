package com.moviesafe.usecase

import com.moviesafe.aMovie
import com.moviesafe.fake.FakeSearchRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ObserveMoviesUseCaseTest {

    private val repository = FakeSearchRepository()
    private val useCase = ObserveMoviesUseCase(repository)

    @Test
    fun `returns repository flow`() = runTest {
        val movies = listOf(aMovie("1"), aMovie("2"))
        repository.setMovies(movies)

        val result = useCase().first()

        assertEquals(movies, result)
    }

    @Test
    fun `emits null when store is empty`() = runTest {
        val result = useCase().first()
        assertEquals(null, result)
    }
}
