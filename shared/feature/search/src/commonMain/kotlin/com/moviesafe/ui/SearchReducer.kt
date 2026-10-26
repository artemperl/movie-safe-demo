package com.moviesafe.ui


internal fun SearchState.reduce(mutation: SearchMutation): SearchState =
    when (mutation) {
        is SearchMutation.SetQuery -> copy(query = mutation.query)
        is SearchMutation.SetMovies -> copy(movies = mutation.movies)
        is SearchMutation.SetPageInfo -> copy(
            currentPage = mutation.pageInfo.currentPage,
            totalPages = mutation.pageInfo.totalPages,
        )

        is SearchMutation.SetSearchLoading -> copy(isSearchLoading = mutation.isLoading)
        is SearchMutation.SetPagingLoading -> copy(isPagingLoading = mutation.isLoading)
        is SearchMutation.SetError -> copy(error = mutation.error)
    }
