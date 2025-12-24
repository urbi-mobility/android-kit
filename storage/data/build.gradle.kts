apply {
    from("$rootDir/base-module.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = Storage.DATA_NAMESPACE
}


dependencies {
    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.gson)
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.jetbrains.kotlinx.serialization.json)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
