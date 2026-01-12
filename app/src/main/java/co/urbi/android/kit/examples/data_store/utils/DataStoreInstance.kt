package co.urbi.android.kit.examples.data_store.utils

import android.content.Context
import android.security.keystore.KeyProperties
import co.urbi.android.kit.data_store.domain.PreferencesDataStore
import co.urbi.android.kit.data_store.domain.SecureDataStore
import co.urbi.android.kit.data_store.domain.model.CipherSetup
import co.urbi.android.kit.data_store.domain.model.DataStoreSetup
import co.urbi.android.kit.data_store.domain.model.FileType
import co.urbi.android.kit.data_store.domain.model.TinkSetup

object DataStoreInstance {

    object Proto {
        fun getCipherInstance(context: Context): SecureDataStore<UserModel> {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(context, "urbi-kit-encrypted")
            ).build()
            return SecureDataStore.Builder(
                default = UserModel(),
                setup = setup,
            ).encrypt(
                CipherSetup(
                    alias = "urbi-kit-encrypted",
                    algorithm = KeyProperties.KEY_ALGORITHM_AES,
                    blockMode = KeyProperties.BLOCK_MODE_CBC,
                    padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
                )
            ).build()
        }

        fun getTinkInstance(context: Context): SecureDataStore<UserModel> {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(context, "urbi-kit-encrypted")
            ).build()
            return SecureDataStore.Builder(
                default = UserModel(),
                setup = setup,
            ).encrypt(
                TinkSetup(
                    context = context.applicationContext,
                    keysetName = "urbi-kit-tink-encrypted",
                    keysetFileName = "urbi-kit-tink-encrypted",
                )
            ).build()
        }

        fun getRawInstance(context: Context): SecureDataStore<UserModel> {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(context, "urbi-kit-encrypted")
            ).build()
            return SecureDataStore.Builder(
                default = UserModel(),
                setup = setup,
            ).build()
        }

    }

    object Preferences {
        fun getCipherInstance(context: Context): PreferencesDataStore {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(
                    context,
                    "urbi-kit-encrypted"
                )
            ).build()
            return PreferencesDataStore.Builder(setup = setup).encrypt(
                CipherSetup(
                    alias = "urbi-kit-encrypted",
                    algorithm = KeyProperties.KEY_ALGORITHM_AES,
                    blockMode = KeyProperties.BLOCK_MODE_CBC,
                    padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
                )
            ).build()
        }

        fun getTinkInstance(context: Context): PreferencesDataStore {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(context, "urbi-kit-encrypted")
            ).build()
            return PreferencesDataStore.Builder(setup = setup).encrypt(
                TinkSetup(
                    context = context.applicationContext,
                    keysetName = "urbi-kit-tink-encrypted",
                    keysetFileName = "urbi-kit-tink-encrypted",
                )
            ).build()
        }

        fun getRawInstance(context: Context): PreferencesDataStore {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(context, "urbi-kit-encrypted")
            ).build()
            return PreferencesDataStore.Builder(setup = setup).build()
        }
    }
}