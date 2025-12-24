import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = ProjectConfig.javaSourceCompatibility
    targetCompatibility = ProjectConfig.javaTargetCompatibility
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(ProjectConfig.JVM_TARGET)
    }
}
