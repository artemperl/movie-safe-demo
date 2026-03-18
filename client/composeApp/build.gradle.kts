plugins {
    id("kmp-library")
}

kotlin {
    android {
        namespace = "com.moviesafe.app"
    }

    iosArm64 {
        binaries.framework {
            baseName = "MovieSafe"
            isStatic = true
        }
    }
    iosSimulatorArm64 {
        binaries.framework {
            baseName = "MovieSafe"
            isStatic = true
        }
    }
    iosX64 {
        binaries.framework {
            baseName = "MovieSafe"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.koin.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
