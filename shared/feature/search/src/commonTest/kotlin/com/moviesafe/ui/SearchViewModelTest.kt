@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.moviesafe.ui

import com.moviesafe.aMovie
import com.moviesafe.fake.FakeSearchRepository
import com.moviesafe.model.PageInformation
import com.moviesafe.usecase.ClearMoviesUseCase
import com.moviesafe.usecase.ObserveMoviesUseCase
import com.moviesafe.usecase.SearchMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val DEBOUNCE_MS = 500L
private const val PAST_DEBOUNCE_MS = 600L

class SearchViewModelTest {

    private val scheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(scheduler)
    private lateinit var repository: FakeSearchRepository
    private lateinit var viewModel: SearchViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeSearchRepository()
        viewModel = SearchViewModel(
            searchMoviesUseCase = SearchMoviesUseCase(repository),
            observeMoviesUseCase = ObserveMoviesUseCase(repository),
            clearMoviesUseCase = ClearMoviesUseCase(repository),
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── Initial state ─────────────────────────────────────────────────────────

    @Test
    fun `initial state is default`() {
        assertEquals(SearchState(), viewModel.state)
    }

    @Test
    fun `init clears movies in repository`() = runTest(testDispatcher) {
        advanceUntilIdle()
        assertEquals(1, repository.clearCallCount)
    }

    // ── UpdateQuery ───────────────────────────────────────────────────────────

    @Test
    fun `UpdateQuery updates query in state immediately`() {
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        assertEquals("batman", viewModel.state.query)
    }

    @Test
    fun `UpdateQuery triggers search after debounce`() = runTest(testDispatcher) {
        repository.searchResult = Result.success(PageInformation(1, 3))

        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        assertEquals(1, repository.searchCallCount)
        assertEquals("batman", repository.lastSearchQuery)
        assertEquals(1, repository.lastSearchPage)
    }

    @Test
    fun `rapid queries within debounce window only trigger one search`() = runTest(testDispatcher) {
        viewModel.onIntent(SearchIntent.UpdateQuery("b"))
        advanceTimeBy(100)
        viewModel.onIntent(SearchIntent.UpdateQuery("ba"))
        advanceTimeBy(100)
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        assertEquals(1, repository.searchCallCount)
        assertEquals("batman", repository.lastSearchQuery)
    }

    @Test
    fun `search success updates page info in state`() = runTest(testDispatcher) {
        repository.searchResult = Result.success(PageInformation(1, 5))

        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        assertEquals(1, viewModel.state.currentPage)
        assertEquals(5, viewModel.state.totalPages)
    }

    @Test
    fun `search failure sets error in state`() = runTest(testDispatcher) {
        val error = RuntimeException("network error")
        repository.searchResult = Result.failure(error)

        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        assertEquals(error, viewModel.state.error)
    }

    @Test
    fun `new search clears previous error`() = runTest(testDispatcher) {
        repository.searchResult = Result.failure(RuntimeException("first error"))
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        repository.searchResult = Result.success(PageInformation(1, 1))
        viewModel.onIntent(SearchIntent.UpdateQuery("inception"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        assertNull(viewModel.state.error)
    }

    @Test
    fun `search loading is false after search completes`() = runTest(testDispatcher) {
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        assertTrue(!viewModel.state.isSearchLoading)
    }

    // ── LoadNextPage ──────────────────────────────────────────────────────────

    @Test
    fun `LoadNextPage does nothing when canPaginate is false`() = runTest(testDispatcher) {
        // Default state has canPaginate = false (currentPage = 0)
        viewModel.onIntent(SearchIntent.LoadNextPage)
        advanceUntilIdle()

        assertEquals(0, repository.searchCallCount)
    }

    @Test
    fun `LoadNextPage loads next page when canPaginate is true`() = runTest(testDispatcher) {
        repository.searchResult = Result.success(PageInformation(1, 3))
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        repository.searchResult = Result.success(PageInformation(2, 3))
        viewModel.onIntent(SearchIntent.LoadNextPage)
        advanceUntilIdle()

        assertEquals(2, viewModel.state.currentPage)
        assertEquals(2, repository.searchCallCount)
    }

    @Test
    fun `LoadNextPage sends correct next page number`() = runTest(testDispatcher) {
        repository.searchResult = Result.success(PageInformation(1, 5))
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        viewModel.onIntent(SearchIntent.LoadNextPage)
        advanceUntilIdle()

        assertEquals(2, repository.lastSearchPage)
    }

    @Test
    fun `LoadNextPage paging loading is false after completion`() = runTest(testDispatcher) {
        repository.searchResult = Result.success(PageInformation(1, 3))
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        viewModel.onIntent(SearchIntent.LoadNextPage)
        advanceUntilIdle()

        assertTrue(!viewModel.state.isPagingLoading)
    }

    // ── SelectMovie ───────────────────────────────────────────────────────────

    @Test
    fun `SelectMovie emits MovieSelected event`() = runTest(testDispatcher) {
        val emittedEvents = mutableListOf<SearchViewModel.Event>()
        val job = launch { viewModel.events.collect { emittedEvents.add(it) } }

        viewModel.onIntent(SearchIntent.SelectMovie("movie-42"))
        advanceUntilIdle()

        assertEquals(1, emittedEvents.size)
        assertEquals(SearchViewModel.Event.MovieSelected("movie-42"), emittedEvents.first())
        job.cancel()
    }

    // ── Repository observation ────────────────────────────────────────────────

    @Test
    fun `movies from repository are reflected in state`() = runTest(testDispatcher) {
        advanceUntilIdle() // let init observer start collecting

        val movies = listOf(aMovie("1"), aMovie("2"))
        repository.setMovies(movies)
        advanceUntilIdle()

        assertEquals(movies, viewModel.state.movies)
    }

    @Test
    fun `null emission from repository results in empty movies list`() = runTest(testDispatcher) {
        repository.setMovies(listOf(aMovie()))
        advanceUntilIdle()

        repository.setMovies(null)
        advanceUntilIdle()

        assertTrue(viewModel.state.movies.isEmpty())
    }
}
