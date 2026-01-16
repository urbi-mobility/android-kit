package co.urbi.android.kit.examples.data_store.event

sealed interface DataStoreEvents {
    data object OnToggleEncryption : DataStoreEvents
    data object OnGenerateCredential : DataStoreEvents
    data object OnClearCredential : DataStoreEvents
}
