import org.gradle.kotlin.dsl.the
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")

}

private val libs = the<LibrariesForLibs>()

kotlin {
    android {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        withHostTest {
            isReturnDefaultValues = true
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.kmLog)
                implementation(libs.compose.ui)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.compose.ui.tooling.preview)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.coroutines.test)
            }
        }
    }
}

dependencies {
    // com.android.kotlin.multiplatform.library has no debug variant to hang ui-tooling
    // off of (unlike com.android.application/library's debugImplementation), so Compose
    // Preview rendering needs it on this classpath instead.
    add("androidRuntimeClasspath", libs.compose.ui.tooling)
}
