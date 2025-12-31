
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false

    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.detekt)
}

subprojects {
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
    detekt {
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
    }
}

buildscript {
    dependencies {
        classpath (libs.jetbrains.kotlin.seralization)
        classpath(libs.google.dagger.hilt.android.gradle.plugin)
    }
}
