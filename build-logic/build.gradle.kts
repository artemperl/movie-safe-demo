plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.androidGradlePlugin)
    implementation(libs.composeGradlePlugin)
    implementation(libs.composeCompilerGradlePlugin)

    implementation(files((libs as Any).javaClass.superclass.protectionDomain.codeSource.location))
}