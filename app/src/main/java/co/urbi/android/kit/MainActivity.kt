package co.urbi.android.kit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import co.urbi.android.kit.examples.data_store.DataStoreExamplesScreen
import co.urbi.android.kit.examples.data_store.PreferencesDataStoreExample
import co.urbi.android.kit.examples.data_store.SecureDataStoreExample
import co.urbi.android.kit.examples.data_store.view_model.DataStoreViewModel
import co.urbi.android.kit.ui.theme.UrbiAndroidKitTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen {
    data object Home : Screen()
    data object SecureDataStore : Screen()
    data object PreferencesDataStore : Screen()
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UrbiAndroidKitTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when (currentScreen) {
                                        Screen.Home -> "DataStore Examples"
                                        Screen.SecureDataStore -> "SecureDataStore"
                                        Screen.PreferencesDataStore -> "PreferencesDataStore"
                                    }
                                )
                            },
                            navigationIcon = {
                                if (currentScreen != Screen.Home) {
                                    IconButton(onClick = { currentScreen = Screen.Home }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    when (currentScreen) {
                        Screen.Home -> {
                            DataStoreExamplesScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                onNavigateToSecureDataStore = {
                                    currentScreen = Screen.SecureDataStore
                                },
                                onNavigateToPreferencesDataStore = {
                                    currentScreen = Screen.PreferencesDataStore
                                }
                            )
                        }

                        Screen.SecureDataStore -> {
                            val viewModel = hiltViewModel<DataStoreViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            SecureDataStoreExample(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                state = state,
                                event = viewModel::uiEvents
                            )
                        }

                        Screen.PreferencesDataStore -> {
                            PreferencesDataStoreExample(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }

}
