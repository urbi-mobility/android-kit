package co.urbi.android.kit.data_store.data

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import co.urbi.android.kit.data_store.domain.model.CryptoSetup
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


internal class Crypto(private val setup: CryptoSetup) {

    private val cipher =
        Cipher.getInstance("${setup.algorithm}/${setup.blockMode}/${setup.padding}")
    private val keyStore = KeyStore
        .getInstance("AndroidKeyStore").apply { load(null) }

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

    fun encrypt(bytes: ByteArray): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(bytes)
        return iv + encrypted
    }

    fun decrypt(bytes: ByteArray): ByteArray {
        val iv = bytes.copyOfRange(0, cipher.blockSize)
        val data = bytes.copyOfRange(cipher.blockSize, bytes.size)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        return cipher.doFinal(data)
    }
}
