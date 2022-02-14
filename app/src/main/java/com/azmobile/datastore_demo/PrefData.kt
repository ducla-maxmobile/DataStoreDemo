package com.azmobile.datastore_demo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView

/**
 * Created by DucLe on 2/10/2022.
 */

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
private const val GENDER_KEY = "gender"

val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = UserPreferencesSerializer,
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(
                context,
                USER_PREFERENCES_NAME
            ) { sharedPrefs: SharedPreferencesView, currentData: UserPreferences ->
                if (currentData.gender == UserPreferences.Gender.UNSPECIFIED) {
                    currentData.toBuilder()
                        .setGender(
                            UserPreferences.Gender.valueOf(
                                sharedPrefs.getString(
                                    GENDER_KEY,
                                    UserPreferences.Gender.MALE.name
                                )!!
                            )
                        )
                        .build()
                } else {
                    currentData
                }
            }
        )
    }
)
