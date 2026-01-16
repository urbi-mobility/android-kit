package co.urbi.android.kit.examples.data_store.state

import androidx.compose.runtime.Immutable

@Immutable
data class PreferencesDataStoreState(
    val useEncryption: Boolean = false,
    val username: String = "",
    val userId: Int = 0,
    val isPremium: Boolean = false,
    val balance: Double = 0.0,
    val loginCount: Long = 0L,
    val rating: Float = 0f,
    val tags: Set<String> = setOf()
)
