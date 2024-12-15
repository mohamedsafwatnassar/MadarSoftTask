package com.madarsoft.android_task.domain.usecase

import com.madarsoft.android_task.data.local.entity.PersonalDataEntity
import com.madarsoft.android_task.domain.repository.LocalPersonalDataRepository
import javax.inject.Inject

class InsertPersonalDataUseCase @Inject constructor(
    private val localRepository: LocalPersonalDataRepository,
) {

    suspend fun addPersonalDataToDatabase(personalData: PersonalDataEntity) {
        localRepository.addPersonalDataToDatabase(personalData)
    }
}
