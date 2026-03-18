package com.moviesafe.usecase

import com.moviesafe.fake.FakeSearchRepository
import com.moviesafe.model.PageInformation
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchMoviesUseCaseTest {

    private val repository = FakeSearchRepository()
    private val useCase = SearchMoviesUseCase(repository)

    @Test
    fun `delegates to repository with query and page`() = runTest {
        repository.searchResult = Result.success(PageInformation(2, 5))

        val result = useCase("batman", page = 2)

        assertEquals("batman", repository.lastSearchQuery)
        assertEquals(2, repository.lastSearchPage)
        assertEquals(PageInformation(2, 5), result.getOrThrow())
    }

    @Test
    fun `defaults page to 1`() = runTest {
        useCase("inception")
        assertEquals(1, repository.lastSearchPage)
    }

    @Test
    fun `propagates failure from repository`() = runTest {
        val error = RuntimeException("service unavailable")
        repository.searchResult = Result.failure(error)

        val result = useCase("batman")

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }
}
