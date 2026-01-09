package co.urbi.android.kit.data_store.domain

import co.urbi.android.kit.data_store.data.SecureDataStoreImpl
import co.urbi.android.kit.data_store.data.crypto.Cipher
import co.urbi.android.kit.data_store.data.crypto.Tink
import co.urbi.android.kit.data_store.domain.model.CipherSetup
import co.urbi.android.kit.data_store.domain.model.TinkSetup
import kotlinx.coroutines.flow.Flow
import java.io.File

interface SecureDataStore<T> {
    fun data(): Flow<T>
    suspend fun updateData(transform: suspend (t: T) -> T): T

    class Builder<T>(private val default: T, private val file: File) {
        internal var cipherSetup: CipherSetup? = null
        internal var tinkSetup: TinkSetup? = null


        fun encrypt(cipher: CipherSetup): Builder<T> {
            cipherSetup = cipher
            return this
        }

        fun encrypt(tink: TinkSetup): Builder<T> {
            tinkSetup = tink
            return this
        }

        fun build(): SecureDataStore<T> {
            return SecureDataStoreImpl(
                default = default,
                file = file,
                cryptoManager = when {
                    cipherSetup != null -> cipherSetup?.let { Cipher(setup = it) }
                    tinkSetup != null -> tinkSetup?.let { Tink(setup = it) }
                    else -> null
                }
            )
        }
    }
}
