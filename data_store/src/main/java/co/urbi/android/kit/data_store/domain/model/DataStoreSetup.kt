package co.urbi.android.kit.data_store.domain.model

import android.content.Context
import java.io.File

interface DataStoreSetup {
    val file: FileType
    val type: DataStoreType

    class Builder(private val file: FileType, private val type: DataStoreType) {
        fun build(): DataStoreSetup {
            return object : DataStoreSetup {
                override val file: FileType = this@Builder.file
                override val type: DataStoreType = this@Builder.type
            }
        }
    }
}

sealed interface FileType {
    data class CredentialProtectedFile(val context: Context, val fileName: String) : FileType
    data class DeviceProtectedFile(val context: Context, val fileName: String) : FileType
    data class CustomProtectedFile(val file: File) : FileType
}


sealed interface DataStoreType {
    data object Preferences : DataStoreType

    /**
     * The [schema] input should have be marked with @Serializable. It will be considered as default value for the [DataStoreType.Proto]. Please pass it as an instance.
     */
    data class Proto<Schema>(val schema: Schema) : DataStoreType
}
