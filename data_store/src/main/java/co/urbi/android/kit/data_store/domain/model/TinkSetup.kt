package co.urbi.android.kit.data_store.domain.model

import android.content.Context
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.proto.KeyTemplate

data class TinkSetup(
    val context: Context,
    val keysetName: String,
    val keysetFileName: String,
    val keyTemplate: KeyTemplate = AeadKeyTemplates.AES256_GCM
): CryptoSetup
