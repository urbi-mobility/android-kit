package co.urbi.android.kit.data_store.domain

import kotlinx.coroutines.flow.Flow

/**
 * Interface definition for a secure data store that manages protocol buffer data.
 * @param T The type of the data managed by this data store.
 */
interface ProtoSecureDataStore<T> {
    /**
     * Returns a Flow containing the data of type T.
     */
    fun data(): Flow<T>

    /**
     * Updates the data transactionally in an atomic read-modify-write operation.
     * @param transform The block that transforms the current data to the new data.
     * @return The new data.
     */
    suspend fun updateData(transform: suspend (t: T) -> T): T
}
