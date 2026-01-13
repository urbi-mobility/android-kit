package co.urbi.android.kit.data_store.domain

import kotlinx.coroutines.flow.Flow

interface PreferencesSecureDataStore {
    fun data(): Flow<Map<String, Any>>
    fun getString(key: String): Flow<String?>
    fun getInt(key: String): Flow<Int?>
    fun getBoolean(key: String): Flow<Boolean?>
    fun getLong(key: String): Flow<Long?>
    fun getFloat(key: String): Flow<Float?>
    fun getDouble(key: String): Flow<Double?>
    fun getStringSet(key: String): Flow<Set<String>?>
    suspend fun putString(key: String, value: String)
    suspend fun putInt(key: String, value: Int)
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun putLong(key: String, value: Long)
    suspend fun putFloat(key: String, value: Float)
    suspend fun putDouble(key: String, value: Double)
    suspend fun putStringSet(key: String, value: Set<String>)
    suspend fun remove(key: String)
    suspend fun clear()
}
