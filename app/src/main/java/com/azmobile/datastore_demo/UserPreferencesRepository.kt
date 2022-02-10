package com.azmobile.datastore_demo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Created by DucLe on 2/10/2022.
 */

enum class Gender {
    MALE,
    FEMALE
}

data class UserPreferences(
    val name: String,
    val age: Int,
    val gender: Gender
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private val TAG = UserPreferencesRepository::class.simpleName

    private object PreferencesKeys {
        val KEY_NAME = stringPreferencesKey("name")
        val KEY_AGE = intPreferencesKey("age")
        val KEY_GENDER = stringPreferencesKey("gender")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {

        val name = preferences[PreferencesKeys.KEY_NAME] ?: ""
        val age = preferences[PreferencesKeys.KEY_AGE] ?: 0
        val gender = preferences[PreferencesKeys.KEY_GENDER] ?: Gender.MALE.name

        return UserPreferences(name, age, Gender.valueOf(gender))
    }

    suspend fun fetchInitialPreferences(): UserPreferences =
        mapUserPreferences(dataStore.data.first().toPreferences())

    suspend fun updateName(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_NAME] = name
        }
    }

    suspend fun updateAge(age: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_AGE] = age
        }
    }

    suspend fun updateGender(isMale: Boolean) {
        dataStore.edit { preferences ->
            val newVal = if (isMale) {
                Gender.MALE.name
            } else {
                Gender.FEMALE.name
            }
            preferences[PreferencesKeys.KEY_GENDER] = newVal
        }
    }
}
