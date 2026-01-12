package co.urbi.android.kit.examples.data_store

import android.content.Context
import android.security.keystore.KeyProperties
import co.urbi.android.kit.data_store.domain.SecureDataStore
import co.urbi.android.kit.data_store.domain.model.CipherSetup
import co.urbi.android.kit.data_store.domain.model.TinkSetup
import java.io.File


object DataStoreInstance {

    fun getCipherInstance(context: Context): SecureDataStore<UserModel> {
        return SecureDataStore.Builder(
            default = UserModel(),
            file = File(context.filesDir, "urbi-kit-encrypted.pb"),
        ).encrypt(
            CipherSetup(
                alias = "urbi-kit-encrypted",
                algorithm = KeyProperties.KEY_ALGORITHM_AES,
                blockMode = KeyProperties.BLOCK_MODE_CBC,
                padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
        ).build()
    }

    fun getTinkInstance(context: Context): SecureDataStore<UserModel>{
        return SecureDataStore.Builder(
            default = UserModel(),
            file = File(context.filesDir, "urbi-kit-tink-encrypted.pb"),
        ).encrypt(
            TinkSetup(
                context = context.applicationContext,
                keysetName = "urbi-kit-tink-encrypted",
                keysetFileName = "urbi-kit-tink-encrypted",
            )
        ).build()
    }

    fun getRawInstance(context: Context): SecureDataStore<UserModel> {
        return SecureDataStore.Builder(
            default = UserModel(),
            file = File(context.filesDir, "urbi-kit-raw.pb"),
        ).build()
    }

}
