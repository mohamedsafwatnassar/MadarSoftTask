package com.madarsoft.android_task.domain.usecase

import com.madarsoft.android_task.domain.repository.LocalPersonalDataRepository
import javax.inject.Inject

class FetchPersonalDatUseCase @Inject constructor(
    private val localRepository: LocalPersonalDataRepository,
) {

    suspend fun fetchPersonalDataByIdFromLocal(personalDataId: Int)  {
        localRepository.fetchPersonalDataByIdFromLocal(personalDataId)
    }
}