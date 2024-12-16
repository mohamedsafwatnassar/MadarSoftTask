package com.madarsoft.android_task.domain.usecase

import com.madarsoft.android_task.domain.model.PersonalData
import com.madarsoft.android_task.domain.repository.LocalPersonalDataRepository
import com.madarsoft.android_task.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchPersonalDatUseCase @Inject constructor(
    private val localRepository: LocalPersonalDataRepository,
) {

    suspend fun fetchPersonalDataByIdFromLocal(personalDataId: Int) : Flow<DataState<PersonalData>> {
        return localRepository.fetchPersonalDataByIdFromLocal(personalDataId)
    }
}