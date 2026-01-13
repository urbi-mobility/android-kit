package co.urbi.android.kit.examples.data_store.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.urbi.android.kit.examples.data_store.event.DataStoreEvents
import co.urbi.android.kit.examples.data_store.state.DataStoreState
import co.urbi.android.kit.examples.data_store.utils.CredentialModel
import co.urbi.android.kit.examples.data_store.utils.DataStoreInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(DataStoreState())
    val state = _state.asStateFlow()


    private val encryptedProto = DataStoreInstance.Proto.getCipherInstance(context = context)
    private val rawProto = DataStoreInstance.Proto.getRawInstance(context = context)


    init {
        viewModelScope.launch {
            encryptedProto.data()
                .combine(rawProto.data()) { encrypted, raw -> encrypted to raw }
                .combine(state) { (encrypted, raw), state ->
                    Triple(
                        encrypted,
                        raw,
                        state.useEncryption
                    )
                }
                .collectLatest { (encrypted, raw, useEncryption) ->
                    _state.update { state ->
                        state.copy(
                            credential = if (useEncryption) encrypted else raw
                        )
                    }
                }
        }
    }


    fun uiEvents(event: DataStoreEvents) {
        when (event) {
            DataStoreEvents.OnToggleEncryption -> {
                _state.update { state ->
                    state.copy(
                        useEncryption = !state.useEncryption
                    )
                }
            }

            DataStoreEvents.OnClearCredential -> {
                viewModelScope.launch {
                    val useEncryption = state.value.useEncryption
                    if (useEncryption)
                        encryptedProto.updateData { CredentialModel() }
                    else
                        rawProto.updateData { CredentialModel() }
                }
            }

            DataStoreEvents.OnGenerateCredential -> {
                viewModelScope.launch {
                    val credential = CredentialModel(
                        accessToken = UUID.randomUUID().toString(),
                        refreshToken = UUID.randomUUID().toString()
                    )
                    val useEncryption = state.value.useEncryption
                    if (useEncryption) {
                        encryptedProto.updateData { credential }
                    } else {
                        rawProto.updateData { credential }
                    }
                }
            }
        }
    }

}