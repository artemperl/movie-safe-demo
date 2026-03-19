package com.moviesafe

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import kotlinx.serialization.KSerializer
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class AppModuleTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkKoinModule() {
        appModule.verify(
            extraTypes = listOf(
                KSerializer::class,
                HttpClientEngine::class,
                HttpClientConfig::class
            )
        )
    }

}
