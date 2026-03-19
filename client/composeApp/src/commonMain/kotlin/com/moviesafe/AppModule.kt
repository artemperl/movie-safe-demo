package com.moviesafe

import org.koin.dsl.module

val appModule = module {
    includes(searchModule)
}