package co.urbi.android.kit.examples.data_store.event

sealed interface PreferencesDataStoreEvents {
    data object OnToggleEncryption : PreferencesDataStoreEvents
    data object OnWriteRandomData : PreferencesDataStoreEvents
    data object OnClearAll : PreferencesDataStoreEvents
    data class OnRemoveKey(val key: String) : PreferencesDataStoreEvents
}
