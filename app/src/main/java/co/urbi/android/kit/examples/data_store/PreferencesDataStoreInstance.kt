package co.urbi.android.kit.examples.data_store

import android.content.Context
import android.security.keystore.KeyProperties
import co.urbi.android.kit.data_store.domain.PreferencesDataStore
import co.urbi.android.kit.data_store.domain.model.CryptoSetup

object PreferencesDataStoreInstance {

    fun getEncryptedInstance(context: Context): PreferencesDataStore {
        return PreferencesDataStore.Builder(
            context = context,
            name = "urbi-kit-prefs-encrypted"
        ).encrypt(
            CryptoSetup(
                alias = "urbi-kit-prefs-key",
                algorithm = KeyProperties.KEY_ALGORITHM_AES,
                blockMode = KeyProperties.BLOCK_MODE_CBC,
                padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
        ).build()
    }

    fun getRawInstance(context: Context): PreferencesDataStore {
        return PreferencesDataStore.Builder(
            context = context,
            name = "urbi-kit-prefs-raw"
        ).build()
    }

}

