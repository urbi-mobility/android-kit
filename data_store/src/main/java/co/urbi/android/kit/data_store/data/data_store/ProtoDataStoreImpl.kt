package co.urbi.android.kit.data_store.data.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.deviceProtectedDataStoreFile
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import co.urbi.android.kit.data_store.data.crypto.CryptoManager
import co.urbi.android.kit.data_store.data.serializer.DataStoreSerializer
import co.urbi.android.kit.data_store.domain.ProtoSecureDataStore
import co.urbi.android.kit.data_store.domain.model.DataStoreSetup
import co.urbi.android.kit.data_store.domain.model.FileType
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import timber.log.Timber


internal class ProtoDataStoreImpl<T>(
    private val schema: T,
    private val setup: DataStoreSetup,
    private val cryptoManager: CryptoManager?,
) : ProtoSecureDataStore<T> {

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
        val serializer: KSerializer<T> = schema!!::class.serializer() as KSerializer<T>
        return DataStoreFactory.create(
            serializer = DataStoreSerializer(
                default = schema,
                cryptoManager = cryptoManager,
                serializer = serializer
            ),
            produceFile = {
                when (val file = setup.file) {
                    is FileType.CredentialProtectedFile -> {
                        file.context.dataStoreFile(fileName = file.fileName)
                    }

                    is FileType.DeviceProtectedFile -> {
                        file.context.deviceProtectedDataStoreFile(fileName = file.fileName)
                    }

                    is FileType.CustomProtectedFile -> {
                        file.file
                    }
                }
            },
            corruptionHandler = ReplaceFileCorruptionHandler { exception ->
                Timber.tag("PreferencesDataStore").e(
                    t = exception,
                    message = "Preferences DataStore corruption detected. Replacing with empty preferences."
                )
                schema
            }
        )
    }
}
