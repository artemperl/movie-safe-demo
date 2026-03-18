package com.moviesafe.fake

import com.moviesafe.usecase.ClearMoviesUseCase

class FakeClearMoviesUseCase : ClearMoviesUseCase(repository = FakeSearchRepository()) {

    var callCount = 0

    override suspend fun invoke() {
        callCount++
    }
}
