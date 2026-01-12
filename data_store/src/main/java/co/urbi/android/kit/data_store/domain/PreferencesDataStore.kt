package co.urbi.android.kit.data_store.domain

import co.urbi.android.kit.data_store.data.PreferencesDataStoreImpl
import co.urbi.android.kit.data_store.data.crypto.Cipher
import co.urbi.android.kit.data_store.data.crypto.Tink
import co.urbi.android.kit.data_store.domain.model.CipherSetup
import co.urbi.android.kit.data_store.domain.model.DataStoreSetup
import co.urbi.android.kit.data_store.domain.model.TinkSetup
import kotlinx.coroutines.flow.Flow

interface PreferencesDataStore {
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

    class Builder(private val setup: DataStoreSetup) {
        internal var cipherSetup: CipherSetup? = null
        internal var tinkSetup: TinkSetup? = null
        fun encrypt(cipher: CipherSetup):Builder {
            cipherSetup = cipher
            return this
        }

        fun encrypt(tink: TinkSetup): Builder {
            tinkSetup = tink
            return this
        }

        fun build(): PreferencesDataStore {
            return PreferencesDataStoreImpl(
                setup = setup,
                cryptoManager = when {
                    cipherSetup != null -> cipherSetup?.let { Cipher(setup = it) }
                    tinkSetup != null -> tinkSetup?.let { Tink(setup = it) }
                    else -> null
                }
            )
        }
    }
}
