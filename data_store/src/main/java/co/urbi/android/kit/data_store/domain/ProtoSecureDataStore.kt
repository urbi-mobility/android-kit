package co.urbi.android.kit.data_store.domain

import kotlinx.coroutines.flow.Flow

interface ProtoSecureDataStore<T> {
    fun data(): Flow<T>
    suspend fun updateData(transform: suspend (t: T) -> T): T
}
