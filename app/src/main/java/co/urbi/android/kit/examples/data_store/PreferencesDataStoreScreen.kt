package co.urbi.android.kit.examples.data_store

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
import co.urbi.android.kit.examples.data_store.event.PreferencesDataStoreEvents
import co.urbi.android.kit.examples.data_store.state.PreferencesDataStoreState

@Composable
fun PreferencesDataStoreExample(
    modifier: Modifier = Modifier,
    state: PreferencesDataStoreState,
    event: (PreferencesDataStoreEvents) -> Unit
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "PreferencesDataStore Example",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Demonstrates key-value storage with encryption support",
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
                    onCheckedChange = { event(PreferencesDataStoreEvents.OnToggleEncryption) }
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
                            event(PreferencesDataStoreEvents.OnWriteRandomData)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Write Random Data")
                    }

                    Button(
                        onClick = {
                            event(PreferencesDataStoreEvents.OnClearAll)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Clear All")
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
                    text = "Stored Values",
                    style = MaterialTheme.typography.titleMedium
                )

                PreferenceItem(label = "Username (String)", value = state.username.ifEmpty { "Not set" })
                PreferenceItem(label = "User ID (Int)", value = state.userId.toString())
                PreferenceItem(label = "Is Premium (Boolean)", value = state.isPremium.toString())
                PreferenceItem(label = "Balance (Double)", value = "%.2f".format(state.balance))
                PreferenceItem(label = "Login Count (Long)", value = state.loginCount.toString())
                PreferenceItem(label = "Rating (Float)", value = "%.2f".format(state.rating))
                PreferenceItem(label = "Tags (StringSet)", value = state.tags.joinToString(", ").ifEmpty { "None" })
            }
        }

        // Individual Remove Buttons
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Remove Individual Keys",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { event(PreferencesDataStoreEvents.OnRemoveKey("username")) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Remove Username", style = MaterialTheme.typography.bodySmall)
                    }

                    Button(
                        onClick = { event(PreferencesDataStoreEvents.OnRemoveKey("user_id")) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Remove User ID", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun PreferenceItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

