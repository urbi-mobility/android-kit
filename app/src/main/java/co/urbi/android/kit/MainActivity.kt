package co.urbi.android.kit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import co.urbi.android.kit.examples.data_store.SecureDataStoreExample
import co.urbi.android.kit.ui.theme.UrbiAndroidKitTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UrbiAndroidKitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SecureDataStoreExample(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding))
                }
            }
        }
    }
}
