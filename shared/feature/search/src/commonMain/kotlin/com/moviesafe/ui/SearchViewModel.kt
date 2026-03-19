package com.moviesafe.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diamondedge.logging.logging
import com.moviesafe.model.PageInformation
import com.moviesafe.usecase.ClearMoviesUseCase
import com.moviesafe.usecase.ObserveMoviesUseCase
import com.moviesafe.usecase.SearchMoviesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val observeMoviesUseCase: ObserveMoviesUseCase,
    private val clearMoviesUseCase: ClearMoviesUseCase,
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val events: SharedFlow<Event> = _events.asSharedFlow()

    private val queryFlow = MutableStateFlow("")

    private val log = logging()

    init {
        viewModelScope.launch {
            // don't handle errors when clearing at the moment
            val clearMoviesResult = clearMoviesUseCase()

            observeMoviesUseCase().collect { movies ->
                mutate(SearchMutation.SetMovies(movies.orEmpty()))
            }
        }
        viewModelScope.launch {
            // fire queries with debounce
            queryFlow
                .filter { it.isNotBlank() }
                .debounce(500L)
                .collect { query ->
                    if (query.isBlank()) {
                        clearSearch()
                    } else {
                        performSearch(query)
                    }
                }
        }
    }

    fun onIntent(intent: SearchIntent) {
        log.d { "[INTENT] $intent" }
        when (intent) {
            is SearchIntent.UpdateQuery -> onUpdateQuery(intent.query)
            SearchIntent.LoadNextPage -> onLoadNextPage()
            is SearchIntent.SelectMovie -> onSelectMovie(intent.movieId)
        }
    }

    private fun mutate(mutation: SearchMutation) {
        log.d { "[MUTATION] $mutation" }
        state = state.reduce(mutation)
    }

    private fun onSelectMovie(movieId: String) {
        viewModelScope.launch {
            _events.emit(Event.MovieSelected(movieId))
        }
    }

    private fun onUpdateQuery(query: String) {
        mutate(SearchMutation.SetQuery(query))
        queryFlow.value = query
    }

    private fun onLoadNextPage() {
        val currentState = state
        if (!currentState.canPaginate) return

        viewModelScope.launch {
            mutate(SearchMutation.SetPagingLoading(true))
            searchMoviesUseCase(
                query = currentState.query,
                page = currentState.currentPage + 1,
            )
                .onSuccess { pageInfo -> mutate(SearchMutation.SetPageInfo(pageInfo)) }
                .onFailure { error -> mutate(SearchMutation.SetError(error)) }
            mutate(SearchMutation.SetPagingLoading(false))
        }
    }

    private suspend fun clearSearch() {
        clearMoviesUseCase()
        mutate(SearchMutation.SetPageInfo(PageInformation(currentPage = 0, totalPages = 1)))
        mutate(SearchMutation.SetError(null))
    }

    private suspend fun performSearch(query: String) {
        mutate(SearchMutation.SetSearchLoading(true))
        mutate(SearchMutation.SetError(null))
        clearMoviesUseCase()
        searchMoviesUseCase(query, page = 1)
            .onSuccess { pageInfo -> mutate(SearchMutation.SetPageInfo(pageInfo)) }
            .onFailure { error -> mutate(SearchMutation.SetError(error)) }
        mutate(SearchMutation.SetSearchLoading(false))
    }

    sealed interface Event {
        data class MovieSelected(val movieId: String) : Event
    }

}
