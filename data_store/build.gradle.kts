apply {
    from("$rootDir/base-module.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.sonatype.central.portal)
}

android {
    namespace = DataStore.NAMESPACE

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

group = DataStore.GROUP_ID
version = ProjectConfig.VERSION_NAME

dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.jetbrains.kotlinx.serialization.json)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}

dependencies {
    implementation(libs.tink)
}

// Helper function: ENV variable first, then gradle.properties as fallback
fun getEnvOrProperty(envName: String, propertyName: String): String? {
    return System.getenv(envName) ?: findProperty(propertyName) as String?
}

// Get credentials: ENV variable first, then gradle.properties as fallback
val mavenUsername = getEnvOrProperty("MAVEN_CENTRAL_USERNAME", "mavenCentralUsername")
val mavenPassword = getEnvOrProperty("MAVEN_CENTRAL_PASSWORD", "mavenCentralPassword")
val signingKeyId = getEnvOrProperty("SIGNING_KEY_ID", "signing.keyId")
val signingPassword = getEnvOrProperty("SIGNING_PASSWORD", "signing.password")
val signingKey = getEnvOrProperty("SIGNING_KEY", "signing.key")
val signingKeyFile = getEnvOrProperty("SIGNING_SECRET_KEY_FILE", "signing.secretKeyRingFile")

// Set signing properties for the signing plugin used by central portal publisher
if (signingKeyId != null && signingPassword != null && signingKeyFile != null) {
    extra["signing.keyId"] = signingKeyId
    extra["signing.password"] = signingPassword
    extra["signing.secretKeyRingFile"] = signingKeyFile
}

// Configure Sonatype Central Portal publishing
centralPortal {
    username = mavenUsername
    password = mavenPassword
    
    // Override the artifact name (default is module name)
    name = DataStore.ARTIFACT_ID
    
    // POM metadata configuration
    pom {
        name = DataStore.POM_NAME
        description = DataStore.POM_DESCRIPTION
        url = DataStore.POM_URL

        licenses {
            license {
                name = DataStore.LICENSE_NAME
                url = DataStore.LICENSE_URL
            }
        }

        developers {
            developer {
                id = DataStore.DEVELOPER_ID
                name = DataStore.DEVELOPER_NAME
                email = DataStore.DEVELOPER_EMAIL
            }
        }

        scm {
            connection = DataStore.SCM_CONNECTION
            developerConnection = DataStore.SCM_DEV_CONNECTION
            url = DataStore.SCM_URL
        }
    }
    
    // Use in-memory signing if SIGNING_KEY env var is provided
    if (signingKey != null && signingKeyId != null && signingPassword != null) {
        signing {
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        }
    }
    
    // USER_MANAGED = Upload to staging, you release manually on portal
    // AUTOMATIC = Upload and release automatically
    publishingType = net.thebugmc.gradle.sonatypepublisher.PublishingType.USER_MANAGED
}