package co.urbi.android.kit.examples.data_store.state

import androidx.compose.runtime.Immutable
import co.urbi.android.kit.examples.data_store.utils.CredentialModel


@Immutable
data class DataStoreState(
    val useEncryption: Boolean = false,
    val credential: CredentialModel = CredentialModel(),
)
