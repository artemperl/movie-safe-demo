plugins {
    id("kmp-library")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        namespace = "com.moviesafe.persistence"
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.dataStore)
                implementation(libs.dataStore.preferences)
            }
        }

    }
}
