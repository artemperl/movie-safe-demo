package com.moviesafe

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okio.Path

class PersistenceDataStore<T>(
    private val serializer: KSerializer<T>,
    private val filename: String
) : MSDataStore<T> {

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath {
        dataStorePath(filename)
    }

    override val data: Flow<T?> = dataStore.data.map { prefs ->
        readData(prefs)
    }

    override suspend fun updateData(update: (T?) -> T?) {
        dataStore.edit { prefs ->
            val currentValue = readData(prefs)
            val newValue = update(currentValue)
            if (newValue == null) {
                prefs.remove(stringPreferencesKey(DATA_KEY))
            } else {
                prefs[stringPreferencesKey(DATA_KEY)] = Json.encodeToString(serializer, newValue)
            }
        }
    }

    private fun readData(prefs: Preferences): T? = prefs[stringPreferencesKey(DATA_KEY)]?.runCatching {
        Json.decodeFromString(serializer, this)
    }?.getOrNull()

}

internal expect fun dataStorePath(filename: String): Path

private const val DATA_KEY = "data"