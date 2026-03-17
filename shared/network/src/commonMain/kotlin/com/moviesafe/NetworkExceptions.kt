package com.moviesafe

class NetworkException(message: String? = null, cause: Throwable? = null) :
    Exception(message = message, cause = cause)