package co.urbi.android.kit.data_store.data.crypto

import java.io.InputStream


internal interface CryptoManager {
    fun encrypt(bytes: ByteArray): ByteArray
    fun decrypt(inputStream: InputStream): InputStream
}
