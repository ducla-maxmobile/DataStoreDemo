package com.azmobile.datastore_demo

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException

/**
 * Created by DucLe on 2/10/2022.
 */

class UserPreferencesRepository(private val dataStore: DataStore<UserPreferences>) {

    private val TAG = UserPreferencesRepository::class.simpleName

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun fetchInitialPreferences(): UserPreferences =
        dataStore.data.first()

    suspend fun updateName(name: String) {
        dataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder().setName(name).build()
        }
    }

    suspend fun updateAge(age: Int) {
        dataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder().setAge(age).build()
        }
    }

    suspend fun updateGender(isMale: Boolean) {
        dataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder().setGender(
                if (isMale) {
                    UserPreferences.Gender.MALE
                } else {
                    UserPreferences.Gender.FEMALE
                }
            ).build()
        }
    }
}
