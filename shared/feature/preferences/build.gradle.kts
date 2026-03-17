plugins {
    id("kmp-library")
}

kotlin {
    android {
        namespace = "com.moviesafe.preferences"
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
