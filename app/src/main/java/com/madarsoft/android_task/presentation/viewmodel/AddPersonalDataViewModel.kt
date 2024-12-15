package com.madarsoft.android_task.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.madarsoft.android_task.domain.model.PersonalData
import com.madarsoft.android_task.domain.usecase.FetchPersonalDatUseCase
import com.madarsoft.android_task.domain.usecase.InsertPersonalDataUseCase
import com.madarsoft.android_task.presentation.base.BaseViewModel
import com.madarsoft.android_task.util.DataState
import com.madarsoft.android_task.util.LoadingErrorState
import com.telda.movieApp.domain.model.CustomError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddPersonalDataViewModel @Inject constructor(
    private val insertPersonalDataUseCase: InsertPersonalDataUseCase,
    private val fetchPersonalDatUseCase: FetchPersonalDatUseCase
) : BaseViewModel() {

    private val _personalData = MutableLiveData<PersonalData>()
    val personalData: LiveData<PersonalData> = _personalData

    fun addPersonalDataToDatabase(
        username: String,
        age: String,
        jobTitle: String,
        gender: String,
    ) {
        viewModelScope.launch {
            try {
                val call = insertPersonalDataUseCase.addPersonalDataToDatabase(
                    username,
                    age,
                    jobTitle,
                    gender
                )
                call.collect {
                    handleDataState(it)
                }
            } catch (e: Exception) {
                // Handle any exceptions and post the error message to _viewState
                _viewState.postValue(LoadingErrorState.ShowError(CustomError(e.localizedMessage.toString())))
            }
        }
    }

    private fun handleDataState(dataState: DataState<PersonalData>) {
        when (dataState) {
            is DataState.Success -> {
                // On success, update the LiveData with the grouped movies data
                _personalData.postValue(dataState.data)
            }

            is DataState.Error -> {
                // Handle error state, post the error message to _viewState
                _viewState.postValue(LoadingErrorState.ShowError(dataState.error))
            }

            DataState.Idle -> {
                // Hide loading state when idle (no data loading in progress)
                _viewState.postValue(LoadingErrorState.HideLoading)
            }

            DataState.Processing -> {
                // Show loading state when data is being processed
                _viewState.postValue(LoadingErrorState.ShowLoading)
            }

            DataState.ServerError -> {
                // Handle server error state if necessary (show a network error)
                _viewState.postValue(LoadingErrorState.ShowNetworkError)
            }
        }
    }
}