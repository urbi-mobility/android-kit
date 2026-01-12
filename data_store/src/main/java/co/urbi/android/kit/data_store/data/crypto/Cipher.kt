package co.urbi.android.kit.data_store.data.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import co.urbi.android.kit.data_store.domain.model.CipherSetup
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

internal class Cipher(private val setup: CipherSetup) : CryptoManager {
    private val cipher = Cipher.getInstance("${setup.algorithm}/${setup.blockMode}/${setup.padding}")
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private fun createKey(): SecretKey {
        return KeyGenerator
            .getInstance(setup.algorithm)
            .apply {
                init(
                    KeyGenParameterSpec.Builder(
                        setup.alias,
                        KeyProperties.PURPOSE_ENCRYPT or
                                KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(setup.blockMode)
                        .setEncryptionPaddings(setup.padding)
                        .setRandomizedEncryptionRequired(true)
                        .setUserAuthenticationRequired(false)
                        .build()
                )
            }
            .generateKey()
    }
    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(setup.alias, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    override fun encrypt(bytes: ByteArray): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(bytes)
        return iv+encryptedBytes
    }

    override fun decrypt(inputStream: InputStream): InputStream {
        val bytes = inputStream.readBytes()
        val iv = bytes.copyOfRange(0, cipher.blockSize)
        val data = bytes.copyOfRange(cipher.blockSize, bytes.size)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        val decryptedBytes = cipher.doFinal(data)
        return ByteArrayInputStream(decryptedBytes)
    }
}

