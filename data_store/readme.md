# Secure Data Store

 This library provides a secure and easy-to-use wrapper around Android's DataStore, supporting both encrypted and plain text storage. It offers two storage modes:
 - **SecureDataStore**: Type-safe Proto DataStore for structured objects using Kotlin Serialization
 - **PreferencesDataStore**: Key-value storage for simple preferences with string keys

 ## Features

 - **Type-safe**: SecureDataStore uses Kotlin Serialization to store and retrieve structured objects.
 - **Key-Value Storage**: PreferencesDataStore provides simple string-based key-value storage for preferences.
 - **Secure**: Built-in support for encryption using Android KeyStore, making it suitable for sensitive data like tokens.
 - **Reactive**: Exposes data as a Kotlin `Flow`, allowing for reactive UI updates.
 - **Simple API**: Fluent Builder pattern for configuration and instantiation.

 ## Usage

 ### SecureDataStore (Proto DataStore)

 Use SecureDataStore for storing structured objects with type safety.

 ### 1. Define your Data Model

 Create a data class and annotate it with `@Serializable`. This will be the structure of the data you want to store.

 ```kotlin
 @Serializable
 data class CredentialModel(
     val accessToken: String = "",
     val refreshToken: String = "",
 )
 ```

 ### 2. Initialize SecureDataStore

 You can initialize the `SecureDataStore` in two modes: **Encrypted** (for sensitive data) or **Raw** (for standard data).

 #### Encrypted Instance

 To enable encryption, pass a `CryptoSetup` object to the `.encrypt()` method of the builder. This uses the Android KeyStore system to securely manage keys.

 ```kotlin
fun getCipherInstance(context: Context): ProtoSecureDataStore<CredentialModel> {
    val setup = DataStoreSetup.Builder(
        file = FileType.CredentialProtectedFile(
            context = context,
            fileName = "urbi-kit-proto-cipher"
        ),
        type = DataStoreType.Proto(schema = CredentialModel())
    ).build()
    return SecureDataStore.Builder<ProtoSecureDataStore<CredentialModel>>(setup = setup)
        .encrypt(
            CipherSetup(
                alias = "urbi-kit-proto-cipher",
                algorithm = KeyProperties.KEY_ALGORITHM_AES,
                blockMode = KeyProperties.BLOCK_MODE_CBC,
                padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
        ).build()
}
 ```

 #### Raw (Unencrypted) Instance

 For data that doesn't require encryption, simply omit the `.encrypt()` call.

 ```kotlin
fun getRawInstance(context: Context): ProtoSecureDataStore<CredentialModel> {
    val setup = DataStoreSetup.Builder(
        file = FileType.CredentialProtectedFile(
            context = context,
            fileName = "urbi-kit-proto-raw"
        ),
        type = DataStoreType.Proto(schema = CredentialModel())
    ).build()
    return SecureDataStore.Builder<ProtoSecureDataStore<CredentialModel>>(setup = setup)
        .build()
}
 ```

 ### 3. Read Data

 Observe changes to the data using the `data()` flow. This is perfect for integration with Compose or other reactive UI frameworks.

 ```kotlin
 dataStore.data().collect { credential ->
     println("Access Token: ${credential.accessToken}")
 }
 ```

 ### 4. Update Data

 Update the data transactionally using the `updateData` suspend function.

 ```kotlin
dataStore.updateData { currentData ->
     currentData.copy(
         accessToken = newAccess,
         refreshToken = newRefresh
     )
 }
 ```

 ---

 ### PreferencesDataStore (Key-Value Storage)

 Use PreferencesDataStore for simple key-value storage with string keys. Perfect for user settings, flags, and simple preferences.

 #### 1. Initialize PreferencesDataStore

 You can initialize the `PreferencesDataStore` in two modes: **Encrypted** or **Unencrypted**.

 ##### Encrypted Instance

 To enable encryption, pass a `CryptoSetup` object to the `.encrypt()` method of the builder.

 ```kotlin
fun getCipherInstance(context: Context): PreferencesSecureDataStore {
    val setup = DataStoreSetup.Builder(
        file = FileType.CredentialProtectedFile(
            context = context,
            fileName = "urbi-kit-preferences-cipher"
        ),
        type = DataStoreType.Preferences
    ).build()
    return SecureDataStore.Builder<PreferencesSecureDataStore>(setup = setup).encrypt(
        CipherSetup(
            alias = "urbi-kit-preferences-cipher",
            algorithm = KeyProperties.KEY_ALGORITHM_AES,
            blockMode = KeyProperties.BLOCK_MODE_CBC,
            padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
        )
    ).build()
}
 ```

 ##### Unencrypted Instance

 For data that doesn't require encryption, simply omit the `.encrypt()` call.

 ```kotlin
fun getRawInstance(context: Context): PreferencesSecureDataStore {
    val setup = DataStoreSetup.Builder(
        file = FileType.CredentialProtectedFile(
            context = context,
            fileName = "urbi-kit-preferences-raw"
        ),
        type = DataStoreType.Preferences
    ).build()
    return SecureDataStore.Builder<PreferencesSecureDataStore>(setup = setup).build()
}
 ```

 #### 2. Write Data

 Store values with simple string keys using typed methods:

 ```kotlin
 // Store different types
 prefsDataStore.putString("username", "john_doe")
 prefsDataStore.putInt("user_id", 12345)
 prefsDataStore.putBoolean("is_logged_in", true)
 prefsDataStore.putLong("last_sync", System.currentTimeMillis())
 prefsDataStore.putFloat("rating", 4.5f)
 prefsDataStore.putDouble("balance", 1234.56)
 prefsDataStore.putStringSet("tags", setOf("tag1", "tag2", "tag3"))
 ```

 #### 3. Read Data

Observe changes to individual preferences using Flow:

 ```kotlin
 // Observe individual values
prefsDataStore.getString("username").collect { username ->
     println("Username: $username")
 }

prefsDataStore.getBoolean("is_logged_in").collect { isLoggedIn ->
     if (isLoggedIn == true) {
         // User is logged in
     }
 }

 // Observe all preferences
prefsDataStore.data().collect { allPrefs ->
     println("All preferences: $allPrefs")
 }
 ```

 #### 4. Remove and Clear

 Remove individual keys or clear all preferences:

 ```kotlin
 // Remove a specific key
 prefsDataStore.remove("username")

 // Clear all preferences
 prefsDataStore.clear()
 ```

 ## Dependencies

 This module uses the following libraries:
 - `androidx.datastore` for Proto DataStore storage mechanism.
 - `androidx.datastore:datastore-preferences-core` for Preferences DataStore.
 - `kotlinx.serialization` for JSON serialization/deserialization.
 - `kotlinx.coroutines` for asynchronous operations.
