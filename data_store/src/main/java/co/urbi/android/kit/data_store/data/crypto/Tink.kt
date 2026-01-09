package co.urbi.android.kit.data_store.data.crypto


import co.urbi.android.kit.data_store.domain.model.TinkSetup
import com.google.crypto.tink.Aead
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import java.io.ByteArrayInputStream
import java.io.InputStream

internal class Tink(
    setup: TinkSetup
) : CryptoManager {

    private val aead: Aead

    init {
        AeadConfig.register()
        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(setup.context.applicationContext, setup.keysetName, setup.keysetFileName)
            .withKeyTemplate(setup.keyTemplate)
            .withMasterKeyUri("android-keystore://${setup.keysetName}")
            .build()
            .keysetHandle
        aead = keysetHandle.getPrimitive(
            RegistryConfiguration.get(),
            Aead::class.java)
    }

    override fun encrypt(bytes: ByteArray): ByteArray {
        return aead.encrypt(bytes, null)
    }

    override fun decrypt(inputStream: InputStream): InputStream {
        val encryptedBytes = inputStream.readBytes()
        val decryptedBytes = aead.decrypt(encryptedBytes, null)
        return ByteArrayInputStream(decryptedBytes)
    }
}
