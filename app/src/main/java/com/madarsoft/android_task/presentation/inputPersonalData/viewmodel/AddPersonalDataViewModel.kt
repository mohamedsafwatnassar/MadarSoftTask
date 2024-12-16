package com.madarsoft.android_task.presentation.inputPersonalData.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.madarsoft.android_task.data.mapper.PersonalDataMapper.toDateModel
import com.madarsoft.android_task.domain.model.PersonalData
import com.madarsoft.android_task.domain.usecase.InsertPersonalDataUseCase
import com.madarsoft.android_task.presentation.base.BaseViewModel
import com.madarsoft.android_task.util.DataState
import com.madarsoft.android_task.util.LoadingErrorState
import com.telda.movieApp.domain.model.CustomError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for adding personal data to the database and managing its UI state.
 * @param insertPersonalDataUseCase The use case for inserting personal data into the local database.
 */
@HiltViewModel
class AddPersonalDataViewModel @Inject constructor(
    private val insertPersonalDataUseCase: InsertPersonalDataUseCase,
) : BaseViewModel() {

    // LiveData to expose personal data to the UI layer
    private val _personalData = MutableLiveData<PersonalData>()
    val personalData: LiveData<PersonalData> = _personalData

    /**
     * Adds personal data to the database.
     * @param username The user's name.
     * @param age The user's age.
     * @param jobTitle The user's job title.
     * @param gender The user's gender.
     */
    fun addPersonalDataToDatabase(
        username: String,
        age: String,
        jobTitle: String,
        gender: String,
    ) {
        viewModelScope.launch {
            try {
                // Invoke the use case to add personal data and collect the resulting Flow
                val call = insertPersonalDataUseCase.addPersonalDataToDatabase(
                    username,
                    age,
                    jobTitle,
                    gender
                )

                // Collect and handle emitted data states
                call.collect { dataState ->
                    handleDataState(dataState)
                }
            } catch (e: Exception) {
                // Handle unexpected exceptions and notify the UI of the error
                _viewState.postValue(
                    LoadingErrorState.ShowError(CustomError(e.localizedMessage ?: "An unknown error occurred"))
                )
            }
        }
    }

    /**
     * Handles different states of data emitted by the use case.
     * @param dataState The current state of the data (success, error, loading, etc.).
     */
    private fun handleDataState(dataState: DataState<PersonalData>) {
        when (dataState) {
            is DataState.Success -> {
                // On success, update the LiveData with the fetched personal data
                val personalData = dataState.data
                _personalData.postValue(personalData)
                _viewState.postValue(LoadingErrorState.HideLoading)
            }

            is DataState.Error -> {
                // On error, update the UI with the error message
                _viewState.postValue(LoadingErrorState.ShowError(dataState.error))
            }

            DataState.Idle -> {
                // When idle, clear the loading state
                _viewState.postValue(LoadingErrorState.HideLoading)
            }

            DataState.Processing -> {
                // When processing, show the loading spinner
                _viewState.postValue(LoadingErrorState.ShowLoading)
            }

            DataState.ServerError -> {
                // Handle server errors with a generic network error message
                _viewState.postValue(LoadingErrorState.ShowNetworkError)
            }
        }
    }
}
