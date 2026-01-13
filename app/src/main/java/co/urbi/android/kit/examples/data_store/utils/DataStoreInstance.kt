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
        fun getCipherInstance(context: Context): SecureDataStore<CredentialModel> {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(context, "urbi-kit-proto-cipher")
            ).build()
            return SecureDataStore.Builder(
                default = CredentialModel(),
                setup = setup,
            ).encrypt(
                CipherSetup(
                    alias = "urbi-kit-proto-cipher",
                    algorithm = KeyProperties.KEY_ALGORITHM_AES,
                    blockMode = KeyProperties.BLOCK_MODE_CBC,
                    padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
                )
            ).build()
        }

        fun getTinkInstance(context: Context): SecureDataStore<CredentialModel> {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(context, "urbi-kit-proto-tink")
            ).build()
            return SecureDataStore.Builder(
                default = CredentialModel(),
                setup = setup,
            ).encrypt(
                TinkSetup(
                    context = context.applicationContext,
                    keysetName = "urbi-kit-proto-tink",
                    keysetFileName = "urbi-kit-proto-tink",
                )
            ).build()
        }

        fun getRawInstance(context: Context): SecureDataStore<CredentialModel> {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(context, "urbi-kit-proto-raw")
            ).build()
            return SecureDataStore.Builder(
                default = CredentialModel(),
                setup = setup,
            ).build()
        }

    }

    object Preferences {
        fun getCipherInstance(context: Context): PreferencesDataStore {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(
                    context=context,
                    fileName = "urbi-kit-preferences-cipher"
                )
            ).build()
            return PreferencesDataStore.Builder(setup = setup).encrypt(
                CipherSetup(
                    alias = "urbi-kit-preferences-cipher",
                    algorithm = KeyProperties.KEY_ALGORITHM_AES,
                    blockMode = KeyProperties.BLOCK_MODE_CBC,
                    padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
                )
            ).build()
        }

        fun getTinkInstance(context: Context): PreferencesDataStore {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(context, "urbi-kit-preferences-tink")
            ).build()
            return PreferencesDataStore.Builder(setup = setup).encrypt(
                TinkSetup(
                    context = context.applicationContext,
                    keysetName = "urbi-kit-preferences-tink",
                    keysetFileName = "urbi-kit-preferences-tink",
                )
            ).build()
        }

        fun getRawInstance(context: Context): PreferencesDataStore {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(context, "urbi-kit-preferences-raw")
            ).build()
            return PreferencesDataStore.Builder(setup = setup).build()
        }
    }
}
