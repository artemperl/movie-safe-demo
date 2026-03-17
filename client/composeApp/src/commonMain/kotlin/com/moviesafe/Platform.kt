package com.moviesafe

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform