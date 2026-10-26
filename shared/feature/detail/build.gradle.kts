plugins {
    id("kmp-library")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.moviesafe.detail"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:library:designsystem"))
                implementation(project(":shared:network"))
                implementation(project(":shared:persistence"))
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)
            }
        }
    }
}
