@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.moviesafe.ui

import com.moviesafe.aMovie
import com.moviesafe.fake.FakeClearMoviesUseCase
import com.moviesafe.fake.FakeObserveMoviesUseCase
import com.moviesafe.fake.FakeSearchMoviesUseCase
import com.moviesafe.model.PageInformation
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

private const val PAST_DEBOUNCE_MS = 600L

class SearchViewModelTest {

    private val scheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(scheduler)
    private lateinit var searchMoviesUseCase: FakeSearchMoviesUseCase
    private lateinit var observeMoviesUseCase: FakeObserveMoviesUseCase
    private lateinit var clearMoviesUseCase: FakeClearMoviesUseCase
    private lateinit var viewModel: SearchViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchMoviesUseCase = FakeSearchMoviesUseCase()
        observeMoviesUseCase = FakeObserveMoviesUseCase()
        clearMoviesUseCase = FakeClearMoviesUseCase()
        viewModel = SearchViewModel(
            searchMoviesUseCase = searchMoviesUseCase,
            observeMoviesUseCase = observeMoviesUseCase,
            clearMoviesUseCase = clearMoviesUseCase,
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Initial state

    @Test
    fun `initial state is default`() {
        assertEquals(SearchState(), viewModel.state)
    }

    @Test
    fun `init clears movies`() = runTest(testDispatcher) {
        advanceUntilIdle()
        assertEquals(1, clearMoviesUseCase.callCount)
    }

    // UpdateQuery

    @Test
    fun `UpdateQuery updates query in state immediately`() {
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        assertEquals("batman", viewModel.state.query)
    }

    @Test
    fun `UpdateQuery triggers search after debounce`() = runTest(testDispatcher) {
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        assertEquals(1, searchMoviesUseCase.callCount)
        assertEquals("batman", searchMoviesUseCase.lastQuery)
        assertEquals(1, searchMoviesUseCase.lastPage)
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

        assertEquals(1, searchMoviesUseCase.callCount)
        assertEquals("batman", searchMoviesUseCase.lastQuery)
    }

    @Test
    fun `search success updates page info in state`() = runTest(testDispatcher) {
        searchMoviesUseCase.result = Result.success(PageInformation(1, 5))

        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        assertEquals(1, viewModel.state.currentPage)
        assertEquals(5, viewModel.state.totalPages)
    }

    @Test
    fun `search failure sets error in state`() = runTest(testDispatcher) {
        val error = RuntimeException("network error")
        searchMoviesUseCase.result = Result.failure(error)

        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        assertEquals(error, viewModel.state.error)
    }

    @Test
    fun `new search clears previous error`() = runTest(testDispatcher) {
        searchMoviesUseCase.result = Result.failure(RuntimeException("first error"))
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        searchMoviesUseCase.result = Result.success(PageInformation(1, 1))
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

    // LoadNextPage

    @Test
    fun `LoadNextPage does nothing when canPaginate is false`() = runTest(testDispatcher) {
        viewModel.onIntent(SearchIntent.LoadNextPage)
        advanceUntilIdle()

        assertEquals(0, searchMoviesUseCase.callCount)
    }

    @Test
    fun `LoadNextPage loads next page when canPaginate is true`() = runTest(testDispatcher) {
        searchMoviesUseCase.result = Result.success(PageInformation(1, 3))
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        searchMoviesUseCase.result = Result.success(PageInformation(2, 3))
        viewModel.onIntent(SearchIntent.LoadNextPage)
        advanceUntilIdle()

        assertEquals(2, viewModel.state.currentPage)
        assertEquals(2, searchMoviesUseCase.callCount)
    }

    @Test
    fun `LoadNextPage sends correct next page number`() = runTest(testDispatcher) {
        searchMoviesUseCase.result = Result.success(PageInformation(1, 5))
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        viewModel.onIntent(SearchIntent.LoadNextPage)
        advanceUntilIdle()

        assertEquals(2, searchMoviesUseCase.lastPage)
    }

    @Test
    fun `LoadNextPage paging loading is false after completion`() = runTest(testDispatcher) {
        searchMoviesUseCase.result = Result.success(PageInformation(1, 3))
        viewModel.onIntent(SearchIntent.UpdateQuery("batman"))
        advanceTimeBy(PAST_DEBOUNCE_MS)
        advanceUntilIdle()

        viewModel.onIntent(SearchIntent.LoadNextPage)
        advanceUntilIdle()

        assertTrue(!viewModel.state.isPagingLoading)
    }

    // SelectMovie

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

    // Use case observation

    @Test
    fun `movies from use case are reflected in state`() = runTest(testDispatcher) {
        advanceUntilIdle()

        val movies = listOf(aMovie("1"), aMovie("2"))
        observeMoviesUseCase.setMovies(movies)
        advanceUntilIdle()

        assertEquals(movies, viewModel.state.movies)
    }

    @Test
    fun `null emission from use case results in empty movies list`() = runTest(testDispatcher) {
        observeMoviesUseCase.setMovies(listOf(aMovie()))
        advanceUntilIdle()

        observeMoviesUseCase.setMovies(null)
        advanceUntilIdle()

        assertTrue(viewModel.state.movies.isEmpty())
    }
}
