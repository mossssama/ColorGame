package com.newOs.colorCraze.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DataStoreManager private constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver.asStateFlow()

    private val _multiplayerGameResult = MutableStateFlow(MultiplayerGameResult(0, 0))
//    val multiplayerGameResult: StateFlow<MultiplayerGameResult> = _multiplayerGameResult.asStateFlow()

    init {
        _isGameOver.value = false
        observeDataStore()
    }

    private fun observeDataStore() {
        GlobalScope.launch {
            dataStore.data.catch { exception ->
                if (exception is Exception) {}
            }.collect { preferences ->
                _isGameOver.value = preferences[booleanPreferencesKey("gameOver")] ?: false
                _multiplayerGameResult.value =
                    MultiplayerGameResult(
                        preferences[intPreferencesKey("playerScore")] ?: 0,
                        preferences[intPreferencesKey("oppositeScore")] ?: 0
                    )
            }
        }
    }

    suspend fun saveGameOver(isGameOver: Boolean) {
        dataStore.edit { preferences -> preferences[booleanPreferencesKey("gameOver")] = isGameOver }
    }

    suspend fun saveMultiplayerGameResult(multiplayerGameResult: MultiplayerGameResult) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey("playerScore")] = multiplayerGameResult.playerScore
            preferences[intPreferencesKey("oppositeScore")] = multiplayerGameResult.oppositeScore
        }
    }

    suspend fun readMultiplayerGameResult(): MultiplayerGameResult {
        val preferences = dataStore.data.first()
        return MultiplayerGameResult(
            preferences[intPreferencesKey("playerScore")] ?: 0,
            preferences[intPreferencesKey("oppositeScore")] ?: 0
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: DataStoreManager? = null

        // Provide a DataStore instance directly, without using createDataStore
        fun create(dataStore: DataStore<Preferences>): DataStoreManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataStoreManager(dataStore).also { INSTANCE = it }
            }
    }
}