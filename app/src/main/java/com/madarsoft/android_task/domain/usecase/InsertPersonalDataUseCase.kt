package com.madarsoft.android_task.domain.usecase

import com.madarsoft.android_task.domain.model.PersonalData
import com.madarsoft.android_task.domain.repository.LocalPersonalDataRepository
import com.madarsoft.android_task.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertPersonalDataUseCase @Inject constructor(
    private val localRepository: LocalPersonalDataRepository,
) {

    suspend fun addPersonalDataToDatabase(
        username: String,
        age: String,
        jobTitle: String,
        gender: String,
    ): Flow<DataState<PersonalData>> {
        val personalData = PersonalData(
            username = username,
            age = age,
            jobTitle = jobTitle,
            gender = gender
        )
        return localRepository.addPersonalDataToDatabase(personalData)
    }
}
