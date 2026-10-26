package com.moviesafe

import kotlinx.coroutines.flow.Flow

interface MSDataStore<T> {
    val data: Flow<T?>
    suspend fun updateData(update: (T?) -> T?): Result<T?>
}