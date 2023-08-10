package com.example.colorgame.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DataStoreManager private constructor(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_settings")

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver.asStateFlow()

    init {  
        _isGameOver.value = false
        observeDataStore()
    }

    private fun observeDataStore() {
        GlobalScope.launch {
            context.dataStore.data.catch { exception ->
                if (exception is Exception) { }
            }.collect { preferences ->
                _isGameOver.value = preferences[booleanPreferencesKey("gameOver")] ?: false
            }
        }
    }

    suspend fun saveGameOver(isGameOver: Boolean) {
        context.dataStore.edit { preferences -> preferences[booleanPreferencesKey("gameOver")] = isGameOver }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataStoreManager? = null
        fun getInstance(context: Context): DataStoreManager =INSTANCE ?: synchronized(this) { INSTANCE ?: DataStoreManager(context).also { INSTANCE = it } }
    }

}