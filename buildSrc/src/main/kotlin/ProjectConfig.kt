import org.gradle.api.JavaVersion

object ProjectConfig {
    const val COMPILE_SDK = 36
    const val APPLICATION_ID = "co.urbi.android.kit"
    const val MIN_SDK = 26
    const val TARGET_SDK = 36
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0.0"
    const val JVM_TARGET = "21"
    val javaSourceCompatibility = JavaVersion.VERSION_21
    val javaTargetCompatibility = JavaVersion.VERSION_21
}