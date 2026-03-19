package com.moviesafe.repository

import com.moviesafe.aMovieDto
import com.moviesafe.aPagedResponse
import com.moviesafe.fake.FakeMovieStore
import com.moviesafe.fake.FakeTmdbSearchService
import com.moviesafe.model.PageInformation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val POSTER_BASE_URL = "https://poster.test"

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {

    private val fakeService = FakeTmdbSearchService()
    private val fakeStore = FakeMovieStore()
    private val repository = SearchRepositoryImpl(
        coroutineDispatcher = UnconfinedTestDispatcher(),
        searchService = fakeService,
        moviesDataStore = fakeStore,
        posterBaseUrl = POSTER_BASE_URL,
    )

    // searchMovies

    @Test
    fun `searchMovies calls service with query and page`() = runTest {
        repository.searchMovies("batman", page = 2)

        assertEquals("batman", fakeService.lastQuery)
        assertEquals(2, fakeService.lastPage)
    }

    @Test
    fun `searchMovies returns PageInformation on success`() = runTest {
        fakeService.searchMoviesResult = Result.success(
            aPagedResponse(page = 2, totalPages = 5)
        )

        val result = repository.searchMovies("batman", page = 2)

        assertEquals(PageInformation(currentPage = 2, totalPages = 5), result.getOrThrow())
    }

    @Test
    fun `searchMovies maps dto to domain and stores it`() = runTest {
        val dto = aMovieDto(id = "42", posterPath = "/poster42.jpg")
        fakeService.searchMoviesResult = Result.success(
            aPagedResponse(results = listOf(dto))
        )

        repository.searchMovies("batman")

        val stored = fakeStore.currentMovies()
        assertEquals(1, stored?.size)
        assertEquals("42", stored?.first()?.id)
        assertEquals("$POSTER_BASE_URL/poster42.jpg", stored?.first()?.posterUrl)
    }

    @Test
    fun `searchMovies appends to existing movies for pagination`() = runTest {
        val firstDto = aMovieDto(id = "1")
        val secondDto = aMovieDto(id = "2")

        fakeService.searchMoviesResult = Result.success(aPagedResponse(results = listOf(firstDto)))
        repository.searchMovies("batman", page = 1)

        fakeService.searchMoviesResult = Result.success(aPagedResponse(results = listOf(secondDto)))
        repository.searchMovies("batman", page = 2)

        val stored = fakeStore.currentMovies()
        assertEquals(2, stored?.size)
        assertEquals("1", stored?.get(0)?.id)
        assertEquals("2", stored?.get(1)?.id)
    }

    @Test
    fun `searchMovies returns failure when service fails`() = runTest {
        val error = RuntimeException("network error")
        fakeService.searchMoviesResult = Result.failure(error)

        val result = repository.searchMovies("batman")

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun `searchMovies does not update store when service fails`() = runTest {
        fakeService.searchMoviesResult = Result.failure(RuntimeException())

        repository.searchMovies("batman")

        assertNull(fakeStore.currentMovies())
    }

    // clearMovies

    @Test
    fun `clearMovies clears the store`() = runTest {
        val dto = aMovieDto()
        fakeService.searchMoviesResult = Result.success(aPagedResponse(results = listOf(dto)))
        repository.searchMovies("batman")

        repository.clearMovies()

        assertNull(fakeStore.currentMovies())
    }

    // onMovies

    @Test
    fun `onMovies emits current store contents`() = runTest {
        val dto = aMovieDto(id = "99")
        fakeService.searchMoviesResult = Result.success(aPagedResponse(results = listOf(dto)))
        repository.searchMovies("batman")

        val movies = repository.onMovies().first()
        assertEquals(1, movies?.size)
        assertEquals("99", movies?.first()?.id)
    }

    @Test
    fun `onMovies emits null when store is empty`() = runTest {
        val movies = repository.onMovies().first()
        assertNull(movies)
    }
}
