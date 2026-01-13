package co.urbi.android.kit.examples.data_store.utils

import android.content.Context
import android.security.keystore.KeyProperties
import co.urbi.android.kit.data_store.domain.PreferencesSecureDataStore
import co.urbi.android.kit.data_store.domain.ProtoSecureDataStore
import co.urbi.android.kit.data_store.domain.SecureDataStore
import co.urbi.android.kit.data_store.domain.model.CipherSetup
import co.urbi.android.kit.data_store.domain.model.DataStoreSetup
import co.urbi.android.kit.data_store.domain.model.DataStoreType
import co.urbi.android.kit.data_store.domain.model.FileType
import co.urbi.android.kit.data_store.domain.model.TinkSetup

object DataStoreInstance {

    object Proto {
        fun getCipherInstance(context: Context): ProtoSecureDataStore<CredentialModel> {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(
                    context = context,
                    fileName = "urbi-kit-proto-cipher"
                ),
                type = DataStoreType.Proto(schema = CredentialModel())
            ).build()
            return SecureDataStore.Builder<ProtoSecureDataStore<CredentialModel>>(setup = setup)
                .encrypt(
                    CipherSetup(
                        alias = "urbi-kit-proto-cipher",
                        algorithm = KeyProperties.KEY_ALGORITHM_AES,
                        blockMode = KeyProperties.BLOCK_MODE_CBC,
                        padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
                    )
                ).build()
        }

        fun getTinkInstance(context: Context): ProtoSecureDataStore<CredentialModel> {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(
                    context = context,
                    fileName = "urbi-kit-proto-tink"
                ),
                type = DataStoreType.Proto(schema = CredentialModel())
            ).build()
            return SecureDataStore.Builder<ProtoSecureDataStore<CredentialModel>>(setup = setup)
                .encrypt(
                    TinkSetup(
                        context = context.applicationContext,
                        keysetName = "urbi-kit-proto-tink",
                        keysetFileName = "urbi-kit-proto-tink",
                    )
                ).build()
        }

        fun getRawInstance(context: Context): ProtoSecureDataStore<CredentialModel> {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(
                    context = context,
                    fileName = "urbi-kit-proto-raw"
                ),
                type = DataStoreType.Proto(schema = CredentialModel())
            ).build()
            return SecureDataStore.Builder<ProtoSecureDataStore<CredentialModel>>(setup = setup)
                .build()
        }

    }

    object Preferences {
        fun getCipherInstance(context: Context): PreferencesSecureDataStore {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(
                    context = context,
                    fileName = "urbi-kit-preferences-cipher"
                ),
                type = DataStoreType.Preferences
            ).build()
            return SecureDataStore.Builder<PreferencesSecureDataStore>(setup = setup).encrypt(
                CipherSetup(
                    alias = "urbi-kit-preferences-cipher",
                    algorithm = KeyProperties.KEY_ALGORITHM_AES,
                    blockMode = KeyProperties.BLOCK_MODE_CBC,
                    padding = KeyProperties.ENCRYPTION_PADDING_PKCS7
                )
            ).build()
        }

        fun getTinkInstance(context: Context): PreferencesSecureDataStore {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(
                    context = context,
                    fileName = "urbi-kit-preferences-tink"
                ),
                type = DataStoreType.Preferences
            ).build()
            return SecureDataStore.Builder<PreferencesSecureDataStore>(setup = setup).encrypt(
                TinkSetup(
                    context = context.applicationContext,
                    keysetName = "urbi-kit-preferences-tink",
                    keysetFileName = "urbi-kit-preferences-tink",
                )
            ).build()
        }

        fun getRawInstance(context: Context): PreferencesSecureDataStore {
            val setup = DataStoreSetup.Builder(
                file = FileType.CredentialProtectedFile(
                    context = context,
                    fileName = "urbi-kit-preferences-raw"
                ),
                type = DataStoreType.Preferences
            ).build()
            return SecureDataStore.Builder<PreferencesSecureDataStore>(setup = setup).build()
        }
    }
}
