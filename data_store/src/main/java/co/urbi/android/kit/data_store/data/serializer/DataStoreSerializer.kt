package co.urbi.android.kit.data_store.data.serializer

import androidx.datastore.core.Serializer
import co.urbi.android.kit.data_store.data.crypto.CryptoManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


internal class DataStoreSerializer<T>(
    private val default: T,
    private val cryptoManager: CryptoManager?,
    private val serializer: KSerializer<T>
) :
    Serializer<T> {
    override val defaultValue: T
        get() = default

    override suspend fun readFrom(input: InputStream): T {
        val inputString = withContext(Dispatchers.IO) {
            val inputStream = cryptoManager?.decrypt(inputStream = input) ?: input
            inputStream.use { it.readBytes() }
        }.decodeToString()
        return Json.decodeFromString(serializer, inputString)
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        val inputString = Json.encodeToString(serializer, t)
        val bytes = inputString.toByteArray()
        withContext(Dispatchers.IO) {
            val info = cryptoManager?.encrypt(bytes = bytes) ?: bytes
            output.use {
                it.write(info)
            }
        }
    }
}
