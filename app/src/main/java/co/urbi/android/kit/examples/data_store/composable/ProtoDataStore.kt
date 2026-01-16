package co.urbi.android.kit.examples.data_store.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.urbi.android.kit.examples.data_store.event.DataStoreEvents
import co.urbi.android.kit.examples.data_store.state.DataStoreState


@Composable
fun ProtoDataStore(
    modifier: Modifier = Modifier,
    state: DataStoreState,
    event: (DataStoreEvents) -> Unit

) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Proto Data Store Example",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Proto Data Store for structured objects with Kotlin Serialization",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Encryption Toggle
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Encryption",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (state.useEncryption) "Enabled (AES CBC)" else "Disabled",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = state.useEncryption,
                    onCheckedChange = { event(DataStoreEvents.OnToggleEncryption) }
                )
            }
        }

        // Action Buttons
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Actions",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                           event(DataStoreEvents.OnGenerateCredential)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Generate Tokens")
                    }

                    Button(
                        onClick = {
                            event(DataStoreEvents.OnClearCredential)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Clear Tokens")
                    }
                }
            }
        }

        // Display Values
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Current User Model",
                    style = MaterialTheme.typography.titleMedium
                )

                TokenItem(label = "Access Token", value = state.credential.accessToken.ifEmpty { "Not set" })
                TokenItem(label = "Refresh Token", value = state.credential.refreshToken.ifEmpty { "Not set" })
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "This example uses SecureDataStore to persist a serializable UserModel object. " +
                            "The entire object is stored as a single entity and can be optionally encrypted.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TokenItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2
        )
    }
}
