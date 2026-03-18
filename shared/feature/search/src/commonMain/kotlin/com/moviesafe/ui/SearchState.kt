package com.moviesafe.ui

import com.moviesafe.model.Movie

data class SearchState(
    val query: String = "",
    val movies: List<Movie> = emptyList(),
    val isSearchLoading: Boolean = false,
    val isPagingLoading: Boolean = false,
    val currentPage: Int = 0,
    val totalPages: Int = 1,
    val error: String? = null,
) {
    val canPaginate: Boolean
        get() = query.isNotBlank() &&
                !isPagingLoading &&
                !isSearchLoading &&
                currentPage > 0 &&
                currentPage < totalPages
}
