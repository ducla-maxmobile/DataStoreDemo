package com.azmobile.datastore_demo

import androidx.lifecycle.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by DucLe on 2/10/2022.
 */
class MyViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    val initialSetupEvent = liveData {
        emit(userPreferencesRepository.fetchInitialPreferences())
    }

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    val user = userPreferencesFlow.asLiveData()

    fun updateName(name: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateName(name)
        }
    }

    fun updateAge(age: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateAge(age)
        }
    }

    fun updateGender(isMale: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateGender(isMale)
        }
    }

}

class MyViewModelFactory(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
