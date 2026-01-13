package co.urbi.android.kit.examples.data_store.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.urbi.android.kit.examples.data_store.event.PreferencesDataStoreEvents
import co.urbi.android.kit.examples.data_store.state.PreferencesDataStoreState
import co.urbi.android.kit.examples.data_store.utils.DataStoreInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PreferencesDataStoreViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(PreferencesDataStoreState())
    val state = _state.asStateFlow()

    private val encryptedPrefsDataStore = DataStoreInstance.Preferences.getTinkInstance(context = context)
    private val rawPrefsDataStore = DataStoreInstance.Preferences.getRawInstance(context = context)

    init {
        viewModelScope.launch {
            launch {
                encryptedPrefsDataStore.getString("username").collectLatest { value ->
                    if (_state.value.useEncryption) {
                        _state.update { it.copy(username = value ?: "") }
                    }
                }
            }
            launch {
                encryptedPrefsDataStore.getInt("user_id").collectLatest { value ->
                    if (_state.value.useEncryption) {
                        _state.update { it.copy(userId = value ?: 0) }
                    }
                }
            }
            launch {
                encryptedPrefsDataStore.getBoolean("is_premium").collectLatest { value ->
                    if (_state.value.useEncryption) {
                        _state.update { it.copy(isPremium = value ?: false) }
                    }
                }
            }
            launch {
                encryptedPrefsDataStore.getDouble("balance").collectLatest { value ->
                    if (_state.value.useEncryption) {
                        _state.update { it.copy(balance = value ?: 0.0) }
                    }
                }
            }
            launch {
                encryptedPrefsDataStore.getLong("login_count").collectLatest { value ->
                    if (_state.value.useEncryption) {
                        _state.update { it.copy(loginCount = value ?: 0L) }
                    }
                }
            }
            launch {
                encryptedPrefsDataStore.getFloat("rating").collectLatest { value ->
                    if (_state.value.useEncryption) {
                        _state.update { it.copy(rating = value ?: 0f) }
                    }
                }
            }
            launch {
                encryptedPrefsDataStore.getStringSet("tags").collectLatest { value ->
                    if (_state.value.useEncryption) {
                        _state.update { it.copy(tags = value ?: setOf()) }
                    }
                }
            }

            launch {
                rawPrefsDataStore.getString("username").collectLatest { value ->
                    if (!_state.value.useEncryption) {
                        _state.update { it.copy(username = value ?: "") }
                    }
                }
            }
            launch {
                rawPrefsDataStore.getInt("user_id").collectLatest { value ->
                    if (!_state.value.useEncryption) {
                        _state.update { it.copy(userId = value ?: 0) }
                    }
                }
            }
            launch {
                rawPrefsDataStore.getBoolean("is_premium").collectLatest { value ->
                    if (!_state.value.useEncryption) {
                        _state.update { it.copy(isPremium = value ?: false) }
                    }
                }
            }
            launch {
                rawPrefsDataStore.getDouble("balance").collectLatest { value ->
                    if (!_state.value.useEncryption) {
                        _state.update { it.copy(balance = value ?: 0.0) }
                    }
                }
            }
            launch {
                rawPrefsDataStore.getLong("login_count").collectLatest { value ->
                    if (!_state.value.useEncryption) {
                        _state.update { it.copy(loginCount = value ?: 0L) }
                    }
                }
            }
            launch {
                rawPrefsDataStore.getFloat("rating").collectLatest { value ->
                    if (!_state.value.useEncryption) {
                        _state.update { it.copy(rating = value ?: 0f) }
                    }
                }
            }
            launch {
                rawPrefsDataStore.getStringSet("tags").collectLatest { value ->
                    if (!_state.value.useEncryption) {
                        _state.update { it.copy(tags = value ?: setOf()) }
                    }
                }
            }
        }
    }

    fun uiEvents(event: PreferencesDataStoreEvents) {
        when (event) {
            PreferencesDataStoreEvents.OnToggleEncryption -> {
                _state.update { state ->
                    state.copy(useEncryption = !state.useEncryption)
                }
            }

            PreferencesDataStoreEvents.OnWriteRandomData -> {
                viewModelScope.launch {
                    val dataStore = if (state.value.useEncryption) encryptedPrefsDataStore else rawPrefsDataStore
                    dataStore.putString("username", "user_${Random.nextInt(1000)}")
                    dataStore.putInt("user_id", Random.nextInt(10000))
                    dataStore.putBoolean("is_premium", Random.nextBoolean())
                    dataStore.putDouble("balance", Random.nextDouble(0.0, 10000.0))
                    dataStore.putLong("login_count", Random.nextLong(0, 1000))
                    dataStore.putFloat("rating", Random.nextFloat() * 5)
                    dataStore.putStringSet("tags", setOf("tag${Random.nextInt(10)}", "tag${Random.nextInt(10)}"))
                }
            }

            PreferencesDataStoreEvents.OnClearAll -> {
                viewModelScope.launch {
                    val dataStore = if (state.value.useEncryption) encryptedPrefsDataStore else rawPrefsDataStore
                    dataStore.clear()
                }
            }

            is PreferencesDataStoreEvents.OnRemoveKey -> {
                viewModelScope.launch {
                    val dataStore = if (state.value.useEncryption) encryptedPrefsDataStore else rawPrefsDataStore
                    dataStore.remove(event.key)
                }
            }
        }
    }
}
