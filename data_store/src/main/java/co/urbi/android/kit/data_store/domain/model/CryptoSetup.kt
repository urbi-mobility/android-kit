package co.urbi.android.kit.data_store.domain.model

import android.content.Context
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.proto.KeyTemplate

sealed interface CryptoSetup{

    data class CipherSetup(
        val alias: String,
        val algorithm: String,
        val blockMode: String,
        val padding: String,
    ): CryptoSetup

    data class TinkSetup(
        val context: Context,
        val keysetName: String,
        val keysetFileName: String,
        val keyTemplate: KeyTemplate = AeadKeyTemplates.AES256_GCM
    ): CryptoSetup

}
