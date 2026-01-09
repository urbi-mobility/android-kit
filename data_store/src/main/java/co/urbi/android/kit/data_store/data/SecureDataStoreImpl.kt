package co.urbi.android.kit.data_store.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import co.urbi.android.kit.data_store.data.crypto.CryptoManager
import co.urbi.android.kit.data_store.domain.SecureDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import java.io.File


internal class SecureDataStoreImpl<T>(
    private val default: T,
    private val file: File,
    private val cryptoManager: CryptoManager?,
) : SecureDataStore<T> {

    private val dataStore: DataStore<T> by lazy { provideDataStore() }

    override fun data(): Flow<T> {
        return dataStore.data
    }

    override suspend fun updateData(transform: suspend (t: T) -> T): T {
        return dataStore.updateData(transform)
    }

    @OptIn(InternalSerializationApi::class)
    private fun provideDataStore(): DataStore<T> {
        @Suppress("UNCHECKED_CAST")
        val serializer: KSerializer<T> = default!!::class.serializer() as KSerializer<T>
        return DataStoreFactory.create(
            serializer = DataStoreSerializer(
                default = default,
                cryptoManager = cryptoManager,
                serializer = serializer
            ),
            produceFile = { file }
        )
    }
}
