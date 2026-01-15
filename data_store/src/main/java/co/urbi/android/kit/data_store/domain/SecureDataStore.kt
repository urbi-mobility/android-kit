package co.urbi.android.kit.data_store.domain

import co.urbi.android.kit.data_store.data.crypto.Cipher
import co.urbi.android.kit.data_store.data.crypto.Tink
import co.urbi.android.kit.data_store.data.data_store.PreferencesDataStoreImpl
import co.urbi.android.kit.data_store.data.data_store.ProtoDataStoreImpl
import co.urbi.android.kit.data_store.domain.model.CipherSetup
import co.urbi.android.kit.data_store.domain.model.CryptoSetup
import co.urbi.android.kit.data_store.domain.model.DataStoreSetup
import co.urbi.android.kit.data_store.domain.model.DataStoreType
import co.urbi.android.kit.data_store.domain.model.TinkSetup

interface SecureDataStore {
    class Builder<Type>(private val setup: DataStoreSetup) {
        internal var cipherSetup: CipherSetup? = null
        internal var tinkSetup: TinkSetup? = null

        fun encrypt(setup: CryptoSetup): Builder<Type> {
            when(setup) {
                is CipherSetup -> cipherSetup = setup
                is TinkSetup -> tinkSetup = setup
            }
            return this
        }

        fun build(): Type {
            return when (val type = setup.type) {
                DataStoreType.Preferences -> {
                    PreferencesDataStoreImpl(
                        setup = setup,
                        cryptoManager = when {
                            cipherSetup != null -> cipherSetup?.let { Cipher(setup = it) }
                            tinkSetup != null -> tinkSetup?.let { Tink(setup = it) }
                            else -> null
                        }
                    ) as Type
                }

                is DataStoreType.Proto<*> -> {
                    ProtoDataStoreImpl(
                        setup = setup,
                        schema = type.schema,
                        cryptoManager = when {
                            cipherSetup != null -> cipherSetup?.let { Cipher(setup = it) }
                            tinkSetup != null -> tinkSetup?.let { Tink(setup = it) }
                            else -> null
                        }
                    ) as Type
                }
            }
        }
    }
}
