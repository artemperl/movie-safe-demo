package com.moviesafe.usecase

import com.moviesafe.model.MovieDetail
import com.moviesafe.repository.DetailRepository

class GetMovieDetailUseCase(
    private val detailRepository: DetailRepository
) {

    suspend operator fun invoke(movieId: String): Result<MovieDetail> =
        detailRepository.getDetail(movieId)

}