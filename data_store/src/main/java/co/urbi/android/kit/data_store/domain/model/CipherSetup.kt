package co.urbi.android.kit.data_store.domain.model

data class CipherSetup(
    val alias: String,
    val algorithm: String,
    val blockMode: String,
    val padding: String,
): CryptoSetup
