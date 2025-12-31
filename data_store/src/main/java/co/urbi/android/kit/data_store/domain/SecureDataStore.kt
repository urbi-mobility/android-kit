package co.urbi.android.kit.data_store.domain

import co.urbi.android.kit.data_store.data.Crypto
import co.urbi.android.kit.data_store.data.SecureDataStoreImpl
import co.urbi.android.kit.data_store.domain.model.CryptoSetup
import kotlinx.coroutines.flow.Flow
import java.io.File

interface SecureDataStore<T> {
    fun data(): Flow<T>
    suspend fun updateData(transform: suspend (t: T) -> T): T

    class Builder<T>(private val default: T, private val file: File) {
        internal var cryptoSetup: CryptoSetup? = null

        fun encrypt(setup: CryptoSetup): Builder<T> {
            cryptoSetup = setup
            return this
        }

        fun build(): SecureDataStore<T> {
            return SecureDataStoreImpl(
                default = default,
                file = file,
                crypto = cryptoSetup?.let { setup -> Crypto(setup) }
            )
        }
    }
}
