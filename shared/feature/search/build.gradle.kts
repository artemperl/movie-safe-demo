plugins {
    id("kmp-library")
}

kotlin {
    android {
        namespace = "com.moviesafe.search"
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
            }
        }
    }
}
