plugins {
    id("kmp-library")
}

android {
    namespace = "com.moviesafe.app"
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
