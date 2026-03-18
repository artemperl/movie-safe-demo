package com.moviesafe.ui

sealed interface SearchIntent {
    data class UpdateQuery(val query: String) : SearchIntent
    data object LoadNextPage : SearchIntent
}
