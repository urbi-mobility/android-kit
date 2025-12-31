package co.urbi.android.kit.examples.data_store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import java.util.UUID


@Composable
fun SecureDataStoreExample(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStoreInstance = remember { DataStoreInstance.getRawInstance(context = context) }
    var accessToken by remember { mutableStateOf("") }
    var refreshToken by remember { mutableStateOf("") }


    LaunchedEffect(true) {
        dataStoreInstance.data().collectLatest {
            accessToken = it.accessToken
            refreshToken = it.refreshToken
        }
    }


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            scope.launch {
                dataStoreInstance.updateData { data ->
                    data.copy(
                        accessToken = UUID.randomUUID().toString(),
                        refreshToken = UUID.randomUUID().toString()
                    )
                }
            }
        }) {
            Text(text = "Write")
        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Text(text = "AccessToken:\n$accessToken")
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Text(text = "RefreshToken:\n$refreshToken")
    }
}
