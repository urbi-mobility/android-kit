package co.urbi.android.kit.examples.data_store.utils

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val accessToken: String = "",
    val refreshToken: String = "",
)
