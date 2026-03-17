plugins {
    id("kmp-library")
}

android {
    namespace = "com.moviesafe.watchlist"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koin.core)
            }
        }
    }
}
