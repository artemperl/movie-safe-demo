plugins {
    id("kmp-library")
}

android {
    namespace = "com.moviesafe.preferences"
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
