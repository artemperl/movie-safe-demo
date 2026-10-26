package com.moviesafe.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diamondedge.logging.logging
import com.moviesafe.usecase.GetMovieDetailUseCase
import io.ktor.events.Events
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class DetailViewModel(
    private val movieId: String,
    getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    private val _events = Channel<Event>(capacity = Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    var state by mutableStateOf(DetailState())
        private set

    private val log = logging()

    init {
        viewModelScope.launch {
            getMovieDetailUseCase(movieId).fold(
                onSuccess = { movieDetail ->
                    state.reduce(DetailMutation.LoadingSuccess(
                        movieDetail = movieDetail
                    ))
                },
                onFailure = { error ->
                    log.e(err = error, msg = { "unexpected error" })
                    state.reduce(DetailMutation.LoadingFailure(error))
                }
            )
        }
    }

    sealed interface Event {
        data class Error(val error: Throwable): Event
    }

}