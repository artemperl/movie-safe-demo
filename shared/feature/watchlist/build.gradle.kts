plugins {
    id("kmp-library")
}

kotlin {
    android {
        namespace = "com.moviesafe.watchlist"
    }
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
