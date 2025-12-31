# Secure Data Store
​
 This library provides a secure and easy-to-use wrapper around Android's DataStore, supporting both encrypted and plain text storage. It leverages Kotlin Serialization for type-safe data persistence and simplifies the setup process for secure storage.
​
 ## Features
​
 - **Type-safe**: Uses Kotlin Serialization to store and retrieve structured objects.
 - **Secure**: Built-in support for encryption using Android KeyStore, making it suitable for sensitive data like tokens.
 - **Reactive**: Exposes data as a Kotlin `Flow`, allowing for reactive UI updates.
 - **Simple API**: Fluent Builder pattern for configuration and instantiation.
​
 ## Usage
​
 ### 1. Define your Data Model
​
 Create a data class and annotate it with `@Serializable`. This will be the structure of the data you want to store.
​
 ```kotlin
 import kotlinx.serialization.Serializable
​
 @Serializable
 data class UserModel(
     val accessToken: String = "",
     val refreshToken: String = "",
 )
 ```
​
 ### 2. Initialize SecureDataStore
​
 You can initialize the `SecureDataStore` in two modes: **Encrypted** (for sensitive data) or **Raw** (for standard data).
​
 #### Encrypted Instance
​
 To enable encryption, pass a `CryptoSetup` object to the `.encrypt()` method of the builder. This uses the Android KeyStore system to securely manage keys.
​
 ```kotlin
 import android.security.keystore.KeyProperties
 import co.urbi.android.kit.data_store.domain.SecureDataStore
 import co.urbi.android.kit.data_store.domain.model.CryptoSetup
 import java.io.File
​
 val secureDataStore = SecureDataStore.Builder(
     default = UserModel(),
     file = File(context.filesDir, "user_data_encrypted.pb")
 ).encrypt(
     CryptoSetup(
         alias = "my_secure_key_alias",
         algorithm = KeyProperties.KEY_ALGORITHM_AES,
         blockMode = KeyProperties.BLOCK_MODE_CBC,
         padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
     )
 ).build()
 ```
​
 #### Raw (Unencrypted) Instance
​
 For data that doesn't require encryption, simply omit the `.encrypt()` call.
​
 ```kotlin
 val rawDataStore = SecureDataStore.Builder(
     default = UserModel(),
     file = File(context.filesDir, "user_prefs.pb")
 ).build()
 ```
​
 ### 3. Read Data
​
 Observe changes to the data using the `data()` flow. This is perfect for integration with Compose or other reactive UI frameworks.
​
 ```kotlin
 dataStore.data().collect { userModel ->
     println("Access Token: ${userModel.accessToken}")
 }
 ```
​
 ### 4. Update Data
​
 Update the data transactionally using the `updateData` suspend function.
​
 ```kotlin
     dataStore.updateData { currentData ->
         currentData.copy(
             accessToken = newAccess,
             refreshToken = newRefresh
         )
     }
 ```
​
 ## Dependencies
​
 This module uses the following libraries:
 - `androidx.datastore` for the underlying storage mechanism.
 - `kotlinx.serialization` for JSON serialization/deserialization.
 - `kotlinx.coroutines` for asynchronous operations.
