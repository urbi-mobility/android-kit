package co.urbi.android.kit.data_store.domain

import kotlinx.coroutines.flow.Flow
@Suppress("TooManyFunctions")
/**
 * Interface definition for a secure data store that manages preferences.
 * This interface provides methods to get and put various data types securely.
 */
interface PreferencesSecureDataStore {
    /**
     * Returns a Flow containing the map of all key-value pairs in the data store.
     */
    fun data(): Flow<Map<String, Any>>

    /**
     * Returns a Flow containing the String value associated with the given key, or null if not found.
     * @param key The key to retrieve.
     */
    fun getString(key: String): Flow<String?>

    /**
     * Returns a Flow containing the Int value associated with the given key, or null if not found.
     * @param key The key to retrieve.
     */
    fun getInt(key: String): Flow<Int?>

    /**
     * Returns a Flow containing the Boolean value associated with the given key, or null if not found.
     * @param key The key to retrieve.
     */
    fun getBoolean(key: String): Flow<Boolean?>

    /**
     * Returns a Flow containing the Long value associated with the given key, or null if not found.
     * @param key The key to retrieve.
     */
    fun getLong(key: String): Flow<Long?>

    /**
     * Returns a Flow containing the Float value associated with the given key, or null if not found.
     * @param key The key to retrieve.
     */
    fun getFloat(key: String): Flow<Float?>

    /**
     * Returns a Flow containing the Double value associated with the given key, or null if not found.
     * @param key The key to retrieve.
     */
    fun getDouble(key: String): Flow<Double?>

    /**
     * Returns a Flow containing the Set<String> value associated with the given key, or null if not found.
     * @param key The key to retrieve.
     */
    fun getStringSet(key: String): Flow<Set<String>?>

    /**
     * Saves a String value to the data store.
     * @param key The key to save.
     * @param value The value to save.
     */
    suspend fun putString(key: String, value: String)

    /**
     * Saves an Int value to the data store.
     * @param key The key to save.
     * @param value The value to save.
     */
    suspend fun putInt(key: String, value: Int)

    /**
     * Saves a Boolean value to the data store.
     * @param key The key to save.
     * @param value The value to save.
     */
    suspend fun putBoolean(key: String, value: Boolean)

    /**
     * Saves a Long value to the data store.
     * @param key The key to save.
     * @param value The value to save.
     */
    suspend fun putLong(key: String, value: Long)

    /**
     * Saves a Float value to the data store.
     * @param key The key to save.
     * @param value The value to save.
     */
    suspend fun putFloat(key: String, value: Float)

    /**
     * Saves a Double value to the data store.
     * @param key The key to save.
     * @param value The value to save.
     */
    suspend fun putDouble(key: String, value: Double)

    /**
     * Saves a Set<String> value to the data store.
     * @param key The key to save.
     * @param value The value to save.
     */
    suspend fun putStringSet(key: String, value: Set<String>)

    /**
     * Removes the value associated with the given key from the data store.
     * @param key The key to remove.
     */
    suspend fun remove(key: String)

    /**
     * Clears all data from the data store.
     */
    suspend fun clear()
}
