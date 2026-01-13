package co.urbi.android.kit.examples.data_store.utils


data class UserModel(
    val username: String = "",
    val userId: String = "",
    val isPremium: Boolean = false,
    val balance: Double = 0.0,
    val loginCounter: Long = 0,
    val rating: Float = 0f,
    val tags: Set<String> = emptySet()
)
