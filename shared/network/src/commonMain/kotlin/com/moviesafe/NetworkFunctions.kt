package com.moviesafe

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> HttpResponse.handleResponse(): Result<T> {
    return when (status.value) {
        in 200..299 -> {
            try {
                val result = if (T::class == Unit::class) Unit as T else body<T>()
                Result.success(result)
            } catch (e: SerializationException) {
                // TODO more specific error/codes
                Result.failure(NetworkException("Serialization error: ${e.message}"))
            }
        }

        else -> {
            val errorBody = bodyAsText()
            // TODO more specific error/codes
            Result.failure(NetworkException("Error ${status.value}: $errorBody"))
        }
    }
}

suspend inline fun <reified T> HttpClient.safeRequest(
    block: suspend HttpClient.() -> HttpResponse
): Result<T> {
    return try {
        val response = block()
        response.handleResponse<T>()
    } catch (e: Exception) {
        // TODO more specific error/codes
        Result.failure(NetworkException("Network error: ${e.message}"))
    }
}