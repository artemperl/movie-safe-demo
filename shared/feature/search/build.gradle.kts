plugins {
    id("kmp-library")
    alias(libs.plugins.kotlinSerialization)
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
                implementation(project(":shared:network"))
            }
        }
    }
}
