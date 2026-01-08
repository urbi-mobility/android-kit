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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun PreferencesDataStoreExample(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var useEncryption by remember { mutableStateOf(false) }
    val prefsDataStore = remember(useEncryption) {
        if (useEncryption) {
            PreferencesDataStoreInstance.getEncryptedInstance(context = context)
        } else {
            PreferencesDataStoreInstance.getRawInstance(context = context)
        }
    }

    var username by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(0) }
    var isPremium by remember { mutableStateOf(false) }
    var balance by remember { mutableStateOf(0.0) }
    var loginCount by remember { mutableStateOf(0L) }
    var rating by remember { mutableStateOf(0f) }
    var tags by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(useEncryption) {
        launch {
            prefsDataStore.getString("username").collectLatest { username = it ?: "" }
        }
        launch {
            prefsDataStore.getInt("user_id").collectLatest { userId = it ?: 0 }
        }
        launch {
            prefsDataStore.getBoolean("is_premium").collectLatest { isPremium = it ?: false }
        }
        launch {
            prefsDataStore.getDouble("balance").collectLatest { balance = it ?: 0.0 }
        }
        launch {
            prefsDataStore.getLong("login_count").collectLatest { loginCount = it ?: 0L }
        }
        launch {
            prefsDataStore.getFloat("rating").collectLatest { rating = it ?: 0f }
        }
        launch {
            prefsDataStore.getStringSet("tags").collectLatest { tags = it ?: setOf() }
        }
    }

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
                        text = if (useEncryption) "Enabled (AES CBC)" else "Disabled",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = useEncryption,
                    onCheckedChange = { useEncryption = it }
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
                            scope.launch {
                                // Write all types of data
                                prefsDataStore.putString("username", "user_${Random.nextInt(1000)}")
                                prefsDataStore.putInt("user_id", Random.nextInt(10000))
                                prefsDataStore.putBoolean("is_premium", Random.nextBoolean())
                                prefsDataStore.putDouble("balance", Random.nextDouble(0.0, 10000.0))
                                prefsDataStore.putLong("login_count", Random.nextLong(0, 1000))
                                prefsDataStore.putFloat("rating", Random.nextFloat() * 5)
                                prefsDataStore.putStringSet("tags", setOf("tag${Random.nextInt(10)}", "tag${Random.nextInt(10)}"))
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Write Random Data")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                prefsDataStore.clear()
                            }
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

                PreferenceItem(label = "Username (String)", value = username.ifEmpty { "Not set" })
                PreferenceItem(label = "User ID (Int)", value = userId.toString())
                PreferenceItem(label = "Is Premium (Boolean)", value = isPremium.toString())
                PreferenceItem(label = "Balance (Double)", value = "%.2f".format(balance))
                PreferenceItem(label = "Login Count (Long)", value = loginCount.toString())
                PreferenceItem(label = "Rating (Float)", value = "%.2f".format(rating))
                PreferenceItem(label = "Tags (StringSet)", value = tags.joinToString(", ").ifEmpty { "None" })
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
                        onClick = { scope.launch { prefsDataStore.remove("username") } },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Remove Username", style = MaterialTheme.typography.bodySmall)
                    }

                    Button(
                        onClick = { scope.launch { prefsDataStore.remove("user_id") } },
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

