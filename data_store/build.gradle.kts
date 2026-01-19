apply {
    from("$rootDir/base-module.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
}

android {
    namespace = DataStore.NAMESPACE
}


dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.jetbrains.kotlinx.serialization.json)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}

dependencies{
    implementation(libs.tink)
}