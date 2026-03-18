package com.moviesafe.ui

import com.moviesafe.model.Movie
import com.moviesafe.model.PageInformation

sealed interface SearchMutation {
    data class SetQuery(val query: String) : SearchMutation
    data class SetMovies(val movies: List<Movie>) : SearchMutation
    data class SetPageInfo(val pageInfo: PageInformation) : SearchMutation
    data class SetSearchLoading(val isLoading: Boolean) : SearchMutation
    data class SetPagingLoading(val isLoading: Boolean) : SearchMutation
    data class SetError(val error: String?) : SearchMutation
}
