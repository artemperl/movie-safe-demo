package com.moviesafe.ui

import com.moviesafe.aMovie
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearchStateTest {

    @Test
    fun `canPaginate is true when all conditions met`() {
        val state = SearchState(
            query = "batman",
            currentPage = 1,
            totalPages = 3,
            isSearchLoading = false,
            isPagingLoading = false,
        )
        assertTrue(state.canPaginate)
    }

    @Test
    fun `canPaginate is false when query is blank`() {
        val state = SearchState(query = "", currentPage = 1, totalPages = 3)
        assertFalse(state.canPaginate)
    }

    @Test
    fun `canPaginate is false when on last page`() {
        val state = SearchState(query = "batman", currentPage = 3, totalPages = 3)
        assertFalse(state.canPaginate)
    }

    @Test
    fun `canPaginate is false when currentPage is 0`() {
        val state = SearchState(query = "batman", currentPage = 0, totalPages = 5)
        assertFalse(state.canPaginate)
    }

    @Test
    fun `canPaginate is false when search is loading`() {
        val state = SearchState(
            query = "batman",
            currentPage = 1,
            totalPages = 3,
            isSearchLoading = true,
        )
        assertFalse(state.canPaginate)
    }

    @Test
    fun `canPaginate is false when paging is loading`() {
        val state = SearchState(
            query = "batman",
            currentPage = 1,
            totalPages = 3,
            isPagingLoading = true,
        )
        assertFalse(state.canPaginate)
    }

    @Test
    fun `default state has no movies`() {
        assertTrue(SearchState().movies.isEmpty())
    }

    @Test
    fun `default state has no error`() {
        val state = SearchState()
        assertFalse(state.isSearchLoading)
        assertFalse(state.isPagingLoading)
    }
}
