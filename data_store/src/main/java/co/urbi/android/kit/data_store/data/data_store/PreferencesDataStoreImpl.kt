package co.urbi.android.kit.data_store.data.data_store


import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.deviceProtectedDataStoreFile
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import co.urbi.android.kit.data_store.data.crypto.CryptoManager
import co.urbi.android.kit.data_store.data.serializer.PreferencesSerializer
import co.urbi.android.kit.data_store.domain.PreferencesSecureDataStore
import co.urbi.android.kit.data_store.domain.model.DataStoreSetup
import co.urbi.android.kit.data_store.domain.model.FileType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class PreferencesDataStoreImpl(
    private val setup: DataStoreSetup,
    private val cryptoManager: CryptoManager?,
) : PreferencesSecureDataStore {

    private val dataStore: DataStore<Preferences> by lazy {
        DataStoreFactory.create(
            serializer = PreferencesSerializer(cryptoManager),
            produceFile = {  when (val file = setup.file) {
                is FileType.CredentialProtectedFile -> file.context.dataStoreFile(fileName = file.fileName)
                is FileType.DeviceProtectedFile -> file.context.deviceProtectedDataStoreFile(fileName = file.fileName)
                is FileType.CustomProtectedFile -> file.file
            } }
        )
    }

    override fun data(): Flow<Map<String, Any>> {
        return dataStore.data.map { preferences ->
            preferences.asMap().mapKeys { it.key.name }
        }
    }

    override fun getString(key: String): Flow<String?> {
        return dataStore.data.map { it[stringPreferencesKey(key)] }
    }

    override fun getInt(key: String): Flow<Int?> {
        return dataStore.data.map { it[intPreferencesKey(key)] }
    }

    override fun getBoolean(key: String): Flow<Boolean?> {
        return dataStore.data.map { it[booleanPreferencesKey(key)] }
    }

    override fun getLong(key: String): Flow<Long?> {
        return dataStore.data.map { it[longPreferencesKey(key)] }
    }

    override fun getFloat(key: String): Flow<Float?> {
        return dataStore.data.map { it[floatPreferencesKey(key)] }
    }

    override fun getDouble(key: String): Flow<Double?> {
        return dataStore.data.map { it[doublePreferencesKey(key)] }
    }

    override fun getStringSet(key: String): Flow<Set<String>?> {
        return dataStore.data.map { it[stringSetPreferencesKey(key)] }
    }

    override suspend fun putString(key: String, value: String) {
        dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    override suspend fun putInt(key: String, value: Int) {
        dataStore.edit { it[intPreferencesKey(key)] = value }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        dataStore.edit { it[booleanPreferencesKey(key)] = value }
    }

    override suspend fun putLong(key: String, value: Long) {
        dataStore.edit { it[longPreferencesKey(key)] = value }
    }

    override suspend fun putFloat(key: String, value: Float) {
        dataStore.edit { it[floatPreferencesKey(key)] = value }
    }

    override suspend fun putDouble(key: String, value: Double) {
        dataStore.edit { it[doublePreferencesKey(key)] = value }
    }

    override suspend fun putStringSet(key: String, value: Set<String>) {
        dataStore.edit { it[stringSetPreferencesKey(key)] = value }
    }

    override suspend fun remove(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
            preferences.remove(intPreferencesKey(key))
            preferences.remove(booleanPreferencesKey(key))
            preferences.remove(longPreferencesKey(key))
            preferences.remove(floatPreferencesKey(key))
            preferences.remove(doublePreferencesKey(key))
            preferences.remove(stringSetPreferencesKey(key))
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
