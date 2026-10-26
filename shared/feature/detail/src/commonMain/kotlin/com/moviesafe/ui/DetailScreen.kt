package com.moviesafe.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onExit: () -> Unit
) {
    HandleEvents(
        viewModel = viewModel
    )

    DetailScreenContent()
}

@Composable
fun HandleEvents(
    viewModel: DetailViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                is DetailViewModel.Event.Error -> {
                    // TODO display error
                }
            }
        }
    }
}

internal fun DetailScreenContent() {

}