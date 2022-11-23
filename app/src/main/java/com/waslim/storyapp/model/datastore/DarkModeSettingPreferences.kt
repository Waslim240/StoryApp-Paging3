package com.waslim.storyapp.model.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DarkModeSettingPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun setThemeSetting(isDarkModeActive: Boolean) {
        context.darkModeSetting.edit { it[DATASTORE_KEY] = isDarkModeActive }
    }

    fun getThemeSetting() : Flow<Boolean> =
        context.darkModeSetting.data.map { it[DATASTORE_KEY] ?: false }

    companion object {
        private const val DATASTORE_SETTING = "setting_preferences"
        private val DATASTORE_KEY = booleanPreferencesKey("datastore_key")
        private val Context.darkModeSetting by preferencesDataStore(DATASTORE_SETTING)
    }

}