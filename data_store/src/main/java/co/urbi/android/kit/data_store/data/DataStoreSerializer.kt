package co.urbi.android.kit.data_store.data

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64


internal class DataStoreSerializer<T>(
    private val default: T,
    private val crypto: Crypto?,
    private val serializer: KSerializer<T>
) :
    Serializer<T> {
    override val defaultValue: T
        get() = default

    override suspend fun readFrom(input: InputStream): T {
        val inputBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }
        val jsonString =
            crypto?.decryption(inputBytes)?.decodeToString() ?: inputBytes.decodeToString()
        return Json.decodeFromString(serializer, jsonString)
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        val json = Json.encodeToString(serializer, t)
        val bytes = json.toByteArray()
        val outputBytes = crypto?.encryption(bytes) ?: bytes
        withContext(Dispatchers.IO) {
            output.use {
                it.write(outputBytes)
            }
        }
    }

    private fun Crypto.encryption(bytes: ByteArray): ByteArray {
        val encryptedBytes = this.encrypt(bytes)
        return Base64.getEncoder().encode(encryptedBytes)
    }

    private fun Crypto.decryption(bytes: ByteArray): ByteArray {
        val encryptedBytesDecoded = Base64.getDecoder().decode(bytes)
        return this.decrypt(encryptedBytesDecoded)
    }
}
