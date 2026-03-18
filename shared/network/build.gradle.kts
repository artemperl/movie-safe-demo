import java.util.Properties

plugins {
    id("kmp-library")
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use(::load)
}

val generateTmdbCredentials by tasks.registering {
    val token = localProperties.getProperty("tmdb.access.token")
        ?: error("tmdb.access.token not found in local.properties")
    val outputDir = layout.buildDirectory.dir("generated/tmdbCredentials/commonMain/kotlin")
    outputs.dir(outputDir)
    doLast {
        val dir = outputDir.get().asFile.also { it.mkdirs() }.resolve("com/moviesafe").also { it.mkdirs() }
        dir.resolve("TmdbCredentials.kt").writeText(
            """
            package com.moviesafe

            internal const val TMDB_ACCESS_TOKEN = "$token"
            """.trimIndent()
        )
    }
}

kotlin {
    android {
        namespace = "com.moviesafe.network"
    }
}

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir(generateTmdbCredentials.map { it.outputs.files.singleFile })
            dependencies {
                api(libs.ktor.client.core)
                api(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.kotlinx)
                implementation(libs.ktor.client.content.negotiation)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}
