apply {
    from("$rootDir/base-module.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
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
