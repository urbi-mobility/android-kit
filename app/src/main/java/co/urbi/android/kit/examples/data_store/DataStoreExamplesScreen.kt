package co.urbi.android.kit.examples.data_store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DataStoreExamplesScreen(
    modifier: Modifier = Modifier,
    onNavigateToSecureDataStore: () -> Unit,
    onNavigateToPreferencesDataStore: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "DataStore Examples",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Choose an example to explore",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.padding(16.dp))

        ExampleCard(
            title = "SecureDataStore",
            description = "Proto DataStore for structured objects with Kotlin Serialization. Perfect for storing complex data models.",
            onClick = onNavigateToSecureDataStore
        )

        ExampleCard(
            title = "PreferencesDataStore",
            description = "Key-value storage for simple preferences with string keys. Ideal for user settings and flags.",
            onClick = onNavigateToPreferencesDataStore
        )
    }
}

@Composable
private fun ExampleCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Open Example")
            }
        }
    }
}

