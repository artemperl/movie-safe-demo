package com.moviesafe.ui

import com.moviesafe.aMovie
import com.moviesafe.model.PageInformation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SearchReducerTest {

    private val default = SearchState()

    // SetQuery

    @Test
    fun `SetQuery updates query`() {
        val result = default.reduce(SearchMutation.SetQuery("batman"))
        assertEquals("batman", result.query)
    }

    @Test
    fun `SetQuery does not affect other fields`() {
        val state = default.copy(movies = listOf(aMovie()), currentPage = 2)
        val result = state.reduce(SearchMutation.SetQuery("inception"))
        assertEquals(listOf(aMovie()), result.movies)
        assertEquals(2, result.currentPage)
    }

    // SetMovies

    @Test
    fun `SetMovies replaces movie list`() {
        val movies = listOf(aMovie("1"), aMovie("2"))
        val result = default.reduce(SearchMutation.SetMovies(movies))
        assertEquals(movies, result.movies)
    }

    @Test
    fun `SetMovies with empty list clears movies`() {
        val state = default.copy(movies = listOf(aMovie()))
        val result = state.reduce(SearchMutation.SetMovies(emptyList()))
        assertTrue(result.movies.isEmpty())
    }

    // SetPageInfo

    @Test
    fun `SetPageInfo updates currentPage and totalPages`() {
        val result = default.reduce(SearchMutation.SetPageInfo(PageInformation(3, 10)))
        assertEquals(3, result.currentPage)
        assertEquals(10, result.totalPages)
    }

    // SetSearchLoading

    @Test
    fun `SetSearchLoading true sets flag`() {
        val result = default.reduce(SearchMutation.SetSearchLoading(true))
        assertTrue(result.isSearchLoading)
    }

    @Test
    fun `SetSearchLoading false clears flag`() {
        val state = default.copy(isSearchLoading = true)
        val result = state.reduce(SearchMutation.SetSearchLoading(false))
        assertFalse(result.isSearchLoading)
    }

    // SetPagingLoading

    @Test
    fun `SetPagingLoading true sets flag`() {
        val result = default.reduce(SearchMutation.SetPagingLoading(true))
        assertTrue(result.isPagingLoading)
    }

    @Test
    fun `SetPagingLoading false clears flag`() {
        val state = default.copy(isPagingLoading = true)
        val result = state.reduce(SearchMutation.SetPagingLoading(false))
        assertFalse(result.isPagingLoading)
    }

    // SetError

    @Test
    fun `SetError sets error`() {
        val error = RuntimeException("network failure")
        val result = default.reduce(SearchMutation.SetError(error))
        assertEquals(error, result.error)
    }

    @Test
    fun `SetError null clears error`() {
        val state = default.copy(error = RuntimeException())
        val result = state.reduce(SearchMutation.SetError(null))
        assertNull(result.error)
    }
}
