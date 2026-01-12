package co.urbi.android.kit.data_store.domain.model

import android.content.Context
import java.io.File

interface DataStoreSetup {
    val file: FileType
    class Builder(private val file: FileType){
        fun build(): DataStoreSetup {
            return object : DataStoreSetup {
                override val file: FileType = this@Builder.file
            }
        }
    }
}

sealed interface FileType {
    data class CredentialProtectedFile(val context: Context, val fileName: String) : FileType
    data class DeviceProtectedFile(val context: Context, val fileName: String) : FileType
    data class CustomProtectedFile(val file: File) : FileType
}
